package com.hao.haoview.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hao.haoview.R;
import com.hao.haoview.RecyclerView.widget.CarouselRecyclerView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    ArrayList<String> mDataList = new ArrayList<>();

    CarouselRecyclerView mCarouselRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button textButton = findViewById(R.id.text_path_btn);
        Button galleryButton = findViewById(R.id.gallery_rv_btn);
        Button carouselButton = findViewById(R.id.carousel_rv_btn);

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TextPathActivity.class));
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });
        carouselButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CarouselActivity.class));
            }
        });

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

//        initData();

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

//        mCarouselRecyclerView = findViewById(R.id.carousel_recycler_view);
//        mCarouselRecyclerView.setAdapter(new RecyclerViewAdpater(this, mDataList));
//        (new HaoPagerSnapHelper()).attachToRecyclerView(mCarouselRecyclerView);
//        mCarouselRecyclerView.startLoop(1, TimeUnit.SECONDS);
//        mCarouselRecyclerView.requestLayout();
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
    }
}
