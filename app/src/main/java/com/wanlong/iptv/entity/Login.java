package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/4/3. 11:34
 * mail:lingchen52@foxmail.com
 */
public class Login implements Serializable {

    /**
     * code : 1
     * group : test,vip
     * stb_name : ljj
     * Owner_Group : 上海市宝山监狱
     * Owner_Group_display : on
     */

    private String code;
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
