package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/1/27. 14:53
 * mail:lingchen52@foxmail.com
 */
public class LiveTypeData implements Serializable {
//    {
//        "channelType": [
//                  "cctv",
//                "satellite",
//                "测试类型"
//    ]
//    }
    private List<String> channelType;

    public List<String> getChannelType() {
        return channelType;
    }

    public void setChannelType(List<String> channelType) {
        this.channelType = channelType;
    }
}
