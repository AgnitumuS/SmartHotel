package com.wanlong.hotel.player;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.wanlong.hotel.R;


/**
 * Created by luojianjun on 2017/9/25.
 */

public class VodVideoPlayer extends StandardGSYVideoPlayer {

    public VodVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public VodVideoPlayer(Context context) {
        super(context);
    }

    public VodVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        getDisplay(context);
    }

    /**
     * 亮度、进度、音频
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        float x = event.getX();
        float y = event.getY();
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            onClickUiToggle();
            startDismissControlViewTimer();
            return true;
        }
        if (id == R.id.fullscreen) {
            return false;
        }
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchSurfaceDown(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //触摸的X
//                    protected float mDownX;
                    //触摸的Y
//                    protected float mDownY;
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if ((mIfCurrentIsFullscreen && mIsTouchWigetFull)
                            || (mIsTouchWiget && !mIfCurrentIsFullscreen)) {
                        if (!mChangePosition && !mChangeVolume && !mBrightness) {
                            touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
                        }
                    }
                    touchSurfaceMove(deltaX, deltaY, y);
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    touchSurfaceUp();
                    startProgressTimer();
                    //不要和隐藏虚拟按键后，滑出虚拟按键冲突
                    if (mHideKey && mShowVKey) {
                        return true;
                    }
                    break;
            }
            gestureDetector.onTouchEvent(event);
        } else if (id == R.id.progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                case MotionEvent.ACTION_MOVE:
                    cancelProgressTimer();
                    ViewParent vpdown = getParent();
                    while (vpdown != null) {
                        vpdown.requestDisallowInterceptTouchEvent(true);
                        vpdown = vpdown.getParent();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    startProgressTimer();
                    ViewParent vpup = getParent();
                    while (vpup != null) {
                        vpup.requestDisallowInterceptTouchEvent(false);
                        vpup = vpup.getParent();
                    }
                    mBrightnessData = -1f;
                    break;
            }
        }
        return false;
    }

    //点击屏幕的默认值：屏幕中心点X,Y数值
    private static int pointX;
    private static int pointY;
    private static int moveX;
    private static int moveY;

    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private static void getDisplay(Context context){
        //获得屏幕宽高
        Point size = getDisplaySize(context);
        pointX = size.x/2;
        pointY = size.y/2;
        moveX = pointX;
        moveY = pointY;
    }

    //处理按键快进和快退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.ACTION_DOWN:
                break;
            case KeyEvent.ACTION_UP:
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                onClickUi();
                firstKeyDown();
                mHandler.sendEmptyMessage(LEFT);
                mHandler.sendEmptyMessageDelayed(CANCLE, 2500);
                resetTime();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                onClickUi();
                firstKeyDown();
                mHandler.sendEmptyMessage(RIGHT);
                mHandler.sendEmptyMessageDelayed(CANCLE, 2500);
                resetTime();
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                int state = getCurrentState();
                switch (state) {
                    case GSYVideoPlayer.CURRENT_STATE_PLAYING:
                        onVideoPause();
                        break;
                    case GSYVideoPlayer.CURRENT_STATE_PAUSE:
                        onVideoResume();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static boolean firstKeyDown = true;

    private void onClickUi(){
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            onClickUiToggle();
            startDismissControlViewTimer();
//                    return true;
        }
    }

    //第一次按下左右键
    private void firstKeyDown(){
        if(firstKeyDown){
            touchSurfaceDown(pointX, pointY);
            firstKeyDown = false;
        }

    }

    //连续按下左右键
    private void keyMove(){
        if ((mIfCurrentIsFullscreen && mIsTouchWigetFull)
                || (mIsTouchWiget && !mIfCurrentIsFullscreen)) {
            if (!mChangePosition && !mChangeVolume && !mBrightness) {
                touchSurfaceMoveFullLogic(Math.abs(moveX-pointX), 0);
            }
        }
        touchSurfaceMove(moveX-pointX, 0, pointY);
    }

    //定义变量
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int CANCLE = 2;

    private static int tim = 2;

    //程序启动时，初始化并发送消息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LEFT:
                    if(tim > 0){
                        if(tim > 1){
                            moveX = moveX - pointX/13;
                        }else {
                            moveX = moveX - pointX/20;
                        }
                        tim-=1;
                    }else {
                        moveX = moveX - pointX/200;
                    }
                    keyMove();
                    break;
                case RIGHT:
                    if(tim > 0){
                        if(tim > 1){
                            moveX = moveX + pointX/13;//13-20
                        }else {
                            moveX = moveX + pointX/20;
                        }
                        tim-=1;
                    }else {
                        moveX = moveX + pointX/200;
                    }
                    keyMove();
                    break;
                case CANCLE:                        //停止按键
                    moveX = pointX;
                    firstKeyDown = true;
                    tim = 2;
                    startDismissControlViewTimer();
                    touchSurfaceUp();
                    startProgressTimer();
                    //不要和隐藏虚拟按键后，滑出虚拟按键冲突
                    if (mHideKey && mShowVKey) {
//                        return true;
                    }
                    break;
            }
        }
    };

    //重置
    public void resetTime() {
            mHandler.removeMessages(CANCLE);
            mHandler.sendEmptyMessageDelayed(CANCLE, 2500);
    }

}
