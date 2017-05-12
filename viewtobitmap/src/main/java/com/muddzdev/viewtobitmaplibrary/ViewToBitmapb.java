package com.muddzdev.viewtobitmaplibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/*
 * Copyright 2017 Muddii Walid (Muddzdev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * xxx is a library made for saving any View as an image in either JPG or PNG very simple.
 * The library does its work in a non blocking AsyncTask and handles any error that may occur </br>.
 */

public class ViewToBitmapb {

    private static final String TAG = ViewToBitmapb.class.getSimpleName();
    private static final String EXTENSION_PNG = ".png";
    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_NOMEDIA = ".nomedia";
    private static final int JPG_MAX_QUALITY = 100;
    private String fileName, directoryPath, fileExtension;
    private OnSaveResultListener onSaveResultListener;
    private int jpgQuality;
    private Handler handler;
    private View view;

    /**
     * @param view The View to save as an image. Can also be a ViewGroup or SurfaceView
     */
    public static ViewToBitmapb from(@NonNull View view) {

        return viewToBitmapb;
    }


    /**
     * Saves the View to a JPG image with the highest quality.
     */
    public ViewToBitmapb toJPG() {
        jpgQuality = JPG_MAX_QUALITY;
        setFileExtension(EXTENSION_JPG);
        return this;
    }

    /**
     * Saves the View to a JPG image.
     * @param jpgQuality set the quality of the JPG file between 1-100. The higher number the bigger JPG file and quality.
     * <br>Default is 100(MAX QUALITY).
     */
    public ViewToBitmapb toJPG(int jpgQuality) {
        this.jpgQuality = (jpgQuality == 0) ? JPG_MAX_QUALITY : jpgQuality;
        setFileExtension(EXTENSION_JPG);
        return this;
    }

    /**
     * Saves the View to a PNG image.
     */
    public ViewToBitmapb toPNG() {
        setFileExtension(EXTENSION_PNG);
        return this;
    }

    /**
     * Saving the View to a .nomedia file format makes it invisible in galleries and other image viewers.
     */
    public ViewToBitmapb toNomedia() {
        setFileExtension(EXTENSION_NOMEDIA);
        return this;
    }


    /**
     * Sets the name of the image file.
     * <p>If not set manually, the files will have a timestamp from {@link System#currentTimeMillis()} as file name.</p>
     */
    public ViewToBitmapb setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }


    /**
     * The root directory is already set to {@link Environment#getExternalStorageDirectory()} and shall not be included in the path String.
     * Directories that don't already exist will be automatically created.
     * @param directoryPath path to where the images will be saved. If not set, the default path {@link Environment#DIRECTORY_PICTURES} will be used.
     */
    public ViewToBitmapb setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        return this;
    }

    /**
     * sets a listener that returns a boolean value for whether the image has been successfully saved or not and a String for the saved image's path.
     */
    public ViewToBitmapb setOnSaveResultListener(OnSaveResultListener onSaveResultListener) {
        this.onSaveResultListener = onSaveResultListener;
        this.handler = new Handler(Looper.myLooper());
        return this;
    }


    public void save() {
        if (Utils.isExternalStorageReady() && Utils.isPermissionGranted(getAppContext())) {
            AsyncSaveImage asyncSaveBitmap = new AsyncSaveImage(getAppContext());
            asyncSaveBitmap.execute();
        }
    }

    //----------------------- PUBLIC METHOD END -------------------------


    private Context getAppContext() {
        Context context = null;
        if (view != null) {
            context = view.getContext().getApplicationContext();
        }
        return context;
    }

    private void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }


    private String getFileExtension() {
        return fileExtension;
    }


    private String getDirectoryPath() {
        if (directoryPath == null || directoryPath.isEmpty()) {
            return Environment.DIRECTORY_PICTURES;
        } else {
            return directoryPath;
        }
    }


    private String getFileName() {
        if (fileName == null || fileName.isEmpty()) {
            return String.valueOf(System.currentTimeMillis() + getFileExtension());
        } else {
            return fileName + getFileExtension();
        }
    }


    private Bitmap getBitmap() {
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

    private void notifyListener(final boolean isSaved, final String path) {
        if (onSaveResultListener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onSaveResultListener.onSaveResult(isSaved, path);
                }
            });
        }
    }


    private class AsyncSaveImage extends AsyncTask<Void, Void, Void> implements MediaScannerConnection.OnScanCompletedListener {
        private Context context;

        public AsyncSaveImage(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            File directoryFile = new File(Environment.getExternalStorageDirectory(), getDirectoryPath());
            directoryFile.mkdirs();
            File imageFile = new File(directoryFile, getFileName());
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile))) {
                if (fileExtension.equals(EXTENSION_JPG)) {
                    getBitmap().compress(Bitmap.CompressFormat.JPEG, jpgQuality, out);
                } else if (fileExtension.equals(EXTENSION_PNG)) {
                    getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                } else {
                    getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out);
                }

            } catch (Exception e) {
                e.printStackTrace();
                notifyListener(false, null);
            }

            MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, this);
            return null;
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            if (uri != null && path != null) {
                notifyListener(true, path);
                Log.i(TAG, "PATH: " + path);
                Log.i(TAG, "URI: " + uri);
            } else {
                notifyListener(false, null);
            }
        }
    }
}

