package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/1/27. 14:54
 * mail:lingchen52@foxmail.com
 */
public class VodListData implements Serializable {

    /**
     * title : 神秘巨星
     * genre : 剧情 / 歌舞
     * area : 印度
     * source : http://v.qq.com/x/cover/esb9yas9hjdbadw/c00250ehei6.html?ptag=360kan.movie.pay
     */

    private String title;
    private String genre;
    private String area;
    private String source;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
