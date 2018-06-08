package com.wanlong.iptv.entity;

import java.util.List;

/**
 * Created by lingchen on 2018/6/1. 13:50
 * mail:lingchen52@foxmail.com
 */
public class PlayBack {

    /**
     * date : 2018-06-01
     * playlist : [{"channel_number":"1","service_name":"HD Phx info 1","start":"13:30:00","end":"14:00:00","name":"明星妈妈","url":"http://192.168.1.109:8080/live/1.m3u8"},{"channel_number":"1","service_name":"HD Phx info 1","start":"14:00:00","end":"14:20:00","name":"杰米熊","url":"http://192.168.1.109:8080/live/2.m3u8"}]
     */

    private String date;
    private List<PlaylistBean> playlist;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<PlaylistBean> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<PlaylistBean> playlist) {
        this.playlist = playlist;
    }

    public static class PlaylistBean {
        /**
         * channel_number : 1
         * service_name : HD Phx info 1
         * start : 13:30:00
         * end : 14:00:00
         * name : 明星妈妈
         * url : http://192.168.1.109:8080/live/1.m3u8
         */

        private String channel_number;
        private String service_name;
        private String start;
        private String end;
        private String name;
        private String url;

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

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
