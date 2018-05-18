package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/4/12. 11:33
 * mail:lingchen52@foxmail.com
 */
public class UserStatus implements Serializable {

    /**
     * code : 1
     * expired_time : 84
     * vod_expired_time : 84
     * group : test,vip
     * stb_name : ljj
     * Owner_Group : 上海市宝山监狱
     * Owner_Group_display : on
     */

    private String code;
    private String expired_time;
    private String vod_expired_time;
    private String group;
    private String stb_name;
    private String Owner_Group;
    private String Owner_Group_display;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStb_name() {
        return stb_name;
    }

    public void setStb_name(String stb_name) {
        this.stb_name = stb_name;
    }

    public String getOwner_Group() {
        return Owner_Group;
    }

    public void setOwner_Group(String Owner_Group) {
        this.Owner_Group = Owner_Group;
    }

    public String getOwner_Group_display() {
        return Owner_Group_display;
    }

    public void setOwner_Group_display(String Owner_Group_display) {
        this.Owner_Group_display = Owner_Group_display;
    }
}
