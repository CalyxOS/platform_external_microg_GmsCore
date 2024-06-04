/*
 * SPDX-FileCopyrightText: 2023 microG Project Team
 * SPDX-License-Identifier: Apache-2.0
 */

package com.google.android.play.core.expressintegrityservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ExpressIntegrityService extends Service {

    private final IExpressIntegrityService.Stub mExpressIntegrityService =
            new IExpressIntegrityService.Stub() {

    };

    public IBinder onBind(Intent intent) {
        return mExpressIntegrityService;
    }
}