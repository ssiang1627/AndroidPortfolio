package com.siang.androidportfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaListActivity extends AppCompatActivity {
    private static final String TAG="MediaList";
    private ListView lvMedia;
    private MediaListAdapter adapter;
    private ArrayList<MediaItem> mediaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Picture List");

        lvMedia = (ListView) findViewById(R.id.lvMedia);

        //get mediaItems from previous activity
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        mediaItems = (ArrayList<MediaItem>) bundle.getSerializable("mediaItems");

        adapter = new MediaListAdapter(MediaListActivity.this, mediaItems);
        lvMedia.setAdapter(adapter);

        lvMedia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MediaListActivity.this, "file size: "+ mediaItems.get(position).getSize(), Toast.LENGTH_SHORT).show();
            }
        });

        //prepare the bitmap asynchronously and update the list view in main UI Thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper()); //Use the main UI Thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (MediaItem mediaItem: mediaItems){
                    mediaItem.prepareBitmap();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            lvMedia.invalidateViews();
                        }
                    });
                }
            }
        });
    }
}