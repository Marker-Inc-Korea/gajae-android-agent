package com.gajae.androidagent.core;

public final class AppCommand {
    public final String appName;
    public final String goal;

    public AppCommand(String appName, String goal) {
        this.appName = TextUtil.clean(appName);
        this.goal = TextUtil.clean(goal);
    }

    public boolean hasApp() {
        return !appName.isEmpty();
    }
}
