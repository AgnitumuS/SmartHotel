package com.wanlong.iptv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.EPG;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingchen on 2018/5/28. 15:08
 * mail:lingchen52@foxmail.com
 */
public class EPGDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<EPG.DetailBean> mDetailBeans;
    private LayoutInflater mInflater;

    public EPGDetailAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDetailBeans = new ArrayList<>();
    }

    public void setData(List<EPG.DetailBean> detailBeans) {
        this.mDetailBeans.clear();
        if (detailBeans != null) {
            this.mDetailBeans.addAll(detailBeans);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_epg_list, parent, false);
        return new ViewHolder(view);
    }

    private int lastPosition;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setIsRecyclable(false);
        viewHolder.mTvItemRecyclerEpgTime.setText(mDetailBeans.get(position).getTime());
        viewHolder.mTvItemRecyclerEpgDetail.setText(mDetailBeans.get(position).getProgram());
        viewHolder.mLlItemRecyclerEpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                mOnItemClickListener.onItemClick(viewHolder.mLlItemRecyclerEpg, position, lastPosition);
                lastPosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDetailBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_recycler_epg_time)
        AppCompatTextView mTvItemRecyclerEpgTime;
        @BindView(R.id.tv_item_recycler_epg_detail)
        AppCompatTextView mTvItemRecyclerEpgDetail;
        @BindView(R.id.ll_item_recycler_epg)
        AutoLinearLayout mLlItemRecyclerEpg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
        }
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int lastPosition);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
