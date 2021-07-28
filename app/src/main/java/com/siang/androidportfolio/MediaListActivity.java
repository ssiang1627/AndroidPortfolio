package com.siang.androidportfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class MediaListActivity extends AppCompatActivity {
    private static final String TAG="MediaList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        ArrayList<MediaItem> mediaItems = (ArrayList<MediaItem>) bundle.getSerializable("mediaItems");
        Log.d(TAG, "mediaItems 0: " + mediaItems.get(0).getDisplayName());

        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Picture List");


    }
}