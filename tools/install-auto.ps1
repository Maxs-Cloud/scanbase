# ASCII-only: строки кнопок собираем из кодов, чтобы PS 5.1 не сломал кириллицу.
function Word([int[]]$codes) { -join ($codes | ForEach-Object { [char]$_ }) }
$INSTALL   = Word @(0x0423,0x0421,0x0422,0x0410,0x041D,0x041E,0x0412,0x0418,0x0422,0x042C)   # USTANOVIT'
$CONTINUE  = Word @(0x041F,0x0420,0x041E,0x0414,0x041E,0x041B,0x0416,0x0418,0x0422,0x042C)   # PRODOLZIT'
$ALLOW     = Word @(0x0420,0x0410,0x0417,0x0420,0x0415,0x0428,0x0418,0x0422,0x042C)          # RAZRESHIT'
$targets = @($INSTALL, $CONTINUE, $ALLOW)
$adb = 'C:\platform-tools\adb.exe'
$ser = @('-s','QLV0220504008011')
$out = 'C:\Users\marhr\work\inst.out'
foreach ($pkg in @('com.android.packageinstaller','com.huawei.android.pkginstaller','com.huawei.appmarket')) {
  & $adb ($ser + @('shell','am','force-stop',$pkg)) | Out-Null
}
Remove-Item $out -ErrorAction SilentlyContinue
Start-Process -FilePath $adb -ArgumentList ($ser + @('install','-r','-t','C:\Users\marhr\work\scanbase.apk')) -NoNewWindow -RedirectStandardOutput $out
for ($i = 0; $i -lt 14; $i++) {
  Start-Sleep -Seconds 2
  & $adb ($ser + @('shell','uiautomator','dump','/sdcard/i.xml')) | Out-Null
  $xml = (& $adb ($ser + @('shell','cat','/sdcard/i.xml'))) -join ''
  foreach ($m in [regex]::Matches($xml, '<node[^>]*>')) {
    $t = [regex]::Match($m.Value, 'text="(.*?)"').Groups[1].Value.Trim()
    if ($targets -notcontains $t) { continue }
    $b = [regex]::Match($m.Value, 'bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"')
    if (-not $b.Success) { continue }
    $cx = ([int]$b.Groups[1].Value + [int]$b.Groups[3].Value) / 2
    $cy = ([int]$b.Groups[2].Value + [int]$b.Groups[4].Value) / 2
    Write-Output ("TAP $i " + [int]$cx + " " + [int]$cy)
    & $adb ($ser + @('shell','input','tap',[string][int]$cx,[string][int]$cy)) | Out-Null
    Start-Sleep -Seconds 2
    break
  }
  $r = (Get-Content $out -Raw)
  if ($r -match 'Success|Failure') { Write-Output ("RESULT " + $r.Trim()); break }
}
