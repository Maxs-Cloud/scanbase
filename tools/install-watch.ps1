$adb = 'C:\platform-tools\adb.exe'
$ser = @('-s','QLV0220504008011')
$apk = 'C:\Users\marhr\work\scanbase.apk'
$out = 'C:\Users\marhr\work\inst.out'
Remove-Item $out -ErrorAction SilentlyContinue
$p = Start-Process -FilePath $adb -ArgumentList ($ser + @('install','-r','-t',$apk)) -PassThru -NoNewWindow -RedirectStandardOutput $out
for ($i = 0; $i -lt 6; $i++) {
  Start-Sleep -Seconds 2
  & $adb ($ser + @('shell','uiautomator','dump','/sdcard/i.xml')) | Out-Null
  $xml = (& $adb ($ser + @('shell','cat','/sdcard/i.xml'))) -join ''
  foreach ($m in [regex]::Matches($xml, '<node[^>]*>')) {
    $n = $m.Value
    $t = [regex]::Match($n, 'text="(.*?)"').Groups[1].Value
    $b = [regex]::Match($n, 'bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"')
    if ($t -ne '' -and $b.Success) {
      Write-Output ("NODE " + $i + " [" + $b.Groups[1].Value + "," + $b.Groups[2].Value + "-" + $b.Groups[3].Value + "," + $b.Groups[4].Value + "] " + $t)
    }
  }
}
Write-Output ("RESULT " + (Get-Content $out -Raw))
