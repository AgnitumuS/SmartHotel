package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/1/27. 14:53
 * mail:lingchen52@foxmail.com
 */
public class LiveListData implements Serializable {

    /**
     * name : CCTV-1综合
     * type : cctv
     * icon : null
     * source : null
     */

    private String name;
    private String type;
    private String icon;
    private String source;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
