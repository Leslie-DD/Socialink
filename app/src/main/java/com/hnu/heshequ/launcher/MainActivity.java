package com.hnu.heshequ.launcher;

import static com.hnu.heshequ.utils.PermissionHelper.PERMISSIONS_STARTUP;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.entity.RefUserInfo;
import com.hnu.heshequ.network.entity.UserInfoBean;
import com.hnu.heshequ.utils.SharedPreferencesHelp;
import com.hnu.heshequ.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends NetWorkActivity {
    private static final String TAG = "[MainActivity]";

    private final int getUserinfo = 1000;
    private Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        Log.i(TAG, "(onCreate) uid = " + SharedPreferencesHelp.getInt("uid", 1));
        Constants.token = SharedPreferencesHelp.getString("token", "");
        WenConstans.token = SharedPreferencesHelp.getString("token", "");
        Constants.userName = SharedPreferencesHelp.getString("user", "18274962484");
        init();
    }

    private void init() {
        gson = new Gson();
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        Constants.screenW = outMetrics.widthPixels;
        Constants.screenH = outMetrics.heightPixels;

        requestPermission(Arrays.asList(PERMISSIONS_STARTUP), 100);

        //请求用户信息
        setBodyParams(new String[]{"uid"}, new String[]{String.valueOf(SharedPreferencesHelp.getInt("uid", 1))});
        sendPost(Constants.base_url + "/api/user/info.do", getUserinfo, Constants.token);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refUserinfo(RefUserInfo refUserInfo) {
        //请求用户信息
        setBodyParams(new String[]{"uid"}, new String[]{"" + Constants.uid});
        sendPost(Constants.base_url + "/api/user/info.do", getUserinfo, Constants.token);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == getUserinfo) {
            switch (result.optInt("code")) {
                case 0:
                    if (result.optString("data").isEmpty()) {
                        Utils.toastShort(this, "服务器开小差了");
                        break;
                    }
                    UserInfoBean userInfoBean = gson.fromJson(result.optString("data"), UserInfoBean.class);
                    Constants.uid = userInfoBean.getId();
                    Constants.userName = userInfoBean.getNickname();
                    //存入用户名
                    SharedPreferencesHelp.getEditor().putString("user", userInfoBean.getNickname()).apply();
                    break;
                case 1:
                    Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                    break;
                case 2:
                    Utils.toastShort(this, result.optString("msg"));
                    break;
                case 3:
                    Utils.toastShort(this, "您没有该操作权限");
                    break;
            }
            Log.i(TAG, "(onSuccess) Constants.uid = " + Constants.uid);
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Log.w(TAG, "(onFailure) Constants.uid = " + Constants.uid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
