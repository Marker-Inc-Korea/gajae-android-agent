package com.gajae.androidagent.core;

public final class LoopReport {
    public final boolean acted;
    public final String summary;

    public LoopReport(boolean acted, String summary) {
        this.acted = acted;
        this.summary = TextUtil.clean(summary);
    }
}
