package com.hao.haoview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

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
