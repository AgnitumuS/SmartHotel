package com.wanlong.hotel.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wanlong.hotel.R;
import com.wanlong.hotel.utils.LanguageSwitchUtils;
import com.wanlong.hotel.utils.Utils;

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

    @Override
    protected int getContentResId() {
        return R.layout.activity_language;
    }

    @Override
    protected void initView() {
        if (!Utils.isPhone(this)) {
            mBtnChinese.requestFocus();
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_chinese, R.id.btn_english})
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
            if ((System.currentTimeMillis() - exitTime) < 2000) {
                new AlertDialog.Builder(LanguageActivity.this)
                        .setTitle(getString(R.string.exitdialog_hint))
                        .setMessage(getString(R.string.exitdialog_out_hint))
                        .setPositiveButton(getString(R.string.exitdialog_out), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                startActivity(new Intent(LanguageActivity.this, LoginActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.exitdialog_back), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//响应事件
                            }
                        }).show();
            } else {
                exitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
