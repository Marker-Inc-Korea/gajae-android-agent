package com.gajae.androidagent.core;

public final class UiNode {
    public final String text;
    public final String description;
    public final String viewId;
    public final String className;
    public final RectBox bounds;
    public final boolean clickable;
    public final boolean scrollable;
    public final boolean selected;
    public final boolean enabled;

    public UiNode(CharSequence text, CharSequence description, CharSequence viewId, CharSequence className,
                  RectBox bounds, boolean clickable, boolean scrollable,
                  boolean selected, boolean enabled) {
        this.text = TextUtil.clean(text);
        this.description = TextUtil.clean(description);
        this.viewId = TextUtil.clean(viewId);
        this.className = TextUtil.clean(className);
        this.bounds = bounds == null ? new RectBox(0, 0, 0, 0) : bounds;
        this.clickable = clickable;
        this.scrollable = scrollable;
        this.selected = selected;
        this.enabled = enabled;
    }

    public String visibleText() {
        if (!text.isEmpty()) return text;
        return description;
    }

    public boolean hasText() {
        return !visibleText().isEmpty();
    }
}
