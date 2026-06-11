package com.gajae.androidagent;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;

import com.gajae.androidagent.core.RectBox;

public final class GestureRunner {
    private final AccessibilityService service;

    public GestureRunner(AccessibilityService service) {
        this.service = service;
    }

    public boolean back() {
        return service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public boolean click(RectBox box) {
        if (box == null) return false;
        Path path = new Path();
        path.moveTo((box.left + box.right) / 2f, (box.top + box.bottom) / 2f);
        GestureDescription gesture = new GestureDescription.Builder()
                .addStroke(new GestureDescription.StrokeDescription(path, 0, 80))
                .build();
        return service.dispatchGesture(gesture, null, null);
    }

    public boolean scrollForward() {
        Path path = new Path();
        path.moveTo(500f, 1400f);
        path.lineTo(500f, 500f);
        GestureDescription gesture = new GestureDescription.Builder()
                .addStroke(new GestureDescription.StrokeDescription(path, 0, 300))
                .build();
        return service.dispatchGesture(gesture, null, null);
    }
}
