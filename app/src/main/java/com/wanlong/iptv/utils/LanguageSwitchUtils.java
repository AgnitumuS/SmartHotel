package com.wanlong.iptv.utils;

import android.content.Context;
import android.content.SharedPreferences;
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
    public static final int THAI = 2;//泰语

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    /**
     * 语言切换
     */
    public static void languageSwitch(Context context, int language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        switch (language) {
            case CHINESE:
                configuration.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case ENGLISH:
                configuration.locale = Locale.ENGLISH;
                break;
            case THAI:
                String languageToLoad = "th-rTH";
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                configuration.locale = Locale.getDefault();
                break;
        }
        resources.updateConfiguration(configuration, dm);
        sharedPreferences = ApkVersion.getSP(context);
        editor = sharedPreferences.edit();
        editor.putInt("language", language);
        editor.commit();
//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }

}
