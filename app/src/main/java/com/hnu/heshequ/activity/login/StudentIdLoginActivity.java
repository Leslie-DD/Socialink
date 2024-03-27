package com.hnu.heshequ.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.SchoolBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.launcher.MainActivity;
import com.hnu.heshequ.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 学号登陆界面
 * Created by 佳佳 on 2019/8/26.
 */

public class StudentIdLoginActivity extends NetWorkActivity {

    private OptionsPickerView schoolChooseHelper;//学校选择器
    private ArrayList<SchoolBean> schools = new ArrayList<>();//学校列表
    private SimpleDraweeView mHead, mHead0;//logo，需要验证码的学校会在后期替换为验证码
    private SchoolBean school = new SchoolBean();//选择的学校
    private final int APPLY_FOR_LOGIN = 0;//申请登陆
    private final int APPLY_FOR_LOGIN_WITH_VERIFICATION_CODE = 1;//验证码登陆
    private int landingMode = APPLY_FOR_LOGIN;//当前登陆方式，默认为不需要验证码账号登陆。
    private SharedPreferences memory;
    private String key;
    private String verification;
    private String studentId;
    private String studentPwd;
    private String code;//学校编码

    /**
     * 选择学校
     */
    private LinearLayout mLlSchool;
    /**
     * 请输入您的账号
     */
    private EditText mEtUser;
    /**
     * 请输入您的密码
     */
    private EditText mEtPwd;
    /**
     * 请输入验证码
     */
    private EditText mEtVerificationcode;
    /**
     * 登录
     */
    private Button mBtLogin;
    /**
     * 账号登陆
     */
    private TextView mStudentidlogin;
    /**
     * 学校
     */
    private TextView mEtSchool;
    private LinearLayout mMain;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        initView();     // 初始化视图
        initSchools();  // 初始化学校列表
        initSchoolChooseHelper();   // 初始化学校选择器
        event();    // 监听事件
    }

    /**
     * 初始化视图
     */
    private void initView() {
        memory = getSharedPreferences("meet", 0);
        mHead = (SimpleDraweeView) findViewById(R.id.head);
        mHead0 = (SimpleDraweeView) findViewById(R.id.head0);
        mLlSchool = (LinearLayout) findViewById(R.id.llSchool);
        mEtSchool = (TextView) findViewById(R.id.etSchool);
        mEtUser = (EditText) findViewById(R.id.etUser);
        mEtPwd = (EditText) findViewById(R.id.etPwd);
        mEtVerificationcode = (EditText) findViewById(R.id.etVerificationcode);
        mBtLogin = (Button) findViewById(R.id.btLogin);
        mStudentidlogin = (TextView) findViewById(R.id.studentidlogin);
        mMain = (LinearLayout) findViewById(R.id.main);
    }

    /**
     * 初始化学校列表
     */
    private void initSchools() {
        SchoolBean schoolBean = new SchoolBean();
        schoolBean.setSchool("湖南大学");
        schoolBean.setType(0);
        schools.add(schoolBean);

        Log.e("ddq", schools.get(0).getSchool());

        schoolBean = new SchoolBean();
        schoolBean.setSchool("中南大学");
        schoolBean.setType(1);
        schools.add(schoolBean);
        Log.e("ddq", schools.get(1).getSchool());

        schoolBean = new SchoolBean();
        schoolBean.setSchool("湖南师范大学");
        schoolBean.setType(2);
        schools.add(schoolBean);
        Log.e("ddq", schools.get(2).getSchool());

        schoolBean = new SchoolBean();
        schoolBean.setSchool("长沙学院");
        schoolBean.setType(3);
        schools.add(schoolBean);
        Log.e("ddq", schools.get(3).getSchool());
    }

    /**
     * 初始化学校选择器
     */
    private void initSchoolChooseHelper() {
        schoolChooseHelper = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                Log.e("ddq", "-------weizhi--" + options1);
                school.setSchool(schools.get(options1).getSchool());
                school.setType(schools.get(options1).getType());
                mEtSchool.setText(school.getSchool());
            }
        }).setSubmitText("确定")  // 确定按钮文字
                .setCancelText("取消")    // 取消按钮文字
                .setTitleText("选择学校") // 标题
                .setSubCalSize(18)  // 确定和取消文字大小
                .setTitleSize(20)   // 标题文字大小
                .setTitleColor(Color.BLACK) // 标题文字颜色
                .setSubmitColor(Color.parseColor("#2CD22B"))    // 确定按钮文字颜色
                .setCancelColor(Color.parseColor("#2CD22B"))    // 取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)    // 标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF) // 滚轮背景颜色
                .setContentTextSize(18) // 滚轮文字大小
                .isCenterLabel(false)   // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideCancelable(true) // 点击外部dismiss default true
                .isDialog(false)    // 是否显示为对话框样式
                .isRestoreItem(true)// 切换时是否还原，设置默认选中第一项。
                .build();

        List<String> results = new ArrayList<>();
        for (int i = 0; i < schools.size(); i++) {
            results.add(schools.get(i).getSchool());
        }

        schoolChooseHelper.setPicker(results);

    }

    /**
     * 事件监听
     */
    private void event() {
        mHead.setOnClickListener(v -> {
            switch (landingMode) {
                case APPLY_FOR_LOGIN:
                    break;
                case APPLY_FOR_LOGIN_WITH_VERIFICATION_CODE:
                    applyForLogin(APPLY_FOR_LOGIN);
            }
        });
        mLlSchool.setOnClickListener(v -> {
            Utils.hideSoftInput(this);
            schoolChooseHelper.show();//选择好学校之后发送登陆申请
            //applyForLogin(0);
        });
        mBtLogin.setOnClickListener(v -> login());
        mStudentidlogin.setOnClickListener(v -> finish());
    }

    /**
     * 登陆申请
     */
    private void applyForLogin(int type) {
        switch (type) {
            case APPLY_FOR_LOGIN://申请登陆
                setBodyParams(new String[]{"studentId", "pwd", "type"}, new String[]{studentId, studentPwd, code});
                sendPost(Constants.base_url + "/api/account/highLogin.do", APPLY_FOR_LOGIN, null);
                break;
            case APPLY_FOR_LOGIN_WITH_VERIFICATION_CODE://验证码申请登陆
                setBodyParams(new String[]{"key", "verification", "studentId", "pwd", "type"}, new String[]{key, verification, studentId, studentPwd, code});
                sendPost(Constants.base_url + "/api/account/verification.do", APPLY_FOR_LOGIN_WITH_VERIFICATION_CODE, null);
                break;
        }

    }

    /**
     * 登陆验证
     */
    private void checkMessage() {
        String studentId = mEtUser.getText().toString();
        String studentPwd = mEtPwd.getText().toString();
        if (school.getSchool() == null || school.getSchool().isEmpty()) {
            Utils.toastShort(this, "请选择学校");
            return;
        } else if (studentId == null || studentId.isEmpty()) {
            Utils.toastShort(this, "学生账号不能为空");
            return;
        } else if (studentId == null || studentPwd.isEmpty()) {
            Utils.toastShort(this, "密码不能为空");
            return;
        }
        applyForLogin(APPLY_FOR_LOGIN);
    }

    /**
     * 验证码输入为空检查
     */
    private void checkMessageCode() {
        if ((verification = mEtVerificationcode.getText().toString()) != null && mEtVerificationcode.getText().toString().length() != 0) {
            applyForLogin(APPLY_FOR_LOGIN_WITH_VERIFICATION_CODE);
        } else {
            Utils.toastShort(this, "验证码不能为空");
        }
    }

    /**
     * 登陆
     */
    private void login() {
        switch (landingMode) {
            case APPLY_FOR_LOGIN://申请登录
                keepStudentMessage();
                checkMessage();
                break;
            case APPLY_FOR_LOGIN_WITH_VERIFICATION_CODE://验证码登陆
                checkMessageCode();
                break;
        }
    }

    /**
     * 保持学生信息
     */
    private void keepStudentMessage() {
        studentId = mEtUser.getText().toString();
        studentPwd = mEtPwd.getText().toString();
        code = school.getType() + "";
    }

    public void viewChanged() {
        mEtPwd.setVisibility(View.GONE);
        mEtUser.setVisibility(View.GONE);
        mLlSchool.setVisibility(View.GONE);
        mEtVerificationcode.setVisibility(View.VISIBLE);
    }

    /**
     * 登陆成功时用户信息的存取以及友盟的加载
     *
     * @param result 后台返回信息
     * @throws JSONException
     */
    private void successLogin(JSONObject result) throws JSONException {
        int ret = result.optInt("code");
        if (ret == 0) {
            JSONObject dd = new JSONObject(result.optString("data"));
            String token = dd.optString("token");
            int uid = dd.optInt("uid");
            Constants.uid = uid;
            memory.edit().putString("ID", studentId).putString("token", token).putInt("uid", uid).putBoolean("isLogin", true).apply();

            this.finish();
            startActivity(new Intent(mContext, MainActivity.class));
            Utils.toastShort(mContext, "登录成功");
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    /**
     * 网络请求成功
     *
     * @param result    返回结果
     * @param where     发送的请求码
     * @param fromCache
     * @throws JSONException
     */
    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        switch (where) {
            case APPLY_FOR_LOGIN://申请登陆
                mBtLogin.setText("验证登陆");
                JSONObject dd = new JSONObject(result.optString("data"));
                String url = dd.optString("URL");
                key = dd.optString("key");
                if (url == null || url.length() == 0) {
                    successLogin(result);
                    return;
                } else {
                    viewChanged();
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.bigger);
                    Uri uri = Uri.parse(WenConstans.BaseUrl + url);
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setUri(uri)
                            //动画支持，
                            .setAutoPlayAnimations(true)
                            .build();
                    mHead0.setVisibility(View.GONE);
                    mHead.setVisibility(View.VISIBLE);
                    mHead.setController(controller);
                    mHead.getHierarchy();
                    landingMode = APPLY_FOR_LOGIN_WITH_VERIFICATION_CODE;
                }
                Utils.toastShort(mContext, "登录成功");
                break;
            case APPLY_FOR_LOGIN_WITH_VERIFICATION_CODE://验证码申请登录
                successLogin(result);
                Log.e("ddq", result.getString("data") + "");
                break;
        }
    }

    /**
     * 网络请求失败
     *
     * @param result 返回结果
     * @param where
     */
    @Override
    protected void onFailure(String result, int where) {
        Log.e("ddq--", result);
        Utils.toastShort(this, "账号或密码错误");
    }


}
