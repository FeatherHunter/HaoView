package com.hao.haoview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    ArrayList<Drawable> mDataList = new ArrayList<>();
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
                     .setAdapter(mAdapter = new RecyclerViewAdpater<Drawable>(this, mDataList));


        // 背景高斯模糊 & 淡入淡出
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    setBlurImage();
                }
            }
        });
        setBlurImage();
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
                // 将该Drawable图片转为Bitmap
                Drawable drawable = mDataList.get(mRecyclerView.getPosition());
                BitmapDrawable bd = (BitmapDrawable) drawable;
                Bitmap resBmp = bd.getBitmap();
                // 将该Bitmap高斯模糊后返回到resBlurBmp
                Bitmap resBlurBmp = BlurBitmapUtil.blurBitmap(mRecyclerView.getContext(), resBmp, 15f);
                // 再将resBlurBmp转为Drawable
                Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
                // 获取前一页的Drawable
                Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);

                /* 以下为淡入淡出效果 */
                Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
                TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
                mRecyclerView.setBackgroundDrawable(transitionDrawable);
                transitionDrawable.startTransition(500);

                // 存入到cache中
                mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
            }
        });
    }

    private void initData() {
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.revenge1));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.revenge2));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.revenge3));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.revenge4));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.revenge5));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.revenge6));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty2));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty3));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty4));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty5));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty6));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty7));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty8));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty9));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty10));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty11));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty12));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.beauty13));
    }
}
