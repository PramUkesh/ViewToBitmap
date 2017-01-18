package com.muddzdev.viewtobitmap;

import android.Manifest;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.muddzdev.viewtobitmaplibrary.ViewToBitmap;
import com.muddzdev.viewtobitmaplibrary.OnBitmapSaveListener;

public class MainActivity extends AppCompatActivity implements OnBitmapSaveListener {

    private RelativeLayout container;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionRequester permissionRequester = new PermissionRequester(this, this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionRequester.request();

        container = (RelativeLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    public void bitmapSave(View v) {

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(60);

        ViewToBitmap toBitmap = new ViewToBitmap(this, container, null, null);
        toBitmap.setOnBitmapSaveListener(this);
        toBitmap.saveToBitmap();
    }


    @Override
    public void onBitmapSaved(final boolean isSaved, final String path) {

        Toast.makeText(MainActivity.this, "Bitmap Saved at; " + path, Toast.LENGTH_SHORT).show();

    }
}
