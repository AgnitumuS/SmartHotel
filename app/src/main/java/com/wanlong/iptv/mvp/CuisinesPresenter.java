package com.wanlong.iptv.mvp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.CuisinesListData;
import com.wanlong.iptv.entity.CuisinesTypeData;

import java.util.List;

/**
 * Created by lingchen on 2018/1/30. 15:47
 * mail:lingchen52@foxmail.com
 */
public class CuisinesPresenter extends BasePresenter<CuisinesPresenter.CuisinesView> {

    public CuisinesPresenter(CuisinesView cuisinesView) {
        super(cuisinesView);
    }

    public void loadCuisinesTypeData(String url) {
        Logger.d("CuisinesTypeData", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            CuisinesTypeData cuisinesTypeData = JSON.parseObject(response.body(), CuisinesTypeData.class);
                            getView().loadCuisinesTypeDataSuccess(cuisinesTypeData);
                        } catch (JSONException e) {
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

    public void loadCuisinesListData(String url) {
        Logger.d("CuisinesTypeData", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            List<CuisinesListData> cuisinesTypeDatas = JSON.parseArray(response.body(), CuisinesListData.class);
                            getView().loadCuisinesListDataSuccess(cuisinesTypeDatas);
                        } catch (JSONException e) {
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
        void loadCuisinesTypeDataSuccess(CuisinesTypeData cuisinesTypeData);
        void loadCuisinesListDataSuccess(List<CuisinesListData> cuisinesTypeDatas);
        void loadFailed();
    }
}
