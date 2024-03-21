package com.example.heshequ;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.example.heshequ.utils.CrashHandler;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

public class MeetApplication extends Application {

    private static final String TAG = "[MeetApplication]";

    private final List<Activity> activityList = new ArrayList<>();
    private static MeetApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        sInstance = this;
        CrashHandler.getInstance().init(this);
        CrashReport.initCrashReport(getApplicationContext(), "8c0feaed02", true);
    }

    public static MeetApplication getInstance() {
        return sInstance;
    }

    public void addActivity(Activity activity) {
        try {
            // 新加进来的 Activity 进行主题的设置
            synchronized (this) {
                if (!activityList.contains(activity)) {
                    activityList.add(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Activity getActivity(String simpleName) {
        Activity activity = null;
        synchronized (this) {
            if (activityList.size() > 0) {
                for (Activity a : activityList) {
                    if (simpleName.equals(a.getClass().getSimpleName())) {
                        activity = a;
                        break;
                    }
                }
            }
        }
        return activity;
    }

    public void finishAll() {
        synchronized (this) {
            if (activityList.size() > 0) {
                for (Activity a : activityList) {
                    a.finish();
                }
            }
        }
    }

    // 有 Activity 手动 finish 的时候需要将其引用在集合中删除
    public void removeActivity(Activity activity) {
        synchronized (this) {
            if (!activityList.isEmpty()) {
                activityList.remove(activity);
            }
        }
        System.gc();
    }

    public SharedPreferences getSharedPreferences() {
        return getInstance().getApplicationContext().getSharedPreferences("meet", 0);
    }
}
