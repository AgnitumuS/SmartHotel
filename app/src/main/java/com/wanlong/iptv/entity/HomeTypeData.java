package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/2/5. 11:29
 * mail:lingchen52@foxmail.com
 */
public class HomeTypeData implements Serializable{

    /**
     * adsType : other,image,home
     */

    private String adsType;

    public String getAdsType() {
        return adsType;
    }

    public void setAdsType(String adsType) {
        this.adsType = adsType;
    }
}
