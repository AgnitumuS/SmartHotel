package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/3/27. 10:21
 * mail:lingchen52@foxmail.com
 */
public class LoginData implements Serializable {

    /**
     * code : 200
     */

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
