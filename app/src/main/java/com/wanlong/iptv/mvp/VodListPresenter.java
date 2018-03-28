package com.wanlong.iptv.mvp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.VodListData;
import com.wanlong.iptv.entity.VodTypeData;

import java.util.List;

/**
 * Created by lingchen on 2018/1/30. 14:51
 * mail:lingchen52@foxmail.com
 */
public class VodListPresenter extends BasePresenter<VodListPresenter.VodListView>{

    public VodListPresenter(VodListView vodListView) {
        super(vodListView);
    }

    public void loadVodTypeData(String url){
        Logger.d("VodListPresenter:"+ url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body().toString());
                        try {
                            VodTypeData vodTypeData = JSON.parseObject(response.body(), VodTypeData.class);
                            getView().loadVodTypeSuccess(vodTypeData);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        super.onCacheSuccess(response);
//                        onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        getView().loadFailed(1);
                    }
                });
    }

    public void loadVodListData(String url){
        Logger.d("VodListPresenter", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            List<VodListData> vodListDatas = JSON.parseArray(response.body(), VodListData.class);
                            getView().loadVodListSuccess(vodListDatas);
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
                        getView().loadFailed(2);
                    }
                });
    }

    public interface VodListView extends BaseView{
        void loadVodTypeSuccess(VodTypeData vodTypeData);
        void loadVodListSuccess(List<VodListData> vodListDatas);
        void loadFailed(int data);
    }
}
