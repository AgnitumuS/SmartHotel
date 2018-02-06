package com.wanlong.hotel.imageloader;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by lingchen on 2018/1/12. 20:22
 * mail:lingchen52@foxmail.com
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setLogLevel(Log.DEBUG);
        int diskCacheSizeBytes = 1024*1024*250;  //250 MB
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,diskCacheSizeBytes));
    }

}

