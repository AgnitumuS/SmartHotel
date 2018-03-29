package com.wanlong.iptv.ui.activity;

import android.support.v7.widget.RecyclerView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.ServicesData;
import com.wanlong.iptv.mvp.ServicesPresenter;
import com.wanlong.iptv.utils.Apis;

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

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setPresenter(new ServicesPresenter(this));
        getPresenter().loadServicesData(Apis.HEADER + Apis.SERVICE);
    }

    @Override
    public void loadDataSuccess(ServicesData servicesData) {

    }

    @Override
    public void loadFailed() {

    }

}
