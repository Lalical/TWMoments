package com.widgets.recycleviewwrap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.lalic.twmoments.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理带header和footer的RecyclerView.Adapter
 * Created by pingyongxia on 2015/11/23.
 */
public class WrapRecyclerViewAdapter<T extends BaseRecyclerItem> extends RecyclerView.Adapter implements BaseRecyclerAdapter.ParentRefreshListener {

    private final String TAG = this.getClass().getName();
    private BaseRecyclerAdapter<T> mAdapter;
    private Context mContext;
    private List<View> mHeaderView = new ArrayList<>();
    private List<View> mFooterView = new ArrayList<>();

    public WrapRecyclerViewAdapter(Context context, ArrayList<View> headerViews, ArrayList<View> footViews, BaseRecyclerAdapter<T> adapter) {
        this(context, adapter);
        this.mHeaderView.addAll(headerViews);
        this.mFooterView.addAll(headerViews);
    }

    public WrapRecyclerViewAdapter(Context context, BaseRecyclerAdapter<T> adapter) {
        mAdapter = adapter;
        mContext = context;
        mAdapter.setParentListener(this);
    }

    public int getHeadersCount() {
        return mHeaderView.size();
    }

    public void addHeaderView(View view) {
        mHeaderView.add(0, view);
        notifyItemInserted(0);
    }

    public void removeHeaderView(View view) {
        int index = mHeaderView.indexOf(view);
        if (index >= 0) {
            mHeaderView.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void resetHeaderView() {
        int headerCount = mHeaderView.size();
        mHeaderView.clear();
        notifyItemRangeRemoved(0, headerCount);
    }

    public int getFootersCount() {
        return mFooterView.size();
    }

    public void addFooterView(View view) {
        mFooterView.add(0, view);
        int mAdapterCount = 0;
        if (mAdapter != null) {
            mAdapterCount = mAdapter.getItemCount();
        }
        notifyItemInserted(mHeaderView.size() + mAdapterCount);
    }

    public void removeFooterView(View view) {
        int index = mFooterView.indexOf(view);
        if (index >= 0) {
            mFooterView.remove(index);
            int mAdapterCount = 0;
            if (mAdapter != null) {
                mAdapterCount = mAdapter.getItemCount();
            }
            notifyItemRemoved(index + mHeaderView.size() + mAdapterCount);
        }
    }

    public void resetFooterView() {
        int footerCount = mFooterView.size();
        mFooterView.clear();
        int mAdapterCount = 0;
        if (mAdapter != null) {
            mAdapterCount = mAdapter.getItemCount();
        }
        notifyItemRangeRemoved(mHeaderView.size() + mAdapterCount, footerCount);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == RecyclerView.INVALID_TYPE || viewType == RecyclerView.INVALID_TYPE - 1) {
            FrameLayout view = (FrameLayout) LayoutInflater.from(mContext).inflate(R.layout.head_root_view, viewGroup, false);
            return new HeaderViewHolder(view);
        } else {
            return mAdapter.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
        }
        int headViewSize = mHeaderView.size();
        if (position < headViewSize) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            headerViewHolder.setHeader(mHeaderView.get(position));
        } else if (position >= headViewSize + adapterCount) {
            HeaderViewHolder footerViewHolder = (HeaderViewHolder) viewHolder;
            footerViewHolder.setHeader(mFooterView.get(position - (headViewSize + adapterCount)));
        } else {
            mAdapter.onBindViewHolder((BaseRecyclerViewHolder) viewHolder, position - headViewSize);
            return;
        }
    }

    @Override
    public int getItemCount() {
        return mHeaderView.size() + mFooterView.size() + (mAdapter != null ? mAdapter.getItemCount() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaderView.size()) {
            return RecyclerView.INVALID_TYPE;
        }
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
        }
        if (position >= mHeaderView.size() + adapterCount) {
            return RecyclerView.INVALID_TYPE - 1;
        } else {
            return mAdapter.getItemViewType(position - mHeaderView.size());
        }
    }

    @Override
    public long getItemId(int position) {
        int numHeaders = 1;
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }

    public BaseRecyclerAdapter getAdapter() {
        return mAdapter;
    }

    public void parentDataSetChanged() {
        notifyDataSetChanged();
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout mRootView;

        public HeaderViewHolder(FrameLayout itemView) {
            super(itemView);
            mRootView = itemView;
        }

        public void setHeader(View view) {
            ViewParent viewParent = view.getParent();
            if (viewParent != null) {
                ((ViewGroup) viewParent).removeView(view);
            }
            mRootView.addView(view);
        }
    }
}
