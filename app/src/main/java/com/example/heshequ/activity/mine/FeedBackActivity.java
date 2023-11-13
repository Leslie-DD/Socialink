package com.example.heshequ.activity.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.CategoryBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 意见反馈页面
 */
public class FeedBackActivity extends NetWorkActivity implements View.OnClickListener {
    private TextView tvCancel, tvSave;
    private LinearLayout llType;
    private TextView tvType;
    private EditText etContent;
    private OptionsPickerView pvOptions;
    private final int getCode = 1001;
    private final int sendCode = 1002;
    private ArrayList<CategoryBean> categorys;
    private int cp = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        init();
        event();
    }

    private void init() {
        setText("意见反馈");
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvType = (TextView) findViewById(R.id.tvType);
        llType = (LinearLayout) findViewById(R.id.llType);
        etContent = (EditText) findViewById(R.id.etContent);
        tvSave.setText("提交");
        //获取反馈类型
        setBodyParams(new String[]{"type"}, new String[]{"feedback"});
        sendConnection(HttpRequest.HttpMethod.POST, Constants.base_url + "/api/pub/category/list.do",
                new String[]{}, new String[]{}, getCode, false, Constants.token);
    }

    private void event() {
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        llType.setOnClickListener(this);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 512) {
                    Utils.toastShort(mContext, "已达到最大字符上限");
                }
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
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        Log.e("YSF", "shezhi le :" + categorys.get(options1).getValue());
                        tvType.setText(categorys.get(options1).getValue() + "");
                        cp = options1;
                    }
                }).setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setTitleSize(20)//标题文字大小
                        .setTitleText("选择反馈类型")//标题文字
                        .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.parseColor("#00BBFF"))//确定按钮文字颜色
                        .setCancelColor(Color.parseColor("#00BBFF"))//取消按钮文字颜色
                        .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                        .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                        .build();
                ArrayList<String> opts = new ArrayList<>();
                for (int i = 0; i < categorys.size(); i++) {
                    opts.add(categorys.get(i).getValue());
                }
                pvOptions.setPicker(opts);
                tvType.setText(categorys.get(0).getValue());
                cp = 0;
                break;
            case sendCode:
                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_feedBack");
                Utils.toastShort(mContext, "感谢您的反馈,我们会及时处理！");
                finish();
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                this.finish();
                break;
            case R.id.llType:
                if (pvOptions != null) {
                    pvOptions.show();
                }
                break;
            case R.id.tvSave:
                String content = etContent.getText().toString().trim();
                if (content.length() == 0) {
                    Utils.toastShort(mContext, "请先输入您的反馈意见！");
                    return;
                }
                if (cp == -1) {
                    return;
                }
                setBodyParams(new String[]{"category", "content"}, new String[]{categorys.get(cp).getId() + "", content});
                sendPost(Constants.base_url + "/api/user/feedback.do", sendCode, Constants.token);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
