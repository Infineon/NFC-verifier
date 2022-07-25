/*
 * Copyright 2022 Infineon Technologies AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.infineon.nfcbpsk.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

/**
 * Custom full drawer layout to manage the navigation drawer width as full screen
 */
public class FullDrawerLayout extends DrawerLayout {
    /**
     * Minimum constant value for full drawer layout margin
     */
    private static final int MIN_DRAWER_MARGIN = 0; // dp

    /**
     * Constructor
     * @param context Context of the application
     */
    public FullDrawerLayout(Context context) {
        super(context);
    }

    /**
     * Constructor
     * @param context Context of the application
     * @param attrs Attribute set
     */
    public FullDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor
     * @param context Context of the application
     * @param attrs Attribute set
     * @param defStyle Default style of drawer layout
     */
    public FullDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Method to get drawer sliding direction in string format
     * @param gravity Constant to direction of drawer sliding
     * @return Text representation of gravity of drawer view
     */
    static String gravityToString(int gravity) {
        if ((gravity & Gravity.START) == Gravity.START) {
            return "LEFT";
        }
        if ((gravity & Gravity.END) == Gravity.END) {
            return "RIGHT";
        }
        return Integer.toHexString(gravity);
    }

    /**
     * Method to update the drawer layout size
     * @param widthMeasureSpec  Width of drawer layout
     * @param heightMeasureSpec Height of drawer layout
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalArgumentException(
                    "DrawerLayout must be measured with MeasureSpec.EXACTLY.");
        }

        setMeasuredDimension(widthSize, heightSize);

        // Gravity value for each drawer we've seen. Only one of each permitted.
        int foundDrawers = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (isContentView(child)) {
                // Content views get measured at exactly the layout's size.
                final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                        widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
                final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                        heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
                child.measure(contentWidthSpec, contentHeightSpec);
            } else if (isDrawerView(child)) {
                final int childGravity =
                        getDrawerViewGravity(child) & Gravity.HORIZONTAL_GRAVITY_MASK;
                final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                        MIN_DRAWER_MARGIN + lp.leftMargin + lp.rightMargin,
                        lp.width);
                final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                        lp.topMargin + lp.bottomMargin,
                        lp.height);
                child.measure(drawerWidthSpec, drawerHeightSpec);
            } else {
                throw new IllegalStateException("");
            }
        }
    }

    /**
     * Method to detect if view is content view
     * @param child View to be check
     * @return Flag indicating view is ContentView
     */
    boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }

    /**
     * Method to detect if view is drawer view
     * @param child View to be check
     * @return Flag indicating view is drawer view
     */
    boolean isDrawerView(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;

        final int absGravity = Gravity.getAbsoluteGravity(gravity,
                child.getLayoutDirection());
        return (absGravity & (Gravity.START | Gravity.END)) != 0;
    }

    /**
     * Method to get gravity of drawer view
     * @param drawerView drawer view
     * @return Gravity of drawer view
     */
    int getDrawerViewGravity(View drawerView) {
        final int gravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
        return Gravity.getAbsoluteGravity(gravity, drawerView.getLayoutDirection());
    }
}