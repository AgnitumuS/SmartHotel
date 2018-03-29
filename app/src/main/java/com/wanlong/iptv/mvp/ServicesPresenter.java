package com.wanlong.iptv.mvp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.ServicesListData;
import com.wanlong.iptv.entity.ServicesTypeData;

import java.util.List;

/**
 * Created by lingchen on 2018/2/5. 11:27
 * mail:lingchen52@foxmail.com
 */
public class ServicesPresenter extends BasePresenter<ServicesPresenter.ServicesView> {

    public ServicesPresenter(ServicesView servicesView) {
        super(servicesView);
    }

    public void loadServicesTypeData(String url) {
        Logger.d("ServicesPresenter:"+ url);
        OkGo.<String>get(url)
                .tag(this)
//                .params("device_id", App.sUUID.toString())
//                .params("device_type", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            ServicesTypeData servicesTypeData = JSON.parseObject(response.body(), ServicesTypeData.class);
                            getView().loadServicesTypeDataSuccess(servicesTypeData);
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

    public void loadServicesListData(String url) {
        Logger.d("ServicesPresenter:"+ url);
        OkGo.<String>get(url)
                .tag(this)
//                .params("device_id", App.sUUID.toString())
//                .params("device_type", "android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            List<ServicesListData> servicesTypeDatas = JSON.parseArray(response.body(), ServicesListData.class);
                            getView().loadServicesListDataSuccess(servicesTypeDatas);
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

    public interface ServicesView extends BaseView {
        void loadServicesTypeDataSuccess(ServicesTypeData servicesTypeData);
        void loadServicesListDataSuccess(List<ServicesListData> servicesTypeDatas);
        void loadFailed();
    }
}
