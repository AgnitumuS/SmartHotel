package com.wanlong.iptv.mvp;

import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

/**
 * Created by lingchen on 2018/1/30. 14:51
 * mail:lingchen52@foxmail.com
 */
public class VodDetailPresenter extends BasePresenter<VodDetailPresenter.VodDetailView>{

    public VodDetailPresenter(VodDetailView vodDetailView) {
        super(vodDetailView);
    }

    public void loadVodDetailData(String url){
        Logger.d("VodDetailData", url);
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("HomePresenter", response.body());
                        try {

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
                        getView().loadVodDetailFailed();
                    }
                });
    }

    public interface VodDetailView extends BaseView{
        void loadVodDetailFailed();
    }
}
