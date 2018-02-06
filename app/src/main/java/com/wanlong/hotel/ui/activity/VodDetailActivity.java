package com.wanlong.hotel.ui.activity;

import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.wanlong.hotel.R;
import com.wanlong.hotel.entity.VodDetailData;
import com.wanlong.hotel.mvp.VodDetailPresenter;

public class VodDetailActivity extends BaseActivity<VodDetailPresenter> implements VodDetailPresenter.VodDetailView{

    @Override
    protected int getContentResId() {
        return R.layout.activity_vod_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setPresenter(new VodDetailPresenter(this));
        getPresenter().loadVodDetailData("");
    }


    @Override
    public void showLoadVodDetailSuccess(VodDetailData vodDetailData) {

    }

    @Override
    public void showLoadVodDetailFailed() {
        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }
}
