package com.hao.haoview.TextView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by mr on 4/19/2018.
 */

public class TextPathView extends AppCompatTextView {
    /**
     * 最终绘制目标的路径
     */
    Path mDstPath = new Path();
    /**
     * Path辅助进行截断
     */
    PathMeasure mPathMeasure = new PathMeasure();
    /**
     * 绘制目标的画笔
     */
    Paint mPaint = new Paint();
    /**
     * 源文字路径
     */
    private Path mSrcPath = new Path();
    /**
     * 动画进度(0~1f)
     */
    private float mProgress = 0f;
    /**
     * 源文本路径的总长度
     */
    private float mLengthSum = 0;

    /**
     * 文字线条的宽度
     */
    private float mStrokeWidth = 1f;

    /**
     * 动画时长
     */
    private int mDuration = 2000;

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
    }

    public TextPathView(Context context) {
        super(context);
        initPath(getText().toString());
    }

    private void initPath(String text) {

        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        //1. 获取到文字路径,保存到path中
        paint.getTextPath(text, 0, text.length(), 0, paint.getTextSize(),
                mSrcPath);

        //2. 设置mPathMeasure
        mPathMeasure.setPath(mSrcPath, false);

        //3. 通过PathMeasure获取文字路径的总长度
        mLengthSum = mPathMeasure.getLength();
        while (mPathMeasure.nextContour()) {
            mLengthSum += mPathMeasure.getLength();
        }
    }
    public TextPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPath(getText().toString());
    }

    public TextPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPath(getText().toString());
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
        postInvalidate();
    }

    /**
     * 设置字符串(内部会进行动画的准备工作)
     * @param text
     */
    public void setText(String text) {
        setText(text, BufferType.NORMAL);
        initPath(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1. 结束的长度
        float stopDistance = mLengthSum * mProgress;
        //2. 配置好pathmeasure
        mPathMeasure.setPath(mSrcPath, false);
        //3.
        while (stopDistance > mPathMeasure.getLength()) {
            stopDistance -= mPathMeasure.getLength();
            mPathMeasure.getSegment(0, mPathMeasure.getLength(), mDstPath, true);
            if (!mPathMeasure.nextContour()) {
                break;
            }
        }
        mPathMeasure.getSegment(0, stopDistance, mDstPath, true);
        //4. 绘制
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(getCurrentTextColor());
        mPaint.setAntiAlias(true);
        canvas.drawPath(mDstPath, mPaint);
    }

    public void startAnim() {
        ObjectAnimator objectAnimator
                = ObjectAnimator.ofFloat(TextPathView.this, "Progress",
                0, 1f);
        objectAnimator.setDuration(mDuration);
        objectAnimator.start();
    }
}

