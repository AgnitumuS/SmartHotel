package com.wanlong.iptv.entity;

/**
 * Created by hasee on 2017/3/21.
 */

public class Update {

    /**
     * apkname : iptv.apk
     * verName : 1.0.0
     * verCode : 2
     * apk_url : http://192.168.1.216:9080/apk/software/iptv.apk
     */

    private String apkname;
    private int verCode;
    private String apk_url;

    public String getApkname() {
        return apkname;
    }

    public void setApkname(String apkname) {
        this.apkname = apkname;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }
}
