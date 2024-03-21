package com.hnu.heshequ.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hnu.heshequ.utils.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Created by dev06 on 2018/6/8.
 */
public class MyXRecyclerView extends XRecyclerView {
    boolean canLoad;
    private int startY, endY;
    private Context mContext;

    public MyXRecyclerView(Context context) {
        super(context);
        mContext = context;
    }

    public MyXRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MyXRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
//    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                canLoad = false;
                break;
            case MotionEvent.ACTION_UP:
                endY = (int) ev.getY();

                LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
                int lastPisiton = manager.findLastVisibleItemPosition();
                View view = manager.findViewByPosition(lastPisiton);

                if ((startY - endY) > Utils.dip2px(mContext, 80) && lastPisiton >= getAdapter().getItemCount()) {
                    canLoad = true;
                } else {
                    canLoad = false;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isCanLoad() {
        return canLoad;
    }

    @Override
    public void setLoadingListener(LoadingListener listener) {
        super.setLoadingListener(listener);
    }

    public void setCanLoad(boolean canLoad) {
        this.canLoad = canLoad;
    }
}
