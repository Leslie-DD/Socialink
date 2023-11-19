package com.example.heshequ.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager2 extends ViewPager {

    public CustomViewPager2(Context context) {

        this(context, null);

    }


    public CustomViewPager2(Context context, AttributeSet attrs) {

        super(context, attrs);

    }


    @Override

    public boolean onInterceptTouchEvent(MotionEvent ev) {

        try {

            return super.onInterceptTouchEvent(ev);

        } catch (IllegalArgumentException e) {

            Log.i("TAG", "onInterceptTouchEvent: 多点触摸系统Bug");

        } catch (ArrayIndexOutOfBoundsException e) {

            Log.i("TAG", "onInterceptTouchEvent: 多点触摸系统Bug");

        }

        return false;

    }

}
