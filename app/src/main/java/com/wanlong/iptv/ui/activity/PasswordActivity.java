package com.wanlong.iptv.ui.activity;

import android.content.Intent;
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

import butterknife.BindView;
import butterknife.OnClick;

public class PasswordActivity extends BaseActivity {

    @BindView(R.id.edit_password)
    EditText mEditPassword;
    @BindView(R.id.btn_password)
    Button mBtnPassword;
    @BindView(R.id.re_password_check_up)
    RelativeLayout mRePasswordCheckUp;
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
    @BindView(R.id.ll_keyboard_number)
    LinearLayout mLlKeyboardNumber;

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
                checkPassword();
            }
        });
    }

    //检查密码
    private void checkPassword() {
        if (password.length() == 8) {
            if (password.equals(Apis.SETTING_PASSWORD)) {
                mEditPassword.setText("");
                startActivity(new Intent(PasswordActivity.this, SettingActivity.class));
                finish();
            } else {
                password = "";
                sb = new StringBuffer(password);
                mEditPassword.setText(password);
                Toast.makeText(PasswordActivity.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
            }
        } else if (password.length() < 8) {
            sb = new StringBuffer(password);
        }
    }

    @OnClick({R.id.btn_password, R.id.btn_number_0, R.id.btn_number_1, R.id.btn_number_2,
            R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6,
            R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_password:
                checkPassword();
                break;
            case R.id.btn_number_0:
                inputNumber(0);
                break;
            case R.id.btn_number_1:
                inputNumber(1);
                break;
            case R.id.btn_number_2:
                inputNumber(2);
                break;
            case R.id.btn_number_3:
                inputNumber(3);
                break;
            case R.id.btn_number_4:
                inputNumber(4);
                break;
            case R.id.btn_number_5:
                inputNumber(5);
                break;
            case R.id.btn_number_6:
                inputNumber(6);
                break;
            case R.id.btn_number_7:
                inputNumber(7);
                break;
            case R.id.btn_number_8:
                inputNumber(8);
                break;
            case R.id.btn_number_9:
                inputNumber(9);
                break;
        }
    }

    private StringBuffer sb = new StringBuffer("");

    private void inputNumber(int number) {
        sb.append("" + number);
        mEditPassword.setText(sb.toString());//设置EditText控件的内容
        mEditPassword.setSelection(sb.length());//将光标移至文字末尾
    }
}
