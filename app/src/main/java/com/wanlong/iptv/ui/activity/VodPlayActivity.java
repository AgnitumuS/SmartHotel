package com.wanlong.iptv.ui.activity;

import android.os.Build;
import android.view.KeyEvent;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.player.SimpleVideoCallBack;
import com.wanlong.iptv.player.VodVideoPlayer;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class VodPlayActivity extends BaseActivity {

    @BindView(R.id.vod_player)
    VodVideoPlayer mVodPlayer;

    @Override
    protected int getContentResId() {
        return R.layout.activity_vod_play;
    }

    private String url;
    private String name;

    @Override
    protected void initView() {
        url = getIntent().getStringExtra("url").replaceAll(" ", "%20");
        name = getIntent().getStringExtra("name");
        initPlayer();
    }

    @Override
    protected void initData() {

    }

    private String starttime;
    private String playtime;
    private long start;//开始时间戳

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
            case "0008":
                GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKPLAYER);
                GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
                break;
            default:
                GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKPLAYER);
                GSYVideoType.setRenderType(GSYVideoType.TEXTURE);
                break;
        }
//        mVodPlayer.setUp("http://192.168.1.231/earth1.mp4", false, "");
        if (App.look_permission) {
            mVodPlayer.setUp(url, false, name);
        } else {
            Toast.makeText(this, "用户已过期,无法继续观看", Toast.LENGTH_SHORT).show();
        }
        mVodPlayer.setBackgroundColor(getResources().getColor(R.color.color_181818));
        mVodPlayer.startPlayLogic();
        mVodPlayer.setIsTouchWigetFull(true);
        mVodPlayer.setVideoAllCallBack(new SimpleVideoCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                start = App.newtime * 1000;
                starttime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(App.newtime * 1000));
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
        mVodPlayer.startPlayLogic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVodPlayer.release();
//        upload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void upload() {
        playtime = new SimpleDateFormat("HH:mm:ss").format(new Date(App.newtime * 1000 - start));
        OkGo.<String>post(Apis.HEADER + Apis.USER_VOD_PLAYBACK_INFO_UPLOAD)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params("mac", Utils.getMac(this))
                .params("program_name", name)
                .params("watch_start_time", starttime)
                .params("watch_continue_time", playtime)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.d("upload vod playback info success");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Logger.d("upload vod playback info failed");
                    }
                });
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
                upload();
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
