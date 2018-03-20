package com.wanlong.iptv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.LiveData;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingchen on 2018/1/27. 15:08
 * mail:lingchen52@foxmail.com
 */
public class LiveCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LiveData mLiveData;
    private LayoutInflater mInflater;
    private String[] categorys;

    public LiveCategoryAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        categorys = mContext.getResources().getStringArray(R.array.live_category);
    }

    public void setData(LiveData liveData) {
        mLiveData = liveData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_live_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setIsRecyclable(false);
        viewHolder.mTvItemRecyclerLivecategory.setText(categorys[position]);
    }

    @Override
    public int getItemCount() {
        return categorys.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_recycler_live_category)
        TextView mTvItemRecyclerLivecategory;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
        }
    }
}
