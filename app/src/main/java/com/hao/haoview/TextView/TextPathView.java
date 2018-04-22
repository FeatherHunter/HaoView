package com.hao.haoview.TextView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.Random;

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

    /**
     *
     * @return
     */
    Decorate mDecorate;

    /**
     * 是否异步
     */
    boolean isAsynced = false;

    public boolean isAsynced() {
        return isAsynced;
    }

    public void setAsynced(boolean asynced) {
        isAsynced = asynced;
    }

    public void setDecorate(Decorate decorate) {
        mDecorate = decorate;
    }

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
        if(isAsynced){
            //2. 配置好pathmeasure
            mPathMeasure.setPath(mSrcPath, false);
            //3.
            mDstPath.reset();
            Path tempPath = new Path();
            while (true) {
                tempPath.reset();
                //1. 该段Segement应该停止的距离
                float stopDistance = mPathMeasure.getLength() * mProgress;
                //2. 将该段(0~stop)的片段路径存入到tempPath中
                mPathMeasure.getSegment(0, stopDistance, tempPath, true);
                //3. 存入到总目标路径中
                mDstPath.addPath(tempPath);
                /**
                 * 4. 绘制装饰品
                 */
                float[] pos = new float[2];
                float[] tan = new float[2];
                //返回当前片段的(x,y点)和正切
                mPathMeasure.getPosTan(stopDistance, pos, tan);
                //绘制装饰品
                if(mDecorate != null && mProgress > 0 && mProgress < 1f){
                    mDecorate.drawDecoratePath(canvas, pos, tan, mPaint);
                }
                //5. 不存在下个片段就退出
                if (!mPathMeasure.nextContour()) {
                    break;
                }
            }
            //6. 绘制整个路径
            mPaint.reset();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setColor(getCurrentTextColor());
            mPaint.setAntiAlias(true);
            canvas.drawPath(mDstPath, mPaint);
        }else{
            //1. 结束的长度
            float stopDistance = mLengthSum * mProgress;
            //2. 配置好pathmeasure
            mPathMeasure.setPath(mSrcPath, false);
            //3.
            mDstPath.reset();
            while (stopDistance > mPathMeasure.getLength()) {
                stopDistance -= mPathMeasure.getLength();
                mPathMeasure.getSegment(0, mPathMeasure.getLength(), mDstPath, true);
                if (!mPathMeasure.nextContour()) {
                    break;
                }
            }
            mPathMeasure.getSegment(0, stopDistance, mDstPath, true);
            float[] pos = new float[2];
            float[] tan = new float[2];
            //返回当前片段的(x,y点)和正切
            mPathMeasure.getPosTan(stopDistance, pos, tan);

            if(mDecorate != null && mProgress > 0 && mProgress < 1f){
                mDecorate.drawDecoratePath(canvas, pos, tan, mPaint);
            }

            //4. 绘制
            mPaint.reset();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setColor(getCurrentTextColor());
            mPaint.setAntiAlias(true);
            canvas.drawPath(mDstPath, mPaint);
        }
    }

    public interface Decorate{
        public void drawDecoratePath(Canvas canvas, float[] pos, float tan[], Paint paint);
    }

    public static class ArrowDecorate implements Decorate {
        float angle = (float) (Math.PI / 4);
        float radius = 20;
        @Override
        public void drawDecoratePath(Canvas canvas, float[] pos, float[] tan, Paint paint) {
            float x = pos[0];
            float y = pos[1];

            float degree = (float)(Math.atan2(tan[1], tan[0]));

            float x1 = (float) (radius * Math.cos(degree + angle));
            float y1 = (float) (radius * Math.sin(degree + angle));
            float x2 = (float) (radius * Math.cos(degree - angle));
            float y2 = (float) (radius * Math.sin(degree - angle));

            Path decoratePath = new Path();
            decoratePath.moveTo(x, y);
            decoratePath.lineTo(x - x1, y - y1);
            decoratePath.moveTo(x, y);
            decoratePath.lineTo(x - x2, y - y2);
            canvas.drawPath(decoratePath, paint);
        }
    }

    public static class FireDecorate implements Decorate {
        float angle = (float) (Math.PI / 4);
        float radius = 15;
        int count = 2;
        Random mRandom = new Random();
        @Override
        public void drawDecoratePath(Canvas canvas, float[] pos, float[] tan, Paint paint) {
            float x = pos[0];
            float y = pos[1];

            float degree = (float)(Math.atan2(tan[1], tan[0]));

            for (int i = 0; i < count; i++) {
                radius = (float) (mRandom.nextFloat() * 40);
                angle = (float) (mRandom.nextFloat() * (Math.PI / 4));

                float x1 = (float) (radius * Math.cos(degree + angle));
                float y1 = (float) (radius * Math.sin(degree + angle));
                float x2 = (float) (radius * Math.cos(degree - angle));
                float y2 = (float) (radius * Math.sin(degree - angle));

                Path decoratePath = new Path();

                decoratePath.moveTo(x, y);
                decoratePath.lineTo(x - x1, y - y1);
                decoratePath.moveTo(x, y);
                decoratePath.lineTo(x - x2, y - y2);

                float drawX = mRandom.nextFloat() * (radius / 2);
                float notDrawX = mRandom.nextFloat() * (radius / 2);

                DashPathEffect dashPathEffect = new DashPathEffect(
                        new float[]{drawX, notDrawX}, //必须为偶数，奇数位的值表示画几个像素，偶数位的值表示空白几个像素(画20空5，画5空10)
                        0); //整个虚线的偏移值
                paint.setPathEffect(dashPathEffect);
                canvas.drawPath(decoratePath, paint);
            }
        }
    }

    public static class CircleDecorate implements Decorate {
        float radius = 8;
        @Override
        public void drawDecoratePath(Canvas canvas, float[] pos, float[] tan, Paint paint) {
            float x = pos[0];
            float y = pos[1];

            canvas.drawCircle(x + tan[0] * radius, y + tan[1] * radius, radius, paint);
        }
    }

    public static class PenDecorate implements Decorate {
        @Override
        public void drawDecoratePath(Canvas canvas, float[] pos, float[] tan, Paint paint) {
            float x = pos[0];
            float y = pos[1];

            canvas.save();
            canvas.rotate(30, x, y);
            Path path = new Path();
            path.moveTo(x, y);
            path.lineTo(x - 30, y - 30);
            path.lineTo(x + 30, y - 30);
            path.lineTo(x, y);
            canvas.drawPath(path, paint);
            canvas.drawRect(x - 30, y - 30 - 60, x + 30, y - 30, paint);
            canvas.restore();
        }
    }

    public void startAnim() {
        float start = 0;
        float end = 1f;
        if(isTraverse){
            start = 1f;
            end = 0f;
        }
        ObjectAnimator objectAnimator
                = ObjectAnimator.ofFloat(TextPathView.this, "Progress",
                start, end);
        objectAnimator.setDuration(mDuration);
        objectAnimator.start();
    }

    boolean isTraverse = false;

    public boolean isTraverse() {
        return isTraverse;
    }

    public void setTraverse(boolean traverse) {
        isTraverse = traverse;
    }
}

