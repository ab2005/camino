/*
 * Copyright (c) 2015. Seagate Technology PLC. All rights reserved.
 */

package com.camino.lib.provider.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.camino.lib.provider.Providers;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxUsers;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;


/**
 * Activity that shows information about the currently logged in user
 */
public class ProviderUserActivity extends AuthActivity {
    private static final String TAG = "ProviderUserActivity";

    @Bind(R.id.login_button) View btnLoginDropbox;
    @Bind(R.id.provider_files_button) View btnFilesDropbox;
    @Bind(R.id.email_text) TextView labelEmail;
    @Bind(R.id.name_text) TextView labelName;
    @Bind(R.id.type_text) TextView labelText;

    @OnClick (R.id.login_button) public void loginDropbox (View v) {
        Auth.startOAuth2Authentication(ProviderUserActivity.this, getString(R.string.app_key));
    }

    @OnClick (R.id.provider_files_button) public void showDropboxFiles (View v) {
        startActivity(ProviderFilesActivity.getIntent(ProviderUserActivity.this, "dropbox", ""));
    }

    @OnClick (R.id.lyve_files_button) public void showLyveFiles (View v) {
        exportDatabase();
        //startActivity(ProviderFilesActivity.getIntent(ProviderUserActivity.this, "seagate", ""));
    }

    public void exportDatabase() {
        // init realm
        Realm realm = Realm.getInstance(this);

        File exportRealmFile = null;
        try {
            // get or create an "export.realm" file
            exportRealmFile = new File(getExternalCacheDir(), "export.realm");

            // if "export.realm" already exists, delete
            exportRealmFile.delete();

            // copy current realm to "export.realm"
            realm.writeCopyTo(exportRealmFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        realm.close();

        // init email intent and add export.realm as attachment
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, "ab2005@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "REALM");
        intent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT");
        Uri u = Uri.fromFile(exportRealmFile);
        intent.putExtra(Intent.EXTRA_STREAM, u);

        // start email intent
        startActivity(Intent.createChooser(intent, "YOUR CHOOSER TITLE"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = Auth.getOAuth2Token(); // Providers.DROPBOX.provider.getAccessToken();
        btnLoginDropbox.setEnabled(token == null);
        btnFilesDropbox.setEnabled(token != null);
    }

    DbxUsers.FullAccount mAccoutInfo;

    @Override
    protected void loadData() {
        // We have to show account info
        if (mAccoutInfo != null) return;

        String token = Auth.getOAuth2Token();
        if (token == null) {
            btnLoginDropbox.setEnabled(true);
            btnFilesDropbox.setEnabled(false);
            return;
        }

        Providers.DROPBOX.provider.setAccessToken(token);
        Providers.storeTokens(Providers.DROPBOX.provider);

        // TODO: implement getAccountInfo(sync/async) for all Providers
        new GetCurrentAccountTask(Providers.DROPBOX.provider, new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(DbxUsers.FullAccount result) {
                mAccoutInfo = result;
                labelEmail.setText(result.email);
                labelName.setText(result.name.displayName);
                labelText.setText(result.accountType.toString());
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "GetCurrentAccountTask failed!");
                btnLoginDropbox.setEnabled(true);
                btnFilesDropbox.setEnabled(false);
            }
        }).execute();
    }

}
