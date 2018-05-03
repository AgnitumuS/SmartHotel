package com.wanlong.iptv.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.wanlong.iptv.R;
import com.wanlong.iptv.ui.weigets.MarqueeTextView;

/**
 * Created by lingchen on 2018/4/16. 13:03
 * mail:lingchen52@foxmail.com
 */
public class WindowUtils {
    private static final String LOG_TAG = "WindowUtils";
    private static View mView = null;
    private static WindowManager mWindowManager = null;
    private static Context mContext = null;

    public static Boolean isShown = false;

    public static MarqueeTextView mMarqueeTextView;
    public static WindowManager wm;
    public static WindowManager.LayoutParams layoutParams;

    public static void initWindow(Context context) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //设置TextView的属性
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //这里是关键，使控件始终在最上方
        layoutParams.alpha = 1f;
        layoutParams.format = 1;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;// 设置窗口类型为系统级
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //这个Gravity也不能少，不然的话，下面"移动歌词"的时候就会出问题了～ 可以试试[官网文档有说明]
        layoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        getText(context);
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
            addText();
        }
        return mMarqueeTextView;
    }

    public static void addText() {
        if (wm != null && mMarqueeTextView != null) {
            wm.addView(mMarqueeTextView, layoutParams);
            wm.updateViewLayout(mMarqueeTextView, layoutParams);
        }
    }

    /**
     * 显示弹出框
     *
     * @param context
     */
    public static void showPopupWindow(final Context context) {
        if (isShown) {
            return;
        }
        isShown = true;

        // 获取应用的Context
        mContext = context.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

//        mView = setUpView(context);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

        // 设置flag

        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        params.gravity = Gravity.TOP;

        mWindowManager.addView(mView, params);

    }

//    private static View setUpView(Context context) {
//        View view;
//        view = LayoutInflater.from(context).inflate(R.layout.window_view, null);
//        return view;
//    }

    /**
     * 隐藏弹出框
     */
    public static void hidePopupWindow() {
        if (isShown && null != mView) {
            mWindowManager.removeView(mView);
            isShown = false;
        }

    }
}
