package com.wanlong.hotel.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.wanlong.hotel.R;
import com.wanlong.hotel.entity.VodListData;
import com.wanlong.hotel.mvp.VodListPresenter;
import com.wanlong.hotel.ui.adapter.VodCategoryAdapter;
import com.wanlong.hotel.ui.adapter.VodListAdapter;

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

    private VodCategoryAdapter mVodCategoryAdapter;
    private VodListAdapter mVodListAdapter;

    @Override
    protected void initView() {
        //点播分类列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerVodCategory.setLayoutManager(linearLayoutManager);
        mVodCategoryAdapter = new VodCategoryAdapter(this);
        mRecyclerVodCategory.setAdapter(mVodCategoryAdapter);
        //点播节目列表
        GridLayoutManager autoGridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerVodList.setLayoutManager(autoGridLayoutManager);
        mVodListAdapter = new VodListAdapter(this);
        mRecyclerVodList.setAdapter(mVodListAdapter);
    }

    @Override
    protected void initData() {
        setPresenter(new VodListPresenter(this));
//        getPresenter().loadVodListData("");

    }

    @Override
    public void showLoadVodListSuccess(VodListData vodListData) {
        mVodCategoryAdapter.setData(vodListData);
        mVodListAdapter.setData(vodListData);
    }

    @Override
    public void showLoadVodListFailed() {
        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }
}
