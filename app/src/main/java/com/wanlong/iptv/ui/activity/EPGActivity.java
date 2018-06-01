package com.wanlong.iptv.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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

    private int datePosition = 0;//右边时间列表日期位置

    //列表点击监听
    private void addListener() {
        //节目列表
        mEPGListAdapter.setOnItemClickListener(new EPGListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int lastPosition) {
                if (position != lastPosition) {
                    loadEPGlist(mLive.getPlaylist().get(position).getChannel_number());
                    RecyclerView.ViewHolder holder = mRecyclerLiveList.findViewHolderForAdapterPosition(lastPosition);
                    try {
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_live_number))
                                .setVisibility(View.VISIBLE);
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_live_number))
                                .setTextColor(getResources().getColor(R.color.color_8d9295));
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_live_list))
                                .setVisibility(View.VISIBLE);
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_live_list))
                                .setTextColor(getResources().getColor(R.color.color_8d9295));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //EPG列表
        mEPGDetailAdapter.setOnItemClickListener(new EPGDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int lastPosition) {
//                Intent intent = new Intent(EPGActivity.this, ReviewActivity.class);
//                intent.putExtra("url", "http://192.168.1.109:8081/live/T0PHrnKTJ3.m3u8");
//                intent.putExtra("name", "节目名");
//                startActivity(intent);
            }
        });
        //日期列表
        mEPGTimeAdapter.setOnItemClickListener(new EPGTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int lastPosition) {
                if (position != lastPosition) {
                    datePosition = position;
                    RecyclerView.ViewHolder holder = mRecyclerTimeList.findViewHolderForAdapterPosition(lastPosition);
                    try {
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_epg_time_week))
                                .setVisibility(View.VISIBLE);
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_epg_time_week))
                                .setTextColor(getResources().getColor(R.color.color_8d9295));
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_epg_time_date))
                                .setVisibility(View.VISIBLE);
                        ((TextView) holder.itemView.findViewById(R.id.tv_item_recycler_epg_time_date))
                                .setTextColor(getResources().getColor(R.color.color_8d9295));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    loadEPG();
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

    //获取EPG列表
    private void loadEPGlist(String channel_number) {
        getPresenter().loadEPGlist(this, Apis.HEADER + Apis.USER_EPG, channel_number);
    }

    private boolean hasEPG = false;//是否有EPG

    //获取EPG
    private void loadEPG() {
        String time = new SimpleDateFormat("yyyy/MM/dd")
                .format(new Date((App.newtime - datePosition * 24 * 3600) * 1000));
        if (mDetailBeans != null) {
            for (int i = 0; i < mDetailBeans.size(); i++) {
                if (time.equals(mDetailBeans.get(i).getDate())) {
                    getPresenter().loadEPGdetail(mDetailBeans.get(i).getUrl());
                    hasEPG = true;
                    return;
                }
                hasEPG = false;
            }
        }
        if (!hasEPG) {
            loadLocalEPG();
        }
    }

    private Live mLive;

    @Override
    public void loadListSuccess(Live liveListDatas) {
        if (liveListDatas != null) {
            mLive = liveListDatas;
            if (liveListDatas.getPlaylist() != null && liveListDatas.getPlaylist().size() > 0) {
                mEPGListAdapter.setData(liveListDatas.getPlaylist(), 0);
                loadEPGlist(mLive.getPlaylist().get(0).getChannel_number());
                mTvCurrentProgram.setText(mLive.getPlaylist().get(0).getService_name());
            }
        }
    }

    private List<EPGlist.DetailBean> mDetailBeans;

    @Override
    public void loadEPGlistSuccess(EPGlist epGlist) {
        mDetailBeans = epGlist.getDetail();
        loadEPG();
    }

    @Override
    public void loadEPGlistFailed(int error) {
        mDetailBeans = null;
        loadLocalEPG();
    }

    @Override
    public void loadEPGSuccess(EPG epg) {
        EPGUtils.sortEPG(epg.getDetail());
        mEPGDetailAdapter.setData(EPGUtils.mDetailBeans);
    }

    @Override
    public void loadEPGFailed(int error) {
        loadLocalEPG();
    }

    @Override
    public void loadFailed(int data) {

    }

    //加载apk内置EPG文件
    private void loadLocalEPG() {
        String epgString = EPGUtils.getJson(this, "epg.json");
        EPG epg = JSON.parseObject(epgString, EPG.class);
        mEPGDetailAdapter.setData(epg.getDetail());
    }
}
