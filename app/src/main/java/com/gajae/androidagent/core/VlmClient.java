package com.gajae.androidagent.core;

public interface VlmClient {
    VlmDecision analyze(VlmRequest request, byte[] pngBytes);
}
