package com.wanlong.iptv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
public class VodUrlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> types;
    private LayoutInflater mInflater;

    public VodUrlAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        types = new ArrayList<>();
    }

    public void setData(String[] vodType) {
        types.clear();
        for (int i = 0; i < vodType.length; i++) {
            types.add(vodType[i]);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_vod_url, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setIsRecyclable(false);
        viewHolder.mBtnMovieUrl.setText(position + 1 + "");
        viewHolder.mBtnMovieUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                mOnItemClickListener.onItemClick(viewHolder.mBtnMovieUrl, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_movie_url)
        Button mBtnMovieUrl;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
        }
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
