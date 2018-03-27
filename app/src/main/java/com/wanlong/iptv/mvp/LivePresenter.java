package com.wanlong.iptv.mvp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.LiveListData;
import com.wanlong.iptv.entity.LiveTypeData;

import java.util.List;

/**
 * Created by lingchen on 2018/1/30. 14:51
 * mail:lingchen52@foxmail.com
 */
public class LivePresenter extends BasePresenter<LivePresenter.LiveView>{

    public LivePresenter(LiveView liveView) {
        super(liveView);
    }

    public void loadLiveTypeData(String url){
        Logger.d("LivePresenter", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.d("LivePresenter:"+response.body());
                        try {
                            LiveTypeData liveTypeData = JSON.parseObject(response.body(), LiveTypeData.class);
                            getView().loadTypeSuccess(liveTypeData);
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
                        getView().loadFailed(1);
                    }
                });
    }

    public void loadLiveListData(String url){
        Logger.d("LivePresenter", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.d("LivePresenter:"+response.body());
                        try {
                            List<LiveListData> liveListDatas = JSON.parseArray(response.body(), LiveListData.class);
                            getView().loadListSuccess(liveListDatas);
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

    public interface LiveView extends BaseView{
        void loadTypeSuccess(LiveTypeData liveTypeData);
        void loadListSuccess(List<LiveListData> liveListDatas);
        void loadFailed(int data);

    }
}
