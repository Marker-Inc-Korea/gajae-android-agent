package com.gajae.androidagent.core;

public final class ApiConfig {
    public final String endpoint;
    public final String apiKey;
    public final String model;

    public ApiConfig(String endpoint, String apiKey, String model) {
        this.endpoint = TextUtil.clean(endpoint);
        this.apiKey = TextUtil.clean(apiKey);
        this.model = TextUtil.clean(model);
    }

    public boolean isConfigured() {
        return !endpoint.isEmpty() && !apiKey.isEmpty();
    }
}
