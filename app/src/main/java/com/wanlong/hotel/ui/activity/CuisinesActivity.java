package com.wanlong.hotel.ui.activity;

import com.wanlong.hotel.R;
import com.wanlong.hotel.entity.CuisinesData;
import com.wanlong.hotel.mvp.CuisinesPresenter;

public class CuisinesActivity extends BaseActivity<CuisinesPresenter> implements CuisinesPresenter.CuisinesView{

    @Override
    protected int getContentResId() {
        return R.layout.activity_cuisines;
    }

    @Override
    protected void initView() {
        setPresenter(new CuisinesPresenter(this));
//        getPresenter().loadCuisinesData("");
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
