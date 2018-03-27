package com.wanlong.iptv.mvp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.HomeTypeData;

/**
 * Created by lingchen on 2018/2/5. 11:27
 * mail:lingchen52@foxmail.com
 */
public class HomePresenter extends BasePresenter<HomePresenter.HomeView> {

    public HomePresenter(HomeView homeView) {
        super(homeView);
    }

    public void loadLiveData(String url) {
        Logger.d("HomeView:"+ url);
        OkGo.<String>get(url)
                .tag(this)
//                .params("device_id", App.sUUID.toString())
//                .params("device_type", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            HomeTypeData homeTypeData = JSON.parseObject(response.body(), HomeTypeData.class);
                            getView().loadDataSuccess(homeTypeData);
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
        void loadDataSuccess(HomeTypeData homeTypeData);

        void loadFailed();
    }
}
