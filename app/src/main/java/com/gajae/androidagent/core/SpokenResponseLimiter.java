package com.gajae.androidagent.core;

public final class SpokenResponseLimiter {
    public String shortAnswer(String text) {
        String clean = TextUtil.clean(text);
        if (clean.length() <= 220) return clean;
        int cut = clean.indexOf("\n\n");
        if (cut > 40 && cut < 220) return clean.substring(0, cut);
        int sentence = clean.indexOf(". ");
        if (sentence > 40 && sentence < 220) return clean.substring(0, sentence + 1);
        return clean.substring(0, 217) + "...";
    }
}
