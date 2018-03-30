package com.wanlong.iptv.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.CuisinesListData;
import com.wanlong.iptv.entity.CuisinesTypeData;
import com.wanlong.iptv.mvp.CuisinesPresenter;
import com.wanlong.iptv.ui.adapter.CuisinesListAdapter;
import com.wanlong.iptv.ui.adapter.CuisinesTypeAdapter;
import com.wanlong.iptv.ui.adapter.VodTypeAdapter;
import com.wanlong.iptv.utils.Apis;

import java.util.List;

import butterknife.BindView;

public class CuisinesActivity extends BaseActivity<CuisinesPresenter> implements CuisinesPresenter.CuisinesView {

    @BindView(R.id.recycler_cuisines_type)
    RecyclerView mRecyclerCuisinesType;
    @BindView(R.id.recycler_cuisines_list)
    RecyclerView mRecyclerCuisinesList;

    @Override
    protected int getContentResId() {
        return R.layout.activity_cuisines;
    }

    private CuisinesTypeAdapter mCuisinesTypeAdapter;
    private CuisinesListAdapter mCuisinesListAdapter;

    @Override
    protected void initView() {
        //美食分类列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerCuisinesType.setLayoutManager(linearLayoutManager);
        mCuisinesTypeAdapter = new CuisinesTypeAdapter(this);
        mRecyclerCuisinesType.setAdapter(mCuisinesTypeAdapter);
        //美食内容列表
        GridLayoutManager autoGridLayoutManager = new GridLayoutManager(this, 5);
        mRecyclerCuisinesList.setLayoutManager(autoGridLayoutManager);
        mCuisinesListAdapter = new CuisinesListAdapter(this);
        mRecyclerCuisinesList.setAdapter(mCuisinesListAdapter);
        listener();
    }

    private void listener() {
        mCuisinesTypeAdapter.setOnItemClickListener(new VodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mCuisinesTypeData.getCuisineType() != null && mCuisinesTypeData.getCuisineType().size() != 0) {
                    getPresenter().loadCuisinesListData(
                            Apis.HEADER + Apis.CUISINES + "/" + mCuisinesTypeData.getCuisineType().get(position));
                }
            }
        });
        mCuisinesListAdapter.setOnItemClickListener(new VodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    @Override
    protected void initData() {
        setPresenter(new CuisinesPresenter(this));
//        getPresenter().loadCuisinesTypeData(Apis.HEADER + Apis.CUISINES);
    }

    private CuisinesTypeData mCuisinesTypeData;

    @Override
    public void loadCuisinesTypeDataSuccess(CuisinesTypeData cuisinesTypeData) {
        mCuisinesTypeData = cuisinesTypeData;
        mCuisinesTypeAdapter.setData(cuisinesTypeData);
        if (cuisinesTypeData.getCuisineType() != null && cuisinesTypeData.getCuisineType().size() != 0) {
            getPresenter().loadCuisinesListData(
                    Apis.HEADER + Apis.CUISINES + "/" + cuisinesTypeData.getCuisineType().get(0));
        }
    }

    @Override
    public void loadCuisinesListDataSuccess(List<CuisinesListData> cuisinesListDatas) {
        mCuisinesListAdapter.setData(cuisinesListDatas);
    }

    @Override
    public void loadFailed() {

    }

}
