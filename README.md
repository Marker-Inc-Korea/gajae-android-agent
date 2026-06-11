# Gajae Android Agent

Android Accessibility-first app agent for operating installed apps from natural-language instructions, with Gmail-specific email reading, generic app launching, observe-plan-act execution, API settings, and VLM/LLM HTTP integration seam.

## Install and use
1. Install the signed APK from `out/gajae-android-agent.apk`.
2. Open Android Settings and enable the `Gajae Android Agent` Accessibility Service.
3. Open the app.
4. Enter API endpoint, API key, and model name.
5. Type a natural-language command, for example:
   - `Gmail 앱에서 새로 온 이메일을 읽고 설명해줘`
   - `카카오톡에서 최근 메시지를 요약해줘`
   - `설정 앱에서 배터리 화면을 확인해줘`
6. Tap `자연어 지시 실행`.

## What the APK does
- Resolves an installed app from the user’s app name.
- Launches the target app.
- Reads the active screen through Android Accessibility.
- Builds a compact text snapshot for LLM-style reasoning.
- Plans small actions: read, click best matching text, scroll.
- Executes click/scroll through Accessibility gestures.
- Falls back to VLM need detection when accessibility text is sparse.
- Persists API configuration locally in Android `SharedPreferences`.

## Main modules
- `MainActivity`: API setup and natural instruction UI.
- `AgentAccessibilityService`: screen observation.
- `NodeTreeReader`: Android node tree to plain `UiSnapshot`.
- `AppResolver`: installed app name to package resolver.
- `UniversalAppAgent`: general app-agent orchestrator.
- `ClosedLoopExecutor`: executes planned click/scroll actions.
- `GmailScenario`: Gmail-specific unread-email scenario.
- `HttpVlmClient`: generic HTTP VLM/LLM client seam.

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
7 tests passed
```

## Product docs
- `docs/PLAN_PRD_SCENARIOS.md`
- `docs/UNIVERSAL_AGENT.md`
- `docs/PRODUCT_STATUS.md`

## Honest product note
This is an installable agent app, not just a toy Gmail script. It can launch arbitrary installed apps, observe screens, summarize text, and perform basic click/scroll actions. Full “perfectly use every Android app” behavior still requires real-device scenario qualification because third-party apps vary in accessibility metadata, custom canvases, security flows, and VLM provider schemas.
