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

        // 反射调用
        relfectInvoke(view,
                startRect.left,
                startRect.top,
                startRect.right,
                startRect.bottom
        );

        Animator reveal = createAnimator(view, startRadius, endRadius);
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
        return new NoPauseAnimator(reveal);
    }


    private void relfectInvoke(View view, int left, int top, int right, int bottom) {
        Class clazz = view.getClass();
        try {
            Method m1 = clazz.getMethod("setLeftTopRightBottom", new Class[]{int.class, int.class, int.class, int.class});
            m1.invoke(view, left, top, right, bottom);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static class NoPauseAnimator extends Animator {
        private final Animator mAnimator;
        private final ArrayMap<AnimatorListener, AnimatorListener> mListeners =
                new ArrayMap<AnimatorListener, AnimatorListener>();

        public NoPauseAnimator(Animator animator) {
            mAnimator = animator;
        }

        @Override
        public void addListener(AnimatorListener listener) {
            AnimatorListener wrapper = new AnimatorListenerWrapper(this, listener);
            if (!mListeners.containsKey(listener)) {
                mListeners.put(listener, wrapper);
                mAnimator.addListener(wrapper);
            }
        }

        @Override
        public void cancel() {
            mAnimator.cancel();
        }

        @Override
        public void end() {
            mAnimator.end();
        }

        @Override
        public long getDuration() {
            return mAnimator.getDuration();
        }

        @Override
        public TimeInterpolator getInterpolator() {
            return mAnimator.getInterpolator();
        }

        @Override
        public ArrayList<AnimatorListener> getListeners() {
            return new ArrayList<AnimatorListener>(mListeners.keySet());
        }

        @Override
        public long getStartDelay() {
            return mAnimator.getStartDelay();
        }

        @Override
        public boolean isPaused() {
            return mAnimator.isPaused();
        }

        @Override
        public boolean isRunning() {
            return mAnimator.isRunning();
        }

        @Override
        public boolean isStarted() {
            return mAnimator.isStarted();
        }

        @Override
        public void removeAllListeners() {
            mListeners.clear();
            mAnimator.removeAllListeners();
        }

        @Override
        public void removeListener(AnimatorListener listener) {
            AnimatorListener wrapper = mListeners.get(listener);
            if (wrapper != null) {
                mListeners.remove(listener);
                mAnimator.removeListener(wrapper);
            }
        }

        /* We don't want to override pause or resume methods because we don't want them
         * to affect mAnimator.
        public void pause();

        public void resume();

        public void addPauseListener(AnimatorPauseListener listener);

        public void removePauseListener(AnimatorPauseListener listener);
        */

        @Override
        public Animator setDuration(long durationMS) {
            mAnimator.setDuration(durationMS);
            return this;
        }

        @Override
        public void setInterpolator(TimeInterpolator timeInterpolator) {
            mAnimator.setInterpolator(timeInterpolator);
        }

        @Override
        public void setStartDelay(long delayMS) {
            mAnimator.setStartDelay(delayMS);
        }

        @Override
        public void setTarget(Object target) {
            mAnimator.setTarget(target);
        }

        @Override
        public void setupEndValues() {
            mAnimator.setupEndValues();
        }

        @Override
        public void setupStartValues() {
            mAnimator.setupStartValues();
        }

        @Override
        public void start() {
            mAnimator.start();
        }
    }

    public static class AnimatorListenerWrapper implements Animator.AnimatorListener {
        private final Animator mAnimator;
        private final Animator.AnimatorListener mListener;

        public AnimatorListenerWrapper(Animator animator, Animator.AnimatorListener listener) {
            mAnimator = animator;
            mListener = listener;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            mListener.onAnimationStart(mAnimator);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mListener.onAnimationEnd(mAnimator);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mListener.onAnimationCancel(mAnimator);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            mListener.onAnimationRepeat(mAnimator);
        }
    }
}
