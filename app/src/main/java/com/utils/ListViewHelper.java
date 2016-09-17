package com.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.widgets.recycleviewwrap.WrapRecyclerView;


public final class ListViewHelper {

    private ListViewHelper() {
    }

    /**
     * 判断RecyclerView是否可以向下拉
     *
     * @param recyclerView
     * @return
     */
    public static boolean canPullDown(RecyclerView recyclerView) {
        // 无数据可以向下滚动
        if (recyclerView == null || recyclerView.getVisibility() != View.VISIBLE) {
            return false;
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int i = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            return (i > 0) || (i == -1);
        }

        View firstView = recyclerView.getChildAt(0);
        if (firstView == null) {
            return false;
        }

        return firstView.getTop() < 0;
    }

    /**
     * 判断RecyclerView是否可以向上拉
     *
     * @param recyclerView
     * @return
     */
    public static boolean canPullUp(WrapRecyclerView recyclerView) {
        if (recyclerView == null || recyclerView.getVisibility() != View.VISIBLE) {
            return false;
        }

        int itemCount = recyclerView.getAdapter().getItemCount();
        int footCount = recyclerView.getFooterViewsCount();
        if (itemCount == 0) {
            return false;
        } else {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int lastVisiblePos = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                if (lastVisiblePos >= itemCount - footCount - 1) {
                    return true;
                }
            }
        }

        return false;
    }


}
