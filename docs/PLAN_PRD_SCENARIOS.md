# Android App Agent PRD, Scenarios, and Granular Module Plan

## Product goal
Build an Android agent app that lets a text LLM operate existing smartphone apps through Accessibility first, and falls back to VLM-style screen reasoning only when the accessibility tree is insufficient. The first delivered scenario is: open the installed Gmail app, inspect the inbox, identify newly visible unread emails, and explain them in Korean.

## Principle
1. Prefer Accessibility tree: text, content descriptions, view ids, bounds, clickability, scrollability.
2. Use deterministic parsers for common apps before asking an LLM.
3. Ask a text LLM with a compact UI snapshot when deterministic parsing cannot decide.
4. Use screenshot/VLM fallback only when text coverage is poor, duplicate, hidden, or contradictory.
5. Keep every implementation module small enough for weak LLMs: one responsibility, mostly below 120 lines, explicit data classes, no clever framework.

## User scenario: Gmail unread summary
1. User opens Gajae Android Agent.
2. User taps “Gmail 새 메일 요약 실행”.
3. App asks the user to enable Accessibility Service if disabled.
4. Agent launches Gmail package `com.google.android.gm`.
5. Accessibility service captures the current UI tree.
6. Agent waits until Gmail inbox-like text appears.
7. Agent extracts visible rows containing unread signals.
8. Agent summarizes sender, subject/snippet, and rough reason.
9. If the tree has too little text, agent requests screenshot analysis through the VLM fallback interface and reports that visual fallback was used.

## UX
- Single screen with status, run button, open accessibility settings button, and latest result.
- No hidden automation: status log shows each major step.
- Privacy-first copy: email text is processed locally by this prototype unless the configured LLM/VLM endpoint is wired.

## Module size rules
- Each module has a narrow purpose and small API.
- No module should own both Android framework IO and agent reasoning.
- Planning modules receive plain data only.
- Android modules translate Android objects into plain data.

## File/module plan
### Core tiny modules
- `RectBox.java`: immutable bounds object.
- `UiNode.java`: one UI node, plain Java.
- `UiSnapshot.java`: package name plus node list and text utilities.
- `TextUtil.java`: null-safe text normalization.
- `ActionType.java`: enum for app actions.
- `AgentAction.java`: action command value object.
- `AgentResult.java`: success/failure summary value object.
- `AgentStep.java`: step label and detail for logs.

### Gmail scenario modules
- `EmailItem.java`: parsed visible email row.
- `GmailSignals.java`: constants and Gmail package detection.
- `GmailInboxParser.java`: deterministic unread/row extraction.
- `GmailSummaryFormatter.java`: Korean summary formatting.
- `GmailScenario.java`: orchestration over snapshot provider and actions.

### LLM/VLM fallback modules
- `TextLlmClient.java`: interface for text LLM.
- `RuleBasedTextLlmClient.java`: deterministic local implementation for tests/prototype.
- `VlmClient.java`: interface for screenshot reasoning.
- `VlmRequest.java`: screenshot metadata and prompt.
- `VlmDecision.java`: fallback output.
- `NoopVlmClient.java`: safe default, no network.
- `FallbackDecider.java`: decides tree-only vs VLM fallback.

### Android adapter/service modules
- `NodeTreeReader.java`: converts AccessibilityNodeInfo to `UiSnapshot`.
- `GestureRunner.java`: click/back/scroll wrapper.
- `GmailLauncher.java`: launches Gmail or market settings.
- `SnapshotStore.java`: holds latest snapshot from service.
- `AgentAccessibilityService.java`: service lifecycle and snapshot capture only.
- `MainActivity.java`: simple GUI and scenario trigger.

### Test modules
- `tests/test_core.py`: static and behavior tests for deterministic Java core using small source inspections.
- `tests/sample_gmail_snapshot.txt`: scenario fixture.

## Acceptance criteria
- Android project compiles into a debug APK using installed Android SDK tools.
- Core Gmail parser passes unit tests on sample inbox snapshots.
- APK contains Accessibility service configured to retrieve UI tree, perform gestures, and request screenshots.
- Gmail scenario can be triggered from the UI and emits a Korean result string.
- VLM fallback path is explicit and replaceable with a real endpoint later.
