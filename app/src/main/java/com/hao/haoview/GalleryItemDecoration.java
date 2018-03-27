package com.hao.haoview;

import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hao.haoview.util.OsUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by mr on 3/24/2018.
 */

public class GalleryItemDecoration extends HaoItemDecoration {
    private final String TAG = "GalleryItemDecoration";

    /**====================================================
     * 3-Item分割线的方向(水平 or 垂直)
     *==================================================*/
    @IntDef({LinearLayoutManager.HORIZONTAL, LinearLayoutManager.VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Oritentions{};
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;
    private @Oritentions static int mDefaultOritention = HORIZONTAL; //默认水平方向

    public static void setOritention(@Oritentions int oritention) {
        GalleryItemDecoration.mDefaultOritention = oritention;
    }

    /**========================================================
     * 4-绘制分割线
     *   原理：1. 更改ItemView的布局参数，并在布局中留下两侧页边距{@link GalleryItemDecoration#mPageMargin}。
     *         2. 同时预留出两侧ItemView想要显示出来的距离{@link GalleryItemDecoration#mOtherPageVisibleWidth}
     *=========================================================*/
    @Override
    public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //1. 当前View的下标
        final int position = parent.getChildAdapterPosition(view);
        //2. Item总数
        final int itemCount = parent.getAdapter().getItemCount();
        //3. 等RecyclerView绘制好后进行布局参数的修改
        parent.post(new Runnable() {
            @Override
            public void run() {
                //4. 水平方向 / 垂直方向
                if(mDefaultOritention == HORIZONTAL){
                    onSetHoritiontalParams(parent, view, position, itemCount);
                }else {
                    onSetVerticalParams(parent, view, position, itemCount);
                }
            }
        });
    }

    /**========================================================
     * 5(1)-设置垂直列表的每个Item的布局参数
     *=========================================================*/
    private void onSetVerticalParams(ViewGroup parent, View itemView, int position, int itemCount) {
        //1. 宽度 = 父控件宽度
        int itemNewWidth = parent.getWidth();
        //2. 新高度 = 父控件高度 - 4个页边距 - 2个两侧ItemView的预留宽度
        int itemNewHeight = parent.getHeight() - OsUtil.dpToPx(4 * mPageMargin + 2 * mOtherPageVisibleWidth);
        //3. 计算出切换一个页面需要的距离(Y轴上)
        mMove1PageNeedY = itemNewHeight + OsUtil.dpToPx(2 * mPageMargin);
        //4. 适配第0页和最后一页没有上页面和下页面，让他们保持上边距和下边距和其他ItemView一致
        int topMargin = position == 0 ? OsUtil.dpToPx(mOtherPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);
        int bottomMargin = position == itemCount - 1 ? OsUtil.dpToPx(mOtherPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);
        //5. 设置布局参数(最终会在ItemView绘制时生效)
        setLayoutParams(itemView, 0, topMargin, 0, bottomMargin, itemNewWidth, itemNewHeight);
    }

    /**========================================================
     * 5(2)-设置水平列表的每个Item的布局参数
     *=========================================================*/
    private void onSetHoritiontalParams(ViewGroup parent, View itemView, int position, int itemCount) {
        //1. 宽度 = 父控件宽度 - 4个页边距 - 2个两侧ItemView的预留宽度
        int itemNewWidth = parent.getWidth() - OsUtil.dpToPx(4 * mPageMargin + 2 * mOtherPageVisibleWidth);
        //2. 新高度 = 父控件高度
        int itemNewHeight = parent.getHeight();
        //3. 计算出切换一个页面需要的距离(X轴上)
        mMove1PageNeedX = itemNewWidth + OsUtil.dpToPx(2 * mPageMargin);
        //4. 适配第0页和最后一页没有左页面和右页面，让他们保持左边距和右边距和其他ItemView一致
        int leftMargin = position == 0 ? OsUtil.dpToPx(mOtherPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);
        int rightMargin = position == itemCount - 1 ? OsUtil.dpToPx(mOtherPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);
        //5. 设置布局参数
        setLayoutParams(itemView, leftMargin, 0, rightMargin, 0, itemNewWidth, itemNewHeight);
    }

    /**=======================================*
     * 更改ItemView布局参数
     * 1. 更改上下左右的margin值
     * 2. 更改控件的宽和高
     *=======================================*/
    private void setLayoutParams(View itemView, int left, int top, int right, int bottom, int itemWidth, int itemHeight) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        boolean mMarginChange = false;
        boolean mWidthChange = false;
        boolean mHeightChange = false;

        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            mMarginChange = true;
        }
        if (lp.width != itemWidth) {
            lp.width = itemWidth;
            mWidthChange = true;
        }
        if (lp.height != itemHeight) {
            lp.height = itemHeight;
            mHeightChange = true;
        }
        // 因为方法会不断调用，只有在真正变化了之后才调用
        if (mWidthChange || mMarginChange || mHeightChange) {
            itemView.setLayoutParams(lp);
        }
    }

    /**
     * ===============================================================================
     * 1. 在Position位置时，滑动的总距离(处于第一个Item时距离为0)
     * =============================================================================
     */
    public int getSumScrollXByPosition(int position) {
        //第position + 1个Item需要有(position X 一页滑动距离)的补充
        return position * mMove1PageNeedX;
    }
    public int getSumScrollYByPosition(int position) {
        //第position + 1个Item需要有(position X 一页滑动距离)的补充
        return position * mMove1PageNeedY;
    }


    public GalleryItemDecoration() {
    }
}
