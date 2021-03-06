package com.wanlong.iptv.mvp;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.entity.HomeAD;
import com.wanlong.iptv.utils.Utils;

/**
 * Created by lingchen on 2018/2/5. 11:27
 * mail:lingchen52@foxmail.com
 */
public class HomePresenter extends BasePresenter<HomePresenter.HomeView> {

    public HomePresenter(HomeView homeView) {
        super(homeView);
    }

    private String result_homeAD;
    private HomeAD homeAD;

    public void loadHomeADData(Context context, String url) {
        OkGo.<String>post(url)
                .tag(this)
                .cacheKey(url)
                .params("mac", Utils.getMac(context))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            if (result_homeAD == null) {
                                checkData(response);
                            } else {
                                if (!result_homeAD.equals(response.body())) {
                                    checkData(response);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
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
                        getView().loadFailed(0);
                    }
                });

    }

    private void checkData(Response<String> response) {
        homeAD = JSON.parseObject(response.body(), HomeAD.class);
        result_homeAD = response.body();
        if (homeAD != null) {
            if (homeAD.getCode().equals("0") || homeAD.getCode().equals("1")) {
                getView().loadHomeADSuccess(homeAD);
            } else {
                getView().loadFailed(0);
            }
        } else {
            getView().loadFailed(0);
        }
    }


    public interface HomeView extends BaseView {
        void loadHomeADSuccess(HomeAD homeAD);

        void loadFailed(int error);
    }
}
