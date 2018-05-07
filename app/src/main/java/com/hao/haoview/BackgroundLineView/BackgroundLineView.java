package com.hao.haoview.BackgroundLineView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;

/**
 *
 */
public class BackgroundLineView extends View {
    public static final Integer TYPE_TRAIN = 0;
    public static final Integer TYPE_PLANE = 1;
    ArrayList<Pair<Path, Integer>> mDatas = new ArrayList<>();

    int mProgress = 50;

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public BackgroundLineView(Context context) {
        super(context);
    }

    public BackgroundLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BackgroundLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPaths(ArrayList<Pair<Path, Integer>> datas){
        mDatas = datas;
    }

    PathMeasure mPathMeasure = new PathMeasure();
    Path mDstPath = new Path();
    int mPathMeasureSumLength = 0;
    Paint mPaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Pair<Path, Integer> item : mDatas) {
            Path path = item.first;
            Integer type = item.second;

            /**
             * 获得总长度
             */
            mPathMeasureSumLength = 0;
            mPathMeasure.setPath(path, false);
            do{
                mPathMeasureSumLength += mPathMeasure.getLength();
            }while(mPathMeasure.nextContour());

            /**
             * 通过比例获取目标Path
             */
            //1. 结束的长度
            float stopDistance = mPathMeasureSumLength * (mProgress/100f);
            //2. 配置好pathmeasure
            mPathMeasure.setPath(path, false);
            //3. 获取到目标Path
            mDstPath.reset();
            while (stopDistance > mPathMeasure.getLength()) {
                stopDistance -= mPathMeasure.getLength();
                mPathMeasure.getSegment(0, mPathMeasure.getLength(), mDstPath, true);
                if (!mPathMeasure.nextContour()) {
                    break;
                }
            }
            mPathMeasure.getSegment(0, stopDistance, mDstPath, true);

            //4. 绘制
            mPaint.reset();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(20);
            mPaint.setColor(Color.RED);
            mPaint.setAntiAlias(true);
            canvas.drawPath(mDstPath, mPaint);
        }

//        if(mProgress == 100){
//            ObjectAnimator animator = ObjectAnimator.ofFloat(BackgroundLineView.this, "alpha", 0f, 1f);
//            animator.start();
//        }
        Log.d("feather", "draw");
    }

    public void startAnimation(){
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", 0, 100);
        animator.setDuration(1000);
        animator.start();
    }
}
