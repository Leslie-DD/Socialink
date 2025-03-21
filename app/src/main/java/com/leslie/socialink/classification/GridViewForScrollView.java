package com.leslie.socialink.classification;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * FileName: ClassificationBean
 * Author: Ding Yifan
 * Data: 2020/9/1
 * Time: 23:02
 * Description:
 */

public class GridViewForScrollView extends GridView {

    public GridViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewForScrollView(Context context) {
        super(context);
    }

    public GridViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec + 30);

    }


}
