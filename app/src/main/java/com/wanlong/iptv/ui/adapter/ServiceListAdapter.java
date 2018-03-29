package com.wanlong.iptv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.ServicesData;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingchen on 2018/1/27. 15:08
 * mail:lingchen52@foxmail.com
 */
public class ServiceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ServicesData> mServicesDatas;
    private LayoutInflater mInflater;

    public ServiceListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mServicesDatas = new ArrayList<>();
    }

    public void setData(List<ServicesData> servicesDatas) {
        this.mServicesDatas.clear();
        this.mServicesDatas.addAll(servicesDatas);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_live_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setIsRecyclable(false);
//        viewHolder.mTvItemRecyclerLiveList.setText(mServicesDatas.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mServicesDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_recycler_live_list)
        AppCompatTextView mTvItemRecyclerLiveList;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
        }
    }
}
