package com.wanlong.iptv.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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
import com.wanlong.iptv.entity.AppUpdate;

import java.io.File;

/**
 * Created by lingchen on 2018/5/22. 09:17
 * mail:lingchen52@foxmail.com
 */
public class UpdateUtils {

    public static AppUpdate appUpdate;

    //请求更新
    public static void checkUpdate(Context context, String url, boolean showDialog, TextView mCheckversion) {
        OkGo.<String>get(url)
                .tag(context)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.json(response.body());
                        try {
                            appUpdate = JSON.parseObject(response.body(), AppUpdate.class);
                            if (!ApkVersion.RELEASE_VERSION && appUpdate.getApkUsage().equals("beta")) {
                                if (compareVersion(context, mCheckversion)) {
                                    if (showDialog) {
                                        showDialog(context, showDialog);
                                    } else {
                                        downloadApk(context, appUpdate.getApkUrl(), false);
                                    }
                                }
                            } else if (ApkVersion.RELEASE_VERSION && appUpdate.getApkUsage().equals("master")) {
                                if (compareVersion(context, mCheckversion)) {
                                    if (showDialog) {
                                        showDialog(context, showDialog);
                                    } else {
                                        downloadApk(context, appUpdate.getApkUrl(), false);
                                    }
                                }
                            } else {
                                if (mCheckversion != null) {
                                    mCheckversion.setText(R.string.latast_version);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (mCheckversion != null) {
                                mCheckversion.setText(R.string.latast_version);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (mCheckversion != null) {
                            mCheckversion.setText(R.string.latast_version);
                        }
                    }
                });
    }

    public static int apkVersion;
    public static int currentVersion;
    public static int versionCode;
    public static int currentVersionCode;
    public static String version;
    public static boolean update;

    //比较版本
    public static boolean compareVersion(Context context, TextView mCheckversion) {
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
            String verName = context.getString(R.string.versionName)
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
            currentVersionCode = Integer.parseInt(context.getString(R.string.versionCode));
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
            deleteApk(context);
            version = String.valueOf(versionCode);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < version.length(); i++) {
                sb.append(version.charAt(i) + ".");
            }
            sb.deleteCharAt(sb.length() - 1);
        } else {
            if (mCheckversion != null) {
                mCheckversion.setText(R.string.latast_version);
            }
        }
        return update;
    }

    public static AlertDialog.Builder mAlertDialog;
    public static String mMessage;

    //提示更新
    public static void showDialog(Context context, boolean showDialog) {
        if (ApkVersion.RELEASE_VERSION) {
            mMessage = context.getString(R.string.current_version) +
                    context.getString(R.string.versionName) + "," +
                    context.getString(R.string.new_version) +
                    UpdateUtils.appUpdate.getApkVersion();
        } else {
            String version = String.valueOf(UpdateUtils.appUpdate.getApkVersion()
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
            mMessage = context.getString(R.string.current_version) +
                    context.getString(R.string.versionName) +
                    "(" + context.getString(R.string.versionCode) + ")，" +
                    context.getString(R.string.new_version) +
                    sb +
                    "(" + UpdateUtils.appUpdate.getVersionCode() + ")";
        }
        new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert)
                .setCancelable(true)
                .setTitle(context.getString(R.string.find_new_version))
                .setMessage(mMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadApk(context, appUpdate.getApkUrl(), showDialog);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public static ProgressDialog progressDialog;

    //下载APK
    public static void downloadApk(Context context, String url, boolean showDialog) {
        deleteApk(context);
        OkGo.<File>get(url)
                .tag(context)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new FileCallback() {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        if (showDialog) {
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setCancelable(true);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setTitle(context.getString(R.string.downloading));
                            progressDialog.setIndeterminate(false);
                            progressDialog.show();
                        }
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        if (showDialog) {
                            progressDialog.dismiss();
                        }
                        ApkVersion.getSP(context).edit()
                                .putString("apkFile", response.body().getAbsoluteFile().getAbsolutePath())
                                .commit();
                        mDownloadListener.downloadSuccess(response.body().getAbsoluteFile());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        if (showDialog) {
                            progressDialog.setProgress((int) (progress.fraction * 100));
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        mDownloadListener.downloadFailed();
                    }
                });
    }

    //删除APK
    public static void deleteApk(Context context) {
        SharedPreferences mSharedPreferences = ApkVersion.getSP(context);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        String apkFile = mSharedPreferences.getString("apkFile", "");
        if (!apkFile.equals("")) {
            try {
                File file = new File(apkFile);
                if (file.exists() && file.isFile()) {
                    if (file.delete()) {
                        Logger.d("删除成功");
                        mEditor.putString("apkFile", "");
                        mEditor.commit();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static DownloadListener mDownloadListener;

    public static void setDownloadListener(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    public interface DownloadListener {
        void downloadSuccess(File apkFile);

        void downloadFailed();

    }
}
