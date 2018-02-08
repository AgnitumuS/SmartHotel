package com.wanlong.iptv.mvp;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.HomeData;

/**
 * Created by lingchen on 2018/2/5. 11:27
 * mail:lingchen52@foxmail.com
 */
public class HomePresenter extends BasePresenter<HomePresenter.HomeView> {

    public HomePresenter(HomeView homeView) {
        super(homeView);
    }

    public void loadLiveData(String url) {
        Logger.d("HomeView", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("HomePresenter", response.body());
                        try {
                            HomeData homeData = JSON.parseObject(response.body(), HomeData.class);
                            getView().loadDataSuccess(homeData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        super.onCacheSuccess(response);
                        onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        getView().loadFailed();
                    }
                });
    }

    public interface HomeView extends BaseView {
        void loadDataSuccess(HomeData homeData);

        void loadFailed();
    }
}
