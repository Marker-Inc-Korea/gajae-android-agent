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
            ApiEndpoint endpoint = endpointFor(config);
            HttpURLConnection conn = (HttpURLConnection) new URL(endpoint.url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            if (!endpoint.google) conn.setRequestProperty("Authorization", "Bearer " + config.apiKey);
            conn.setDoOutput(true);
            writeBody(conn, bodyFor(endpoint.google, request));
            int code = conn.getResponseCode();
            return new VlmDecision(code >= 200 && code < 300, "API 요청 완료 HTTP " + code);
        } catch (Exception e) {
            return new VlmDecision(false, "API 요청 실패: " + e.getClass().getSimpleName());
        }
    }

    public static ApiEndpoint endpointFor(ApiConfig config) {
        String endpoint = config.endpoint;
        boolean google = endpoint.contains("generativelanguage.googleapis.com") || endpoint.equalsIgnoreCase("google");
        if (!google) return new ApiEndpoint(endpoint, false);
        String model = config.model.isEmpty() ? "gemini-1.5-flash" : config.model;
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + config.apiKey;
        return new ApiEndpoint(url, true);
    }

    public static String bodyFor(boolean google, VlmRequest request) {
        String prompt = escape(request.prompt);
        if (google) return "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";
        return "{\"prompt\":\"" + prompt + "\"}";
    }

    private static void writeBody(HttpURLConnection conn, String body) throws Exception {
        OutputStream out = conn.getOutputStream();
        out.write(body.getBytes("UTF-8"));
        out.close();
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
