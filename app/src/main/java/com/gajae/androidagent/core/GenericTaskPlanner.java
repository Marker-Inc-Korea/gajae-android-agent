package com.gajae.androidagent.core;

import java.util.ArrayList;
import java.util.List;

public final class GenericTaskPlanner {
    public List<PlanStep> plan(AppCommand command, UiSnapshot snapshot) {
        List<PlanStep> steps = new ArrayList<>();
        String goal = command.goal;
        if (TextUtil.containsAny(goal, "읽", "요약", "설명", "확인")) {
            steps.add(new PlanStep("READ_SCREEN", ""));
        }
        if (TextUtil.containsAny(goal, "누르", "탭", "클릭", "선택")) {
            steps.add(new PlanStep("CLICK_BEST_TEXT", targetAfterClickWord(goal)));
        }
        if (TextUtil.containsAny(goal, "아래", "더", "스크롤")) {
            steps.add(new PlanStep("SCROLL", ""));
        }
        if (steps.isEmpty()) steps.add(new PlanStep("READ_SCREEN", ""));
        return steps;
    }

    private String targetAfterClickWord(String goal) {
        String[] keys = {"누르", "탭", "클릭", "선택"};
        for (String key : keys) {
            int p = goal.indexOf(key);
            if (p > 0) return TextUtil.clean(goal.substring(0, p));
        }
        return "";
    }
}
