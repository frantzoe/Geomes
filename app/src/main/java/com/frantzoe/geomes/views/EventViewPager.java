package com.frantzoe.geomes.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class EventViewPager extends ViewPager {

    private boolean isSwipable = true;

    public EventViewPager(@NonNull Context context) {
        super(context);
        enableSmoothScroll();
    }

    public EventViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        enableSmoothScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.isSwipable) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.isSwipable) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setSwipable(boolean swipable) {
        this.isSwipable = swipable;
    }

    public void enableSmoothScroll() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new EventScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class EventScroller extends Scroller {

        private EventScroller(Context context) {
            super(context);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 360);
        }
    }
}
