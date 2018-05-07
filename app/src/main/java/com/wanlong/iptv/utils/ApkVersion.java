package com.wanlong.iptv.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lingchen on 2018/5/4. 16:23
 * mail:lingchen52@foxmail.com
 */
public class ApkVersion {

    //标准版本
    public static final int STANDARD_VERSION = 0;
    //运营商版本
    public static final int OPERATOR_VERSION = 1;
    //监狱版本
    public static final int PRISON_VERSION = 2;
    //酒店版本
    public static final int HOTEL_VERSION = 3;
    //学校版本
    public static final int SCHOOL_VERSION = 4;

    //当前版本
    public static final int CURRENT_VERSION = PRISON_VERSION;

    //是否是正式版    true-正式版   false-测试版
    public static final boolean RELEASE_VERSION = false;

    public static SharedPreferences getSP(Context context) {
        String spName = "";
        switch (CURRENT_VERSION) {
            case STANDARD_VERSION:
                spName = "STANDARD_login";
                break;
            case OPERATOR_VERSION:
                spName = "OPERATOR_login";
                break;
            case PRISON_VERSION:
                spName = "PRISON_login";
                break;
            case HOTEL_VERSION:
                spName = "HOTEL_login";
                break;
            case SCHOOL_VERSION:
                spName = "SCHOOL_login";
                break;
            default:
                spName = "STANDARD_login";
                break;
        }
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }


}
