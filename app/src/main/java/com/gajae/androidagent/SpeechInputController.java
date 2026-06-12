package com.gajae.androidagent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;
import java.util.Locale;

public final class SpeechInputController implements RecognitionListener {
    private final SpeechRecognizer recognizer;
    private final VoiceCallback callback;

    public SpeechInputController(Context context, VoiceCallback callback) {
        this.recognizer = SpeechRecognizer.createSpeechRecognizer(context.getApplicationContext());
        this.callback = callback;
        recognizer.setRecognitionListener(this);
    }

    public void start() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toLanguageTag());
        recognizer.startListening(intent);
    }

    public void stop() { recognizer.stopListening(); }
    public void cancel() { recognizer.cancel(); }
    public void destroy() { recognizer.destroy(); }

    @Override public void onReadyForSpeech(Bundle params) {}
    @Override public void onBeginningOfSpeech() {}
    @Override public void onRmsChanged(float rmsdB) {}
    @Override public void onBufferReceived(byte[] buffer) {}
    @Override public void onEndOfSpeech() {}
    @Override public void onError(int error) { callback.onVoiceError("음성 인식 오류 " + error); }
    @Override public void onEvent(int eventType, Bundle params) {}

    @Override public void onPartialResults(Bundle results) {
        String text = first(results);
        if (!text.isEmpty()) callback.onPartialText(text);
    }

    @Override public void onResults(Bundle results) {
        String text = first(results);
        if (!text.isEmpty()) callback.onFinalText(text);
    }

    private String first(Bundle bundle) {
        ArrayList<String> list = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        return list == null || list.isEmpty() ? "" : list.get(0);
    }
}
