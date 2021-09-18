package com.siang.androidportfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaRecyclerActivity extends AppCompatActivity {
    private static final String TAG = "MediaRecyclerActivity";
    private RecyclerView rvMedia;
    private MediaRecyclerAdapter adapter;
    private ArrayList<MediaItem> mediaItems;
    private ArrayList<MediaItem> shownMediaItems;
    private int lastNumber = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_recycler);
        rvMedia = findViewById(R.id.rvMedia);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        mediaItems = (ArrayList<MediaItem>) bundle.getSerializable("mediaItems");
        shownMediaItems = new ArrayList<>(mediaItems.subList(0,lastNumber));

        adapter = new MediaRecyclerAdapter(this, shownMediaItems);
        rvMedia.setAdapter(adapter);
        rvMedia.setLayoutManager(new LinearLayoutManager(this));

        rvMedia.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rvMedia.canScrollVertically(1)) {
                    if ( lastNumber < mediaItems.size() && lastNumber+10 > mediaItems.size()){
                        shownMediaItems.addAll(mediaItems.subList(lastNumber, mediaItems.size()));
                    }else if (lastNumber < mediaItems.size()){
                        shownMediaItems.addAll(mediaItems.subList(lastNumber, lastNumber+10));
                    }
                    lastNumber = lastNumber+10;
                    Log.d(TAG, "shownMediaItems:" +shownMediaItems.size());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}