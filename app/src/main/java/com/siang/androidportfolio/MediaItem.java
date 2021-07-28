package com.siang.androidportfolio;

import java.io.Serializable;

public class MediaItem implements Serializable {
    private String path;
    private String displayName;
    private Long size;
    public MediaItem() {

    }

    public String getPath() {
        return path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Long getSize() {
        return size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
