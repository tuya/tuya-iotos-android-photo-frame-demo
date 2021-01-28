package com.tuya.smart.tuyasmart_photoframe.entity;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Date class represents media files(image/video).
 * @author vico
 * @date 1/20/21
 */
public class MediaFile {

    private String name;
    private String path;
    private boolean internal;
    private Date modifyTime;
    private String type;
    private Bitmap bitmap;
    private long duration;

    public MediaFile(String name, String path, boolean internal, Date modifyTime, String type, Bitmap bitmap,long duration) {
        this.name = name;
        this.path = path;
        this.internal = internal;
        this.modifyTime = modifyTime;
        this.type = type;
        this.bitmap = bitmap;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
