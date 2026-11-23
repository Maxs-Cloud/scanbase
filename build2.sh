#!/bin/bash
# Сборка ScanBase: aapt2 -> javac -> d8 -> упаковка -> zipalign -> apksigner.
set -euo pipefail
P="$(cd "$(dirname "$0")" && pwd)"
SDK="$HOME/android-sdk"
BT="$SDK/build-tools/34.0.0"
PLATFORM="$SDK/platforms/android-34/android.jar"
STAGE="$P/build"
rm -rf "$STAGE" && mkdir -p "$STAGE/obj" "$STAGE/classes" "$STAGE/apk/assets" "$STAGE/apk/lib/arm64-v8a"

"$BT/aapt2" compile --dir "$P/res" -o "$STAGE/res.zip"
"$BT/aapt2" link -o "$STAGE/app.apk" -I "$PLATFORM" --manifest "$P/AndroidManifest.xml" "$STAGE/res.zip" \
    --auto-add-overlay
javac -nowarn -encoding UTF-8 -Xlint:-options -source 11 -target 11 -classpath "$PLATFORM:$P/lib/zxing-core-3.5.3.jar" -d "$STAGE/obj" $(find "$P/src" -name '*.java')
"$BT/d8" --lib "$PLATFORM" --lib "$P/lib/zxing-core-3.5.3.jar" --min-api 24 --output "$STAGE/classes" $(find "$STAGE/obj" -name '*.class')
cp "$STAGE/classes/classes.dex" "$STAGE/apk/classes.dex"
cp "$P/assets/"* "$STAGE/apk/assets/"
cp "$P/lib/arm64-v8a/"* "$STAGE/apk/lib/arm64-v8a/"
cp "$STAGE/app.apk" "$STAGE/unaligned.apk"
cd "$STAGE/apk" && zip -q -X ../unaligned.apk classes.dex && zip -q -r -X ../unaligned.apk assets && zip -q -0 -r -X ../unaligned.apk lib
cd "$P"
"$BT/zipalign" -f -p 4 "$STAGE/unaligned.apk" "$STAGE/aligned.apk"
"$BT/apksigner" sign --ks "$HOME/.android/debug.keystore" --ks-pass pass:android \
    --out "$P/scanbase.apk" "$STAGE/aligned.apk"
ls -l "$P/scanbase.apk"
"$BT/zipalign" -c -p -v 4 "$P/scanbase.apk" >/dev/null || { echo "ОШИБКА: APK не выровнен по странице — установка упадёт с res=-2"; exit 1; }
