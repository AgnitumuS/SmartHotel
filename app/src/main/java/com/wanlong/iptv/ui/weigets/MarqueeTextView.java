package com.wanlong.iptv.ui.weigets;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 不依赖焦点和选中的TextView跑马灯
 * Created by lingchen on 2018/3/7. 13:46
 * mail:lingchen52@foxmail.com
 * http://www.cnblogs.com/over140/p/3687952.html
 */

public class MarqueeTextView extends AppCompatTextView {

    private boolean mStopMarquee;//是否停止移动
    private String mText;
    private float mCoordinateX;//移动时的偏离度
    private float mTextWidth;
    private float windowsWith;//控件的总长度

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text) {
        this.mText = text;
        mTextWidth = getPaint().measureText(mText);
        if (mHandler.hasMessages(0)) {
            mHandler.removeMessages(0);
        }
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    private boolean textIsEmpty(String mText) {
        if (mText == null || mText.equals("")) {
            return true;
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        mStopMarquee = false;
        if (!textIsEmpty(mText)) {
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mStopMarquee = true;
        if (mHandler.hasMessages(0)) {
            mHandler.removeMessages(0);
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        windowsWith = canvas.getWidth();
        if (!textIsEmpty(mText)) {
            canvas.drawText(mText, mCoordinateX + windowsWith, 30, getPaint());
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (Math.abs(mCoordinateX) > (mTextWidth + windowsWith)) {//当移动的偏离度大于总长度
                        mCoordinateX = 0;
                        invalidate();
                        if (!mStopMarquee) {
                            sendEmptyMessageDelayed(0, 2000);
                        }
                    } else {
                        mCoordinateX -= 2;//这里越大移动速度就越快
                        invalidate();
                        if (!mStopMarquee) {
                            sendEmptyMessageDelayed(0, 30);
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
