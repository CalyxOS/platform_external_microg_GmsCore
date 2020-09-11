/*
 * Copyright (C) 2013-2017 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.microg.gms.checkin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import org.microg.gms.common.ForegroundServiceContext;

import static org.microg.gms.checkin.CheckinService.EXTRA_FORCE_CHECKIN;

public class TriggerReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "GmsCheckinTrigger";
    public static final String PREF_ENABLE_CHECKIN = "checkin_enable_service";
    private static final long REGULAR_CHECKIN_INTERVAL = 12 * 60 * 60 * 1000; // 12 hours

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            boolean force = "android.provider.Telephony.SECRET_CODE".equals(intent.getAction());
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_ENABLE_CHECKIN, true) || force) {
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()) &&
                        LastCheckinInfo.read(context).lastCheckin > System.currentTimeMillis() - REGULAR_CHECKIN_INTERVAL) {
                    return;
                }

                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() || force) {
                    Intent subIntent = new Intent(context, CheckinService.class);
                    subIntent.putExtra(EXTRA_FORCE_CHECKIN, force);
                    startWakefulService(new ForegroundServiceContext(context), subIntent);
                }
            } else {
                Log.d(TAG, "Ignoring " + intent + ": checkin is disabled");
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }
}
