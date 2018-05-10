package com.hao.haoview.demo;

import android.graphics.drawable.Animatable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hao.haoview.R;

public class SVGActivity extends AppCompatActivity {

    boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);

        final ImageView imageView = (ImageView) findViewById(R.id.svg_activity_animatedvectordrawable_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked == false){
                    imageView.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.menu_arrow_check_vector_animated));
                    ((Animatable)imageView.getDrawable()).start();
                    isChecked = true;
                }else{
                    imageView.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.menu_arrow_uncheck_vector_animated));
                    ((Animatable)imageView.getDrawable()).start();
                    isChecked = false;
                }
            }
        });
    }
}
