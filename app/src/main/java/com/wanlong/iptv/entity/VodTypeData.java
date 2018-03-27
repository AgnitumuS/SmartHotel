package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/3/27. 13:59
 * mail:lingchen52@foxmail.com
 */
public class VodTypeData implements Serializable{

    private List<String> genre;

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }
}
