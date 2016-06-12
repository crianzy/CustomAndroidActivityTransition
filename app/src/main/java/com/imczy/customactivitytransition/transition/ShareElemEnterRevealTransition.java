package com.imczy.customactivitytransition.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by chenzhiyong on 16/6/7.
 */
public class ShareElemEnterRevealTransition extends Transition {
    private static final String TAG = "ShareElemEnterRevealTransition";

    private static final String PROPNAME_BACKGROUND = "custom_reveal:change_radius:radius";

    private boolean hasAnim = false;

    private View animView;

    public ShareElemEnterRevealTransition(View animView) {
        this.animView = animView;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_BACKGROUND, transitionValues.view.getWidth() / 2);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        float widthSquared = view.getWidth() * view.getWidth();
        float heightSquared = view.getHeight() * view.getHeight();
        int radius = (int) Math.sqrt(widthSquared + heightSquared) / 2;
        transitionValues.values.put(PROPNAME_BACKGROUND, radius);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {

        if (null == startValues || null == endValues) {
            return null;
        }

//        Log.d(TAG, "createAnimator() called with: " + "sceneRoot = [" + sceneRoot + "], startValues = [" + startValues + "], endValues = [" + endValues + "]");

//        if (hasAnim) {
//            return null;
//        }

        final View view = endValues.view;
        int startRadius = (int) startValues.values.get(PROPNAME_BACKGROUND);
        int endRadius = (int) endValues.values.get(PROPNAME_BACKGROUND);

        if (view == animView) {
            Animator reveal = createAnimator(view, startRadius, endRadius);
            hasAnim = true;
            return reveal;
        }

        return null;
    }

    private Animator createAnimator(View view, float startRadius, float endRadius) {
        int centerX = view.getWidth() / 2;
        int centerY = view.getHeight() / 2;
        Animator reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
                startRadius, endRadius);
        return new NoPauseAnimator(reveal);
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
