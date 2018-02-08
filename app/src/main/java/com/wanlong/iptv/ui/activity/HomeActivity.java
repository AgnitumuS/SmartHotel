package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.HomeData;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.mvp.HomePresenter;
import com.wanlong.iptv.utils.TimeUtils;
import com.wanlong.iptv.utils.Utils;

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
    TextView mTvRoom;
    @BindView(R.id.tv_message)
    TextView mTvMessage;


    @Override
    protected int getContentResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        if (!Utils.isPhone(this)) {
            mTvLive.requestFocus();
        }
        GlideApp.with(this)
                .load(R.drawable.wooden_house)
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
        new TimeThread().start();
        setPresenter(new HomePresenter(this));
//        getPresenter().loadLiveData("");
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
                startActivity(new Intent(HomeActivity.this, ScnenriesActivity.class));
                break;
            case R.id.tv_expense:
                startActivity(new Intent(HomeActivity.this, ExpenseActivity.class));
                break;
            case R.id.tv_setting:
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                break;
        }
    }

    @Override
    public void loadDataSuccess(HomeData homeData) {

    }

    @Override
    public void loadFailed() {

    }

    //时钟
    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    mHandler.sendEmptyMessage(UPDATE_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

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
            if ((System.currentTimeMillis() - exitTime) < 2000) {
//                new AlertDialog.Builder(HomeActivity.this)
//                        .setTitle(getString(R.string.exitdialog_hint))
//                        .setMessage(getString(R.string.exitdialog_out_hint))
//                        .setPositiveButton(getString(R.string.exitdialog_out), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                startActivity(new Intent(HomeActivity.this, LanguageActivity.class));
                finish();
//                            }
//                        })
//                        .setNegativeButton(getString(R.string.exitdialog_back), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {//响应事件
//                            }
//                        }).show();
            } else {
                Toast.makeText(this, R.string.click_again_to_welcome_activity, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
