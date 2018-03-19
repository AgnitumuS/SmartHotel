package com.wanlong.iptv.ui.activity;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.CuisinesData;
import com.wanlong.iptv.mvp.CuisinesPresenter;

public class CuisinesActivity extends BaseActivity<CuisinesPresenter> implements CuisinesPresenter.CuisinesView{

    @Override
    protected int getContentResId() {
        return R.layout.activity_cuisines;
    }

    @Override
    protected void initView() {
        setPresenter(new CuisinesPresenter(this));
        getPresenter().loadCuisinesData("");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void loadCuisinesDataSuccess(CuisinesData cuisinesData) {

    }

    @Override
    public void loadFailed() {

    }
}
