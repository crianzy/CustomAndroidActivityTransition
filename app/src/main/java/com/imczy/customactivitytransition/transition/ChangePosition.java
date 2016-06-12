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
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.PathMotion;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

public class ChangePosition extends Transition {
    private static final String TAG = "ChangePosition";

    private static final String PROPNAME_POSITION = "custom_position:change_position:position";


    public ChangePosition() {
        setPathMotion(new PathMotion() {
            @Override
            public Path getPath(float startX, float startY, float endX, float endY) {
                Path path = new Path();
                path.moveTo(startX, startY);

                float controlPointX = (startX + endX) / 3;
                float controlPointY = (startY + endY) / 2;

                path.quadTo(controlPointX, controlPointY, endX, endY);
                return path;
            }
        });
    }

    private void captureValues(TransitionValues values) {
        values.values.put(PROPNAME_POSITION, values.view.getBackground());

        Rect rect = new Rect();
        values.view.getGlobalVisibleRect(rect);
        values.values.put(PROPNAME_POSITION, rect);
    }


    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot,
                                   TransitionValues startValues, TransitionValues endValues) {
        if (null == startValues || null == endValues) {
            return null;
        }

        if (startValues.view.getId() > 0) {
            Rect startRect = (Rect) startValues.values.get(PROPNAME_POSITION);
            Rect endRect = (Rect) endValues.values.get(PROPNAME_POSITION);

            final View view = endValues.view;

            Path changePosPath = getPathMotion().getPath(startRect.centerX(), startRect.centerY(), endRect.centerX(), endRect.centerY());

            int radius = startRect.centerY() - endRect.centerY();

            ObjectAnimator objectAnimator = ObjectAnimator.ofObject(view, new PropPosition(PointF.class, "position", new PointF(endRect.centerX(), endRect.centerY())), null, changePosPath);
            objectAnimator.setInterpolator(new FastOutSlowInInterpolator());

            return objectAnimator;
        }
        return null;

    }

    static class PropPosition extends Property<View, PointF> {

        public PropPosition(Class<PointF> type, String name) {
            super(type, name);
        }

        public PropPosition(Class<PointF> type, String name, PointF startPos) {
            super(type, name);
            this.startPos = startPos;
        }

        PointF startPos;

        @Override
        public void set(View view, PointF topLeft) {

            int x = Math.round(topLeft.x);
            int y = Math.round(topLeft.y);

            int startX = Math.round(startPos.x);
            int startY = Math.round(startPos.y);

            int transY = y - startY;
            int transX = x - startX;

            view.setTranslationX(transX);
            view.setTranslationY(transY);
        }

        @Override
        public PointF get(View object) {
            return null;
        }
    }

}
