package com.gajae.androidagent.core;

public final class VlmRequest {
    public final String prompt;
    public final int width;
    public final int height;

    public VlmRequest(String prompt, int width, int height) {
        this.prompt = TextUtil.clean(prompt);
        this.width = width;
        this.height = height;
    }
}
