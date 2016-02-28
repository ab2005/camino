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

package com.camino.lib.provider.imagepipeline.calls;

import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import com.camino.lib.provider.Provider;
import com.camino.lib.provider.dropbox.DbxProvider;
import com.camino.lib.provider.imagepipeline.OkHttpNetworkFetcher;
import com.camino.lib.provider.lyve.LyveCloudProvider;
import com.camino.lib.provider.network.ServiceGenerator;
import com.facebook.imagepipeline.producers.NetworkFetcher;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LyveCloudOkHttpDownloadCall implements OkHttpNetworkFetcher.Call {
    private static final String TAG = LyveCloudOkHttpDownloadCall.class.getSimpleName();

    private final OkHttpNetworkFetcher.HttpNetworkFetchState mFetchState;
    private final NetworkFetcher.Callback mCallback;
    private final Provider mProvider;
    private final okhttp3.Call mHttpCall;
    private final String path;

    public LyveCloudOkHttpDownloadCall(final OkHttpNetworkFetcher.HttpNetworkFetchState fetchState,
                                       final NetworkFetcher.Callback callback,
                                       Provider provider,
                                       final OkHttpClient mOkHttpClient) {
        this.mFetchState = fetchState;
        this.mCallback = callback;
        this.mProvider = provider;
        Uri uri = fetchState.getUri();
        path = uri.getPath().substring(1);
        Request request = createLyveCloudRequest(uri);
        mHttpCall = mOkHttpClient.newCall(request);
        Log.d(TAG, path + ": created call");
        mFetchState.responseTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void run() {
        Log.d(TAG, path + ": running call..");
        try {
            Response response = mHttpCall.execute();
            mFetchState.responseTime = SystemClock.elapsedRealtime();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected HTTP response " + response);
            }

            final ResponseBody body = response.body();
            final int length = (int) response.body().contentLength();
            Log.d(TAG, path + ": got " + length + " bytes in " + (mFetchState.responseTime - mFetchState.submitTime) + " ms");
            try {
                mCallback.onResponse(body.byteStream(), length);
                Log.d(TAG, path + ": callback took " + (SystemClock.elapsedRealtime() - mFetchState.responseTime) + " ms");
            } finally {
                try {
                    body.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        } catch (IOException e) {
            if (!mHttpCall.isCanceled()) {
                Log.d(TAG, path + ": http call failed in " + (SystemClock.elapsedRealtime() - mFetchState.submitTime) + " ms,  " + e.getMessage());
                mCallback.onFailure(e);
            }
        } catch (Exception e) {
            Log.e(TAG, path + ": http call fatal error in " + (SystemClock.elapsedRealtime() - mFetchState.submitTime) + " ms,  " + e.getMessage());
            throw new RuntimeException("Fatal error: ", e);
        }
    }

    private void handleException(Exception e) {
    }

    @Override
    public void cancel() {
        try {
            mHttpCall.cancel();
        } finally {
            // We have to deliver cancel notification immediately after canceling http call.
            mCallback.onCancellation();
        }
    }

    private Request createLyveCloudRequest(Uri uri) {
        String size = uri.getQueryParameter("size");
        String format = uri.getQueryParameter("format");
        String cmd = "/v1/files/download";
        String args = String.format("{\"path\":\"%s\"}", path);

        if (size != null || format != null) {
            cmd = "/v1/files/get_thumbnail";
            if (format == null) {
                format = "jpeg";
            }
            if (size == null) {
                size = "w64h64";
            }
            args = String.format("{" +
                    "\"path\":\"%s\"," +
                    "\"format\":\"%s\"," +
                    "\"size\":\"%s\"" +
                    "}", path, format, size);
//            String args1 = new ThumbnailRequest(path, "jpeg", size).toString();
//            if (args.equals(args1)) {
//                Log.d(TAG, "ok");
//            } else {
//                Log.d(TAG, "KO");
//            }
//            args = args1;
        }

        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noStore().build())
                .url(ServiceGenerator.API_BASE_URL + cmd)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + mProvider.getAccessToken())
                .header("Connection", "Keep-Alive")
                .method("POST", RequestBody.create(MediaType.parse(""), args))
                .build();
        Log.d(TAG, args);

        return request;
    }

    private Request createDropboxRequest(Uri uri) {
        String size = uri.getQueryParameter("size");
        String format = uri.getQueryParameter("format");
        String cmd = null;
        String args = null;
        String path = Uri.decode(uri.getPath());
        if (size == null && format == null) {
            // full size image
            cmd = "/2/files/download";
            args = String.format("{\"path\":\"%s\"}", path);
        } else {
            cmd = "/2/files/get_thumbnail";
            if (format == null) {
                format = "jpeg";
            }
            if (size == null) {
                size = "w64h64";
            }
            args = String.format("{" +
                    "  \"path\": \"%s\"," +
                    "  \"format\":{\".tag\": \"%s\"}," +
                    "  \"size\":{\".tag\":\"%s\"}" +
                    "}", path, format, size);
        }

        Request request = null;
        Log.d(TAG, path + ": creating request...");
        try {
            new Request.Builder()
                    .cacheControl(new CacheControl.Builder().noStore().build())
                    .url(ServiceGenerator.DBX_API_BASE_URL + cmd)
                    .header("Authorization", "Bearer " + mProvider.getAccessToken())
                    .header("Connection", "Keep-Alive")
                    .header("Dropbox-API-Arg", args)
                    .method("POST", RequestBody.create(MediaType.parse(""), args))
                    .build();
            Log.d(TAG, path + ": created request");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return request;
    }

}
