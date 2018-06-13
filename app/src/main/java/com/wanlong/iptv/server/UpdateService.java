package com.wanlong.iptv.server;

import android.app.Service;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wanlong.iptv.callback.OnPackagedObserver;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.UpdateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateService extends Service implements OnPackagedObserver, UpdateUtils.DownloadListener {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static final int INTERVAL_TIME = 3 * 60;//间隔时间:秒

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mUpdateTimer != null && mUpdateTimerTask != null) {
            try {
                mUpdateTimer.schedule(mUpdateTimerTask, 0, INTERVAL_TIME * 1000);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    //时钟
    private Timer mUpdateTimer = new Timer(true);

    private TimerTask mUpdateTimerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                autoUpdate();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private String url = "";
    private boolean autoUpdate = false;

    private void autoUpdate() {
        if (Build.MODEL.equals("0008") && ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            autoUpdate = true;
        } else if (Build.MODEL.equals("KI PLUS") && ApkVersion.CURRENT_VERSION == ApkVersion.STANDARD_VERSION) {
            autoUpdate = true;
        } else {
            autoUpdate = false;
        }
        if (autoUpdate) {
            if (ApkVersion.RELEASE_VERSION) {
                url = Apis.HEADER + Apis.USER_APP_UPDATE;
            } else {
                url = Apis.HEADER + Apis.USER_APP_UPDATE_BETA;
            }
            UpdateUtils.setDownloadListener(this);
            UpdateUtils.checkUpdate(this, url, false, null);
        }
    }

    private File mFile;

    @Override
    public void downloadSuccess(File apkFile) {
        try {
            if (formetFileSize(getFileSize(apkFile), SIZETYPE_MB) > 10) {
                if (Build.MODEL.equals("0008") || Build.MODEL.equals("KI PLUS")) {
                    mFile = apkFile;
                    mUpdateListener.showDialog();
                    mHandler.sendEmptyMessageDelayed(0, 5 * 1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static UpdateListener mUpdateListener;

    public static void setAdListener(UpdateListener updateListener) {
        mUpdateListener = updateListener;
    }

    public interface UpdateListener {
        void showDialog();
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
                Log.e("UpdateService", "获取文件大小不存在!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double formetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    install(mFile.getAbsolutePath());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void downloadFailed() {

    }

    private void install(String apkPath) {
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }
        if (mUpdateTimerTask != null) {
            mUpdateTimerTask.cancel();
            mUpdateTimerTask = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private PackageManager pm;
    private Method method, uninstallmethod;
    private OnPackagedObserver onInstalledPackaged;
    private PackageInstallObserver mPackageInstallObserver = new PackageInstallObserver();
    private PackageDeleteObserver mPackageDeleteObserver = new PackageDeleteObserver();

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

    @Override
    public void packageInstalled(String packageName, int returnCode) {

    }

    @Override
    public void packageDeleted(String packageName, int returnCode) {

    }
}
