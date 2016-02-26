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

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.Stetho;
import com.seagate.alto.provider.dropbox.DbxProvider;
import com.seagate.alto.provider.imagepipeline.ConfigFactory;
import com.seagate.alto.provider.local.MediaProvider;
import com.seagate.alto.provider.lyve.LyveCloudProvider;

/**
 * The collection of available providers.
 */
public enum Providers {
    DROPBOX(new DbxProvider()),
    SEAGATE(new LyveCloudProvider()),
    LOCAL(new MediaProvider());

    private static Application sApplication;
    public final Provider provider;

    Providers(Provider provider) {
        this.provider = provider;
    }

    // TODO: add option to provide config

    public static Application getContext() {
        return sApplication;
    }

    /**
     * A single initialization step which occurs once in your Application class {@link Application#onCreate()}.
     *
     * @param context
     */
    public static void initWithDefaults(@NonNull Application context) {
        sApplication = context;
        ImagePipelineConfig config = ConfigFactory.getDefaultConfig(context);
        Fresco.initialize(context, config);
        //FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        Stetho.initializeWithDefaults(context);
        applyStoredTokens();
    }

    public static Provider providerForUri(Uri uri) {
        String authority = uri.getAuthority();
        switch(authority) {
            case "seagate": return SEAGATE.provider;
            case "dropbox": return DROPBOX.provider;
        }
        return null;
    }

    /**
     * Load access tokens stored in the context and set providers.
     */
    public static void applyStoredTokens() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(sApplication);
        for (Providers p : Providers.values()) {
            String key = "Providers.access.token." + p.provider.getDomain();
            String token = prefs.getString(key, null);
            if (token != null) {
                p.provider.setAccessToken(token);
            }
        }
    }

    /**
     * Save access tokens from provider.
     */
    public static void storeTokens(Provider provider) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(sApplication).edit();
        edit.commit();
        String key = "Providers.access.token." + provider.getDomain();
        String token = provider.getAccessToken();
        edit.putString(key, token);
        edit.commit();
    }

    /**
     * Save access tokens from providers.
     */
    public static void storeTokens() {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(sApplication).edit();
        edit.commit();
        for (Providers p : Providers.values()) {
            String key = "Providers.access.token." + p.provider.getDomain();
            String token = p.provider.getAccessToken();
            edit.putString(key, token);
        }
        edit.commit();
    }
}
