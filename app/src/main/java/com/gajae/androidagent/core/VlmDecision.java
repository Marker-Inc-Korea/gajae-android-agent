package com.gajae.androidagent.core;

public final class VlmDecision {
    public final boolean available;
    public final String summary;

    public VlmDecision(boolean available, String summary) {
        this.available = available;
        this.summary = TextUtil.clean(summary);
    }
}
