/*
 * Copyright (c) 2016  ab2005@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.camino.lib.provider;

import android.os.AsyncTask;

import com.camino.lib.provider.lyve.LyveCloudProvider;

import java.io.IOException;

/**
 * Async task to list items in a folder.
 */
public class ListFolderAsyncTask extends AsyncTask<String, Void, Provider.ListFolderResult> {
    private final Provider mProvider;
    private Exception mException;
    private Callback mCallback;

    public ListFolderAsyncTask(Provider provider, Callback callback) {
        mProvider = provider;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(Provider.ListFolderResult result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onDataLoaded(result);
        }
    }

    @Override
    protected Provider.ListFolderResult doInBackground(String... params) {
        try {
            if (mProvider != Providers.LOCAL.provider && mProvider.getAccessToken() == null) {
                if (mProvider instanceof LyveCloudProvider) {
                    try {
                        String token = LyveCloudProvider.login("abarilov@geagate.com", "pitkelevo");
                        mProvider.setAccessToken(token);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }

            if (mProvider instanceof LyveCloudProvider) {
                Provider.ListFolderResult res = null;
                try {
                    res = mProvider.listFolder("");
                } catch (Provider.ProviderException e) {
                    e.printStackTrace();
                    return null;
                }

                String deviceRoot = res.entries().get(0).pathLower();
                String path = deviceRoot + "/Photos/thumbs";
                return mProvider.listFolder(path);
            } else {
                return mProvider.listFolder(params[0]);
            }
        } catch (Provider.ProviderException e) {
            mException = e;
            e.printStackTrace();
        }
        return null;
    }

    public interface Callback {
        void onDataLoaded(Provider.ListFolderResult result);

        void onError(Exception e);
    }
}
