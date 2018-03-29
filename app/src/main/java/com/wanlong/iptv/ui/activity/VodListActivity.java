package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.VodListData;
import com.wanlong.iptv.entity.VodTypeData;
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

    private void listener(){
        mVodTypeAdapter.setOnItemClickListener(new VodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getPresenter().loadVodListData(Apis.HEADER + Apis.VOD_TYPE + "/" + mVodTypeData.getGenre().get(position));
            }
        });
        mVodListAdapter.setOnItemClickListener(new VodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(VodListActivity.this,VodPlayActivity.class);
                intent.putExtra("url","http://192.168.1.231/earth1.mp4");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        setPresenter(new VodListPresenter(this));
        getPresenter().loadVodTypeData(Apis.HEADER + Apis.VOD_TYPE);

    }

    private VodTypeData mVodTypeData;

    @Override
    public void loadVodTypeSuccess(VodTypeData vodTypeData) {
        mVodTypeData = vodTypeData;
        mVodTypeAdapter.setData(vodTypeData);
        getPresenter().loadVodListData(Apis.HEADER + Apis.VOD_TYPE + "/" + vodTypeData.getGenre().get(0));
    }

    @Override
    public void loadVodListSuccess(List<VodListData> vodListDatas) {
        mVodListAdapter.setData(vodListDatas);
    }

    @Override
    public void loadFailed(int data) {
//        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }
}
