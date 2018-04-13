package com.wanlong.iptv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.VodList;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingchen on 2018/1/27. 15:08
 * mail:lingchen52@foxmail.com
 */
public class VodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<VodList.PlaylistBean> mVodListDatas;
    private LayoutInflater mInflater;

    public VodListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mVodListDatas = new ArrayList<>();
    }

    public void setData(List<VodList.PlaylistBean> vodListDatas) {
        mVodListDatas.clear();
        mVodListDatas.addAll(vodListDatas);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_vod_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setIsRecyclable(false);
        if(mVodListDatas.get(position).getPic_url().equals("")){
            Glide.with(mContext).load(R.drawable.sence).into(viewHolder.mImgRecycleviewMovie);
        }else {
            Glide.with(mContext).load(mVodListDatas.get(position).getPic_url()).into(viewHolder.mImgRecycleviewMovie);
        }
        viewHolder.mTextRecycleviewMovieName.setText(mVodListDatas.get(position).getVod_name());
//        viewHolder.mTextRecycleviewMovieName.setText(movies[position]);
        viewHolder.mTextRecycleviewMovieScore.setText(mVodListDatas.get(position).getVod_scores());
        viewHolder.mImgRecycleviewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                mOnItemClickListener.onItemClick(viewHolder.mImgRecycleviewMovie,position);
            }
        });
    }

    private String[] movies = {"Planet Earth1","Planet Earth2","Planet Earth3","Planet Earth4","Planet Earth5","Planet Earth6","Planet Earth7","Planet Earth8","Planet Earth9","Planet Earth10",};

    @Override
    public int getItemCount() {
        return mVodListDatas.size();
//        return movies.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_recycleview_movie)
        ImageView mImgRecycleviewMovie;
        @BindView(R.id.view_recycleview_movie_type)
        View mViewRecycleviewMovieType;
        @BindView(R.id.text_recycleview_movie_name)
        AppCompatTextView mTextRecycleviewMovieName;
        @BindView(R.id.text_recycleview_movie_score)
        AppCompatTextView mTextRecycleviewMovieScore;
        @BindView(R.id.relativelayout_movie_list)
        AutoRelativeLayout mRelativelayoutMovieList;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
        }
    }

    private VodTypeAdapter.OnItemClickListener mOnItemClickListener;//声明接口

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(VodTypeAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
