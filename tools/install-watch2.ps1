$adb = 'C:\platform-tools\adb.exe'
$ser = @('-s','QLV0220504008011')
$out = 'C:\Users\marhr\work\inst.out'
foreach ($pkg in @('com.android.packageinstaller','com.huawei.android.pkginstaller','com.huawei.appmarket')) {
  & $adb ($ser + @('shell','am','force-stop',$pkg)) | Out-Null
}
Remove-Item $out -ErrorAction SilentlyContinue
Start-Process -FilePath $adb -ArgumentList ($ser + @('install','-r','-t','C:\Users\marhr\work\scanbase.apk')) -NoNewWindow -RedirectStandardOutput $out
for ($i = 0; $i -lt 8; $i++) {
  Start-Sleep -Seconds 2
  & $adb ($ser + @('shell','uiautomator','dump','/sdcard/i.xml')) | Out-Null
  $xml = (& $adb ($ser + @('shell','cat','/sdcard/i.xml'))) -join ''
  foreach ($m in [regex]::Matches($xml, '<node[^>]*>')) {
    $t = [regex]::Match($m.Value, 'text="(.*?)"').Groups[1].Value
    $b = [regex]::Match($m.Value, 'bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"')
    if ($t -ne '' -and $b.Success) {
      Write-Output ("NODE $i [" + $b.Groups[1].Value + "," + $b.Groups[2].Value + "-" + $b.Groups[3].Value + "," + $b.Groups[4].Value + "] " + $t)
    }
  }
  $r = (Get-Content $out -Raw)
  if ($r -match 'Success|Failure') { Write-Output ("RESULT " + $r); break }
}
