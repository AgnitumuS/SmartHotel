package com.wanlong.iptv.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.Live;
import com.wanlong.iptv.ijkplayer.services.Settings;
import com.wanlong.iptv.ijkplayer.widget.media.IjkVideoView;
import com.wanlong.iptv.mvp.LivePresenter;
import com.wanlong.iptv.player.LiveVideoPlayer;
import com.wanlong.iptv.player.SimpleVideoCallBack;
import com.wanlong.iptv.ui.adapter.LiveListAdapter;
import com.wanlong.iptv.ui.adapter.VodTypeAdapter;
import com.wanlong.iptv.utils.Apis;

import butterknife.BindView;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class LiveActivity extends BaseActivity<LivePresenter> implements LivePresenter.LiveView {

    @BindView(R.id.live_video_player)
    LiveVideoPlayer mLiveVideoPlayer;
    @BindView(R.id.recycler_live_list)
    RecyclerView mRecyclerLiveList;
    @BindView(R.id.channel_list)
    LinearLayout mChannelList;
    @BindView(R.id.channel_info)
    RelativeLayout mChannelInfo;
    @BindView(R.id.relativelayout_channel)
    RelativeLayout mRelativelayoutChannel;
    @BindView(R.id.tv_live_category)
    AppCompatTextView mTvLiveCategory;
    @BindView(R.id.img_left)
    ImageView mImgLeft;
    @BindView(R.id.img_right)
    ImageView mImgRight;
    @BindView(R.id.tv_live_name)
    AppCompatTextView mTvLiveName;
    @BindView(R.id.ijkVideoView)
    IjkVideoView mIjkVideoView;
//    @BindView(R.id.tv_num)
//    AppCompatTextView mTvNum;
//    @BindView(R.id.re_key_num)
//    RelativeLayout mReKeyNum;

    @Override
    protected int getContentResId() {
        return R.layout.activity_live;
    }

    private LiveListAdapter mLiveListAdapter;

    @Override
    protected void initView() {
        //直播节目列表
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerLiveList.setNestedScrollingEnabled(false);
        mRecyclerLiveList.setLayoutManager(linearLayoutManager2);
        mLiveListAdapter = new LiveListAdapter(this);
        mRecyclerLiveList.setAdapter(mLiveListAdapter);
        initPlayer();
        initListener();
    }

    private int currentPlayPosition;

    private void initListener() {
        mLiveListAdapter.setOnItemClickListener(new VodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                playNewUrl(mLive.getPlaylist().get(position).getUrl());
//                mLiveVideoPlayer.setUp(mLive.getPlaylist().get(position).getUrl(), false, "");
//                mLiveVideoPlayer.startPlayLogic();
                currentPlayPosition = position;
                mTvLiveName.setText(mLive.getPlaylist().get(position).getService_name());
                editor.putInt("liveLastPlayPosition", position);
                editor.commit();
            }
        });
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int liveLastPlayPosition;

    @Override
    protected void initData() {
        sharedPreferences = getSharedPreferences("PRISON-login", Context.MODE_PRIVATE);
        liveLastPlayPosition = sharedPreferences.getInt("liveLastPlayPosition", 0);
        editor = sharedPreferences.edit();
        setPresenter(new LivePresenter(this));
        mTvLiveCategory.setText("全部");
        mImgLeft.setVisibility(View.GONE);
        mImgRight.setVisibility(View.GONE);
        getPresenter().loadLiveListData(Apis.HEADER + Apis.USER_LIVE);
        resetTime();
    }

    private String[] urls = {"http://192.168.1.231/vod/file-list.m3u8",
            "http://192.168.1.231/earth1.mp4",
            "http://192.168.1.109:9080/stream/vod/行星地球二01.mp4"};

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
            case "0008":
                GSYVideoManager.instance().setVideoType(this, GSYVideoType.SYSTEMPLAYER);
                GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
                initIjkVideoView();
                break;
            default:
                GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKPLAYER);
                GSYVideoType.setRenderType(GSYVideoType.TEXTURE);
                break;
        }
        mLiveVideoPlayer.setIsTouchWigetFull(true);
        mLiveVideoPlayer.setVideoAllCallBack(new SimpleVideoCallBack() {
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

            @Override
            public void onClickBlank(String url, Object... objects) {
                showList();
                showInfo();
                super.onClickBlank(url, objects);
            }
        });
    }

    private void initIjkVideoView() {
        Settings.setPlayer(Settings.PV_PLAYER__AndroidMediaPlayer);
        mIjkVideoView.setVisibility(View.VISIBLE);
        mIjkVideoView.setFocusable(false);
        mLiveVideoPlayer.setVisibility(View.GONE);
//        mLiveVideoPlayer = null;
        mIjkVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
                showInfo();
            }
        });
        mIjkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                try {
                    iMediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//        mIjkVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
//                return false;
//            }
//        });
        mIjkVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.e("IJKMEDIA", "ERROR:" + i + "," + i1);
                iMediaPlayer.stop();
                iMediaPlayer.release();
                return true;
            }
        });
    }

    private void playNewUrl(String newurl) {
        if (newurl.startsWith("udp")) {
            mIjkVideoView.setVisibility(View.GONE);
            mIjkVideoView.stopPlayback();
            mLiveVideoPlayer.setVisibility(View.VISIBLE);
        } else {
            mIjkVideoView.setVisibility(View.VISIBLE);
            mLiveVideoPlayer.setVisibility(View.GONE);
            mLiveVideoPlayer.release();
        }
        if (mIjkVideoView.getVisibility() == View.VISIBLE) {
            try {
                mIjkVideoView.setVideoPath(newurl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (mLiveVideoPlayer != null && mLiveVideoPlayer.getVisibility() == View.VISIBLE) {
            mLiveVideoPlayer.setUp(newurl, false, "");
            mLiveVideoPlayer.startPlayLogic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLiveVideoPlayer != null && mLiveVideoPlayer.getVisibility() == View.VISIBLE) {
            mLiveVideoPlayer.onVideoPause();
        }
        if (mIjkVideoView.getVisibility() == View.VISIBLE) {
            mIjkVideoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLiveVideoPlayer != null && mLiveVideoPlayer.getVisibility() == View.VISIBLE) {
            mLiveVideoPlayer.onVideoResume();
        }
        if (mIjkVideoView.getVisibility() == View.VISIBLE) {
            mIjkVideoView.resume();
        }
        if (mLive != null && mLive.getPlaylist() != null && mLive.getPlaylist().size() > 0) {
            try {
                playNewUrl(mLive.getPlaylist().get(currentPlayPosition).getUrl());
//                mLiveVideoPlayer.setUp(mLive.getPlaylist().get(currentPlayPosition).getUrl(), false, "");
//                mLiveVideoPlayer.startPlayLogic();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        urls = null;
        mHandler.removeMessages(MOBILE_QWER);
        mHandler = null;
        if (mLiveVideoPlayer != null) {
            mLiveVideoPlayer.release();
            mLiveVideoPlayer = null;
        }
        mIjkVideoView.release(true);
        mIjkVideoView = null;
    }

    @OnClick({R.id.img_left, R.id.img_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                resetTime();
                break;
            case R.id.img_right:
                resetTime();
                break;
        }
    }

    private static final int LEFT = 1;
    private static final int RIGHT = 2;


    public static final int KEYCODE_UP = 0;
    public static final int KEYCODE_DOWN = 1;

    //上下键切换节目
    private void switchChannel(int oritation) {
        //2个及以上节目才可切换
        if (mLive != null && mLive.getPlaylist() != null && mLive.getPlaylist().size() > 1) {
            if (currentPlayPosition == 0) {
                if (oritation == KEYCODE_UP) {
                    currentPlayPosition += 1;
                } else if (oritation == KEYCODE_DOWN) {
                    currentPlayPosition = mLive.getPlaylist().size() - 1;
                }
            } else if (currentPlayPosition == mLive.getPlaylist().size() - 1) {
                if (oritation == KEYCODE_UP) {
                    currentPlayPosition = 0;
                } else if (oritation == KEYCODE_DOWN) {
                    currentPlayPosition -= 1;
                }
            } else {
                if (oritation == KEYCODE_UP) {
                    currentPlayPosition += 1;
                } else if (oritation == KEYCODE_DOWN) {
                    currentPlayPosition -= 1;
                }
            }
            if (currentPlayPosition >= 0 && currentPlayPosition < mLive.getPlaylist().size()) {
                playNewUrl(mLive.getPlaylist().get(currentPlayPosition).getUrl());
//            mLiveVideoPlayer.setUp(mLive.getPlaylist().get(currentPlayPosition).getUrl(), false, "");
//            mLiveVideoPlayer.startPlayLogic();
                mTvLiveName.setText(mLive.getPlaylist().get(currentPlayPosition).getService_name());
                showInfo();
                editor.putInt("liveLastPlayPosition", currentPlayPosition);
                editor.commit();
            }
        }
    }

    /**
     * 2s内点击退出
     */
    private long exitTime;
    private String number;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP://上一个节目
                if (mChannelList.getVisibility() == View.GONE) {
                    switchChannel(KEYCODE_UP);
                }
//                showList();
//                showInfo();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN://下一个节目
                if (mChannelList.getVisibility() == View.GONE) {
                    switchChannel(KEYCODE_DOWN);
                }
//                showList();
//                showInfo();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (mChannelList.getVisibility() == View.GONE) {
                    showList();
                } else {
//                    switchType(LEFT);
                }
                showInfo();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (mChannelList.getVisibility() == View.GONE) {
                    showList();
                } else {
//                    switchType(RIGHT);
                }
                showInfo();
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                showList();
                showInfo();
                break;
            case KeyEvent.KEYCODE_BACK:
                if ((System.currentTimeMillis() - exitTime) < 2000) {
//                    if (App.PRISON) {
//                        startActivity(new Intent(LiveActivity.this, HomeActivity.class));
//                    }
                    finish();
                } else {
                    Toast.makeText(this, R.string.click_again_to_exit_playback, Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                }
                return true;
            case KeyEvent.KEYCODE_0:
                inputNumber(0);
                break;
            case KeyEvent.KEYCODE_1:
                inputNumber(1);
                break;
            case KeyEvent.KEYCODE_2:
                inputNumber(2);
                break;
            case KeyEvent.KEYCODE_3:
                inputNumber(3);
                break;
            case KeyEvent.KEYCODE_4:
                inputNumber(4);
                break;
            case KeyEvent.KEYCODE_5:
                inputNumber(5);
                break;
            case KeyEvent.KEYCODE_6:
                inputNumber(6);
                break;
            case KeyEvent.KEYCODE_7:
                inputNumber(7);
                break;
            case KeyEvent.KEYCODE_8:
                inputNumber(8);
                break;
            case KeyEvent.KEYCODE_9:
                inputNumber(9);
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void inputNumber(int number) {

    }

    //显示左边节目列表
    private void showList() {
        if (mChannelList.getVisibility() == View.GONE) {
            mChannelList.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessageDelayed(MOBILE_QWER, 5000);
        } else {
            resetTime();
        }
    }

    //显示下方节目信息
    private void showInfo() {
        if (mChannelInfo.getVisibility() == View.GONE) {
            mChannelInfo.setVisibility(View.VISIBLE);
            mHandler.sendEmptyMessageDelayed(MOBILE_QWER, 5000);
        } else {
            resetTime();
        }
    }

    //重置UI消失时间
    public void resetTime() {
        if (mChannelList.getVisibility() == View.VISIBLE
                || mChannelInfo.getVisibility() == View.VISIBLE) {
            mHandler.removeMessages(MOBILE_QWER);
            mHandler.sendEmptyMessageDelayed(MOBILE_QWER, 5000);
        }
    }

    //定义变量
    private static final int STOPPLAY = 0;
    private static final int MOBILE_QWER = 1;

    //程序启动时，初始化并发送消息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPPLAY:
                    mLiveVideoPlayer.onVideoPause();
                    break;
                case MOBILE_QWER:
                    //当3秒到达后，作相应的操作。
                    if (mChannelList.getVisibility() == View.VISIBLE) {
                        mChannelList.setVisibility(View.GONE);
                    }
                    if (mChannelInfo.getVisibility() == View.VISIBLE) {
                        mChannelInfo.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    private Live mLive;
    private boolean exception;

    @Override
    public void loadListSuccess(Live liveListDatas) {
        if (liveListDatas != null) {
            mLive = liveListDatas;
            if (liveListDatas.getPlaylist() != null && liveListDatas.getPlaylist().size() > 0) {
                mLiveListAdapter.setData(liveListDatas.getPlaylist());
                try {
                    playNewUrl(liveListDatas.getPlaylist().get(liveLastPlayPosition).getUrl());
//                    mLiveVideoPlayer.setUp(liveListDatas.getPlaylist().get(liveLastPlayPosition).getUrl(), false, "");
//                    mLiveVideoPlayer.startPlayLogic();
                    currentPlayPosition = liveLastPlayPosition;
                    mTvLiveName.setText(mLive.getPlaylist().get(liveLastPlayPosition).getService_name());
                    exception = false;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    exception = true;
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    exception = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    exception = true;
                } finally {
                    if (exception) {
                        playNewUrl(liveListDatas.getPlaylist().get(0).getUrl());
//                        mLiveVideoPlayer.setUp(liveListDatas.getPlaylist().get(0).getUrl(), false, "");
//                        mLiveVideoPlayer.startPlayLogic();
                        currentPlayPosition = 0;
                        mTvLiveName.setText(mLive.getPlaylist().get(0).getService_name());
                    }
                }
            }
        }
    }

    @Override
    public void loadFailed(int data) {
//        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }
}
