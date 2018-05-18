package com.wanlong.iptv.mvp;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.EPG;
import com.wanlong.iptv.entity.EPGlist;
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

    //获取节目类型：直播、自办 获取所有节目
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
                        Logger.json(response.body());
                        try {
                            EPGlist epGlist = JSON.parseObject(response.body(), EPGlist.class);
                            if (epGlist.getCode() == 0) {
                                if (epGlist.getDetail() != null && epGlist.getDetail().size() > 0) {
                                    getView().loadEPGlistSuccess(epGlist);
                                } else {
                                    getView().loadEPGlistFailed(2);
                                }
                            } else {
                                getView().loadEPGlistFailed(1);
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
                        getView().loadEPGlistFailed(-1);
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
                        Logger.json(response.body());
                        try {
                            EPG epg = JSON.parseObject(response.body(), EPG.class);
                            if (epg.getCode() == 0) {
                                if (epg.getDetail() != null && epg.getDetail().size() > 0) {
                                    getView().loadEPGSuccess(epg);
                                } else {
                                    getView().loadEPGFailed(2);
                                }
                            } else {
                                getView().loadEPGFailed(1);
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
                        getView().loadEPGFailed(-1);
                    }
                });
    }

    public interface LiveView extends BaseView {
        void loadListSuccess(Live liveListDatas);

        void loadEPGlistSuccess(EPGlist epGlist);

        void loadEPGlistFailed(int error);

        void loadEPGSuccess(EPG epg);

        void loadEPGFailed(int error);

        void loadFailed(int data);

    }
}
