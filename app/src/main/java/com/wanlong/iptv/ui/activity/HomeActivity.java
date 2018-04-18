package com.wanlong.iptv.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
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
import com.wanlong.iptv.entity.Login;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.mvp.HomePresenter;
import com.wanlong.iptv.server.AdService;
import com.wanlong.iptv.ui.weigets.MarqueeTextView;
import com.wanlong.iptv.utils.ActivityCollector;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.TimeUtils;
import com.wanlong.iptv.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomePresenter.HomeView, AdService.AdListener {

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
    @BindView(R.id.tv_setting)
    TextView mTvSetting;
    @BindView(R.id.tv_room)
    AppCompatTextView mTvRoom;
    @BindView(R.id.tv_message)
    AppCompatTextView mTvMessage;


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
    private LinearLayout.LayoutParams mParams;

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
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mMarqueeTextView.setFocusable(false);
        mMarqueeTextView.setClickable(false);
        mParams.weight = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mParams.setMargins(0, 16, 0, 16);
        mParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        mMarqueeTextView.setIncludeFontPadding(false);
        mMarqueeTextView.setTextSize(32f);
        mMarqueeTextView.setPadding(0, 0, 0, 0);
        mMarqueeTextView.setLayoutParams(mParams);
        mMarqueeTextView.setTextColor(Color.WHITE);
        mMarqueeTextView.setBackgroundColor(getResources().getColor(R.color.transparent));
        mMarqueeTextView.setText("");
        wm.addView(mMarqueeTextView, layoutParams);
        wm.updateViewLayout(mMarqueeTextView, layoutParams);
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void initView() {
        sharedPreferences = getSharedPreferences("PRISON-login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (App.PRISON) {
            autoLogin();
        }
        if (!Utils.isPhone(this)) {
            mTvLive.requestFocus();
        }
        getTime();
        initImgAd();
        mTvWelcomeGuest.setText(getString(R.string.room_number) + ":" +
                sharedPreferences.getString("room", Apis.ROOM_ORIGIN));
        mTvRoom.setText("Mac:" + Utils.getMac(this));
//        mTvMessage.setText("You have a new message. Please check it.");
    }

    //加载图片
    private void initImgAd() {
//        mImgShow.setOnClickListener(event -> {
//            Log.d("HomeActivity", "event.getId():" + event.getId());
//            Log.d("HomeActivity", "onclick");
//        });//Lambda 表达式
    }

    @Override
    protected void initData() {
        Logger.d("mac:" + Utils.getMac(this));
        mTimer.schedule(mTimerTask, 0, 1000);
        setPresenter(new HomePresenter(this));
        if (!App.ADserver) {
            startService(new Intent(HomeActivity.this, AdService.class));
            App.ADserver = true;
        }
        adCallback();
        getPresenter().loadHomeADData(Apis.HEADER + Apis.USER_HOME_AD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().loadHomeADData(Apis.HEADER + Apis.USER_HOME_AD);
    }

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
            case R.id.tv_setting:
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                break;
        }
    }

    private Login data;
    private String ip;

    private void autoLogin() {
        ip = sharedPreferences.getString("ip", "");
        if (ip.equals("")) {
            editor.putString("ip", Apis.HEADER);
            editor.commit();
            ip = sharedPreferences.getString("ip", "");
        }
        Apis.HEADER = ip;
        OkGo.<String>post(Apis.HEADER + Apis.USER_LOGIN)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params("mac", Utils.getMac(this))
                .params("uuid", App.sUUID.toString())
                .params("ip", Utils.getIpAddressString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            data = JSON.parseObject(response.body(), Login.class);
                            if (data != null && data.getCode() != null) {
                                if (data.getCode().equals("0")) {
//                                    Toast.makeText(HomeActivity.this, "用户未登录/即将过期", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("1")) {
                                    //存储
                                    loginSuccess();
//                                    Toast.makeText(HomeActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-1")) {
                                    loginFailed();
//                                    Toast.makeText(HomeActivity.this, "用户名或者密码输入不符合规则", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-2")) {
                                    loginFailed();
//                                    Toast.makeText(HomeActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-3")) {
                                    loginFailed();
//                                    Toast.makeText(HomeActivity.this, "达到最大连接数", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-4")) {
                                    loginFailed();
//                                    Toast.makeText(HomeActivity.this, "用户已过期", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-5")) {
                                    loginFailed();
//                                    Toast.makeText(HomeActivity.this, "服务器有错误", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-6")) {
                                    loginFailed();
//                                    Toast.makeText(HomeActivity.this, "用户名或者密码输入为空", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-7")) {
                                    loginFailed();
//                                    Toast.makeText(HomeActivity.this, "登陆过期", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                loginFailed();
                                Logger.d("HomeActivity:" + "服务器返回数据异常");
//                                Toast.makeText(HomeActivity.this, "服务器返回数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginFailed();
//                            Toast.makeText(HomeActivity.this, "服务器返回数据异常", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        super.onCacheSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        loginFailed();
                        super.onError(response);
                    }
                });
    }

    private boolean firstOpen;

    private void loginSuccess() {
        Logger.d("登录成功");
//        if (App.ADserver) {
//            stopService(new Intent(HomeActivity.this, AdService.class));
//            startService(new Intent(HomeActivity.this, AdService.class));
//            App.ADserver = true;
//        }
//        getTime();
//        mTimer.cancel();
//        mTimer.schedule(mTimerTask, 0, 1000);
//        getPresenter().loadHomeADData(Apis.HEADER + Apis.USER_HOME_AD);
//        sharedPreferences = getSharedPreferences("PRISON-login", Context.MODE_PRIVATE);
//        firstOpen = sharedPreferences.getBoolean("firstOpen", true);
//        if (firstOpen) {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("firstOpen", false);
//            editor.commit();
//        }
    }

    private void loginFailed() {
        Logger.d("登录失败");
//        Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show();
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
            } else {
                loadFailed(3);
            }
            if (homeAD.getAd_image() != null && homeAD.getAd_image().size() > 0) {
                mAdImageBeans.addAll(homeAD.getAd_image());
                showImgAD(mAdImageBeans);
            } else {
                loadFailed(3);
            }
            if (homeAD.getAd_text() != null && homeAD.getAd_text().size() > 0) {
                mAdTextBeans.addAll(homeAD.getAd_text());
                showTextAD();
            }
        } else {
            loadFailed(3);
        }
    }

    //显示文字广告
    private void showTextAD() {
        if (mAdTextBeans.size() > 0) {
            mTvMessage.setText(mAdTextBeans.get(0).getAd_detail());
        }
    }

    private List<String> imgUrls1;
    private List<String> imgUrls2;
    private List<String> imgUrls3;

    //显示图片广告
    private void showImgAD(List<HomeAD.AdImageBean> mAdImageBeans) {
        imgUrls1 = new ArrayList<>();
        imgUrls2 = new ArrayList<>();
        imgUrls3 = new ArrayList<>();
        for (int i = 0; i < mAdImageBeans.size(); i++) {
            if (mAdImageBeans.get(i).getAd_display_location().indexOf("1") != -1) {
                imgUrls1.add(mAdImageBeans.get(i).getAd_src());
            }
            if (mAdImageBeans.get(i).getAd_display_location().indexOf("2") != -1) {
                imgUrls2.add(mAdImageBeans.get(i).getAd_src());
            }
            if (mAdImageBeans.get(i).getAd_display_location().indexOf("3") != -1) {
                imgUrls3.add(mAdImageBeans.get(i).getAd_src());
            }
        }
        if (imgUrls1.size() > 0) {
            GlideApp.with(this)
                    .load(imgUrls1.get(0))
                    .transform(new RoundedCorners(12))
                    .into(mImgShow);
        } else {
            GlideApp.with(this)
                    .load(R.drawable.hotel_room)
                    .transform(new RoundedCorners(12))
                    .into(mImgShow);
        }
        if (imgUrls2.size() > 0) {
            GlideApp.with(this)
                    .load(imgUrls2.get(0))
                    .transform(new RoundedCorners(12))
                    .into(mImgWeather);
        } else {
            GlideApp.with(this)
                    .load(R.drawable.weather)
                    .transform(new RoundedCorners(12))
                    .into(mImgWeather);
        }
        if (imgUrls3.size() > 0) {
            GlideApp.with(this)
                    .load(imgUrls3.get(0))
                    .transform(new RoundedCorners(12))
                    .into(mImgAd);
        } else {
            GlideApp.with(this)
                    .load(R.drawable.sence)
                    .transform(new RoundedCorners(12))
                    .into(mImgAd);
        }
    }

    @Override
    public void loadFailed(int error) {
        Logger.e("HomeActivity:load failed");
        if (error == 3) {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(HomeActivity.this, AdService.class));
        App.ADserver = false;
        mTimer.cancel();
        mTimer = null;
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
                                try {
                                    App.newtime = Long.parseLong(response.body());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    App.newtime = System.currentTimeMillis() / 1000;
                                }
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
                if (Build.MODEL.equals("0008")) {
                    return true;
                } else {
                    new AlertDialog.Builder(HomeActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
                            .setTitle(getString(R.string.exitdialog_hint))
                            .setMessage(getString(R.string.exitdialog_out_hint))
                            .setPositiveButton(getString(R.string.exitdialog_out), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    stopService(new Intent(HomeActivity.this, AdService.class));
                                    App.getApplication().exit();
                                }
                            })
                            .setNegativeButton(getString(R.string.exitdialog_back), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                }
                            }).show();
                }
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

    private void adCallback() {
        AdService.setAdListener(this);
    }

    @Override
    public void showText(String text, String place, String font_size,
                         String back_color, String font_color, String lucency_size) {
        if (App.adText.equals("")) {
            App.adText = text;
        } else {
            App.adText = App.adText + "     " + text;
        }
        Logger.d(App.adText);
        Log.d("adtext", App.adText);
        if (mMarqueeTextView != null) {
            mMarqueeTextView.setText(App.adText);
            try {
                mMarqueeTextView.setTextSize(Integer.parseInt(font_size));
                mMarqueeTextView.setTextColor(Color.parseColor("#" + lucency_size + font_color));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showVideo(String type, String url) {
        Intent intent = new Intent(getApplicationContext(), AdActivity.class);
        intent.putExtra("url", url);
        ActivityCollector.activities.get(ActivityCollector.activities.size() - 1)
                .startActivity(intent);
    }

    @Override
    public void dismissAllText() {
        if (App.adText.equals("")) {

        } else {
            App.adText = "";
        }
        if (mMarqueeTextView != null) {
            mMarqueeTextView.setText(App.adText);
        }
    }

    @Override
    public void dismissText(String text) {
        if (App.adText.equals("")) {

        } else {
            App.adText = App.adText.replace(text, "");
        }
        if (mMarqueeTextView != null) {
            mMarqueeTextView.setText(App.adText);
        }
    }

    @Override
    public void dismissVideo() {
        if (ActivityCollector.activities.get(ActivityCollector.activities.size() - 1) instanceof AdActivity) {
            ActivityCollector.finishActivity(ActivityCollector.activities.size() - 1);
            Log.d("dismissVideo", "HomeActivity-dismissVideo");
        }
    }
}
