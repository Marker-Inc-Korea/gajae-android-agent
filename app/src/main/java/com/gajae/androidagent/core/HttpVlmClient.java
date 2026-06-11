package com.gajae.androidagent.core;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class HttpVlmClient implements VlmClient {
    private final ApiConfig config;

    public HttpVlmClient(ApiConfig config) {
        this.config = config;
    }

    @Override
    public VlmDecision analyze(VlmRequest request, byte[] pngBytes) {
        if (!config.isConfigured()) return new VlmDecision(false, "API가 설정되지 않았습니다.");
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(config.endpoint).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + config.apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            String body = "{\"model\":\"" + escape(config.model) + "\",\"prompt\":\"" + escape(request.prompt) + "\"}";
            OutputStream out = conn.getOutputStream();
            out.write(body.getBytes("UTF-8"));
            out.close();
            int code = conn.getResponseCode();
            return new VlmDecision(code >= 200 && code < 300, "VLM 요청 완료 HTTP " + code);
        } catch (Exception e) {
            return new VlmDecision(false, "VLM 요청 실패: " + e.getClass().getSimpleName());
        }
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
