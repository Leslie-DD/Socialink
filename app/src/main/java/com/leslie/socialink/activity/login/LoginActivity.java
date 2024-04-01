package com.leslie.socialink.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.entity.EventBean;
import com.leslie.socialink.launcher.MainActivity;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.util.AuthorizationInterceptor;
import com.leslie.socialink.utils.SharedPreferencesHelp;
import com.leslie.socialink.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends NetWorkActivity {
    private LinearLayout main;
    private TextView tvSign, tvForget, studentIDLogin;
    private EditText etUser, etPwd;
    private Button btLogin;
    private final int loginCode = 1000;
    private String Phone;
    private int loginMethod = 0;//登陆方式 0->账号密码登陆 1-> 学号登陆
    private RelativeLayout outsideLayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        event();
    }

    private void init() {
        tvSign = (TextView) findViewById(R.id.tvSign);
        etUser = (EditText) findViewById(R.id.etUser);
        etPwd = (EditText) findViewById(R.id.etPwd);
        btLogin = (Button) findViewById(R.id.btLogin);
        tvForget = (TextView) findViewById(R.id.tvForget);
        studentIDLogin = (TextView) findViewById(R.id.studentidlogin);
        studentIDLogin.setVisibility(View.VISIBLE);
        main = (LinearLayout) findViewById(R.id.main);
        outsideLayer = (RelativeLayout) findViewById(R.id.outside_layer);
        EventBus.getDefault().register(this);

    }

    @Subscribe
    public void signSuc(EventBean bean) {
        etUser.setText(bean.getMsg());
        etUser.setSelection(bean.getMsg().length());
    }

    private void event() {
        btLogin.setOnClickListener(v -> {
            Phone = etUser.getText().toString();
            String pwd = etPwd.getText().toString();
            if (TextUtils.isEmpty(Phone)) {
                Utils.toastShort(mContext, "请先输入账号");
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                Utils.toastShort(mContext, "请先输入密码");
                return;
            }
            //setBodyParams(new String[]{"phone", "pwd"}, new String[]{Phone, EncryptUtils.encryptMD5ToString(pwd)});
            setBodyParams(new String[]{"phone", "pwd"}, new String[]{Phone, pwd});
            sendPost(Constants.base_url + "/api/account/login.do", loginCode, null);
        });
        tvSign.setOnClickListener(v -> startActivity(new Intent(mContext, SignActivity.class)));
        tvForget.setOnClickListener(v -> startActivity(new Intent(mContext, ForgetPwdActivity.class).putExtra("type", 1)));
        studentIDLogin.setOnClickListener(v -> startActivity(new Intent(mContext, StudentIdLoginActivity.class)));
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Intent intent = null;
        Intent intent1 = null;
        int ret = result.optInt("code");
        if (ret == 0) {
            JSONObject dd = new JSONObject(result.optString("data"));
            String token = dd.optString("token");
            int uid = dd.optInt("uid");

//            Constants.uid=uid;
            Constants.uid = uid;
            Constants.token = token;

            /**
             * 2020.09.08 注释
             * 下面代码很奇怪，为什么要跳转下面两个页面？
             */
//            intent=new Intent();
//            intent.setClass(LoginActivity.this, SendNew.class);
//            SendNew.id = uid;
//            startActivity(intent);
//            intent1= new Intent();
//            intent1.setClass(LoginActivity.this, FriendSet.class);
//            FriendSet.id=uid;
//            startActivity(intent1);

            SharedPreferencesHelp.getEditor().putString("phone", Phone).putString("token", token).putInt("uid", uid).putBoolean(
                    "isLogin", true).apply();
            AuthorizationInterceptor.cacheToken(token);
            this.finish();
            startActivity(new Intent(mContext, MainActivity.class));

            //Constants.uid = uid;
            Utils.toastShort(mContext, "登录成功；用户id：" + Constants.uid);

        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "请求失败");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
