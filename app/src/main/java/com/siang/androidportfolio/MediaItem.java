package com.siang.androidportfolio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaItem implements Serializable {
    private String path;
    private String title;
    private Long size;
    private String description;
    private Bitmap bitmap;
    private String imgUrl;

    public MediaItem() {
    }

    public MediaItem(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public Long getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void prepareBitmap(){
        if (bitmap != null){
            return;
        }

        //prepare Bitmap for url
        if (imgUrl != null){
            try {
                URL url = new URL(imgUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                Log.d("MediaItem", "bitmap url");
                return;
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }
        }

        //prepare Bitmap for local file
        if (path != null){
            try {
                File file = new File(path);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
