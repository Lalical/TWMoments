package com.widgets.recycleviewwrap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by knero on 7/17/15.
 *
 * @param <T>
 */
public abstract class BaseRecyclerAdapter<T extends BaseRecyclerItem> extends
        RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<T> mData;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ParentRefreshListener mParentListener;

    public static interface ParentRefreshListener {

        public void parentDataSetChanged();
    }

    public void setParentListener(ParentRefreshListener parentListener) {
        this.mParentListener = parentListener;
    }

    /**
     * @param context context
     */
    public BaseRecyclerAdapter(Context context) {
        super();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = new ArrayList<T>();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("TAG", "onCreateViewHolder - viewType:" + viewType);
        View view = mLayoutInflater.inflate(getItemLayoutRes(viewType), parent, false);
        return new BaseRecyclerViewHolder(view);
    }

    /**
     * 获取某一个Item对应的的布局文件
     *
     * @param viewType item type
     * @return 返回布局文件Id
     */
    protected abstract int getItemLayoutRes(int viewType);

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder:" + holder + "  " + position);
        convert(holder, getData(position), position);
    }

    /**
     * @param holder   viewholder
     * @param item     item
     * @param position position
     */
    public abstract void convert(BaseRecyclerViewHolder holder, T item, int position);

    /**
     * 获取某个Position的数据
     *
     * @param position 所在下标
     * @return 返回数据
     */
    protected T getData(int position) {
        if (position < 0 || position >= mData.size()) {
            return null;
        }
        return mData.get(position);
    }

    /**
     * 添加一个数据项
     *
     * @param data data
     */
    public void addItem(T data) {
        if (data != null) {
            mData.add(data);
            if (mParentListener != null) {
                mParentListener.parentDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void addItemAtPos(int pos, T data) {
        if (data != null) {
            mData.add(pos, data);
            if (mParentListener != null) {
                mParentListener.parentDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 添加数据
     *
     * @param datas 要添加的数据据
     */
    public void addItems(Collection<T> datas) {
        if (datas != null) {
            mData.addAll(datas);
            if (mParentListener != null) {
                mParentListener.parentDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 更新数据
     *
     * @param datas 更新后的数据
     */
    public void updateItems(Collection<T> datas) {
        mData.clear();
        if (datas != null) {
            mData.addAll(datas);
            if (mParentListener != null) {
                mParentListener.parentDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 删除数据
     *
     * @pos 需要删除pos
     */
    public void deleteItem(int pos) {
        if (pos >= 0 && pos < mData.size()) {
            mData.remove(pos);
//          notifyItemRemoved(pos);
            if (mParentListener != null) {
                mParentListener.parentDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void replaceItem(int pos, T data) {
        if (pos >= 0 && pos < mData.size() && data != null) {
            mData.set(pos, data);
            if (mParentListener != null) {
                mParentListener.parentDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void notifyParentDataSetChanged() {
        if (mParentListener != null) {
            mParentListener.parentDataSetChanged();
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getDataType();
    }

    /**
     * 获取当前显示所有的数据
     *
     * @return 数据
     */
    public List<T> getAllData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public Context getContext() {
        return mContext;
    }
}

