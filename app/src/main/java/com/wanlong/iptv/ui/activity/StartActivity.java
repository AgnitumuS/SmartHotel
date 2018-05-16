package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.VideoView;

import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.ijkplayer.services.Settings;
import com.wanlong.iptv.ijkplayer.widget.media.IjkVideoView;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.LanguageSwitchUtils;
import com.wanlong.iptv.utils.Utils;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by lingchen on 2018/1/24. 14:25
 * mail:lingchen52@foxmail.com
 */
public class StartActivity extends BaseActivity {
    @BindView(R.id.img_start)
    ImageView mImgStart;
    @BindView(R.id.videoview)
    VideoView mVideoview;
    @BindView(R.id.ijkVideoView)
    IjkVideoView mIjkVideoView;

    @Override
    protected int getContentResId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        GlideApp.with(this).load(R.drawable.hotel_logo).into(mImgStart);
//        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_image);
//        mImgStart.setAnimation(loadAnimation);
//        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {

//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
    }

    private static final int LOGIN = 0;
    private static final int OPEN = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean firstOpen;
    private String ip;

    @Override
    protected void initData() {
        Logger.d("mac:" + Utils.getMac(this));
        Logger.d("model:" + Build.MODEL);
        createSP();
        if (ApkVersion.CURRENT_VERSION == ApkVersion.STANDARD_VERSION) {
            initPlayer();
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            mHandler.sendEmptyMessageDelayed(OPEN, 1000);
        }
    }

    private void initPlayer() {
        Settings.setPlayer(Settings.PV_PLAYER__AndroidMediaPlayer);
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
                mHandler.sendEmptyMessageDelayed(OPEN, 0);
            }
        });
        mIjkVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                return false;
            }
        });
//        mVideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//            }
//        });
//        mVideoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mp.stop();
//                mHandler.sendEmptyMessageDelayed(OPEN, 0);
//            }
//        });
//        mVideoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                mp.stop();
//                return true;
//            }
//        });
        try {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.prevail);
            mIjkVideoView.setVideoURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoview.canPause()) {
            mVideoview.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoview.isPlaying()) {
            mVideoview.resume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoview.stopPlayback();
    }

    //创建SharedPreferences
    private void createSP() {
        sharedPreferences = ApkVersion.getSP(this);
        firstOpen = sharedPreferences.getBoolean("firstOpen", true);
        ip = sharedPreferences.getString("ip", "");
        if (ip.equals("")) {
            editor = sharedPreferences.edit();
            editor.putString("ip", Apis.HEADER);
            editor.commit();
            ip = sharedPreferences.getString("ip", "");
        }
        Apis.HEADER = ip;
//        mHandler.sendEmptyMessageDelayed(OPEN, 1000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN:
                    Intent intent1 = new Intent(StartActivity.this, LoginSettingActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                case OPEN:
                    if (ApkVersion.CURRENT_VERSION == ApkVersion.STANDARD_VERSION) {
                        if (firstOpen) {
                            Intent intent2 = new Intent(StartActivity.this, LanguageActivity.class);
                            startActivity(intent2);
                            finish();
                        } else {
                            LanguageSwitchUtils.languageSwitch(StartActivity.this,
                                    sharedPreferences.getInt("language", 0));
                            Intent intent2 = new Intent(StartActivity.this, HomeActivity.class);
                            startActivity(intent2);
                            finish();
                        }
                    }
                    if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
                        Intent intent2 = new Intent(StartActivity.this, HomeActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
