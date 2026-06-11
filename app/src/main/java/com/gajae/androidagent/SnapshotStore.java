package com.gajae.androidagent;

import com.gajae.androidagent.core.UiSnapshot;

public final class SnapshotStore {
    private static UiSnapshot latest = UiSnapshot.empty();

    private SnapshotStore() {}

    public static synchronized void set(UiSnapshot snapshot) {
        latest = snapshot == null ? UiSnapshot.empty() : snapshot;
    }

    public static synchronized UiSnapshot get() {
        return latest;
    }
}
