package com.wanlong.iptv.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wanlong.iptv.ui.activity.StartActivity;

/**
 * Created by lingchen on 2018/5/21. 17:06
 * mail:lingchen52@foxmail.com
 */
public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
//            Toast.makeText(context, "升级成功", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(context, StartActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }

        //接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
//            System.out.println("安装了:" +packageName + "包名的程序");
        }

        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
//            System.out.println("卸载了:"  + packageName + "包名的程序");

        }
    }
}
