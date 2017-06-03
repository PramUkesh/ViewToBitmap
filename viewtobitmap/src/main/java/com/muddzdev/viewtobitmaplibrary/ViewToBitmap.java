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
 *
 * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
 *
 */

@Deprecated
public class ViewToBitmap {

    private static final String TAG = "ViewToBitmap";
    private static final String EXTENSION_PNG = ".png";
    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_NOMEDIA = ".nomedia";
    private static final int JPG_MAX_QUALITY = 100;
    private View view;
    private int jpgQuality;
    private String fileName, directoryPath;
    private boolean saveAsPNG, saveAsNomedia;
    private Handler handler;
    private Runnable runnable;


    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public ViewToBitmap() {
    }

    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public ViewToBitmap(@NonNull View view) {
        this.view = view;
    }

    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public ViewToBitmap(@Nullable View view, @Nullable String directoryPath) {
        this.view = view;
        this.directoryPath = directoryPath;
    }


    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public ViewToBitmap(@Nullable View view, @Nullable String directoryPath, @Nullable String fileName) {
        this.view = view;
        this.directoryPath = directoryPath;
        this.fileName = fileName;
    }


    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public void saveAsNomedia() {
        saveAsNomedia = true;
    }
    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public void saveAsPNG() {
        saveAsPNG = true;
    }

    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public void setJpgQuality(int jpgQuality) {
        this.jpgQuality = jpgQuality;
    }


    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }


    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public void setView(View view) {
        this.view = view;
    }

    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public void setOnSaveResultListener() {
        this.handler = new Handler(Looper.myLooper());
    }


    private Context getAppContext() {
        Context context = null;
        if (view != null) {
            context = view.getContext().getApplicationContext();
        }

        return context;
    }

    private String getDirectoryPath() {
        if (directoryPath == null || directoryPath.isEmpty()) {
            return Environment.DIRECTORY_PICTURES;
        } else {
            return directoryPath;
        }
    }


    private int getJpgQuality() {
        if (jpgQuality == 0) {
            return JPG_MAX_QUALITY;
        } else {
            return jpgQuality;
        }
    }


    private void responseListener(final boolean isSaved, final String path) {

        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }

        runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        handler.post(runnable);

    }


    private String getFileName() {
        if (fileName == null || fileName.isEmpty()) {
            return String.valueOf(System.currentTimeMillis() + getFileExtension());
        } else {
            return fileName + getFileExtension();
        }
    }


    private String getFileExtension() {
        if (saveAsPNG) {
            return EXTENSION_PNG;
        } else if (saveAsNomedia) {
            return EXTENSION_NOMEDIA;
        } else {
            return EXTENSION_JPG;
        }
    }


    //Returns the passed View/ViewGroup as a Bitmap object
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
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    //We need to check for writing permission before we execute any saving operation
    private boolean isPermissionGranted() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(getAppContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * * This library is deprecated! Use the new Viewshot: https://github.com/Muddz/Viewshot
     */
    @Deprecated
    public void saveImage() {
        if (isExternalStorageReady() && isPermissionGranted()) {
            AsyncSaveBitmap asyncSaveBitmap = new AsyncSaveBitmap(getAppContext());
            asyncSaveBitmap.execute();
        } else {
            Log.d(TAG, "Make sure external storage is available and permission WRITE_EXTERNAL_STORAGE is granted");
        }
    }


    private class AsyncSaveBitmap extends AsyncTask<Void, Void, Void> implements MediaScannerConnection.OnScanCompletedListener {
        private Context context;
        private File directoryFile, imageFile;

        public AsyncSaveBitmap(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            directoryFile = new File(Environment.getExternalStorageDirectory(), getDirectoryPath());
            directoryFile.mkdirs();
            imageFile = new File(directoryFile, getFileName());
            OutputStream out = null;

            try {
                out = new BufferedOutputStream(new FileOutputStream(imageFile));
                if (saveAsPNG) {
                    viewToBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
                } else {
                    viewToBitmap().compress(Bitmap.CompressFormat.JPEG, getJpgQuality(), out);
                }

            } catch (IOException e) {
                e.printStackTrace();
                responseListener(false, null);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        responseListener(false, null);
                        e.printStackTrace();
                    }
                }

                MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, this);
            }

            return null;
        }


        @Override
        public void onScanCompleted(String path, Uri uri) {
            if (uri != null && path != null) {
                responseListener(true, path);
                Log.i(TAG, "PATH: " + path);
                Log.i(TAG, "URI: " + uri);

            } else {
                responseListener(false, null);
            }

        }
    }
}

