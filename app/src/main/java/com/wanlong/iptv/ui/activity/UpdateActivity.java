package com.wanlong.iptv.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.entity.AppUpdate;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkUtils;

import java.io.File;

import butterknife.BindView;

/**
 * Created by lingchen on 2018/3/20. 15:09
 * mail:lingchen52@foxmail.com
 */
public class UpdateActivity extends BaseActivity {
    @BindView(R.id.checkversion)
    TextView mCheckversion;
    @BindView(R.id.version)
    TextView mVersion;

    @Override
    protected int getContentResId() {
        return R.layout.activity_update;
    }

    @Override
    protected void initView() {
        mVersion.setText("Version :" + getResources().getString(R.string.versionName));
    }

    private String url = "";

    @Override
    protected void initData() {
        if (App.RELEASE_VERSION) {
            url = Apis.HEADER + Apis.USER_APP_UPDATE;
        } else {
            url = Apis.HEADER + Apis.USER_APP_UPDATE_BETA;
        }
        update(url);
    }

    private AppUpdate appUpdate;

    //请求更新
    private void update(String url) {
        OkGo.<String>get(url)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            appUpdate = JSON.parseObject(response.body(), AppUpdate.class);
                            if (!App.RELEASE_VERSION && appUpdate.getApkUsage().equals("beta")) {
                                compareVersion();
                            } else if (App.RELEASE_VERSION && appUpdate.getApkUsage().equals("master")) {
                                compareVersion();
                            } else {
                                mCheckversion.setText(R.string.latast_version);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    private int apkVersion;
    private int currentVersion;
    private int verCode;
    private int currentVerCode;
    private String version;
    private StringBuffer sb;
    private boolean update;

    //比较版本
    private void compareVersion() {
        try {
            Log.d("UpdateActivity", getResources().getString(R.string.versionName));
            apkVersion = Integer.parseInt(appUpdate.getApkVersion()
                    .replaceAll(" ", ""));
            currentVersion = Integer.valueOf(getResources().getString(R.string.versionName)
                    .replaceAll(".", "")).intValue();
            verCode = Integer.parseInt(appUpdate.getVersionCode()
                    .replaceAll(" ", ""));
            currentVerCode = Integer.parseInt(getString(R.string.versionCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (apkVersion > currentVersion) {
            update = true;
        } else if (apkVersion == currentVersion) {
            if (App.RELEASE_VERSION) {
                update = false;
            } else {
                if (verCode > currentVerCode) {
                    update = true;
                } else {
                    update = false;
                }
            }
        } else {
            update = false;
        }
        if (update) {
            version = String.valueOf(verCode);
            sb = new StringBuffer();
            for (int i = 0; i < version.length(); i++) {
                sb.append(version.charAt(i) + ".");
            }
            sb.deleteCharAt(sb.length() - 1);
            showDialog();
        } else {
            mCheckversion.setText(R.string.latast_version);
        }
    }

    private AlertDialog.Builder mAlertDialog;
    private String mMessage;

    //提示更新
    private void showDialog() {
        if (App.RELEASE_VERSION) {
            mMessage = getString(R.string.current_version) +
                    getString(R.string.versionName) + "," +
                    getString(R.string.new_version) +
                    appUpdate.getApkVersion();
        } else {
            String version = String.valueOf(appUpdate.getApkVersion()
                    .replaceAll(" ", ""));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < version.length(); i++) {
                sb.append(version.charAt(i) + ".");
            }
            if (sb.toString().startsWith(".")) {
                sb = sb.deleteCharAt(0);
            }
            if (sb.toString().endsWith(".")) {
                sb = sb.deleteCharAt(sb.length() - 1);
            }
            mMessage = getString(R.string.current_version) +
                    getString(R.string.versionName) +
                    "(" + getString(R.string.versionCode) + ")，" +
                    getString(R.string.new_version) +
                    sb +
                    "(" + appUpdate.getVersionCode() + ")";
        }
        new AlertDialog.Builder(UpdateActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
                .setCancelable(true)
                .setTitle(getString(R.string.find_new_version))
                .setMessage(mMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadApk(appUpdate.getApkUrl());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private ProgressDialog progressDialog;

    //下载APK
    private void downloadApk(String url) {
        OkGo.<File>get(url)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new FileCallback() {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        progressDialog = new ProgressDialog(UpdateActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setCancelable(true);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setTitle("Downloading...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        progressDialog.dismiss();
                        ApkUtils.install(UpdateActivity.this, response.body().getAbsoluteFile());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        progressDialog.setProgress((int) (progress.fraction * 100));
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
