package com.gajae.androidagent.core;

import java.util.List;

public final class ClosedLoopExecutor {
    private final AppActionPerformer performer;
    private final TargetMatcher matcher = new TargetMatcher();

    public ClosedLoopExecutor(AppActionPerformer performer) {
        this.performer = performer;
    }

    public LoopReport execute(List<PlanStep> steps, UiSnapshot snapshot) {
        StringBuilder log = new StringBuilder();
        boolean acted = false;
        for (PlanStep step : steps) {
            if ("CLICK_BEST_TEXT".equals(step.intent)) {
                UiNode node = matcher.bestClickable(snapshot, step.targetText);
                if (node != null && performer.click(node.bounds)) {
                    acted = true;
                    log.append("클릭: ").append(node.visibleText()).append('\n');
                }
            } else if ("SCROLL".equals(step.intent) && performer.scrollForward()) {
                acted = true;
                log.append("스크롤 수행\n");
            }
        }
        if (log.length() == 0) log.append("읽기 중심 작업으로 판단해 화면 조작 없이 요약했습니다.");
        return new LoopReport(acted, log.toString());
    }
}
