package org.microg.gms.auth.account

import android.accounts.Account
import android.accounts.AccountManager
import android.util.Log
import com.google.android.gms.auth.account.IWorkAccountCallback
import com.google.android.gms.auth.account.IWorkAccountService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Feature
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import com.google.android.gms.common.api.Status
import com.google.android.gms.common.internal.ConnectionInfo
import com.google.android.gms.common.internal.GetServiceRequest
import com.google.android.gms.common.internal.IGmsCallbacks
import org.microg.gms.BaseService
import org.microg.gms.common.GmsConnector
import org.microg.gms.common.GmsService
import org.microg.gms.common.api.InstantPendingResult

private const val TAG = "WorkAccountService"

class WorkAccountService : BaseService(TAG, GmsService.WORK_ACCOUNT) {
    override fun handleServiceRequest(
        callback: IGmsCallbacks,
        request: GetServiceRequest,
        service: GmsService
    ) {
        callback.onPostInitCompleteWithConnectionInfo(
            ConnectionResult.SUCCESS,
            WorkAccountServiceImpl(AccountManager.get(this), request.packageName).asBinder(),
            ConnectionInfo().apply {
                features = arrayOf(Feature("work_account_client_is_whitelisted", 1))
            }
        )
    }
}

class WorkAccountServiceImpl(val accountManager: AccountManager, val packageName: String) :
    IWorkAccountService.Stub() {

    override fun addWorkAccount(token: String?) : Account {
        Log.d(TAG, "addWorkAccount: $token")
        if (token != null) {
            for (account in accountManager.getAccountsByTypeForPackage(null, packageName)) {
                accountManager.setAuthToken(account, token, null)
            }
        }
        return accountManager.accounts[0]
    }

    override fun removeWorkAccount(account: Account?) {
        Log.d(TAG, "removeWorkAccount: $account")
        accountManager.removeAccountExplicitly(account)
    }

    override fun setWorkAuthenticatorEnabled(enabled: Boolean) {
        Log.d(TAG, "setWorkAuthenticatorEnabled: $enabled")
        var result = false
        for (account in accountManager.getAccountsByTypeForPackage(null, packageName)) {
            Log.d(TAG, "setWorkAuthenticatorEnabled: $account")
            result = result or accountManager.setAccountVisibility(
                account,
                packageName,
                if (enabled) AccountManager.VISIBILITY_VISIBLE
                else AccountManager.VISIBILITY_NOT_VISIBLE
            )
        }
    }
}