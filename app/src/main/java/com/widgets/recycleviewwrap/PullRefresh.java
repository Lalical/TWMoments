package com.widgets.recycleviewwrap;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.utils.ScreenUtils;


/**
 * Created by liuyuwei on 2016/8/25.
 * 下拉刷新控件，刷新状态暂时是一个红色小圆
 * <p/>
 * 内部可下拉控件需实现IAllowOutLayoutPullDown(必须)，和OnPullDownListener(可选)
 * OnPullDownListener实现中需调用notifyEnd结束刷新状态
 */
public class PullRefresh extends FrameLayout {

    private final int mTouchSlop;

    int mScreenHeight = ScreenUtils.getScreenHeight(getContext());
    int mScreenWidth = ScreenUtils.getScreenWidth(getContext());
    int mMaxPullHeight = mScreenHeight / 5;

    //测试用handler
    Handler mHandler;

    IAllowOutLayoutPullDown mIsAllow;
    OnPullDownListener onPullDown;


    public PullRefresh(Context context) {
        this(context, null);
    }

    public PullRefresh(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration) / 2;
        setWillNotDraw(false);
        mHandler = new Handler();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mFreshIconX = getMeasuredWidth() / 2;
    }

    int mFreshIconY;
    int mFreshIconX;

    /**
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mFreshIconX, mFreshIconY - 50, 50, p);
    }


    boolean intercept = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return intercept;
    }

    float mAcMove;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mAcMove = ev.getY();
                mAcDown = mAcMove;
                break;
            case MotionEvent.ACTION_MOVE:
                mAcMove = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        if (Math.abs(mAcMove - mAcDown) < mTouchSlop) {
            intercept = false;
            return super.dispatchTouchEvent(ev);
        }

        if (mIsAllow == null) {
            throw new RuntimeException("敢不敢告诉我能不能下拉？！");
        }
        if (mIsAllow.allowOutLayoutPullDown()) {
            //内部list到顶
            if (mAcMove - mAcDown > 0) {
                //下拉  拦截操作
                intercept = true;
            } else {
                intercept = false;
            }
        } else {
            //内部list没到顶
            intercept = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    float mAcDown;

    boolean mRefreshing = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mRefreshing) return true;
        float acMove;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mAcDown = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                acMove = ev.getY();
                mFreshIconY = getIconY((acMove - mAcDown));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mRefreshing = true;
                ValueAnimator va;
                if (mFreshIconY > 250) {
                    va = ValueAnimator.ofObject(new MyEvaluator(), Float.valueOf(mFreshIconY), Float.valueOf(250));
                    if (onPullDown != null) {
                        onPullDown.onPullDown();
                    }

                    //_________________测试用_______________________________
                    Message mess = Message.obtain(mHandler, new Runnable() {
                                @Override
                                public void run() {
                                    notifyEnd();
                                }
                            }
                    );
                    mHandler.sendMessageDelayed(mess, 1300);
                    //_________________测试用_______________________________

                } else {
                    va = ValueAnimator.ofObject(new MyEvaluator(), Float.valueOf(mFreshIconY), Float.valueOf(-50));
                    mRefreshing = false;
                }
                va.setDuration(300);
                va.setInterpolator(new LinearInterpolator());
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float f = (float) animation.getAnimatedValue();
                        mFreshIconY = (int) f;
                        invalidate();
                    }
                });
                va.start();
                break;
        }
        invalidate();
        return true;
    }

    public void notifyEnd() {
        ValueAnimator reset;
        reset = ValueAnimator.ofObject(new MyEvaluator(), Float.valueOf(mFreshIconY), Float.valueOf(-50));
        reset.setDuration(200);
        reset.setInterpolator(new LinearInterpolator());
        reset.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                mFreshIconY = (int) f;
                invalidate();
            }
        });
        reset.start();
        mRefreshing = false;
    }

    /**
     * 手指滑动距离和刷新状态小圆运动距离的关系
     * 暂用0.5倍代替
     * 根据屏幕、控件高度等参数重新设计可优化体验
     *
     * @param slidDistance
     * @return
     */
    private int getIconY(double slidDistance) {
        return (int) (0.5 * slidDistance);
    }

    public void setIsAllowPullDown(IAllowOutLayoutPullDown mIsAllow) {
        this.mIsAllow = mIsAllow;
    }

    public void setOnPullDown(OnPullDownListener onPullDown) {
        this.onPullDown = onPullDown;
    }

    public interface IAllowOutLayoutPullDown {
        boolean allowOutLayoutPullDown();
    }

    public interface OnPullDownListener {
        void onPullDown();
    }
}
