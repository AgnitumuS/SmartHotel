package com.wanlong.iptv.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.utils.Apis;

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

    @Override
    protected int getContentResId() {
        return R.layout.activity_login_setting;
    }

    @Override
    protected void initView() {
        mEditIp.setText(Apis.HEADER);
        mEditRoom.setText("501");
//        if (!Utils.isPhone(this)) {
//            mEditIp.requestFocus();
//        }
        initListener();
//        mEditIp.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View arg0, MotionEvent event) {
//                // TODO Auto-generated method stub
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    InputMethodManager imm = (InputMethodManager) LoginSettingActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                    //activity要换成自己的activity名字
//                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//                }
//                return true;
//            }
//        });
//        mEditRoom.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View arg0, MotionEvent event) {
//                // TODO Auto-generated method stub
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    InputMethodManager imm = (InputMethodManager) LoginSettingActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                    //activity要换成自己的activity名字
//                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//                }
//                return true;
//            }
//        });
    }

    @Override
    protected void initData() {

    }

    private String newIP = "";
    private boolean changeIP;

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

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.btn_submit_ip, R.id.btn_submit_room, R.id.btn_recovery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_ip:
                submitIP();
                break;
            case R.id.btn_submit_room:
                break;
            case R.id.btn_recovery:
                mEditIp.setText(Apis.HEADER);
                break;
        }
    }

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
                                    Toast.makeText(LoginSettingActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
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
        }
    }

    private SharedPreferences sharedPreferences;

    private void saveIP() {
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ip", newIP);
        editor.commit();
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
////            startActivity(new Intent(LoginSettingActivity.this, LoginActivity.class));
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
