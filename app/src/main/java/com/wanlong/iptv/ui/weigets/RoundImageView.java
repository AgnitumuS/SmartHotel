package com.wanlong.iptv.ui.weigets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lingchen on 2018/1/25. 15:00
 * mail:lingchen52@foxmail.com
 */

//    作者：前世小书童
//    链接：https://www.jianshu.com/p/626dbd93207d
//    來源：简书
//    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

public class RoundImageView extends AppCompatImageView {
    float width,height;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    public static int mBorderRadius = 12;

    @Override
    protected void onDraw(Canvas canvas) {

        if (width > mBorderRadius && height > mBorderRadius) {
            Path path = new Path();
            path.moveTo(mBorderRadius, 0);
            path.lineTo(width - mBorderRadius, 0);
            path.quadTo(width, 0, width, mBorderRadius);
            path.lineTo(width, height - mBorderRadius);
            path.quadTo(width, height, width - mBorderRadius, height);
            path.lineTo(mBorderRadius, height);
            path.quadTo(0, height, 0, height - mBorderRadius);
            path.lineTo(0, mBorderRadius);
            path.quadTo(0, 0, mBorderRadius, 0);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }

}
