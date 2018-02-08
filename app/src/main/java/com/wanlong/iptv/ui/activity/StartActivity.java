package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.wanlong.iptv.R;

import butterknife.BindView;

/**
 * Created by lingchen on 2018/1/24. 14:25
 * mail:lingchen52@foxmail.com
 */
public class StartActivity extends BaseActivity {
    @BindView(R.id.img_start)
    ImageView mImgStart;

    @Override
    protected int getContentResId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
//        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_image);
//        mImgStart.setAnimation(loadAnimation);
//        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
        mHandler.sendEmptyMessageDelayed(LOGIN, 1000);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

    }

    private static final int LOGIN = 0;

    @Override
    protected void initData() {

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN:
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
