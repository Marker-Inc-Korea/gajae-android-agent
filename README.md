# Gajae Android Agent

Android Accessibility-first app agent for operating installed apps from short voice or text instructions, with Gmail-specific email reading, generic app launching, observe-plan-act execution, API settings, TTS replies, interruption controls, and VLM/LLM HTTP integration seam.

## Install and use
1. Install the signed APK from `out/gajae-android-agent.apk`.
2. Open Android Settings and enable the `Gajae Android Agent` Accessibility Service.
3. Open the app.
4. For Google Gemini API, enter API endpoint `google`, your Google API key, and model such as `gemini-1.5-flash`. For other HTTP APIs, enter the full endpoint URL, key, and model.
5. Type a command or tap `말로 지시하기 / 끼어들기` and speak.
6. Tap `자연어 지시 실행`, or let final speech recognition run it.
7. Tap `말 끊기` to interrupt listening or TTS immediately.

## What the APK does
- Resolves an installed app from the user’s app name.
- Launches the target app.
- Reads the active screen through Android Accessibility.
- Builds a compact text snapshot for LLM-style reasoning.
- Plans small actions: read, click best matching text, scroll.
- Executes click/scroll through Accessibility gestures.
- Falls back to VLM need detection when accessibility text is sparse.
- Persists API configuration locally in Android `SharedPreferences`.
- Accepts voice commands through Android system speech recognition.
- Speaks short replies through Android system TextToSpeech.
- Supports barge-in by stopping TTS when a new voice turn starts.

## Main modules
- `MainActivity`: API setup and natural instruction UI.
- `AgentAccessibilityService`: screen observation.
- `NodeTreeReader`: Android node tree to plain `UiSnapshot`.
- `AppResolver`: installed app name to package resolver.
- `UniversalAppAgent`: general app-agent orchestrator.
- `ClosedLoopExecutor`: executes planned click/scroll actions.
- `GmailScenario`: Gmail-specific unread-email scenario.
- `HttpVlmClient`: generic HTTP VLM/LLM client seam.
- `SpeechInputController`: Android speech recognition wrapper.
- `SpeechOutputController`: Android TTS wrapper.
- `SpokenResponseLimiter`: keeps spoken replies short.

## Build
Unsigned APK:

```bash
cd /tmp/android-agent
bash build_apk.sh
```

Signed debug APK:

```bash
cd /tmp/android-agent
bash build_release_apk.sh
```

Output:

```text
/tmp/android-agent/out/gajae-android-agent.apk
```

## Tests
```bash
cd /tmp/android-agent
python3 tests/run_tests.py
```

Verified result:

```text
9 tests passed
```

## Product docs
- `docs/PLAN_PRD_SCENARIOS.md`
- `docs/UNIVERSAL_AGENT.md`
- `docs/PRODUCT_STATUS.md`
- `docs/VOICE_AGENT.md`

## Honest product note
This is an installable agent app, not just a toy Gmail script. It can launch arbitrary installed apps, observe screens, summarize text, and perform basic click/scroll actions. Full “perfectly use every Android app” behavior still requires real-device scenario qualification because third-party apps vary in accessibility metadata, custom canvases, security flows, and VLM provider schemas.
