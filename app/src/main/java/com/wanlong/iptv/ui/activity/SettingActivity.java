package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wanlong.iptv.R;
import com.wanlong.iptv.utils.Apis;
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
    @BindView(R.id.edit_password)
    EditText mEditPassword;
    @BindView(R.id.btn_password)
    Button mBtnPassword;
    @BindView(R.id.re_password_check_up)
    RelativeLayout mRePasswordCheckUp;

    @Override
    protected int getContentResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        mEditPassword.requestFocus();
    }

    private void initBorder() {
        if (!Utils.isPhone(this)) {
            BorderView borderView = new BorderView(this);
            BorderEffect.mScale = 1.05f;
            borderView.setBackgroundResource(R.drawable.border_touming);
            borderView.attachTo(mLl1);
            borderView.attachTo(mLl2);
        }
    }

    private String password = "";

    @Override
    protected void initData() {
        mEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
                if (password.length() == 6) {
                    if (password.equals(Apis.SETTING_PASSWORD)) {
                        mRePasswordCheckUp.setVisibility(View.GONE);
                        mLl.setVisibility(View.VISIBLE);
                        initBorder();
                    }
                }
            }
        });
    }

    @OnClick({R.id.btn_password, R.id.system_setting, R.id.login_setting, R.id.play_setting,
            R.id.report_setting, R.id.update_setting, R.id.about_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_password:
                if (password.equals(Apis.SETTING_PASSWORD)) {
                    mRePasswordCheckUp.setVisibility(View.GONE);
                    mLl.setVisibility(View.VISIBLE);
                    initBorder();
                } else {
                    Toast.makeText(this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                }
                break;
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
