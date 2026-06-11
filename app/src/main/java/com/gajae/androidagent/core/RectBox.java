package com.gajae.androidagent.core;

public final class RectBox {
    public final int left;
    public final int top;
    public final int right;
    public final int bottom;

    public RectBox(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int width() {
        return Math.max(0, right - left);
    }

    public int height() {
        return Math.max(0, bottom - top);
    }

    public String compact() {
        return left + "," + top + "," + right + "," + bottom;
    }
}
