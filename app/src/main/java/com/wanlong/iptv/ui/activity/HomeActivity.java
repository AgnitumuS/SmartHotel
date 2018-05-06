package com.wanlong.iptv.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.HomeAD;
import com.wanlong.iptv.entity.Login;
import com.wanlong.iptv.imageloader.GlideImageLoader;
import com.wanlong.iptv.mvp.HomePresenter;
import com.wanlong.iptv.server.AdService;
import com.wanlong.iptv.utils.ActivityCollector;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.TimeUtils;
import com.wanlong.iptv.utils.Utils;
import com.wanlong.iptv.utils.WindowUtils;
import com.youth.banner.Banner;
import com.youth.banner.transformer.BackgroundToForegroundTransformer;
import com.youth.banner.transformer.ForegroundToBackgroundTransformer;

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
    Banner mImgShow;
    @BindView(R.id.img_weather)
    Banner mImgWeather;
    @BindView(R.id.img_ad)
    Banner mImgAd;
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
    @BindView(R.id.tv_self_management)
    TextView mTvSelfManagement;


    @Override
    protected int getContentResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initWindowManager() {
        super.initWindowManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                WindowUtils.initWindow(getApplicationContext());
            }
        } else {
            WindowUtils.initWindow(getApplicationContext());
        }
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void initView() {
        sharedPreferences = ApkVersion.getSP(this);
        editor = sharedPreferences.edit();
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            autoLogin();
        } else {
            mTvSelfManagement.setVisibility(View.GONE);
        }
        if (!Utils.isPhone(this)) {
            mTvLive.requestFocus();
        }
    }

    @Override
    protected void initData() {
        if (!App.ADserver) {
            startService(new Intent(getApplicationContext(), AdService.class));
            App.ADserver = true;
        }
        mTimer.schedule(mTimerTask, 0, 1000);
        adCallback();
        setPresenter(new HomePresenter(this));
        reflashData();
        Logger.d("mac:" + Utils.getMac(this));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        reflashData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reflashData();
    }

    //刷新首页数据
    private void reflashData() {
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            mTvWelcomeGuest.setText("上海市宝山监狱:" +
                    sharedPreferences.getString("group", Apis.ROOM_ORIGIN) + " " +
                    sharedPreferences.getString("stb_name", ""));
        } else {
            mTvWelcomeGuest.setText(getString(R.string.room_number) + ":" +
                    sharedPreferences.getString("room", Apis.ROOM_ORIGIN));
        }
        mTvRoom.setText("Mac:" + Utils.getMac(this));
        getTime();
        getPresenter().loadHomeADData(this, Apis.HEADER + Apis.USER_HOME_AD);
    }

    @OnClick({R.id.img_show, R.id.img_weather, R.id.img_ad, R.id.tv_live,
            R.id.tv_self_management, R.id.tv_vod, R.id.tv_setting})
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
            case R.id.tv_self_management:
                startActivity(new Intent(HomeActivity.this, SelfManagementActivity.class));
                break;
            case R.id.tv_vod:
                startActivity(new Intent(HomeActivity.this, VodListActivity.class));
                break;
            case R.id.tv_setting:
                startActivity(new Intent(HomeActivity.this, PasswordActivity.class));
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
                                loginSuccess();
                                if (data.getCode().equals("0")) {
//                                    Toast.makeText(HomeActivity.this, "用户未登录/即将过期", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("1")) {
                                    //存储
                                    loginSuccess();
                                    App.look_permission = true;
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
                                    App.look_permission = false;
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
        firstOpen = sharedPreferences.getBoolean("firstOpen", true);
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            try {
                editor.putString("group", data.getGroup());
                editor.putString("stb_name", data.getStb_name());
                editor.commit();
                mTvWelcomeGuest.setText("上海市宝山监狱:" +
                        sharedPreferences.getString("group", Apis.ROOM_ORIGIN) + " " +
                        sharedPreferences.getString("stb_name", ""));
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        editor.putString("ip", Apis.HEADER);
        editor.commit();
        if (firstOpen) {
            editor = sharedPreferences.edit();
            editor.putBoolean("firstOpen", false);
            editor.commit();
        }
    }

    private void loginFailed() {
        Logger.d("登录失败");
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

            }
            if (homeAD.getAd_image() != null && homeAD.getAd_image().size() > 0) {
                mAdImageBeans.addAll(homeAD.getAd_image());
                showImgAD(mAdImageBeans);
            } else {
                loadFailed(0);
            }
            if (homeAD.getAd_text() != null && homeAD.getAd_text().size() > 0) {
                mAdTextBeans.addAll(homeAD.getAd_text());
                showTextAD();
            } else {

            }
        } else {
            loadFailed(0);
        }
    }

    //显示文字广告
    private void showTextAD() {
        if (mAdTextBeans.size() > 0) {
//            mTvMessage.setText(mAdTextBeans.get(0).getAd_detail());
        }
    }

    private List<Object> imgUrls1;
    private List<Object> imgUrls2;
    private List<Object> imgUrls3;

    //显示图片广告
    private void showImgAD(List<HomeAD.AdImageBean> mAdImageBeans) {
        Logger.e("HomeActivity:load success");
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
            mImgShow.setImages(imgUrls1)
                    .setPageTransformer(true, new BackgroundToForegroundTransformer())
                    .setImageLoader(new GlideImageLoader())
                    .start();
        } else {
            loadFailed(1);
        }
        if (imgUrls2.size() > 0) {
            mImgWeather.setImages(imgUrls2)
                    .setPageTransformer(true, new ForegroundToBackgroundTransformer())
                    .setImageLoader(new GlideImageLoader())
                    .start();
        } else {
            loadFailed(2);
        }
        if (imgUrls3.size() > 0) {
            mImgAd.setImages(imgUrls3)
                    .setImageLoader(new GlideImageLoader())
                    .start();
        } else {
            loadFailed(3);
        }
    }

    @Override
    public void loadFailed(int error) {
        Logger.e("HomeActivity:load failed");
        switch (error) {
            case 0:
                loadDefaultImg(1);
                loadDefaultImg(2);
                loadDefaultImg(3);
                break;
            case 1:
                loadDefaultImg(1);
                break;
            case 2:
                loadDefaultImg(2);
                break;
            case 3:
                loadDefaultImg(3);
                break;
        }
    }

    //加载默认图片
    private void loadDefaultImg(int error) {
        if (error == 1) {
            imgUrls1 = new ArrayList<>();
            imgUrls1.add(getResources().getDrawable(R.drawable.hotel_room));
            mImgShow.setImages(imgUrls1)
                    .setPageTransformer(true, new BackgroundToForegroundTransformer())
                    .setImageLoader(new GlideImageLoader())
                    .start();
        }
        if (error == 2) {
            imgUrls2 = new ArrayList<>();
            imgUrls2.add(getResources().getDrawable(R.drawable.weather));
            mImgWeather.setImages(imgUrls2)
                    .setPageTransformer(true, new ForegroundToBackgroundTransformer())
                    .setImageLoader(new GlideImageLoader())
                    .start();
        }
        if (error == 3) {
            imgUrls3 = new ArrayList<>();
            imgUrls3.add(getResources().getDrawable(R.drawable.sence));
            mImgAd.setImages(imgUrls3)
                    .setImageLoader(new GlideImageLoader())
                    .start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        mImgShow.stopAutoPlay();
        mImgWeather.stopAutoPlay();
        mImgAd.stopAutoPlay();
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
                    String str_dayofWeek = getResources()
                            .getStringArray(R.array.day_of_week)[TimeUtils.getDay(App.newtime * 1000) - 1];
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
            if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
                if (Build.MODEL.equals("0008") || Build.MODEL.equals("Prevail CATV")) {
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
        WindowUtils.getText(getApplicationContext()).setText(App.adText);
        try {
            WindowUtils.getText(getApplicationContext()).setTextSize(Integer.parseInt(font_size));
            if (lucency_size.length() == 1) {
                lucency_size = "0" + lucency_size;
            } else {

            }
            WindowUtils.getText(getApplicationContext())
                    .setTextColor(Color.parseColor("#" + lucency_size + font_color));
            WindowUtils.addText();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
        if (WindowUtils.getText(getApplicationContext()) != null) {
            WindowUtils.removeText();
        }
    }
}
