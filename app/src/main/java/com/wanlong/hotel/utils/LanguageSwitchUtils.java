package com.wanlong.hotel.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by lingchen on 2018/1/26. 10:08
 * mail:lingchen52@foxmail.com
 */
public class LanguageSwitchUtils {

    public static final int CHINESE = 0;//中文
    public static final int ENGLISH = 1;//英语

    /**
     * 语言切换
     */
    public static void languageSwitch(Context context,int language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if(language == CHINESE){
            configuration.locale = Locale.SIMPLIFIED_CHINESE;
        }else if(language == ENGLISH){
            configuration.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(configuration, dm);
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }

}
