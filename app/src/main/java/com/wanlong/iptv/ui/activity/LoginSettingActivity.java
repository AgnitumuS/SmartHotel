package com.wanlong.iptv.ui.activity;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        mEditIp.setText(Apis.HEADER+"api");
        mEditRoom.setText("501");
//        if (!Utils.isPhone(this)) {
//            mEditIp.requestFocus();
//        }
        mEditIp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodManager imm = (InputMethodManager) LoginSettingActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    //activity要换成自己的activity名字
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                }
                return true;
            }
        });
        mEditRoom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodManager imm = (InputMethodManager) LoginSettingActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    //activity要换成自己的activity名字
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                }
                return true;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_submit_ip, R.id.btn_submit_room, R.id.btn_recovery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_ip:
                break;
            case R.id.btn_submit_room:
                break;
            case R.id.btn_recovery:
                break;
        }
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
