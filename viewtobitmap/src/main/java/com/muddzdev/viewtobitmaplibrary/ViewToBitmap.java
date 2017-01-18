package com.muddzdev.viewtobitmaplibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
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
 * The most easy way to save any View as Bitmap to the phones gallery with options as; file format, jpg quality, naming of the files and folder.
 * The saving operation happens in a AsyncTask thread to prevent blocking the UI Thread.
 * <p>The class contains a listener that returns a boolean value confirming weather the file has been saved or not and a path to file</p>
 */

public class ViewToBitmap {

    private static final String EXTENSION_PNG = ".png";
    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_NOMEDIA = ".nomedia";
    private static final String TAG = "ViewToBitmap";
    private static final int JPG_MAX_QUALITY = 100;

    private Context context;
    private View view;
    private int jpgQuality;
    private boolean saveAsPNG, saveAsNomedia;
    private String fileName;
    private String folderName;
    private OnBitmapSaveListener onBitmapSaveListener;


    public ViewToBitmap(@NonNull Context context) {
        this.context = context;
    }

    /**
     * @param view       The view that will be saved as Bitmap. Can also be a ViewGroup.
     * @param folderName Name of the folder/directory that will be created to store your saved Bitmaps.
     */
    public ViewToBitmap(@NonNull Context context, @NonNull View view, String folderName) {
        this.context = context;
        this.view = view;
        this.folderName = folderName;
    }

    /**
     * @param saveAsNomedia Saving the bitmap as .nomedia fileformat makes it invisible in the gallery.
     */
    public ViewToBitmap setSaveAsNomedia(boolean saveAsNomedia) {
        this.saveAsNomedia = saveAsNomedia;
        return this;
    }

    public ViewToBitmap setSaveAsPNG(boolean saveAsPNG) {
        this.saveAsPNG = saveAsPNG;
        return this;
    }

    /**
     * Set the quality of the JPG between 1-100 before saving.
     * <p>As default the quality for JPG will be 100(MAX).</p>
     * <p>Any value set will be ignored for PNG.</p>
     *
     * @param jpgQuality
     */
    public ViewToBitmap setJpgQuality(int jpgQuality) {
        this.jpgQuality = jpgQuality;
        return this;
    }

    /**
     * Set the name of saved file.
     * <p>If not set manually, the files will have a timestamp as filename.</p>
     *
     * @param fileName
     */
    public ViewToBitmap setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }


    /**
     * Set the name of the folder that will be shown in the users phones gallery app.
     * @param folderName
     */
    public ViewToBitmap setFolderName(String folderName) {
        this.folderName = folderName;
        return this;
    }


    private String getFolderName() {

        String result;

        if (folderName == "" || folderName.isEmpty()) {
            result = String.valueOf(System.currentTimeMillis());
        } else {
            result = folderName;
        }

        return result;
    }

    public ViewToBitmap setView(View view) {
        this.view = view;
        return this;
    }


    public ViewToBitmap setOnBitmapSaveListener(OnBitmapSaveListener onBitmapSaveListener) {
        this.onBitmapSaveListener = onBitmapSaveListener;
        return this;
    }

    /**
     * Saves the view as a bitmap in AsyncTask to the phones gallery/Environment.DIRECTORY_PICTURES
     */
    public ViewToBitmap saveToBitmap() {
        AsyncSaveBitmap asyncSaveBitmap = new AsyncSaveBitmap();
        asyncSaveBitmap.execute();
        return this;
    }

    private void onBitmapSavedListener(boolean isSaved, String path) {
        if (onBitmapSaveListener != null) {
            onBitmapSaveListener.onBitmapSaved(isSaved, path);
        }
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


    private class AsyncSaveBitmap extends AsyncTask<Void, Void, Void> implements MediaScannerConnection.OnScanCompletedListener {

        @Override
        protected Void doInBackground(Void... params) {

            final File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getFolderName());
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

            } catch (NullPointerException e) {
                e.printStackTrace();

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

