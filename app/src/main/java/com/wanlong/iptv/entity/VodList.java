package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/4/3. 16:47
 * mail:lingchen52@foxmail.com
 */
public class VodList implements Serializable {

    /**
     * code : 0
     * category : Action movie
     * playlist : [{"vod_name":"test","director":"some body","vod_actor":"some body","vod_language":"english","vod_release_area":"area","vod_release_time":"2017-04-01","vod_category":"Action movie,sci-fi movie","vod_time_time":"res","vod_duration_time":"120","vod_watch_level":"res","vod_detail":"nothing hehe!","vod_scores":"5.0","vod_price":"0","vod_movie_player_src":"http://192.168.1.166:80/stream/vod/fun.ts","pic_url":"http://192.168.1.166:80/stream/vod/sence.jpg","vod_pic_dir":"http://192.168.1.166:80/stream/vod/sence.jpg"}]
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
         * vod_name : test
         * director : some body
         * vod_actor : some body
         * vod_language : english
         * vod_release_area : area
         * vod_release_time : 2017-04-01
         * vod_category : Action movie,sci-fi movie
         * vod_time_time : res
         * vod_duration_time : 120
         * vod_watch_level : res
         * vod_detail : nothing hehe!
         * vod_scores : 5.0
         * vod_price : 0
         * vod_movie_player_src : http://192.168.1.166:80/stream/vod/fun.ts
         * pic_url : http://192.168.1.166:80/stream/vod/sence.jpg
         * vod_pic_dir : http://192.168.1.166:80/stream/vod/sence.jpg
         */

        private String vod_name;
        private String director;
        private String vod_actor;
        private String vod_language;
        private String vod_release_area;
        private String vod_release_time;
        private String vod_category;
        private String vod_time_time;
        private String vod_duration_time;
        private String vod_watch_level;
        private String vod_detail;
        private String vod_scores;
        private String vod_price;
        private String vod_movie_player_src;
        private String pic_url;
        private String vod_pic_dir;

        public String getVod_name() {
            return vod_name;
        }

        public void setVod_name(String vod_name) {
            this.vod_name = vod_name;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getVod_actor() {
            return vod_actor;
        }

        public void setVod_actor(String vod_actor) {
            this.vod_actor = vod_actor;
        }

        public String getVod_language() {
            return vod_language;
        }

        public void setVod_language(String vod_language) {
            this.vod_language = vod_language;
        }

        public String getVod_release_area() {
            return vod_release_area;
        }

        public void setVod_release_area(String vod_release_area) {
            this.vod_release_area = vod_release_area;
        }

        public String getVod_release_time() {
            return vod_release_time;
        }

        public void setVod_release_time(String vod_release_time) {
            this.vod_release_time = vod_release_time;
        }

        public String getVod_category() {
            return vod_category;
        }

        public void setVod_category(String vod_category) {
            this.vod_category = vod_category;
        }

        public String getVod_time_time() {
            return vod_time_time;
        }

        public void setVod_time_time(String vod_time_time) {
            this.vod_time_time = vod_time_time;
        }

        public String getVod_duration_time() {
            return vod_duration_time;
        }

        public void setVod_duration_time(String vod_duration_time) {
            this.vod_duration_time = vod_duration_time;
        }

        public String getVod_watch_level() {
            return vod_watch_level;
        }

        public void setVod_watch_level(String vod_watch_level) {
            this.vod_watch_level = vod_watch_level;
        }

        public String getVod_detail() {
            return vod_detail;
        }

        public void setVod_detail(String vod_detail) {
            this.vod_detail = vod_detail;
        }

        public String getVod_scores() {
            return vod_scores;
        }

        public void setVod_scores(String vod_scores) {
            this.vod_scores = vod_scores;
        }

        public String getVod_price() {
            return vod_price;
        }

        public void setVod_price(String vod_price) {
            this.vod_price = vod_price;
        }

        public String getVod_movie_player_src() {
            return vod_movie_player_src;
        }

        public void setVod_movie_player_src(String vod_movie_player_src) {
            this.vod_movie_player_src = vod_movie_player_src;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getVod_pic_dir() {
            return vod_pic_dir;
        }

        public void setVod_pic_dir(String vod_pic_dir) {
            this.vod_pic_dir = vod_pic_dir;
        }
    }
}
