package com.example.heshequ.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dengdongqi on 2018/7/30.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class AllShowViewPager extends ViewPager {
    public AllShowViewPager(Context context) {
        super(context);
    }

    public AllShowViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    float xDown, yDown, xMove, yMove, offsetX, offsetY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getX();
                yDown = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = ev.getX();
                yMove = ev.getY();
                offsetX += ev.getX() - xDown;
                offsetY += ev.getY() - yDown;
                xDown = ev.getX();
                yDown = ev.getY();

                if (Math.abs(offsetX) > Math.abs(offsetY) || Math.abs(offsetX) == 0) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                offsetX = 0;
                offsetY = 0;
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
