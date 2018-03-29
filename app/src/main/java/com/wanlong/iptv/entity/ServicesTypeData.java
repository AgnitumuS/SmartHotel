package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/3/19. 09:08
 * mail:lingchen52@foxmail.com
 */
public class ServicesTypeData implements Serializable {


    private List<String> serviceType;

    public List<String> getServiceType() {
        return serviceType;
    }

    public void setServiceType(List<String> serviceType) {
        this.serviceType = serviceType;
    }
}
