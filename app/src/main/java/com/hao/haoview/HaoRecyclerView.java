package com.hao.haoview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hao.haoview.util.OsUtil;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by mr on 3/25/2018.
 */

public class HaoRecyclerView extends RecyclerView {
    private Context mContext;
    private ArrayList<Object> mDatas = null;
    /**
     * =====================================*
     * 1-滑动距离百分比(比例越大, 滑动越快)
     *
     * @see HaoRecyclerView#fling(int, int)
     * =====================================
     */
    private float mFlingSpeedRatio = 1f;

    /**
     * ===========================================================*
     * 2-SnapHelper滑动辅助器: 保证当前ViewItem在RecyclerView中居中
     * LinearSnapHelper: 能连续滑动多个页面
     * PagerSnapHelper: 一次只能滑动一个页面(类似ViewPager)
     *
     * @see HaoRecyclerView#setSnapHelper(int)
     * ===========================================================
     */
    @IntDef({LinearSnapHelper, PagerSnapHelper})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SnapHelper {
    }

    ;
    public static final int LinearSnapHelper = 0;
    public static final int PagerSnapHelper = 1;
    private int mSnapHelper = LinearSnapHelper;
    /**
     * ========================================================================*
     * 3-整个RecyclerView在X轴上滑动的全部距离累加
     * 例如：滑动到第10个Item-mSumScrollX = 当前Item滑动到左侧需要的距离 * 9
     * ========================================================================
     */
    private int mSumScrollX = 0;
    private int mSumScrollY = 0;

    /**
     * ========================================================================*
     * 4-滑动的方向
     * ========================================================================
     */
    @IntDef({SLIDE_LEFT, SLIDE_RIGHT, SLIDE_TOP, SLIDE_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SlideDirection {
    }

    private static final int SLIDE_LEFT = 1;    // 左滑
    private static final int SLIDE_RIGHT = 2;   // 右滑
    private static final int SLIDE_TOP = 3;     // 上滑
    private static final int SLIDE_BOTTOM = 4;  // 下滑
    // 滑动方向默认为右滑
    private int slideDirct = SLIDE_RIGHT;

    /**
     * ===============================*
     * 5-动画(需要实现HaoAnimatible接口)
     * ===============================
     */
    private HaoAnimatible mAnimation;
    /**
     * ===============================*
     * 6-RecyclerView是否持有焦点
     * ===============================
     */
    public boolean mHasWindowFocus = false;
    /**
     * ===============================*
     * 7-RecyclerView第一次运行需要修正第一页的Margin问题
     * ===============================
     */
    private boolean isFirst = true;
    //当前ItemView的Position
    private int mCurItemPosition = 0;
    // 8-分隔线绘制，并获得每个Item的实际长宽
    private HaoItemDecoration mHaoItemDecoration;
    // 9-是否开启高斯模糊
    private boolean isBlured = false; //默认关闭

    public HaoRecyclerView hasBlurBackground(boolean isBlured){
        this.isBlured = isBlured;
        return this;
    }

    public HaoRecyclerView setPageMargin(int pageMargin) {
        mHaoItemDecoration.setPageMargin(pageMargin);
        return this;
    }

    public HaoRecyclerView setOtherPageVisibleWidth(int pageVisibleWidth) {
        mHaoItemDecoration.setOtherPageVisibleWidth(pageVisibleWidth);
        return this;
    }

    public HaoRecyclerView(Context context) {
        super(context);
        mContext = context;
        initRecyclerView();
    }

    public HaoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initRecyclerView();
    }

    public HaoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initRecyclerView();
    }

    private void initRecyclerView() {
//        //1. 默认采用LinearSnapHelper作为滑动居中辅助器
//        setSnapHelper(LinearSnapHelper);
        //2. 默认使用GalleryItemDecoration作为分割线
        addItemDecoration(mHaoItemDecoration = new GalleryItemDecoration());
        //3. 设置滑动监听器
        addOnScrollListener(new GalleryScrollerListener());
        //4.
        setAnimation(new ScaleAnimation());
    }

    /**
     * =================================================================================*
     * 1-设置RecyclerView的滑动速度
     * 重载fling方法，将横向和纵向的移动距离 x 滑动速度百分比{@link HaoRecyclerView#mFlingSpeedRatio} = 增速or减速后的移动距离。
     *
     * @param velocityX 横向的移动距离(可为正负)
     * @param velocityY 纵向的移动距离
     *                  =================================================================================
     */
    @Override
    public boolean fling(int velocityX, int velocityY) {
        return super.fling((int) (velocityX * mFlingSpeedRatio), (int) (velocityY * mFlingSpeedRatio));
    }

    /**
     * =================================================================================*
     * 2-设置滑动居中辅助器
     *
     * @param snapHelperStyle 决定采用哪种滑动居中辅助器
     *                        {@link HaoRecyclerView#LinearSnapHelper}: 能连续滑动多个页面
     *                        {@link HaoRecyclerView#PagerSnapHelper} : 只能一次滑动一个页面
     *                        =================================================================================
     */
    public HaoRecyclerView setSnapHelper(@SnapHelper int snapHelperStyle) {
        mSnapHelper = snapHelperStyle;
        switch (snapHelperStyle) {
            case LinearSnapHelper: {
                new LinearSnapHelper().attachToRecyclerView(this);
                break;
            }
            case PagerSnapHelper: {
                new PagerSnapHelper().attachToRecyclerView(this);
                break;
            }
        }
        return this;
    }

    /**
     * =================================================================================*
     * 3-滑动监听器
     * 1. onScrolled()在列表滑动时调用
     * =================================================================================
     */
    private class GalleryScrollerListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //1. 没有获得焦点不进行滑动操作
            if (!mHasWindowFocus) {
                return;
            }
            if (((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                //2. 水平滑动时的滑动动画
                onHoritiontalScroll(dx);
            } else {
                //3. 垂直滑动时的滑动动画
                onVerticalScroll(dy);
            }
        }

        /**
         * ==============================
         * 水平滑动时：
         * 1. 计算出当前ItemView的位置和滑动的百分比
         * 2. 进行动画处理{@link HaoAnimatible#startAnimation(RecyclerView, int, float)}
         * ==============================
         */
        private void onHoritiontalScroll(final int dx) {
            //1. X轴方向上滑动的距离累加
            mSumScrollX += dx;
            if (mSumScrollX < 0) {
                mSumScrollX = 0;
            }
            if (dx > 0) {
                slideDirct = SLIDE_RIGHT;  // 右滑
            } else {
                slideDirct = SLIDE_LEFT;  //左滑· ·   ·   ·
            }
            //2. 让RecyclerView测绘完成后再调用，避免mMove1PageNeedX的值为0
            HaoRecyclerView.this.post(new Runnable() {
                @Override
                public void run() {
                    int move1PageNeedX = mHaoItemDecoration.getMove1PageNeedX();
                    // 3. 获取当前ItemView在RecyclerView中的位置
                    int position = getPosition(mSumScrollX, move1PageNeedX);
                    // 4.滑动到不同Item需要不同的背景
                    if(mCurItemPosition != position){
                        setBlurBackground(position);
                    }
                    mCurItemPosition = position;
                    // 4. 获取当前ItemView移动的百分值(等效于 (mSumScrollX % move1PageNeedX) / move1PageNeedX)
                    float offset = (float) mSumScrollX / (float) move1PageNeedX;

                    // 避免offset值取整时进一，从而影响了percent值
                    if (offset >= ((LinearLayoutManager) ((HaoRecyclerView.this).getLayoutManager())).findFirstVisibleItemPosition() + 1 && slideDirct == SLIDE_RIGHT) {
                        return;
                    }

                    float percent = offset - ((int) offset);
                    // 5. 开始动画
                    mAnimation.startAnimation(HaoRecyclerView.this, position, percent);
                }
            });

        }

        /**
         * ==============================
         * 垂直滑动时：
         * 1. 计算出当前ItemView的位置和滑动的百分比
         * 2. 进行动画处理{@link HaoAnimatible#startAnimation(RecyclerView, int, float)}
         * ==============================
         */
        private void onVerticalScroll(int dy) {
            //1. X轴方向上滑动的距离累加
            mSumScrollY += dy;
            if (dy > 0) {
                slideDirct = SLIDE_BOTTOM;
            } else {
                slideDirct = SLIDE_TOP;
            }
            // 2. 让RecyclerView测绘完成后再调用，避免mMove1PageNeedY的值为0
            (HaoRecyclerView.this).post(new Runnable() {
                @Override
                public void run() {
                    int move1PageNeedY = mHaoItemDecoration.getMove1PageNeedY();
                    // 3. 获取当前ItemView在RecyclerView中垂直方向的位置
                    int position = getPosition(mSumScrollY, move1PageNeedY);
                    // 4.滑动到不同Item需要不同的背景
                    if(mCurItemPosition != position){
                        setBlurBackground(position);
                    }
                    mCurItemPosition = position;
                    float offset = (float) mSumScrollY / (float) move1PageNeedY;
                    // 4. 获取当前ItemView移动的百分值
                    float percent = offset - ((int) offset);
                    // 5. 开始动画
                    mAnimation.startAnimation(HaoRecyclerView.this, position, percent);
                }
            });
        }
    }

    /**
     * ====================================================================
     * 4-获取到当前ItemView在RecyclerView中的位置
     * 如：第10个ItemView的下标应该是9
     *
     * @param mSumScrollDistance  RecyclerView启动至现在所滑动的所有距离
     * @param moveOnePageDistance 移动一页需要的距离(左边Item的最右侧和当前Item最右侧之间的举例)
     *                            =========================================================================
     */
    private int getPosition(int mSumScrollDistance, int moveOnePageDistance) {
        float offset = (float) mSumScrollDistance / (float) moveOnePageDistance;
        int position = Math.round(offset);        // 四舍五入获取位置
        return position;
    }

    /**
     * RecyclerView获得焦点的状态改变时调用
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (getAdapter().getItemCount() <= 0) {
            return;
        }
        //1. 获得焦点
        this.mHasWindowFocus = hasWindowFocus;

        /**==========================================
         * 2. 获得焦点时跳转到失去焦点时所在的ItemView
         *    1-需要得到正确的滑动总距离
         *==================================*/
        if (mHasWindowFocus) {
            //1. 获得焦点后
            ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(mCurItemPosition,
                    OsUtil.dpToPx(mHaoItemDecoration.getOtherPageVisibleWidth() + mHaoItemDecoration.getPageMargin()));
            //2. 通过ItemDecoration计算出需要偏移的距离
            mSumScrollX = mHaoItemDecoration.getSumScrollXByPosition(mCurItemPosition);
            mSumScrollY = mHaoItemDecoration.getSumScrollYByPosition(mCurItemPosition);
            //3. 动画：将当前ItemView放置到最大，两侧的缩小到合适大小
            mAnimation.startAnimation(HaoRecyclerView.this, mCurItemPosition, 0);
            //4.
            setBlurBackground(mCurItemPosition);
        }
    }

    /**
     * 设置布局管理器(必须为LinearLayoutManager，否则会强制默认一个)
     */
    @Override
    public void setLayoutManager(@NotNull LayoutManager layoutManager) {
        if ((layoutManager instanceof LinearLayoutManager) && !(layoutManager instanceof GridLayoutManager)) {
            //1. 是LinearLayoutManager, 就继续采用
        } else {
            //2. 是其他布局就强行更改为默认的线性布局，且为水平方向。防止出现问题
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        }
        super.setLayoutManager(layoutManager);
    }

    /**
     * 设置动画
     */
    public HaoRecyclerView setAnimation(HaoAnimatible animation) {
        mAnimation = animation;
        return this;
    }

    public int getPosition() {
        return mCurItemPosition;
    }

    // RecyclerView背景的高斯模糊
    private void setBlurBackground(int position){
        if(isBlured){
            if(mDatas == null){
                mDatas = ((RecyclerViewAdpater)getAdapter()).getDatas();
            }
            if(position >= mDatas.size() || position < 0){
                return;
            }

            //1. 给背景添加图片
            SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    HaoRecyclerView.this.setBackground(resource);
                }
            };

            if(mDatas.get(position) instanceof String){ //为URL

            }else if(mDatas.get(position) instanceof Drawable){

            }else if(mDatas.get(position) instanceof Integer){
                //2. 虚化效果
                Glide.with(mContext).load((Integer) mDatas.get(position))
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                        .into(simpleTarget);
            }
        }
    }
}
