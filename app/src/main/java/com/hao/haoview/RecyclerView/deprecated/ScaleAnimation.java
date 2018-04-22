package com.hao.haoview.RecyclerView.deprecated;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ScaleAnimation implements HaoAnimatible {
    private static final String TAG = ScaleAnimation.class.getName();
    /**========================================
     * 1-动画类型和缩放因子
     *   View最小为(1-缩放因子)%，如:1-0.2=80%
     *========================================*/
    @IntDef({SCALE_BIG_TO_SMALL, SCALE_SMALL_TO_BIG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScaleStyle{}
    public static final int SCALE_BIG_TO_SMALL = 0;
    public static final int SCALE_SMALL_TO_BIG = 1;
    private int mAnimType = SCALE_BIG_TO_SMALL; //动画类型
    private float mAnimFactor = 0.2f;   //缩放因子
    /**=============================*
     * 2-开启动画
     *==============================*/
    @Override
    public void startAnimation(RecyclerView recyclerView, int curItemPosition, float percent) {
        switch (mAnimType) {
            case SCALE_BIG_TO_SMALL:
                setBigToSmallAnim(recyclerView, curItemPosition, percent);
                break;
            case SCALE_SMALL_TO_BIG:
                setSmallToBigAnim(recyclerView, curItemPosition, percent);
                break;
            default:
                setBigToSmallAnim(recyclerView, curItemPosition, percent);
                break;
        }
    }
    /**=============================================================================*
     * 页面正中的ItemView的：
     *   1. 消失过程从大到小
     *   2. 出现过程相反
     * @param recyclerView recycleView用于获取到当前ItemView和两侧ItemView
     * @param position 表明当前ItemView的下标
     * @param percent 滑动动作所占整个页面的百分比(ItemView的大小跟随着缩放)
     *===============================================================================*/
    private void setBigToSmallAnim(RecyclerView recyclerView, int position, float percent) {
        View mCurView = recyclerView.getLayoutManager().findViewByPosition(position);       // 中间页
        View mRightView = recyclerView.getLayoutManager().findViewByPosition(position + 1); // 左边页
        View mLeftView = recyclerView.getLayoutManager().findViewByPosition(position - 1);  // 右边页

        if (percent <= 0.5) {
            if (mLeftView != null) {
                mLeftView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mLeftView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mCurView != null) {
                mCurView.setScaleX(1 - percent * mAnimFactor);
                mCurView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mRightView != null) {
                mRightView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mRightView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
        } else {
            if (mLeftView != null) {
                mLeftView.setScaleX(1 - percent * mAnimFactor);
                mLeftView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mCurView != null) {
                mCurView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mCurView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mRightView != null) {
                mRightView.setScaleX(1 - percent * mAnimFactor);
                mRightView.setScaleY(1 - percent * mAnimFactor);
            }
        }
    }
    /**=============================================================================*
     * 页面正中的ItemView的动画效果：
     *   1. 消失过程从小到到
     *   2. 出现过程相反
     * @param recyclerView recycleView用于获取到当前ItemView和两侧ItemView
     * @param position 表明当前ItemView的下标
     * @param percent 滑动动作所占整个页面的百分比(ItemView的大小跟随着缩放)
     *===============================================================================*/
    private void setSmallToBigAnim(RecyclerView recyclerView, int position, float percent) {
        View mCurView = recyclerView.getLayoutManager().findViewByPosition(position);       // 中间页
        View mRightView = recyclerView.getLayoutManager().findViewByPosition(position + 1); // 左边页
        View mLeftView = recyclerView.getLayoutManager().findViewByPosition(position - 1);  // 右边页

        if (percent <= 0.5) {
            if (mLeftView != null) {
                mLeftView.setScaleX(1 - percent * mAnimFactor);
                mLeftView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mCurView != null) {
                mCurView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mCurView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mRightView != null) {
                mRightView.setScaleX(1 - percent * mAnimFactor);
                mRightView.setScaleY(1 - percent * mAnimFactor);
            }

        } else {
            if (mLeftView != null) {
                mLeftView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mLeftView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mCurView != null) {
                mCurView.setScaleX(1 - percent * mAnimFactor);
                mCurView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mRightView != null) {
                mRightView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mRightView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
        }
    }
    //1.
    public void setmAnimFactor(float mAnimFactor) {
        this.mAnimFactor = mAnimFactor;
    }
    public void setmAnimType(@ScaleStyle int mAnimType) {
        this.mAnimType = mAnimType;
    }
}
