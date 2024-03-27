package com.hnu.heshequ.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.CategoryBean;
import com.hnu.heshequ.bean.QuestionBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DefaultProblemActivity extends NetWorkActivity {
    private TextView tvQuestion1, tvQuestion2, tvQuestion3, tvTitle, tvSkip, tvHint;
    private EditText etAnswer1, etAnswer2, etAnswer3;
    private Button btSub;
    private final int getCode = 100;
    private final int getCode2 = 101;
    private final int getCode3 = 102;
    private final int saveCode = 1001;
    private final int myQuestion = 1002;
    private ArrayList<CategoryBean> categorys, categorys2, categorys3;
    private OptionsPickerView pvOptions1;
    private OptionsPickerView pvOptions2;
    private OptionsPickerView pvOptions3;
    private int cp = -1;
    private Gson gson = new Gson();
    private int type = 0;   // 1 = 注册   2 = 设置
    private ArrayList<QuestionBean> qd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_problem);
        init();
        event();
    }

    private void init() {
        type = getIntent().getIntExtra("type", 0);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("预设问题");
        tvQuestion1 = (TextView) findViewById(R.id.tvQuestion1);
        tvQuestion2 = (TextView) findViewById(R.id.tvQuestion2);
        tvQuestion3 = (TextView) findViewById(R.id.tvQuestion3);
        tvHint = findViewById(R.id.tvHint);
        tvSkip = (TextView) findViewById(R.id.tvSkip);
        etAnswer1 = (EditText) findViewById(R.id.etAnswer1);
        etAnswer2 = (EditText) findViewById(R.id.etAnswer2);
        etAnswer3 = (EditText) findViewById(R.id.etAnswer3);
        btSub = (Button) findViewById(R.id.btSub);
        if (type == 2) {
            initIsQ();
            tvSkip.setText("跳过");
        } else if (type == 1) {
            getQ();
        }

    }

    private void initIsQ() {
        sendPost(Constants.base_url + "/api/user/myQuestion.do", myQuestion, Constants.token);
    }

    private void getQ() {
        setBodyParams(new String[]{"type"}, new String[]{"question1"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", getCode, Constants.token);
        setBodyParams(new String[]{"type"}, new String[]{"question2"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", getCode2, Constants.token);
        setBodyParams(new String[]{"type"}, new String[]{"question3"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", getCode3, Constants.token);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        tvQuestion1.setOnClickListener(v -> {
            if (pvOptions1 != null) {
                pvOptions1.show();
            }
        });
        tvQuestion2.setOnClickListener(v -> {
            if (pvOptions2 != null) {
                pvOptions2.show();
            }
        });
        tvQuestion3.setOnClickListener(v -> {
            if (pvOptions3 != null) {
                pvOptions3.show();
            }
        });
        btSub.setOnClickListener(v -> {
            String q1 = etAnswer1.getText().toString();
            String q2 = etAnswer2.getText().toString();
            String q3 = etAnswer3.getText().toString();
            if (TextUtils.isEmpty(q1) || TextUtils.isEmpty(q2) || TextUtils.isEmpty(q3)) {
                Utils.toastShort(mContext, "请把答案输入完整");
                return;
            }
            JSONArray array = new JSONArray();
            try {
                JSONObject obj1 = new JSONObject();
                obj1.put("content", tvQuestion1.getText().toString() + "");
                obj1.put("answer", q1 + "");
                array.put(obj1);
                JSONObject obj2 = new JSONObject();
                obj2.put("content", tvQuestion2.getText().toString() + "");
                obj2.put("answer", q2 + "");
                array.put(obj2);
                JSONObject obj3 = new JSONObject();
                obj3.put("content", tvQuestion3.getText().toString() + "");
                obj3.put("answer", q3 + "");
                array.put(obj3);
                setBodyParams(new String[]{"questions"}
                        , new String[]{array.toString() + ""});
                sendPost(Constants.base_url + "/api/user/question.do", 66, Constants.token);
            } catch (Exception e) {

            }
        });
        tvSkip.setOnClickListener(v -> {
            if (type == 2) {
                this.finish();
            } else {
                startActivity(new Intent(this, LabelSelectionActivity.class)
                        .putExtra("type", 1)
                        .putExtra("phone", getIntent().getStringExtra("phone"))
                        .putExtra("pwd", getIntent().getStringExtra("pwd"))
                );
            }
        });
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        if (ret != 0) {
            Utils.toastShort(mContext, result.optString("msg"));
            return;
        }
        Gson gs = new Gson();
        switch (where) {
            case getCode:
                categorys = gs.fromJson(result.getString("data"), new TypeToken<List<CategoryBean>>() {
                }.getType());
                if (categorys == null) {
                    return;
                }
                pvOptions1 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        Log.e("YSF", "shezhi le :" + categorys.get(options1).getValue());
                        tvQuestion1.setText(categorys.get(options1).getValue() + "");
                        cp = options1;
                    }
                }).setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setTitleSize(20)//标题文字大小
                        .setTitleText("选择问题")//标题文字
                        .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.parseColor("#2CD22B"))//确定按钮文字颜色
                        .setCancelColor(Color.parseColor("#00BBFF"))//取消按钮文字颜色
                        .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                        .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                        .build();
                ArrayList<String> opts = new ArrayList<>();
                for (int i = 0; i < categorys.size(); i++) {
                    opts.add(categorys.get(i).getValue());
                }
                pvOptions1.setPicker(opts);
                cp = 0;
                break;
            case getCode2:
                categorys2 = gs.fromJson(result.getString("data"), new TypeToken<List<CategoryBean>>() {
                }.getType());
                if (categorys2 == null) {
                    return;
                }
                pvOptions2 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        Log.e("YSF", "shezhi le :" + categorys2.get(options1).getValue());
                        tvQuestion2.setText(categorys2.get(options1).getValue() + "");
                        cp = options1;
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
                opts = new ArrayList<>();
                for (int i = 0; i < categorys2.size(); i++) {
                    opts.add(categorys2.get(i).getValue());
                }
                pvOptions2.setPicker(opts);
                break;
            case getCode3:
                categorys3 = gs.fromJson(result.getString("data"), new TypeToken<List<CategoryBean>>() {
                }.getType());
                if (categorys3 == null) {
                    return;
                }
                pvOptions3 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        Log.e("YSF", "shezhi le :" + categorys3.get(options1).getValue());
                        tvQuestion3.setText(categorys3.get(options1).getValue() + "");
                        cp = options1;
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
                opts = new ArrayList<>();
                for (int i = 0; i < categorys3.size(); i++) {
                    opts.add(categorys3.get(i).getValue());
                }
                pvOptions3.setPicker(opts);
                break;
            case 66:
                if (result.optInt("code") == 0) {
                    Utils.toastShort(mContext, "设置成功");
                    if (type == 2) {
                        finish();
                    } else if (type == 1) {
                        //进标签选择
                        startActivity(new Intent(this, LabelSelectionActivity.class)
                                .putExtra("type", 1)
                                .putExtra("phone", getIntent().getStringExtra("phone"))
                                .putExtra("pwd", getIntent().getStringExtra("pwd"))
                        );
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case myQuestion:
                if (result.optInt("code") == 0) {
                    qd = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<QuestionBean>>() {
                    }.getType());
                    if (qd != null && qd.size() > 0) {
                        if (type == 2) {

                            for (int i = 0; i < qd.size(); i++) {
                                switch (i) {
                                    case 0:
                                        tvQuestion1.setText(qd.get(0).getContent());
                                        break;
                                    case 1:
                                        tvQuestion2.setText(qd.get(1).getContent());
                                        break;
                                    case 2:
                                        tvQuestion3.setText(qd.get(2).getContent());
                                        break;
                                }
                            }
                            etAnswer1.setText("******");
                            etAnswer2.setText("******");
                            etAnswer3.setText("******");
                            tvQuestion1.setEnabled(false);
                            tvQuestion2.setEnabled(false);
                            tvQuestion3.setEnabled(false);
                            etAnswer1.setEnabled(false);
                            etAnswer2.setEnabled(false);
                            etAnswer3.setEnabled(false);
                            btSub.setVisibility(View.INVISIBLE);
                            tvSkip.setVisibility(View.INVISIBLE);
                            tvHint.setText("预设问题已设置");
                        }
                    } else {
                        getQ();
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "网络异常");
    }

}
