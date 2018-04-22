package com.hao.haoview.RecyclerView.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.hao.haoview.RecyclerView.LayoutManager.CarouselLayoutManager;
import com.hao.haoview.RecyclerView.LayoutManager.ViewPagerLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mr on 4/15/2018.
 */
public class CarouselRecyclerView extends RecyclerView {
    private CarouselLayoutManager mCarouselLayoutManager;
    private Context mContext;
    private boolean DEBUG = false;
    /**
     * 存放数据
     */
    private ArrayList<String> mBottomTexts = null;
    /**
     * 当前Item的位置
     */
    private int mCurItemPosition = 0;

    public int getCurItemPosition() {
        return mCurItemPosition;
    }

    /**
     *     默认为没有循环点
     */
    private int mDotPosition = DOT_NULL;
    private float mDotRadiusDp = 0; //循环点半径
    private float mDotIntervalDp = 0; //循环点间隔
    /**
     * 轮播图循环刷新的任务
     */
    private ScheduledFuture mScheduledFuture;
    /**
     * 1. mStartLooping true表示开始循环
     * 2. mPauseLooping true表示已经暂停(前提是已经开始循环)
     */
    private boolean mStartLooping = false;
    private boolean mPauseLooping = false;
    /**
     * 轮播图的时间间隔和单位
     */
    private long mDelay = 0;
    private TimeUnit mTimeUnit;
    /**
     * ===================================*
     * 1-是否有循环点，以及循环点的位置
     * ==================================
     */
    @IntDef({DOT_BOTTOM_LEFT, DOT_BOTTOM_RIGHT, DOT_BOTTOM_CENTER, DOT_NULL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DotDirection {
    }

    public static final int DOT_NULL = 0;
    public static final int DOT_BOTTOM_LEFT = 1;    // 底部左边
    public static final int DOT_BOTTOM_RIGHT = 2;   // 底部右边
    public static final int DOT_BOTTOM_CENTER = 3;  // 底部中间
    private static final String TAG = CarouselRecyclerView.class.getName();

    private int mDotSelectColor = Color.parseColor("#4fc3f7");
    private int mDotOtherColor = Color.WHITE;
    private float mDotMarginDp = 10f;

    public float getDotMarginDp() {
        return mDotMarginDp;
    }

    public void setDotMarginDp(float dotMarginDp) {
        mDotMarginDp = dotMarginDp;
    }

    public void setDotSelectColor(int dotSelectColor) {
        mDotSelectColor = dotSelectColor;
    }

    public void setDotOtherColor(int dotOtherColor) {
        mDotOtherColor = dotOtherColor;
    }

    // 9-是否开启高斯模糊
    private boolean isBlured = false; //默认关闭
    //设置是否使用背景模糊循环
    public CarouselRecyclerView setBlurBackground(boolean isBlured) {
        this.isBlured = isBlured;
        return this;
    }


    /**
     * 绘制底部文字内容
     */
    private boolean mHasBottomText = false;
    private float mBgHeightDp = 0;
    private int mBgColor = Color.parseColor("#77bcbcbc");
    private int mBottomTextColor = Color.parseColor("#ffffff");
    private float mBottomTextSizeDp = 10f;
    private float mBottomTextMarginDp = 1f;


    public void setBottomTextMarginDp(float bottomTextMarginDp) {
        mBottomTextMarginDp = bottomTextMarginDp;
    }

    public void setBottomTexts(ArrayList<String> bottomTexts) {
        mBottomTexts = bottomTexts;
    }

    public void setHasBottomText(boolean hasBottomText) {
        mHasBottomText = hasBottomText;
    }

    public void setBgHeightDp(float bgHeightDp) {
        mBgHeightDp = bgHeightDp;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public void setBottomTextColor(int bottomTextColor) {
        mBottomTextColor = bottomTextColor;
    }

    public void setBottomTextSizeDp(float bottomTextSizeDp) {
        mBottomTextSizeDp = bottomTextSizeDp;
    }

    /**
     * ===================================*
     * 周期线程池(Banner效果，定时切换Item)
     * ==================================
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "CarouselRecyclerView #" + mCount.getAndIncrement());
        }
    };
    public static final ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, sThreadFactory);

    public void init() {
        //设置循环布局
        mCarouselLayoutManager = new CarouselLayoutManager(mContext, 0, ViewPagerLayoutManager.HORIZONTAL);
        mCarouselLayoutManager.setInfinite(true);
        mCarouselLayoutManager.setMoveSpeed(1f);
        mCarouselLayoutManager.setMinScale(1f);
        this.setLayoutManager(mCarouselLayoutManager);
        //轮播点半径
        setDotRadiusDp(3f);
        //轮播点间隔
        setDotIntervalDp(2f);
    }

    public void setDotPosition(@DotDirection int dotPosition) {
        this.mDotPosition = dotPosition;
    }

    public void setDotRadiusDp(float dotRadiusDp) {
        this.mDotRadiusDp = dotRadiusDp;
    }

    public CarouselRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CarouselRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    /**
     * =============================
     * 绘制循环点(在子View和前景之间)
     * =============================
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        // 获得转换后的px值
        float circleRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDotRadiusDp, mContext.getResources().getDisplayMetrics());
        float dotIntervalPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDotIntervalDp, mContext.getResources().getDisplayMetrics());
        float dotMarginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDotMarginDp, mContext.getResources().getDisplayMetrics());

        int itemCount = getLayoutManager().getItemCount();

        // 没有数据就Return
        if (itemCount <= 0) {
            return;
        }
        // RV宽高
        int width = CarouselRecyclerView.this.getWidth();
        int height = CarouselRecyclerView.this.getHeight();

        mCurItemPosition = ((ViewPagerLayoutManager) getLayoutManager()).getCurrentPosition();


        Paint paint = new Paint();

        paint.setColor(mBgColor);
        if(mHasBottomText && mBottomTexts != null && mBottomTexts.size() > 0){
            float bgHeightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBgHeightDp, mContext.getResources().getDisplayMetrics());

            float bottomTextSize =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBottomTextSizeDp, mContext.getResources().getDisplayMetrics());

            float textMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBottomTextMarginDp, mContext.getResources().getDisplayMetrics());

            canvas.drawRect(0, height - bgHeightPx, width, height, paint);
            paint.setColor(mBottomTextColor);
            paint.setTextSize(bottomTextSize);
            paint.setAntiAlias(true);

            String text = mBottomTexts.get(mCurItemPosition % mBottomTexts.size());
            canvas.drawText(text, 0 + textMargin, height - textMargin, paint);

            if(DEBUG){
                Log.d(TAG, "draw text");
            }
        }else{
            if(DEBUG){
                Log.d(TAG, "Dont draw text");
            }
        }

        switch (mDotPosition) {
            case DOT_NULL:
                break;
            case DOT_BOTTOM_CENTER:

                float cy = height - dotMarginPx;

                int count = itemCount;
                float leftDistance = (count / 2 + (count % 2) * 0.5f) * (dotIntervalPx + circleRadiusPx);
                float oneDistance = (2 * circleRadiusPx) + dotIntervalPx;

                float startCx = width / 2 - leftDistance;

                int position = (mCurItemPosition + itemCount) % itemCount;

                for (int i = 0; i < itemCount; i++) {
                    paint.reset();
                    //需要特殊处理
                    if (i == position) {
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(Color.parseColor("#4fc3f7"));
                        paint.setAntiAlias(true);
                        canvas.drawCircle(startCx, cy, circleRadiusPx, paint);
                    } else {
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(Color.WHITE);
                        paint.setAntiAlias(true);
                        canvas.drawCircle(startCx, cy, circleRadiusPx, paint);
                    }
                    startCx += oneDistance;
                }
                break;
            case DOT_BOTTOM_LEFT:
                break;
            case DOT_BOTTOM_RIGHT:

                cy = height - dotMarginPx;

                count = itemCount;

                leftDistance = (count * circleRadiusPx * 2) + (count - 1) * dotIntervalPx;
                oneDistance = (2 * circleRadiusPx) + dotIntervalPx;

                startCx = width  - leftDistance;

                position = (mCurItemPosition + itemCount) % itemCount;

                for (int i = 0; i < itemCount; i++) {
                    //需要特殊处理
                    if (i == position) {
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(mDotSelectColor);
                        paint.setAntiAlias(true);
                        canvas.drawCircle(startCx, cy, circleRadiusPx, paint);
                    } else {
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(mDotOtherColor);
                        paint.setAntiAlias(true);
                        canvas.drawCircle(startCx, cy, circleRadiusPx, paint);
                    }
                    startCx += oneDistance;
                }

                break;
        }
    }

    /**
     * ===================================*
     * 1. 开启循环(给定间隔)
     * 2. 停止循环
     * ==================================
     */
    public void startLoop(long delay, TimeUnit unit) {
        if (DEBUG) {
            Log.d(TAG, "startLoop: delay = " + delay + " TimeUnit = " + unit.toString());
        }
        mStartLooping = true; //已经开始
        mPauseLooping = false; //不处于暂停状态
        mDelay = delay;
        mTimeUnit = unit;
        //在切换横竖屏时进行修正
        mCurItemPosition = ((ViewPagerLayoutManager) getLayoutManager()).getCurrentPosition();
        mScheduledFuture = mScheduledThreadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                mCurItemPosition++;

                int itemCount = mCarouselLayoutManager.getItemCount();
                mCurItemPosition = (itemCount == 0 ? mCurItemPosition : (mCurItemPosition % mCarouselLayoutManager.getItemCount()));

                CarouselRecyclerView.this.smoothScrollToPosition(mCurItemPosition);
            }
        }, delay, delay, unit);
    }

    /**
     * 因为mScheduledThreadPoolExecutor是静态且全局的，因此只停止任务，不进行shutdown。
     */
    public void stopLoop() {
        if (DEBUG) {
            Log.d(TAG, "stopLoop");
        }
        mStartLooping = false;
        mScheduledFuture.cancel(true);
    }

    /**
     * RecyclerView获得焦点的状态改变时调用
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        /**==========================================
         * 1. 获得焦点时, 开启轮播的执行
         *==================================*/
        if (hasWindowFocus) {
            //开启状态并且已经被暂停，
            if (mStartLooping == true && mPauseLooping == true) {
                mPauseLooping = false; //不处于暂停状态
                mScheduledFuture = mScheduledThreadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        mCurItemPosition++;
                        int itemCount = mCarouselLayoutManager.getItemCount();
                        mCurItemPosition = (itemCount == 0 ? mCurItemPosition : (mCurItemPosition % mCarouselLayoutManager.getItemCount()));
                        CarouselRecyclerView.this.smoothScrollToPosition(mCurItemPosition);
                    }
                }, mDelay, mDelay, mTimeUnit);
                if (DEBUG) {
                    Log.d(TAG, "onWindowFocusChanged: start new Thread for Looping, Delay"
                            + mDelay + " TimeUnit = " + mTimeUnit.toString());
                }
            }
        } else {
            //正在循环，失去焦点就停止轮播的定时器
            if (mStartLooping == true) {
                mPauseLooping = true;
                mScheduledFuture.cancel(false);
                if (DEBUG) {
                    Log.d(TAG, "onWindowFocusChanged: pause Looping.");
                }
            }
        }
    }

    public float getDotIntervalDp() {
        return mDotIntervalDp;
    }

    public void setDotIntervalDp(float dotIntervalDp) {
        mDotIntervalDp = dotIntervalDp;
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
//        if(mBackgroundSetter != null){
//            mBackgroundSetter.setBackground(mCurItemPosition);
//        }
    }

    public interface BackgroundSetter{
        public void setBackground(int position);
    }

    BackgroundSetter mBackgroundSetter;

    public void setBackgroundSetter(BackgroundSetter backgroundSetter) {
        mBackgroundSetter = backgroundSetter;
    }
}
