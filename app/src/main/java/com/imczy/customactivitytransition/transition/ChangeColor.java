/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imczy.customactivitytransition.transition;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

public class ChangeColor extends Transition {
    private static final String TAG = "ChangeColor";

    private static final String PROPNAME_BACKGROUND = "customtransition:change_color:backgroundcolor";


    int mStartColor;
    int mEndColor;

    public ChangeColor(int startColor, int endColor) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
    }

    private void captureValues(TransitionValues values) {
        values.values.put(PROPNAME_BACKGROUND, values.view.getBackground());
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
        transitionValues.values.put(PROPNAME_BACKGROUND, mStartColor);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_BACKGROUND, mEndColor);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot,
                                   TransitionValues startValues, TransitionValues endValues) {
        if (null == startValues || null == endValues) {
            return null;
        }
        final View view = endValues.view;

        int startColor = (int) startValues.values.get(PROPNAME_BACKGROUND);
        int endColor = (int) endValues.values.get(PROPNAME_BACKGROUND);

        if (startColor != endColor) {
            ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Object value = animation.getAnimatedValue();
                    if (null != value) {
                        view.setBackgroundColor((Integer) value);
                    }
                }
            });
            return animator;
        }
        return null;
    }
}
