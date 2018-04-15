package com.hao.haoview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.hao.haoview.LayoutManager.HaoPagerSnapHelper;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    ArrayList<String> mDataList = new ArrayList<>();
    HaoRecyclerView mRecyclerView;
    RecyclerViewAdpater mAdapter;
    LinearLayoutManager mLinearLayoutManager;

    CarouselRecyclerView mCarouselRecyclerView;

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
//        mRecyclerView = findViewById(R.id.recycler_view);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        mRecyclerView.setPageMargin(0)
//                .setOtherPageVisibleWidth(0)
//                .setSnapHelper(HaoRecyclerView.PagerSnapHelper)
//                .hasBlurBackground(false)
//                .setAdapter(mAdapter = new RecyclerViewAdpater<String>(this, mDataList));
//        mAdapter.setLoop(true);
//        mRecyclerView.startLoop(2, TimeUnit.SECONDS);
//        mRecyclerView.setDotPosition(HaoRecyclerView.DOT_BOTTOM_CENTER);
//        mRecyclerView.setDotRadiusDp(3.5f);

//        RecyclerView recyclerView = findViewById(R.id.recycler_view_test);
//        CarouselLayoutManager carouselLayoutManager = new CarouselLayoutManager(this, -100, ViewPagerLayoutManager.HORIZONTAL);
//        carouselLayoutManager.setInfinite(true);
//        carouselLayoutManager.setMoveSpeed(1f);
//        carouselLayoutManager.setMinScale(0.5f);

//        recyclerView.setLayoutManager(carouselLayoutManager);
//        recyclerView.setLayoutManager(new HaoLayoutManager(this, HaoLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(new RecyclerViewAdpater<Integer>(this, mDataList));
//        new HaoPagerSnapHelper().attachToRecyclerView(recyclerView);

        mCarouselRecyclerView = findViewById(R.id.carousel_recycler_view);
        mCarouselRecyclerView.setAdapter(new RecyclerViewAdpater(this, mDataList));
        (new HaoPagerSnapHelper()).attachToRecyclerView(mCarouselRecyclerView);
        mCarouselRecyclerView.startLoop(1, TimeUnit.SECONDS);
        mCarouselRecyclerView.requestLayout();
    }

    private void initData() {
        mDataList.add("https://images.alphacoders.com/766/thumb-1920-76631.jpg");
        mDataList.add("https://images.alphacoders.com/581/thumb-1920-581358.jpg");
        mDataList.add("https://images6.alphacoders.com/581/thumb-1920-581360.jpg");
        mDataList.add("https://images4.alphacoders.com/467/thumb-1920-4674.jpg");
        mDataList.add("https://images4.alphacoders.com/206/thumb-1920-20658.jpg");
        mDataList.add("https://images4.alphacoders.com/250/thumb-1920-25066.jpg");
        mDataList.add("https://images.alphacoders.com/309/thumb-1920-30923.jpg");
        mDataList.add("https://images3.alphacoders.com/312/thumb-1920-31277.jpg");
        mDataList.add("https://images.alphacoders.com/428/thumb-1920-42873.jpg");
//        mDataList.add(R.drawable.assassins_creed);
//        mDataList.add(R.drawable.watch_dog1);
//        mDataList.add(R.drawable.lol2);
//        mDataList.add(R.drawable.diablo1);
//        mDataList.add(R.drawable.lol1);
//        mDataList.add(R.drawable.pubg);
//        mDataList.add(R.drawable.wow1);
//        mDataList.add(R.drawable.wow3);
//        mDataList.add(R.drawable.batman);
//        mDataList.add(R.drawable.assassins_creed2);
//        mDataList.add(R.drawable.call_of_duty);
//        mDataList.add(R.drawable.wow2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCarouselRecyclerView.stopLoop();
    }
}
