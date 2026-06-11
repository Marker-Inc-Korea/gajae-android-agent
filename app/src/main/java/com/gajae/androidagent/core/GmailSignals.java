package com.gajae.androidagent.core;

public final class GmailSignals {
    public static final String PACKAGE = "com.google.android.gm";

    private GmailSignals() {}

    public static boolean isGmail(UiSnapshot snapshot) {
        return PACKAGE.equals(snapshot.packageName);
    }

    public static boolean looksInbox(UiSnapshot snapshot) {
        String text = snapshot.allText();
        return TextUtil.containsAny(text, "inbox", "primary", "받은편지", "기본", "메일 검색", "search in mail");
    }

    public static boolean isNoise(String text) {
        return TextUtil.containsAny(text, "compose", "편지쓰기", "search", "검색", "navigation", "settings", "설정");
    }
}
