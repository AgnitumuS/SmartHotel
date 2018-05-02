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
    public static int getDay(long milliseconds) {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH) + 1;// Java月份从0开始算
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        return dayOfWeek;

        Calendar cal = zeroFromHour(milliseconds);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 将时间戳转换成当天零点的时间戳
     *
     * @param milliseconds
     * @return
     */
    public static Calendar zeroFromHour(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(completMilliseconds(milliseconds));
        zeroFromHour(calendar);
        return calendar;
    }

    /**
     * 将时，分，秒，以及毫秒值设置为0
     *
     * @param calendar
     */
    public static void zeroFromHour(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 由于服务器返回的是10位，手机端使用需要补全3位
     *
     * @param milliseconds
     * @return
     */
    private static long completMilliseconds(long milliseconds) {
        String milStr = Long.toString(milliseconds);
        if (milStr.length() == 10) {
            milliseconds = milliseconds * 1000;
        }
        return milliseconds;
    }

}
