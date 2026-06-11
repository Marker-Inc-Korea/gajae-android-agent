package com.gajae.androidagent.core;

public interface AppActionPerformer {
    boolean click(RectBox bounds);
    boolean scrollForward();
    boolean back();
    boolean typeText(String text);
}
