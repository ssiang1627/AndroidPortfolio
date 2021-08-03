package com.siang.androidportfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaListActivity extends AppCompatActivity {
    private static final String TAG="MediaList";
    private ListView lvMedia;
    private MediaListAdapter adapter;
    private ArrayList<MediaItem> mediaItems;
    private ArrayList<MediaItem> shownMediaItems;
    private int lastNumber = 10;

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
        shownMediaItems = new ArrayList<>(mediaItems.subList(0,lastNumber));

        adapter = new MediaListAdapter(MediaListActivity.this, shownMediaItems);
        lvMedia.setAdapter(adapter);

        lvMedia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MediaListActivity.this, "file size: "+ mediaItems.get(position).getSize(), Toast.LENGTH_SHORT).show();
            }
        });
        updateImageAsync(shownMediaItems);
        lvMedia.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lvMedia.getLastVisiblePosition() >= (adapter.getCount() - 1)) {
                    Log.d(TAG,"hit the bottom "+
                            "/lastVisible: "+lvMedia.getLastVisiblePosition()+
                            "/adapter: "+adapter.getCount()
                    );
                    synchronized (shownMediaItems){
                        if ( lastNumber < mediaItems.size() && lastNumber+10 > mediaItems.size()){
                            shownMediaItems.addAll(mediaItems.subList(lastNumber, mediaItems.size()));
                        }else if (lastNumber < mediaItems.size()){
                            shownMediaItems.addAll(mediaItems.subList(lastNumber, lastNumber+10));
                        }
                    }
                    lastNumber = lastNumber+10;
                    Log.d(TAG, "shownMediaItems:" +shownMediaItems.size());
                    adapter.notifyDataSetChanged();
                    updateImageAsync(shownMediaItems);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    //prepare the bitmap asynchronously and update the list view in main UI Thread
    public void updateImageAsync(List<MediaItem> mediaItemList){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper()); //Use the main UI Thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (mediaItemList){
                    for (MediaItem mediaItem: mediaItemList){
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
            }
        });
    }
}