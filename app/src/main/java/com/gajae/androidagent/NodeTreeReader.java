package com.gajae.androidagent;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import com.gajae.androidagent.core.RectBox;
import com.gajae.androidagent.core.UiNode;
import com.gajae.androidagent.core.UiSnapshot;

import java.util.ArrayList;
import java.util.List;

public final class NodeTreeReader {
    private static final int MAX_NODES = 600;

    public UiSnapshot read(String packageName, AccessibilityNodeInfo root) {
        List<UiNode> nodes = new ArrayList<>();
        visit(root, nodes);
        return new UiSnapshot(packageName, nodes);
    }

    private void visit(AccessibilityNodeInfo node, List<UiNode> out) {
        if (node == null || out.size() >= MAX_NODES) return;
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        CharSequence viewId = node.getViewIdResourceName();
        CharSequence clazz = node.getClassName();
        out.add(new UiNode(node.getText(), node.getContentDescription(), viewId, clazz,
                new RectBox(rect.left, rect.top, rect.right, rect.bottom),
                node.isClickable(), node.isScrollable(), node.isSelected(), node.isEnabled()));
        for (int i = 0; i < node.getChildCount(); i++) visit(node.getChild(i), out);
    }
}
