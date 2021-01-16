package com.example.heshequ.interfaces;

import android.content.Context;
import android.util.Log;
import com.example.heshequ.utils.Utils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;

/**
 * Created by Dengdongqi on 2018/8/10.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener {
    private Context context;

    public SelfWbAuthListener(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(final Oauth2AccessToken token) {
        Utils.toastShort(context, "微博分享成功");
    }

    @Override
    public void cancel() {
        Utils.toastShort(context, "取消了微博分享");
    }

    @Override
    public void onFailure(WbConnectErrorMessage errorMessage) {
        Log.e("DDQ-->", errorMessage.toString());
        Utils.toastShort(context, "微博分享失败");
    }
}
