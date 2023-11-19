package com.example.heshequ.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

/**
 * 可拖动的ImageView
 * Created by Dengdongqi on 2018/4/20.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class DragImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TAG = "DragImageView";
    private int sx;
    private int sy;
    private int fristX;
    private int fristY;
    private int endX;
    private int endY;

    public DragImageView(Context context) {
        super(context);
    }

    public DragImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                sx = (int) event.getRawX();
                sy = (int) event.getRawY();
                // 获取第一次接触所在位置
                fristX = sx;
                fristY = sy;
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                // 获取手指移动的距离
                int dx = x - sx;
                int dy = y - sy;
                // 得到View最开始的各顶点的坐标
                int l = this.getLeft();
                int r = this.getRight();
                int t = this.getTop();
                int b = this.getBottom();
                // 更改View在窗体的位置
                this.layout(l + dx, t + dy, r + dx, b + dy);
                // 获取移动后的位置
                sx = (int) event.getRawX();
                sy = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                // 获取最后View在窗体的位置
                endX = (int) event.getRawX();
                endY = (int) event.getRawY();
                Log.e(TAG, "lasty:" + endX + " ; " + "lastx:" + endY);
                //移动范围大于5px则屏蔽点击事件
                if (Math.abs(fristX - endX) > 5 || Math.abs(fristY - endY) > 5) {
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
