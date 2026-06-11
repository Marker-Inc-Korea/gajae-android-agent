package com.gajae.androidagent.core;

public final class TargetMatcher {
    public UiNode bestClickable(UiSnapshot snapshot, String targetText) {
        UiNode fallback = null;
        String target = TextUtil.lower(targetText);
        for (UiNode node : snapshot.nodes) {
            if (!node.clickable || !node.enabled) continue;
            if (fallback == null) fallback = node;
            String text = TextUtil.lower(node.visibleText());
            if (!target.isEmpty() && (text.contains(target) || target.contains(text))) return node;
        }
        return fallback;
    }
}
