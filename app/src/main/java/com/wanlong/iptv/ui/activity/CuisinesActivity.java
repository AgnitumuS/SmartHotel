package com.wanlong.iptv.ui.activity;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.CuisinesListData;
import com.wanlong.iptv.entity.CuisinesTypeData;
import com.wanlong.iptv.mvp.CuisinesPresenter;
import com.wanlong.iptv.utils.Apis;

public class CuisinesActivity extends BaseActivity<CuisinesPresenter> implements CuisinesPresenter.CuisinesView {

    @Override
    protected int getContentResId() {
        return R.layout.activity_cuisines;
    }

    @Override
    protected void initView() {
        setPresenter(new CuisinesPresenter(this));
        getPresenter().loadCuisinesTypeData(Apis.HEADER + Apis.CUISINES);
    }

    @Override
    protected void initData() {

    }

    private CuisinesTypeData mCuisinesTypeData;

    @Override
    public void loadCuisinesTypeDataSuccess(CuisinesTypeData cuisinesTypeData) {
        mCuisinesTypeData = cuisinesTypeData;
        if (cuisinesTypeData.getCuisineType() != null && cuisinesTypeData.getCuisineType().size() != 0) {
            getPresenter().loadCuisinesListData(Apis.HEADER + Apis.CUISINES + "/" + cuisinesTypeData.getCuisineType().get(0));
        }

    }

    @Override
    public void loadCuisinesListDataSuccess(CuisinesListData cuisinesListData) {

    }

    @Override
    public void loadFailed() {

    }
}
