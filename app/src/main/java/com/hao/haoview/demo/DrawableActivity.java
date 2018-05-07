package com.hao.haoview.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.shapes.*;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hao.haoview.R;

public class DrawableActivity extends AppCompatActivity {

    Button mTransitionButton;
    TransitionDrawable mTransitionDrawable;

    Button mRotateButton;
    RotateDrawable mRotateDrawable;

    ImageView mShapeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);

        mTransitionButton = findViewById(R.id.drawable_activity_transition_btn);
        mTransitionDrawable = (TransitionDrawable) mTransitionButton.getBackground();
        mTransitionButton.setOnClickListener(new View.OnClickListener() {
            boolean isFlag = true;

            @Override
            public void onClick(View v) {
                if (isFlag) {
                    mTransitionDrawable.startTransition(1000);
                } else {
                    mTransitionDrawable.reverseTransition(1000);
                }
                isFlag = !isFlag;
            }
        });

        View scaleView = findViewById(R.id.drawable_activity_scale_view);
        ScaleDrawable scaleDrawable = (ScaleDrawable) scaleView.getBackground();
        scaleDrawable.setLevel(1);

        View bitmapView = findViewById(R.id.drawable_activity_bitmap_view2);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) bitmapView.getBackground();
        bitmapDrawable.setTintMode(PorterDuff.Mode.SCREEN);

        ImageView imageView = findViewById(R.id.drawable_activity_clip_imgaeview);
        imageView.setImageLevel(10000);

        mRotateButton = findViewById(R.id.drawable_activity_rotate_btn);
        mRotateDrawable = (RotateDrawable) mRotateButton.getBackground();
        mRotateButton.setOnClickListener(new View.OnClickListener() {
            boolean isFlag = true;

            @Override
            public void onClick(View v) {
                if (isFlag) {
                    mRotateDrawable.setLevel(0);
                } else {
                    mRotateDrawable.setLevel(10000);
                }
                isFlag = !isFlag;
            }
        });

        ImageView animationImageView = findViewById(R.id.drawable_activity_animation_imageview);
        AnimationDrawable animationDrawable = (AnimationDrawable) animationImageView.getDrawable();
        animationDrawable.start();
        animationDrawable.stop();


        mShapeImageView = findViewById(R.id.drawable_activity_shapedrawable_imageview);

//        /**==============================
//         * 1. 创建椭圆Shape
//         * 2. 创建ShapeDrawable
//         * 3. 设置颜色等内容
//         * 4. 使用ShapeDrawable
//         *======================================*/
//        OvalShape ovalShape = new OvalShape();
//        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
//        shapeDrawable.getPaint().setColor(Color.BLUE);
//        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
//        shapeImageView.setBackground(shapeDrawable);
//
//        RectShape rectShape = new RectShape();
//        shapeDrawable.setShape(rectShape);
//
//        /**
//         * startAngle: 初始角度-x轴正方向为0
//         * sweepAngle：顺时针方向划过的度数
//         */
//        ArcShape arcShape = new ArcShape(0, 100);
//        shapeDrawable.setShape(arcShape);
//
//        // 外部矩形弧度-外部圆角矩阵的四个角弧度(两两一组)
//        float[] outerR = new float[]{8, 8, 8, 8, 8, 8, 8, 8};
//        // 内部矩形与外部矩形的距离
//        RectF inset = new RectF(100, 100, 50, 50);
//        // 内部矩形弧度
//        float[] innerR = new float[]{20, 20, 20, 20, 20, 20, 20, 20};
//
//        RoundRectShape roundRectShape = new RoundRectShape(outerR, inset, innerR);
//
//        shapeDrawable.setShape(roundRectShape);

//        Path path = new Path();
//        path.moveTo(50, 0);
//        path.lineTo(0, 50);
//        path.lineTo(50, 100);
//        path.lineTo(100, 50);
//        path.lineTo(50, 0);
//        PathShape pathShape = new PathShape(path, 100, 100);
//        final ShapeDrawable shapeDrawable = new ShapeDrawable(pathShape);

        PaintDrawable paintDrawable = new PaintDrawable(Color.GREEN);
        //1. 圆角, 所有角的半径相同。
        paintDrawable.setCornerRadius(30);
        //2. 为四个角的每一个指定半径。 对于每个角落，数组包含2个值[X_radius，Y_radius].
//        paintDrawable.setCornerRadii (float[] radii);
        mShapeImageView.setBackground(paintDrawable);

//        /**
//         * 已经设置过ShapDrawable基础上：
//         * 1. 获取Bitmap，并构造BitmapShader
//         * 2. 对BitmapShader用Matrix进行缩放
//         * 3. getShapeDrawable的Paint设置Shader
//         */
//        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.beauty1)).getBitmap();
//        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.REPEAT);
//        Matrix matrix = new Matrix();
//        //根据View控件的宽高进行缩放
//        matrix.preScale(100f / bitmap.getWidth(),
//                100f / bitmap.getHeight());//view:w=100,h=100
//        bitmapShader.setLocalMatrix(matrix);
//
//        shapeDrawable.getPaint().setShader(bitmapShader);

    }

}
