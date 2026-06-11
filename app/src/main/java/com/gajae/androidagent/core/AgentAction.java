package com.gajae.androidagent.core;

public final class AgentAction {
    public final ActionType type;
    public final String target;
    public final RectBox bounds;

    public AgentAction(ActionType type, String target, RectBox bounds) {
        this.type = type;
        this.target = TextUtil.clean(target);
        this.bounds = bounds;
    }

    public static AgentAction launch(String packageName) {
        return new AgentAction(ActionType.LAUNCH_APP, packageName, null);
    }
}
