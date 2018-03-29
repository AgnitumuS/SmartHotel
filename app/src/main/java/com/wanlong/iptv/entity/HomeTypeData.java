package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/2/5. 11:29
 * mail:lingchen52@foxmail.com
 */
public class HomeTypeData implements Serializable{


    private List<String> adsType;

    public List<String> getAdsType() {
        return adsType;
    }

    public void setAdsType(List<String> adsType) {
        this.adsType = adsType;
    }
}
