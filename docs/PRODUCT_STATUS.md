# Product Status

This repository now contains an installable Android APK prototype with production-facing surfaces: API configuration, Accessibility service, universal app launching, observe-plan-act loop, Gmail-specific parser, generic screen summarizer, and VLM HTTP client seam.

It is not truthful to claim that any Android agent can perfectly use every third-party app without real-device qualification. Android apps differ in accessibility metadata, custom canvases, WebViews, anti-abuse flows, and account/security prompts. This build is designed so a user can install it, enter API settings, enable Accessibility, and run real app tasks; continued app-by-app evaluation is still required for product-grade guarantees.

## Ready-to-use pieces
- Signed debug APK build script: `build_release_apk.sh`.
- API settings persisted in `SharedPreferences` through `ConfigStore`.
- Universal instruction entry point through `UniversalAppAgent`.
- Installed-app resolution through `AppResolver`.
- Accessibility screen reading through `AgentAccessibilityService` and `NodeTreeReader`.
- Closed-loop click/scroll executor through `ClosedLoopExecutor` and `AndroidActionPerformer`.
- VLM HTTP seam through `HttpVlmClient`.

## Known hard limits
- Real VLM providers use different request/response schemas; `HttpVlmClient` is a minimal generic POST client, not a provider-specific SDK.
- Full arbitrary task completion needs live phone scenario testing and provider-specific action policy tuning.
- Secure release distribution should replace the included debug signing key with Play/App signing or private release signing.
