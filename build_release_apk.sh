#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SDK="${ANDROID_HOME:-/home/cheol/Android/Sdk}"
BT="$SDK/build-tools/35.0.0"
APK="$ROOT/out/android-agent-unsigned.apk"
KEYSTORE="$ROOT/out/gajae-agent-debug.keystore"
SIGNED="$ROOT/out/gajae-android-agent.apk"
bash "$ROOT/build_apk.sh" >/dev/null
if [ ! -f "$KEYSTORE" ]; then
  keytool -genkeypair -v -keystore "$KEYSTORE" -storepass android -keypass android -alias gajae-agent -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Gajae Android Agent, OU=Marker, O=Marker Inc Korea, L=Seoul, S=Seoul, C=KR" >/dev/null
fi
"$BT/apksigner" sign --ks "$KEYSTORE" --ks-pass pass:android --key-pass pass:android --out "$SIGNED" "$APK"
"$BT/apksigner" verify "$SIGNED"
echo "$SIGNED"
