package com.siang.androidportfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.siang.androidportfolio.utils.PermissionHelper;
import com.siang.androidportfolio.view.NewsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnQRScan;
    private Button btnScanPic;
    private Button btnPicList;
    private Button btnNewsList;
    private TextView tvToolbarTitle;
    private Toolbar toolbar;
    private static final String TAG_MEDIA_LIST = "MediaList";
    private static final String TAG_FCM_TOKEN = "FCMToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnQRScan = findViewById(R.id.btnQRScan);
        btnScanPic = findViewById(R.id.btnScanPic);
        btnPicList = findViewById(R.id.btnPicList);
        btnNewsList = findViewById(R.id.btnNewsList);
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

        btnNewsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewsActivity();
            }
        });

        getFCMToken();
        initNotificationChannel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("campaign", MODE_PRIVATE);
        boolean displayDialog = pref.getBoolean("DISPLAY", true);
        long campaignNewsId = pref.getLong("CAMPAIGN_NEWS_ID", 0);
        if (!displayDialog){
            startDialogActivity(campaignNewsId);
        }
    }

    private void startNewsActivity() {
        Intent newsIntent = new Intent(MainActivity.this, NewsActivity.class);
        startActivity(newsIntent);
    }

    public void startQRScanActivity(){
        Intent qrIntent = new Intent(MainActivity.this, QRScanActivity.class );
        startActivity(qrIntent);
    }

    public void startScanPicActivity(){
        Intent scanPicIntent = new Intent(MainActivity.this, ScanPicActivity.class);
        startActivity(scanPicIntent);
    }

    public void startDialogActivity(long campaignNewsId){
        Intent intent = new Intent(MainActivity.this, DialogActivity.class);
        intent.putExtra("CAMPAIGN_NEWS_ID", campaignNewsId);
        startActivity(intent);
    }

    public void startPicListActivity(){
        PermissionHelper.requestPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (PermissionHelper.isPermissionGranted(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Log.d(TAG_MEDIA_LIST, "all images: "+getAllShownImagesPath(MainActivity.this).size());
            ArrayList<MediaItem> mediaItems = getAllShownImagesPath(MainActivity.this);

            Intent picListIntent = new Intent(MainActivity.this, MediaRecyclerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("mediaItems",mediaItems);
            picListIntent.putExtras(bundle);
            startActivity(picListIntent);
        }
    }

    private ArrayList<MediaItem> getAllShownImagesPath(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_display_name, column_index_size;

        ArrayList<MediaItem> listOfAllImages = new ArrayList<MediaItem>();
        String absolutePathOfImage = null;
        String title = null;
        Long fileSize = null;

        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.SIZE};
        String selection = MediaStore.Images.Media.DATA + " like ? ";
        String[] selectionArgs = new String[]{"%DCIM/LINE%"};
        cursor = context.getContentResolver().query(uri, projection, selection,
                selectionArgs, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_display_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        column_index_size = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            title = cursor.getString(column_index_display_name);
            fileSize = cursor.getLong(column_index_size);

            MediaItem mediaItem = new MediaItem(title, absolutePathOfImage);
            mediaItem.setPath(absolutePathOfImage);
            mediaItem.setSize(fileSize);
            //Use img from internet
            mediaItem.setImgUrl("https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png");

            listOfAllImages.add(mediaItem);
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

    private void initNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String defaultChannelId  = getString(R.string.notification_channel_id_default);
            String defaultChannelName = getString(R.string.notification_channel_name_default);
            String importantChannelId  = getString(R.string.notification_channel_id_important);
            String importantChannelName = getString(R.string.notification_channel_name_important);

            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(defaultChannelId,
                    defaultChannelName, NotificationManager.IMPORTANCE_DEFAULT));
            notificationManager.createNotificationChannel(new NotificationChannel(importantChannelId,
                    importantChannelName, NotificationManager.IMPORTANCE_HIGH));
        }
    }

    private void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if (!task.isSuccessful()){
                    Log.w(TAG_FCM_TOKEN, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.i(TAG_FCM_TOKEN, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}