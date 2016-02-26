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

package com.camino.lib.provider.imagepipeline;

import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.NetworkFetcher;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpCallbackHandler extends BaseProducerContextCallbacks implements okhttp3.Callback {
    private static final String TAG = OkHttpCallbackHandler.class.getSimpleName();

    private final NetworkFetcher.Callback callback;
    private final OkHttpNetworkFetcher.HttpNetworkFetchState fetchState;
    private final okhttp3.Call call;
    private final Executor executor;

    public OkHttpCallbackHandler(okhttp3.Call call, Executor executoor, final OkHttpNetworkFetcher.HttpNetworkFetchState fetchState, final NetworkFetcher.Callback callback) {
        this.callback = callback;
        this.fetchState = fetchState;
        this.call = call;
        fetchState.getContext().addCallbacks(this);
        this.executor = executoor;
    }

    @Override
    public void onFailure(okhttp3.Call call, IOException e) {
        if (call.isCanceled()) {
            callback.onCancellation();
        } else {
            callback.onFailure(e);
        }
    }

    @Override
    public void onResponse(okhttp3.Call call, Response response) throws IOException {
        fetchState.responseTime = SystemClock.elapsedRealtime();
        final ResponseBody body = response.body();
        try {
            long contentLength = body.contentLength();
            Log.d(TAG, fetchState.getUri() + " length = " + contentLength + " - time(ms) = " +
                    (fetchState.responseTime - fetchState.submitTime));
            callback.onResponse(body.byteStream(), (int) contentLength);
        } catch (IOException e) {
            onFailure(call, e);
        } finally {
            try {
                body.close();
            } catch (Exception e) {
                Log.e(TAG, " ", e);
            }
        }
    }

    @Override
    public void onCancellationRequested() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            call.cancel();
        } else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    call.cancel();
                }
            });
        }
    }
}
