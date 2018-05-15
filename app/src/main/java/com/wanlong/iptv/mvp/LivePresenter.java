package com.wanlong.iptv.mvp;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.Live;
import com.wanlong.iptv.utils.Utils;

/**
 * Created by lingchen on 2018/1/30. 14:51
 * mail:lingchen52@foxmail.com
 */
public class LivePresenter extends BasePresenter<LivePresenter.LiveView> {

    public LivePresenter(LiveView liveView) {
        super(liveView);
    }

    //获取节目类型：直播、自办
    public void loadLiveTypeData(Context context, String url) {
        Logger.d("LivePresenter:" + url);
        OkGo.<String>post(url)
                .tag(this)
                .cacheKey(url + "?")
                .params("mac", Utils.getMac(context))
                .params("category", "?")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            Live liveListDatas = JSON.parseObject(response.body(), Live.class);
                            if (liveListDatas.getCode().equals("0")) {
                                getView().loadListSuccess(liveListDatas);
                            } else {
                                getView().loadFailed(1);
                            }

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

    //获取节目列表 type:直播、自办
    public void loadLiveListData(Context context, String url, String type) {
        Logger.d("LivePresenter:" + url);
        OkGo.<String>post(url)
                .tag(this)
                .cacheKey(url + type)
                .params("mac", Utils.getMac(context))
                .params("category", type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            Live liveListDatas = JSON.parseObject(response.body(), Live.class);
                            if (liveListDatas.getCode().equals("0")) {
                                getView().loadListSuccess(liveListDatas);
                            } else {
                                getView().loadFailed(1);
                            }

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

    //获取EPG文件列表
    public void loadEPGlist(Context context, String url, String channel_number) {
        OkGo.<String>post(url)
                .tag(this)
                .cacheKey(url + channel_number)
                .params("program", channel_number)
                .params("mac", Utils.getMac(context))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        super.onCacheSuccess(response);
                        onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    //获取EPG文件
    public void loadEPGdetail(String url) {
        OkGo.<String>get(url)
                .tag(this)
                .cacheKey(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        super.onCacheSuccess(response);
                        onSuccess(response);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    public interface LiveView extends BaseView {
        void loadListSuccess(Live liveListDatas);

        void loadFailed(int data);

    }
}
