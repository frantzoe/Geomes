package com.frantzoe.geomes.helpers;


import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<View> {

    private int lastStartedType;
    private ValueAnimator offsetAnimator;
    private boolean isSnappingEnabled = true;

    public BottomNavigationBehavior() {
        super();
    }

    public BottomNavigationBehavior(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            this.updateSnackbar(child, (Snackbar.SnackbarLayout)dependency);
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    private void updateSnackbar(View child, Snackbar.SnackbarLayout snackbarLayout) {
        if(snackbarLayout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)snackbarLayout.getLayoutParams();
            params.setAnchorId(child.getId());
            params.anchorGravity = Gravity.CENTER_HORIZONTAL;
            params.gravity = Gravity.CENTER_HORIZONTAL;
            snackbarLayout.setLayoutParams(params);
        }
    }
}
