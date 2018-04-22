package com.hao.haoview.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hao.haoview.R;
import com.hao.haoview.TextView.TextPathView;

public class TextPathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_path);

        TextPathView textPathView1 = findViewById(R.id.text_path_1_txt);
        textPathView1.setText("顾文慧");
        textPathView1.setDecorate(new TextPathView.FireDecorate());
        textPathView1.setStrokeWidth(5);
        textPathView1.setDuration(8000);
        textPathView1.startAnim();

        TextPathView textPathView2 = findViewById(R.id.text_path_2_txt);
        textPathView2.setText("王辰浩");
        textPathView2.setDecorate(new TextPathView.CircleDecorate());
        textPathView2.setStrokeWidth(5);
        textPathView2.setDuration(6000);
        textPathView2.startAnim();

        TextPathView textPathAsyncView = findViewById(R.id.text_path_async_txt);
        textPathAsyncView.setText("王辰浩");
        textPathAsyncView.setDecorate(new TextPathView.CircleDecorate());
        textPathAsyncView.setStrokeWidth(5);
        textPathAsyncView.setDuration(3000);
        textPathAsyncView.setAsynced(true);
        textPathAsyncView.startAnim();

        TextPathView textPathTraverseView = findViewById(R.id.text_path_traverse_txt);
        textPathTraverseView.setText("辰雯");
        textPathTraverseView.setDecorate(new TextPathView.FireDecorate());
        textPathTraverseView.setStrokeWidth(5);
        textPathTraverseView.setDuration(6000);
        textPathTraverseView.setTraverse(true);
        textPathTraverseView.startAnim();

        TextPathView textPathTraverseAsyncView = findViewById(R.id.text_path_traverse_async_txt);
        textPathTraverseAsyncView.setText("Feather");
        textPathTraverseAsyncView.setDecorate(new TextPathView.CircleDecorate());
        textPathTraverseAsyncView.setStrokeWidth(5);
        textPathTraverseAsyncView.setDuration(4000);
        textPathTraverseAsyncView.setAsynced(true);
        textPathTraverseAsyncView.setTraverse(true);
        textPathTraverseAsyncView.startAnim();

        TextPathView textPathPenView = findViewById(R.id.text_path_pen_txt);
        textPathPenView.setText("abcdefg");
        textPathPenView.setDecorate(new TextPathView.PenDecorate());
        textPathPenView.setStrokeWidth(5);
        textPathPenView.setDuration(10000);
        textPathPenView.startAnim();
    }
}
