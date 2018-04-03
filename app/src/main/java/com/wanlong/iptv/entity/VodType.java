package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/4/3. 16:37
 * mail:lingchen52@foxmail.com
 */
public class VodType implements Serializable {

    /**
     * code : 0
     * category : ["Action movie","sci-fi movie","Romance","comedy"]
     */

    private String code;
    private List<String> category;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
