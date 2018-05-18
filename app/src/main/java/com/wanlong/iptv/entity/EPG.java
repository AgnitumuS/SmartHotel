package com.wanlong.iptv.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lingchen on 2018/5/16. 09:14
 * mail:lingchen52@foxmail.com
 */
public class EPG implements Serializable{

    /**
     * code : 0
     * date : 2018/05/16
     * detail : [{"time":"04:47","program":"精彩一刻：2012-79"},{"time":"04:59","program":"新闻联播"},{"time":"05:29","program":"人与自然：2012-199"},{"time":"06:00","program":"朝闻天下"},{"time":"08:35","program":"魔幻手机36/42"},{"time":"09:26","program":"魔幻手机37/42"},{"time":"10:17","program":"魔幻手机38/42"},{"time":"11:08","program":"魔幻手机39/42"},{"time":"12:00","program":"新闻30分"},{"time":"12:35","program":"今日说法：2012-206"},{"time":"13:13","program":"电视剧：公主嫁到18/32"},{"time":"14:03","program":"电视剧：公主嫁到19/32"},{"time":"14:52","program":"电视剧：公主嫁到20/32"},{"time":"15:44","program":"电视剧：公主嫁到21/32"},{"time":"16:35","program":"第一动画乐园：2012-308"},{"time":"17:05","program":"第一动画乐园：2012-89"},{"time":"17:45","program":"2012世界大力士中国争霸赛 2"},{"time":"18:52","program":"生活提示：2012-52"},{"time":"19:00","program":"新闻联播"},{"time":"19:38","program":"焦点访谈"},{"time":"19:55","program":"身边的感动 554"},{"time":"20:00","program":"电视剧：前情提要《先遣连》20/24"},{"time":"20:05","program":"电视剧：先遣连20/24"},{"time":"20:57","program":"电视剧：前情提要《先遣连》21/24"},{"time":"21:02","program":"电视剧：先遣连21/24"},{"time":"22:00","program":"晚间新闻"},{"time":"22:40","program":"魅力纪录：2012-72"},{"time":"23:36","program":"电视剧：红娘子39/47"},{"time":"00:27","program":"电视剧：红娘子40/47"},{"time":"01:18","program":"电视剧：红娘子41/47"},{"time":"02:09","program":"动物世界：2012-169"},{"time":"02:39","program":"国际艺苑：2012-30"},{"time":"03:29","program":"精彩一刻：2012-77"},{"time":"03:39","program":"分秒必争：2012-22"}]
     */

    private int code;
    private String date;
    private List<DetailBean> detail;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public static class DetailBean {
        /**
         * time : 04:47
         * program : 精彩一刻：2012-79
         */

        private String time;
        private String program;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getProgram() {
            return program;
        }

        public void setProgram(String program) {
            this.program = program;
        }
    }
}
