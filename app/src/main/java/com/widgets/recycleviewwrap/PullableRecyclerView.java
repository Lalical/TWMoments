package com.widgets.recycleviewwrap;

import android.content.Context;
import android.util.AttributeSet;

import com.utils.ListViewHelper;


/**
 * Created by pingyongxia on 2015/11/23.
 */
public class PullableRecyclerView extends WrapRecyclerView implements PullRefresh.IAllowOutLayoutPullDown {


    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean allowOutLayoutPullDown() {
        return !ListViewHelper.canPullDown(this);
    }
}
