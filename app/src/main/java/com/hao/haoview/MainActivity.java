package com.hao.haoview;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

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
        mRecyclerView.setAdapter(mAdapter = new RecyclerViewAdpater<Drawable>(this, mDataList));
    }

    private void initData() {
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog1));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog2));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog3));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog4));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog5));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog6));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog7));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog10));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog4));
//        mDataList.add(ContextCompat.getDrawable(this, R.drawable.dog5));
    }
}
