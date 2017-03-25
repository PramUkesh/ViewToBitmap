package com.muddzdev.viewtobitmap;

import android.Manifest;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.muddzdev.viewtobitmaplibrary.OnBitmapSaveListener;
import com.muddzdev.viewtobitmaplibrary.ViewToBitmap;


public class MainActivity extends AppCompatActivity implements OnBitmapSaveListener {

    private RelativeLayout quotePicture;
    private Toolbar toolbar;
    private PermissionRequester permissionRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionRequester = new PermissionRequester(this, this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionRequester.request();

        quotePicture = (RelativeLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    public void saveToGallery(View v) {

        String directoryPathExample1 = "/MyApp/Media/Photos";
        String directoryPathExample2 = Environment.DIRECTORY_PICTURES + "/MyApp";

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(60);

        ViewToBitmap image = new ViewToBitmap(quotePicture);
        image.setOnBitmapSaveListener(this);
        image.saveImage();
    }


    @Override
    public void onBitmapSaved(final boolean isSaved, final String path) {
        if (isSaved) {
            Toast.makeText(this, "Bitmap Saved at; " + path, Toast.LENGTH_SHORT).show();
        }
    }
}

