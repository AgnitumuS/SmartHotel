package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wanlong.iptv.R;
import com.wanlong.iptv.utils.Utils;

import org.evilbinary.tv.widget.BorderEffect;
import org.evilbinary.tv.widget.BorderView;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.system_setting)
    Button mSystemSetting;
    @BindView(R.id.login_setting)
    Button mLoginSetting;
    @BindView(R.id.play_setting)
    Button mPlaySetting;
    @BindView(R.id.ll_1)
    LinearLayout mLl1;
    @BindView(R.id.report_setting)
    Button mReportSetting;
    @BindView(R.id.update_setting)
    Button mUpdateSetting;
    @BindView(R.id.about_setting)
    Button mAboutSetting;
    @BindView(R.id.ll_2)
    LinearLayout mLl2;
    @BindView(R.id.ll)
    LinearLayout mLl;

    @Override
    protected int getContentResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initBorder();
    }

    private void initBorder() {
        if (!Utils.isPhone(this)) {
            BorderView borderView = new BorderView(this);
            BorderEffect.mScale = 1.05f;
            borderView.setBackgroundResource(R.drawable.border_touming);
            borderView.attachTo(mLl1);
            borderView.attachTo(mLl2);
            mSystemSetting.requestFocus();
            mSystemSetting.requestFocus();
        }
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.system_setting, R.id.login_setting, R.id.play_setting,
            R.id.report_setting, R.id.update_setting, R.id.about_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.system_setting:
                Intent intent1 = new Intent(Settings.ACTION_SETTINGS); //进入到系统设置界面
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
            case R.id.login_setting:
                Intent intent2 = new Intent(SettingActivity.this, LoginSettingActivity.class);
                intent2.putExtra("from", "SettingActivity");
                intent2.putExtra("firstOpen", false);
                startActivity(intent2);
                break;
            case R.id.play_setting:
                break;
            case R.id.report_setting:
                break;
            case R.id.update_setting:
                startActivity(new Intent(SettingActivity.this, UpdateActivity.class));
                break;
            case R.id.about_setting:
                break;
        }
    }
}
