package com.gajae.androidagent;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public final class SpeechOutputController implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private boolean ready;

    public SpeechOutputController(Context context) {
        tts = new TextToSpeech(context.getApplicationContext(), this);
    }

    @Override public void onInit(int status) {
        ready = status == TextToSpeech.SUCCESS;
        if (ready) {
            tts.setLanguage(Locale.getDefault());
            tts.setSpeechRate(1.05f);
        }
    }

    public void speak(String text) {
        if (!ready || text == null || text.isEmpty()) return;
        tts.stop();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "agent_reply");
    }

    public void stop() {
        if (tts != null) tts.stop();
    }

    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
