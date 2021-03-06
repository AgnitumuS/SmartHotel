package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.imageloader.GlideApp;
import com.wanlong.iptv.mvp.VodDetailPresenter;
import com.wanlong.iptv.ui.adapter.VodUrlAdapter;
import com.wanlong.iptv.utils.ApkVersion;

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
    @BindView(R.id.ll_movie_detail)
    LinearLayout mLlMovieDetail;
    @BindView(R.id.recycler_movie_urls)
    RecyclerView mRecyclerMovieUrls;
    @BindView(R.id.ll_movie_urls)
    LinearLayout mLlMovieUrls;

    @Override
    protected int getContentResId() {
        return R.layout.activity_vod_detail;
    }

    private Intent intent;
    private String url_header;
    private String name;
    private String[] urls;
    private String vod_pic_url;
    private String total_sets;
    private String current_sets;
    private String vod_release_time;
    private String vod_scores;
    private String vod_category;
    private String vod_actor;
    private String vod_detail;

    @Override
    protected void initView() {
        intent = getIntent();
        url_header = intent.getStringExtra("url_header");
        urls = intent.getStringArrayExtra("urls");
        name = intent.getStringExtra("vod_name");
        total_sets = intent.getStringExtra("total_sets");
        current_sets = intent.getStringExtra("current_sets");
        vod_scores = intent.getStringExtra("vod_scores");
        vod_release_time = intent.getStringExtra("vod_release_time");
        vod_category = intent.getStringExtra("vod_category");
        vod_actor = intent.getStringExtra("vod_actor");
        vod_detail = intent.getStringExtra("vod_detail");
        vod_pic_url = intent.getStringExtra("vod_pic_url");
        if (urls != null && urls.length > 1) {
            mLlMovieUrls.setVisibility(View.VISIBLE);
            mTextMovieDetailPlay.setVisibility(View.GONE);
            mTextMoviePeopleDetail.setMaxLines(4);
            mTextMovieDescriptionDetail.setMaxLines(6);
        } else {
            mLlMovieUrls.setVisibility(View.GONE);
        }
        if (ApkVersion.CURRENT_VERSION == ApkVersion.PRISON_VERSION) {
            mTextMovieTimeDetail.setVisibility(View.GONE);
            mTextMovieCountDetail.setVisibility(View.GONE);
            mTextMovieTypeDetail.setVisibility(View.GONE);
            mTextMoviePeopleDetail.setVisibility(View.GONE);
            mTextMovieDescriptionDetail.setVisibility(View.GONE);
        }else {
            mTextMovieTimeDetail.setText(getString(R.string.time) + "：" + vod_release_time);
            mTextMovieCountDetail.setText(vod_scores);
            mTextMovieTypeDetail.setText(getString(R.string.category) + "：" + vod_category);
            mTextMoviePeopleDetail.setText(getString(R.string.actor) + "：" + vod_actor);
            mTextMovieDescriptionDetail.setText(getString(R.string.synopsis) + "：" + vod_detail);
        }
        if (total_sets.equals("1")) {
            mTextMovieNameDetail.setText(name);
        } else {
            mTextMovieNameDetail.setText(name + "(" + getString(R.string.updated)
                    + " " + current_sets + "/" + getString(R.string.total) + " " + total_sets + ")");
        }
        if (vod_pic_url == null || vod_pic_url.equals("")) {
            GlideApp.with(this)
                    .load(R.drawable.img_bg_color)
                    .centerCrop()
                    .into(mImgMovieDetail);
        } else {
            GlideApp.with(this)
                    .load(vod_pic_url)
                    .error(R.drawable.img_bg_color)
                    .centerCrop()
                    .into(mImgMovieDetail);
        }
    }

    private VodUrlAdapter mVodUrlAdapter;

    @Override
    protected void initData() {
//        setPresenter(new VodDetailPresenter(this));
//        getPresenter().loadVodDetailData("");
        //点播节目列表
        GridLayoutManager autoGridLayoutManager = new GridLayoutManager(this, 10);
        mRecyclerMovieUrls.setLayoutManager(autoGridLayoutManager);
        mVodUrlAdapter = new VodUrlAdapter(this);
        mRecyclerMovieUrls.setAdapter(mVodUrlAdapter);
        mVodUrlAdapter.setData(urls);
        listener();
    }

    private void listener() {
        mVodUrlAdapter.setOnItemClickListener(new VodUrlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(VodDetailActivity.this, VodPlayActivity.class);
                if (urls != null && urls.length > 0) {
                    intent.putExtra("url", url_header + urls[position]);
                } else {
                    intent.putExtra("url", url_header);
                }
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.text_movie_detail_play)
    public void onViewClicked() {
        Intent intent = new Intent(VodDetailActivity.this, VodPlayActivity.class);
        if (urls != null && urls.length > 0) {
            intent.putExtra("url", url_header + urls[0]);
        } else {
            intent.putExtra("url", url_header);
        }
        intent.putExtra("name", name);
        startActivity(intent);
    }

    @Override
    public void loadVodDetailFailed() {
//        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }
}
