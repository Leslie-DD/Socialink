package com.leslie.socialink.activity.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.MatcherUtils;
import com.leslie.socialink.utils.SharedPreferencesHelp;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPwdActivity extends NetWorkActivity {
    private int type = 1;
    private TextView tvTitle, tvCode;
    private EditText etPhone, etCode, etPwd, etPwd2;
    private Button btSave;
    private ImageView ivSee, ivSee2;
    private boolean canSee, canSee2;
    private int count = 30;
    private String phone;
    private final int S_CODE_CODE = 1000;
    private final int RESET_PWD_CODE = 1001;

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            count--;
            if (count <= 0) {
                count = 60;
                tvCode.setText("获取验证码");
                getting = false;
            } else {
                tvCode.setText("倒计时" + count);

            }
        }
    };
    private boolean getting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        init();
        event();
    }

    private void init() {
        type = getIntent().getIntExtra("type", 0);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (type == 1) {
            tvTitle.setText("忘记密码");
        } else {
            tvTitle.setText("修改密码");
        }
        tvCode = (TextView) findViewById(R.id.tvCode);
        etCode = (EditText) findViewById(R.id.etCode);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPwd = (EditText) findViewById(R.id.etPwd);
        etPwd2 = findViewById(R.id.etPwd2);
        ivSee2 = findViewById(R.id.ivSee2);
        btSave = (Button) findViewById(R.id.btSave);
        ivSee = (ImageView) findViewById(R.id.ivSee);
    }

    private void event() {
        tvCode.setOnClickListener(v -> {
            if (getting) {
                return;
            }
            phone = etPhone.getText().toString();
            if (phone.isEmpty()) {
                Utils.toastShort(mContext, "手机号码不能为空");
                return;
            }
            if (!MatcherUtils.isPhone(phone)) {
                Utils.toastShort(mContext, "请输入正确的手机号码！");
                return;
            }
            if (type != 1) {
                if (!phone.equals(SharedPreferencesHelp.getString("phone", ""))) {
                    Utils.toastShort(mContext, "非本账户绑定手机，请使用注册时的手机号！！");
                    return;
                }
            }
            setBodyParams(new String[]{"phone", "forget"}, new String[]{phone, "forget"});
            sendPost(Constants.base_url + "/api/account/scode.do", S_CODE_CODE, null);
        });
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        btSave.setOnClickListener(v -> {
            phone = etPhone.getText().toString().trim();
            String pwd = etPwd.getText().toString().trim();
            String pwd2 = etPwd2.getText().toString().trim();
            String code = etCode.getText().toString().trim();
            if (phone.isEmpty()) {
                Utils.toastShort(mContext, "手机号码不能为空");
                return;
            }
            if (!MatcherUtils.isPhone(phone)) {
                Utils.toastShort(mContext, "请输入正确的手机号码！");
                return;
            }
            if (type != 1) {
                if (!phone.equals(SharedPreferencesHelp.getString("phone", ""))) {
                    Utils.toastShort(mContext, "非本账户绑定手机，请使用注册时的手机号！！");
                    return;
                }
            }
            if (code.length() == 0) {
                Utils.toastShort(mContext, "验证码不能为空,请获取验证码后再填入验证码");
                return;
            }
            if (pwd.isEmpty()) {
                Utils.toastShort(mContext, "新密码不能为空！");
                return;
            }

            if (!MatcherUtils.isPwd(pwd)) {
                Utils.toastShort(mContext, "密码格式不正确！");
                return;
            }
            if (pwd2.isEmpty()) {
                Utils.toastShort(mContext, "‘确认密码’不能为空！");
                return;
            }
            if (!MatcherUtils.isPwd(pwd)) {
                Utils.toastShort(mContext, "‘确认密码’格式错误！");
                return;
            }
            if (!pwd.equals(pwd2)) {
                Utils.toastShort(mContext, "二次密码不一致！");
                return;
            }
            setBodyParams(new String[]{"phone", "code", "pwd"}, new String[]{phone, code, pwd /*EncryptUtils.encryptMD5ToString(pwd)*/});
            sendPost(Constants.base_url + "/api/account/resetpwd.do", RESET_PWD_CODE, null);
        });
        ivSee.setOnClickListener(v -> {
            canSee = !canSee;
            ivSee.setImageResource(canSee ? R.mipmap.kj : R.mipmap.bkj);
                /*
                //从密码不可见模式变为密码可见模式
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                //从密码可见模式变为密码不可见模式
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                */
            etPwd.setTransformationMethod(canSee ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());

            etPwd.setSelection(etPwd.getText().toString().length());
        });
        ivSee2.setOnClickListener(v -> {
            canSee2 = !canSee2;
            ivSee2.setImageResource(canSee2 ? R.mipmap.kj : R.mipmap.bkj);
            etPwd2.setTransformationMethod(canSee2 ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            etPwd2.setSelection(etPwd2.getText().toString().length());
        });
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        switch (where) {
            case S_CODE_CODE:
                if (result.optInt("code") == 0) {
                    new Thread(() -> {
                        count = 30;
                        getting = true;
                        while (getting) {
                            handler.sendEmptyMessage(1);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    getting = false;
                }
                String msg = result.optString("msg");
                if (!TextUtils.isEmpty(msg)) {
                    Utils.toastShort(mContext, msg);
                }
                break;
            case RESET_PWD_CODE:
                if (result.optInt("code") == 0) {
                    this.finish();
                }
                Utils.toastShort(mContext, result.optString("msg"));
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
