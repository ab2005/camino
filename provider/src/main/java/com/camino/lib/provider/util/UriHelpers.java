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

package com.camino.lib.provider.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

/**
 * Utility functions to support Uri conversion and processing.
 */
public final class UriHelpers {

    private UriHelpers() {
    }

    /**
     * Get the file path for an uri. This is a convoluted way to get the path for an Uri created using the
     * StorageAccessFramework. This in no way is the official way to do this but there does not seem to be a better
     * way to do this at this point. It is taken from https://github.com/iPaulPro/aFileChooser.
     *
     * @param context The context of the application
     * @param uri     The uri of the saved file
     * @return The file with path pointing to the saved file. It can return null if we can't resolve the uri properly.
     */
    public static File getFileForUri(final Context context, final Uri uri) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return null;
        }

        String scheme = uri.getScheme();

        String path = null;
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris
                        .withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                path = getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                path = getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            path = getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            path = uri.getPath();
        }

        if (path != null) {
            return new File(path);
        }
        return null;
    }

    public static String getFilePathForUri(final Context context, final Uri uri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return null;
        }

        if (!DocumentsContract.isDocumentUri(context, uri)) {
            if ("file".equalsIgnoreCase(uri.getScheme())) return uri.getPath();
            if ("content".equalsIgnoreCase(uri.getScheme()))
                return getDataColumn(context, uri, null, null);
            // Error
            return null;
        }

        // DocumentProvider
        final String docId = DocumentsContract.getDocumentId(uri);

        if (isDownloadsDocument(uri)) {
            // DownloadsProvider
            Uri base = Uri.parse("content://downloads/public_downloads");
            final Uri contentUri = ContentUris.withAppendedId(base, Long.valueOf(docId));
            return getDataColumn(context, contentUri, null, null);
        }

        final String[] split = docId.split(":");

        if (isExternalStorageDocument(uri)) {
            // ExternalStorageProvider
            final String type = split[0];
            if (!"primary".equalsIgnoreCase(type)) {
                // error
                return null;
            }
            return Environment.getExternalStorageDirectory() + "/" + split[1];
        }

        if (isMediaDocument(uri)) {
            // MediaProvider
            final String type = split[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            final String selection = "_id=?";
            final String[] selectionArgs = {split[1]};
            return getDataColumn(context, contentUri, selection, selectionArgs);
        }

        // Error
        return null;
    }

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        final String[] projection = {"_data"};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            final int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } finally {
                cursor.close();
            }
        }
        // error
        return null;
    }


    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}