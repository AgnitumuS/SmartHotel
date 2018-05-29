package com.wanlong.iptv.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanlong.iptv.R;
import com.wanlong.iptv.app.App;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingchen on 2018/5/28. 15:08
 * mail:lingchen52@foxmail.com
 */
public class EPGTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater mInflater;
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    public EPGTimeAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
    }

    public void setDate(int date) {
        mList.clear();
        mList.add(mContext.getResources().getString(R.string.today));
        String[] day_of_week = mContext.getResources().getStringArray(R.array.day_of_week);
        switch (date) {
            case SUNDAY:
                mList.add(day_of_week[SATURDAY]);
                mList.add(day_of_week[FRIDAY]);
                mList.add(day_of_week[THURSDAY]);
                mList.add(day_of_week[WEDNESDAY]);
                mList.add(day_of_week[TUESDAY]);
                mList.add(day_of_week[MONDAY]);
                break;
            case MONDAY:
                mList.add(day_of_week[SUNDAY]);
                mList.add(day_of_week[SATURDAY]);
                mList.add(day_of_week[FRIDAY]);
                mList.add(day_of_week[THURSDAY]);
                mList.add(day_of_week[WEDNESDAY]);
                mList.add(day_of_week[TUESDAY]);
                break;
            case TUESDAY:
                mList.add(day_of_week[MONDAY]);
                mList.add(day_of_week[SUNDAY]);
                mList.add(day_of_week[SATURDAY]);
                mList.add(day_of_week[FRIDAY]);
                mList.add(day_of_week[THURSDAY]);
                mList.add(day_of_week[WEDNESDAY]);
                break;
            case WEDNESDAY:
                mList.add(day_of_week[TUESDAY]);
                mList.add(day_of_week[MONDAY]);
                mList.add(day_of_week[SUNDAY]);
                mList.add(day_of_week[SATURDAY]);
                mList.add(day_of_week[FRIDAY]);
                mList.add(day_of_week[THURSDAY]);

                break;
            case THURSDAY:
                mList.add(day_of_week[WEDNESDAY]);
                mList.add(day_of_week[TUESDAY]);
                mList.add(day_of_week[MONDAY]);
                mList.add(day_of_week[SUNDAY]);
                mList.add(day_of_week[SATURDAY]);
                mList.add(day_of_week[FRIDAY]);
                break;
            case FRIDAY:
                mList.add(day_of_week[THURSDAY]);
                mList.add(day_of_week[WEDNESDAY]);
                mList.add(day_of_week[TUESDAY]);
                mList.add(day_of_week[MONDAY]);
                mList.add(day_of_week[SUNDAY]);
                mList.add(day_of_week[SATURDAY]);
                break;
            case SATURDAY:
                mList.add(day_of_week[FRIDAY]);
                mList.add(day_of_week[THURSDAY]);
                mList.add(day_of_week[WEDNESDAY]);
                mList.add(day_of_week[TUESDAY]);
                mList.add(day_of_week[MONDAY]);
                mList.add(day_of_week[SUNDAY]);
                break;
            default:
                break;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_epg_time, parent, false);
        return new ViewHolder(view);
    }

    private int lastPosition;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setIsRecyclable(false);
        if (position == 0) {
            viewHolder.mTvItemRecyclerEpgTimeWeek.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.mTvItemRecyclerEpgTimeDate.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        viewHolder.mTvItemRecyclerEpgTimeWeek.setText(mList.get(position));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
        String date = simpleDateFormat.format(new Date((App.newtime - position * 24 * 3600) * 1000));
        viewHolder.mTvItemRecyclerEpgTimeDate.setText(date);
        viewHolder.mLlItemRecyclerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                viewHolder.mTvItemRecyclerEpgTimeWeek.setTextColor(mContext.getResources().getColor(R.color.white));
                viewHolder.mTvItemRecyclerEpgTimeDate.setTextColor(mContext.getResources().getColor(R.color.white));
                mOnItemClickListener.onItemClick(viewHolder.mLlItemRecyclerTime, position, lastPosition);
                lastPosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_recycler_epg_time_week)
        AppCompatTextView mTvItemRecyclerEpgTimeWeek;
        @BindView(R.id.tv_item_recycler_epg_time_date)
        AppCompatTextView mTvItemRecyclerEpgTimeDate;
        @BindView(R.id.ll_item_recycler_time)
        AutoLinearLayout mLlItemRecyclerTime;

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
