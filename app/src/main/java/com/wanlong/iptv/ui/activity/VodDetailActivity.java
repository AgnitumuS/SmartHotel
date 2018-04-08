package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.entity.VodDetailData;
import com.wanlong.iptv.mvp.VodDetailPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class VodDetailActivity extends BaseActivity<VodDetailPresenter> implements VodDetailPresenter.VodDetailView {

    @BindView(R.id.img_movie_detail)
    ImageView mImgMovieDetail;
    @BindView(R.id.text_movie_name_detail)
    TextView mTextMovieNameDetail;
    @BindView(R.id.text_movie_time_detail)
    TextView mTextMovieTimeDetail;
    @BindView(R.id.text_movie_count_detail)
    TextView mTextMovieCountDetail;
    @BindView(R.id.text_movie_type_detail)
    TextView mTextMovieTypeDetail;
    @BindView(R.id.text_movie_people_detail)
    TextView mTextMoviePeopleDetail;
    @BindView(R.id.text_movie_description_detail)
    TextView mTextMovieDescriptionDetail;
    @BindView(R.id.text_movie_detail_play)
    TextView mTextMovieDetailPlay;
    @BindView(R.id.relativelayout_movie_detail)
    RelativeLayout mRelativelayoutMovieDetail;

    @Override
    protected int getContentResId() {
        return R.layout.activity_vod_detail;
    }

    private Intent intent;
    private String url;

    @Override
    protected void initView() {
        intent = getIntent();
        url = intent.getStringExtra("url");

//        mTextMovieCountDetail.setTextColor(Color.parseColor("#000000"));
//        mTextMovieCountDetail.setAlpha();
        mTextMovieNameDetail.setText(intent.getStringExtra("vod_name"));
        mTextMovieTimeDetail.setText("(" + intent.getStringExtra("vod_release_time") + ")");
        mTextMovieCountDetail.setText(intent.getStringExtra("vod_scores"));
        mTextMovieTypeDetail.setText("类型："+intent.getStringExtra("vod_category"));
        mTextMoviePeopleDetail.setText("主演："+intent.getStringExtra("vod_actor"));
        mTextMovieDescriptionDetail.setText("简介："+intent.getStringExtra("vod_detail"));
        Glide.with(this).load(intent.getStringExtra("vod_pic_dir")).into(mImgMovieDetail);
    }

    @Override
    protected void initData() {
//        setPresenter(new VodDetailPresenter(this));
//        getPresenter().loadVodDetailData("");
    }

    @OnClick(R.id.text_movie_detail_play)
    public void onViewClicked() {
        Intent intent = new Intent(VodDetailActivity.this, VodPlayActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void loadVodDetailSuccess(VodDetailData vodDetailData) {

    }

    @Override
    public void loadVodDetailFailed() {
//        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }

}
