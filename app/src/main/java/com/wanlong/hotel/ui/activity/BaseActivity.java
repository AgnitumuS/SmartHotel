package com.wanlong.hotel.ui.activity;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager.LayoutParams;

import com.orhanobut.logger.Logger;
import com.wanlong.hotel.mvp.BasePresenter;
import com.wanlong.hotel.mvp.BaseView;
import com.wanlong.hotel.utils.ActivityCollector;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;

/**
 * Created by lingchen on 2018/1/24. 13:34
 * mail:lingchen52@foxmail.com
 */
public abstract class BaseActivity<T extends BasePresenter<? extends BaseView>> extends AutoLayoutActivity implements BaseView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityCollector.addActivity(this);
        Logger.d(getClass().getSimpleName());
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);
        //设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(getContentResId());
        ButterKnife.bind(this);
        initView();
        initData();
    }

    protected abstract int getContentResId();

    protected abstract void initView();

    protected abstract void initData();

    //    作者：Ggx的代码之旅
    //    链接：https://www.jianshu.com/p/a14592dfc96a
    //    來源：简书
    //    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

    private T presenter;

    public T getPresenter() {
        return presenter;
    }

    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.recycle();
        }
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
