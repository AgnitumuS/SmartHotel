package com.wanlong.iptv.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.EPG;
import com.wanlong.iptv.entity.EPGlist;
import com.wanlong.iptv.entity.Live;
import com.wanlong.iptv.mvp.LivePresenter;
import com.wanlong.iptv.ui.adapter.EPGDetailAdapter;
import com.wanlong.iptv.ui.adapter.EPGListAdapter;
import com.wanlong.iptv.ui.adapter.EPGTimeAdapter;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.EPGUtils;
import com.wanlong.iptv.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    private EPGListAdapter mEPGListAdapter;
    private EPGDetailAdapter mEPGDetailAdapter;
    private EPGTimeAdapter mEPGTimeAdapter;

    @Override
    protected void initView() {
        //节目列表
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerLiveList.setLayoutManager(linearLayoutManager1);
        mEPGListAdapter = new EPGListAdapter(this);
        mRecyclerLiveList.setAdapter(mEPGListAdapter);
        //EPG列表
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerEpgList.setLayoutManager(linearLayoutManager2);
        mEPGDetailAdapter = new EPGDetailAdapter(this);
        mRecyclerEpgList.setAdapter(mEPGDetailAdapter);
        //时间列表
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerTimeList.setLayoutManager(linearLayoutManager3);
        mEPGTimeAdapter = new EPGTimeAdapter(this);
        mRecyclerTimeList.setAdapter(mEPGTimeAdapter);
        addListener();
    }

    private void addListener() {
        mEPGListAdapter.setOnItemClickListener(new EPGListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int lastPosition) {
                if (position != lastPosition) {
                    RecyclerView.ViewHolder holder = mRecyclerLiveList.findViewHolderForAdapterPosition(lastPosition);
                    try {
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_live_number)).setVisibility(View.VISIBLE);
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_live_number))
                                .setTextColor(getResources().getColor(R.color.color_8d9295));
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_live_list)).setVisibility(View.VISIBLE);
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_live_list))
                                .setTextColor(getResources().getColor(R.color.color_8d9295));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    loadEPG(mLive.getPlaylist().get(position).getChannel_number());
                }
            }
        });
        mEPGDetailAdapter.setOnItemClickListener(new EPGDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int lastPosition) {

            }
        });
        mEPGTimeAdapter.setOnItemClickListener(new EPGTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int lastPosition) {
                if (position != lastPosition) {
                    RecyclerView.ViewHolder holder = mRecyclerTimeList.findViewHolderForAdapterPosition(lastPosition);
                    try {
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_epg_time_week)).setVisibility(View.VISIBLE);
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_epg_time_week))
                                .setTextColor(getResources().getColor(R.color.color_8d9295));
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_epg_time_date)).setVisibility(View.VISIBLE);
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_epg_time_date))
                                .setTextColor(getResources().getColor(R.color.color_8d9295));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    String time = new SimpleDateFormat("yyyy/MM/dd").format(new Date((App.newtime - position * 24 * 3600) * 1000));
                    for (int i = 0; i < detailBeans.size(); i++) {
                        if (time.equals(detailBeans.get(i).getDate())) {
                            getPresenter().loadEPGdetail(detailBeans.get(i).getUrl());
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        setPresenter(new LivePresenter(this));
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            getPresenter().loadLiveListData(this, Apis.HEADER + Apis.USER_LIVE, "直播");
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.STANDARD_VERSION) {
            getPresenter().loadLiveTypeData(this, Apis.HEADER + Apis.USER_LIVE);
        }
        mEPGTimeAdapter.setDate(TimeUtils.getDay(App.newtime * 1000) - 1);
    }

    private Live mLive;

    @Override
    public void loadListSuccess(Live liveListDatas) {
        if (liveListDatas != null) {
            mLive = liveListDatas;
            if (liveListDatas.getPlaylist() != null && liveListDatas.getPlaylist().size() > 0) {
                mEPGListAdapter.setData(liveListDatas.getPlaylist(), 0);
                loadEPG(mLive.getPlaylist().get(0).getChannel_number());
                mTvCurrentProgram.setText(mLive.getPlaylist().get(0).getService_name());
            }
        }
    }

    //获取EPG
    private void loadEPG(String channel_number) {
        getPresenter().loadEPGlist(this, Apis.HEADER + Apis.USER_EPG, channel_number);
    }

    List<EPGlist.DetailBean> detailBeans;

    @Override
    public void loadEPGlistSuccess(EPGlist epGlist) {
        String time = new SimpleDateFormat("yyyy/MM/dd").format(new Date(App.newtime * 1000));
        detailBeans = epGlist.getDetail();
        for (int i = 0; i < detailBeans.size(); i++) {
            if (time.equals(detailBeans.get(i).getDate())) {
                getPresenter().loadEPGdetail(detailBeans.get(i).getUrl());
                return;
            }
        }
    }

    @Override
    public void loadEPGlistFailed(int error) {

    }

    @Override
    public void loadEPGSuccess(EPG epg) {
        EPGUtils.parseEPG(epg.getDetail());
        mEPGDetailAdapter.setData(epg.getDetail());
    }

    @Override
    public void loadEPGFailed(int error) {

    }

    @Override
    public void loadFailed(int data) {

    }
}
