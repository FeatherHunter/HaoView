package com.hao.haoview.ademo;

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
        textPathView1.setText("Feather");
        textPathView1.setDecorate(new TextPathView.FireDecorate());
        textPathView1.setStrokeWidth(5);
        textPathView1.setDuration(10000);
        textPathView1.startAnim();

        TextPathView textPathView2 = findViewById(R.id.text_path_2_txt);
        textPathView2.setText("Feather");
        textPathView2.setDecorate(new TextPathView.CircleDecorate());
        textPathView2.setStrokeWidth(5);
        textPathView2.setDuration(10000);
        textPathView2.startAnim();
    }
}
