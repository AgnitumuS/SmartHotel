package com.wanlong.iptv.ui.activity;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.ServicesData;
import com.wanlong.iptv.mvp.ServicesPresenter;

public class ServicesActivity extends BaseActivity<ServicesPresenter> implements ServicesPresenter.ServicesView {

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
        getPresenter().loadServicesData("");
    }

    @Override
    public void loadDataSuccess(ServicesData servicesData) {

    }

    @Override
    public void loadFailed() {

    }
}
