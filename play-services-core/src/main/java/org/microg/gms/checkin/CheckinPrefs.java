/*
 * SPDX-FileCopyrightText: 2020, microG Project Team
 * SPDX-License-Identifier: Apache-2.0
 */

package org.microg.gms.checkin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.microg.gms.common.PackageUtils;

import java.io.File;

public class CheckinPrefs implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String PREF_ENABLE_CHECKIN = "checkin_enable_service";
    private static CheckinPrefs INSTANCE;

    public static CheckinPrefs get(Context context) {
        if (INSTANCE == null) {
            if (!context.getPackageName().equals(PackageUtils.getProcessName())) {
                Log.w("Preferences", CheckinPrefs.class.getName() + " initialized outside main process", new RuntimeException());
            }
            if (context == null) return new CheckinPrefs(null);
            INSTANCE = new CheckinPrefs(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private SharedPreferences preferences;
    private SharedPreferences systemDefaultPreferences;
    private boolean checkinEnabled = true;

    private CheckinPrefs(Context context) {
        if (context != null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences.registerOnSharedPreferenceChangeListener(this);
            try {
                systemDefaultPreferences = (SharedPreferences) Context.class.getDeclaredMethod("getSharedPreferences", File.class, int.class).invoke(context, new File("/system/etc/microg.xml"), Context.MODE_PRIVATE);
            } catch (Exception ignored) {
            }
            update();
        }
    }

    private boolean getSettingsBoolean(String key, boolean def) {
        if (systemDefaultPreferences != null) {
            def = systemDefaultPreferences.getBoolean(key, def);
        }
        return preferences.getBoolean(key, def);
    }

    private void update() {
        checkinEnabled = getSettingsBoolean(PREF_ENABLE_CHECKIN, true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        update();
    }

    public boolean isEnabled() {
        return checkinEnabled;
    }

    public static void setEnabled(Context context, boolean newStatus) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_ENABLE_CHECKIN, newStatus).commit();
        if (newStatus) {
            context.sendOrderedBroadcast(new Intent(context, TriggerReceiver.class), null);
        }
    }
}
