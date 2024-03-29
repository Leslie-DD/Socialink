package com.hnu.heshequ.widget;


import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * 圆角ViewOutlineProvider
 */
public class RoundViewOutlineProvider extends ViewOutlineProvider {
    private final int roundSize;

    public RoundViewOutlineProvider(int roundSize) {
        this.roundSize = roundSize;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), roundSize);
    }
}