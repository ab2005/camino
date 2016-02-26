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


import android.content.Context;
import android.util.SparseIntArray;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.imagepipeline.memory.PoolConfig;
import com.facebook.imagepipeline.memory.PoolFactory;
import com.facebook.imagepipeline.memory.PoolParams;
import com.facebook.imagepipeline.producers.NetworkFetcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

/**
 * Creates ImagePipeline configuration that uses {@link }OkHttpNetworkFetcher}
 * with OkHttp as a backend for {@link com.camino.lib.provider.Provider} calls.
 */
public class ConfigFactory {
    public final static int MAX_REQUEST_PER_TIME = 164;
    private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";

    /**
     * Default config with {@link OkHttpNetworkFetcher} as network backend.
     */
    public static com.facebook.imagepipeline.core.ImagePipelineConfig getDefaultConfig(Context context) {
        Set<RequestListener> requestListeners = new HashSet<>(
                Arrays.asList(new RequestListener[]{new RequestLoggingListener()}));

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                .setMaxCacheSize(ConfigConstants.MAX_DISK_CACHE_SIZE)
                .build();

        OkHttpClient httpClient = new OkHttpClient.Builder()
                //   .addInterceptor(new StethoInterceptor())
                .build();

        NetworkFetcher networkFetcher = new OkHttpNetworkFetcher(httpClient);

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(true)
                .setWebpSupportEnabled(true)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setNetworkFetcher(networkFetcher)
                .setRequestListeners(requestListeners)
                .build();

        return config;
    }

    /**
     * Default builder with custom pool factory (see:{@link PoolFactory}).
     */
    public static ImagePipelineConfig.Builder newBuilder(Context context, OkHttpClient okHttpClient) {
        SparseIntArray defaultBuckets = new SparseIntArray();
        defaultBuckets.put(16 * 1024, MAX_REQUEST_PER_TIME);
        PoolParams smallByteArrayPoolParams = new PoolParams(
                16 * 1024 * MAX_REQUEST_PER_TIME,
                4 * 1024 * 1024,
                defaultBuckets);
        PoolFactory factory = new PoolFactory(PoolConfig.newBuilder()
                .setSmallByteArrayPoolParams(smallByteArrayPoolParams)
                .build());
        return ImagePipelineConfig
                .newBuilder(context)
                .setDownsampleEnabled(true)
                .setPoolFactory(factory)
                .setNetworkFetcher(new OkHttpNetworkFetcher(okHttpClient));
    }
}
