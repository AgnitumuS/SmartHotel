package com.wanlong.iptv.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.EPG;
import com.wanlong.iptv.entity.EPGlist;
import com.wanlong.iptv.entity.Live;
import com.wanlong.iptv.mvp.LivePresenter;

import butterknife.BindView;

/**
 * Created by lingchen on 2018/5/28. 10:45
 * mail:lingchen52@foxmail.com
 */
public class EPGActivity extends BaseActivity<LivePresenter> implements LivePresenter.LiveView {

    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.recycler_live_list)
    RecyclerView mRecyclerLiveList;
    @BindView(R.id.tv_current_program)
    TextView mTvCurrentProgram;
    @BindView(R.id.recycler_epg_list)
    RecyclerView mRecyclerEpgList;
    @BindView(R.id.recycler_time_list)
    RecyclerView mRecyclerTimeList;

    @Override
    protected int getContentResId() {
        return R.layout.activity_epg;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void loadListSuccess(Live liveListDatas) {

    }

    @Override
    public void loadEPGlistSuccess(EPGlist epGlist) {

    }

    @Override
    public void loadEPGlistFailed(int error) {

    }

    @Override
    public void loadEPGSuccess(EPG epg) {

    }

    @Override
    public void loadEPGFailed(int error) {

    }

    @Override
    public void loadFailed(int data) {

    }
}
