package com.wanlong.iptv.ui.activity;

import android.os.Build;
import android.view.KeyEvent;

import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.wanlong.iptv.R;
import com.wanlong.iptv.player.LiveVideoPlayer;
import com.wanlong.iptv.player.SimpleVideoCallBack;

import butterknife.BindView;

/**
 * Created by lingchen on 2018/4/8.
 */

public class AdActivity extends BaseActivity {

    @BindView(R.id.ad_player)
    LiveVideoPlayer mAdPlayer;

    @Override
    protected int getContentResId() {
        return R.layout.activity_ad;
    }

    @Override
    protected void initView() {
        url = getIntent().getStringExtra("url");
        Logger.d("url:" + url);
        initPlayer();
    }

    private String url;

    @Override
    protected void initData() {

    }

    //初始化播放器
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
        mAdPlayer.setUp(url, false, "");
        mAdPlayer.startPlayLogic();
        mAdPlayer.setIsTouchWigetFull(true);
        mAdPlayer.setVideoAllCallBack(new SimpleVideoCallBack() {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdPlayer.release();
        mAdPlayer = null;
    }
}
