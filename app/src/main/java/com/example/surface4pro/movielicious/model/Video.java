/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious.model;

public class Video {

    private String key;
    private String name;
    private String site;
    private String type;

    /**
     * Video constructor
     *
     * @param key  The video's youtube key.
     * @param name Name of the video.
     * @param site Site that is hosting the video.
     * @param type Type of the video.
     */
    public Video(String key, String name, String site, String type) {
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
