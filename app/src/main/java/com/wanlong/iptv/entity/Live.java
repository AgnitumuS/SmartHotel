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
     * category : 直播,自办
     * playlist : [{"category":"直播","channel_number":"DFRKk1LSdz","service_name":"cctv1","url":"udp://224.0.3.120:10119","icon":"http://192.168.1.166:80/stream/live/CCTV1.png","live_package":" 1#,2#,test,3#,4#","type":"live","program_num":"1"},{"category":"直播","channel_number":"KE4KZJtuEM","service_name":"cctv2","url":"udp://224.0.3.121:10120","icon":"http://192.168.1.166:80/stream/live/CCTV1.png","live_package":" 1#,2#,3#,4#","type":"live","program_num":"2"},{"category":"直播","channel_number":"L9UXAoQY7I","service_name":"cctv3","url":"http://192.168.1.231/earth1.mp4","icon":"http://192.168.1.166:80/stream/live/CCTV1.png","live_package":" 1#,2#,4#","type":"live","program_num":"3"}]
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
         * category : 直播
         * channel_number : DFRKk1LSdz
         * service_name : cctv1
         * url : udp://224.0.3.120:10119
         * icon : http://192.168.1.166:80/stream/live/CCTV1.png
         * live_package :  1#,2#,test,3#,4#
         * type : live
         * program_num : 1
         */

        private String category;
        private String channel_number;
        private String service_name;
        private String url;
        private String icon;
        private String live_package;
        private String type;
        private String program_num;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

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

        public String getProgram_num() {
            return program_num;
        }

        public void setProgram_num(String program_num) {
            this.program_num = program_num;
        }
    }
}
