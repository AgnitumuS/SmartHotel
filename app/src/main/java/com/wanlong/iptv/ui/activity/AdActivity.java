package com.wanlong.iptv.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.ijkplayer.widget.media.IjkVideoView;
import com.wanlong.iptv.player.LiveVideoPlayer;
import com.wanlong.iptv.player.SimpleVideoCallBack;
import com.wanlong.iptv.server.AdService;
import com.wanlong.iptv.ui.weigets.MarqueeTextView;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by lingchen on 2018/4/8.
 */

public class AdActivity extends BaseActivity {

    @BindView(R.id.ad_player)
    LiveVideoPlayer mAdPlayer;
    @BindView(R.id.tv_hint)
    MarqueeTextView mTvHint;
    @BindView(R.id.ijkVideoView)
    IjkVideoView mIjkVideoView;

    @Override
    protected int getContentResId() {
        return R.layout.activity_ad;
    }

    @Override
    protected void initView() {
//        mTvHint.setWidth(Utils.getDisplaySize(this).x);
        mTvHint.setText("正在播放强制插播节目");
        url = getIntent().getStringExtra("url");
        Logger.d("url:" + url);
        initPlayer();
    }

    private String url;

    @Override
    protected void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTvHint.setText("正在播放强制插播节目");
        url = getIntent().getStringExtra("url");
        Logger.d("url:" + url);
        initPlayer();
    }

    //初始化播放器
    private void initPlayer() {
        Toast.makeText(this, "进入插播", Toast.LENGTH_SHORT).show();
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
            case "0008":
                initijkVideoView();
//                GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKEXOPLAYER2);
//                GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
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

    private void initijkVideoView() {
        mAdPlayer.setVisibility(View.GONE);
        mIjkVideoView.setVisibility(View.VISIBLE);
        mIjkVideoView.setVideoURI(Uri.parse(url));
        mIjkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
            }
        });
        mIjkVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.stop();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (App.PRISON) {
                new AlertDialog.Builder(AdActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
                        .setTitle(getString(R.string.exitdialog_hint))
                        .setMessage(getString(R.string.exitdialog_out_hint))
                        .setPositiveButton(getString(R.string.exitdialog_out), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
//                              Toast toast = Toast.makeText(AdActivity.this,"10秒后将再次进入插播",10*1000);
//                              toast.show();
                                Toast.makeText(AdActivity.this, "稍后将再次进入插播", Toast.LENGTH_LONG).show();
                                mHandler.sendEmptyMessageDelayed(AUTO_INTO_ADACTIVITY, 5 * 1000);
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.exitdialog_back), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//响应事件

                            }
                        }).show();
                return true;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdPlayer.onVideoPause();
        mIjkVideoView.stopPlayback();
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

    public static final int AUTO_INTO_ADACTIVITY = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_INTO_ADACTIVITY:
                    AdService.videoResult = "";
                    break;
            }

        }
    };

}
