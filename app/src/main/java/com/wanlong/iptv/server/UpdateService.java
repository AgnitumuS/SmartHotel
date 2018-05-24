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
import android.support.v7.app.AlertDialog;

import com.wanlong.iptv.R;
import com.wanlong.iptv.callback.OnPackagedObserver;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.UpdateUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateService extends Service implements OnPackagedObserver, UpdateUtils.DownloadListener {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static final int INTERVAL_TIME = 10 * 60;//间隔时间:秒

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

    private void autoUpdate() {
        if (Build.MODEL.equals("0008")) {
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
        if (Build.MODEL.equals("0008")) {
            mFile = apkFile;
            new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                    .setCancelable(false)
                    .setMessage("检测到新版本，正在升级...")
                    .show();
            mHandler.sendEmptyMessageDelayed(0, 5 * 1000);
        }
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
