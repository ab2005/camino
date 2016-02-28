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

import android.net.Uri;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.BaseNetworkFetcher;
import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.ProducerContext;
import com.camino.lib.provider.Provider;
import com.camino.lib.provider.Providers;
import com.camino.lib.provider.imagepipeline.callbacks.OkHttpCallbackHandler;
import com.camino.lib.provider.imagepipeline.callbacks.RetrofitCallbackHandler;
import com.camino.lib.provider.imagepipeline.calls.DbxFilesDownloadCall;
import com.camino.lib.provider.imagepipeline.calls.LyveCloudOkHttpDownloadCall;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Network fetcher that uses OkHttp and Provider's service as a backend for image request calls.
 */
public class OkHttpNetworkFetcher extends BaseNetworkFetcher<OkHttpNetworkFetcher.HttpNetworkFetchState> {
    private static final boolean DEBUG = true;

    public static class HttpNetworkFetchState extends FetchState {
        public long submitTime;
        public long responseTime;
        public long fetchCompleteTime;

        /**
         * Constructs instance for JUnit testing.
         */
        public HttpNetworkFetchState() {
            this(null, null);
        }

        public HttpNetworkFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
            super(consumer, producerContext);
        }
    }

    /**
     * A runner for cancelable network requests.
     */
    public interface Call extends Runnable {
        void cancel();
    }

    private static final String TAG = OkHttpNetworkFetcher.class.getSimpleName();

    private static final String QUEUE_TIME = "queue_time";
    private static final String FETCH_TIME = "fetch_time";
    private static final String TOTAL_TIME = "total_time";
    private static final String IMAGE_SIZE = "image_size";

    private final OkHttpClient mOkHttpClient;

    private final Executor mExecutor;

    /**
     * Image fetcher that uses http client's executor to run providers API calls.
     * @param okHttpClient client to use
     */
    public OkHttpNetworkFetcher(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
        mExecutor = okHttpClient.dispatcher().executorService();
    }

    @Override
    public HttpNetworkFetchState createFetchState(Consumer<EncodedImage> consumer, ProducerContext context) {
        return new HttpNetworkFetchState(consumer, context);
    }

    @Override
    public void fetch(final HttpNetworkFetchState fetchState, final Callback callback) {
        fetchState.submitTime = SystemClock.elapsedRealtime();
        final Uri uri = fetchState.getUri();
        Log.d(TAG, uri + " fetch...");
        final String domain = fetchState.getUri().getAuthority();
        switch (domain) {
            case "seagate":
                executeCallOnExecutorThread(fetchState, callback);
                break;
            case "seagate.retro":
                executeOnLyveCloudProviderServiceThread(fetchState, callback);
                break;
            case "dropbox":
                executeCallOnExecutorThread(fetchState, callback);
                break;
            default:
                final Request request = new Request.Builder()
                        .cacheControl(new CacheControl.Builder().noStore().build())
                        .url(uri.toString())
                        .get()
                        .build();
                final okhttp3.Call call = mOkHttpClient.newCall(request);
                call.enqueue(new OkHttpCallbackHandler(call, mExecutor, fetchState, callback));
                break;
        }
    }

    @Override
    public void onFetchCompletion(HttpNetworkFetchState fetchState, int byteSize) {
        if (DEBUG) {
            fetchState.fetchCompleteTime = SystemClock.elapsedRealtime();
            final Uri uri = fetchState.getUri();
            Log.d(TAG, uri + " completed. " + TOTAL_TIME + " = " + Long.toString(fetchState.fetchCompleteTime - fetchState.submitTime));
        }
    }

    @Override
    public Map<String, String> getExtraMap(HttpNetworkFetchState fetchState, int byteSize) {
        Map<String, String> extraMap = new HashMap<>(4);
        extraMap.put(QUEUE_TIME, Long.toString(fetchState.responseTime - fetchState.submitTime));
        extraMap.put(FETCH_TIME, Long.toString(fetchState.fetchCompleteTime - fetchState.responseTime));
        extraMap.put(TOTAL_TIME, Long.toString(fetchState.fetchCompleteTime - fetchState.submitTime));
        extraMap.put(IMAGE_SIZE, Integer.toString(byteSize));
        return extraMap;
    }

    private void executeCallOnExecutorThread(final HttpNetworkFetchState fetchState, final Callback callBack) {
        final String domain = fetchState.getUri().getAuthority();

        final Call call;
        switch (domain) {
            case "seagate":
                call = new LyveCloudOkHttpDownloadCall(fetchState, callBack, Providers.SEAGATE.provider, mOkHttpClient);
                break;
            case "dropbox":
                call = new DbxFilesDownloadCall(fetchState, callBack, Providers.DROPBOX.provider);
                break;
            default:
                throw new RuntimeException("Unknown provider " + domain);
        }

        fetchState.getContext().addCallbacks(new BaseProducerContextCallbacks() {
            @Override
            public void onCancellationRequested() {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    call.cancel();
                } else {
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            call.cancel();
                        }
                    });
                }
            }
        });

        Log.d(TAG, fetchState.getUri() + ": executor execute ");
        mExecutor.execute(call);
    }

    /*
     * Enqueue retrofit callback handler to LyveCloudProvider's service callback thread
     */
    private void executeOnLyveCloudProviderServiceThread(final HttpNetworkFetchState fetchState, final Callback callback) {
        Uri uri = fetchState.getUri();
        String path = uri.getPath();
        path = path.startsWith("//") ? path.substring(1) : path;
        String size = uri.getQueryParameter("size");
        String format = uri.getQueryParameter("format");
        try {
            final retrofit2.Call<okhttp3.ResponseBody> call;
            if (size != null && format != null) {
                call = Providers.SEAGATE.provider.downloadThumbnail(path, format, size);
            } else {
                call = Providers.SEAGATE.provider.download(path);
            }
            call.enqueue(new RetrofitCallbackHandler(call, mExecutor, fetchState, callback));
        } catch (Provider.ProviderException e) {
            callback.onFailure(e);
        }
    }
}
