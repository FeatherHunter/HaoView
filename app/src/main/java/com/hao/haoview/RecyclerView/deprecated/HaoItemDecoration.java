package com.hao.haoview.RecyclerView.deprecated;

import android.support.v7.widget.RecyclerView;

/**
 * Created by mr on 3/27/2018.
 */

public abstract class HaoItemDecoration extends RecyclerView.ItemDecoration{
    /**===============================================
     * 1-每个页面的左右或者上下的页边距
     *=============================================*/
    protected int mPageMargin = 0;          // 每一个页面默认页边距(左右或者上下)
    protected int mOtherPageVisibleWidth = 0; // 左右或者上下两个Page的能见宽度
    /**=====================================================
     * 2-当前页面的Item滑动到下一个Item所需要的距离(X/Y)
     *=====================================================*/
    protected int mMove1PageNeedY = 0;
    protected int mMove1PageNeedX = 0;

    public int getPageMargin() {
        return mPageMargin;
    }

    public void setPageMargin(int pageMargin) {
        mPageMargin = pageMargin;
    }

    public int getOtherPageVisibleWidth() {
        return mOtherPageVisibleWidth;
    }

    public void setOtherPageVisibleWidth(int otherPageVisibleWidth) {
        mOtherPageVisibleWidth = otherPageVisibleWidth;
    }

    public int getMove1PageNeedY() {
        return mMove1PageNeedY;
    }

    public void setMove1PageNeedY(int move1PageNeedY) {
        mMove1PageNeedY = move1PageNeedY;
    }

    public int getMove1PageNeedX() {
        return mMove1PageNeedX;
    }

    public void setMove1PageNeedX(int move1PageNeedX) {
        mMove1PageNeedX = move1PageNeedX;
    }

    public abstract int getSumScrollXByPosition(int position);
    public abstract int getSumScrollYByPosition(int position);
}
