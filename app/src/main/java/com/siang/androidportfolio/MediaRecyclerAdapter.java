package com.siang.androidportfolio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaRecyclerAdapter extends RecyclerView.Adapter<MediaRecyclerAdapter.MediaViewHolder> {
    private ArrayList<MediaItem> mediaItems;
    private Context context;

    public MediaRecyclerAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
        this.context = context;
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
        holder.tvTitle.setText(mediaItems.get(position).getTitle());
        holder.tvDescription.setText(mediaItems.get(position).getDescription());
        holder.ivThumbnail.setImageBitmap(mediaItems.get(position).getBitmap());
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
}
