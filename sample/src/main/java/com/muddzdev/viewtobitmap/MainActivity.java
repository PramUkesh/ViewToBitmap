package com.muddzdev.viewtobitmap;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.muddzdev.viewtobitmaplibrary.OnBitmapSaveListener;
import com.muddzdev.viewtobitmaplibrary.ViewToBitmap;

public class MainActivity extends AppCompatActivity implements OnBitmapSaveListener {

    private RelativeLayout quotePictor;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionRequester permissionRequester = new PermissionRequester(this, this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionRequester.request();

        quotePictor = (RelativeLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    public void saveToGallery(View v) {

        ViewToBitmap image = new ViewToBitmap(quotePictor);
        image.setOnBitmapSaveListener(this);
        image.saveToGallery();


    }


    @Override
    public void onBitmapSaved(final boolean isSaved, final String path) {

        if (isSaved) {
            Toast.makeText(this, "Bitmap Saved at; " + path, Toast.LENGTH_SHORT).show();
        }
    }
}
