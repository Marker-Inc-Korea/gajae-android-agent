package com.gajae.androidagent.core;

import java.util.List;

public final class GmailSummaryFormatter {
    public String format(List<EmailItem> items, boolean usedVlm) {
        if (items.isEmpty()) {
            return usedVlm ? "접근성 텍스트가 부족해서 화면 분석 보조가 필요했지만, 새 메일을 확정하지 못했습니다."
                    : "현재 보이는 Gmail 화면에서 새 메일을 찾지 못했습니다.";
        }
        StringBuilder out = new StringBuilder();
        out.append("현재 보이는 Gmail 새 메일은 ").append(items.size()).append("개입니다.");
        if (usedVlm) out.append(" 접근성 정보가 부족해 화면 분석 보조를 함께 사용했습니다.");
        for (int i = 0; i < items.size(); i++) {
            EmailItem item = items.get(i);
            out.append('\n').append(i + 1).append(". ").append(item.sender);
            if (!item.subject.isEmpty()) out.append(" — ").append(item.subject);
            if (!item.snippet.isEmpty()) out.append(": ").append(item.snippet);
        }
        return out.toString();
    }
}
