package com.muddzdev.viewtobitmap;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.muddzdev.viewtobitmaplibrary.OnSaveResultListener;
import com.muddzdev.viewtobitmaplibrary.ViewToBitmap;


public class MainActivity extends AppCompatActivity implements OnSaveResultListener {

    private RelativeLayout quotePicture;
    private Toolbar toolbar;
    private Vibrator vibrator;
    private ViewToBitmap viewToBitmap;

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

        viewToBitmap = new ViewToBitmap();
        viewToBitmap.setOnSaveResultListener(this);


    }


    public void saveToGallery(View v) {

        String directoryPathExample1 = "/MyApp/Media/Photos";
        String directoryPathExample2 = Environment.DIRECTORY_PICTURES + "/MyApp";


//        viewToBitmap.setDirectoryPath(directoryPathExample1);
        vibrator.vibrate(60);
        viewToBitmap.setView(quotePicture);

        ViewToBitmap viewToBitmap = new ViewToBitmap();
        viewToBitmap.saveAsNomedia();


    }


    @Override
    public void onSaveResult(boolean isSaved, String path) {
        if (isSaved) {
            Toast.makeText(this, "Bitmap Saved at; " + path, Toast.LENGTH_SHORT).show();
        }
    }
}

