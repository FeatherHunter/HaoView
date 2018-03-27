package com.hao.haoview;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MainActivity extends Activity {
    ArrayList<Integer> mDataList = new ArrayList<>();
    HaoRecyclerView mRecyclerView;
    RecyclerViewAdpater mAdapter;
    LinearLayoutManager mLinearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setPageMargin(10)
                     .setOtherPageVisibleWidth(50)
                     .setSnapHelper(HaoRecyclerView.LinearSnapHelper)
                     .hasBlurBackground(true)
                     .setAdapter(mAdapter = new RecyclerViewAdpater<Integer>(this, mDataList));


//        // 背景高斯模糊 & 淡入淡出
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    setBlurImage();
//                }
//            }
//        });
//        setBlurImage();
    }

    private Map<String, Drawable> mTSDraCacheMap = new HashMap<>();
    private static final String KEY_PRE_DRAW = "key_pre_draw";
    public void setBlurImage() {
        RecyclerViewAdpater adapter = (RecyclerViewAdpater) mRecyclerView.getAdapter();

        if (adapter == null || mRecyclerView == null) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                //1. 给背景添加图片
                SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mRecyclerView.setBackground(resource);
                    }
                };
                //2. 虚化效果
                Glide.with(MainActivity.this).load((mDataList.get(mRecyclerView.getPosition())))
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                        .into(simpleTarget);
//                // 将该Drawable图片转为Bitmap
//                Drawable drawable = mDataList.get(mRecyclerView.getPosition());
//                BitmapDrawable bd = (BitmapDrawable) drawable;
//                Bitmap resBmp = bd.getBitmap();
//                // 将该Bitmap高斯模糊后返回到resBlurBmp
//                Bitmap resBlurBmp = BlurBitmapUtil.blurBitmap(mRecyclerView.getContext(), resBmp, 15f);
//                // 再将resBlurBmp转为Drawable
//                Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
//                // 获取前一页的Drawable
//                Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);
//
//                /* 以下为淡入淡出效果 */
//                Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
//                TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
//                mRecyclerView.setBackgroundDrawable(transitionDrawable);
//                transitionDrawable.startTransition(500);
//
//                // 存入到cache中
//                mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
            }
        });
    }

    private void initData() {
        mDataList.add(R.drawable.assassins_creed);
        mDataList.add(R.drawable.watch_dog1);
        mDataList.add(R.drawable.lol2);
        mDataList.add(R.drawable.diablo1);
        mDataList.add(R.drawable.lol1);
        mDataList.add(R.drawable.pubg);
        mDataList.add(R.drawable.wow1);
        mDataList.add(R.drawable.wow3);
        mDataList.add(R.drawable.batman);
        mDataList.add(R.drawable.assassins_creed2);
        mDataList.add(R.drawable.call_of_duty);
        mDataList.add(R.drawable.wow2);
    }
}
