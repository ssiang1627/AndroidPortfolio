package com.siang.androidportfolio;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MediaListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MediaItem> mediaItems;

    public MediaListAdapter(Context context, ArrayList<MediaItem> mediaItemArrayList) {
        this.context = context;
        this.mediaItems = mediaItemArrayList;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_media_list, null);
        TextView tvDisplayName = (TextView) view.findViewById(R.id.tvDisplayName);
        TextView tvPath = (TextView) view.findViewById(R.id.tvPath);
        ImageView ivThumbnail = (ImageView) view.findViewById(R.id.ivThumbnail);

        //set content for view
        tvDisplayName.setText(mediaItems.get(position).getDisplayName());
        tvPath.setText(mediaItems.get(position).getPath());
//        Glide.with(view.getContext())
//                .asBitmap()
//                .load(mediaItems.get(position).getPath())
//                .thumbnail(0.1f)
//                .into(ivThumbnail);
        ivThumbnail.setImageBitmap(mediaItems.get(position).getBitmap());
        return view;
    }
}
