package com.wanlong.iptv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.VodList;
import com.wanlong.iptv.imageloader.GlideApp;
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
        if (mVodListDatas.get(position).getPic_url() != null &&
                mVodListDatas.get(position).getPic_url().size() > 0) {
            GlideApp.with(mContext)
                    .load(mVodListDatas.get(position).getVod_pic_dir() +
                            mVodListDatas.get(position).getPic_url().get(0))
                    .error(R.drawable.img_bg_color)
                    .into(viewHolder.mImgRecycleviewMovie);
        } else {
            GlideApp.with(mContext)
                    .load(R.drawable.img_bg_color)
                    .into(viewHolder.mImgRecycleviewMovie);
        }
        viewHolder.mTextRecycleviewMovieName.setText(mVodListDatas.get(position).getVod_name());
        if (mVodListDatas.get(position).getTotal_sets().equals("1")) {
            viewHolder.mTextRecycleviewMovieScore.setText(mVodListDatas.get(position).getVod_scores());
        } else {
            viewHolder.mTextRecycleviewMovieScore.setText(mContext.getString(R.string.updated) + " " +
                    mVodListDatas.get(position).getCurrent_sets() + "/" +
                    mContext.getString(R.string.total) + " " +
                    mVodListDatas.get(position).getTotal_sets());
        }
        viewHolder.mImgRecycleviewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                mOnItemClickListener.onItemClick(viewHolder.mImgRecycleviewMovie, position);
            }
        });
    }

    private String[] movies = {"Planet Earth1", "Planet Earth2", "Planet Earth3", "Planet Earth4", "Planet Earth5", "Planet Earth6", "Planet Earth7", "Planet Earth8", "Planet Earth9", "Planet Earth10",};

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

    private OnItemClickListener mOnItemClickListener;//声明接口

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
