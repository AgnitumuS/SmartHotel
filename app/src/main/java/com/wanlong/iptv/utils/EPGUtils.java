package com.wanlong.iptv.utils;

import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.EPG;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by lingchen on 2018/5/15. 17:15
 * mail:lingchen52@foxmail.com
 */
public class EPGUtils {

    public static String currentPlay = "";
    public static String nextPlay = "";
    public static List<EPG.DetailBean> mDetailBeans;

    //解析EPG
    public static void parseEPG(List<EPG.DetailBean> detailBeans) {
        long currentParseTime = 0;//当前时间戳
        long programTime = 0;//节目时间戳
        long[] differenceValues = new long[detailBeans.size()];//节目时间和当前时间的差
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        //获取当前时间戳
        String currentTime = simpleDateFormat.format(new Date(App.newtime * 1000));//当前时间 04:30
        try {
            Date currentDate = simpleDateFormat.parse(currentTime);
            currentParseTime = currentDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //得到节目时间和当前时间的差 数组
        for (int i = 0; i < detailBeans.size(); i++) {
            String time = detailBeans.get(i).getTime();
            try {
                Date date = simpleDateFormat.parse(time);
                programTime = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            differenceValues[i] = Math.abs(programTime - currentParseTime);
        }
        //得到最小差值所在的位置
        long min = differenceValues[0];//最小差值
        int position = 0;//最小差值的位置
        for (int i = 0; i < differenceValues.length; i++) {
            if (differenceValues[i] < min) {
                min = differenceValues[i];
                position = i;
            }
        }
        //得到最小差值的时间戳
        String time = detailBeans.get(position).getTime();
        try {
            Date parse = simpleDateFormat.parse(time);
            programTime = parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //得到当前和下一条播放的节目
        if (programTime - currentParseTime > 0) {
            if (position > 0) {
                currentPlay = detailBeans.get(position - 1).getTime() + "    " +
                        detailBeans.get(position - 1).getProgram();
            } else {
                currentPlay = detailBeans.get(position).getTime() + "    " +
                        detailBeans.get(position).getProgram();
            }
            nextPlay = detailBeans.get(position).getTime() + "    " +
                    detailBeans.get(position).getProgram();
        } else {
            currentPlay = detailBeans.get(position).getTime() + "    " +
                    detailBeans.get(position).getProgram();
            nextPlay = detailBeans.get(position + 1).getTime() + "    " +
                    detailBeans.get(position + 1).getProgram();
        }
    }

    //获取当前播放节目
    public static String getCurrentPlay() {
        return currentPlay;
    }

    //获取下一条播放节目
    public static String getNextPlay() {
        return nextPlay;
    }

    public static void sortEPG(List<EPG.DetailBean> detailBeans) {
        Collections.sort(detailBeans, new Comparator<EPG.DetailBean>() {
            @Override
            public int compare(EPG.DetailBean o1, EPG.DetailBean o2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Date date = null;
                long time1 = 0, time2 = 0;
                try {
                    date = simpleDateFormat.parse(o1.getTime().replaceAll(" ", ""));
                    time1 = date.getTime();
                    date = simpleDateFormat.parse(o2.getTime().replaceAll(" ", ""));
                    time2 = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //按时间降序
                if (time1 > time2) {
                    return -1;
                } else if (time1 < time2) {
                    return 1;
                }
                return 0;
            }
        });
        mDetailBeans = new ArrayList<>();
        mDetailBeans = detailBeans;
    }
}
