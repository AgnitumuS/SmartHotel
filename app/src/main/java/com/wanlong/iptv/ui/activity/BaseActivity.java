package com.wanlong.iptv.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wanlong.iptv.R;
import com.wanlong.iptv.mvp.BasePresenter;
import com.wanlong.iptv.mvp.BaseView;
import com.wanlong.iptv.server.UpdateService;
import com.wanlong.iptv.utils.ActivityCollector;
import com.wanlong.iptv.utils.Utils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lingchen on 2018/1/24. 13:34
 * mail:lingchen52@foxmail.com
 */
public abstract class BaseActivity<T extends BasePresenter<? extends BaseView>> extends AutoLayoutActivity implements BaseView, UpdateService.UpdateListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActivityCollector.addActivity(this);
        Logger.d(getClass().getSimpleName());
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);
        //设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initWindowManager();
        setContentView(getContentResId());
        ButterKnife.bind(this);
        if (Utils.isPhone(this) || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission();
        }
        initView();
        initData();
        UpdateService.setAdListener(this);
    }

    protected abstract int getContentResId();

    protected void initWindowManager() {

    }

    protected abstract void initView();

    protected abstract void initData();

    //    作者：Ggx的代码之旅
    //    链接：https://www.jianshu.com/p/a14592dfc96a
    //    來源：简书
    //    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

    private T presenter;

    public T getPresenter() {
        return presenter;
    }

    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    //获取权限
    protected void getPermission() {
        RxPermissions rxPermissions = new RxPermissions(BaseActivity.this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean value) {
                        if (value) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestDrawOverLays();
                            }
                            Log.d("BaseActivity", "getpermission success");
                        } else {
                            Log.d("BaseActivity", "getpermission failed");
                            ActivityCollector.finishAll();
                            //退出程序
                            Process.killProcess(Process.myPid());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //参考自http://stackoverflow.com/questions/32061934/permission-from-manifest-doesnt-work-in-android-6
    //    作者：七号大蒜
//    链接：http://www.jianshu.com/p/2746a627c6d2
//    來源：简书
//    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestDrawOverLays() {
        if (!Settings.canDrawOverlays(BaseActivity.this)) {
            Toast.makeText(this, "为了应用正常运行，请勾选上“允许出现在其他应用上”", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + BaseActivity.this.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
                Log.d("BaseActivity", "Permission Denieddd by user.Please Check it in Settings");
//                Toast.makeText(this, "Permission Denieddd by user.Please Check it in Settings", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("BaseActivity", "Permission Allowed");
//                Toast.makeText(this, "Permission Allowed", Toast.LENGTH_SHORT).show();
                // Already hold the SYSTEM_ALERT_WINDOW permission, do addview or something.
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_PROG_RED:
                if (this instanceof LiveActivity) {

                } else {
                    startActivity(new Intent(this, LiveActivity.class));
                    if (this instanceof HomeActivity) {

                    } else {
                        finish();
                    }
                }
                break;
            case KeyEvent.KEYCODE_PROG_YELLOW:
                if (this instanceof VodListActivity) {

                } else {
                    startActivity(new Intent(this, VodListActivity.class));
                    if (this instanceof HomeActivity) {

                    } else {
                        finish();
                    }
                }
                break;
            case KeyEvent.KEYCODE_SETTINGS:
                if (this instanceof PasswordActivity) {

                } else {
                    startActivity(new Intent(this, PasswordActivity.class));
                    if (this instanceof HomeActivity) {

                    } else {
                        finish();
                    }
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.recycle();
        }
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void showDialog() {
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert)
                .setCancelable(false)
                .setMessage("检测到新版本，正在升级...")
                .show();
    }
}
