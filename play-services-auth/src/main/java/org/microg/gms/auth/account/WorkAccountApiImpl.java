package org.microg.gms.auth.account;

import android.accounts.Account;

import com.google.android.gms.auth.account.IWorkAccountCallback;
import com.google.android.gms.auth.account.WorkAccount;
import com.google.android.gms.auth.account.WorkAccountApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;

import org.microg.gms.common.GmsConnector;

public class WorkAccountApiImpl implements WorkAccountApi {
    @Override
    public PendingResult<AddAccountResult> addWorkAccount(GoogleApiClient apiClient, String token) {
        return GmsConnector.call(apiClient, WorkAccount.API,
                (GmsConnector.Callback<WorkAccountGmsClient, AddAccountResult>)
                        (client, resultProvider) -> {
//                            client.addWorkAccount(new IWorkAccountCallback.Stub() {
//
//                            }, token);
//                            resultProvider.onResultAvailable(new AddAccountResult() {
//                                @Override
//                                public Account getAccount() {
//                                    return account;
//                                }
//
//                                @Override
//                                public Status getStatus() {
//                                    return account != null ? Status.SUCCESS : Status.INTERNAL_ERROR;
//                                }
//                            });
                        });
    }

    @Override
    public PendingResult<Result> removeWorkAccount(GoogleApiClient apiClient, Account account) {
        return GmsConnector.call(apiClient, WorkAccount.API,
                (GmsConnector.Callback<WorkAccountGmsClient, Result>) (client, resultProvider) -> {
//                    client.removeWorkAccount(new IWorkAccountCallback.Stub() {
//
//                    }, account);
//                    resultProvider.onResultAvailable(() -> status ? Status.SUCCESS
//                            : Status.INTERNAL_ERROR);
                });
    }

    @Override
    public void setWorkAuthenticatorEnabled(GoogleApiClient apiClient, boolean enabled) {
        setWorkAuthenticatorEnabledWithResult(apiClient, enabled);
    }

    @Override
    public PendingResult<Result> setWorkAuthenticatorEnabledWithResult(GoogleApiClient apiClient,
                                                                       boolean enabled) {
        return GmsConnector.call(apiClient, WorkAccount.API,
                (GmsConnector.Callback<WorkAccountGmsClient, Result>) (client, resultProvider) -> {
//                    client.setWorkAuthenticatorEnabled(
//                            new IWorkAccountCallback.Stub() {
//
//                            }, enabled);
//                    resultProvider.onResultAvailable(() -> status ? Status.SUCCESS
//                            : Status.INTERNAL_ERROR);
                });
    }
}
