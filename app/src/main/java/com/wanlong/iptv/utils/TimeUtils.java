package com.wanlong.iptv.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lingchen on 2018/1/26. 09:34
 * mail:lingchen52@foxmail.com
 */
public class TimeUtils {
    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    //返回星期几
    public static int getDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;// Java月份从0开始算
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek ;
    }
}
