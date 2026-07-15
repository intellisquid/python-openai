#!/usr/bin/env bash
# Builds the debug APK and installs + launches it on a connected device or running emulator.
set -euo pipefail

cd "$(dirname "$0")/.."

if ! command -v adb >/dev/null 2>&1; then
    echo "adb not found on PATH. Install Android SDK platform-tools or open this project in Android Studio once first." >&2
    exit 1
fi

if ! adb devices | grep -qE "device$|emulator.*device$"; then
    echo "No connected device or running emulator found. Start an emulator (Android Studio > Device Manager) or plug in a device with USB debugging enabled." >&2
    exit 1
fi

echo "Building debug APK..."
./gradlew assembleDebug

APK="app/build/outputs/apk/debug/app-debug.apk"
APP_ID="com.intellisquid.dashboard.todo"

echo "Installing on device..."
adb install -r "$APK"

echo "Launching..."
adb shell am start -n "$APP_ID/$APP_ID.MainActivity"
