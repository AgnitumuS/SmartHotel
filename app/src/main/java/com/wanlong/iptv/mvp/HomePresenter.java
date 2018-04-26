package com.wanlong.iptv.mvp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.HomeAD;

/**
 * Created by lingchen on 2018/2/5. 11:27
 * mail:lingchen52@foxmail.com
 */
public class HomePresenter extends BasePresenter<HomePresenter.HomeView> {

    public HomePresenter(HomeView homeView) {
        super(homeView);
    }

    public void loadHomeADData(String url) {
        Logger.d("HomeView:"+ url);
        OkGo.<String>post(url)
                .tag(this)
                .params("mac", App.mac)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            HomeAD homeAD = JSON.parseObject(response.body(), HomeAD.class);
                            getView().loadHomeADSuccess(homeAD);
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
                        getView().loadFailed(3);
                    }
                });
    }

    public interface HomeView extends BaseView {
        void loadHomeADSuccess(HomeAD homeAD);
        void loadFailed(int error);
    }
}
