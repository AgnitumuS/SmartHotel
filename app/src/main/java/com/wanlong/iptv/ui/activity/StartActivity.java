package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.server.UpdateService;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkUtils;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.LanguageSwitchUtils;
import com.wanlong.iptv.utils.Utils;

import butterknife.BindView;

/**
 * Created by lingchen on 2018/1/24. 14:25
 * mail:lingchen52@foxmail.com
 */
public class StartActivity extends BaseActivity {
    @BindView(R.id.img_start)
    ImageView mImgStart;
    @BindView(R.id.img_bg)
    ImageView mImgBg;

    @Override
    protected int getContentResId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        if (ApkVersion.CURRENT_VERSION == ApkVersion.STANDARD_VERSION) {
            mImgBg.setVisibility(View.GONE);
            startAnimation();
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            mImgStart.setVisibility(View.GONE);
        }
    }

    private void startAnimation() {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_image);
        mImgStart.setAnimation(loadAnimation);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.sendEmptyMessageDelayed(OPEN, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private static final int OPEN = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean firstOpen;
    private String ip;
    private boolean hasDTV;
    private String mac;
    private String model;

    @Override
    protected void initData() {
        createSP();
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            if (Build.MODEL.equals("0008")) {
                startService(new Intent(getApplicationContext(), UpdateService.class));
            }
            mHandler.sendEmptyMessageDelayed(OPEN, 1000);
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.STANDARD_VERSION) {
            if (Build.MODEL.equals("KI PLUS")) {
                startService(new Intent(getApplicationContext(), UpdateService.class));
            }
        }
    }

    //创建SharedPreferences
    private void createSP() {
        sharedPreferences = ApkVersion.getSP(this);
        editor = sharedPreferences.edit();
        firstOpen = sharedPreferences.getBoolean("firstOpen", true);
        ip = sharedPreferences.getString("ip", "");
        if (ip.equals("")) {
            editor.putString("ip", Apis.HEADER);
            editor.commit();
            ip = sharedPreferences.getString("ip", "");
        }
        Apis.HEADER = ip;
        mac = Utils.getMac(this);
        model = Build.MODEL;
        Logger.d("mac:" + mac);
        Logger.d("model:" + model);
        hasDTV = ApkUtils.isAvailable(this, "th.dtv");
        if (mac != null && !mac.equals("") && !mac.equals("02:00:00:00:00:00")) {
            editor.putString("mac", mac);
        } else {
            editor.putString("mac", "");
        }
        editor.putString("model", model);
        editor.putBoolean("hasDTV", hasDTV);
        editor.commit();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OPEN:
                    if (ApkVersion.CURRENT_VERSION == ApkVersion.STANDARD_VERSION) {
                        if (firstOpen) {
                            toActivity(2);
                        } else {
                            LanguageSwitchUtils.languageSwitch(StartActivity.this,
                                    sharedPreferences.getInt("language", 0));
                            toActivity(1);
                        }
                    } else if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
                        toActivity(1);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void toActivity(int activity) {
        Intent intent = null;
        if (activity == 1) {
            intent = new Intent(StartActivity.this, HomeActivity.class);
        } else if (activity == 2) {
            intent = new Intent(StartActivity.this, LanguageActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
