package com.wanlong.iptv.mvp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.VodList;
import com.wanlong.iptv.entity.VodType;

/**
 * Created by lingchen on 2018/1/30. 14:51
 * mail:lingchen52@foxmail.com
 */
public class VodListPresenter extends BasePresenter<VodListPresenter.VodListView> {

    public VodListPresenter(VodListView vodListView) {
        super(vodListView);
    }

    public void loadVodTypeData(String url) {
        Logger.d("VodListPresenter:" + url);
        OkGo.<String>post(url)
                .tag(this)
                .params("mac", App.mac)
                .params("category", "?")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            VodType vodType = JSON.parseObject(response.body(), VodType.class);
                            getView().loadVodTypeSuccess(vodType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
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
                        getView().loadFailed(1);
                    }
                });
    }

    public void loadVodListData(String url, String type) {
        Logger.d("VodListPresenter", url);
        OkGo.<String>post(url)
                .tag(this)
                .params("mac", App.mac)
                .params("category", type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            VodList vodListDatas = JSON.parseObject(response.body(), VodList.class);
                            getView().loadVodListSuccess(vodListDatas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
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
                        getView().loadFailed(2);
                    }
                });
    }

    public interface VodListView extends BaseView {
        void loadVodTypeSuccess(VodType vodType);

        void loadVodListSuccess(VodList vodListDatas);

        void loadFailed(int data);
    }
}
