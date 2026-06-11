package com.gajae.androidagent.core;

public final class AgentResult {
    public final boolean ok;
    public final String message;

    public AgentResult(boolean ok, String message) {
        this.ok = ok;
        this.message = TextUtil.clean(message);
    }

    public static AgentResult ok(String message) {
        return new AgentResult(true, message);
    }

    public static AgentResult fail(String message) {
        return new AgentResult(false, message);
    }
}
