package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hasee on 2017/9/7.
 */

public class PushMSG implements Serializable{

    /**
     * code : 0
     * cut_in : [{"status":"ON","to_area":"","to_user":"","type":"emergency","categoryid":"video","play_path":"http://192.168.1.231/earth1.mp4","play_start_time":"08:00:00","play_end_time":"22:00:00","play_start_day":"2018-01-01","play_end_day":"2018-12-31","font_size":"50","lucency_size":"0","font_color":"000000","place":"bottom","back_color":"000000"},{"status":"ON","to_area":"","to_user":"","type":"timer","categoryid":"video","play_path":"http://192.168.1.231/vod/file-list.m3u8","play_start_time":"08:00:00","play_end_time":"22:00:00","play_start_day":"2018-01-01","play_end_day":"2018-12-31","font_size":"50","lucency_size":"0","font_color":"000000","place":"bottom","back_color":"000000"},{"status":"ON","to_area":"","to_user":"","type":"emergency","categoryid":"text","play_path":"DMM推出新功能啦,欢迎试用","play_start_time":"08:00:00","play_end_time":"22:00:00","play_start_day":"2018-01-01","play_end_day":"2018-12-31","font_size":"50","lucency_size":"0","font_color":"000000","place":"bottom","back_color":"000000"},{"status":"ON","to_area":"","to_user":"","type":"timer","categoryid":"text","play_path":"杭州万隆光电设备股份有限公司成都分公司","play_start_time":"08:00:00","play_end_time":"22:00:00","play_start_day":"2018-01-01","play_end_day":"2018-12-31","font_size":"50","lucency_size":"0","font_color":"000000","place":"bottom","back_color":"000000"}]
     */

    private String code;
    private List<CutInBean> cut_in;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CutInBean> getCut_in() {
        return cut_in;
    }

    public void setCut_in(List<CutInBean> cut_in) {
        this.cut_in = cut_in;
    }

    public static class CutInBean {
        /**
         * status : ON
         * to_area :
         * to_user :
         * type : emergency
         * categoryid : video
         * play_path : http://192.168.1.231/earth1.mp4
         * play_start_time : 08:00:00
         * play_end_time : 22:00:00
         * play_start_day : 2018-01-01
         * play_end_day : 2018-12-31
         * font_size : 50
         * lucency_size : 0
         * font_color : 000000
         * place : bottom
         * back_color : 000000
         */

        private String status;
        private String to_area;
        private String to_user;
        private String type;
        private String categoryid;
        private String play_path;
        private String play_start_time;
        private String play_end_time;
        private String play_start_day;
        private String play_end_day;
        private String font_size;
        private String lucency_size;
        private String font_color;
        private String place;
        private String back_color;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTo_area() {
            return to_area;
        }

        public void setTo_area(String to_area) {
            this.to_area = to_area;
        }

        public String getTo_user() {
            return to_user;
        }

        public void setTo_user(String to_user) {
            this.to_user = to_user;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(String categoryid) {
            this.categoryid = categoryid;
        }

        public String getPlay_path() {
            return play_path;
        }

        public void setPlay_path(String play_path) {
            this.play_path = play_path;
        }

        public String getPlay_start_time() {
            return play_start_time;
        }

        public void setPlay_start_time(String play_start_time) {
            this.play_start_time = play_start_time;
        }

        public String getPlay_end_time() {
            return play_end_time;
        }

        public void setPlay_end_time(String play_end_time) {
            this.play_end_time = play_end_time;
        }

        public String getPlay_start_day() {
            return play_start_day;
        }

        public void setPlay_start_day(String play_start_day) {
            this.play_start_day = play_start_day;
        }

        public String getPlay_end_day() {
            return play_end_day;
        }

        public void setPlay_end_day(String play_end_day) {
            this.play_end_day = play_end_day;
        }

        public String getFont_size() {
            return font_size;
        }

        public void setFont_size(String font_size) {
            this.font_size = font_size;
        }

        public String getLucency_size() {
            return lucency_size;
        }

        public void setLucency_size(String lucency_size) {
            this.lucency_size = lucency_size;
        }

        public String getFont_color() {
            return font_color;
        }

        public void setFont_color(String font_color) {
            this.font_color = font_color;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getBack_color() {
            return back_color;
        }

        public void setBack_color(String back_color) {
            this.back_color = back_color;
        }
    }
}
