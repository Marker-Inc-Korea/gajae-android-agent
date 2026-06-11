package com.gajae.androidagent.core;

public final class FallbackDecider {
    public boolean needsVlm(UiSnapshot snapshot) {
        if (snapshot.nodes.isEmpty()) return true;
        if (snapshot.textNodeCount() < 4) return true;
        int textLength = snapshot.allText().length();
        return textLength < 24;
    }
}
