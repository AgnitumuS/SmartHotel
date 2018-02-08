package com.wanlong.iptv.mvp;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.CuisinesData;

/**
 * Created by lingchen on 2018/1/30. 15:47
 * mail:lingchen52@foxmail.com
 */
public class CuisinesPresenter extends BasePresenter<CuisinesPresenter.CuisinesView> {

    public CuisinesPresenter(CuisinesView cuisinesView) {
        super(cuisinesView);
    }

    public void loadCuisinesData(String url) {
        Logger.d("CuisinesData", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("HomePresenter", response.body());
                        try {
                            CuisinesData cuisinesData = JSON.parseObject(response.body(), CuisinesData.class);
                            getView().loadCuisinesDataSuccess(cuisinesData);
                        }catch (JSONException e){
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

    public interface CuisinesView extends BaseView {
        void loadCuisinesDataSuccess(CuisinesData cuisinesData);
        void loadFailed();
    }
}
