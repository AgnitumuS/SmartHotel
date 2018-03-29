package com.wanlong.iptv.ui.activity;

import android.os.Build;
import android.view.KeyEvent;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.wanlong.iptv.R;
import com.wanlong.iptv.player.SimpleVideoCallBack;
import com.wanlong.iptv.player.VodVideoPlayer;

import butterknife.BindView;

public class VodPlayActivity extends BaseActivity {

    @BindView(R.id.vod_player)
    VodVideoPlayer mVodPlayer;

    @Override
    protected int getContentResId() {
        return R.layout.activity_vod_play;
    }

    private String url;

    @Override
    protected void initView() {
        url = getIntent().getStringExtra("url");
        initPlayer();
    }

    @Override
    protected void initData() {

    }

    private void initPlayer() {
        switch (Build.MODEL) {
            case "etv2021":
            case "jb_dmp":
            case "GX-1":
            case "S905W":
            case "Prevail CATV":
            case "p230":
                GSYVideoManager.instance().setVideoType(this, GSYVideoType.SYSTEMPLAYER);
                GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
                break;
            default:
                GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKPLAYER);
                GSYVideoType.setRenderType(GSYVideoType.TEXTURE);
                break;
        }
//        mVodPlayer.setUp("http://192.168.1.231/earth1.mp4", false, "");
        mVodPlayer.setUp(url, false, "");
        mVodPlayer.startPlayLogic();
        mVodPlayer.setIsTouchWigetFull(true);
        mVodPlayer.setVideoAllCallBack(new SimpleVideoCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }

            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVodPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVodPlayer.onVideoResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVodPlayer.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 2s内点击退出
     */
    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) < 2000) {
//                new AlertDialog.Builder(LiveActivity.this)
//                        .setTitle(getString(R.string.exitdialog_hint))
//                        .setMessage(getString(R.string.exitdialog_out_hint))
//                        .setPositiveButton(getString(R.string.exitdialog_out), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                finish();
//                            }
//                        })
//                        .setNegativeButton(getString(R.string.exitdialog_back), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {//响应事件
//                            }
//                        }).show();
            } else {
                Toast.makeText(this, R.string.click_again_to_exit_playback, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
