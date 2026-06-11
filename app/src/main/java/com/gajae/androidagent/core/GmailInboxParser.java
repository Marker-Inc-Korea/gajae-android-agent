package com.gajae.androidagent.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class GmailInboxParser {
    public List<EmailItem> parse(UiSnapshot snapshot) {
        List<UiNode> textNodes = sortedTextNodes(snapshot);
        List<EmailItem> items = new ArrayList<>();
        for (int i = 0; i < textNodes.size(); i++) {
            UiNode node = textNodes.get(i);
            String text = node.visibleText();
            if (!isUnreadCandidate(node, text)) continue;
            String sender = stripUnreadMarker(text);
            if (sender.isEmpty() || GmailSignals.isNoise(sender)) continue;
            String subject = nextUseful(textNodes, i + 1);
            String snippet = nextUseful(textNodes, i + 2);
            items.add(new EmailItem(sender, subject, snippet, true));
            i += 2;
        }
        return items;
    }

    private List<UiNode> sortedTextNodes(UiSnapshot snapshot) {
        List<UiNode> nodes = new ArrayList<>();
        for (UiNode node : snapshot.nodes) if (node.hasText()) nodes.add(node);
        java.util.Collections.sort(nodes, new Comparator<UiNode>() {
            @Override public int compare(UiNode a, UiNode b) {
                if (a.bounds.top != b.bounds.top) return a.bounds.top - b.bounds.top;
                return a.bounds.left - b.bounds.left;
            }
        });
        return nodes;
    }

    private boolean isUnreadCandidate(UiNode node, String text) {
        String lower = TextUtil.lower(text);
        if (lower.startsWith("unread") || lower.startsWith("읽지 않음")) return true;
        if (lower.contains("unread,")) return true;
        return node.selected && !GmailSignals.isNoise(text);
    }

    private String stripUnreadMarker(String text) {
        String cleaned = TextUtil.clean(text);
        cleaned = cleaned.replaceFirst("(?i)^unread[, ]*", "");
        cleaned = cleaned.replaceFirst("^읽지 않음[, ]*", "");
        return TextUtil.clean(cleaned);
    }

    private String nextUseful(List<UiNode> nodes, int start) {
        for (int i = start; i < nodes.size() && i < start + 4; i++) {
            String text = nodes.get(i).visibleText();
            if (!text.isEmpty() && !GmailSignals.isNoise(text)) return text;
        }
        return "";
    }
}
