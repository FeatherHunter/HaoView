package com.hao.haoview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mr on 3/25/2018.
 */

public interface HaoAnimatible {
    public void startAnimation(RecyclerView recyclerView,int curItemPosition, float percent);
}
