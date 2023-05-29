package com.resh.coders.springmvc.models;

import java.util.Arrays;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder
public class videos {
    String title;
    String videoId;
    String lengthSeconds;   
    videoThumbnails[] videoThumbnails;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public String getlengthSeconds() {
        return lengthSeconds;
    }
    public void setlengthSeconds(String lengthSeconds) {
        this.lengthSeconds = lengthSeconds;
    }
    public videoThumbnails[] getVideoThumbnails() {
        return videoThumbnails;
    }
    public void setVideoThumbnails(videoThumbnails[] videoThumbnails) {
        this.videoThumbnails = videoThumbnails;
    }
    @Override
    public String toString() {
        return "video [title=" + title + ", videoId=" + videoId + ", lengthSeconds=" + lengthSeconds + ", videoThumbnails="
                + Arrays.toString(videoThumbnails) + "]";
    }
    public videos(String title, String videoId, String lengthSeconds,
            com.resh.coders.springmvc.models.videoThumbnails[] videoThumbnails) {
        this.title = title;
        this.videoId = videoId;
        this.lengthSeconds = lengthSeconds;
        this.videoThumbnails = videoThumbnails;
    }
    public videos() {
    }
    
}