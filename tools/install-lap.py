#!/usr/bin/env python3
"""Ставит APK на телефон через adb Windows-ноута: сам жмёт диалоги Huawei."""
import re, subprocess, sys, time, pathlib

PORT = "2027"
KH = str(pathlib.Path.home() / ".config/protei/winlap.known_hosts")
ADB = r"C:\platform-tools\adb.exe"
SER = "-s QLV0220504008011"


def lap(cmd, timeout=180):
    r = subprocess.run(["ssh", "-p", PORT, "-o", "BatchMode=yes", "-o", "ConnectTimeout=15",
                        "-o", "StrictHostKeyChecking=yes", "-o", f"UserKnownHostsFile={KH}",
                        "-o", "ClearAllForwardings=yes", "marhr@127.0.0.1", cmd],
                       capture_output=True, text=True, timeout=timeout)
    noise = ("post-quantum", "store now", "upgraded", "WARNING")
    keep = [l for l in (r.stdout + r.stderr).replace("\r", "").splitlines()
            if l.strip() and not any(n in l for n in noise)]
    return "\n".join(keep)


def adb(args, timeout=180):
    return lap(f"{ADB} {SER} {args}", timeout)


def dump():
    adb("shell uiautomator dump /sdcard/i.xml", 90)
    return adb("shell cat /sdcard/i.xml", 90)


def tap_text(*words):
    x = dump()
    for node in re.findall(r"<node[^>]*>", x):
        t = re.search(r'text="(.*?)"', node)
        b = re.search(r'bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"', node)
        if not (t and b):
            continue
        label = t.group(1).strip().upper()
        if any(w.upper() in label for w in words):
            x1, y1, x2, y2 = map(int, b.groups())
            cx, cy = (x1 + x2) // 2, (y1 + y2) // 2
            print(f"тап «{t.group(1)}» @ {cx},{cy}", flush=True)
            adb(f"shell input tap {cx} {cy}", 60)
            return True
    return False


def main(apk_local):
    print("копирую APK на ноут…", flush=True)
    subprocess.run(["scp", "-P", PORT, "-o", "BatchMode=yes", "-o", "StrictHostKeyChecking=yes",
                    "-o", f"UserKnownHostsFile={KH}", "-q", apk_local,
                    "marhr@127.0.0.1:C:/Users/marhr/work/scanbase.apk"], check=True, timeout=600)
    print("кладу в /data/local/tmp…", flush=True)
    print(adb("push C:\\Users\\marhr\\work\\scanbase.apk /data/local/tmp/scanbase.apk").strip()[-120:])
    for pkg in ("com.android.packageinstaller", "com.huawei.android.pkginstaller", "com.huawei.appmarket"):
        adb(f"shell am force-stop {pkg}", 60)
    print("ставлю…", flush=True)
    out = adb("shell pm install -r -t /data/local/tmp/scanbase.apk")
    for _ in range(12):
        time.sleep(2)
        if "Success" in out:
            break
        if tap_text("РАЗРЕШИТЬ", "ПРОДОЛЖИТЬ", "УСТАНОВИТЬ"):
            out = adb("shell pm install -r -t /data/local/tmp/scanbase.apk")
    print("\nИТОГ:", out.strip()[-200:] or "нет ответа")
    print("проверка:", adb("shell dumpsys package ru.scanbase", 90).splitlines()[-1][:0] or
          [l.strip() for l in adb("shell dumpsys package ru.scanbase", 90).splitlines()
           if "lastUpdateTime" in l or "versionName" in l])
    return 0 if "Success" in out else 1


if __name__ == "__main__":
    sys.exit(main(sys.argv[1]))
