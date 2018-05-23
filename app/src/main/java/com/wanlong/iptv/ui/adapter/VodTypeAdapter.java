package com.wanlong.iptv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanlong.iptv.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingchen on 2018/1/27. 15:08
 * mail:lingchen52@foxmail.com
 */
public class VodTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> types;
    private LayoutInflater mInflater;

    public VodTypeAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        categorys = mContext.getResources().getStringArray(R.array.vod_category);
        types = new ArrayList<>();
    }

    public void setData(List<String> vodType) {
        types.clear();
        types.addAll(vodType);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_vod_category, parent, false);
        return new ViewHolder(view);
    }

    private int lastPosition;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setIsRecyclable(false);
        viewHolder.mTvItemRecyclerVodCategory.setText(types.get(position));
//        viewHolder.mTvItemRecyclerVodCategory.setText(categorys[position]);
        if (position == 0) {
            viewHolder.mTvItemRecyclerVodCategory.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        viewHolder.mTvItemRecyclerVodCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                ((TextView) v).setTextColor(mContext.getResources().getColor(R.color.white));
                mOnItemClickListener.onItemClick(viewHolder.mTvItemRecyclerVodCategory, position, lastPosition);
                lastPosition = position;
            }
        });

    }

    private String[] categorys = {"搜    索", "全    部", "筛    选", "猜你喜欢", "港片情怀",
            "动画电影", "动作科幻", "欧美大片", "爆笑喜剧", "浪漫爱情", "高分佳片"};

    @Override
    public int getItemCount() {
        return types.size();
//        return categorys.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_recycler_vod_category)
        TextView mTvItemRecyclerVodCategory;

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
