package com.gajae.androidagent;

public interface VoiceCallback {
    void onPartialText(String text);
    void onFinalText(String text);
    void onVoiceError(String message);
}
