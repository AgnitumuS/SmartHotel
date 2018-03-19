package com.wanlong.iptv.ui.activity;

import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.VodDetailData;
import com.wanlong.iptv.mvp.VodDetailPresenter;

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
    public void loadVodDetailSuccess(VodDetailData vodDetailData) {

    }

    @Override
    public void loadVodDetailFailed() {
        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }
}
