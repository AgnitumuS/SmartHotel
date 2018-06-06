package com.wanlong.iptv.server;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.Login;
import com.wanlong.iptv.entity.PushMSG;
import com.wanlong.iptv.entity.UserStatus;
import com.wanlong.iptv.ui.activity.AdActivity;
import com.wanlong.iptv.utils.ActivityCollector;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.Utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by hasee on 2017/4/12.
 */

public class AdService extends Service {

    public static final int INTERVAL_TIME = 5;//间隔时间
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = ApkVersion.getSP(this);
        editor = sharedPreferences.edit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mTimer != null && mTimerTask != null) {
            try {
                mTimer.schedule(mTimerTask, 0, INTERVAL_TIME * 1000);
                mTimer.schedule(mLoginTask, 0, INTERVAL_TIME * 4 * 1000);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //时钟
    private Timer mTimer = new Timer(true);

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                getTextAd();
                getVideoAd();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private TimerTask mLoginTask = new TimerTask() {
        @Override
        public void run() {
            try {
                autoUploadLoginStatus();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mLoginTask != null) {
            mLoginTask.cancel();
            mLoginTask = null;
        }
        App.ADserver = false;
    }

    private UserStatus mUserStatus;

    //自动上传登录状态
    private void autoUploadLoginStatus() {
        OkGo.<String>post(Apis.HEADER + Apis.USER_LOGIN_STATUS)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params("mac", Utils.getMac(this))
                .params("uuid", App.sUUID.toString())
                .params("ip", Utils.getIpAddressString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            mUserStatus = JSON.parseObject(response.body(), UserStatus.class);
                            if (mUserStatus != null && mUserStatus.getCode() != null) {
                                //存储
                                editor.putString("expired_time", mUserStatus.getExpired_time());
                                editor.putString("vod_expired_time", mUserStatus.getVod_expired_time());
                                editor.commit();
                                if (mUserStatus.getCode().equals("0")) {
                                    uploadFailed();
//                                    Toast.makeText(AdServiceold.this, "用户未登录/即将过期", Toast.LENGTH_SHORT).show();
                                } else if (mUserStatus.getCode().equals("1")) {
                                    uploadSuccess();
                                    App.look_permission = true;
                                    autoLogin();
//                                    Toast.makeText(AdServiceold.this, "成功", Toast.LENGTH_SHORT).show();
                                } else if (mUserStatus.getCode().equals("-1")) {
                                    uploadFailed();
//                                    Toast.makeText(AdServiceold.this, "用户名或者密码输入不符合规则", Toast.LENGTH_SHORT).show();
                                } else if (mUserStatus.getCode().equals("-2")) {
                                    uploadFailed();
//                                    Toast.makeText(AdServiceold.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                } else if (mUserStatus.getCode().equals("-3")) {
                                    uploadFailed();
//                                    Toast.makeText(AdServiceold.this, "达到最大连接数", Toast.LENGTH_SHORT).show();
                                } else if (mUserStatus.getCode().equals("-4")) {
                                    App.look_permission = false;
                                    uploadFailed();
                                    autoLogin();
                                    Toast.makeText(AdService.this, getString(R.string.user_is_not_registered), Toast.LENGTH_LONG).show();
                                } else if (mUserStatus.getCode().equals("-5")) {
                                    uploadFailed();
//                                    Toast.makeText(AdServiceold.this, "服务器有错误", Toast.LENGTH_SHORT).show();
                                } else if (mUserStatus.getCode().equals("-6")) {
                                    uploadFailed();
//                                    Toast.makeText(AdServiceold.this, "用户名或者密码输入为空", Toast.LENGTH_SHORT).show();
                                } else if (mUserStatus.getCode().equals("-7")) {
                                    uploadFailed();
//                                    Toast.makeText(AdServiceold.this, "服务器返回数据异常", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Logger.d("autoUploadLoginStatus:" + "服务器返回数据异常");
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
                    public void onError(Response<String> response) {
                        super.onError(response);
                        uploadFailed();
                        autoLogin();
                    }
                });
    }

    private Login data;

    //自动登录
    private void autoLogin() {
        OkGo.<String>post(Apis.HEADER + Apis.USER_LOGIN)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .retryCount(5)
                .params("mac", Utils.getMac(this))
                .params("uuid", App.sUUID.toString())
                .params("ip", Utils.getIpAddressString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            data = JSON.parseObject(response.body(), Login.class);
                            if (data != null && data.getCode() != null) {
                                if (data.getCode().equals("0")) {
//                                    Toast.makeText(LoginSettingActivity.this, "用户未登录/即将过期", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("1")) {
                                    //存储
                                    loginSuccess();
                                    App.look_permission = true;
//                                    Toast.makeText(LoginSettingActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-1")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "用户名或者密码输入不符合规则", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-2")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-3")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "达到最大连接数", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-4")) {
                                    loginFailed();
                                    App.look_permission = false;
//                                    Toast.makeText(LoginSettingActivity.this, "用户已过期", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-5")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "服务器有错误", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-6")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "用户名或者密码输入为空", Toast.LENGTH_SHORT).show();
                                } else if (data.getCode().equals("-7")) {
                                    loginFailed();
//                                    Toast.makeText(LoginSettingActivity.this, "登陆过期", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                loginFailed();
                                Logger.d("autoLogin:" + "服务器返回数据异常");
//                                Toast.makeText(LoginSettingActivity.this, "服务器返回数据异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginFailed();
//                            Toast.makeText(LoginSettingActivity.this, "服务器返回数据异常", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        loginFailed();
                    }
                });
    }

    private void loginSuccess() {
        Logger.d("登录成功");
        try {
            editor.putString("group", data.getGroup());
            editor.putString("stb_name", data.getStb_name());
            editor.putString("area", data.getArea());
            editor.putString("Owner_Group", data.getOwner_Group());
            editor.putString("Owner_Group_display", data.getOwner_Group_display());
            editor.putString("playback_url", data.getPlayback_url());
            editor.commit();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginFailed() {
        Logger.d("登录失败");
    }

    private void uploadSuccess() {
        Logger.d("上传成功");
    }

    private void uploadFailed() {
        Logger.d("上传失败");
    }

    public static final int AD_TYPE_TEXT = 0;
    public static final int AD_TYPE_VIDEO = 1;
    public static final int AD_TYPE_PIC = 2;

    //获取文字插播
    //type = video \ pic  \ text
    private void getTextAd() {
        getAd(AD_TYPE_TEXT);
    }

    //获取视频插播
    private void getVideoAd() {
        getAd(AD_TYPE_VIDEO);
    }

    //获取图片插播
    private void getPicAd() {
        getAd(AD_TYPE_PIC);
    }

    private PushMSG pushMSG;

    //获取插播内容
    private void getAd(int type) {
        String category = "text";
        if (type == AD_TYPE_TEXT) {
            category = "text";
        } else if (type == AD_TYPE_VIDEO) {
            category = "video";
        } else if (type == AD_TYPE_PIC) {
            category = "pic";
        }
        OkGo.<String>post(Apis.HEADER + Apis.USER_IN_STREAM)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .params("mac", Utils.getMac(this))
                .params("type", category)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        if (response != null && response.body() != null) {
                            try {
                                pushMSG = JSON.parseObject(response.body(), PushMSG.class);
                                if (type == AD_TYPE_TEXT) {
                                    executeData(AD_TYPE_TEXT, pushMSG, response);
                                } else if (type == AD_TYPE_VIDEO) {
                                    executeData(AD_TYPE_VIDEO, pushMSG, response);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    public static String textResult, videoResult, picResult;//返回字符串result

    //处理返回数据
    private void executeData(int type, PushMSG mPushMSG, Response<String> response) {
        if (mPushMSG != null) {
            if (type == AD_TYPE_TEXT) {
                if (textResult == null) {
                    textResult = response.body();
                    if (mPushMSG.getCut_in() != null && mPushMSG.getCut_in().size() > 0) {
                        showAD(type, mPushMSG);
                    }
                } else {
                    if (!textResult.equals(response.body())) {
                        textResult = response.body();
                        try {
                            mAdListener.dismissAllText();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mPushMSG.getCut_in() != null && mPushMSG.getCut_in().size() > 0) {
                            showAD(type, mPushMSG);
                        }
                    }
                }
            } else if (type == AD_TYPE_VIDEO) {
                if (videoResult == null) {
                    videoResult = response.body();
                    if (mPushMSG != null && mPushMSG.getCut_in().size() > 0) {
                        showAD(type, mPushMSG);
                    }
                } else {
                    if (!videoResult.equals(response.body())) {
                        videoResult = response.body();
                        try {
                            if ((ActivityCollector.activities.get(ActivityCollector.activities.size() - 1) instanceof AdActivity)) {
                                ActivityCollector.finishActivity(ActivityCollector.activities.size() - 1);
                            }
                            if (mPushMSG.getCut_in() != null && mPushMSG.getCut_in().size() > 0) {
                                showAD(type, mPushMSG);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    //显示插播
    private void showAD(int type, PushMSG mPushMSG) {
        List<PushMSG.CutInBean> mCutInBeens;
        PushMSG.CutInBean mCutInBean;
        if (type == AD_TYPE_TEXT) {
            mCutInBeens = mPushMSG.getCut_in();
            if (mCutInBeens != null && mCutInBeens.size() > 0) {
                for (int i = 0; i < mCutInBeens.size(); i++) {
                    if (mCutInBeens.get(i).getStatus().equals("ON")) {
                        mCutInBean = mCutInBeens.get(i);
                        if (mCutInBean.getType().equals("emergency")) {
                            try {
                                mAdListener.showText(mCutInBean.getPlay_path(), mCutInBean.getPlace(),
                                        mCutInBean.getFont_size(), mCutInBean.getBack_color(),
                                        mCutInBean.getFont_color(), mCutInBean.getLucency_size());
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (mCutInBean.getType().equals("timer")) {
                            try {
                                mAdListener.showText(mCutInBean.getPlay_path(), mCutInBean.getPlace(),
                                        mCutInBean.getFont_size(), mCutInBean.getBack_color(),
                                        mCutInBean.getFont_color(), mCutInBean.getLucency_size());
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else if (type == AD_TYPE_VIDEO) {
            mCutInBeens = mPushMSG.getCut_in();
            if (mCutInBeens != null && mCutInBeens.size() > 0) {
                for (int i = 0; i < mCutInBeens.size(); i++) {
                    if (mCutInBeens.get(i).getStatus().equals("ON")) {
                        mCutInBean = mCutInBeens.get(i);
                        if (mCutInBean.getType().equals("emergency")) {
                            try {
                                mAdListener.showVideo(mCutInBean.getType(), mCutInBean.getPlay_path());
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (mCutInBean.getType().equals("timer")) {
                            try {
                                mAdListener.showVideo(mCutInBean.getType(), mCutInBean.getPlay_path());
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private static AdListener mAdListener;

    public static void setAdListener(AdListener madListener) {
        mAdListener = madListener;
    }

    public interface AdListener {
        void showText(String text, String place, String font_size,
                      String back_color, String font_color, String lucency_size);

        void showVideo(String type, String url);

        void dismissAllText();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
