package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wanlong.iptv.R;
import com.wanlong.iptv.utils.Apis;

import butterknife.BindView;
import butterknife.OnClick;

public class PasswordActivity extends BaseActivity {

    @BindView(R.id.edit_password)
    EditText mEditPassword;
    @BindView(R.id.btn_password)
    Button mBtnPassword;
    @BindView(R.id.re_password_check_up)
    RelativeLayout mRePasswordCheckUp;

    @Override
    protected int getContentResId() {
        return R.layout.activity_password;
    }

    @Override
    protected void initView() {

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
                        mEditPassword.setText("");
                        startActivity(new Intent(PasswordActivity.this, SettingActivity.class));
                        finish();
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_password)
    public void onViewClicked() {
        if (password.length() == 6) {
            if (password.equals(Apis.SETTING_PASSWORD)) {
                mEditPassword.setText("");
                startActivity(new Intent(PasswordActivity.this, SettingActivity.class));
                finish();
            } else {
                Toast.makeText(this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
