package com.gajae.androidagent.core;

public final class PlanStep {
    public final String intent;
    public final String targetText;

    public PlanStep(String intent, String targetText) {
        this.intent = TextUtil.clean(intent);
        this.targetText = TextUtil.clean(targetText);
    }

    public String line() {
        return targetText.isEmpty() ? intent : intent + " -> " + targetText;
    }
}
