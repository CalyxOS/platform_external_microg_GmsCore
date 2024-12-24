/*
 * SPDX-FileCopyrightText: 2023 microG Project Team
 * SPDX-License-Identifier: Apache-2.0
 */

package com.google.android.gms.auth.account;

import android.accounts.Account;
import com.google.android.gms.auth.account.IWorkAccountCallback;
import java.lang.String;

interface IWorkAccountService {
    Account addWorkAccount(String token) = 1;
    void removeWorkAccount(in Account account) = 2;
    void setWorkAuthenticatorEnabled(boolean enabled) = 0;
}
