package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/2/5. 11:29
 * mail:lingchen52@foxmail.com
 */
public class HomeData implements Serializable{

    /**
     * name : 王老吉
     * description : 一年卖出十亿灌
     * imgSrc : http://www.wlj.com.cn/UploadFile/ads/060cde09-95d1-4380-b043-2e236ba2ed27.jpg
     * videoSrc : null
     * location : start
     */

    private String name;
    private String description;
    private String imgSrc;
    private Object videoSrc;
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public Object getVideoSrc() {
        return videoSrc;
    }

    public void setVideoSrc(Object videoSrc) {
        this.videoSrc = videoSrc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
