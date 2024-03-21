package com.hnu.heshequ.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hnu.heshequ.MeetApplication;
import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.utils.EncryptUtils;
import com.hnu.heshequ.utils.MatcherUtils;
import com.hnu.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignActivity extends NetWorkActivity implements View.OnClickListener {
    private TextView tvTitle, tvCode, tvSchool;
    private EditText etPhone, etCode, etPwd, etPwd2;
    private LinearLayout llSchool;
    private Button btNext;
    private ImageView ivSee, ivSee2;
    private boolean canSee, canSee2;
    private final int getCode = 1000;
    private final int signCode = 1001;
    private final int upSchool = 1003;
    private String school;
    private int count = 30;
    private boolean getting;
    private String phone;
    private String pwd, pwd2;
    private SharedPreferences sp;
    private Gson gson;
    private OptionsPickerView pvOptions;
    private ArrayList<LabelSelectionActivity.LableBean> datas;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            count--;
            if (count <= 0) {
                count = 60;
                tvCode.setText("获取验证码");
                tvCode.setEnabled(true);
                getting = false;
            } else {
                tvCode.setText("倒计时" + count);

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        init();
        event();
    }

    private void init() {
        sp = MeetApplication.getInstance().getSharedPreferences();
        gson = new Gson();
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("注册");
        tvCode = (TextView) findViewById(R.id.tvCode);
        tvSchool = (TextView) findViewById(R.id.tvSchool);
        llSchool = (LinearLayout) findViewById(R.id.llSchool);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etCode = (EditText) findViewById(R.id.etCode);
        btNext = (Button) findViewById(R.id.btNext);
        etPwd = (EditText) findViewById(R.id.etPwd);
        ivSee = (ImageView) findViewById(R.id.ivSee);
        etPwd2 = findViewById(R.id.etPwd2);
        ivSee2 = findViewById(R.id.ivSee2);
        getSchoolData();
    }

    private void getSchoolData() {
        setBodyParams(new String[]{"type"}, new String[]{"school"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", 10086, Constants.token);
    }

    private void event() {
        tvCode.setOnClickListener(this);
        findViewById(R.id.ivBack).setOnClickListener(this);
        llSchool.setOnClickListener(this);
        btNext.setOnClickListener(this);
        ivSee.setOnClickListener(this);
        ivSee2.setOnClickListener(this);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("ret");
//        Utils.toastShort(mContext, ret+" ");
        switch (where) {
            case getCode:
                if (ret == 0) {
                    if (result.optInt("code") == 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
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
                            }
                        }).start();
                    }
                } else {
                    getting = false;
                }
                String msg = result.optString("msg");
                if (!TextUtils.isEmpty(msg)) {
                    Utils.toastShort(mContext, msg);
                }
                break;
            case signCode:
                if (ret == 0) {
//                    Utils.toastShort(mContext, "注册成功");
//                    Utils.toastShort(mContext, ret+" " + result.optString("msg"));
                    Utils.toastShort(mContext, result.optString("msg"));
                    startActivity(new Intent(this, DefaultProblemActivity.class)
                            .putExtra("phone", phone)
                            .putExtra("pwd", pwd)
                            .putExtra("type", 1)
                    );
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case 10086:
                if (result.optInt("code") == 0) {
                    datas = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<LabelSelectionActivity.LableBean>>() {
                    }.getType());
                    final ArrayList<String> data = new ArrayList<>();
                    for (int i = 0; i < datas.size(); i++) {
                        data.add(datas.get(i).getValue());
                    }
                    pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int option2, int options3, View v) {
                            school = data.get(options1);
                            tvSchool.setText(school);
                        }
                    }).setSubmitText("确定")//确定按钮文字
                            .setCancelText("取消")//取消按钮文字
                            .setTitleText("选择学校")//标题
                            .setSubCalSize(18)//确定和取消文字大小
                            .setTitleSize(20)//标题文字大小
                            .setTitleColor(Color.BLACK)//标题文字颜色
                            .setSubmitColor(Color.parseColor("#00BBFF"))//确定按钮文字颜色
                            .setCancelColor(Color.parseColor("#00BBFF"))//取消按钮文字颜色
                            .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                            .setBgColor(0xFFFFFFFF)//滚轮背景颜色
                            .setContentTextSize(18)//滚轮文字大小
                            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                            .setOutSideCancelable(true)//点击外部dismiss default true
                            .isDialog(false)//是否显示为对话框样式
                            .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                            .build();

                    pvOptions.setPicker(data);//添加数据源
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case upSchool:

        }
    }

    @Override
    protected void onFailure(String result, int where) {
        getting = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.tvCode:
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

                setBodyParams(new String[]{"phone"}, new String[]{phone});
                sendPost(Constants.base_url + "/api/account/scode.do", getCode, null);
                break;
            case R.id.llSchool:
                Utils.hideSoftInput(this);
                if (pvOptions == null) {
                    Utils.toastShort(mContext, "获取数据中~~~");
                    return;
                }
                pvOptions.show();
                break;
            case R.id.btNext:
                String code = etCode.getText().toString();
                phone = etPhone.getText().toString();
                if (phone.isEmpty()) {
                    Utils.toastShort(mContext, "手机号码不能为空");
                    return;
                }
                if (!MatcherUtils.isPhone(phone)) {
                    Utils.toastShort(mContext, "请输入正确的手机号码！");
                    return;
                }
                if (code.length() == 0) {
                    Utils.toastShort(mContext, "验证码不能为空,请获取验证码后再填入验证码");
                    return;
                }
                if (code.length() != 6) {
                    Utils.toastShort(mContext, "请输入6位正确的验证码");
                    return;
                }
                pwd = etPwd.getText().toString();
                if (pwd.isEmpty()) {
                    Utils.toastShort(mContext, "密码不能为空！");
                    return;
                }
                if (!MatcherUtils.isPwd(pwd)) {
                    Utils.toastShort(mContext, "密码格式错误！");
                    return;
                }

                pwd2 = etPwd2.getText().toString().trim();
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
                if (school == null || school.isEmpty()) {
                    Utils.toastShort(mContext, "请先选择学校！");
                    return;
                }
                setBodyParams(new String[]{"phone", "code", "pwd", "school"},
                        new String[]{phone, code, EncryptUtils.encryptMD5ToString(pwd), school});
                sendPost(Constants.base_url + "/api/account/register.do", signCode, null);
                break;
            case R.id.ivSee:
                canSee = !canSee;
                ivSee.setImageResource(canSee ? R.mipmap.kj : R.mipmap.bkj);
                etPwd.setTransformationMethod(canSee ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                etPwd.setSelection(etPwd.getText().toString().length());
                break;
            case R.id.ivSee2:
                canSee2 = !canSee2;
                ivSee2.setImageResource(canSee2 ? R.mipmap.kj : R.mipmap.bkj);
                etPwd2.setTransformationMethod(canSee2 ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                etPwd2.setSelection(etPwd2.getText().toString().length());
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
