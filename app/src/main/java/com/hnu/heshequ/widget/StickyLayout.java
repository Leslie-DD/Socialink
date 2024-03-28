package com.hnu.heshequ.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ThreadUtils;

import java.util.NoSuchElementException;

import dagger.hilt.android.internal.ThreadUtil;

public class StickyLayout extends LinearLayout {
    private static final String TAG = "[StickyLayout]";
    private static final boolean DEBUG = true;

    private View mHeader;
    private View mContent;
    private OnGiveUpTouchEventListener mGiveUpTouchEventListener;

    /**
     * header 的高度（单位：px）
     */
    private int mOriginalHeaderHeight;
    private int mHeaderHeight;

    private int mStatus = STATUS_EXPANDED;
    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;

    private int mTouchSlop;

    /**
     * 分别记录上次滑动的坐标
     */
    private int mLastX = 0;
    private int mLastY = 0;

    /**
     * 分别记录上次滑动的坐标 (onInterceptTouchEvent)
     */
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    /**
     * 用来控制滑动角度，仅当角度a满足如下条件才进行滑动：tan a = deltaX / deltaY > 2
     */
    private static final int TAN = 2;

    private boolean mIsSticky = true;
    private boolean mInitDataSucceed = false;
    private boolean mDisallowInterceptTouchEventOnHeader = true;

    private int headerId;
    private int contentId;

    public StickyLayout(Context context) {
        super(context);
    }

    public StickyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        super.onWindowFocusChanged(hasWindowFocus);
//        if (DEBUG) {
//            Log.i(TAG, "(onWindowFocusChanged) hasWindowFocus = " + hasWindowFocus + ", mHeader = " + mHeader + ", mContent =" +
//                    " " + mContent);
//        }
//        if (hasWindowFocus && (mHeader == null || mContent == null)) {
//            initData();
//        }
//    }

    public void setHeaderAndContentId(int headerId, int contentId) {
        this.headerId = headerId;
        this.contentId = contentId;
    }

    public void initData() {
        RuntimeException re = new RuntimeException();
        re.fillInStackTrace();
        if (DEBUG) {
            Log.i(TAG, "(initData)", re);
        }
        if (headerId == 0 || contentId == 0) {
            throw new NoSuchElementException("Did your view with id \"sticky_header\" or \"sticky_content\" exists?");
        }
        mHeader = findViewById(headerId);
        mContent = findViewById(contentId);
        mOriginalHeaderHeight = mHeader.getMeasuredHeight();
        mHeaderHeight = mOriginalHeaderHeight;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        if (mHeaderHeight > 0) {
            mInitDataSucceed = true;
        }
        if (DEBUG) {
            Log.d(TAG + this.hashCode(), "(initData) mTouchSlop = " + mTouchSlop + ", mHeaderHeight = " + mHeaderHeight);
        }
    }

    public void setOnGiveUpTouchEventListener(OnGiveUpTouchEventListener l) {
        mGiveUpTouchEventListener = l;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int intercepted = 0;

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastXIntercept = x;
                mLastYIntercept = y;
                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (mDisallowInterceptTouchEventOnHeader && y <= getHeaderHeight()) {
                    intercepted = 0;
                } else if (Math.abs(deltaY) <= Math.abs(deltaX)) {
                    intercepted = 0;
                } else if (mStatus == STATUS_EXPANDED && deltaY <= -mTouchSlop) {
                    intercepted = 1;
                } else if (mGiveUpTouchEventListener != null && mGiveUpTouchEventListener.giveUpTouchEvent(event) && deltaY >= mTouchSlop) {
                    intercepted = 1;
                }
                break;

            case MotionEvent.ACTION_UP:
                mLastXIntercept = mLastYIntercept = 0;
                break;

            default:
                break;
        }

        if (DEBUG) {
            Log.d(TAG, " y = " + y + ", headerHeight = " + mHeaderHeight + ", " +  "intercepted = " + intercepted + " event" +
                    ".action = " + event.getAction());
        }
        return intercepted != 0 && mIsSticky;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsSticky) {
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (DEBUG) {
                    Log.d(TAG, "mHeaderHeight=" + mHeaderHeight + "  deltaY=" + deltaY + "  mLastY=" + mLastY);
                }
                mHeaderHeight += deltaY;
                setHeaderHeight(mHeaderHeight);
                break;

            case MotionEvent.ACTION_UP:
                // 这里做了下判断，当松开手的时候，会自动向两边滑动，具体向哪边滑，要看当前所处的位置
                int destHeight = 0;
                if (mHeaderHeight <= mOriginalHeaderHeight * 0.5) {
                    mStatus = STATUS_COLLAPSED;
                } else {
                    destHeight = mOriginalHeaderHeight;
                    mStatus = STATUS_EXPANDED;
                }
                // 慢慢滑向终点
                this.smoothSetHeaderHeight(mHeaderHeight, destHeight, 200);
                break;

            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    public void smoothSetHeaderHeight(final int from, final int to, long duration) {
        smoothSetHeaderHeight(from, to, duration, false);
    }

    private static final int FRAME_RATE = 100;
    private static final int FRAME_INTERVAL = 1000 / FRAME_RATE;

    public void smoothSetHeaderHeight(final int from, final int to, long duration, final boolean modifyOriginalHeaderHeight) {

        if (DEBUG) {
            Log.d(TAG + this.hashCode(), "(smoothSetHeaderHeight) from = " + from + " to = " + to);
        }
        final int frameCount = (int) (duration / FRAME_INTERVAL);
        final float partition = (to - from) / (float) frameCount;
        // TODO: opt this thread
        new Thread("Thread#smoothSetHeaderHeight") {
            @Override
            public void run() {
                for (int i = 0; i < frameCount; i++) {
                    final int height;
                    if (i == frameCount - 1) {
                        height = to;
                    } else {
                        height = (int) (from + partition * i);
                    }
                    postDelayed(() -> setHeaderHeight(height), (long) FRAME_INTERVAL * i);
                }

//                if (modifyOriginalHeaderHeight) {
//                    setOriginalHeaderHeight(to);
//                }
            }
        }.start();
    }

//    public void setOriginalHeaderHeight(int originalHeaderHeight) {
//        mOriginalHeaderHeight = originalHeaderHeight;
//    }

//    public void setHeaderHeight(int height, boolean modifyOriginalHeaderHeight) {
//        if (modifyOriginalHeaderHeight) {
//            setOriginalHeaderHeight(height);
//        }
//        setHeaderHeight(height);
//    }

    public void setHeaderHeight(int height) {
        if (DEBUG) {
            Log.d(TAG + this.hashCode(), "(setHeaderHeight) height = " + height);
        }
        if (!mInitDataSucceed) {
            initData();
        }

        if (DEBUG) {
            Log.d(TAG, "setHeaderHeight height=" + height);
        }
        if (height <= 0) {
            height = 0;
        } else if (height > mOriginalHeaderHeight) {
            height = mOriginalHeaderHeight;
        }

        if (height == 0) {
            mStatus = STATUS_COLLAPSED;
        } else {
            mStatus = STATUS_EXPANDED;
        }

        if (mHeader != null && mHeader.getLayoutParams() != null) {
            mHeader.getLayoutParams().height = height;
            mHeader.requestLayout();
            mHeaderHeight = height;
        } else {
            if (DEBUG) {
                Log.e(TAG, "null LayoutParams when setHeaderHeight");
            }
        }
    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public void setSticky(boolean isSticky) {
        mIsSticky = isSticky;
    }

    public void requestDisallowInterceptTouchEventOnHeader(boolean disallowIntercept) {
        mDisallowInterceptTouchEventOnHeader = disallowIntercept;
    }

    public interface OnGiveUpTouchEventListener {
        boolean giveUpTouchEvent(MotionEvent event);
    }
}