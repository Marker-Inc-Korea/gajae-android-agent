package com.gajae.androidagent;

import com.gajae.androidagent.core.AppActionPerformer;
import com.gajae.androidagent.core.RectBox;

public final class AndroidActionPerformer implements AppActionPerformer {
    @Override
    public boolean click(RectBox bounds) {
        AgentAccessibilityService service = AgentAccessibilityService.instance();
        return service != null && new GestureRunner(service).click(bounds);
    }

    @Override
    public boolean scrollForward() {
        AgentAccessibilityService service = AgentAccessibilityService.instance();
        return service != null && new GestureRunner(service).scrollForward();
    }

    @Override
    public boolean back() {
        AgentAccessibilityService service = AgentAccessibilityService.instance();
        return service != null && new GestureRunner(service).back();
    }

    @Override
    public boolean typeText(String text) {
        return false;
    }
}
