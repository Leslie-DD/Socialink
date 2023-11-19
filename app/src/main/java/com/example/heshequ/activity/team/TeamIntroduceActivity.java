package com.example.heshequ.activity.team;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.EventBean;
import com.example.heshequ.entity.RefStatementEvent;
import com.example.heshequ.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class TeamIntroduceActivity extends NetWorkActivity implements View.OnClickListener {
    private static final String TAG = "[TeamIntroduceActivity]";
    private TextView tvCancel, tvSave, tvTitle;
    private EditText etIntroduce;
    private String content;
    private int teamId = 0;
    private final int upcode = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_introduce);

        init();
        event();
    }

    private void init() {
        teamId = getIntent().getIntExtra("teamId", 0);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("团队介绍");
        etIntroduce = (EditText) findViewById(R.id.etContent);
        String introduce = getIntent().getStringExtra("introduce");
        if (!introduce.contains("该团队很懒")) {
            etIntroduce.setText(introduce);
            etIntroduce.setSelection(introduce.length());
        } else {
            etIntroduce.setHint(introduce);
        }
    }

    private void event() {
        tvSave.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                this.finish();
                break;
            case R.id.tvSave:
                content = etIntroduce.getText().toString();
                if (content.length() == 0) {
                    Utils.toastShort(mContext, "请先输入团队介绍");
                    return;
                }
                setBodyParams(new String[]{"id", "introduction"}, new String[]{"" + teamId, "" + content});
                sendPost(Constants.base_url + "/api/club/base/update.do", upcode, Constants.token);
                break;
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.e(TAG, "" + result);
        if (where == upcode) {
            switch (result.optInt("code")) {
                case 0:
                    EventBean bean = new EventBean();
                    bean.setIntroduce(content);
                    EventBus.getDefault().post(bean);
                    EventBus.getDefault().post(new RefStatementEvent());
                    Utils.toastShort(mContext, "保存成功");
                    this.finish();
                    break;
                case 1:
                    Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                    break;
                case 2:
                    Utils.toastShort(this, result.optString("msg"));
                    break;
                case 3:
                    Utils.toastShort(this, "您没有该功能操作权限");
                    break;
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "网络异常");
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
