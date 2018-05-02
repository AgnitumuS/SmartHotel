package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/4/3. 11:34
 * mail:lingchen52@foxmail.com
 */
public class Login implements Serializable {

    /**
     * code : 1
     * group : test
     * stb_name :
     */

    private String code;
    private String group;
    private String stb_name;

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
}
