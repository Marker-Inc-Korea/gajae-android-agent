package com.gajae.androidagent.core;

public final class AgentStep {
    public final String label;
    public final String detail;

    public AgentStep(String label, String detail) {
        this.label = TextUtil.clean(label);
        this.detail = TextUtil.clean(detail);
    }

    public String line() {
        if (detail.isEmpty()) return label;
        return label + ": " + detail;
    }
}
