package com.google.android.gms.auth.account;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.common.api.Api;

import org.microg.gms.auth.account.WorkAccountApiImpl;
import org.microg.gms.auth.account.WorkAccountClientImpl;
import org.microg.gms.auth.account.WorkAccountGmsClient;
import org.microg.gms.common.PublicApi;

@PublicApi
public class WorkAccount {
    public static final Api<Api.ApiOptions.NoOptions> API = new Api<>(
            (options, context, looper, clientSettings, callbacks, connectionFailedListener) ->
                    new WorkAccountGmsClient(context, callbacks, connectionFailedListener));

    @Deprecated
    public static final WorkAccountApi WorkAccountApi = new WorkAccountApiImpl();

    public static WorkAccountClient getClient(Activity activity) {
        return new WorkAccountClientImpl(activity);
    }

    public static WorkAccountClient getClient(Context context) {
        return new WorkAccountClientImpl(context);
    }
}
