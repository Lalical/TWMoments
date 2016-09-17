package com.widgets.recycleviewwrap;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * 封装带header和footer的RecyclerView
 * Created by pingyongxia on 2015/11/23.
 */
public class WrapRecyclerView extends RecyclerView {

    private ArrayList<View> mHeaderViews = new ArrayList<View>();
    private ArrayList<View> mFootViews = new ArrayList<View>();
    private Adapter mAdapter;
    private Context mContext;

    public WrapRecyclerView(Context context) {
        super(context);
        mContext = context;
    }

    public WrapRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public WrapRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public void addHeaderView(View view) {
        if (mAdapter != null) {
            LayoutManager manager = this.getLayoutManager();
            // 现在只判断LinearLayoutManager 的布局，添加Header的时候，如果当前显示的Item为第一个 也就是说Position == 0 时，将RecyclerView 滚动到position为0的位置
            if (manager instanceof LinearLayoutManager) {
                int position = ((LinearLayoutManager) manager).findFirstCompletelyVisibleItemPosition();
                if (position <= 0 && position != -1) {
                    this.scrollToPosition(0);
                }
            }
            ((WrapRecyclerViewAdapter) mAdapter).addHeaderView(view);
        } else {
            mHeaderViews.add(view);
        }
    }

    public void removeHeaderView(View view) {
        if (mAdapter != null) {
            ((WrapRecyclerViewAdapter) mAdapter).removeHeaderView(view);
        } else {
            try {
                mHeaderViews.remove(view);
            } catch (Exception e) {

            }
        }
    }


    public void resetHeaderView() {
        if (mAdapter != null) {
            ((WrapRecyclerViewAdapter) mAdapter).resetHeaderView();
        } else {
            mHeaderViews.clear();
        }
    }

    public void addFooterView(View view) {
        if (mAdapter != null) {
            ((WrapRecyclerViewAdapter) mAdapter).addFooterView(view);
        } else {
            mFootViews.add(view);
        }
    }

    public void removeFooterView(View view) {
        if (mAdapter != null) {
            ((WrapRecyclerViewAdapter) mAdapter).removeFooterView(view);
        } else {
            try {
                mFootViews.remove(view);
            } catch (Exception e) {

            }
        }
    }


    public void resetFooterView() {
        if (mAdapter != null) {
            ((WrapRecyclerViewAdapter) mAdapter).resetFooterView();
        } else {
            mFootViews.clear();
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof BaseRecyclerAdapter)) {
            throw new IllegalStateException("adpater should be instance of BaseRecyclerAdapter");
        }
        if (mHeaderViews.size() > 0 || mFootViews.size() > 0) {
            mAdapter = new WrapRecyclerViewAdapter(mContext, mHeaderViews, mFootViews, (BaseRecyclerAdapter) adapter);
        } else {
            mAdapter = new WrapRecyclerViewAdapter(mContext, (BaseRecyclerAdapter) adapter);
        }
        super.setAdapter(mAdapter);
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    public int getHeaderViewsCount() {
        if (mAdapter != null && mAdapter instanceof WrapRecyclerViewAdapter) {
            return ((WrapRecyclerViewAdapter) mAdapter).getHeadersCount();
        }

        return 0;
    }

    public int getFooterViewsCount() {
        if (mAdapter != null && mAdapter instanceof WrapRecyclerViewAdapter) {
            return ((WrapRecyclerViewAdapter) mAdapter).getFootersCount();
        }

        return 0;
    }
}
