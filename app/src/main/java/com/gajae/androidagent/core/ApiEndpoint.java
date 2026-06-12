package com.gajae.androidagent.core;

public final class ApiEndpoint {
    public final String url;
    public final boolean google;

    public ApiEndpoint(String url, boolean google) {
        this.url = TextUtil.clean(url);
        this.google = google;
    }
}
