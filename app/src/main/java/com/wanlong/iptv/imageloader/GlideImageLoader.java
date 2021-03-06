package com.wanlong.iptv.imageloader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.wanlong.iptv.R;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        //Glide.with(context).load(path).into(imageView);
        //缓存参数
        //ALL:缓存源资源和转换后的资源（即所有版本，默认行为）
        //NONE:不作任何磁盘缓存。然而，默认的它将仍然使用内存缓存！
        //SOURCE:仅缓存源资源（原来的全分辨率的图像）。
        //在我们上面略缩图的例子中，将会只有一个1000x1000 像素的图片
        //RESULT：缓存转换后的资源（最终的图像，即降低分辨率后的（或者是转换后的）
//        imageView.setBackgroundColor(context.getResources().getColor(R.color.white));
//        .transform(new RoundedCorners(25))1
//        GlideApp.with(context).load(path).fitCenter().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
        imageView.setFocusable(false);
        GlideApp.with(context)
                .load(path)
                .placeholder(mDrawable != null ? mDrawable : context.getResources().getDrawable(R.drawable.wooden_house))
                .centerCrop()
                .into(imageView);
    }

    private Drawable mDrawable;

    public GlideImageLoader(Context context, int position) {
        if (position == 1) {
            mDrawable = context.getResources().getDrawable(R.drawable.wooden_house);
        }
        if (position == 2) {
            mDrawable = context.getResources().getDrawable(R.drawable.weather);
        }
        if (position == 3) {
            mDrawable = context.getResources().getDrawable(R.drawable.sence);
        }
    }

    public GlideImageLoader() {
    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
//    @Override
//    public ImageView createImageView(Context context) {
//        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
//        RoundImageView roundImageView = new RoundImageView(context);
//        return roundImageView;
//    }

}
