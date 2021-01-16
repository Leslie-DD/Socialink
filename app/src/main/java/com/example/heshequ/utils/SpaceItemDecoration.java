package com.example.heshequ.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dev06 on 2018/5/15.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace ;
    int style;

    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(Context context,int space) {
        this.mSpace = Utils.dip2px(context,space);
    }
    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(Context context,int space,int style) {
        this.mSpace = Utils.dip2px(context,space);
        this.style=style;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();
        int pos = parent.getChildAdapterPosition(view);
        outRect.left = 0;
        outRect.top = 0;
        if(style==0)
        {
            outRect.bottom = 0;
            if (pos != (itemCount -1)) {
                outRect.right = mSpace;
            } else {
                outRect.right = 0;
            }
        }else {
            outRect.right = 0;
            if (pos != (itemCount -1)) {
                outRect.bottom = mSpace;
            } else {
                outRect.bottom = 0;
            }
        }


    }


    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state,boolean isVer) {
        int itemCount = parent.getAdapter().getItemCount();
        int pos = parent.getChildAdapterPosition(view);
        outRect.left = 0;
        outRect.top = 0;
        outRect.right = 0;
        if (pos != (itemCount -1)) {
            outRect.bottom = mSpace;
        } else {
            outRect.bottom = 0;
        }
    }
}