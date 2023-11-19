package com.example.heshequ.interfaces;

import android.content.Context;
import android.util.Log;

import com.example.heshequ.utils.Utils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by Dengdongqi on 2018/8/9.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class BaseUiListener implements IUiListener {
    private Context context;

    public BaseUiListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCancel() {
        Utils.toastShort(context, "取消了QQ分享");
    }

    @Override
    public void onComplete(Object response) {
        Utils.toastShort(context, "QQ分享成功");
    }

    @Override
    public void onError(UiError e) {
        Log.e("DDQ-->", String.valueOf(e));
        Utils.toastShort(context, "QQ分享失败");
    }

}



