package com.google.android.gms.auth.account;

import android.accounts.Account;

import com.google.android.gms.tasks.Task;

public interface WorkAccountClient {
    Task<Account> addWorkAccount(String token);
    Task<Void> removeWorkAccount(Account account);
    Task<Void> setWorkAuthenticatorEnabled(boolean enabled);
}
