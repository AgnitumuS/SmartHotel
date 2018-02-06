package com.wanlong.hotel.mvp;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.hotel.entity.LiveData;

/**
 * Created by lingchen on 2018/1/30. 14:51
 * mail:lingchen52@foxmail.com
 */
public class LivePresenter extends BasePresenter<LivePresenter.LiveView>{

    public LivePresenter(LiveView liveView) {
        super(liveView);
    }

    public void loadLiveData(String url){
        Logger.d("LiveData", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("HomePresenter", response.body());
                        try {
                            LiveData liveData = JSON.parseObject(response.body(), LiveData.class);
                            getView().loadDataSuccess(liveData);
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

    public interface LiveView extends BaseView{
        void loadDataSuccess(LiveData liveData);
        void loadFailed();

    }
}
