package com.example.heshequ;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefreshBean;
import com.example.heshequ.utils.CrashHandler;
import com.example.heshequ.utils.QueryPatchUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2017/8/23 16:48
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class MeetApplication extends MultiDexApplication {
    public ImageLoader mImageLoader = ImageLoader.getInstance();
    private List<Activity> activityList = new ArrayList<>();
    private static MeetApplication instance;
    public static Tencent mTencent;
    private PushAgent mPushAgent;
    private String typeId;
    private int mFinalCount;

    @Override
    public void onCreate() {
        super.onCreate();
        //配置图片加载的方法
        Fresco.initialize(this);
        Log.e("THIS = ", this.toString());
        instance = this;
        UMConfigure.init(this, null,null, UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.onEvent(MeetApplication.getInstance(),"event_appLaunch");
        regTowx();
        if (mTencent == null) {
            mTencent = Tencent.createInstance("1107493816", this.getApplicationContext());
        }
        AuthInfo mAuthInfo = new AuthInfo(this, Constants.APP_KEY_WB, Constants.REDIRECT_URL, Constants.SCOPE);
        WbSdk.install(this,mAuthInfo);
        //注册你的ShareHandler：
        initUm();
        CrashHandler.getInstance().init(this);
        initActivityLifecycleCallbacks();

        /*查询是否有更新补丁*/
        //SophixManager.getInstance().queryAndLoadNewPatch();
        /*1.4h查询一次是否有更新补丁（平均单台设备每天免费查询20次，查询超过20次，收费标准：2元/万次请求。）*/
        QueryPatchUtils.freeQuery();
    }

    private void regTowx(){
        Constants.api = WXAPIFactory.createWXAPI(getApplicationContext(), Constants.APP_AD_WX,true);
        Constants.api.registerApp(Constants.APP_AD_WX);
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

    public Activity currentActivity(){
        Activity activity = null;
        if (!activityList.isEmpty()){
            activity = activityList.get(activityList.size()-1);
        }
        return activity;
    }

    //获取sp
    public SharedPreferences getSharedPreferences() {
        return getInstance().getApplicationContext().getSharedPreferences("meet", 0);
    }

    private void initUm() {
        try {
            mPushAgent = PushAgent.getInstance(this);
            mPushAgent.setMessageHandler(new UmengMessageHandler() {
                @Override
                public Notification getNotification(Context context, UMessage uMessage) {
                    Log.e("ying","getNotification");
                    RefreshBean refreshBean=new RefreshBean();
                    if (uMessage.extra != null) {
                        if (uMessage.extra.containsKey("type")) {
                            typeId = uMessage.extra.get("type");
                            Log.e("ying","uMessage.extra:"+typeId);
                        } else {
                            typeId="0";
                        }
                    }else{
                        typeId="0";
                    }
                    Log.e("ying","uMessage.extra:"+typeId);
                    refreshBean.type=typeId;
                    EventBus.getDefault().post(refreshBean);

                    if (Build.VERSION.SDK_INT >= 26) {
                        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH);
                        if (manager != null) {
                            manager.createNotificationChannel(channel);
                        }
                        Notification.Builder builder = new Notification.Builder(context, "channel_id");
                        builder.setSmallIcon(R.mipmap.launcher_icon)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.launcher_icon))
                                .setContentTitle(uMessage.title)
                                .setContentText(uMessage.text)
                                .setAutoCancel(true);
                        return builder.build();
                    }else{
                        NotificationCompat.Builder mBuilder
                                = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.launcher_icon)
                                .setTicker("湘遇").setContentTitle(uMessage.title).setDefaults(Notification.DEFAULT_SOUND)
                                .setAutoCancel(true).setWhen(System.currentTimeMillis())
                                .setContentText(uMessage.text);
                        return mBuilder.build();
                    }
                }
            });
            mPushAgent.register(new IUmengRegisterCallback() {
                @Override
                public void onSuccess(String deviceToken) {
                    Log.e("ying", "deviceToken" + deviceToken);
                    //注册成功会返回device token
                }

                @Override
                public void onFailure(String s, String s1) {
                    Log.e("ying", "deviceToken没有-" + s + "--" + s1);
                }
            });
        } catch (Exception e) {
            Log.e("ying", e.toString());
        }

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
                if (mFinalCount == 1){
                    //说明从后台回到了前台
                    Log.e("DDQ-->","应用从后台回到了前台");
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
                Log.e("onActivityStopped", mFinalCount +"");
                if (mFinalCount == 0){
                    //说明从前台回到了后台
                    Log.e("DDQ-->","应用从从前台回到了后台");
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
