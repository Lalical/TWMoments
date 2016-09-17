package com.widgets.recycleviewwrap;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalic.twmoments.R;
import com.momentslist.TweetsBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews = new SparseArray<View>();
    private View mRootView;

    /**
     * @param itemView itemView
     */
    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        this.mRootView = itemView;
    }

    /**
     * 返回对应view
     *
     * @param resId view resource Id
     * @param <T>   返回view的类型
     * @return 返回View
     */
    public <T extends View> T getView(@IdRes int resId) {
        View view = mViews.get(resId);
        if (view == null) {
            view = mRootView.findViewById(resId);
            mViews.put(resId, view);
        }
        return (T) view;
    }

    /**
     * 给ImageView设置背景
     *
     * @param imgViewResId ImageView对应资源ID
     * @param imgResId     图片资源ID
     */
    public void setImageView(@IdRes int imgViewResId, @DrawableRes int imgResId) {
        ImageView view = getView(imgViewResId);
        view.setBackgroundResource(imgResId);
    }

    /**
     * 设置View的可见性
     *
     * @param resId   View的Resource ID
     * @param visible 是否可见
     */
    public void setVisible(@IdRes int resId, @VisibleType int visible) {
        View view = getView(resId);
        view.setVisibility(visible);
    }

    /**
     * 设置字体颜色
     *
     * @param resId     TextView ResId
     * @param textColor 字体颜色
     */
    public void setTextColor(@IdRes int resId, int textColor) {
        TextView textView = getView(resId);
        textView.setTextColor(textColor);
    }

    /**
     * 设置View的选中状态
     *
     * @param resId    View的Resource ID
     * @param selected 是不选中，如果为true, 则选中
     */
    public void setViewSelected(@IdRes int resId, boolean selected) {
        View view = getView(resId);
        view.setSelected(selected);
    }

    /**
     * 设置View的背景颜色
     *
     * @param resId View的Resource ID
     * @param color 颜色值
     */
    public void setViewBackground(@IdRes int resId, int color) {
        View view = getView(resId);
        view.setBackgroundColor(color);
    }

    /**
     * 设置文字
     *
     * @param resId TextView ResId
     * @param text  文字内容
     */
    public void setText(@IdRes int resId, String text) {
        TextView view = getView(resId);
        view.setText(text);
    }

    /**
     * 设置文字
     */
    public void setText(@IdRes int resId, Spanned text) {
        TextView view = getView(resId);
        view.setText(text);
    }

    /**
     * 设置文字
     *
     * @param resId    TextView ResId
     * @param txtResId String res id
     */
    public void setText(@IdRes int resId, @StringRes int txtResId) {
        TextView view = getView(resId);
        view.setText(txtResId);
    }

    /**
     * 设置View的点击事件
     *
     * @param resId           View的Resource ID
     * @param onClickListener 点击事件监听器
     */
    public void setViewOnClickListener(@IdRes int resId, View.OnClickListener onClickListener) {
        View view = getView(resId);
        view.setOnClickListener(onClickListener);
    }

    /**
     * 给整个Item设置点击事件
     *
     * @param onItemClickListener onClickListener
     */
    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.mRootView.setOnClickListener(onItemClickListener);
    }

    /**
     * 取每一个Item的View
     *
     * @return 返回整个Item
     */
    public View getItemView() {
        return mRootView;
    }


    /**
     * 添加评论列表
     *
     * @param comments_list_rsid
     * @param comments
     */
    public void setComments(int comments_list_rsid, List<TweetsBean.Comments> comments) {
        TextView view = getView(comments_list_rsid);
        String htmlFormat = processData(comments);
        if (TextUtils.isEmpty(htmlFormat)) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(Html.fromHtml(htmlFormat));
        }
    }

    private String processData(List<TweetsBean.Comments> comments) {
        StringBuilder ret = new StringBuilder();

        for (TweetsBean.Comments com : comments) {
            ret.append("<font color=#7777ff>" + com.getSender().getNick() + ": </font>" + com.getContent() + "<br/>");
        }
        if (ret.length() > 0) {
            ret.delete(ret.length() - 5, ret.length());
        }
        return ret.toString();
    }



    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface VisibleType {
    }

}
