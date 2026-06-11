package com.gajae.androidagent.core;

public final class NaturalInstructionParser {
    public InstructionParseResult parse(String instruction) {
        String clean = TextUtil.clean(instruction);
        if (clean.isEmpty()) return InstructionParseResult.fail("지시문이 비어 있습니다.");
        String app = appBeforeKeyword(clean);
        if (app.isEmpty()) app = firstToken(clean);
        String goal = clean;
        if (!app.isEmpty() && goal.startsWith(app)) goal = TextUtil.clean(goal.substring(app.length()));
        goal = goal.replaceFirst("^(앱|을|를|에서|열고|열어서|켜고|실행하고|실행해서)+", "").trim();
        return InstructionParseResult.ok(new AppCommand(app, goal.isEmpty() ? clean : goal));
    }

    private String appBeforeKeyword(String text) {
        String[] keys = {"앱", "에서", "열고", "열어서", "켜고", "실행"};
        int best = -1;
        for (int i = 0; i < keys.length; i++) {
            int p = text.indexOf(keys[i]);
            if (p > 0 && (best < 0 || p < best)) best = p;
        }
        return best > 0 ? TextUtil.clean(text.substring(0, best)) : "";
    }

    private String firstToken(String text) {
        String[] parts = text.split("\\s+");
        return parts.length == 0 ? "" : parts[0];
    }
}
