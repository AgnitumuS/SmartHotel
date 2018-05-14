package com.wanlong.iptv.ui.activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.Login;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lingchen on 2018/1/24. 15:28
 * mail:lingchen52@foxmail.com
 */
public class LoginSettingActivity extends BaseActivity {
    @BindView(R.id.tv_server_address)
    TextView mTvServerAddress;
    @BindView(R.id.edit_ip)
    EditText mEditIp;
    @BindView(R.id.btn_submit_ip)
    Button mBtnSubmitIp;
    @BindView(R.id.tv_room_number)
    TextView mTvRoomNumber;
    @BindView(R.id.edit_room)
    EditText mEditRoom;
    @BindView(R.id.btn_submit_room)
    Button mBtnSubmitRoom;
    @BindView(R.id.btn_recovery)
    Button mBtnRecovery;
    @BindView(R.id.btn_number_0)
    Button mBtnNumber0;
    @BindView(R.id.btn_number_1)
    Button mBtnNumber1;
    @BindView(R.id.btn_number_2)
    Button mBtnNumber2;
    @BindView(R.id.btn_number_3)
    Button mBtnNumber3;
    @BindView(R.id.btn_number_4)
    Button mBtnNumber4;
    @BindView(R.id.btn_number_5)
    Button mBtnNumber5;
    @BindView(R.id.btn_number_6)
    Button mBtnNumber6;
    @BindView(R.id.btn_number_7)
    Button mBtnNumber7;
    @BindView(R.id.btn_number_8)
    Button mBtnNumber8;
    @BindView(R.id.btn_number_9)
    Button mBtnNumber9;
    @BindView(R.id.btn_keycode_del)
    Button mBtnKeycodeDel;

    @Override
    protected int getContentResId() {
        return R.layout.activity_login_setting;
    }

    @Override
    protected void initView() {
        mEditIp.setText(Apis.HEADER);
        mEditIp.setSelection(Apis.HEADER.length());
        initListener();
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            mTvRoomNumber.setText("上海市宝山监狱");
        }
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void initData() {
        sharedPreferences = ApkVersion.getSP(this);
        editor = sharedPreferences.edit();
        mEditRoom.setText(sharedPreferences.getString("room", Apis.ROOM_ORIGIN));
    }

    private String newIP = "";
    private boolean changeIP;
    private boolean changeRoom;
    private String newRoom = "";

    //监听输入
    private void initListener() {
        mEditIp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeIP = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                newIP = s.toString();
                Logger.d("newIP:" + newIP);
            }
        });
        mEditRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeRoom = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                newRoom = s.toString();
                Logger.d("newRoom:" + newRoom);
            }
        });
    }

    @OnClick({R.id.btn_number_0, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3,
            R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7,
            R.id.btn_number_8, R.id.btn_number_9, R.id.btn_keycode_del, R.id.btn_submit_ip,
            R.id.btn_submit_room, R.id.btn_recovery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_number_0:
                inputNumber(KeyEvent.KEYCODE_0);
                break;
            case R.id.btn_number_1:
                inputNumber(KeyEvent.KEYCODE_1);
                break;
            case R.id.btn_number_2:
                inputNumber(KeyEvent.KEYCODE_2);
                break;
            case R.id.btn_number_3:
                inputNumber(KeyEvent.KEYCODE_3);
                break;
            case R.id.btn_number_4:
                inputNumber(KeyEvent.KEYCODE_4);
                break;
            case R.id.btn_number_5:
                inputNumber(KeyEvent.KEYCODE_5);
                break;
            case R.id.btn_number_6:
                inputNumber(KeyEvent.KEYCODE_6);
                break;
            case R.id.btn_number_7:
                inputNumber(KeyEvent.KEYCODE_7);
                break;
            case R.id.btn_number_8:
                inputNumber(KeyEvent.KEYCODE_8);
                break;
            case R.id.btn_number_9:
                inputNumber(KeyEvent.KEYCODE_9);
                break;
            case R.id.btn_keycode_del:
                inputNumber(KeyEvent.KEYCODE_DEL);
                break;
            case R.id.btn_submit_ip:
                submitIP();
                break;
            case R.id.btn_submit_room:
                submitRoom();
                break;
            case R.id.btn_recovery:
                mEditIp.setText(Apis.HEADER_ORIGIN);
                mEditIp.setSelection(Apis.HEADER_ORIGIN.length());
                mEditRoom.setText(Apis.ROOM_ORIGIN);
                break;
        }
    }

    //输入模拟按键
    private void inputNumber(int keycode) {
        mEditIp.requestFocus();
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keycode);
                } catch (Exception e) {
                    Log.e("sendPointerSync", e.toString());
                }
            }
        }.start();
    }

    //提交房间号
    private void submitRoom() {
        if (changeRoom) {
            if (!newIP.equals(Apis.ROOM_ORIGIN)) {
                saveRoom();
            }
        }
    }

    //保存房间号
    private void saveRoom() {
        editor.putString("room", newRoom);
        editor.commit();
        Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
    }

    //验证IP地址
    private void submitIP() {
        if (changeIP) {
            if (newIP.equals("") && !newIP.startsWith("http://")) {
                Toast.makeText(this, "请输入正确的IP地址", Toast.LENGTH_SHORT).show();
            } else {
                OkGo.<String>get(newIP + Apis.TIME_UPDATE)
                        .tag(this)
                        .cacheMode(CacheMode.NO_CACHE)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response != null && response.body() != null && !response.body().equals("")) {
//                                    Toast.makeText(LoginSettingActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                    saveIP();
                                } else {
                                    Toast.makeText(LoginSettingActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                Toast.makeText(LoginSettingActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            login();
        }
    }

    //保存IP地址
    private void saveIP() {
        Apis.HEADER = newIP;
        editor.putString("ip", newIP);
        editor.commit();
        login();
    }

    private Login data;
    private String ip;

    //登录
    private void login() {
//        Toast.makeText(LoginActivity.this, "正在登录", Toast.LENGTH_SHORT).show();
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
                                if (data.getCode().equals("0")) {
//                                    Toast.makeText(LoginSettingActivity.this, "用户未登录/即将过期", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("1")) {
                                    //存储
                                    loginSuccess();
                                    App.look_permission = true;
                                    Toast.makeText(LoginSettingActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-1")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "用户名或者密码输入不符合规则", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-2")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-3")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "达到最大连接数", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-4")) {
                                    App.look_permission = false;
                                    loginFailed();
                                    Toast.makeText(LoginSettingActivity.this, "用户未注册", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-5")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "服务器有错误", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-6")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "用户名或者密码输入为空", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-7")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "登陆过期", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                loginFailed();
//                                Log.d("ServerSettingActivity", "服务器返回数据异常");
//                                Toast.makeText(LoginSettingActivity.this, "服务器返回数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginFailed();
//                            Toast.makeText(LoginSettingActivity.this, "服务器返回数据异常", Toast.LENGTH_SHORT).show();
                        }

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
        sharedPreferences = ApkVersion.getSP(this);
        firstOpen = sharedPreferences.getBoolean("firstOpen", true);
        if (firstOpen) {
            editor = sharedPreferences.edit();
            editor.putBoolean("firstOpen", false);
            editor.commit();
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            Intent intent = new Intent(LoginSettingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(LoginSettingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
//            startActivity(new Intent(LoginSettingActivity.this, LanguageActivity.class));
//            finish();
        }
    }

    private void loginFailed() {
        Logger.d("登录失败");
        Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            startActivity(new Intent(LoginSettingActivity.this, LoginActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }
}
