package com.hnu.heshequ.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class CustomXRecyclerView extends XRecyclerView {
    public CustomXRecyclerView(Context context) {
        super(context);
    }

    public CustomXRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean atBottom = false;


    @Override
    public void onScrollStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();

        // 在滚动时检查是否滚动到底部
        if (layoutManager != null) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

            atBottom = pastVisibleItems + visibleItemCount >= totalItemCount;
        }
    }
}
