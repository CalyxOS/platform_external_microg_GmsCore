package com.google.android.gms.auth.account;

import android.accounts.Account;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;

@Deprecated
public interface WorkAccountApi {

    @Deprecated
    interface AddAccountResult extends Result {
        abstract Account getAccount();
    }

    @Deprecated
    PendingResult<AddAccountResult> addWorkAccount(GoogleApiClient apiClient, String token);

    @Deprecated
    PendingResult<Result> removeWorkAccount(GoogleApiClient apiClient, Account account);

    @Deprecated
    void setWorkAuthenticatorEnabled(GoogleApiClient apiClient, boolean enabled);

    @Deprecated
    PendingResult<Result> setWorkAuthenticatorEnabledWithResult(GoogleApiClient apiClient,
                                                                boolean enabled);
}
