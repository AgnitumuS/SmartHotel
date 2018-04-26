package com.wanlong.iptv.utils;

/**
 * Created by luo on 2018/1/24.
 */

public class Apis {
    public static final String KEY = "76c833fa47e747d5c3bd24241f027eac";
    public static String HEADER = "http://192.168.1.166:80/";
    public static final String HEADER_ORIGIN = "http://192.168.1.166:80/";
    public static String ROOM_ORIGIN = "501";
    public static final String SETTING_PASSWORD = "000000";
    //登录
    public static final String APP_LOGIN = "api";
    //广告
    public static final String HOME_AD = "api/ads";
    //消息
    public static final String HOME_MSG = "api/message";
    //直播
    public static final String LIVE_TYPE = "api/channel";
    //点播
    public static final String VOD_TYPE = "api/genre";
    //服务
    public static final String SERVICE = "api/service";
    //美食
    public static final String CUISINES = "api/cuisine";
    //应用更新接口
    public static final String APP_UPDATE_RELEASE = "api/apk";
    public static final String APP_UPDATE_BETA = "api/apk/beta";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //用户登录接口
    public static final String USER_LOGIN = "user/user_login.php";
    //直播接口
    public static final String USER_LIVE = "user/fun/user_play.php?live_json=?";
    //点播
    public static final String USER_VOD_TYPE = "user/fun/user_vodplay.php?vod_json=?";
    //时间校准
    public static final String TIME_UPDATE = "user/time_update.php?update_time=?";
    //插播
    public static final String USER_IN_STREAM = "user/fun/user_emergency.php?emer_json=?";
    //首页广告
    public static final String USER_HOME_AD = "user/fun/ad_play.php?ad_json=?";
    //用户登录状态
    public static final String USER_LOGIN_STATUS = "user/user_login_status.php";
    //Apk崩溃日志上传
    public static final String USER_CRASHLOG_UPLOAD = "user/fun/user_report.php";
    //用户观看点播信息上传
    public static final String USER_VOD_PLAYBACK_INFO_UPLOAD = "user/fun/user_vod_report.php";
    //应用更新接口
    public static final String USER_APP_UPDATE = "apk/software/apk_update.json";
    public static final String USER_APP_UPDATE_BETA = "apk/software/beta/apk_update.json";
}
