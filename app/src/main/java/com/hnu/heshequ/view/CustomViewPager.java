package com.hnu.heshequ.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.Map;

public class CustomViewPager extends ViewPager {
    private final Map<Integer, Integer> map = new HashMap<>(4);
    private int currentPage;
    private boolean noScroll = true;

    public void setCanScroll(boolean canScroll) {
        this.noScroll = !canScroll;
    }

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            map.put(i, h);
            if (h > height) {
                height = h;
            }
            if (map.containsKey(currentPage)) {
                height = map.get(currentPage);
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current) {
        currentPage = current;
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        if (!map.isEmpty() && map.containsKey(currentPage)) {
            if (current == 0) {
                if (params == null) {
                    params = new MarginLayoutParams(LayoutParams.MATCH_PARENT, map.get(current));
                } else {
                    params.height = map.get(current);
                }
            } else {
                if (params == null) {
                    params = new MarginLayoutParams(LayoutParams.MATCH_PARENT, map.get(current));
                } else {
                    params.height = map.get(current);
                }
            }
            setLayoutParams(params);
        }
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return !noScroll && super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        //false 去除滚动效果
        super.setCurrentItem(item, false);
    }
}
