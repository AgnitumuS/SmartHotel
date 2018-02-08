package com.wanlong.iptv.ui.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.wanlong.iptv.R;

public class VodPlayActivity extends BaseActivity {

    @Override
    protected int getContentResId() {
        return R.layout.activity_vod_play;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    /**
     * 2s内点击退出
     */
    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) < 2000) {
                new AlertDialog.Builder(VodPlayActivity.this)
                        .setTitle(getString(R.string.exitdialog_hint))
                        .setMessage(getString(R.string.exitdialog_out_hint))
                        .setPositiveButton(getString(R.string.exitdialog_out), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                finish();
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
