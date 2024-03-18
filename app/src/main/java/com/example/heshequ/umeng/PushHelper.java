package com.example.heshequ.umeng;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.api.UPushThirdTokenCallback;
import com.umeng.message.entity.UMessage;

import org.android.agoo.fcm.FCMRegister;
import org.android.agoo.honor.HonorRegister;
import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

/**
 * PushSDK集成帮助类
 */
public class PushHelper {

    public static final String TAG = "UPushDemo";

    /**
     * 推送设置
     */
    public static void setting(PushAgent api) {
        //修改为您app/src/main/AndroidManifest.xml中package值
        api.setResourcePackageName("com.umeng.message.sample");

        //设置通知栏显示通知的最大个数（0～10），0：不限制个数
        api.setDisplayNotificationNumber(0);

        //推送消息处理
        UmengMessageHandler msgHandler = new UmengMessageHandler() {
            //处理通知栏消息
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {
                super.dealWithNotificationMessage(context, msg);
                Log.i(TAG, "notification receiver:\n" + msg.getRaw().toString());
            }

            //自定义通知样式，此方法可以修改通知样式等
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                return super.getNotification(context, msg);
            }

            //处理透传消息
            @Override
            public void dealWithCustomMessage(Context context, UMessage msg) {
                super.dealWithCustomMessage(context, msg);
                Log.i(TAG, "custom receiver:\n" + msg.getRaw().toString());
            }
        };
        api.setMessageHandler(msgHandler);

        //推送消息点击处理
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
                Log.i(TAG, "click open activity:\n" + msg.getRaw().toString());
            }

            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
                Log.i(TAG, "click launch app:\n" + msg.getRaw().toString());
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
                Log.i(TAG, "click open deeplink:\n" + msg.getRaw().toString());
            }

            @Override
            public void dismissNotification(Context context, UMessage msg) {
                super.dismissNotification(context, msg);
                Log.i(TAG, "dismissNotification:\n" + msg.getRaw().toString());
            }
        };
        api.setNotificationClickHandler(notificationClickHandler);

        //通过Service自定义接收并处理消息
//        api.setPushIntentServiceClass(MyCustomMessageService.class);

        //设置厂商Token回调
        api.setThirdTokenCallback(new UPushThirdTokenCallback() {
            @Override
            public void onToken(String type, String token) {
                Log.i(TAG, "push type:" + type + " token:" + token);
            }
        });
    }

    /**
     * 注册设备推送通道（小米、华为等设备的推送）
     */
    public static void registerDevicePush(Context context) {
        //小米推送：填写您在小米后台APP对应的xiaomi id和key
        MiPushRegistar.register(context, PushConstants.MI_ID, PushConstants.MI_KEY);
        //华为推送：注意华为推送的初始化参数在AndroidManifest.xml中配置
        HuaWeiRegister.register(context.getApplicationContext());
        //魅族推送：填写您在魅族后台APP对应的app id和key
        MeizuRegister.register(context, PushConstants.MEI_ZU_ID, PushConstants.MEI_ZU_KEY);
        //OPPO推送：填写您在OPPO后台APP对应的app key和secret
        OppoRegister.register(context, PushConstants.OPPO_KEY, PushConstants.OPPO_SECRET);
        //vivo推送：注意vivo推送的初始化参数在AndroidManifest.xml中配置
        VivoRegister.register(context);
        //荣耀推送：注意荣耀推送的初始化参数在AndroidManifest.xml中配置
        HonorRegister.register(context);
        //谷歌fcm推送
        FCMRegister.register(context);
    }
}
