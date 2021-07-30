package com.siang.androidportfolio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

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
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        ImageView ivThumbnail = (ImageView) view.findViewById(R.id.ivThumbnail);

        //set content for view
        tvTitle.setText(mediaItems.get(position).getTitle());
        tvDescription.setText(mediaItems.get(position).getDescription());
//        mediaItems.get(position).prepareBitmapForUrl(view.getContext(), "https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png");
        ivThumbnail.setImageBitmap(mediaItems.get(position).getBitmap());
        return view;
    }
}
