package com.hao.haoview.Button;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by mr on 4/5/2018.
 */

public class HaoToggleButton extends View{

    private float progress = 100;
    private boolean isHorizontal = true;
    private int mPadding = 3;
    private int mRadius = 0; //圆弧半径
    private int mLeft = 0;
    private int mTop = 0;
    private int mBackColor = Color.parseColor("#42bd41");
    private int mBackUnColor = Color.parseColor("#bdbdbd");
    private boolean isChecked = true;

    public HaoToggleButton(Context context) {
        super(context);
    }

    public HaoToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HaoToggleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HaoToggleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setBackColor(int backColor) {
        mBackColor = backColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if(height > width){
            isHorizontal = false;
            mRadius = width / 2; //半径为宽度的一半
        }else{
            isHorizontal = true;
            mRadius = height / 2; //半径为高度的一半
        }

        Paint paint = new Paint();
        if(progress >= 50){
            paint.setColor(mBackColor);
        }else{
            paint.setColor(mBackUnColor);
        }
        canvas.drawRoundRect(mLeft, mTop, width, height, mRadius, mRadius, paint);

        float ratio =  progress / 100;

        int centerDistanceX = (int) (((width - 2 * mPadding) * (1f - ratio)) / 2);
        int centerDistanceY = (int) (((height - 2 * mPadding) * (1f - ratio)) / 2);

        int centerX = width / 2;
        int centerY = height / 2;
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(centerX - centerDistanceX, centerY - centerDistanceY,
                centerX + centerDistanceX, centerY + centerDistanceY
                , centerDistanceX / 2, centerDistanceX / 2, paint);

        int circleRadius = mRadius - mPadding; //圆的半径为=背景半圆的半径-padding
        int totalDistance = width - mRadius - mRadius; //圆心移动的长短
        int curDistance = (int)(totalDistance * ratio); //当前处于的距离
        int cirLeft = mLeft + mRadius + curDistance;

        paint.setColor(Color.WHITE);
        canvas.drawCircle(cirLeft, mTop + mRadius, circleRadius, paint);
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mPadding);
        if(progress >= 50){
            paint.setColor(mBackColor);
        }else{
            paint.setColor(mBackUnColor);
        }
        canvas.drawCircle(cirLeft, mTop + mRadius, circleRadius, paint);


        Log.d("HaoToggleButton", "progress:" + progress);
        Log.d("HaoToggleButton", "cirLeft:" + cirLeft);

    }

    // 创建 getter 方法
    public float getProgress() {
        return progress;
    }
    // 创建 setter 方法
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void startAnimOff(){
        // 创建 ObjectAnimator 对象
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress", 100, 0);
        animator.setDuration(100);
        // 执行动画
        animator.start();
    }

    public void startAnimOn(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(HaoToggleButton.this, "progress", 0, 100);
        objectAnimator.setDuration(100);
        objectAnimator.start();
    }

    private OnToggleSelectListener mSelectListener;

    public interface OnToggleSelectListener{
        public void turnOn();
        public void turnOff();
    }

    public void setOnToggleSelectListener(final OnToggleSelectListener selectListener) {
        mSelectListener = selectListener;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked){
                    startAnimOff();
                    selectListener.turnOff();
                }else{
                    startAnimOn();
                    selectListener.turnOn();
                }
                isChecked = !isChecked;
            }
        });
    }

}
