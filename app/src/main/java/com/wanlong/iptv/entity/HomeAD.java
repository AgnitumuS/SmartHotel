package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/4/10. 17:14
 * mail:lingchen52@foxmail.com
 */
public class HomeAD implements Serializable {

    /**
     * code : 0
     * error : 0
     * ad_video : [{"ad_name":"Sample-video","ad_category":"video","ad_detail":"test video","ad_src":"http://192.168.1.166:80/stream/ad/sample.mp4","ad_start_time":"2017-05-04 00:00:00","ad_end_time":"2017-05-04 00:00:00","ad_duration_time":"6","ad_status":"on","ad_display_location":"10,7,1"}]
     * ad_image : [{"ad_name":"Sample-image","ad_category":"image","ad_detail":"test video","ad_src":"http://192.168.1.166:80/stream/ad/great wall.jpg","ad_start_time":"2017-05-04 00:00:00","ad_end_time":"2017-05-04 00:00:00","ad_duration_time":"6","ad_status":"on","ad_display_location":"1,2"}]
     * ad_text : [{"ad_name":"test","ad_category":"text","ad_detail":"this is a test txt","ad_src":"","ad_start_time":"2017-05-25 00:00:00","ad_end_time":"2017-05-25 00:00:00","ad_font_size":"50","ad_roll_speed":"5","ad_display_area":"down","ad_duration_time":"10","ad_status":"on"}]
     */

    private String code;
    private String error;
    private List<AdVideoBean> ad_video;
    private List<AdImageBean> ad_image;
    private List<AdTextBean> ad_text;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<AdVideoBean> getAd_video() {
        return ad_video;
    }

    public void setAd_video(List<AdVideoBean> ad_video) {
        this.ad_video = ad_video;
    }

    public List<AdImageBean> getAd_image() {
        return ad_image;
    }

    public void setAd_image(List<AdImageBean> ad_image) {
        this.ad_image = ad_image;
    }

    public List<AdTextBean> getAd_text() {
        return ad_text;
    }

    public void setAd_text(List<AdTextBean> ad_text) {
        this.ad_text = ad_text;
    }

    public static class AdVideoBean {
        /**
         * ad_name : Sample-video
         * ad_category : video
         * ad_detail : test video
         * ad_src : http://192.168.1.166:80/stream/ad/sample.mp4
         * ad_start_time : 2017-05-04 00:00:00
         * ad_end_time : 2017-05-04 00:00:00
         * ad_duration_time : 6
         * ad_status : on
         * ad_display_location : 10,7,1
         */

        private String ad_name;
        private String ad_category;
        private String ad_detail;
        private String ad_src;
        private String ad_start_time;
        private String ad_end_time;
        private String ad_duration_time;
        private String ad_status;
        private String ad_display_location;

        public String getAd_name() {
            return ad_name;
        }

        public void setAd_name(String ad_name) {
            this.ad_name = ad_name;
        }

        public String getAd_category() {
            return ad_category;
        }

        public void setAd_category(String ad_category) {
            this.ad_category = ad_category;
        }

        public String getAd_detail() {
            return ad_detail;
        }

        public void setAd_detail(String ad_detail) {
            this.ad_detail = ad_detail;
        }

        public String getAd_src() {
            return ad_src;
        }

        public void setAd_src(String ad_src) {
            this.ad_src = ad_src;
        }

        public String getAd_start_time() {
            return ad_start_time;
        }

        public void setAd_start_time(String ad_start_time) {
            this.ad_start_time = ad_start_time;
        }

        public String getAd_end_time() {
            return ad_end_time;
        }

        public void setAd_end_time(String ad_end_time) {
            this.ad_end_time = ad_end_time;
        }

        public String getAd_duration_time() {
            return ad_duration_time;
        }

        public void setAd_duration_time(String ad_duration_time) {
            this.ad_duration_time = ad_duration_time;
        }

        public String getAd_status() {
            return ad_status;
        }

        public void setAd_status(String ad_status) {
            this.ad_status = ad_status;
        }

        public String getAd_display_location() {
            return ad_display_location;
        }

        public void setAd_display_location(String ad_display_location) {
            this.ad_display_location = ad_display_location;
        }
    }

    public static class AdImageBean {
        /**
         * ad_name : Sample-image
         * ad_category : image
         * ad_detail : test video
         * ad_src : http://192.168.1.166:80/stream/ad/great wall.jpg
         * ad_start_time : 2017-05-04 00:00:00
         * ad_end_time : 2017-05-04 00:00:00
         * ad_duration_time : 6
         * ad_status : on
         * ad_display_location : 1,2
         */

        private String ad_name;
        private String ad_category;
        private String ad_detail;
        private String ad_src;
        private String ad_start_time;
        private String ad_end_time;
        private String ad_duration_time;
        private String ad_status;
        private String ad_display_location;

        public String getAd_name() {
            return ad_name;
        }

        public void setAd_name(String ad_name) {
            this.ad_name = ad_name;
        }

        public String getAd_category() {
            return ad_category;
        }

        public void setAd_category(String ad_category) {
            this.ad_category = ad_category;
        }

        public String getAd_detail() {
            return ad_detail;
        }

        public void setAd_detail(String ad_detail) {
            this.ad_detail = ad_detail;
        }

        public String getAd_src() {
            return ad_src;
        }

        public void setAd_src(String ad_src) {
            this.ad_src = ad_src;
        }

        public String getAd_start_time() {
            return ad_start_time;
        }

        public void setAd_start_time(String ad_start_time) {
            this.ad_start_time = ad_start_time;
        }

        public String getAd_end_time() {
            return ad_end_time;
        }

        public void setAd_end_time(String ad_end_time) {
            this.ad_end_time = ad_end_time;
        }

        public String getAd_duration_time() {
            return ad_duration_time;
        }

        public void setAd_duration_time(String ad_duration_time) {
            this.ad_duration_time = ad_duration_time;
        }

        public String getAd_status() {
            return ad_status;
        }

        public void setAd_status(String ad_status) {
            this.ad_status = ad_status;
        }

        public String getAd_display_location() {
            return ad_display_location;
        }

        public void setAd_display_location(String ad_display_location) {
            this.ad_display_location = ad_display_location;
        }
    }

    public static class AdTextBean {
        /**
         * ad_name : test
         * ad_category : text
         * ad_detail : this is a test txt
         * ad_src :
         * ad_start_time : 2017-05-25 00:00:00
         * ad_end_time : 2017-05-25 00:00:00
         * ad_font_size : 50
         * ad_roll_speed : 5
         * ad_display_area : down
         * ad_duration_time : 10
         * ad_status : on
         */

        private String ad_name;
        private String ad_category;
        private String ad_detail;
        private String ad_src;
        private String ad_start_time;
        private String ad_end_time;
        private String ad_font_size;
        private String ad_roll_speed;
        private String ad_display_area;
        private String ad_duration_time;
        private String ad_status;

        public String getAd_name() {
            return ad_name;
        }

        public void setAd_name(String ad_name) {
            this.ad_name = ad_name;
        }

        public String getAd_category() {
            return ad_category;
        }

        public void setAd_category(String ad_category) {
            this.ad_category = ad_category;
        }

        public String getAd_detail() {
            return ad_detail;
        }

        public void setAd_detail(String ad_detail) {
            this.ad_detail = ad_detail;
        }

        public String getAd_src() {
            return ad_src;
        }

        public void setAd_src(String ad_src) {
            this.ad_src = ad_src;
        }

        public String getAd_start_time() {
            return ad_start_time;
        }

        public void setAd_start_time(String ad_start_time) {
            this.ad_start_time = ad_start_time;
        }

        public String getAd_end_time() {
            return ad_end_time;
        }

        public void setAd_end_time(String ad_end_time) {
            this.ad_end_time = ad_end_time;
        }

        public String getAd_font_size() {
            return ad_font_size;
        }

        public void setAd_font_size(String ad_font_size) {
            this.ad_font_size = ad_font_size;
        }

        public String getAd_roll_speed() {
            return ad_roll_speed;
        }

        public void setAd_roll_speed(String ad_roll_speed) {
            this.ad_roll_speed = ad_roll_speed;
        }

        public String getAd_display_area() {
            return ad_display_area;
        }

        public void setAd_display_area(String ad_display_area) {
            this.ad_display_area = ad_display_area;
        }

        public String getAd_duration_time() {
            return ad_duration_time;
        }

        public void setAd_duration_time(String ad_duration_time) {
            this.ad_duration_time = ad_duration_time;
        }

        public String getAd_status() {
            return ad_status;
        }

        public void setAd_status(String ad_status) {
            this.ad_status = ad_status;
        }
    }
}
