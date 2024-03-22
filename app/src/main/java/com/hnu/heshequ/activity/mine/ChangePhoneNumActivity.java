package com.hnu.heshequ.activity.mine;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.listview.CpqAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.QuestionBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.utils.MatcherUtils;
import com.hnu.heshequ.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChangePhoneNumActivity extends NetWorkActivity {
    private TextView tvTitle, tvCancel, tvSchool;
    private EditText etPhone, etPwd, etCode;
    private String phone, pwd, code;
    private TextView tvCode;
    private Button btSub;
    private final int getCode = 1000;
    private int count = 30;
    private boolean getting;
    private final int myQuestion = 1001;
    private PopupWindow pop;
    private WindowManager.LayoutParams layoutParams;
    private ImageView ivClose;
    private ListView lv;
    private CpqAdapter cpqAdapter;
    private ArrayList<QuestionBean> data;
    private Gson gson = new Gson();
    private OptionsPickerView pvOptions1;
    private int cp;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            count--;
            if (count <= 0) {
                count = 30;
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
        setContentView(R.layout.activity_change_phone_num);
        init();
        event();
    }

    private void init() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("更改手机号");
        btSub = (Button) findViewById(R.id.btSub);
        tvCode = (TextView) findViewById(R.id.tvCode);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etCode = (EditText) findViewById(R.id.etCode);
        etPwd = (EditText) findViewById(R.id.etPwd);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSchool = (TextView) findViewById(R.id.tvSchool);
        initQ();
        initPop();
    }

    private void initPop() {
        pop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.list_changephone_q, null);
        ivClose = (ImageView) pv.findViewById(R.id.ivClose);
        lv = (ListView) pv.findViewById(R.id.lv);
        ivClose.setOnClickListener(v -> {
            if (pop != null) {
                pop.dismiss();
            }
        });
        cpqAdapter = new CpqAdapter(this);
        lv.setAdapter(cpqAdapter);
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        pop.setOutsideTouchable(true);
        // 设置焦点
        pop.setFocusable(true);
        pop.setOnDismissListener(() -> {
            layoutParams.alpha = 1f;
            getWindow().setAttributes(layoutParams);
        });
        // 设置所在布局
        pop.setContentView(pv);
    }

    //初始化问题
    private void initQ() {
        sendPost(Constants.base_url + "/api/user/myQuestion.do", myQuestion, Constants.token);
    }

    public void showPop() {
        if (pvOptions1 != null) {
            pvOptions1.show();
        }
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        btSub.setOnClickListener(v -> {
            phone = etPhone.getText().toString().trim();
            String code = etCode.getText().toString();
            String content = etPwd.getText().toString();
            String title = tvSchool.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                Utils.toastShort(this, "手机号不能为空");
                return;
            }
            if (TextUtils.isEmpty(code)) {
                Utils.toastShort(this, "验证码不能为空");
                return;
            }
            if (title.equals("请选择问题名称")) {
                Utils.toastShort(this, "请选择问题名称");
                return;
            }
            if (TextUtils.isEmpty(content)) {
                Utils.toastShort(this, "答案不能为空");
                return;
            }
            setBodyParams(new String[]{"phone", "code", "questionId", "answer"}
                    , new String[]{phone, code, data.get(cp).getId() + "", content});
            sendPost(Constants.base_url + "/api/user/updatePhone.do", 66, Constants.token);
        });
        tvCancel.setOnClickListener(v -> finish());
        tvCode.setOnClickListener(v -> {
            phone = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                Utils.toastShort(this, "手机号不能为空");
                return;
            }
            if (!MatcherUtils.isPhone(phone)) {
                Utils.toastShort(mContext, "请输入正确的手机号码！");
                return;
            }
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
            tvCode.setEnabled(false);
            setBodyParams(new String[]{"phone"}, new String[]{phone});
            sendPost(Constants.base_url + "/api/account/scode.do", getCode, null);
        });
        tvSchool.setOnClickListener(v -> showPop());
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        switch (where) {
            case getCode:
                if (ret == 0) {
                    Utils.toastShort(this, "获取验证码成功");
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case myQuestion:
                if (ret == 0) {
                    data = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<QuestionBean>>() {
                    }.getType());
                    if (data != null && data.size() > 0) {
                        pvOptions1 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                            @Override
                            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                                //返回的分别是三个级别的选中位置
                                cp = options1;
                                tvSchool.setText(data.get(options1).getContent() + "");
                            }
                        }).setCancelText("取消")//取消按钮文字
                                .setSubmitText("确定")//确认按钮文字
                                .setTitleSize(20)//标题文字大小
                                .setTitleText("选择问题")//标题文字
                                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                                .setTitleColor(Color.BLACK)//标题文字颜色
                                .setSubmitColor(Color.parseColor("#00BBFF"))//确定按钮文字颜色
                                .setCancelColor(Color.parseColor("#00BBFF"))//取消按钮文字颜色
                                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                                .build();
                        ArrayList<String> opts = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            opts.add(data.get(i).getContent());
                        }
                        pvOptions1.setPicker(opts);
                    }
                }
                break;
            case 66:
                if (ret == 0) {
                    this.finish();
                    Utils.toastShort(mContext, "更改成功");
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "网络错误");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
