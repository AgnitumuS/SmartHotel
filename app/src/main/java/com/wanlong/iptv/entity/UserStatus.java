package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/4/12. 11:33
 * mail:lingchen52@foxmail.com
 */
public class UserStatus implements Serializable {

    /**
     * code : -4
     * expired_time : 2000-01-01
     * vod_expired_time : 2000-01-01
     */

    private String code;
    private String expired_time;
    private String vod_expired_time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(String expired_time) {
        this.expired_time = expired_time;
    }

    public String getVod_expired_time() {
        return vod_expired_time;
    }

    public void setVod_expired_time(String vod_expired_time) {
        this.vod_expired_time = vod_expired_time;
    }
}
