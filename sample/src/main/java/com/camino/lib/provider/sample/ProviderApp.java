/*
 * Copyright (c) 2015. Seagate Technology PLC. All rights reserved.
 */

package com.camino.lib.provider.sample;

// the Application class is the first Alto code to execute

import android.app.Application;

import com.camino.lib.provider.Providers;
import com.facebook.drawee.backends.pipeline.Fresco;

public class ProviderApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Providers.initWithDefaults(this);
        Fresco.getImagePipeline().clearCaches();
    }
}
