package com.example.wxapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.utils.Utils;
import org.json.JSONObject;


/**
 * 微信客户端回调activity
 */
public class WXEntryActivity extends NetWorkActivity {

    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.e("DDQ", result + "");

    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "访问网络失败~");
        finish();
    }
}


