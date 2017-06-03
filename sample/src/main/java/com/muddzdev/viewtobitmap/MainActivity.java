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
import com.muddzdev.viewtobitmaplibrary.Viewshot.OnSaveResultListener;


public class MainActivity extends AppCompatActivity{

    private RelativeLayout quotePicture;
    private Toolbar toolbar;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionRequester permissionRequester = new PermissionRequester(this, this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionRequester.request();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        quotePicture = (RelativeLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


}

