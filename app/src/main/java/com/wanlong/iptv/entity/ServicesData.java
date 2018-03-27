package com.wanlong.iptv.entity;

import java.io.Serializable;

/**
 * Created by lingchen on 2018/3/19. 09:08
 * mail:lingchen52@foxmail.com
 */
public class ServicesData implements Serializable {

    /**
     * serviceType : 健身,桑拿
     */

    private String serviceType;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
