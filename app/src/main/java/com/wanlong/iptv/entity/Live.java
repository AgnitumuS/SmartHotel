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
     * category : ["央视","卫视"]
     * playlist : [{"category":"央视","channel_number":"dSUSVPzffl","service_name":"cctv1","url":"udp://224.0.3.120:10119","icon":"http://192.168.1.166:80/stream/live/CCTV1.png","live_package":" test","type":"live","program_num":"1"},{"category":"卫视","channel_number":"2rGoKqb1Cm","service_name":"cctv2","url":"udp://224.0.3.121:10120","icon":"","live_package":" vip","type":"live","program_num":"2"},{"category":"央视","channel_number":"UzJbxk6Eh5","service_name":"cctv3","url":"http://192.168.1.231/earth1.mp4","icon":"http://192.168.1.166:80/stream/live/CCTV1.png","live_package":" test","type":"live","program_num":"3"},{"category":"卫视","channel_number":"uf5Bv7DxHt","service_name":"cctv10","url":"udp://224.0.3.121:10121","icon":"http://192.168.1.166:80/stream/live/CCTV1.png","live_package":" test","type":"live","program_num":"10"},{"category":"央视","channel_number":"DhgwhtILHI","service_name":"cctv11","url":"http://192.168.1.231/vod/file-list.m3u8","icon":"http://192.168.1.166:80/stream/live/QQ图片20160104133544.png","live_package":" test,vip","type":"live","program_num":"11"},{"category":"央视","channel_number":"tgDvUm8e7z","service_name":"四川1","url":"udp://224.0.1.6:1235","icon":"http://192.168.1.166:80/stream/live/QQ图片20151110105126.png","live_package":" test","type":"live","program_num":"15"},{"category":"卫视","channel_number":"fz6kgP2eoB","service_name":"cctv12","url":"https://abclive1-lh.akamaihd.net/i/abc_live02@423396/master.m3u8","icon":"","live_package":" vip","type":"live","program_num":"100"}]
     */

    private String code;
    private List<String> category;
    private List<PlaylistBean> playlist;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
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
         * category : 央视
         * channel_number : dSUSVPzffl
         * service_name : cctv1
         * url : udp://224.0.3.120:10119
         * icon : http://192.168.1.166:80/stream/live/CCTV1.png
         * live_package :  test
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
