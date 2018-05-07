package com.hao.haoview.demo;

import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;

import com.hao.haoview.BackgroundLineView.BackgroundLineView;
import com.hao.haoview.R;

import java.util.ArrayList;

public class BackgroundLineViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_line_view);

        BackgroundLineView backgroundLineView = findViewById(R.id.background_activity_bgline_view);

        Path path = new Path();
        path.moveTo(100, 0);
        path.lineTo(100, 1000);

        ArrayList<Pair<Path, Integer>> datas = new ArrayList<>();
        datas.add(0, new Pair<Path, Integer>(path, 1));

        backgroundLineView.setPaths(datas);
        backgroundLineView.startAnimation();
    }
}
