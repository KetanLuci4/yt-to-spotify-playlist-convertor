package com.resh.coders.springmvc.models;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

@JsonIgnoreProperties(ignoreUnknown = true)
public class searchResult {
    String playlistId;
    String title;
    String author;
    String authorUrl;
    String playlistThumbnail;
    String videoCount;
    videos[] videos;

    public searchResult(){}

    public searchResult(String playlistId, String title, String author, String authorUrl, String playlistThumbnail,
            String videoCount) {
        this.playlistId = playlistId;
        this.title = title;
        this.author = author;
        this.authorUrl = authorUrl;
        this.playlistThumbnail = playlistThumbnail;
        this.videoCount = videoCount;
    }

    public String getPlaylistId() {
        return playlistId;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getAuthorUrl() {
        return authorUrl;
    }
    public String getPlaylistThumbnail() {
        return playlistThumbnail;
    }
    public String getVideoCount() {
        return videoCount;
    }
    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }
    public void setPlaylistThumbnail(String playlistThumbnail) {
        this.playlistThumbnail = playlistThumbnail;
    }
    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }


    
    public static searchResult fromJson(String json, boolean mapAllFields) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !mapAllFields);
        if (!mapAllFields) {
            objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
                @Override
                public boolean hasIgnoreMarker(AnnotatedMember a) {
                    return !mapAllFields && super.hasIgnoreMarker(a);
                }
            });
        }
        return objectMapper.readValue(json, searchResult.class);
    }

    public videos[] getVideo() {
        return videos;
    }

    public void setVideo(videos[] videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "searchResult [playlistId=" + playlistId + ", title=" + title + ", author=" + author + ", authorUrl="
                + authorUrl + ", playlistThumbnail=" + playlistThumbnail + ", videoCount=" + videoCount + ", video="
                + Arrays.toString(videos) + "]";
    }
}