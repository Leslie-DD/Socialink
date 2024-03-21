package com.example.heshequ.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.Map;


public class CustomViewPager extends ViewPager {
    private Map<Integer, Integer> map = new HashMap<>(4);
    private int currentPage;
    private int max;
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
            addHeight(i, h);
            if (h > height) height = h;
            max = height;
            if (map.size() > 0 && map.containsKey(currentPage)) {
                height = map.get(currentPage);
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        //Log.e("ysf","height"+height+"&&"+widthMeasureSpec+"&&"+heightMeasureSpec+"&&"+currentPage);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current) {
        // Log.e("YSF","我是resrtHeait"+current);
        this.currentPage = current;
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        if (map.size() > 0 && map.containsKey(currentPage)) {
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

    /**
     * 获取、存储每一个tab的高度，在需要的时候显示存储的高度
     *
     * @param current tab的position
     * @param height  当前tab的高度
     */
    public void addHeight(int current, int height) {
        map.put(current, height);
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
