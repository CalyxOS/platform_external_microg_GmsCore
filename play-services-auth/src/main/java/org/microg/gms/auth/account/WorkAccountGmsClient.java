package org.microg.gms.auth.account;

import android.accounts.Account;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.google.android.gms.auth.account.IWorkAccountCallback;
import com.google.android.gms.auth.account.IWorkAccountService;

import org.microg.gms.common.GmsClient;
import org.microg.gms.common.GmsService;
import org.microg.gms.common.api.ConnectionCallbacks;
import org.microg.gms.common.api.OnConnectionFailedListener;

public class WorkAccountGmsClient extends GmsClient<IWorkAccountService> {

    public WorkAccountGmsClient(Context context, ConnectionCallbacks callbacks,
                                OnConnectionFailedListener connectionFailedListener) {
        super(context, callbacks, connectionFailedListener, GmsService.WORK_ACCOUNT.ACTION);
        serviceId = GmsService.WORK_ACCOUNT.SERVICE_ID;
    }

    @Override
    protected IWorkAccountService interfaceFromBinder(IBinder binder) {
        return IWorkAccountService.Stub.asInterface(binder);
    }

    public void addWorkAccount(String token) throws RemoteException {
        getServiceInterface().addWorkAccount(token);
    }

    public void removeWorkAccount(Account account)
            throws RemoteException {
        getServiceInterface().removeWorkAccount(account);
    }

    public void setWorkAuthenticatorEnabled(boolean enabled)
            throws RemoteException {
        getServiceInterface().setWorkAuthenticatorEnabled(enabled);
    }
}
