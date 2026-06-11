package com.gajae.androidagent.core;

public final class NoopVlmClient implements VlmClient {
    @Override
    public VlmDecision analyze(VlmRequest request, byte[] pngBytes) {
        return new VlmDecision(false, "VLM 엔드포인트가 아직 설정되지 않았습니다.");
    }
}
