package com.wanlong.iptv.entity;

/**
 * Created by hasee on 2017/3/21.
 */

public class AppUpdate {


    /**
     * apkVersion : 00-fdgfdA3
     * versionCode : 32432f
     * apkUrl : 192.168.1.109:8021/files/20180326133256/adsfsfv.mp3
     * apkUsage : beta
     */

    private String apkVersion;
    private String versionCode;
    private String apkUrl;
    private String apkUsage;

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getApkUsage() {
        return apkUsage;
    }

    public void setApkUsage(String apkUsage) {
        this.apkUsage = apkUsage;
    }
}
