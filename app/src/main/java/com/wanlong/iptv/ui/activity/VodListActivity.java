package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.VodList;
import com.wanlong.iptv.entity.VodType;
import com.wanlong.iptv.mvp.VodListPresenter;
import com.wanlong.iptv.ui.adapter.VodListAdapter;
import com.wanlong.iptv.ui.adapter.VodTypeAdapter;
import com.wanlong.iptv.utils.Apis;

import java.util.List;

import butterknife.BindView;

public class VodListActivity extends BaseActivity<VodListPresenter> implements VodListPresenter.VodListView {

    @BindView(R.id.recycler_vod_type)
    RecyclerView mRecyclerVodType;
    @BindView(R.id.recycler_vod_list)
    RecyclerView mRecyclerVodList;

    @Override
    protected int getContentResId() {
        return R.layout.activity_vod_list;
    }

    private VodTypeAdapter mVodTypeAdapter;
    private VodListAdapter mVodListAdapter;

    @Override
    protected void initView() {
        //点播分类列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerVodType.setLayoutManager(linearLayoutManager);
        mVodTypeAdapter = new VodTypeAdapter(this);
        mRecyclerVodType.setAdapter(mVodTypeAdapter);
        //点播节目列表
        GridLayoutManager autoGridLayoutManager = new GridLayoutManager(this, 5);
        mRecyclerVodList.setLayoutManager(autoGridLayoutManager);
        mVodListAdapter = new VodListAdapter(this);
        mRecyclerVodList.setAdapter(mVodListAdapter);
        listener();
    }

    private void listener() {
        mVodTypeAdapter.setOnItemClickListener(new VodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getPresenter().loadVodListData(Apis.HEADER + Apis.USER_VOD_TYPE, mVodType.getCategory().get(position));
            }
        });
        mVodListAdapter.setOnItemClickListener(new VodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(VodListActivity.this, VodDetailActivity.class);
                intent.putExtra("url_header", mPlaylistBeans.get(position).getVod_movie_player_src());
                String[] urls = null;
                if (mPlaylistBeans.get(position).getVod_movie() != null &&
                        mPlaylistBeans.get(position).getVod_movie().size() > 0) {
                    urls = new String[mPlaylistBeans.get(position).getVod_movie().size()];
                    for (int i = 0; i < urls.length; i++) {
                        urls[i] = mPlaylistBeans.get(position).getVod_movie().get(i);
                    }
                } else {
                    urls = new String[0];
                }
//                String[] urls = new String[mPlaylistBeans.get(position).getVod_movie().size()];
//                for (int i = 0; i < urls.length; i++) {
//                    urls[i] = mPlaylistBeans.get(position).getVod_movie().get(i);
//                }
                intent.putExtra("urls", urls);
                if (mPlaylistBeans.get(position).getPic_url() != null &&
                        mPlaylistBeans.get(position).getPic_url().size() > 0) {
                    intent.putExtra("vod_pic_url", mPlaylistBeans.get(position).getVod_pic_dir() +
                            mPlaylistBeans.get(position).getPic_url().get(0));
                } else {
                    intent.putExtra("vod_pic_url", "");
                }
//                intent.putExtra("vod_pic_url", mPlaylistBeans.get(position).getVod_pic_dir() +
//                        mPlaylistBeans.get(position).getPic_url().get(0));
                intent.putExtra("total_sets", mPlaylistBeans.get(position).getTotal_sets());
                intent.putExtra("current_sets", mPlaylistBeans.get(position).getCurrent_sets());
                intent.putExtra("vod_name", mPlaylistBeans.get(position).getVod_name());
                intent.putExtra("vod_release_time", mPlaylistBeans.get(position).getVod_release_time());
                intent.putExtra("vod_scores", mPlaylistBeans.get(position).getVod_scores());
                intent.putExtra("vod_category", mPlaylistBeans.get(position).getVod_category());
                intent.putExtra("vod_actor", mPlaylistBeans.get(position).getVod_actor());
                intent.putExtra("vod_detail", mPlaylistBeans.get(position).getVod_detail());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        setPresenter(new VodListPresenter(this));
        getPresenter().loadVodTypeData(Apis.HEADER + Apis.USER_VOD_TYPE);
    }

    private VodType mVodType;

    @Override
    public void loadVodTypeSuccess(VodType vodType) {
        if (vodType != null && vodType.getCategory() != null && vodType.getCategory().size() > 0) {
            mVodType = vodType;
            mVodTypeAdapter.setData(vodType.getCategory());
            getPresenter().loadVodListData(Apis.HEADER + Apis.USER_VOD_TYPE, vodType.getCategory().get(0));
        }
    }

    private List<VodList.PlaylistBean> mPlaylistBeans;

    @Override
    public void loadVodListSuccess(VodList vodListDatas) {
        if (vodListDatas.getPlaylist() != null && vodListDatas.getPlaylist().size() > 0) {
            mPlaylistBeans = vodListDatas.getPlaylist();
            mVodListAdapter.setData(vodListDatas.getPlaylist());
        }
    }

    @Override
    public void loadFailed(int data) {
//        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }
}
