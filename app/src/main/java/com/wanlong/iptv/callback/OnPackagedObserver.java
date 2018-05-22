package com.wanlong.iptv.callback;

/**
 * Created by lingchen on 2018/5/21. 16:03
 * mail:lingchen52@foxmail.com
 */
public interface OnPackagedObserver {
    public void packageInstalled(String packageName, int returnCode);
    public void packageDeleted(String packageName,int returnCode);
}
