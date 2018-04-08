package com.hao.haoview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.hao.haoview.Button.HaoToggleButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    ArrayList<Integer> mDataList = new ArrayList<>();
    HaoRecyclerView mRecyclerView;
    RecyclerViewAdpater mAdapter;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        HaoToggleButton button = findViewById(R.id.button);
//        button.setOnToggleSelectListener(new HaoToggleButton.OnToggleSelectListener() {
//            @Override
//            public void turnOn() {
//                Toast.makeText(MainActivity.this, "已开启", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void turnOff() {
//                Toast.makeText(MainActivity.this, "已关闭", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        HaoToggleButton button2 = findViewById(R.id.button2);
//        button2.setBackColor(Color.parseColor("#f36c60"));
//        button2.setOnToggleSelectListener(new HaoToggleButton.OnToggleSelectListener() {
//            @Override
//            public void turnOn() {
//                Toast.makeText(MainActivity.this, "已开启", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void turnOff() {
//                Toast.makeText(MainActivity.this, "已关闭", Toast.LENGTH_SHORT).show();
//            }
//        });

        initData();
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mLinearLayoutManager.setItemPrefetchEnabled(true);
        mLinearLayoutManager.setInitialPrefetchItemCount(5);
        mRecyclerView.setPageMargin(0)
                .setOtherPageVisibleWidth(0)
                .setSnapHelper(HaoRecyclerView.PagerSnapHelper)
                .hasBlurBackground(false)
                .setAdapter(mAdapter = new RecyclerViewAdpater<Integer>(this, mDataList));
        mAdapter.setLoop(false);
        mRecyclerView.startLoop(2, TimeUnit.SECONDS);
        mRecyclerView.setDotPosition(HaoRecyclerView.DOT_BOTTOM_CENTER);
        mRecyclerView.setDotRadiusDp(3.5f);
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
