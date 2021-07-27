package com.siang.androidportfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.siang.androidportfolio.utils.PermissionHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnQRScan;
    private Button btnScanPic;
    private Button btnPicList;
    private TextView tvToolbarTitle;
    private Toolbar toolbar;
    private static final String PIC_LIST_TAG = "PicList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnQRScan = findViewById(R.id.btnQRScan);
        btnScanPic = findViewById(R.id.btnScanPic);
        btnPicList = findViewById(R.id.btnPicList);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText("Android Portfolio");

        btnQRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanActivity();
            }
        });

        btnScanPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanPicActivity();
            }
        });

        btnPicList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPicListActivity();
            }
        });
    }

    public void startQRScanActivity(){
        Intent qrIntent = new Intent(MainActivity.this, QRScanActivity.class );
        MainActivity.this.startActivity(qrIntent);
    }

    public void startScanPicActivity(){
        Intent scanPicIntent = new Intent(MainActivity.this, ScanPicActivity.class);
        MainActivity.this.startActivity(scanPicIntent);
    }

    public void startPicListActivity(){
        PermissionHelper.requestPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (PermissionHelper.isPermissionGranted(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Log.d(PIC_LIST_TAG, "all images: "+getAllShownImagesPath(MainActivity.this).size());
            Intent picListIntent = new Intent(MainActivity.this, PicListActivity.class);
            MainActivity.this.startActivity(picListIntent);
        }
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_display_name, column_index_size;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        String displayName = null;
        Long fileSize = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.SIZE};
        String selection = MediaStore.Images.Media.DATA + " like ? ";
        String[] selectionArgs = new String[]{"%DCIM/LINE%"};
        cursor = activity.getContentResolver().query(uri, projection, selection,
                selectionArgs, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_display_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        column_index_size = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            displayName = cursor.getString(column_index_display_name);
            fileSize = cursor.getLong(column_index_size);

            Log.d(PIC_LIST_TAG, "image: path:"+absolutePathOfImage + " / name:" + displayName + " / size:"+ fileSize/1024 +"kb");

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionHelper.READ_EXTERNAL_STORAGE_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startPicListActivity();
            }
        }
    }
}