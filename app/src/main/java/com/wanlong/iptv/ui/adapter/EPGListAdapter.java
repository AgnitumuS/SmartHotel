package com.wanlong.iptv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.Live;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.utils.ApkVersion;
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
public class EPGListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Live.PlaylistBean> mLiveListDatas;
    private LayoutInflater mInflater;
    private int mlastPosition;

    public EPGListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLiveListDatas = new ArrayList<>();
    }

    public void setData(List<Live.PlaylistBean> liveListDatas, int lastPosition) {
        if (liveListDatas.size() >= lastPosition) {
            mlastPosition = lastPosition;
        } else {
            mlastPosition = 0;
        }
        this.mLiveListDatas.clear();
        this.mLiveListDatas.addAll(liveListDatas);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_epg_live_list, parent, false);
        return new ViewHolder(view);
    }

    private int lastPosition;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setIsRecyclable(false);
        if (position == mlastPosition) {
            viewHolder.mReLiveChannel.requestFocus();
        }
        if (position == 0) {
            viewHolder.mTvItemRecyclerLiveNumber.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.mTvItemLiveList.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            viewHolder.mImgItemLiveIcon.setVisibility(View.GONE);
            viewHolder.mTvItemLiveList.setText(mLiveListDatas.get(position).getService_name());
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.STANDARD_VERSION) {
            if (!mLiveListDatas.get(position).getIcon().equals("")) {
                GlideApp.with(mContext)
                        .load(mLiveListDatas.get(position).getIcon())
                        .centerInside()
                        .into(viewHolder.mImgItemLiveIcon);
                viewHolder.mTvItemLiveList.setText("");
            } else {
                viewHolder.mImgItemLiveIcon.setVisibility(View.GONE);
                viewHolder.mTvItemLiveList.setText(mLiveListDatas.get(position).getService_name());
            }
        }
        viewHolder.mTvItemRecyclerLiveNumber.setText(mLiveListDatas.get(position).getProgram_num());
        viewHolder.mReLiveChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                viewHolder.mTvItemRecyclerLiveNumber.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.mTvItemLiveList.setTextColor(mContext.getResources().getColor(R.color.white));
                mOnItemClickListener.onItemClick(viewHolder.mTvItemLiveList, position, lastPosition);
                lastPosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLiveListDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.re_live_channel)
        AutoLinearLayout mReLiveChannel;
        @BindView(R.id.tv_item_recycler_live_number)
        AppCompatTextView mTvItemRecyclerLiveNumber;
        @BindView(R.id.img_item_recycler_live_icon)
        ImageView mImgItemLiveIcon;
        @BindView(R.id.tv_item_recycler_live_list)
        AppCompatTextView mTvItemLiveList;

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
