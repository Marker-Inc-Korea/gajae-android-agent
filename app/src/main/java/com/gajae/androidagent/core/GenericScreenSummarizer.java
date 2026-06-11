package com.gajae.androidagent.core;

public final class GenericScreenSummarizer {
    public String summarize(AppCommand command, UiSnapshot snapshot, boolean usedVlm) {
        String text = snapshot.allText();
        if (text.isEmpty()) {
            return usedVlm ? "접근성 텍스트가 부족해서 화면 분석이 필요합니다." : "현재 화면에서 텍스트를 읽지 못했습니다.";
        }
        String[] lines = text.split("\\n");
        StringBuilder out = new StringBuilder();
        out.append(command.appName).append(" 화면에서 읽은 주요 텍스트입니다.");
        if (usedVlm) out.append(" 접근성 정보가 부족해 VLM 보조가 필요합니다.");
        int max = Math.min(12, lines.length);
        for (int i = 0; i < max; i++) out.append('\n').append(i + 1).append(". ").append(lines[i]);
        return out.toString();
    }
}
