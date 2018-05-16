package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/5/16. 08:57
 * mail:lingchen52@foxmail.com
 */
public class EPGlist implements Serializable{

    /**
     * code : 0
     * detail : [{"date":"2018/05/15","url":"http://192.168.1.166:80/apk/epg/epg.txt_1526313600.json"},{"date":"2018/05/16","url":"http://192.168.1.166:80/apk/epg/epg.txt_1526400000.json"},{"date":"2018/05/17","url":"http://192.168.1.166:80/apk/epg/epg.txt_1526486400.json"},{"date":"2018/05/18","url":"http://192.168.1.166:80/apk/epg/epg.txt_1526572800.json"},{"date":"2018/05/19","url":"http://192.168.1.166:80/apk/epg/epg.txt_1526659200.json"},{"date":"2018/05/20","url":"http://192.168.1.166:80/apk/epg/epg.txt_1526745600.json"},{"date":"2018/05/21","url":"http://192.168.1.166:80/apk/epg/epg.txt_1526832000.json"}]
     */

    private int code;
    private List<DetailBean> detail;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public static class DetailBean {
        /**
         * date : 2018/05/15
         * url : http://192.168.1.166:80/apk/epg/epg.txt_1526313600.json
         */

        private String date;
        private String url;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
