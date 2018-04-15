package com.hao.haoview.LayoutManager.Sorry;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.os.TraceCompat;
import android.support.v7.widget.*;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hao.haoview.LayoutManager.ViewPagerLayoutManager;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * 自定义LayoutManager主要完成三件事:
 * 1. 计算每个ItemView的位置
 * 2. 处理滑动事件
 * 3. 缓存并且重用ItemView
 */
public class HaoLayoutManager extends RecyclerView.LayoutManager {
    /**
     * 布局的方向：水平/垂直方向
     */
    public static final int HORIZONTAL = android.support.v7.widget.OrientationHelper.HORIZONTAL;
    public static final int VERTICAL = android.support.v7.widget.OrientationHelper.VERTICAL;
    static final boolean DEBUG = false;
    private int mOrientation;
    private OrientationHelper mOrientationHelper;
    private LayoutState mLayoutState;
    private int totalHeight = 0;

    /**
     * 创建垂直布局
     */
    public HaoLayoutManager(Context context) {
        this(context, VERTICAL, false);
    }

    public HaoLayoutManager(Context context, int orientation, boolean reverseLayout) {
        setOrientation(orientation);
//        setReverseLayout(reverseLayout);
//        setAutoMeasureEnabled(true);
    }

    /**
     * 当LayoutManager在XML上通过RV的属性“LayoutManager”设置时使用的构造器。
     * 默认垂直方向。
     *
     * @attr ref android.support.v7.recyclerview.R.styleable#RecyclerView_android_orientation
     * @attr ref android.support.v7.recyclerview.R.styleable#RecyclerView_reverseLayout
     * @attr ref android.support.v7.recyclerview.R.styleable#RecyclerView_stackFromEnd
     */
    public HaoLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
        Properties properties = getProperties(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(properties.orientation);
//        setReverseLayout(properties.reverseLayout);
//        setStackFromEnd(properties.stackFromEnd);
//        setAutoMeasureEnabled(true);
    }

    public int getOrientation() {
        return mOrientation;
    }

    /**
     * Sets the orientation of the layout. {@link ViewPagerLayoutManager}
     * will do its best to keep scroll position.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation:" + orientation);
        }
        assertNotInLayoutOrScroll(null);
        if (orientation == mOrientation) {
            return;
        }
        mOrientation = orientation;
        mOrientationHelper = null;
//        mDistanceToBottom = INVALID_SIZE;
        removeAllViews();
    }

    /**
     * 2、自定义LayoutManager核心
     * 强调：会在RecyclerView的onMeasure()调用dispatchLayoutStep2，最终调用该方法，这是第一次调用。
     * 在RecyclerView的onLayout()调用dispatchLayoutStep2, 这是第二次调用。记得要进行detach所有View，否则数量会翻倍。
     * 1. 测量和布局Child ItemView
     * 2. 对View进行复用
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        //确认有LayoutState(LayoutState中保存了item布局的offset)
        ensureLayoutState();
        //确认有初始化OrientationHelper
        ensureOrientationHelper();

        /**
         * 先把所有的View先从RecyclerView中detach掉，然后标记为"Scrap"状态，表示这些View处于可被重用状态(非显示中)。
         * 实际就是把View放到了Recycler中的一个集合中。
         **/
        detachAndScrapAttachedViews(recycler);
//        calculateChildrenSite(recycler);
//
        //1. 将Item进行测量和布局
        fill(recycler, mLayoutState, state, false);

    }

    /**
     * 1、给ItemView提供默认的LayoutParams。
     * 使用`wrap_content`将布局的决定权交给ItemView
     */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }
    /**==========================================*
     * 滑动相关的四个方法重写：
     * 1. canScrollHorizontally():是否可以水平滑动
     * 2. canScrollVertically():是否可以垂直滑动
     *=============================================*/
    /**
     * @return true if {@link #getOrientation()} is {@link #HORIZONTAL}
     */
    @Override
    public boolean canScrollHorizontally() {
        return mOrientation == HORIZONTAL;
    }

    /**
     * @return true if {@link #getOrientation()} is {@link #VERTICAL}
     */
    @Override
    public boolean canScrollVertically() {
        return mOrientation == VERTICAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler,
                                    RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            return 0;
        }
        return scrollBy(dx, recycler, state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                  RecyclerView.State state) {
        if (mOrientation == HORIZONTAL) {
            return 0;
        }
        return scrollBy(dy, recycler, state);
    }
    int scrollBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        }
        //1. 实际要滑动的距离
        int scrolled = dy;

        mLayoutState.mRecycle = true;
        ensureLayoutState();
        mLayoutState.mScrollingOffset += scrolled;
        mOrientationHelper.offsetChildren(-scrolled);

        mLayoutState.mLastScrollDelta = scrolled;

        fill(recycler, mLayoutState, state, false);

        return scrolled;
    }


    void fill(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state, boolean stopOnFocusable) {
        //复用不管
        layoutChunk(recycler, layoutState, state);
    }

    void layoutChunk(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state) {
        mLayoutState.mOffset = 0;
        for (int i = 0; i < getItemCount(); i++) {
            final View itemView = recycler.getViewForPosition(i);
            addView(itemView);
            //测量itemViewd
            measureChildWithMargins(itemView, 0, 0);
            //布局ItemView
            layoutScrap(itemView, layoutState);
        }
    }

    private void layoutScrap(View itemView, LayoutState layoutState) {
        int consumed = mOrientationHelper.getDecoratedMeasurement(itemView);
        int left, top, right, bottom;
        if (mOrientation == VERTICAL) {
            //宽(paddingLeft ~ left + 宽度)
            left = getPaddingLeft();
            right = left + mOrientationHelper.getDecoratedMeasurementInOther(itemView);
            //高(top ~ top + 高度)
            top = layoutState.mOffset;
            bottom = layoutState.mOffset + consumed;

        } else {
            //高度
            top = getPaddingTop();
            bottom = top + mOrientationHelper.getDecoratedMeasurementInOther(itemView);
            //宽度
            left = layoutState.mOffset;
            right = layoutState.mOffset + consumed;
        }
        // We calculate everything with View's bounding box (which includes decor and margins)
        // To calculate correct layout position, we subtract margins.
        layoutDecoratedWithMargins(itemView, left, top, right, bottom);
        layoutState.mOffset += consumed;

    }

    void ensureLayoutState() {
        if (mLayoutState == null) {
            mLayoutState = createLayoutState();
        }
    }

    /**
     * Test overrides this to plug some tracking and verification.
     *
     * @return A new LayoutState
     */
    LayoutState createLayoutState() {
        return new LayoutState();
    }

    private void ensureOrientationHelper() {
        if (mOrientationHelper == null) {
            mOrientationHelper = OrientationHelper.createOrientationHelper(this, mOrientation);
        }
    }

    /**
     * Helper class that keeps temporary state while {LayoutManager} is filling out the empty
     * space.
     */
    static class LayoutState {

        static final String TAG = "LLM#LayoutState";

        static final int LAYOUT_START = -1;

        static final int LAYOUT_END = 1;

        static final int INVALID_LAYOUT = Integer.MIN_VALUE;

        static final int ITEM_DIRECTION_HEAD = -1;

        static final int ITEM_DIRECTION_TAIL = 1;

        static final int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;

        /**
         * 在布局等情况下我们不想要回收Children
         * We may not want to recycle children in some cases (e.g. layout)
         */
        boolean mRecycle = true;

        /**
         * 布局应该开始的地方，每个ItemView的layout阶段都以该offset作为起始位置。
         * 例如垂直布局 layout：
         * left = 0 + paddingLeft. right = left + Item宽度(包含装饰、margins)。
         * top = thisOffset; bottom = Offset + Item高度(包含装饰、和margins)
         */
        int mOffset;

        /**
         * Number of pixels that we should fill, in the layout direction.
         */
        int mAvailable;

        /**
         * Current position on the adapter to get the next item.
         */
        int mCurrentPosition;

        /**
         * Defines the direction in which the data adapter is traversed.
         * Should be {@link #ITEM_DIRECTION_HEAD} or {@link #ITEM_DIRECTION_TAIL}
         */
        int mItemDirection;

        /**
         * Defines the direction in which the layout is filled.
         * Should be {@link #LAYOUT_START} or {@link #LAYOUT_END}
         */
        int mLayoutDirection;

        /**
         * Used when LayoutState is constructed in a scrolling state.
         * It should be set the amount of scrolling we can make without creating a new view.
         * Settings this is required for efficient view recycling.
         */
        int mScrollingOffset;

        /**
         * Used if you want to pre-layout items that are not yet visible.
         * The difference with {@link #mAvailable} is that, when recycling, distance laid out for
         * {@link #mExtra} is not considered to avoid recycling visible children.
         */
        int mExtra = 0;

        /**
         * Equal to {@link RecyclerView.State#isPreLayout()}. When consuming scrap, if this value
         * is set to true, we skip removed views since they should not be laid out in post layout
         * step.
         */
        boolean mIsPreLayout = false;

        /**
         * The most recent {@link #scrollBy(int, RecyclerView.Recycler, RecyclerView.State)}
         * amount.
         */
        int mLastScrollDelta;

        /**
         * When LLM needs to layout particular views, it sets this list in which case, LayoutState
         * will only return views from this list and return null if it cannot find an item.
         */
        List<RecyclerView.ViewHolder> mScrapList = null;

        /**
         * Used when there is no limit in how many views can be laid out.
         */
        boolean mInfinite;

        /**
         * @return true if there are more items in the data adapter
         */
        boolean hasMore(RecyclerView.State state) {
            return mCurrentPosition >= 0 && mCurrentPosition < state.getItemCount();
        }

        /**
         * Gets the view for the next element that we should layout.
         * Also updates current item index to the next item, based on {@link #mItemDirection}
         *
         * @return The next element that we should layout.
         */
        View next(RecyclerView.Recycler recycler) {
            if (mScrapList != null) {
                return nextViewFromScrapList();
            }
            final View view = recycler.getViewForPosition(mCurrentPosition);
            mCurrentPosition += mItemDirection;
            return view;
        }

        /**
         * Returns the next item from the scrap list.
         * <p>
         * Upon finding a valid VH, sets current item position to VH.itemPosition + mItemDirection
         *
         * @return View if an item in the current position or direction exists if not null.
         */
        private View nextViewFromScrapList() {
            final int size = mScrapList.size();
            for (int i = 0; i < size; i++) {
                final View view = mScrapList.get(i).itemView;
                final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (lp.isItemRemoved()) {
                    continue;
                }
                if (mCurrentPosition == lp.getViewLayoutPosition()) {
                    assignPositionFromScrapList(view);
                    return view;
                }
            }
            return null;
        }

        public void assignPositionFromScrapList(View ignore) {
            final View closest = nextViewInLimitedList(ignore);
            if (closest == null) {
                mCurrentPosition = NO_POSITION;
            } else {
                mCurrentPosition = ((RecyclerView.LayoutParams) closest.getLayoutParams())
                        .getViewLayoutPosition();
            }
        }

        public View nextViewInLimitedList(View ignore) {
            int size = mScrapList.size();
            View closest = null;
            int closestDistance = Integer.MAX_VALUE;
            if (DEBUG && mIsPreLayout) {
                throw new IllegalStateException("Scrap list cannot be used in pre layout");
            }
            for (int i = 0; i < size; i++) {
                View view = mScrapList.get(i).itemView;
                final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (view == ignore || lp.isItemRemoved()) {
                    continue;
                }
                final int distance = (lp.getViewLayoutPosition() - mCurrentPosition)
                        * mItemDirection;
                if (distance < 0) {
                    continue; // item is not in current direction
                }
                if (distance < closestDistance) {
                    closest = view;
                    closestDistance = distance;
                    if (distance == 0) {
                        break;
                    }
                }
            }
            return closest;
        }

        public void assignPositionFromScrapList() {
            assignPositionFromScrapList(null);
        }

        void log() {
            Log.d(TAG, "avail:" + mAvailable + ", ind:" + mCurrentPosition + ", dir:"
                    + mItemDirection + ", offset:" + mOffset + ", layoutDir:" + mLayoutDirection);
        }
    }
}
