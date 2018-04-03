package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/4/3. 11:49
 * mail:lingchen52@foxmail.com
 */
public class Live implements Serializable {

    /**
     * code : 0
     * category : live,live1
     * playlist : [{"channel_number":"t9MXW4ccqp","service_name":"xxx","url":"http://127.0.0.1:8080/live/t9MXW4ccqp.m3u8","icon":"","category":" live","live_package":" ","type":"live"}]
     */

    private String code;
    private String category;
    private List<PlaylistBean> playlist;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<PlaylistBean> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<PlaylistBean> playlist) {
        this.playlist = playlist;
    }

    public static class PlaylistBean {
        /**
         * channel_number : t9MXW4ccqp
         * service_name : xxx
         * url : http://127.0.0.1:8080/live/t9MXW4ccqp.m3u8
         * icon :
         * category :  live
         * live_package :
         * type : live
         */

        private String channel_number;
        private String service_name;
        private String url;
        private String icon;
        private String category;
        private String live_package;
        private String type;

        public String getChannel_number() {
            return channel_number;
        }

        public void setChannel_number(String channel_number) {
            this.channel_number = channel_number;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getLive_package() {
            return live_package;
        }

        public void setLive_package(String live_package) {
            this.live_package = live_package;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
