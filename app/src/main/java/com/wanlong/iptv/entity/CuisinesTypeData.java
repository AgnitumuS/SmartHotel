package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/1/30. 15:48
 * mail:lingchen52@foxmail.com
 */
public class CuisinesTypeData implements Serializable {

    private List<String> cuisineType;

    public List<String> getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(List<String> cuisineType) {
        this.cuisineType = cuisineType;
    }
}
