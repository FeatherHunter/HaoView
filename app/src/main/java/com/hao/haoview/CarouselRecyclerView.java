package com.hao.haoview;

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

import com.hao.haoview.LayoutManager.CarouselLayoutManager;
import com.hao.haoview.LayoutManager.ViewPagerLayoutManager;

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
    private boolean DEBUG = true;
    /**
     * 存放数据
     */
    private ArrayList<Object> mDatas = null;
    /**
     * 当前Item的位置
     */
    private int mCurItemPosition = 0;
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
    @IntDef({DOT_BOTTOM_LEFT, DOT_BOTTOM_RIGHT, DOT_BOTTOM_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DotDirection {
    }

    public static final int DOT_NULL = 0;
    public static final int DOT_BOTTOM_LEFT = 1;    // 底部左边
    public static final int DOT_BOTTOM_RIGHT = 2;   // 底部右边
    public static final int DOT_BOTTOM_CENTER = 3;  // 底部中间
    private static final String TAG = CarouselRecyclerView.class.getName();
    /**
     * ===================================*
     * 1-周期线程池(Banner效果，定时切换Item)
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

    public CarouselRecyclerView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public void init() {
        //设置循环布局
        mCarouselLayoutManager = new CarouselLayoutManager(mContext, 0, ViewPagerLayoutManager.HORIZONTAL);
        mCarouselLayoutManager.setInfinite(true);
        mCarouselLayoutManager.setMoveSpeed(1f);
        mCarouselLayoutManager.setMinScale(1f);
        this.setLayoutManager(mCarouselLayoutManager);
        //默认轮播点在中央正下方
        setDotPosition(DOT_BOTTOM_RIGHT);
        //轮播点半径
        setDotRadiusDp(2);
        //轮播点间隔
        setDotIntervalDp(1);
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
        float dorIntervalPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDotIntervalDp, mContext.getResources().getDisplayMetrics());
        // 获取数据集合
        if (mDatas == null) {
            mDatas = ((RecyclerViewAdpater) getAdapter()).getDatas();
        }
        // RV宽高
        int width = CarouselRecyclerView.this.getWidth();
        int height = CarouselRecyclerView.this.getHeight();

        Paint paint = new Paint();

        switch (mDotPosition) {
            case DOT_NULL:
                break;
            case DOT_BOTTOM_CENTER:

                float cy = height - 2 * circleRadiusPx;

                int count = mDatas.size();
                float leftDistance = (count / 2 + (count % 2) * 0.5f) * (dorIntervalPx + circleRadiusPx);
                float oneDistance = (2 * circleRadiusPx) + dorIntervalPx;

                float startCx = width / 2 - leftDistance;

                mCurItemPosition = ((ViewPagerLayoutManager) getLayoutManager()).getCurrentPosition();
                int position = (mCurItemPosition + mDatas.size()) % mDatas.size();

                for (int i = 0; i < mDatas.size(); i++) {
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

                cy = height - 2 * circleRadiusPx;

                count = mDatas.size();

                leftDistance = (count * circleRadiusPx * 2) + (count - 1) * dorIntervalPx;
                oneDistance = (2 * circleRadiusPx) + dorIntervalPx;

                startCx = width  - leftDistance;

                mCurItemPosition = ((ViewPagerLayoutManager) getLayoutManager()).getCurrentPosition();

                position = (mCurItemPosition + mDatas.size()) % mDatas.size();

                for (int i = 0; i < mDatas.size(); i++) {
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
//        CarouselRecyclerView.this.smoothScrollToPosition(mCurItemPosition);
//        requestLayout();
        mScheduledFuture = mScheduledThreadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                mCurItemPosition++;
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
//                CarouselRecyclerView.this.smoothScrollToPosition(mCurItemPosition);
//                requestLayout();
                mScheduledFuture = mScheduledThreadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        mCurItemPosition++;
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
}
