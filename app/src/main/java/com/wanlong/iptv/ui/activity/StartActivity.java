package com.wanlong.iptv.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.utils.Apis;

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
        GlideApp.with(this).load(R.drawable.hotel_logo).into(mImgStart);
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

//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

    }

    private static final int LOGIN = 0;
    private static final int OPEN = 1;
    private SharedPreferences sharedPreferences;
    private boolean firstOpen;
    private String ip;

    @Override
    protected void initData() {
        createSP();
    }

    private void createSP() {
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        firstOpen = sharedPreferences.getBoolean("firstOpen", true);
        ip = sharedPreferences.getString("ip", "");
        if (ip.equals("")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ip", Apis.HEADER);
            editor.commit();
            ip = sharedPreferences.getString("ip", "");
        }
        Apis.HEADER = ip;
        if (firstOpen) {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("first", false);
//            editor.commit();
            mHandler.sendEmptyMessageDelayed(LOGIN, 1000);
        } else {
            mHandler.sendEmptyMessageDelayed(OPEN, 1000);
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN:
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    finish();
                    break;
                case OPEN:
                    startActivity(new Intent(StartActivity.this, HomeActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
