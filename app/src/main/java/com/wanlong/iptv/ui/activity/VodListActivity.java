package com.wanlong.iptv.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.VodListData;
import com.wanlong.iptv.entity.VodTypeData;
import com.wanlong.iptv.mvp.VodListPresenter;
import com.wanlong.iptv.ui.adapter.VodListAdapter;
import com.wanlong.iptv.ui.adapter.VodTypeAdapter;
import com.wanlong.iptv.utils.Apis;

import butterknife.BindView;

public class VodListActivity extends BaseActivity<VodListPresenter> implements VodListPresenter.VodListView {

    @BindView(R.id.recycler_vod_category)
    RecyclerView mRecyclerVodCategory;
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
        mRecyclerVodCategory.setLayoutManager(linearLayoutManager);
        mVodTypeAdapter = new VodTypeAdapter(this);
        mRecyclerVodCategory.setAdapter(mVodTypeAdapter);
        //点播节目列表
        GridLayoutManager autoGridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerVodList.setLayoutManager(autoGridLayoutManager);
        mVodListAdapter = new VodListAdapter(this);
        mRecyclerVodList.setAdapter(mVodListAdapter);
    }

    @Override
    protected void initData() {
        setPresenter(new VodListPresenter(this));
        getPresenter().loadVodTypeData(Apis.HEADER + Apis.VOD_TYPE);

    }

    @Override
    public void loadVodTypeSuccess(VodTypeData vodTypeData) {
        mVodTypeAdapter.setData(vodTypeData);
    }

    @Override
    public void loadVodListSuccess(VodListData vodListData) {
        mVodListAdapter.setData(vodListData);
    }

    @Override
    public void loadFailed(int data) {
//        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }
}
