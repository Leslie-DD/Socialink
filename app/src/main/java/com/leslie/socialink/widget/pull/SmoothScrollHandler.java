package com.leslie.socialink.widget.pull;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.leslie.socialink.utils.WorkThread;

import java.lang.ref.WeakReference;

class SmoothScrollHandler extends Handler {
    private static final String TAG = "[SmoothScrollHandler]";

    public static final int MSG_SCHEDULE_HEADER_SCROLL = 1;
    public static final int MSG_SCHEDULE_PULL_DOWN_SCROLL = 2;
    public static final int MSG_SCHEDULE_PULL_UP_SCROLL = 3;

    public static final int FRAME_RATE = 100;
    public static final int FRAME_INTERVAL = 1000 / FRAME_RATE;

    private final WeakReference<PullLayout> stickyLayoutWeakReference;
    private final WeakReference<View> pullDownWeakReference;
    private final WeakReference<View> pullUpWeakReference;

    public SmoothScrollHandler(PullLayout stickyLayout, View pullDownView, View pullUpView) {
        super(WorkThread.getWorkLooper());
        stickyLayoutWeakReference = new WeakReference<>(stickyLayout);
        pullDownWeakReference = new WeakReference<>(pullDownView);
        pullUpWeakReference = new WeakReference<>(pullUpView);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        PullLayout stickyLayout = stickyLayoutWeakReference.get();
        if (stickyLayout == null) {
            Log.d(TAG, "stickyLayout is null");
            return;
        }
        int from = msg.arg1;
        int to = msg.arg2;
        long duration = (long) msg.obj;
        int frameCount = (int) (duration / FRAME_INTERVAL);
        float partition = (to - from) / (float) frameCount;

        switch (msg.what) {
            case MSG_SCHEDULE_HEADER_SCROLL:
                for (int i = 0; i < frameCount; i++) {
                    final int offset = (i == frameCount - 1) ? (to - from) : (int) partition * i;
                    final int lastHeightFinal = from;
                    stickyLayout.postDelayed(() -> stickyLayout.setHeaderHeight(lastHeightFinal, offset), (long) FRAME_INTERVAL * i);
                }
                break;
            case MSG_SCHEDULE_PULL_DOWN_SCROLL:
                View pullDownView = pullDownWeakReference.get();
                if (pullDownView == null) {
                    Log.d(TAG, "pullDownView is null");
                    return;
                }

                for (int i = 0; i < frameCount; i++) {
                    final int offset = (i == frameCount - 1) ? (to - from) : (int) partition * i;
                    final int lastHeightFinal = from;
                    stickyLayout.postDelayed(() -> stickyLayout.setPullDownHeight(lastHeightFinal, offset, false), (long) FRAME_INTERVAL * i);
                    from += offset;
                }
                break;
            case MSG_SCHEDULE_PULL_UP_SCROLL:
                View pullUpView = pullUpWeakReference.get();
                if (pullUpView == null) {
                    Log.d(TAG, "pullDownView is null");
                    return;
                }

                for (int i = 0; i < frameCount; i++) {
                    final int offset = (i == frameCount - 1) ? (to - from) : (int) partition * i;
                    final int lastHeightFinal = from;
                    stickyLayout.postDelayed(() -> stickyLayout.setPullUpHeight(lastHeightFinal, offset, false), (long) FRAME_INTERVAL * i);
                    from += offset;
                }
                break;
            default:
                break;
        }
    }
}
