/**
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
package com.example.heshequ.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author dev06
 *         2016年6月17日
 */
public class MyLv extends ListView {
    public MyLv(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
