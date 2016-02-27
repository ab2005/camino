/*
 * Copyright (c) 2015. Seagate Technology PLC. All rights reserved.
 */

package com.camino.lib.provider.sample.tasks;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxUsers;
import com.camino.lib.provider.Provider;
import com.camino.lib.provider.Providers;
import com.camino.lib.provider.dropbox.DbxProvider;

/**
 * Async task for getting user account info
 */
public class GetCurrentAccountTask extends AsyncTask<Void, Void, DbxUsers.FullAccount> {

    private final DbxUsers mDbxUsersClient;
    private final Callback mCallback;
    private Exception mException;

    public interface Callback {
        void onComplete(DbxUsers.FullAccount result);
        void onError(Exception e);
    }

    public GetCurrentAccountTask(Provider provider, Callback callback) {
        mDbxUsersClient = ((DbxProvider)Providers.DROPBOX.provider).getUsersClient();;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(DbxUsers.FullAccount account) {
        super.onPostExecute(account);
        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onComplete(account);
        }
    }

    @Override
    protected DbxUsers.FullAccount doInBackground(Void... params) {
        try {
            if (mDbxUsersClient != null) {
                return mDbxUsersClient.getCurrentAccount();
            }
        } catch (DbxException e) {
            mException = e;
            e.printStackTrace();
        }
        return null;
    }
}
