package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.HomeTypeData;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.mvp.HomePresenter;
import com.wanlong.iptv.ui.weigets.MarqueeTextView;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.TimeUtils;
import com.wanlong.iptv.utils.Utils;

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
    @BindView(R.id.tv_services)
    TextView mTvServices;
    @BindView(R.id.tv_cuisines)
    TextView mTvCuisines;
    @BindView(R.id.tv_scnenries)
    TextView mTvScnenries;
    @BindView(R.id.tv_expense)
    TextView mTvExpense;
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
    protected void initView() {
        if (!Utils.isPhone(this)) {
            mTvLive.requestFocus();
        }
        initImgAd();
        mTvMessage.setText("You have a new message. Please check it.");
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
        mTimer.schedule(mTimerTask, 0, 1000);
        setPresenter(new HomePresenter(this));
        getPresenter().loadTypeData(Apis.HEADER + Apis.HOME_AD);
        getPresenter().loadMsgData(Apis.HEADER + Apis.HOME_MSG);
    }

    @OnClick({R.id.img_show, R.id.img_weather, R.id.img_ad, R.id.tv_live, R.id.tv_vod, R.id.tv_services,
            R.id.tv_cuisines, R.id.tv_scnenries, R.id.tv_expense, R.id.tv_setting})
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
            case R.id.tv_services:
                startActivity(new Intent(HomeActivity.this, ServicesActivity.class));
                break;
            case R.id.tv_cuisines:
                startActivity(new Intent(HomeActivity.this, CuisinesActivity.class));
                break;
            case R.id.tv_scnenries:
                startActivity(new Intent(HomeActivity.this, SceneriesActivity.class));
                break;
            case R.id.tv_expense:
                startActivity(new Intent(HomeActivity.this, ExpenseActivity.class));
                break;
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

    @Override
    public void loadFailed() {
        Logger.e("HomeActivity:load failed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimer = null;
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
                    String date = TimeUtils.stampToDate(System.currentTimeMillis());
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
            startActivity(new Intent(HomeActivity.this, LanguageActivity.class));
            finish();
//            } else {
//                Toast.makeText(this, R.string.click_again_to_welcome_activity, Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
