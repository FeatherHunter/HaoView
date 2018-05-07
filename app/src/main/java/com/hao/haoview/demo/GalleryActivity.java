package com.hao.haoview.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hao.haoview.R;
import com.hao.haoview.RecyclerView.LayoutManager.CarouselLayoutManager;
import com.hao.haoview.RecyclerView.LayoutManager.HaoPagerSnapHelper;
import com.hao.haoview.RecyclerView.LayoutManager.ViewPagerLayoutManager;
import com.hao.haoview.RecyclerView.widget.CarouselRecyclerView;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    CarouselRecyclerView mRecyclerView;
    ArrayList<String> mImgUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //隐藏标题栏,有效
        getSupportActionBar().hide();

        initData();

        mRecyclerView = findViewById(R.id.gallert_activity_recyclerview);
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(this, 0, ViewPagerLayoutManager.HORIZONTAL);
        layoutManager.setMinScale(0.8f);
        layoutManager.setInfinite(true);
        layoutManager.setOtherItemVisibleProportion(0.15f);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new GalleryAdapter(this, mImgUrls));


//        // RecyclerView背景的高斯模糊
//        String url = mImgUrls.get(0);
//        //1. 给背景添加图片
//        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                mRecyclerView.setBackground(resource);
//            }
//        };
//        //2. 虚化效果
//        Glide.with(GalleryActivity.this).load(url)
//                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
//                .into(simpleTarget);
//
//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState == RecyclerView.SCROLL_STATE_IDLE){
//                // RecyclerView背景的高斯模糊
//                String url = mImgUrls.get(mRecyclerView.getCurItemPosition());
//                //1. 给背景添加图片
//                SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        mRecyclerView.setBackground(resource);
//                    }
//                };
//                //2. 虚化效果
//                Glide.with(GalleryActivity.this).load(url)
//                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
//                        .into(simpleTarget);
//                }
//            }
//        });
//        mRecyclerView.setBackgroundSetter(new CarouselRecyclerView.BackgroundSetter() {
//            @Override
//            public void setBackground(int position) {
//                // RecyclerView背景的高斯模糊
//                String url = mImgUrls.get(position);
//                //1. 给背景添加图片
//                SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        mRecyclerView.setBackground(resource);
//                    }
//                };
//                //2. 虚化效果
//                Glide.with(GalleryActivity.this).load(url)
//                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
//                        .into(simpleTarget);
//            }
//        });
        new HaoPagerSnapHelper().attachToRecyclerView(mRecyclerView);
    }

    public void initData() {
        mImgUrls.add("http://old.bz55.com/uploads/allimg/140925/138-140925115333.jpg");
        mImgUrls.add("http://bizhi.bcoderss.com/wp-content/uploads/2017/11/20171130_084948.jpg");
        mImgUrls.add("http://www.lzshuli.com/game_images/110438130.jpeg");
        mImgUrls.add("http://www.lzshuli.com/game_images/110438132.jpeg");
        mImgUrls.add("https://i0download.pchome.net/t_600x1024/g1/M00/11/14/ooYBAFYWKQ2IPa1wAAHWY8yPBrgAACuFgFoeawAAdZ7486.jpg");
    }
}
