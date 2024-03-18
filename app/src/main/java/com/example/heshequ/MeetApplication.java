package com.example.heshequ;

import static com.example.heshequ.umeng.PushConstants.APP_KEY;
import static com.example.heshequ.umeng.PushConstants.CHANNEL;
import static com.example.heshequ.umeng.PushConstants.MESSAGE_SECRET;
import static com.example.heshequ.umeng.PushHelper.registerDevicePush;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.heshequ.umeng.MyPreferences;
import com.example.heshequ.umeng.PushHelper;
import com.example.heshequ.utils.CrashHandler;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.PushAgent;
import com.umeng.message.api.UPushRegisterCallback;

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

        //友盟日志输出开关
        UMConfigure.setLogEnabled(true);
        //预初始化
        UMConfigure.preInit(this, APP_KEY, CHANNEL);
        //是否同意隐私政策
        boolean agreed = MyPreferences.getInstance(this).hasAgreePrivacyAgreement();
        if (!agreed) {
            return;
        }
        //建议在子线程中初始化
        new Thread(() -> init(getApplicationContext())).start();
    }

    public static MeetApplication getInstance() {
        return sInstance;
    }

    public static void init(final Context context) {
        // 初始化配置，应用配置信息：http://message.umeng.com/list/apps
        // 参数1：上下文context；
        // 参数2：应用申请的Appkey；
        // 参数3：发布渠道名称；
        // 参数4：设备类型，UMConfigure.DEVICE_TYPE_PHONE：手机；UMConfigure.DEVICE_TYPE_BOX：盒子；默认为手机
        // 参数5：Push推送业务的secret，填写Umeng Message Secret对应信息
        UMConfigure.init(context, APP_KEY, CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, MESSAGE_SECRET);

        // 推送注册
        PushAgent api = PushAgent.getInstance(context);
        PushHelper.setting(api);
        api.register(new UPushRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                Log.i(TAG, "deviceToken: " + deviceToken);
                //注册厂商通道
                registerDevicePush(context);
            }

            @Override
            public void onFailure(String errCode, String errDesc) {
                Log.e(TAG, "register failed! " + "code:" + errCode + ",desc:" + errDesc);
            }
        });
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
