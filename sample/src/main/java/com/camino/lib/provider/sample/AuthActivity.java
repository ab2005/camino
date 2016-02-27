/*
 * Copyright (c) 2015. Seagate Technology PLC. All rights reserved.
 */

package com.camino.lib.provider.sample;

import android.support.v7.app.AppCompatActivity;


/**
 * Base class for Activities that require auth tokens.
 * Will redirect to auth flow if needed.
 */
public abstract class AuthActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    protected abstract void loadData();
}
