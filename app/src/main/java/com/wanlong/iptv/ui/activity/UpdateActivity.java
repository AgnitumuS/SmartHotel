package com.wanlong.iptv.ui.activity;

import android.app.ProgressDialog;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.OkGo;
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
            url = Apis.HEADER + Apis.APP_UPDATE_RELEASE;
        } else {
            url = Apis.HEADER + Apis.APP_UPDATE_BETA;
        }
        update(url);
    }

    private AppUpdate appUpdate;

    private void update(String url) {
        OkGo.<String>get(url)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            appUpdate = JSON.parseObject(response.body(), AppUpdate.class);
//                            dawnload();
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

    private void dawnload() {
//        int verCode = appUpdate.getVersionCode();
//        if (verCode > Integer.parseInt(getResources().getString(R.string.versionCode))) {
//            String version = String.valueOf(verCode);
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < version.length(); i++) {
//                sb.append(version.charAt(i) + ".");
//            }
//            sb.deleteCharAt(sb.length() - 1);
//            new AlertDialog.Builder(UpdateActivity.this, R.style.Theme_AppCompat_Dialog_Alert)
//                    .setTitle("find new version")
//                    .setMessage(getResources().getString(R.string.current_version) +
//                            getResources().getString(R.string.versionName) + "，" +
//                            getResources().getString(R.string.new_version) + sb)
//                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            downloadApk(appUpdate.getApk_url());
//                        }
//                    })
//                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .show();
//        } else {
//            mCheckversion.setText(R.string.latast_version);
//        }
    }

    private ProgressDialog progressDialog;

    //下载APK
    private void downloadApk(String url) {
        OkGo.<File>get(url)
                .tag(this)
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
