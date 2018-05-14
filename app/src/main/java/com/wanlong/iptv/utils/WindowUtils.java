package com.wanlong.iptv.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.wanlong.iptv.R;
import com.wanlong.iptv.ui.weigets.MarqueeTextView;

/**
 * Created by lingchen on 2018/4/16. 13:03
 * mail:lingchen52@foxmail.com
 */
public class WindowUtils {

    public static MarqueeTextView mMarqueeTextView;
    public static WindowManager wm;
    public static WindowManager.LayoutParams layoutParams;

    public static WindowManager initWindow(Context context) {
        if (wm == null) {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            //设置TextView的属性
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //这里是关键，使控件始终在最上方
            layoutParams.alpha = 1f;
            layoutParams.format = PixelFormat.TRANSLUCENT;
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                    | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;// 设置窗口类型为系统级
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            //这个Gravity也不能少，不然的话，下面"移动歌词"的时候就会出问题了～ 可以试试[官网文档有说明]
            layoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        }
        return wm;
    }

    public static MarqueeTextView getText(Context context) {
        if (mMarqueeTextView == null) {
            //创建自定义的TextView
            mMarqueeTextView = new MarqueeTextView(context);
            mMarqueeTextView.setFocusable(false);
            mMarqueeTextView.setIncludeFontPadding(false);
            mMarqueeTextView.setTextSize(36f);
            mMarqueeTextView.setPadding(0, 0, 0, 16);
            mMarqueeTextView.setTextColor(Color.WHITE);
            mMarqueeTextView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            mMarqueeTextView.setText("");
        }
        return mMarqueeTextView;
    }

    public static void addText() {
        if (wm != null && mMarqueeTextView != null) {
            wm.addView(mMarqueeTextView, layoutParams);
            wm.updateViewLayout(mMarqueeTextView, layoutParams);
        }
    }

    public static void removeText() {
        if (wm != null && mMarqueeTextView != null) {
            wm.removeView(mMarqueeTextView);
        }
    }

}
