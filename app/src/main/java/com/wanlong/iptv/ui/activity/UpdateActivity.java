package com.wanlong.iptv.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
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
import com.wanlong.iptv.callback.OnPackagedObserver;
import com.wanlong.iptv.entity.AppUpdate;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkController;
import com.wanlong.iptv.utils.ApkUtils;
import com.wanlong.iptv.utils.ApkVersion;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;

/**
 * Created by lingchen on 2018/3/20. 15:09
 * mail:lingchen52@foxmail.com
 */
public class UpdateActivity extends BaseActivity implements OnPackagedObserver {
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
        String version;
        if (ApkVersion.RELEASE_VERSION) {
            version = getString(R.string.versionName);
        } else {
            version = getString(R.string.versionName) + "(" + getString(R.string.versionCode) + ")";
        }
        mVersion.setText(getString(R.string.version) + version);
    }

    private String url = "";
    public static String apkPath = "";

    @Override
    protected void initData() {
        if (ApkVersion.RELEASE_VERSION) {
            url = Apis.HEADER + Apis.USER_APP_UPDATE;
        } else {
            url = Apis.HEADER + Apis.USER_APP_UPDATE_BETA;
        }
        Log.d("UpdateActivity", "hasRootPerssion:" + ApkController.hasRootPerssion());
        apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "Download" + File.separator + "app-debug.apk";
        if (ApkController.hasRootPerssion()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ApkController.clientInstall(apkPath);
//                    ApkController.copy2SystemApp(apkPath);
                    //ApkController.install(apkPath, App.getApplication());
                }
            }).start();
        } else if (Build.MODEL.equals("0008")) {
            try {
                pm = this.getPackageManager();
                Class<?>[] types = new Class[]{Uri.class, IPackageInstallObserver.class, int.class, String.class};
                Class<?>[] uninstalltypes = new Class[]{String.class, IPackageDeleteObserver.class, int.class};
                method = pm.getClass().getMethod("installPackage", types);
                uninstallmethod = pm.getClass().getMethod("deletePackage", uninstalltypes);
                method.setAccessible(true);
                File apkFile = new File(apkPath);
                Uri apkUri = Uri.fromFile(apkFile);
                method.invoke(pm, new Object[]{apkUri, mPackageInstallObserver, Integer.valueOf(2), "com.wanlong.iptv"});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            update(url);
        }
    }

    private PackageManager pm;
    private Method method, uninstallmethod;
    private OnPackagedObserver onInstalledPackaged;
    private PackageInstallObserver mPackageInstallObserver = new PackageInstallObserver();
    private PackageDeleteObserver mPackageDeleteObserver = new PackageDeleteObserver();

    @Override
    public void packageInstalled(String packageName, int returnCode) {

    }

    @Override
    public void packageDeleted(String packageName, int returnCode) {

    }

    class PackageInstallObserver extends IPackageInstallObserver.Stub {

        public void packageInstalled(String packageName, int returnCode) throws RemoteException {
            if (onInstalledPackaged != null) {
                onInstalledPackaged.packageInstalled(packageName, returnCode);
            }
        }
    }

    class PackageDeleteObserver extends IPackageDeleteObserver.Stub {

        public void packageDeleted(String packageName, int returnCode) throws RemoteException {
            if (onInstalledPackaged != null) {
                onInstalledPackaged.packageDeleted(packageName, returnCode);
            }
        }
    }

    public void uninstallPackage(String packagename) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        uninstallmethod.invoke(pm, new Object[]{packagename, mPackageDeleteObserver, 0});
    }

    public void installPackage(String apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        installPackage(new File(apkFile));
    }

    public void installPackage(File apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (!apkFile.exists()) throw new IllegalArgumentException();
        Uri packageURI = Uri.fromFile(apkFile);
        installPackage(packageURI);
    }

    public void installPackage(Uri apkFile) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        method.invoke(pm, new Object[]{apkFile, mPackageInstallObserver, 2, null});
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
                            if (!ApkVersion.RELEASE_VERSION && appUpdate.getApkUsage().equals("beta")) {
                                compareVersion();
                            } else if (ApkVersion.RELEASE_VERSION && appUpdate.getApkUsage().equals("master")) {
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
    private int versionCode;
    private int currentVersionCode;
    private String version;
    private boolean update;

    //比较版本
    private void compareVersion() {
        try {
            //解析服务器版本名称
            String server_apkVersion = appUpdate.getApkVersion()
                    .replaceAll(" ", "");
            StringBuffer sb_apkVersion = new StringBuffer();
            for (int i = 0; i < server_apkVersion.length(); i++) {
                if (!String.valueOf(server_apkVersion.charAt(i)).equals(".")) {
                    sb_apkVersion.append(server_apkVersion.charAt(i));
                }
            }
            apkVersion = Integer.parseInt(sb_apkVersion.toString());
            //解析本地版本名称
            String verName = getString(R.string.versionName)
                    .replaceAll(" ", "");
            StringBuffer sb_verName = new StringBuffer();
            for (int i = 0; i < verName.length(); i++) {
                if (!String.valueOf(verName.charAt(i)).equals(".")) {
                    sb_verName.append(verName.charAt(i));
                }
            }
            currentVersion = Integer.parseInt(sb_verName.toString());
            //解析服务器版本号
            versionCode = Integer.parseInt(appUpdate.getVersionCode()
                    .replaceAll(" ", ""));
            //解析本地版本号
            currentVersionCode = Integer.parseInt(getString(R.string.versionCode));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (apkVersion > currentVersion) {
            update = true;
        } else if (apkVersion == currentVersion) {
            if (ApkVersion.RELEASE_VERSION) {
                if (versionCode > currentVersionCode) {
                    update = true;
                } else {
                    update = false;
                }
//                update = false;
            } else {
                if (versionCode > currentVersionCode) {
                    update = true;
                } else {
                    update = false;
                }
            }
        } else {
            update = false;
        }
        if (update) {
            version = String.valueOf(versionCode);
            StringBuffer sb = new StringBuffer();
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
        if (ApkVersion.RELEASE_VERSION) {
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
                        progressDialog.setTitle(getString(R.string.downloading));
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
