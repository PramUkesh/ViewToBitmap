package com.muddzdev.viewtobitmaplibrary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

//        Copyright 2017 Muddii Walid (Muuddz)
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

/**
 * <p></p>
 * The most easy way to save any View as an image to the phones gallery with options as; file format, jpg quality, naming of the files and folder.
 * The saving operation happens in a AsyncTask thread to prevent blocking the UI Thread.
 * <p>The class contains an optional listener that returns a boolean value confirming weather the file has been saved or not and a path to file</p>
 */

public class ViewToBitmap {

    private static final String EXTENSION_PNG = ".png";
    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_NOMEDIA = ".nomedia";
    private static final String TAG = "ViewToBitmap";
    private static final int JPG_MAX_QUALITY = 100;

    private View view;
    private int jpgQuality;
    private String fileName;
    private String folderName;
    private boolean saveAsPNG, saveAsNomedia;
    private OnBitmapSaveListener onBitmapSaveListener;


    /**
     * @param view The view to save as image
     */
    public ViewToBitmap(@NonNull View view) {
        this.view = view;
    }

    /**
     * @param view       The view that will be saved as Bitmap. Can also be a ViewGroup.
     * @param folderName Name of the folder/directory that will be created to store your saved Bitmaps. If null Bitmaps will be saved to DIRECTORY_PICTURES.
     */
    public ViewToBitmap(@Nullable View view, @Nullable String folderName) {
        this.view = view;
        this.folderName = folderName;
    }

    /**
     * @param view       The view that will be saved as Bitmap. Can also be a ViewGroup.
     * @param folderName Name of the folder/directory that will be created to store your saved Bitmaps. If null Bitmaps will be saved to DIRECTORY_PICTURES.
     * @param fileName   If null the filename will be a timestamp at saving time.
     */
    public ViewToBitmap(@Nullable View view, @Nullable String folderName, @Nullable String fileName) {
        this.view = view;
        this.folderName = folderName;
        this.fileName = fileName;
    }


    /**
     * saveAsNomedia Saving the bitmap as .nomedia fileformat makes it invisible in the gallery.
     */
    public void saveAsNomedia() {
        saveAsNomedia = true;
    }

    public void saveAsPNG() {
        saveAsPNG = true;
    }

    /**
     * Set the quality of the JPG between 1-100 before saving.
     * <p>As default the quality for JPG will be 100(MAX).</p>
     * <p>Any value set will be ignored for PNG.</p>
     *
     * @param jpgQuality
     */
    public void setJpgQuality(int jpgQuality) {
        this.jpgQuality = jpgQuality;

    }


    /**
     * Set the name of saved file.
     * <p>If not set manually, the files will have a timestamp as filename.</p>
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    /**
     * Sets the name of the folder/Directory to where your bitmaps will be saved.
     * <p>Default directory: Environment.DIRECTORY_PICTURES.</p>
     * Other standard Android directories in Environment.DIRECTORY_X can also be used
     * <p>or just name your own directory as you wish</p>
     * @param folderName
     */
    public void setDirectoryName(String folderName) {
        this.folderName = folderName;

    }


    private String getFolderName() {
        String result;
        if (folderName == null || folderName.isEmpty()) {
            result = Environment.DIRECTORY_PICTURES;
        } else {
            result = folderName;
        }

        return result;
    }

    public void setView(View view) {
        this.view = view;
    }

    private Context getAppContext() {

        Context context = null;

        if (view != null) {
            context = view.getContext().getApplicationContext();
        }

        return context;
    }

    public void setOnBitmapSaveListener(OnBitmapSaveListener onBitmapSaveListener) {
        this.onBitmapSaveListener = onBitmapSaveListener;
    }

    private void onBitmapSavedListener(final boolean isSaved, final String path) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (onBitmapSaveListener != null) {
                    onBitmapSaveListener.onBitmapSaved(isSaved, path);
                }
            }
        });
    }


    private String getFileName() {
        String result;
        if (fileName == null || fileName.isEmpty()) {
            result = String.valueOf(System.currentTimeMillis());
        } else {
            result = fileName;
        }
        return result;
    }


    private String getFileExtension() {
        String result;

        if (saveAsPNG) {
            result = EXTENSION_PNG;
        } else if (saveAsNomedia) {
            result = EXTENSION_NOMEDIA;
        } else {
            result = EXTENSION_JPG;
        }
        return result;
    }

    /**
     * Converts a view into a bitmap/image
     */
    private Bitmap viewToBitmap() {

        Bitmap bitmap = null;

        if (view != null) {

            view.setDrawingCacheEnabled(true);
            view.getDrawingCache();
            view.buildDrawingCache();
            bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            view.buildDrawingCache(false);
        }

        return bitmap;

    }

    private boolean isExternalStorageReady() {
        boolean result;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    private boolean isPermissionGranted() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        boolean result;

        if (ContextCompat.checkSelfPermission(getAppContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    /**
     * Saves the view as an Image in AsyncTask to the phones gallery
     */

    public void saveToGallery() {

        if (isExternalStorageReady() && isPermissionGranted()) {
            AsyncSaveBitmap asyncSaveBitmap = new AsyncSaveBitmap(getAppContext());
            asyncSaveBitmap.execute();
        } else {
            Log.d(TAG, "Make sure external storage is available and permission WRITE_EXTERNAL_STORAGE is granted");
        }

    }


    private class AsyncSaveBitmap extends AsyncTask<Void, Void, Void> implements MediaScannerConnection.OnScanCompletedListener {

        private final Context context;

        public AsyncSaveBitmap(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {

            final File myDir = new File(Environment.getExternalStorageDirectory(), getFolderName());
            myDir.mkdirs();
            final File file = new File(myDir, getFileName() + getFileExtension());
            OutputStream out = null;
            int quality = 0;

            try {
                out = new BufferedOutputStream(new FileOutputStream(file));

                if (saveAsPNG) {
                    viewToBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);

                } else {
                    if (jpgQuality == 0) {
                        quality = JPG_MAX_QUALITY;
                    }

                    viewToBitmap().compress(Bitmap.CompressFormat.JPEG, quality, out);
                }

            } catch (IOException e) {
                e.printStackTrace();
                onBitmapSavedListener(false, null);

            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        onBitmapSavedListener(false, null);
                        e.printStackTrace();
                    }
                }

                MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, this);
            }

            return null;
        }


        @Override
        public void onScanCompleted(String path, Uri uri) {

            if (uri != null && path != null) {
                onBitmapSavedListener(true, path);
                Log.i(TAG, "PATH: " + path);
                Log.i(TAG, "URI: " + uri);

            } else {
                onBitmapSavedListener(false, null);
            }
        }
    }


}

