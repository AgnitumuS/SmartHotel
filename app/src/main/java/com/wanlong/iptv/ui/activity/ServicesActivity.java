package com.wanlong.iptv.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.ServicesListData;
import com.wanlong.iptv.entity.ServicesTypeData;
import com.wanlong.iptv.mvp.ServicesPresenter;
import com.wanlong.iptv.ui.adapter.ServiceListAdapter;
import com.wanlong.iptv.ui.adapter.ServiceTypeAdapter;
import com.wanlong.iptv.ui.adapter.VodTypeAdapter;
import com.wanlong.iptv.utils.Apis;

import java.util.List;

import butterknife.BindView;

public class ServicesActivity extends BaseActivity<ServicesPresenter> implements ServicesPresenter.ServicesView {

    @BindView(R.id.recycler_service_type)
    RecyclerView mRecyclerServiceType;
    @BindView(R.id.recycler_service_list)
    RecyclerView mRecyclerServiceList;

    @Override
    protected int getContentResId() {
        return R.layout.activity_services;
    }

    private ServiceTypeAdapter mServiceTypeAdapter;
    private ServiceListAdapter mServiceListAdapter;

    @Override
    protected void initView() {
        //服务分类列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerServiceType.setLayoutManager(linearLayoutManager);
        mServiceTypeAdapter = new ServiceTypeAdapter(this);
        mRecyclerServiceType.setAdapter(mServiceTypeAdapter);
        //服务内容列表
        GridLayoutManager autoGridLayoutManager = new GridLayoutManager(this, 5);
        mRecyclerServiceList.setLayoutManager(autoGridLayoutManager);
        mServiceListAdapter = new ServiceListAdapter(this);
        mRecyclerServiceList.setAdapter(mServiceListAdapter);
        listener();
    }

    private void listener() {
        mServiceTypeAdapter.setOnItemClickListener(new VodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getPresenter().loadServicesListData(Apis.HEADER + Apis.SERVICE + "/" + mServicesTypeData.getServiceType().get(position));
            }
        });
        mServiceListAdapter.setOnItemClickListener(new VodTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    @Override
    protected void initData() {
        setPresenter(new ServicesPresenter(this));
        getPresenter().loadServicesTypeData(Apis.HEADER + Apis.SERVICE);
    }

    private ServicesTypeData mServicesTypeData;

    @Override
    public void loadServicesTypeDataSuccess(ServicesTypeData servicesTypeData) {
        mServicesTypeData = servicesTypeData;
        mServiceTypeAdapter.setData(servicesTypeData);
        getPresenter().loadServicesListData(Apis.HEADER + Apis.SERVICE + "/" + servicesTypeData.getServiceType().get(0));
    }

    @Override
    public void loadServicesListDataSuccess(List<ServicesListData> servicesTypeDatas) {

    }

    @Override
    public void loadFailed() {

    }

}
