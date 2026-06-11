package com.gajae.androidagent;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public final class AgentAccessibilityService extends AccessibilityService {
    private final NodeTreeReader reader = new NodeTreeReader();
    private static AgentAccessibilityService current;

    public static AgentAccessibilityService instance() {
        return current;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        String packageName = event == null || event.getPackageName() == null ? "" : event.getPackageName().toString();
        SnapshotStore.set(reader.read(packageName, root));
        if (root != null) root.recycle();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        current = this;
    }

    @Override
    public void onDestroy() {
        if (current == this) current = null;
        super.onDestroy();
    }
    protected void onServiceConnected() {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        SnapshotStore.set(reader.read("", root));
        if (root != null) root.recycle();
    }

    @Override
    public void onInterrupt() {
    }
}
