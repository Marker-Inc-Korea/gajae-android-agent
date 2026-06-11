# Universal App Agent Generalization

## Answer
The app is now generalized beyond Gmail, but it is still a prototype. It can accept a natural-language instruction, resolve an installed app by label/package heuristic, launch that app, read the current screen through Accessibility, produce a text summary, and create a simple action plan. It does not yet complete arbitrary multi-step tasks with robust closed-loop clicking/typing across every app.

## Universal flow
1. User types a Korean or English-ish instruction such as `카카오톡에서 최근 메시지를 요약해줘`.
2. `NaturalInstructionParser` extracts an app name and goal.
3. `AppResolver` scans launchable installed apps through `PackageManager` and resolves the best package.
4. `GmailLauncher` launches that package.
5. `AgentAccessibilityService` captures the active window accessibility tree.
6. `UniversalAppAgent` asks `GenericTaskPlanner` for a small plan.
7. `GenericScreenSummarizer` reports the visible screen text.
8. `FallbackDecider` marks cases where VLM is needed because accessibility text is too sparse.

## What works for any app now
- Open an installed app by approximate display name.
- Read and summarize visible accessibility text.
- Detect when VLM fallback is necessary.
- Produce a compact plan such as read screen, click best text, or scroll.

## What is not fully solved yet
- Robust arbitrary clicking: target grounding across all app layouts needs a `TargetMatcher` and gesture execution loop.
- Typing into arbitrary fields: needs editable-node detection and IME-safe text input.
- Multi-step closed-loop autonomy: needs observe-plan-act-repeat with safety limits.
- Real VLM: current VLM implementation is still a no-op seam.

## Added small modules
- `AppCommand.java`
- `InstructionParseResult.java`
- `NaturalInstructionParser.java`
- `PlanStep.java`
- `GenericTaskPlanner.java`
- `GenericScreenSummarizer.java`
- `UniversalAppAgent.java`
- Android adapter: `AppResolver.java`

Every new core module follows the weak-LLM small-module rule.
