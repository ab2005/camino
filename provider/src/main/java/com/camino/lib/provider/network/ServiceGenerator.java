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

package com.camino.lib.provider.network;

import com.camino.lib.provider.dropbox.DbxCloudClient;
import com.camino.lib.provider.lyve.LyveCloudClient;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    public static final String API_BASE_URL = "https://api.dogfood.blackpearlsystems.net";
    public static final String DBX_API_BASE_URL = "https://api.dropboxapi.com";
    private static final long READ_TIMEOUT_SEC = 20;
    private final static int MAX_CONNECTIONS = 3;

    public static LyveCloudClient createLyveCloudService(String authToken) {
        return createService(API_BASE_URL, LyveCloudClient.class, authToken);
    }

    public static DbxCloudClient createDropboxService(String authToken) {
        return createService(API_BASE_URL, DbxCloudClient.class, authToken);
    }

    /**
     * A factory method to create a service instance with authorization
     */
    public static <S> S createService(String baseUrl, Class<S> serviceClass, final String authToken) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
//                .connectionPool(new ConnectionPool(MAX_CONNECTIONS, 5, TimeUnit.MINUTES))
//                .addInterceptor(new StethoInterceptor())
//                .sslSocketFactory(SSLConfig.getSSLSocketFactory())
                .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS);

        if (authToken != null) {
            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + authToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClientBuilder.build();

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .callbackExecutor(Executors.newCachedThreadPool())
                        .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.client(client).build();

        return retrofit.create(serviceClass);
    }

}