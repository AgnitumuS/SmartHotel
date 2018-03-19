package com.wanlong.iptv.ui.activity;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.SceneriesData;
import com.wanlong.iptv.mvp.SceneriesPresenter;

public class SceneriesActivity extends BaseActivity<SceneriesPresenter> implements SceneriesPresenter.SceneriesView {

    @Override
    protected int getContentResId() {
        return R.layout.activity_scnenries;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setPresenter(new SceneriesPresenter(this));
        getPresenter().loadSceneriesData("");
    }

    @Override
    public void loadDataSuccess(SceneriesData sceneriesData) {

    }

    @Override
    public void loadFailed() {

    }
}
