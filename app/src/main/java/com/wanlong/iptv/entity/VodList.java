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
     * playlist : [{"vod_name":"hall","director":"x","vod_actor":"xx","vod_language":"xxxx","vod_release_area":"xxx","vod_release_time":"2000-01-01","vod_category":"Action movie","vod_time_time":"res","vod_duration_time":"120","vod_watch_level":"normal","vod_detail":"test","vod_scores":"5.0","vod_price":"0","total_sets":"1","vod_movie_player_src":"http://192.168.1.166:80/stream/vod/fun/","vod_pic_dir":"http://192.168.1.166:80/stream/vod/fun/","pic_url":["01.jpg","02.jpg"],"vod_movie":["fun01.ts","fun02.ts","fun03.ts","fun04.ts"],"current_sets":"4"}]
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
         * vod_name : hall
         * director : x
         * vod_actor : xx
         * vod_language : xxxx
         * vod_release_area : xxx
         * vod_release_time : 2000-01-01
         * vod_category : Action movie
         * vod_time_time : res
         * vod_duration_time : 120
         * vod_watch_level : normal
         * vod_detail : test
         * vod_scores : 5.0
         * vod_price : 0
         * total_sets : 1
         * vod_movie_player_src : http://192.168.1.166:80/stream/vod/fun/
         * vod_pic_dir : http://192.168.1.166:80/stream/vod/fun/
         * pic_url : ["01.jpg","02.jpg"]
         * vod_movie : ["fun01.ts","fun02.ts","fun03.ts","fun04.ts"]
         * current_sets : 4
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
        private String total_sets;
        private String vod_movie_player_src;
        private String vod_pic_dir;
        private String current_sets;
        private List<String> pic_url;
        private List<String> vod_movie;

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

        public String getTotal_sets() {
            return total_sets;
        }

        public void setTotal_sets(String total_sets) {
            this.total_sets = total_sets;
        }

        public String getVod_movie_player_src() {
            return vod_movie_player_src;
        }

        public void setVod_movie_player_src(String vod_movie_player_src) {
            this.vod_movie_player_src = vod_movie_player_src;
        }

        public String getVod_pic_dir() {
            return vod_pic_dir;
        }

        public void setVod_pic_dir(String vod_pic_dir) {
            this.vod_pic_dir = vod_pic_dir;
        }

        public String getCurrent_sets() {
            return current_sets;
        }

        public void setCurrent_sets(String current_sets) {
            this.current_sets = current_sets;
        }

        public List<String> getPic_url() {
            return pic_url;
        }

        public void setPic_url(List<String> pic_url) {
            this.pic_url = pic_url;
        }

        public List<String> getVod_movie() {
            return vod_movie;
        }

        public void setVod_movie(List<String> vod_movie) {
            this.vod_movie = vod_movie;
        }
    }
}
