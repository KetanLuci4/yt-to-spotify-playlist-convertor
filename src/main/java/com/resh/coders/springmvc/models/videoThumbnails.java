package com.resh.coders.springmvc.models;

public class videoThumbnails{
    String quality;
    String url;
    String width;
    String height;
    public String getQuality() {
        return quality;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getWidth() {
        return width;
    }
    public void setWidth(String width) {
        this.width = width;
    }
    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
    }
    public videoThumbnails(String quality, String url, String width, String height) {
        this.quality = quality;
        this.url = url;
        this.width = width;
        this.height = height;
    }
    public videoThumbnails() {
    }
    @Override
    public String toString() {
        return "videoThumbnails [quality=" + quality + ", url=" + url + ", width=" + width + ", height=" + height + "]";
    }
    
}