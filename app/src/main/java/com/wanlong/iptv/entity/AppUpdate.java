package com.wanlong.iptv.entity;

/**
 * Created by hasee on 2017/3/21.
 */

public class AppUpdate {

    /**
     * apkName : iptv.apk
     * apkVersion :  136
     * versionCode : 1007
     * apkUrl : http://192.168.1.109:9080/apk/software/iptv.apk
     * description : Prevail IPTV DMM
     * apkUsage : beta
     */

    private String apkName;
    private String apkVersion;
    private String versionCode;
    private String apkUrl;
    private String description;
    private String apkUsage;

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApkUsage() {
        return apkUsage;
    }

    public void setApkUsage(String apkUsage) {
        this.apkUsage = apkUsage;
    }
}
