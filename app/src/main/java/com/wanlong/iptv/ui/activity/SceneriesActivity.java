package com.wanlong.iptv.ui.activity;

import android.graphics.drawable.Drawable;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.SceneriesData;
import com.wanlong.iptv.imageloader.GlideImageLoader;
import com.wanlong.iptv.mvp.SceneriesPresenter;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SceneriesActivity extends BaseActivity<SceneriesPresenter> implements SceneriesPresenter.SceneriesView {

    @BindView(R.id.scnenries_banner)
    Banner mScnenriesBanner;

    @Override
    protected int getContentResId() {
        return R.layout.activity_scnenries;
    }

    private List<Drawable> images = new ArrayList<>();

    @Override
    protected void initView() {
        images.add(getResources().getDrawable(R.drawable.timg1));
        images.add(getResources().getDrawable(R.drawable.timg2));
        images.add(getResources().getDrawable(R.drawable.timg3));
        images.add(getResources().getDrawable(R.drawable.timg4));
        images.add(getResources().getDrawable(R.drawable.timg5));
        mScnenriesBanner.setImages(images)
                .setImageLoader(new GlideImageLoader())
                .start();
    }

    @Override
    protected void initData() {
        setPresenter(new SceneriesPresenter(this));
        getPresenter().loadSceneriesData("");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        mScnenriesBanner.stopAutoPlay();
    }

    @Override
    public void loadDataSuccess(SceneriesData sceneriesData) {

    }

    @Override
    public void loadFailed() {

    }
}
