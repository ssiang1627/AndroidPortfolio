package com.siang.androidportfolio;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaRecyclerAdapter extends RecyclerView.Adapter<MediaRecyclerAdapter.MediaViewHolder> {
    private ArrayList<MediaItem> mediaItems;
    private Context context;
    ExecutorService executorService;


    public MediaRecyclerAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
        this.context = context;
        executorService = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @NotNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_list, parent, false);
        MediaViewHolder holder = new MediaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MediaViewHolder holder, int position) {
        MediaItem mediaItem = mediaItems.get(position);
        holder.tvTitle.setText(mediaItem.getTitle());
        holder.tvDescription.setText(mediaItem.getDescription());
        Bitmap bitmap = mediaItem.getBitmap();
        if (bitmap == null){
            updateImageAsync(mediaItem, holder.ivThumbnail, position);
        }else{
            holder.ivThumbnail.setImageBitmap(bitmap);
        }
//        Glide.with(holder.ivThumbnail).load(mediaItem.getPath()).into(holder.ivThumbnail);
        holder.layoutMediaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Size:" +mediaItems.get(position).getSize(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvDescription;
        ImageView ivThumbnail;
        LinearLayout layoutMediaItem;
        public MediaViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            layoutMediaItem = itemView.findViewById(R.id.layout_media_item);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }

    public void updateImageAsync(MediaItem mediaItem, ImageView imageView, int position){
        Handler handler = new Handler(Looper.getMainLooper()); //Use the main UI Thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mediaItem.prepareBitmap();
                Log.d("updateImageAsync", "position: "+ position + "/ thread: " +Thread.currentThread().getName());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(mediaItem.getBitmap());
                    }
                });
            }
        });
    }
}
