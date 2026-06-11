package com.gajae.androidagent;

import android.content.Context;
import android.content.SharedPreferences;

import com.gajae.androidagent.core.ApiConfig;

public final class ConfigStore {
    private final SharedPreferences prefs;

    public ConfigStore(Context context) {
        prefs = context.getSharedPreferences("agent_config", Context.MODE_PRIVATE);
    }

    public ApiConfig load() {
        return new ApiConfig(prefs.getString("endpoint", ""), prefs.getString("apiKey", ""), prefs.getString("model", ""));
    }

    public void save(String endpoint, String apiKey, String model) {
        prefs.edit().putString("endpoint", endpoint).putString("apiKey", apiKey).putString("model", model).apply();
    }
}
