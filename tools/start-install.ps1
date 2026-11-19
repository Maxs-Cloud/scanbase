$adb = 'C:\platform-tools\adb.exe'
$apk = 'C:\Users\marhr\work\scanbase.apk'
$out = 'C:\Users\marhr\work\inst.out'
Remove-Item $out -ErrorAction SilentlyContinue
$p = Start-Process -FilePath $adb -ArgumentList '-s','QLV0220504008011','install','-r','-t',$apk -PassThru -NoNewWindow -RedirectStandardOutput $out
Write-Output ("PID=" + $p.Id)
