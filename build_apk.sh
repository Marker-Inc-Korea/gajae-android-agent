#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SDK="${ANDROID_HOME:-/home/cheol/Android/Sdk}"
BT="$SDK/build-tools/35.0.0"
PLATFORM="$SDK/platforms/android-35/android.jar"
OUT="$ROOT/out"
rm -rf "$OUT"
mkdir -p "$OUT/classes" "$OUT/dex" "$OUT/apk"
"$BT/aapt2" compile --dir "$ROOT/app/src/main/res" -o "$OUT/res.zip"
"$BT/aapt2" link -I "$PLATFORM" --manifest "$ROOT/app/src/main/AndroidManifest.xml" "$OUT/res.zip" -o "$OUT/unsigned.apk" --java "$OUT/gen" --auto-add-overlay
javac -source 8 -target 8 -bootclasspath "$PLATFORM" -d "$OUT/classes" $(find "$ROOT/app/src/main/java" "$OUT/gen" -name '*.java')
"$BT/d8" --lib "$PLATFORM" --output "$OUT/dex" $(find "$OUT/classes" -name '*.class')
cp "$OUT/unsigned.apk" "$OUT/apk/base.apk"
(cd "$OUT/dex" && zip -q "$OUT/apk/base.apk" classes.dex)
"$BT/zipalign" -f 4 "$OUT/apk/base.apk" "$OUT/android-agent-unsigned.apk"
echo "$OUT/android-agent-unsigned.apk"
