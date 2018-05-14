package com.wanlong.iptv.ui.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.wanlong.iptv.R;
import com.wanlong.iptv.mvp.VodDetailPresenter;
import com.wanlong.iptv.ui.adapter.VodUrlAdapter;

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
    private String url;
    private String name;
    private String[] urls;
    private String vod_pic_url;

    @Override
    protected void initView() {
        intent = getIntent();
        url = intent.getStringExtra("url_header");
        urls = intent.getStringArrayExtra("urls");
        if (urls.length > 1) {
            mLlMovieUrls.setVisibility(View.VISIBLE);
        } else {
            mLlMovieUrls.setVisibility(View.GONE);
        }
        name = intent.getStringExtra("vod_name");
//        mTextMovieCountDetail.setTextColor(Color.parseColor("#000000"));
//        mTextMovieCountDetail.setAlpha();
        String total_sets = intent.getStringExtra("total_sets");
        String current_sets = intent.getStringExtra("current_sets");
        if (total_sets.equals("1")) {
            mTextMovieNameDetail.setText(name);
        } else {
            mTextMovieNameDetail.setText(name + "(" + getString(R.string.updated)
                    + " " + current_sets + "/" + getString(R.string.total) + " " + total_sets + ")");
        }
        mTextMovieTimeDetail.setText(getString(R.string.time) + "：" + intent.getStringExtra("vod_release_time"));
        mTextMovieCountDetail.setText(intent.getStringExtra("vod_scores"));
        mTextMovieTypeDetail.setText(getString(R.string.category) + "：" + intent.getStringExtra("vod_category"));
        mTextMoviePeopleDetail.setText(getString(R.string.actor) + "：" + intent.getStringExtra("vod_actor"));
        mTextMovieDescriptionDetail.setText(getString(R.string.synopsis) + "：" + intent.getStringExtra("vod_detail"));
        vod_pic_url = intent.getStringExtra("vod_pic_url");
        if (vod_pic_url.equals("")) {
            Glide.with(this)
                    .load(getResources()
                            .getDrawable(R.drawable.sence))
                    .into(mImgMovieDetail);
        } else {
            Glide.with(this)
                    .load(vod_pic_url)
                    .into(mImgMovieDetail);
        }
//        Glide.with(this).load(vod_pic_url).into(mImgMovieDetail);
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
                if (urls.length > 0) {
                    intent.putExtra("url", url + urls[position]);
                } else {
                    intent.putExtra("url", url);
                }
//                intent.putExtra("url", url + urls[position]);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.text_movie_detail_play)
    public void onViewClicked() {
        Intent intent = new Intent(VodDetailActivity.this, VodPlayActivity.class);
        intent.putExtra("url", url + urls[0]);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    @Override
    public void loadVodDetailFailed() {
//        Toast.makeText(this, "请求数据失败", Toast.LENGTH_SHORT).show();
        Logger.d("请求直播数据失败");
    }
}
