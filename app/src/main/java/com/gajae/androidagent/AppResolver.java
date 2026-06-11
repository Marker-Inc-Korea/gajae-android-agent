package com.gajae.androidagent;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.gajae.androidagent.core.TextUtil;
import com.gajae.androidagent.core.UniversalAppAgent;

import java.util.List;

public final class AppResolver implements UniversalAppAgent.AppResolver {
    private final PackageManager packages;

    public AppResolver(Context context) {
        this.packages = context.getPackageManager();
    }

    @Override
    public String packageForName(String appName) {
        String query = TextUtil.lower(appName);
        if (query.equals("gmail") || query.equals("지메일")) return "com.google.android.gm";
        List<ApplicationInfo> apps = packages.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo app : apps) {
            Intent launch = packages.getLaunchIntentForPackage(app.packageName);
            if (launch == null) continue;
            String label = TextUtil.lower(String.valueOf(packages.getApplicationLabel(app)));
            if (label.equals(query) || label.contains(query) || query.contains(label)) return app.packageName;
        }
        return "";
    }
}
