package com.wanlong.iptv.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.HomeAD;
import com.wanlong.iptv.entity.HomeTypeData;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.mvp.HomePresenter;
import com.wanlong.iptv.server.AdService;
import com.wanlong.iptv.ui.weigets.MarqueeTextView;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.TimeUtils;
import com.wanlong.iptv.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomePresenter.HomeView {

    @BindView(R.id.tv_welcome_guest)
    TextView mTvWelcomeGuest;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.img_show)
    ImageView mImgShow;
    @BindView(R.id.img_weather)
    ImageView mImgWeather;
    @BindView(R.id.img_ad)
    ImageView mImgAd;
    @BindView(R.id.tv_live)
    TextView mTvLive;
    @BindView(R.id.tv_vod)
    TextView mTvVod;
    //    @BindView(R.id.tv_services)
//    TextView mTvServices;
//    @BindView(R.id.tv_cuisines)
//    TextView mTvCuisines;
//    @BindView(R.id.tv_scnenries)
//    TextView mTvScnenries;
//    @BindView(R.id.tv_expense)
//    TextView mTvExpense;
    @BindView(R.id.tv_setting)
    TextView mTvSetting;
    @BindView(R.id.tv_room)
    AppCompatTextView mTvRoom;
    @BindView(R.id.tv_message)
    MarqueeTextView mTvMessage;


    @Override
    protected int getContentResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initWindowManager() {
        super.initWindowManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (android.provider.Settings.canDrawOverlays(this)) {
                setText();
            }
        } else {
            setText();
        }
    }

    private MarqueeTextView mMarqueeTextView;
    private WindowManager wm;
    private WindowManager.LayoutParams layoutParams;

    private void setText() {
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //设置TextView的属性
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //这里是关键，使控件始终在最上方
        layoutParams.alpha = 1f;
        layoutParams.format = 1;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;// 设置窗口类型为系统级
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //这个Gravity也不能少，不然的话，下面"移动歌词"的时候就会出问题了～ 可以试试[官网文档有说明]
        layoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        //创建自定义的TextView
        mMarqueeTextView = new MarqueeTextView(this);
        mMarqueeTextView.setTextSize(16f);
        mMarqueeTextView.setPadding(0, 0, 0, 32);
//        mMarqueeTextView.setWidth(Utils.getDisplaySize(this).y);
//        mMarqueeTextView.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mMarqueeTextView.setLayoutParams(layoutParams);
        mMarqueeTextView.setTextColor(Color.WHITE);
        mMarqueeTextView.setBackgroundColor(getResources().getColor(R.color.transparent));
        mMarqueeTextView.setText("我是字幕");
        wm.addView(mMarqueeTextView, layoutParams);
        wm.updateViewLayout(mMarqueeTextView, layoutParams);
    }

    @Override
    protected void initView() {
        if (!Utils.isPhone(this)) {
            mTvLive.requestFocus();
        }
        getTime();
        initImgAd();
        mTvWelcomeGuest.setText("");
//        mTvMessage.setText("You have a new message. Please check it.");
    }

    //加载图片
    private void initImgAd() {
//        mImgShow.setOnClickListener(event -> Log.d("HomeActivity", "onclick"));//Lambda 表达式
        GlideApp.with(this)
                .load(R.drawable.hotel_room)
                .transform(new RoundedCorners(12))
                .into(mImgShow);
        GlideApp.with(this)
                .load(R.drawable.weather)
                .transform(new RoundedCorners(12))
                .into(mImgWeather);
        GlideApp.with(this)
                .load(R.drawable.sence)
                .transform(new RoundedCorners(12))
                .into(mImgAd);
    }

    @Override
    protected void initData() {
        Logger.d("mac:" + Utils.getMac(this));
        mTimer.schedule(mTimerTask, 0, 1000);
        setPresenter(new HomePresenter(this));
        startService(new Intent(HomeActivity.this, AdService.class));
        getPresenter().loadHomeADData(Apis.HEADER + Apis.USER_HOME_AD);
//        getPresenter().loadTypeData(Apis.HEADER + Apis.HOME_AD);
//        getPresenter().loadMsgData(Apis.HEADER + Apis.HOME_MSG);
    }

    //    @OnClick({R.id.img_show, R.id.img_weather, R.id.img_ad, R.id.tv_live, R.id.tv_vod, R.id.tv_services,
//            R.id.tv_cuisines, R.id.tv_scnenries, R.id.tv_expense, R.id.tv_setting})
    @OnClick({R.id.img_show, R.id.img_weather, R.id.img_ad, R.id.tv_live, R.id.tv_vod, R.id.tv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_show:
                break;
            case R.id.img_weather:
                break;
            case R.id.img_ad:
                break;
            case R.id.tv_live:
                startActivity(new Intent(HomeActivity.this, LiveActivity.class));
                break;
            case R.id.tv_vod:
                startActivity(new Intent(HomeActivity.this, VodListActivity.class));
                break;
//            case R.id.tv_services:
//                startActivity(new Intent(HomeActivity.this, ServicesActivity.class));
//                break;
//            case R.id.tv_cuisines:
//                startActivity(new Intent(HomeActivity.this, CuisinesActivity.class));
//                break;
//            case R.id.tv_scnenries:
//                startActivity(new Intent(HomeActivity.this, SceneriesActivity.class));
//                break;
//            case R.id.tv_expense:
//                startActivity(new Intent(HomeActivity.this, ExpenseActivity.class));
//                break;
            case R.id.tv_setting:
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                break;
        }
    }

    private HomeTypeData mHomeTypeData;

    @Override
    public void loadDataSuccess(HomeTypeData homeTypeData) {
        Logger.d("HomeActivity: load success");
        this.mHomeTypeData = homeTypeData;
//        getPresenter().loadMsgData(Apis.HEADER + Apis.HOME_AD + "/" + mHomeTypeData.getAdsType().get(0));
    }

    private List<HomeAD.AdVideoBean> mAdVideoBeans;
    private List<HomeAD.AdImageBean> mAdImageBeans;
    private List<HomeAD.AdTextBean> mAdTextBeans;

    @Override
    public void loadHomeADSuccess(HomeAD homeAD) {
        if (homeAD != null && homeAD.getCode().equals("0")) {
            mAdVideoBeans = new ArrayList<>();
            mAdImageBeans = new ArrayList<>();
            mAdTextBeans = new ArrayList<>();
            if (homeAD.getAd_video() != null && homeAD.getAd_video().size() > 0) {
                mAdVideoBeans.addAll(homeAD.getAd_video());
            }
            if (homeAD.getAd_image() != null && homeAD.getAd_image().size() > 0) {
                mAdImageBeans.addAll(homeAD.getAd_image());
                showImgAD();
            }
            if (homeAD.getAd_text() != null && homeAD.getAd_text().size() > 0) {
                mAdTextBeans.addAll(homeAD.getAd_text());
            }
        }
    }

    private void showImgAD(){

    }

    @Override
    public void loadFailed() {
        Logger.e("HomeActivity:load failed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mTimer.cancel();
//        mTimer = null;
    }

    //获取服务器时间
    private void getTime() {
        if (Utils.isNetworkConnected(this)) {
            OkGo.<String>get(Apis.HEADER + Apis.TIME_UPDATE)
                    .tag(this)
                    .cacheMode(CacheMode.NO_CACHE)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (response != null && response.body() != null && !response.body().equals("")) {
                                App.newtime = Long.parseLong(response.body());
                            } else {
                                App.newtime = System.currentTimeMillis() / 1000;
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            App.newtime = System.currentTimeMillis() / 1000;
                        }
                    });
        } else {
            App.newtime = System.currentTimeMillis() / 1000;
        }
    }

    //时钟
    private Timer mTimer = new Timer(true);

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(UPDATE_TIME);
        }
    };

    private static final int UPDATE_TIME = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TIME:
                    App.newtime += 1;
                    String date = TimeUtils.stampToDate(App.newtime * 1000);
                    String str_dayofWeek = getResources().getStringArray(R.array.day_of_week)[TimeUtils.getDay() - 1];
                    mTvTime.setText(date + "  " + str_dayofWeek);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 2s内点击退出
     */
    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - exitTime) < 2000) {
//            if (App.PRISON) {
//                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                finish();
//            } else {
//                startActivity(new Intent(HomeActivity.this, LanguageActivity.class));
//                finish();
//            }
            if (App.PRISON) {
//                new AlertDialog.Builder(HomeActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
//                        .setTitle(getString(R.string.exitdialog_hint))
//                        .setMessage(getString(R.string.exitdialog_out_hint))
//                        .setPositiveButton(getString(R.string.exitdialog_out), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
//                                stopService(new Intent(HomeActivity.this, AdService.class));
//                                App.getApplication().exit();
//                            }
//                        })
//                        .setNegativeButton(getString(R.string.exitdialog_back), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {//响应事件
//                            }
//                        }).show();
            } else {
                startActivity(new Intent(HomeActivity.this, LanguageActivity.class));
                finish();
            }
//            } else {
//                Toast.makeText(this, R.string.click_again_to_welcome_activity, Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
