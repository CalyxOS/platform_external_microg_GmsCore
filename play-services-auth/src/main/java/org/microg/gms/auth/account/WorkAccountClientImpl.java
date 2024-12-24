package org.microg.gms.auth.account;

import android.accounts.Account;
import android.content.Context;

import com.google.android.gms.auth.account.IWorkAccountCallback;
import com.google.android.gms.auth.account.WorkAccount;
import com.google.android.gms.auth.account.WorkAccountClient;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.Task;

import org.microg.gms.common.PublicApi;
import org.microg.gms.common.api.PendingGoogleApiCall;

/**
 * The main entry point for Work Account
 */

@PublicApi
public class WorkAccountClientImpl extends GoogleApi<Api.ApiOptions.NoOptions>
        implements WorkAccountClient {

    @PublicApi(exclude = true)
    public WorkAccountClientImpl(Context context) {
        super(context, WorkAccount.API);
    }

    @Override
    public Task<Account> addWorkAccount(String token) {
        return scheduleTask((PendingGoogleApiCall<Account, WorkAccountGmsClient>)
                (client, completionSource) -> {
//            client.addWorkAccount(new IWorkAccountCallback.Stub() {
//
//            }, token);
//            if (account != null) {
//                completionSource.trySetResult(account);
//            } else {
//                completionSource.trySetException(new ApiException(Status.INTERNAL_ERROR));
//            }
        });
    }

    @Override
    public Task<Void> removeWorkAccount(Account account) {
        return scheduleTask((PendingGoogleApiCall<Void, WorkAccountGmsClient>)
                (client, completionSource) -> {
//            client.removeWorkAccount(new IWorkAccountCallback.Stub() {
//
//            }, account);
//            if (status) {
//                completionSource.trySetResult(null);
//            } else {
//                completionSource.trySetException(new ApiException(Status.INTERNAL_ERROR));
//            }
        });
    }

    @Override
    public Task<Void> setWorkAuthenticatorEnabled(boolean enabled) {
        return scheduleTask((PendingGoogleApiCall<Void, WorkAccountGmsClient>)
                (client, completionSource) -> {
//            client.setWorkAuthenticatorEnabled(new IWorkAccountCallback.Stub() {
//
//            }, enabled);
//            if (status) {
//                completionSource.trySetResult(null);
//            } else {
//                completionSource.trySetException(new ApiException(Status.INTERNAL_ERROR));
//            }
        });
    }
}
