package com.gajae.androidagent.core;

import java.util.Locale;

public final class TextUtil {
    private TextUtil() {}

    public static String clean(CharSequence value) {
        if (value == null) return "";
        return value.toString().replace('\n', ' ').trim().replaceAll("\\s+", " ");
    }

    public static String lower(String value) {
        return clean(value).toLowerCase(Locale.ROOT);
    }

    public static boolean containsAny(String haystack, String... needles) {
        String lower = lower(haystack);
        for (String needle : needles) {
            if (!needle.isEmpty() && lower.contains(needle.toLowerCase(Locale.ROOT))) return true;
        }
        return false;
    }
}
