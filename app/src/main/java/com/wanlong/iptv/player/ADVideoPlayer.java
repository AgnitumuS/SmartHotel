package com.wanlong.iptv.player;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.wanlong.iptv.R;

/**
 * 无任何控制ui的播放
 * Created by luojianjun on 2017/8/21.
 */

public class ADVideoPlayer extends StandardGSYVideoPlayer {
    public ADVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public ADVideoPlayer(Context context) {
        super(context);
    }

    public ADVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.empty_control_video;
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }

    @Override
    protected void touchDoubleUp() {
//        super.touchDoubleUp();
        //不需要双击暂停
    }
}
