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

package com.camino.lib.provider.imagepipeline.callbacks;

import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.camino.lib.provider.imagepipeline.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.NetworkFetcher;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.ResponseBody;

public class RetrofitCallbackHandler extends BaseProducerContextCallbacks implements retrofit2.Callback<okhttp3.ResponseBody> {
    private static final String TAG = RetrofitCallbackHandler.class.getSimpleName();

    private final NetworkFetcher.Callback callback;
    private final OkHttpNetworkFetcher.HttpNetworkFetchState fetchState;
    private final retrofit2.Call<ResponseBody> call;
    private final Executor executor;
    private final long start;

    public RetrofitCallbackHandler(retrofit2.Call<ResponseBody> call, Executor executoor, final OkHttpNetworkFetcher.HttpNetworkFetchState fetchState, final NetworkFetcher.Callback callback) {
        this.callback = callback;
        this.fetchState = fetchState;
        this.call = call;
        fetchState.getContext().addCallbacks(this);
        this.executor = executoor;
        this.start = SystemClock.elapsedRealtime();
    }

    @Override
    public void onResponse(final retrofit2.Call<ResponseBody> call, final retrofit2.Response<ResponseBody> response) {
        fetchState.responseTime = SystemClock.elapsedRealtime();
        if (response.isSuccess()) {
            try {
                long contentLength = response.body().contentLength();
                Log.d(TAG, "onResponse(): length = " + contentLength + " - response time = " +
                        (fetchState.responseTime - fetchState.submitTime) + "/"
                        + (fetchState.responseTime - start) + ": " + fetchState.getUri());

                long t = SystemClock.elapsedRealtime();
                callback.onResponse(response.body().byteStream(), -1);
                Log.d(TAG, "callback time = " + (SystemClock.elapsedRealtime() - t) + ": " + fetchState.getUri());
            } catch (IOException e) {
                onFailure(call, e);
            } finally {
                response.body().close();
            }
        } else {
            Log.d(TAG, "not success, " + (fetchState.responseTime - fetchState.submitTime) + ": " + fetchState.getUri());
            String errMessage = response.code() + ", " + response.message();
            onFailure(call, new IOException("Request failed!" + errMessage));
        }
    }

    @Override
    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
        long t0 = SystemClock.elapsedRealtime();
        Log.d(TAG, "onFailure() " + t.getMessage() + " after " + (t0 - start) + ": " + fetchState.getUri());
        if (call.isCanceled()) {
            callback.onCancellation();
        } else {
            callback.onFailure(t);
        }
    }

    @Override
    public void onCancellationRequested() {
        long t0 = SystemClock.elapsedRealtime();
        Log.d(TAG, "onCancelationRequest() after " + (t0 - start) + ": " + fetchState.getUri());
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
