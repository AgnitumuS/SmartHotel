package com.wanlong.iptv.mvp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.SceneriesData;

/**
 * Created by lingchen on 2018/2/5. 11:27
 * mail:lingchen52@foxmail.com
 */
public class SceneriesPresenter extends BasePresenter<SceneriesPresenter.SceneriesView> {

    public SceneriesPresenter(SceneriesView sceneriesView) {
        super(sceneriesView);
    }

    public void loadSceneriesData(String url) {
        Logger.d("SceneriesPresenter:"+ url);
        OkGo.<String>get(url)
                .tag(this)
                .params("device_id", App.sUUID.toString())
                .params("device_type", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body().toString());
                        try {
                            SceneriesData sceneriesData = JSON.parseObject(response.body(), SceneriesData.class);
                            getView().loadDataSuccess(sceneriesData);
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

    public interface SceneriesView extends BaseView {
        void loadDataSuccess(SceneriesData sceneriesData);

        void loadFailed();
    }
}
