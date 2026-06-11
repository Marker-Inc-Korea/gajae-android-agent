package com.gajae.androidagent.core;

public final class EmailItem {
    public final String sender;
    public final String subject;
    public final String snippet;
    public final boolean unread;

    public EmailItem(String sender, String subject, String snippet, boolean unread) {
        this.sender = TextUtil.clean(sender);
        this.subject = TextUtil.clean(subject);
        this.snippet = TextUtil.clean(snippet);
        this.unread = unread;
    }

    public String compact() {
        String mark = unread ? "새 메일" : "읽은 메일";
        return mark + " · " + sender + " · " + subject + " · " + snippet;
    }
}
