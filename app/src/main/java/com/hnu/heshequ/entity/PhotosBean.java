package com.hnu.heshequ.entity;

import java.io.Serializable;

/**
 * Created by dev06 on 2018/6/7.
 */
public class PhotosBean implements Serializable {
    private String photoId;
    private int bzId;

    public int getId() {
        return id;
    }

    private int id;

    public void setBzId(int bzId) {
        this.bzId = bzId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public int getBzId() {
        return bzId;
    }
}
