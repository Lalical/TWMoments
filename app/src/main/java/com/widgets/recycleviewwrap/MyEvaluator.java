package com.widgets.recycleviewwrap;

import android.animation.TypeEvaluator;

/**
 * Created by liuyuwei on 2016/8/25.
 */
public class MyEvaluator implements TypeEvaluator<Float> {

    @Override
    public Float evaluate(float fraction, Float startValue, Float endValue) {
        return (endValue - startValue) * fraction + startValue;
    }
}
