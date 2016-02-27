/*
 * Copyright (c) 2015. Seagate Technology PLC. All rights reserved.
 */

package com.camino.lib.provider.sample.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.dropbox.core.v2.DbxFiles;
import com.camino.lib.provider.Provider;
import com.camino.lib.provider.dropbox.DbxProvider;

import java.io.File;

/**
 * Task to download a file from Dropbox and put it in the Downloads folder
 */
public class DownloadFileTask extends AsyncTask<Provider.FileMetadata, Void, File> {

    private static final String TAG = DownloadFileTask.class.getName();
    private final Context mContext;
    private final DbxFiles mFilesClient;
    private Exception mException;
    private Callback mCallback;

    public interface Callback {
        void onDownloadComplete(File result);
        void onError(Exception e);
    }

    public DownloadFileTask(Context context, Provider provider, Callback callback) {
        mContext = context;
        mFilesClient = ((DbxProvider)provider).getFilesClient();
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else {
            mCallback.onDownloadComplete(result);
        }
    }

    @Override
    protected File doInBackground(Provider.FileMetadata... params) {
        Provider.FileMetadata metadata = params[0];
//        try {
//            final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            File file = new File(path, metadata.name());
//
//            // Upload the file.
//            OutputStream outputStream = new FileOutputStream(file);
//            try {
//                // TODO: Replace with Provider API
//                mFilesClient.downloadBuilder(metadata.pathLower()).rev(metadata.rev()).run(outputStream);
//            } finally {
//                outputStream.close();
//            }
//
//            // Tell android about the file
//            MediaScannerConnection.scanFile(
//                    mContext,
//                    new String[]{file.getAbsolutePath()},
//                    null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        @Override
//                        public void onScanCompleted(String path, Uri uri) {
//                            Log.v(TAG, "File " + path + " was scanned seccessfully: " + uri);
//                        }
//                    });
//            return file;
//        } catch (DbxException | IOException e) {
//            e.printStackTrace();
//            mException = e;
//        }

        return null;
    }
}
