package com.leslie.socialink.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leslie.socialink.MeetApplication;
import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.entity.RefUserInfo;
import com.leslie.socialink.entity.TestBean;
import com.leslie.socialink.launcher.MainActivity;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.UserInfoBean;
import com.leslie.socialink.utils.EncryptUtils;
import com.leslie.socialink.utils.SharedPreferencesHelp;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.FlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LabelSelectionActivity extends NetWorkActivity {
    private FlowLayout flowLayout;
    private TextView tvTitle, tvSkip;
    private boolean skip = true;
    private Button btStart;
    private boolean start = true;
    private LinearLayout llMale, llFemale;
    private ImageView ivMale, ivFemale;
    private TextView tvMale, tvFemale;
    private int sex;
    private ArrayList<LableBean> datas;
    private Gson gson;
    private final int loginCode = 1000;
    private String pwd;
    private String phone;
    private ArrayList<UserInfoBean.UserLabelsBean> userLabelsBeans;
    /**
     * 显示的文字
     */
    private ArrayList<TestBean> testData;
    private List<String> stringList;
    private int type = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labelelection);
        init();
        event();
    }

    private void init() {
        gson = new Gson();
        stringList = new ArrayList<>();
        type = getIntent().getIntExtra("type", 0);
        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("标签选择");
        tvSkip = (TextView) findViewById(R.id.tvSkip);
        tvMale = (TextView) findViewById(R.id.tvMale);
        tvFemale = (TextView) findViewById(R.id.tvFemale);
        ivMale = (ImageView) findViewById(R.id.ivMale);
        ivFemale = (ImageView) findViewById(R.id.ivFemale);
        llMale = (LinearLayout) findViewById(R.id.llMale);
        llFemale = (LinearLayout) findViewById(R.id.llFemale);
        btStart = (Button) findViewById(R.id.btStart);
        setBg(0);
        getLableData();
        if (type == 2) {

            btStart.setText("确定");
            tvSkip.setVisibility(View.GONE);
            userLabelsBeans = (ArrayList<UserInfoBean.UserLabelsBean>) getIntent().getSerializableExtra("userLabelsBeans");
            if (userLabelsBeans != null && userLabelsBeans.size() > 0) {
                for (int i = 0; i < userLabelsBeans.size(); i++) {
                    stringList.add(userLabelsBeans.get(i).getLabel());
                }
            }
        }
    }

    private void getLableData() {
        setBodyParams(new String[]{"type"}, new String[]{"label"});
        sendPost(Constants.base_url + "/api/pub/category/list.do", 10086, Constants.token);
    }

    private void event() {
        llMale.setOnClickListener(v -> setBg(0));
        llFemale.setOnClickListener(v -> setBg(1));
        btStart.setOnClickListener(v -> {
            if (start) {
                start = false;
                if (stringList.size() == 0) {
                    Utils.toastShort(mContext, "您还没有选择标签");
                    start = true;
                    return;
                }
                String lables = "";
                for (int i = 0; i < stringList.size(); i++) {
                    lables = lables + stringList.get(i) + ",";
                }
                lables = lables.substring(0, lables.length() - 1);
                setBodyParams(new String[]{"sex", "labels"}
                        , new String[]{sex + 1 + "", lables});
                sendPost(Constants.base_url + "/api/user/label.do", 66, Constants.token);
            }
        });
        tvSkip.setOnClickListener(v -> {
            if (skip) {
                skip = false;
                if (type == 2) {
                    this.finish();
                } else if (type == 1) {
                    //logo
                    /*MeetApplication.getInstance().finishAll();
                    startActivity(new Intent(mContext, MainActivity.class));*/
                    setBodyParams(new String[]{"phone", "pwd"}, new String[]{phone, EncryptUtils.encryptMD5ToString(pwd)});
                    sendPost(Constants.base_url + "/api/account/login.do", loginCode, null);
                }
            }
        });
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        testData = new ArrayList<TestBean>();
    }

    private void setTvBg(TextView view, int status) {
        if (status == 0) {
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).equals(view.getText().toString())) {
                    stringList.remove(i);
                }
            }
//            stringList.remove(view.getText().toString());
        } else {
            stringList.add(view.getText().toString());
        }
        view.setBackgroundResource(status == 0 ? R.drawable.e6e6e6_17 : R.drawable.bg_2cd22b_17);
        String color = status == 0 ? "#999999" : "#ffffff";
        view.setTextColor(Color.parseColor(color));
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            start = true;
            skip = true;
            return;
        }
        if (where == 66) {
            start = true;
            if (type == 2) {
                Utils.toastShort(mContext, "标签设置成功");
                EventBus.getDefault().post(new RefUserInfo());
                finish();
            } else if (type == 1) {
                //logo
                setBodyParams(new String[]{"phone", "pwd"}, new String[]{phone, EncryptUtils.encryptMD5ToString(pwd)});
                sendPost(Constants.base_url + "/api/account/login.do", loginCode, null);
            }
        } else if (where == 10086) {
            if (result.optInt("code") == 0) {
                datas = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<LableBean>>() {
                }.getType());
                if (datas != null && datas.size() > 0) {


                    for (LableBean b : datas) {
                        // 循环添加TextView到容器
                        TestBean bean = new TestBean();
                        if (type == 2) {
                            if (userLabelsBeans != null && userLabelsBeans.size() > 0) {
                                for (int j = 0; j < userLabelsBeans.size(); j++) {
                                    if (b.getValue().equals(userLabelsBeans.get(j).getLabel())) {
                                        bean.setStatus(1);
                                    }
                                }
                            }
                        }
                        bean.setName(b.getValue());
                        testData.add(bean);
                        final TextView view = new TextView(this);
                        view.setText(b.getValue());
                        view.setTextColor(Color.parseColor(bean.getStatus() == 0 ? "#999999" : "#ffffff"));
                        view.setBackgroundResource(bean.getStatus() == 0 ? R.drawable.e6e6e6_17 : R.drawable.bg_2cd22b_17);
                        view.setHeight(Utils.dip2px(context, 34));
                        view.setPadding(Utils.dip2px(context, 17), 0, Utils.dip2px(context, 17), 0);
                        view.setGravity(Gravity.CENTER);
                        view.setTextSize(14);
                        view.setTag(b.getValue());
                        // 设置点击事件
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String text = view.getText().toString();
                                int index = -1;
                                for (int j = 0; j < testData.size(); j++) {
                                    TestBean bean = testData.get(j);
                                    if (bean.getName().equals(text)) {
                                        bean.setStatus(Math.abs(bean.getStatus() - 1));
                                        index = j;
                                        break;
                                    }
                                }
                                setTvBg(view, testData.get(index).getStatus());
                            }
                        });
                        flowLayout.addView(view);
                    }
                }
            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        } else if (where == loginCode) {
            skip = true;
            int ret = result.optInt("code");
            if (ret == 0) {
                JSONObject dd = new JSONObject(result.optString("data"));
                String token = dd.optString("token");
                int uid = dd.optInt("uid");
                SharedPreferencesHelp.getEditor().putString("token", token).putInt("uid", uid).putBoolean("isLogin", true).apply();
                MeetApplication.getInstance().finishAll();
                startActivity(new Intent(mContext, MainActivity.class));
                Utils.toastShort(mContext, "登录成功");
            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    private void setBg(int sex) {
        this.sex = sex;
        ivMale.setImageResource(sex == 0 ? R.mipmap.male1 : R.mipmap.male2);
        ivFemale.setImageResource(sex == 1 ? R.mipmap.female1 : R.mipmap.female2);
        String c1 = "@color/light_gray";
        String c2 = "#999999";
        tvMale.setTextColor(Color.parseColor(sex == 0 ? c1 : c2));
        tvFemale.setTextColor(Color.parseColor(sex == 1 ? c1 : c2));

    }

    public class LableBean {
        /**
         * id : 1
         * category : label
         * value : 湖南大学
         */

        private int id;
        private String category;
        private String value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
