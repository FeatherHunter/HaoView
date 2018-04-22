package com.hao.haoview.demo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hao.haoview.R;
import com.hao.haoview.RecyclerView.LayoutManager.CarouselLayoutManager;
import com.hao.haoview.RecyclerView.LayoutManager.HaoPagerSnapHelper;
import com.hao.haoview.RecyclerView.LayoutManager.ViewPagerLayoutManager;
import com.hao.haoview.RecyclerView.widget.CarouselRecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CarouselActivity extends AppCompatActivity {

    CarouselRecyclerView mRecyclerView;
    ArrayList<String> mImgUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        //隐藏标题栏,有效
        getSupportActionBar().hide();

        initData();

        mRecyclerView = findViewById(R.id.carousel_activity_rv);
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(this, 20, ViewPagerLayoutManager.HORIZONTAL);
        layoutManager.setMinScale(1f);
        layoutManager.setInfinite(true);
        layoutManager.setOtherItemVisibleProportion(0.05f);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setDotPosition(CarouselRecyclerView.DOT_BOTTOM_RIGHT);
        mRecyclerView.setDotOtherColor(Color.parseColor("#bcbcbcbc"));
        mRecyclerView.setDotIntervalDp(3f);
        mRecyclerView.setDotRadiusDp(4f);
        mRecyclerView.setDotMarginDp(8f);
        mRecyclerView.setAdapter(new GalleryAdapter(this, mImgUrls));
        new HaoPagerSnapHelper().attachToRecyclerView(mRecyclerView);
        mRecyclerView.startLoop(3, TimeUnit.SECONDS);
    }

    public void initData() {
        mImgUrls.add("http://webpiaoliang.com/uploads/allimg/c171015/150P60094V0-12F2.jpg");
        mImgUrls.add("http://img.pc841.com/2017/1214/20171214041431577.jpg");
        mImgUrls.add("http://i2.17173cdn.com/i7mz64/YWxqaGBf/tu17173com/20161128/GWkVSVblcvazpky.jpg");
        mImgUrls.add("https://desk-fd.zol-img.com.cn/t_s1680x1050c5/g5/M00/01/0F/ChMkJ1bKwuOIMR3JAAlxoEW-xGAAALGuwEXEyAACXG4337.jpg");
        mImgUrls.add("https://i0download.pchome.net/t_1600x1200/g1/M00/0D/06/ooYBAFSRVYqIYYdEAAio3UayJDMAACKdgOWBu4ACKj1727.jpg");

        mImgUrls.add("http://pic1.win4000.com/wallpaper/0/57cfb0594b0b4.jpg");
        mImgUrls.add("https://i0download.pchome.net/t_1280x1024/g1/M00/04/16/ooYBAFHC3eOIUmwGAAjf6gCBtacAAAttAE8ulYACOAC477.jpg");

    }
}
