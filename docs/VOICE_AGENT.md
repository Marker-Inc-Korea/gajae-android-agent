# Voice Agent

## Technology choice
The app now uses Android's built-in `SpeechRecognizer` for speech-to-text and Android's built-in `TextToSpeech` for replies. This is the best default product choice on Android because it uses the user's installed system engines, supports interruption cleanly, avoids bundling very large native models, and works across devices without extra model downloads.

## Open-source fallback recommendation
If a device has no usable system speech stack, the recommended open-source fallback is Sherpa-ONNX:
- ASR: Sherpa-ONNX streaming models with VAD for interruption/barge-in.
- TTS: Android system TTS first; otherwise VoxSherpa/Sherpa-ONNX with Piper or Kokoro-class voices.

We did not bundle a model in the APK because even small multilingual offline ASR/TTS bundles can add tens to hundreds of MB. Keeping system TTS/STT first makes the APK immediately downloadable and usable; Sherpa-ONNX is the planned native fallback layer for devices without working system engines.

## Implemented voice behavior
- `말로 지시하기 / 끼어들기`: stops current TTS, cancels any current recognition, starts listening.
- Partial speech results update the instruction text box live.
- Final speech result runs the universal app agent.
- Agent replies are shortened through `SpokenResponseLimiter` before display and TTS.
- `말 끊기`: immediately cancels listening and stops TTS.

## Modules
- `SpeechInputController.java`: Android `SpeechRecognizer` wrapper.
- `SpeechOutputController.java`: Android `TextToSpeech` wrapper.
- `VoiceCallback.java`: recognition callback interface.
- `VoiceState.java`: conversation state enum.
- `SpokenResponseLimiter.java`: short spoken responses.

## Required permission
- `android.permission.RECORD_AUDIO`
