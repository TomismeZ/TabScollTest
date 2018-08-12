package com.example.test.mytest.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
    private boolean mDisableSroll = false;
 
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public NoScrollViewPager(Context context) {
        super(context);
    }
 
    public void setmDisableSroll(boolean mDisableSroll) {
        this.mDisableSroll = mDisableSroll;
    }
    public boolean getmDisableSroll() {
       return mDisableSroll;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
 

 
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
 
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDisableSroll)
            return false;
        else
            return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mDisableSroll)
            return false;
        else
            return super.onTouchEvent(ev);
    }
}
