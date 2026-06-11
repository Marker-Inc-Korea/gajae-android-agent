package com.gajae.androidagent.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class UiSnapshot {
    public final String packageName;
    public final List<UiNode> nodes;

    public UiSnapshot(String packageName, List<UiNode> nodes) {
        this.packageName = TextUtil.clean(packageName);
        this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    public static UiSnapshot empty() {
        return new UiSnapshot("", Collections.<UiNode>emptyList());
    }

    public int textNodeCount() {
        int count = 0;
        for (UiNode node : nodes) if (node.hasText()) count++;
        return count;
    }

    public String allText() {
        StringBuilder out = new StringBuilder();
        for (UiNode node : nodes) {
            if (node.hasText()) out.append(node.visibleText()).append('\n');
        }
        return out.toString().trim();
    }
}
