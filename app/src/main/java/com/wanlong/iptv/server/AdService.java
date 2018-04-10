package com.wanlong.iptv.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.PushMSG;
import com.wanlong.iptv.ui.activity.AdActivity;
import com.wanlong.iptv.utils.ActivityCollector;
import com.wanlong.iptv.utils.Apis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by hasee on 2017/4/12.
 */

public class AdService extends Service {

    public static final int INTERVAL_TIME = 5;//间隔时间

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mTimer != null && mTimerTask != null){
            mTimer.schedule(mTimerTask, 0, INTERVAL_TIME * 1000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //时钟
    private Timer mTimer = new Timer(true);

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
//          mHandler.sendEmptyMessage(REQUEST);
            getAd();
            doQueue();
        }
    };

//    private static final int REQUEST = 0;
//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case REQUEST:
//                    getAd();
//                    doQueue();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimer = null;
    }

    private PushMSG mPushMSG;
    private String result;
    private String date;//日期
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    //获取插播内容
    private void getAd() {
        OkGo.<String>post(Apis.HEADER + Apis.USER_IN_STREAM)
                .tag(this)
                .params("mac", App.mac)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        Logger.json(response.body());
                        if (response.body() != null) {
                            try {
                                mPushMSG = JSON.parseObject(response.body(), PushMSG.class);
                                executeData(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    //处理返回数据
    private void executeData(Response<String> response) {
        if (mPushMSG != null && mPushMSG.getCode().equals("0")) {
            if (mPushMSG.getCut_in() != null && mPushMSG.getCut_in().size() > 0) {//集合不为空
                if (result == null) {
                    result = response.body();
                    date = mDateFormat.format(new Date(App.newtime * 1000));
                    showAD(mPushMSG);
                } else {
                    if (!result.equals(response.body())) {//字符串比较返回数据是否变化
                        result = response.body();
                        if ((ActivityCollector.activities.get(ActivityCollector.activities.size() - 1) instanceof AdActivity)) {
                            ActivityCollector.finishActivity(ActivityCollector.activities.size() - 1);
                        }
//                        mAdListener.dismissAllText();
                        showAD(mPushMSG);
                    }
                    //时间是否变化，单位：天
                    if (!date.equals(mDateFormat.format(new Date(App.newtime * 1000)))) {
                        date = mDateFormat.format(new Date(App.newtime * 1000));
                        showAD(mPushMSG);
                    }
                }
            }
        }
    }

    //任务队列
    private void doQueue() {
        if (differs != null) {
            for (int i = 0; i < differs.size(); i++) {
                differ = differs.get(i);
                if (!differ.isPower()) {
                    if (differ.getSdiffer() > 0) {
                        differ.setSdiffer(differ.getSdiffer() - 1);
                        if (differ.getSdiffer() == 0) {
                            differ.setPower(true);
                            if (differ.getCategoryid().equals("text")) {
//                                mAdListener.showText(differ.getPlaypath(), differ.getPlace(),
//                                        differ.getFont_size(), differ.getBack_color(), differ.getFont_color());
                            } else if (differ.getCategoryid().equals("video")) {
//                                mAdListener.showVideo(differ.getPlaypath());
                            }
                        }
                    }
                } else {
                    if (differ.getEdiffer() > 0) {
                        differ.setEdiffer(differ.getEdiffer() - 1);
                        if (differ.getEdiffer() == 0) {
                            differ.setPower(false);
                            if (differ.getCategoryid().equals("text")) {
//                                mAdListener.dismissText(differ.getPlaypath());
                            } else if (differ.getCategoryid().equals("video")) {
//                                mAdListener.dismissVideo();
                            }
                        }
                    }
                }
            }
        }
    }

    private List<PushMSG.CutInBean> mCutInBeens;
    private PushMSG.CutInBean mCutInBean;
    private String startTime, endTime, currentTime;//开始时间,结束时间,当前时间:HH:mm:ss
    private String startDay, endDay, currentDay;//开始日期,结束日期,当前日期:yyyy-MM-dd
    private long stime, etime, ctime;//开始时间戳,结束时间戳,当前时间戳
    private long sdiffer = -1;//开始时间和当前时间的差
    private long ediffer = -1;//结束时间和当前时间的差
    private SimpleDateFormat simpleDateFormat;

    //获取时间  紧急插播
    private void getStringTime(PushMSG.CutInBean mCutInBean) {
        startTime = mCutInBean.getPlay_start_time();
        endTime = mCutInBean.getPlay_end_time();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        currentTime = simpleDateFormat.format(new Date(App.newtime * 1000));
    }

    //获取时间戳
    private void getlongTime(String start, String end, String current) {
        try {
            stime = simpleDateFormat.parse(start).getTime() / 1000;
            etime = simpleDateFormat.parse(end).getTime() / 1000;
            ctime = simpleDateFormat.parse(current).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //获取日期
    private void getDay() {
        startDay = mCutInBean.getPlay_start_day();
        endDay = mCutInBean.getPlay_end_day();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDay = simpleDateFormat.format(new Date(App.newtime * 1000));
    }

    //得到未过期插播
    private void getNewCutInBeens(PushMSG.CutInBean mCutInBean) {
        getStringTime(mCutInBean);
        getlongTime(startTime, endTime, currentTime);
        if (ctime < etime) {//当前时间小于结束时间
            newCutInBeens.add(new MyCutInBean(mCutInBean));
        }
    }

    private List<MyCutInBean> newCutInBeens;
    private MyCutInBean myCutInBean;

    //显示插播
    private void showAD(PushMSG mPushMSG) {
        mCutInBeens = mPushMSG.getCut_in();
        newCutInBeens = new ArrayList<>();
        for (int i = 0; i < mCutInBeens.size(); i++) {
            if (mCutInBeens.get(i).getStatus().equals("ON")) {
                mCutInBean = mCutInBeens.get(i);
                if (mCutInBean.getType().equals("emergency")) {//紧急插播
                    getNewCutInBeens(mCutInBean);
                }
                if (mCutInBean.getType().equals("timer")) {//日常插播
                    getDay();
                    getlongTime(startDay, endDay, currentDay);
                    //比较日期，看当前日期是否在区间内
                    if (ctime >= stime && ctime <= etime) {//当前日期在开始日期和结束日期之间
                        getNewCutInBeens(mCutInBean);
                    }
                }
            }
        }
        Collections.sort(newCutInBeens);//排序
        getStartEnd();
    }

    private List<Differ> differs;
    private Differ differ;

    //获取开始时间和当前时间的差，获取结束时间和当前时间的差
    private void getStartEnd() {
        differs = new ArrayList<>();
        for (int i = 0; i < newCutInBeens.size(); i++) {
            myCutInBean = newCutInBeens.get(i);
            getStringTime(myCutInBean);
            getlongTime(startTime, endTime, currentTime);
            sdiffer = (stime - ctime) / INTERVAL_TIME;
            ediffer = (etime - ctime) / INTERVAL_TIME;
            differ = new Differ(sdiffer, ediffer, myCutInBean.getType(), myCutInBean.getCategoryid(),
                    myCutInBean.getPlay_path(), myCutInBean.getPlace(), myCutInBean.getFont_size(),
                    myCutInBean.getBack_color(), myCutInBean.getFont_color());
            if (ctime < stime) { //当前时间小于开始时间
                differs.add(differ);
            } else if (ctime >= stime && ctime < etime) {//当前时间在开始时间和结束时间之间
                differ.setPower(true);
                differs.add(differ);
                if (differ.getCategoryid().equals("text")) {
//                    mAdListener.showText(differ.getPlaypath(), differ.getPlace(),
//                            differ.getFont_size(), differ.getBack_color(), differ.getFont_color());
                } else if (differ.getCategoryid().equals("video")) {
//                    mAdListener.showVideo(differ.getPlaypath());
                }
            } else {

            }
        }
    }

    private static AdListener mAdListener;

    public static void setAdListener(AdListener madListener) {
        mAdListener = madListener;
    }

    public interface AdListener {
        void showText(String text, String place, String font_size, String back_color, String font_color);

        void showVideo(String url);

        void dismissAllText();

        void dismissText(String text);

        void dismissVideo();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class Differ {
        private long sdiffer;
        private long ediffer;
        private boolean power;
        private String type;
        private String categoryid;
        private String playpath;
        private String place;
        private String font_size;
        private String back_color;
        private String font_color;

        public Differ(long sdiffer, long ediffer, String type, String categoryid, String playpath,
                      String place, String font_size, String back_color, String font_color) {
            setSdiffer(sdiffer);
            setEdiffer(ediffer);
            setType(type);
            setCategoryid(categoryid);
            setPlaypath(playpath);
            setPlace(place);
            setFont_size(font_size);
            setBack_color(back_color);
            setFont_color(font_color);
        }

        public long getSdiffer() {
            return sdiffer;
        }

        public void setSdiffer(long sdiffer) {
            this.sdiffer = sdiffer;
        }

        public long getEdiffer() {
            return ediffer;
        }

        public void setEdiffer(long ediffer) {
            this.ediffer = ediffer;
        }

        public boolean isPower() {
            return power;
        }

        public void setPower(boolean power) {
            this.power = power;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(String categoryid) {
            this.categoryid = categoryid;
        }

        public String getPlaypath() {
            return playpath;
        }

        public void setPlaypath(String playpath) {
            this.playpath = playpath;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getFont_size() {
            return font_size;
        }

        public void setFont_size(String font_size) {
            this.font_size = font_size;
        }

        public String getBack_color() {
            return back_color;
        }

        public void setBack_color(String back_color) {
            this.back_color = back_color;
        }

        public String getFont_color() {
            return font_color;
        }

        public void setFont_color(String font_color) {
            this.font_color = font_color;
        }
    }

    public static class MyCutInBean extends PushMSG.CutInBean implements Comparable<MyCutInBean> {

        public MyCutInBean(PushMSG.CutInBean cutInBean) {
            setStatus(cutInBean.getStatus());
            setTo_area(cutInBean.getTo_area());
            setTo_user(cutInBean.getTo_user());
            setCategoryid(cutInBean.getCategoryid());
            setPlay_path(cutInBean.getPlay_path());
            setPlay_start_time(cutInBean.getPlay_start_time());
            setPlay_end_time(cutInBean.getPlay_end_time());
            setPlay_start_day(cutInBean.getPlay_start_day());
            setPlay_end_day(cutInBean.getPlay_end_day());
            setType(cutInBean.getType());
            setPlace(cutInBean.getPlace());
            setLucency_size(cutInBean.getLucency_size());
            setFont_size(cutInBean.getFont_size());
            setFont_color("ff" + cutInBean.getFont_color());
            setBack_color("ff" + cutInBean.getBack_color());
        }

        //排序
        @Override
        public int compareTo(@NonNull MyCutInBean bean) {
            String startTime1 = this.getPlay_start_time();
            String startTime2 = bean.getPlay_start_time();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            long stime1 = -1, stime2 = -1;
            try {
                stime1 = simpleDateFormat.parse(startTime1).getTime() / 1000;
                stime2 = simpleDateFormat.parse(startTime2).getTime() / 1000;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (stime1 > stime2) {
                return 1;
            } else if (stime1 < stime2) {
                return -1;
            } else {

            }
            return 0;
        }
    }
}
