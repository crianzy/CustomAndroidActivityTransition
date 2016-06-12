package com.imczy.customactivitytransition.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.graphics.Rect;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by chenzhiyong on 16/6/7.
 */
public class ShareElemReturnRevealTransition extends Transition {
    private static final String TAG = "ShareElemReturnRevealTransition";

    private static final String PROPNAME_BACKGROUND = "custom_reveal:change_radius:radius";

    private boolean hasAnim = false;

    private View animView;

    private Rect startRect;
    private Rect endRect;


    public ShareElemReturnRevealTransition(View animView) {
        this.animView = animView;
        startRect = new Rect();
        endRect = new Rect();
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        float widthSquared = view.getWidth() * view.getWidth();
        float heightSquared = view.getHeight() * view.getHeight();
        int radius = (int) Math.sqrt(widthSquared + heightSquared) / 2;

        transitionValues.values.put(PROPNAME_BACKGROUND, radius);
        transitionValues.view.getGlobalVisibleRect(startRect);

    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        transitionValues.view.getLocalVisibleRect(endRect);

        transitionValues.values.put(PROPNAME_BACKGROUND, transitionValues.view.getWidth() / 2);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, final TransitionValues endValues) {

        if (null == startValues || null == endValues) {
            return null;
        }

        final View view = endValues.view;
        int startRadius = (int) startValues.values.get(PROPNAME_BACKGROUND);
        int endRadius = (int) endValues.values.get(PROPNAME_BACKGROUND);

        // 在执行返回动画的时候,  View 默认的被控制 为 前一个页面的 ShareElem 的打消了
        // 所以这里 需要改变的 View 大小 才能 正常的使用揭露动画
        // 反射调用
        relfectInvoke(view,
                startRect.left,
                startRect.top,
                startRect.right,
                startRect.bottom
        );

        Animator reveal = createAnimator(view, startRadius, endRadius);

        // 在动画的最后 被我们放大后的 View 会闪一些  这里可以有防止那种情况发生
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setClipBounds(new Rect(0, 0, 1, 1));
                view.setVisibility(View.GONE);
            }
        });
        return reveal;

    }

    private Animator createAnimator(View view, float startRadius, float endRadius) {
        int centerX = view.getWidth() / 2;
        int centerY = view.getHeight() / 2;

        Animator reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
                startRadius, endRadius);
        return new ShareElemEnterRevealTransition.NoPauseAnimator(reveal);
    }

    // setLeftTopRightBottom 需要反射执行, 该方法能够控制 View 的大小以及位置 在 ChangeBounds 类中有调用
    private void relfectInvoke(View view, int left, int top, int right, int bottom) {
        Class clazz = view.getClass();
        try {
            Method m1 = clazz.getMethod("setLeftTopRightBottom", new Class[]{int.class, int.class, int.class, int.class});
            m1.invoke(view, left, top, right, bottom);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
