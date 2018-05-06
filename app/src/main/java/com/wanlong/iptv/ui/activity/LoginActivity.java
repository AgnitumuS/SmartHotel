package com.wanlong.iptv.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.Login;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkVersion;
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
    @BindView(R.id.img_bg_login)
    ImageView mImgBgLogin;

    @Override
    protected int getContentResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        GlideApp.with(this).load(R.drawable.bg_login).into(mImgBgLogin);
        if (!Utils.isPhone(this)) {
            mBtnLogin.requestFocus();
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            mTvDeviceID.setText("MAC:" + Utils.getMac(this));
        } else {
            mTvDeviceID.setText(getString(R.string.device_id) + " " + App.sUUID.toString());
        }
        mTvVersion.setText(getString(R.string.version) + " " + getString(R.string.versionName));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_login, R.id.btn_system_setting, R.id.btn_login_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
//                loginSuccess();
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

    private Login data;
    private String ip;

    //登录
    private void login() {
//        Toast.makeText(LoginActivity.this, "正在登录", Toast.LENGTH_SHORT).show();
        sharedPreferences = ApkVersion.getSP(this);
        ip = sharedPreferences.getString("ip", "");
        if (ip.equals("")) {
            editor = sharedPreferences.edit();
            editor.putString("ip", Apis.HEADER);
            editor.commit();
            ip = sharedPreferences.getString("ip", "");
        }
        Apis.HEADER = ip;
        OkGo.<String>post(Apis.HEADER + Apis.USER_LOGIN)
                .tag(this)
//                .params("mac", Utils.getMac(this))
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
                                    Toast.makeText(LoginActivity.this, "用户未登录/即将过期", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("1")) {
                                    //存储
                                    loginSuccess();
                                    Toast.makeText(LoginActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-1")) {
                                    loginFailed();
                                    Toast.makeText(LoginActivity.this, "用户名或者密码输入不符合规则", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-2")) {
                                    loginFailed();
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-3")) {
                                    loginFailed();
                                    Toast.makeText(LoginActivity.this, "达到最大连接数", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-4")) {
                                    loginFailed();
                                    Toast.makeText(LoginActivity.this, "用户已过期", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-5")) {
                                    loginFailed();
                                    Toast.makeText(LoginActivity.this, "服务器有错误", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-6")) {
                                    loginFailed();
                                    Toast.makeText(LoginActivity.this, "用户名或者密码输入为空", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-7")) {
                                    loginFailed();
                                    Toast.makeText(LoginActivity.this, "登陆过期", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                loginFailed();
                                Log.d("ServerSettingActivity", "服务器返回数据异常");
                                Toast.makeText(LoginActivity.this, "服务器返回数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginFailed();
                            Toast.makeText(LoginActivity.this, "服务器返回数据异常", Toast.LENGTH_SHORT).show();
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

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean firstOpen;

    private void loginSuccess() {
        Logger.d("登录成功");
        sharedPreferences = ApkVersion.getSP(this);
        firstOpen = sharedPreferences.getBoolean("firstOpen", true);
        if (firstOpen) {
            editor = sharedPreferences.edit();
            editor.putBoolean("firstOpen", false);
            editor.commit();
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        } else {
            startActivity(new Intent(LoginActivity.this, LanguageActivity.class));
            finish();
        }
    }

    private void loginFailed() {
        Logger.d("登录失败");
        Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 2s内点击退出
     */
    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - exitTime) < 2000) {
            switch (Build.MODEL) {
                case "S905W":
                case "Prevail CATV":
                case "0008":
                    return true;
                default:
                    new AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
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
                    break;
            }
//            } else {
//                exitTime = System.currentTimeMillis();
//            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
