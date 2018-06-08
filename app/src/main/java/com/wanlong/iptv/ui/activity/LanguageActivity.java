package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.LanguageSwitchUtils;
import com.wanlong.iptv.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lingchen on 2018/1/24. 14:27
 * mail:lingchen52@foxmail.com
 */
public class LanguageActivity extends BaseActivity {
    @BindView(R.id.tv_guest_name)
    TextView mTvGuestName;
    @BindView(R.id.tv_hotel_welcome_text)
    TextView mTvHotelWelcomeText;
    @BindView(R.id.btn_chinese)
    Button mBtnChinese;
    @BindView(R.id.btn_english)
    Button mBtnEnglish;
    @BindView(R.id.img_language_activity)
    ImageView mImgLanguageActivity;
    @BindView(R.id.btn_thai)
    Button mBtnThai;
    @BindView(R.id.btn_russian)
    Button mBtnRussian;
    @BindView(R.id.btn_armenia)
    Button mBtnArmenia;

    @Override
    protected int getContentResId() {
        return R.layout.activity_language;
    }

    @Override
    protected void initView() {
        GlideApp.with(this)
                .load(R.drawable.wooden_house)
                .centerCrop()
                .into(mImgLanguageActivity);
        if (!Utils.isPhone(this)) {
            mBtnThai.requestFocus();
        }
    }

    private SharedPreferences sharedPreferences;
    private boolean firstOpen;

    @Override
    protected void initData() {
        sharedPreferences = ApkVersion.getSP(this);
        firstOpen = sharedPreferences.getBoolean("firstOpen", true);
    }

    @OnClick({R.id.btn_chinese, R.id.btn_english, R.id.btn_thai, R.id.btn_russian, R.id.btn_armenia})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chinese:
                LanguageSwitchUtils.languageSwitch(this, LanguageSwitchUtils.CHINESE);
                toHomeActivity();
                break;
            case R.id.btn_english:
                LanguageSwitchUtils.languageSwitch(this, LanguageSwitchUtils.ENGLISH);
                toHomeActivity();
                break;
            case R.id.btn_thai:
                LanguageSwitchUtils.languageSwitch(this, LanguageSwitchUtils.THAI);
                toHomeActivity();
                break;
            case R.id.btn_russian:
                LanguageSwitchUtils.languageSwitch(this, LanguageSwitchUtils.RUSSIAN);
                toHomeActivity();
                break;
            case R.id.btn_armenia:
                LanguageSwitchUtils.languageSwitch(this, LanguageSwitchUtils.ARMENIA);
                toHomeActivity();
                break;
        }
    }

    private void toHomeActivity() {
        Intent intent = new Intent(LanguageActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 2s内点击退出
     */
    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - exitTime) < 2000) {
            if (firstOpen) {
                return true;
            } else {
//                startActivity(new Intent(LanguageActivity.this, HomeActivity.class));
                finish();
            }
//            } else {
//                exitTime = System.currentTimeMillis();
//            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
