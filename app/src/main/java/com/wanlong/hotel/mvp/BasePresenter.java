package com.wanlong.hotel.mvp;

import java.lang.ref.WeakReference;

/**
 * Created by lingchen on 2018/1/30. 14:45
 * mail:lingchen52@foxmail.com
 */

//    作者：Ggx的代码之旅
//    链接：https://www.jianshu.com/p/a14592dfc96a
//    來源：简书
//    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

public abstract class BasePresenter <T extends BaseView>{
    private WeakReference<T> weakReference;

    public BasePresenter(T baseView) {
        setView(baseView);
    }

    public T getView() {
        return weakReference!=null?weakReference.get():null;
    }

    public void setView(T view) {
        this.weakReference = new WeakReference<>(view);
    }

    public void recycle(){
        if(weakReference!=null){
            weakReference.clear();
            weakReference=null;
        }
    }

}
