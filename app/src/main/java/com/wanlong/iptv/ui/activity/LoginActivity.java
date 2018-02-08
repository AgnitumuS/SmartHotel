package com.wanlong.iptv.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lingchen on 2018/1/24. 14:59
 * mail:lingchen52@foxmail.com
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_deviceID)
    TextView mTvDeviceID;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.tv_hotel_login)
    TextView mTvHotelLogin;
    @BindView(R.id.btn_system_setting)
    Button mBtnSystemSetting;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.btn_login_setting)
    Button mBtnLoginSetting;

    @Override
    protected int getContentResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        if (!Utils.isPhone(this)) {
            mBtnLogin.requestFocus();
        }
        mTvDeviceID.setText(getString(R.string.device_id) + " " + App.sUUID.toString());
        mTvVersion.setText(getString(R.string.version) + " " + getString(R.string.versionName));
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_login, R.id.btn_system_setting, R.id.btn_login_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(LoginActivity.this, LanguageActivity.class));
                finish();
                break;
            case R.id.btn_system_setting:
                Intent intent = new Intent(Settings.ACTION_SETTINGS); //进入到系统设置界面
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_login_setting:
                startActivity(new Intent(LoginActivity.this, LoginSettingActivity.class));
                break;
        }
    }

    /**
     * 2s内点击退出
     */
    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) < 2000) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(getString(R.string.exitdialog_hint))
                        .setMessage(getString(R.string.exitdialog_out_hint))
                        .setPositiveButton(getString(R.string.exitdialog_out), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                App.getApplication().exit();
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
