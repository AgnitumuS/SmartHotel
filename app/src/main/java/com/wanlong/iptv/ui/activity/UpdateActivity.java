package com.wanlong.iptv.ui.activity;

import android.widget.TextView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.utils.Apis;
import com.wanlong.iptv.utils.ApkUtils;
import com.wanlong.iptv.utils.ApkVersion;
import com.wanlong.iptv.utils.UpdateUtils;

import java.io.File;

import butterknife.BindView;

/**
 * Created by lingchen on 2018/3/20. 15:09
 * mail:lingchen52@foxmail.com
 */
public class UpdateActivity extends BaseActivity implements UpdateUtils.DownloadListener {
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

    @Override
    protected void initData() {
        if (ApkVersion.RELEASE_VERSION) {
            url = Apis.HEADER + Apis.USER_APP_UPDATE;
        } else {
            url = Apis.HEADER + Apis.USER_APP_UPDATE_BETA;
        }
        UpdateUtils.setDownloadListener(this);
        UpdateUtils.checkUpdate(this, url, true, mCheckversion);
    }

    @Override
    public void downloadSuccess(File apkFile) {
        ApkUtils.install(this, apkFile);
    }

    @Override
    public void downloadFailed() {

    }
}
