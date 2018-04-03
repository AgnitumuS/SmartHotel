package com.wanlong.iptv.mvp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.Live;
import com.wanlong.iptv.entity.LiveTypeData;

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
        Logger.d("LivePresenter:"+url);
        OkGo.<String>post(url)
                .tag(this)
                .params("mac","00:11:22:33:44:55")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            Live liveListDatas = JSON.parseObject(response.body(), Live.class);
                            if(liveListDatas.getCode().equals("0")){
                                getView().loadListSuccess(liveListDatas);
                            }else {
                                getView().loadFailed(1);
                            }

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
        void loadListSuccess(Live liveListDatas);
        void loadFailed(int data);

    }
}
