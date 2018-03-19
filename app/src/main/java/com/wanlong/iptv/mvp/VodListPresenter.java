package com.wanlong.iptv.mvp;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.VodListData;

/**
 * Created by lingchen on 2018/1/30. 14:51
 * mail:lingchen52@foxmail.com
 */
public class VodListPresenter extends BasePresenter<VodListPresenter.VodListView>{

    public VodListPresenter(VodListView vodListView) {
        super(vodListView);
    }

    public void loadVodListData(String url){
        Logger.d("VodListPresenter", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("HomePresenter", response.body());
                        try {
                            VodListData vodListData = JSON.parseObject(response.body(), VodListData.class);
                            getView().loadVodListSuccess(vodListData);
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
                        getView().loadVodListFailed();
                    }
                });
    }

    public interface VodListView extends BaseView{
        void loadVodListSuccess(VodListData vodListData);
        void loadVodListFailed();
    }
}
