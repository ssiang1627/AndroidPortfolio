package com.siang.androidportfolio.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.siang.androidportfolio.DialogActivity;
import com.siang.androidportfolio.R;
import com.siang.androidportfolio.model.SFNews;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SFNewsAdapter extends RecyclerView.Adapter<SFNewsAdapter.ViewHolder>{
    private final Context context;
    ArrayList<SFNews> sfNewsArrayList;

    public SFNewsAdapter(Context context, ArrayList<SFNews> sfNewsArrayList) {
        this.context = context;
        this.sfNewsArrayList = sfNewsArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public SFNewsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new SFNewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SFNewsAdapter.ViewHolder holder, int position) {
        SFNews sfNews = sfNewsArrayList.get(position);
        holder.tvTitle.setText(sfNews.getTitle());
        Glide.with(context)
                .load(sfNews.getImageUrl())
                .into(holder.ivCover);
        holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("CAMPAIGN_NEWS_ID", sfNews.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sfNewsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivCover;
        private final TextView tvTitle;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
