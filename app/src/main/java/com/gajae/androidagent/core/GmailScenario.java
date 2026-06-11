package com.gajae.androidagent.core;

import java.util.List;

public final class GmailScenario {
    public interface SnapshotProvider {
        UiSnapshot latestSnapshot();
    }

    public interface AppActions {
        boolean launch(String packageName);
        void waitMillis(long millis);
    }

    private final SnapshotProvider snapshots;
    private final AppActions actions;
    private final GmailInboxParser parser;
    private final GmailSummaryFormatter formatter;
    private final FallbackDecider fallbackDecider;
    private final VlmClient vlmClient;

    public GmailScenario(SnapshotProvider snapshots, AppActions actions, VlmClient vlmClient) {
        this.snapshots = snapshots;
        this.actions = actions;
        this.vlmClient = vlmClient;
        this.parser = new GmailInboxParser();
        this.formatter = new GmailSummaryFormatter();
        this.fallbackDecider = new FallbackDecider();
    }

    public AgentResult run() {
        if (!actions.launch(GmailSignals.PACKAGE)) {
            return AgentResult.fail("Gmail 앱을 실행할 수 없습니다. 설치 여부를 확인하세요.");
        }
        actions.waitMillis(1200);
        UiSnapshot snapshot = snapshots.latestSnapshot();
        boolean usedVlm = fallbackDecider.needsVlm(snapshot);
        List<EmailItem> items = parser.parse(snapshot);
        if (usedVlm && items.isEmpty()) {
            VlmDecision decision = vlmClient.analyze(new VlmRequest("Gmail inbox unread email summary", 0, 0), new byte[0]);
            if (decision.available && !decision.summary.isEmpty()) return AgentResult.ok(decision.summary);
        }
        return AgentResult.ok(formatter.format(items, usedVlm));
    }
}
