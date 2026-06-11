package com.gajae.androidagent.core;

import java.util.List;

public final class UniversalAppAgent {
    public interface AppResolver {
        String packageForName(String appName);
    }

    private final GmailScenario.AppActions actions;
    private final GmailScenario.SnapshotProvider snapshots;
    private final AppResolver resolver;
    private final AppActionPerformer performer;
    private final NaturalInstructionParser parser = new NaturalInstructionParser();
    private final GenericTaskPlanner planner = new GenericTaskPlanner();
    private final GenericScreenSummarizer summarizer = new GenericScreenSummarizer();
    private final FallbackDecider fallback = new FallbackDecider();

    public UniversalAppAgent(GmailScenario.SnapshotProvider snapshots, GmailScenario.AppActions actions, AppResolver resolver,
                             AppActionPerformer performer) {
        this.snapshots = snapshots;
        this.actions = actions;
        this.resolver = resolver;
        this.performer = performer;
    }

    public AgentResult run(String instruction) {
        InstructionParseResult parsed = parser.parse(instruction);
        if (!parsed.ok) return AgentResult.fail(parsed.error);
        String packageName = resolver.packageForName(parsed.command.appName);
        if (packageName.isEmpty()) return AgentResult.fail("앱을 찾지 못했습니다: " + parsed.command.appName);
        if (!actions.launch(packageName)) return AgentResult.fail("앱을 실행하지 못했습니다: " + parsed.command.appName);
        actions.waitMillis(1200);
        UiSnapshot snapshot = snapshots.latestSnapshot();
        List<PlanStep> steps = planner.plan(parsed.command, snapshot);
        boolean usedVlm = fallback.needsVlm(snapshot);
        StringBuilder out = new StringBuilder(summarizer.summarize(parsed.command, snapshot, usedVlm));
        out.append("\n\n수행 계획:");
        for (PlanStep step : steps) out.append('\n').append("- ").append(step.line());
        LoopReport report = new ClosedLoopExecutor(performer).execute(steps, snapshot);
        out.append("\n\n조작 결과:\n").append(report.summary);
        return AgentResult.ok(out.toString());
    }
}
