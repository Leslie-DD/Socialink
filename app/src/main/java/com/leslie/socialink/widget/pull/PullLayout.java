package com.leslie.socialink.widget.pull;

import static com.leslie.socialink.widget.pull.SmoothScrollHandler.MSG_SCHEDULE_HEADER_SCROLL;
import static com.leslie.socialink.widget.pull.SmoothScrollHandler.MSG_SCHEDULE_PULL_DOWN_SCROLL;
import static com.leslie.socialink.widget.pull.SmoothScrollHandler.MSG_SCHEDULE_PULL_UP_SCROLL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.leslie.socialink.utils.Utils;

import java.util.NoSuchElementException;

public class PullLayout extends LinearLayout {
    private static final String TAG = "[PullLayout]";

    private static final boolean DEBUG = false;

    public static final int STATUS_HEADER_EXPANDED = 1;
    public static final int STATUS_HEADER_EXPANDING = 2;
    public static final int STATUS_HEADER_COLLAPSED = 3;
    public static final int STATUS_HEADER_COLLAPSING = 4;

    private int mStatus = STATUS_HEADER_EXPANDED;

    private boolean supportPullUp = false;

    private View mPullDown;
    private View mHeader;
    private View mContent;
    private View mPullUp;

    private int pullDownId;
    private int headerId;
    private int contentId;
    private int pullUpId;

    private boolean isHeaderViewInitCollapsed = false;

    private OnHeaderChangedListener mOnHeaderChangedListener;
    private OnGiveUpTouchEventListener mGiveUpTouchEventListener;

    private int mOriginalPullDownHeight;
    private int mOriginalHeaderHeight;
    private int mOriginalPullUpHeight;

    private int mPullDownHeight;
    private int mHeaderHeight;
    private int mPullUpHeight;

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

    private boolean mInitDataSucceed = false;

    private Handler mHandler;

    public PullLayout(Context context) {
        super(context);
    }

    public PullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setViewId(int pullDownId, int headerId, int contentId, int pullUpId) {
        this.pullDownId = pullDownId;
        this.headerId = headerId;
        this.contentId = contentId;
        this.pullUpId = pullUpId;
    }

    public void init() {
        if (pullDownId == 0 || headerId == 0 || contentId == 0 || pullUpId == 0) {
            throw new NoSuchElementException("Did your header, content, pull down and pull up view exists?");
        }

        mPullDown = findViewById(pullDownId);
        mHeader = findViewById(headerId);
        mContent = findViewById(contentId);
        mPullUp = findViewById(pullUpId);

        mHandler = new SmoothScrollHandler(this, mPullDown, mPullUp);

        mOriginalPullDownHeight = mPullDown.getMeasuredHeight();
        mOriginalHeaderHeight = mHeader.getMeasuredHeight();
        mOriginalPullUpHeight = mPullUp.getMeasuredHeight();

        mPullDownHeight = mOriginalPullDownHeight;
        mHeaderHeight = isHeaderViewInitCollapsed ? 0 : mOriginalHeaderHeight;
        mPullUpHeight = mOriginalPullUpHeight;

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
//        if (mHeaderHeight > 0) {
            mInitDataSucceed = true;
//        }
        RuntimeException runtimeException = new RuntimeException();
        runtimeException.fillInStackTrace();
        printLog("(initData)", runtimeException);
        Log.d(TAG, "(initData) mTouchSlop = " + mTouchSlop +
                ", origin mHeaderHeight = " + mHeaderHeight +
                ", mPullDownHeight = " + mPullDownHeight +
                ", mPullUpHeight = " + mPullUpHeight);

    }

    private boolean lockPullUpGesture = true;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int intercepted = 0;
        lockPullUpGesture = true;

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
                if (y <= mHeaderHeight) {
                    intercepted = -2;
                } else if (Math.abs(deltaY) <= Math.abs(deltaX)) {
                    intercepted = -1;
                } else if (mStatus == STATUS_HEADER_EXPANDED && deltaY <= -mTouchSlop) {
                    intercepted = 2;
                } else if (mGiveUpTouchEventListener != null) {
                    if (supportPullUp && mStatus == STATUS_HEADER_COLLAPSED && mGiveUpTouchEventListener.giveUpTouchEventSinceBottomList(event)) {
                        lockPullUpGesture = false;
                        if (deltaY < 0) {
                            // 向下滑
                            intercepted = 3;
                        } else {
                            if (mPullUpHeight > 0) {
                                intercepted = 4;
                            } else {
                                intercepted = -3;
                            }
                        }
                    } else if (deltaY >= mTouchSlop && mGiveUpTouchEventListener.giveUpTouchEventSinceTopList(event)) {
                        intercepted = 5;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                mLastXIntercept = mLastYIntercept = 0;
                break;

            default:
                break;
        }

        printLog("mStatus = " + mStatus +
                ", y = " + y +
                ", headerHeight = " + mHeaderHeight +
                ", intercepted = " + intercepted +
                ", event.action = " + event.getAction());

        return intercepted > 0;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (mStatus == STATUS_HEADER_EXPANDED && (deltaY > 0 || mPullDownHeight > 0)) {
                    printLog("mPullDownHeight=" + mPullDownHeight + "  deltaY=" + deltaY + "  mLastY=" + mLastY);
                    setPullDownHeight(mPullDownHeight, deltaY);
                } else if (supportPullUp && mStatus == STATUS_HEADER_COLLAPSED && (deltaY < 0 || mPullUpHeight > 0)) {
                    if (!lockPullUpGesture) {
                        printLog("mPullUpHeight=" + mPullUpHeight + "  deltaY=" + deltaY + "  mLastY=" + mLastY);
                        setPullUpHeight(mPullUpHeight, deltaY);
                    }
                } else {
                    printLog("mHeaderHeight=" + mHeaderHeight + "  deltaY=" + deltaY + "  mLastY=" + mLastY);
                    setHeaderHeight(mHeaderHeight, deltaY);
                }
                break;

            case MotionEvent.ACTION_UP:
                // 松开手自动滑动
                if (mPullDownHeight > 0) {
                    smoothScrollView(MSG_SCHEDULE_PULL_DOWN_SCROLL, mPullDownHeight, 0, 200);
                } else if (mPullUpHeight > 0) {
                    if (supportPullUp) {
                        smoothScrollView(MSG_SCHEDULE_PULL_UP_SCROLL, mPullUpHeight, 0, 200);
                    }
                } else {
                    int destHeight = 0;
                    if (mHeaderHeight <= mOriginalHeaderHeight * 0.5) {
                        mStatus = STATUS_HEADER_COLLAPSED;
                    } else {
                        destHeight = mOriginalHeaderHeight;
                        mStatus = STATUS_HEADER_EXPANDED;
                    }
                    smoothScrollView(MSG_SCHEDULE_HEADER_SCROLL, mHeaderHeight, destHeight, 200);
                }
                break;

            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    public void smoothScrollView(int message, final int from, final int to, long duration) {
        printLog("(smoothScrollView) " + message + ", from = " + from + " to = " + to);
        if (mHandler != null) {
            mHandler.obtainMessage(message, from, to, duration).sendToTarget();
        }
    }

    public void setHeaderHeight(int lastHeight, int offset) {
        printLog("(setHeaderHeight) lastHeight = " + lastHeight + ", offset = " + offset + ", mOriginalHeaderHeight = " + mOriginalHeaderHeight);
        if (!mInitDataSucceed) {
            init();
        }

        int targetHeight = lastHeight + offset;

        if (targetHeight <= 0) {
            targetHeight = 0;
        } else if (targetHeight > mOriginalHeaderHeight) {
            targetHeight = mOriginalHeaderHeight;
        }

        if (targetHeight == 0) {
            if (mStatus != STATUS_HEADER_COLLAPSED && mOnHeaderChangedListener != null) {
                mOnHeaderChangedListener.onCollapsed();
            }
            mStatus = STATUS_HEADER_COLLAPSED;
        } else if (targetHeight > mHeaderHeight) {
            mStatus = STATUS_HEADER_EXPANDING;
        } else if (targetHeight < mHeaderHeight) {
            mStatus = STATUS_HEADER_COLLAPSING;
        }

        if (targetHeight == mOriginalHeaderHeight) {
            if (mStatus != STATUS_HEADER_EXPANDED && mOnHeaderChangedListener != null) {
                mOnHeaderChangedListener.onExpanded();
            }
            mStatus = STATUS_HEADER_EXPANDED;
        }

        printLog("(setHeaderHeight) height = " + targetHeight + ", status = " + mStatus);

        if (mHeader != null && mHeader.getLayoutParams() != null) {
            mHeader.getLayoutParams().height = targetHeight;
            mHeader.requestLayout();
            mHeaderHeight = targetHeight;
        } else {
            printLog("null LayoutParams when setHeaderHeight");
        }
    }

    public void setPullDownHeight(int lastHeight, int offset) {
        setPullDownHeight(lastHeight, offset, true);
    }

    public void setPullDownHeight(int lastHeight, int offset, boolean withDamping) {
        int pullDownOriginHeight = getPullDownOriginHeight();
        printLog("(setPullDownHeight) lastHeight = " + lastHeight + ", offset = " + offset + ", pullDownOriginHeight = " + pullDownOriginHeight);

        if (!mInitDataSucceed) {
            init();
        }

        if (withDamping && lastHeight < pullDownOriginHeight) {
            offset = offset * (pullDownOriginHeight - lastHeight) / pullDownOriginHeight;
        }
        int tarHeight = lastHeight + offset;

        if (tarHeight <= 0) {
            tarHeight = 0;
        } else if (tarHeight > pullDownOriginHeight) {
            tarHeight = pullDownOriginHeight;
        }
        printLog("(setPullDownHeight) tarHeight = " + tarHeight);

        if (mPullDown != null && mPullDown.getLayoutParams() != null) {
            mPullDown.getLayoutParams().height = tarHeight;
            mPullDown.requestLayout();
            mPullDownHeight = tarHeight;
        } else {
            printLog("(setPullDownHeight) LayoutParams null");
        }
    }

    public void setPullUpHeight(int lastHeight, int offset) {
        setPullUpHeight(lastHeight, offset, true);
    }

    public void setPullUpHeight(int lastHeight, int offset, boolean withDamping) {
        int pullUpOriginHeight = getPullUpOriginHeight();
        printLog("(setPullUpHeight) lastHeight = " + lastHeight + ", offset = " + offset + ", pullUpOriginHeight = " + pullUpOriginHeight);
        if (!mInitDataSucceed) {
            init();
        }

        if (withDamping && lastHeight < pullUpOriginHeight) {
            offset = offset * (pullUpOriginHeight - lastHeight) / pullUpOriginHeight;
        }
        int tarHeight = lastHeight - offset;

        if (tarHeight <= 0) {
            tarHeight = 0;
        } else if (tarHeight > pullUpOriginHeight) {
            tarHeight = pullUpOriginHeight;
        }
        printLog("(setPullUpHeight) tarHeight = " + tarHeight);

        if (mPullUp != null && mPullUp.getLayoutParams() != null) {
            mPullUp.getLayoutParams().height = tarHeight;
            mPullUp.requestLayout();
            mPullUpHeight = tarHeight;
        } else {
            printLog("(setPullUpHeight) LayoutParams null");
        }
    }

    public int getPullDownOriginHeight() {
        if (mOriginalPullDownHeight <= 0) {
            mOriginalPullDownHeight = Utils.dip2px(getContext(), 100);
        }
        return mOriginalPullDownHeight;
    }

    public int getPullUpOriginHeight() {
        if (mOriginalPullUpHeight <= 0) {
            mOriginalPullUpHeight = Utils.dip2px(getContext(), 100);
        }
        return mOriginalPullUpHeight;
    }

    private void printLog(@NonNull String message) {
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    private void printLog(@NonNull String message, @NonNull Throwable throwable) {
        if (DEBUG) {
            Log.d(TAG, message, throwable);
        }
    }

    @SuppressWarnings("unused")
    public void setSupportPullUp(boolean supportPullUp) {
        this.supportPullUp = supportPullUp;
    }

    public void setHeaderViewInitCollapsed(boolean collapsed) {
        isHeaderViewInitCollapsed = collapsed;
    }

    public void setOnHeaderChangedListener(OnHeaderChangedListener l) {
        mOnHeaderChangedListener = l;
    }

    public void setOnGiveUpTouchEventListener(OnGiveUpTouchEventListener l) {
        mGiveUpTouchEventListener = l;
    }


    public interface OnHeaderChangedListener {
        void onCollapsed();
        void onExpanded();
    }

    public interface OnGiveUpTouchEventListener {
        boolean giveUpTouchEventSinceTopList(MotionEvent event);
        boolean giveUpTouchEventSinceBottomList(MotionEvent event);
    }

}