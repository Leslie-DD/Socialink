package com.example.heshequ;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.heshequ.utils.CrashHandler;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2017/8/23 16:48
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class MeetApplication extends Application {
    public ImageLoader mImageLoader = ImageLoader.getInstance();
    private List<Activity> activityList = new ArrayList<>();
    private static MeetApplication instance;
    private String typeId;
    private int mFinalCount;

    @Override
    public void onCreate() {
        super.onCreate();
        //配置图片加载的方法
        Fresco.initialize(this);
        Log.e("THIS = ", this.toString());
        instance = this;
        //注册你的ShareHandler：
        CrashHandler.getInstance().init(this);
        initActivityLifecycleCallbacks();
    }

    public static MeetApplication getInstance() {
        return instance;
    }

    public void addActivity(Activity activity) {
        try {
            // 新加进来的Activity进行主题的设置
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
            if (activityList != null && activityList.size() > 0) {
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

    public Activity finishAll() {
        Activity activity = null;
        synchronized (this) {
            if (activityList != null && activityList.size() > 0) {
                for (Activity a : activityList) {
                    a.finish();
                }
            }
        }
        return activity;
    }

    // 有Activity手动finish的时候需要将其引用在集合中删除
    public void removeActivity(Activity activity) {
        synchronized (this) {
            if (!activityList.isEmpty() && activityList.contains(activity))
                activityList.remove(activity);
        }
        System.gc();
    }

    public Activity currentActivity() {
        Activity activity = null;
        if (!activityList.isEmpty()) {
            activity = activityList.get(activityList.size() - 1);
        }
        return activity;
    }

    //获取sp
    public SharedPreferences getSharedPreferences() {
        return getInstance().getApplicationContext().getSharedPreferences("meet", 0);
    }

    private void initActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mFinalCount++;
                //如果mFinalCount ==1，说明是从后台到前台
                if (mFinalCount == 1) {
                    //说明从后台回到了前台
                    Log.e("DDQ-->", "应用从后台回到了前台");
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                //如果mFinalCount ==0，说明是前台到后台
                Log.e("onActivityStopped", mFinalCount + "");
                if (mFinalCount == 0) {
                    //说明从前台回到了后台
                    Log.e("DDQ-->", "应用从从前台回到了后台");
                    /**
                     * 没明白下面的代码到底是修复什么补丁才要求重启
                     * 先注释掉，不然上传图片容易关闭进程
                     * 2020 07 13 16:52
                     */
//                    if (Constants.isHotFix){
//                        Log.e("DDQ-->","关闭进程");
//                        Constants.isHotFix = false;
//                        finishAll();
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
