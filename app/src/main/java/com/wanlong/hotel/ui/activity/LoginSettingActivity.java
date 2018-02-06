package com.wanlong.hotel.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wanlong.hotel.R;
import com.wanlong.hotel.utils.Utils;

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
        if (!Utils.isPhone(this)) {
            mBtnSubmitIp.requestFocus();
        }
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
}
