package com.wanlong.iptv.ui.activity;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.LiveData;
import com.wanlong.iptv.mvp.LivePresenter;
import com.wanlong.iptv.player.LiveVideoPlayer;
import com.wanlong.iptv.player.SimpleVideoCallBack;
import com.wanlong.iptv.ui.adapter.LiveCategoryAdapter;
import com.wanlong.iptv.ui.adapter.LiveListAdapter;

import butterknife.BindView;

public class LiveActivity extends BaseActivity<LivePresenter> implements LivePresenter.LiveView {

    @BindView(R.id.live_video_player)
    LiveVideoPlayer mLiveVideoPlayer;
    @BindView(R.id.recycler_live_category)
    RecyclerView mRecyclerLiveCategory;
    @BindView(R.id.recycler_live_list)
    RecyclerView mRecyclerLiveList;

    @Override
    protected int getContentResId() {
        return R.layout.activity_live;
    }

    private LiveCategoryAdapter mLiveCategoryAdapter;
    private LiveListAdapter mLiveListAdapter;

    @Override
    protected void initView() {
        //直播分类列表
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerLiveCategory.setLayoutManager(linearLayoutManager1);
        mLiveCategoryAdapter = new LiveCategoryAdapter(this);
        mRecyclerLiveCategory.setAdapter(mLiveCategoryAdapter);
        //直播节目列表
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerLiveList.setLayoutManager(linearLayoutManager2);
        mLiveListAdapter = new LiveListAdapter(this);
        mRecyclerLiveList.setAdapter(mLiveListAdapter);
        initPlayer();
    }

    @Override
    protected void initData() {
        setPresenter(new LivePresenter(this));
//        getPresenter().loadLiveData("");
    }

    //初始化播放器
    private void initPlayer() {
        switch (Build.MODEL) {
            case "etv2021":
            case "jb_dmp":
            case "GX-1":
            case "S905W":
            case "p230":
                GSYVideoManager.instance().setVideoType(this, GSYVideoType.SYSTEMPLAYER);
                break;
            default:
                GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKPLAYER);
                break;
        }
        mLiveVideoPlayer.setUp("http://192.168.1.231/earth1.mp4", false, "");
        mLiveVideoPlayer.startPlayLogic();
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
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLiveVideoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLiveVideoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveVideoPlayer.release();
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

    @Override
    public void loadDataSuccess(LiveData liveData) {
        mLiveCategoryAdapter.setData(liveData);
        mLiveListAdapter.setData(liveData);
    }

    @Override
    public void loadFailed() {
        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }

}
