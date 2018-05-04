package com.wanlong.iptv.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.app.App;
import com.wanlong.iptv.server.AdService;
import com.wanlong.iptv.ui.activity.HomeActivity;
import com.wanlong.iptv.ui.activity.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2017/5/22.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    /**
     * 系统默认UncaughtExceptionHandler
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * context
     */
    private Context mContext;

    /**
     * 存储异常和参数信息
     */
    private Map<String, String> paramsMap = new HashMap<>();

    /**
     * 格式化时间
     */
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private String TAG = this.getClass().getSimpleName();

    private static CrashHandler mInstance;

    private CrashHandler() {

    }

    /**
     * 获取CrashHandler实例
     */
    public static synchronized CrashHandler getInstance() {
        if (null == mInstance) {
            mInstance = new CrashHandler();
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为系统默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * uncaughtException 回调函数
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (!handleException(ex) && mDefaultHandler != null) {//如果自己没处理交给系统处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {//自己处理
            try {//延迟3秒杀进程
                Thread.sleep(1000); // 1秒后重启，可有可无，仅凭个人喜好
                mContext.stopService(new Intent(mContext, AdService.class));
                App.ADserver = false;
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("from", "CrashHandler");
                mContext.startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "error : ", e);
                Logger.e(TAG + "  error : " + e);
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        }
    }

    public Class<?> getTopActivity() {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String className = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * 收集错误信息.发送到服务器
     *
     * @return 处理了该异常返回true, 否则false
     */
    private boolean showToast;
    private boolean filehasUpload;

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //添加自定义信息
        addCustomInfo();
        //使用Toast来显示异常信息
        if (!showToast) {
            showToast = true;
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
//                    Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
        }
        //保存日志文件
        String filename = saveCrashInfo2File(ex);
        if (filename != null && Utils.isNetworkConnected(mContext) && !filehasUpload) {
            uploadFile(filename);
        }
        return true;
    }

    /**
     * 上传文件到服务器
     */
    private void uploadFile(String filename) {
        Log.d("hotel-crash", filename);
        Log.d("hotel-crash", Apis.HEADER + Apis.USER_CRASHLOG_UPLOAD);
        final String file = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crash/" + filename;
        Log.d("hotel-crash", file);
        List<File> files = new ArrayList<>();
        files.add(new File(filename));
        OkGo.<String>post(Apis.HEADER + Apis.USER_CRASHLOG_UPLOAD)
                .tag(this)
                .cacheMode(CacheMode.NO_CACHE)
//                .params("mac",Utils.getMac(mContext))
//                .upFile(new File(file))
                .params("crashlog", new File(file))
//                .addFileParams("crashlog",files)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d("hotel-crash", response.body().toString());
                        Log.d("hotel-crash", "upload error log success");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        File dir = new File(file);
                        if (dir.exists()) {
                            dir.delete();
                        }
                        filehasUpload = true;
                        Log.d("hotel-crash", "upload error log finish");
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        super.uploadProgress(progress);
                        Log.d("hotel-crash", "progress:" + progress.currentSize);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        filehasUpload = false;
                        Log.d("hotel-crash", "upload error log failed");
                    }
                });
        Log.d("hotel-crash", "OkGo");
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        //获取versionName,versionCode
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                paramsMap.put("versionName", versionName);
                paramsMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(TAG + "  an error occured when collect package info  " + e);
        }
        //获取所有系统信息
        Field[] fields = Build.class.getDeclaredFields();
        Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                paramsMap.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Logger.e(TAG + "  an error occured when collect crash info  " + e);
            }
        }
    }

    /**
     * 添加自定义参数
     */
    private void addCustomInfo() {

    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp;
            if (App.newtime != 0) {
                timestamp = App.newtime * 1000;
            } else {
                timestamp = System.currentTimeMillis();
            }
            String time = format.format(new Date(timestamp));
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Logger.e(TAG + "  an error occured while writing file...  " + e);
        }
        return null;
    }
}
