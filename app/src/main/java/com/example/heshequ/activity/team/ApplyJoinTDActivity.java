package com.example.heshequ.activity.team;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplyJoinTDActivity extends NetWorkActivity implements View.OnClickListener {
    private TextView tvCancel,tvSave,tvTitle;
    private EditText etContent;
    private int id;
    private String content;
    private final  int join = 1000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_join_td);
        init();
        event();
    }

    private void init() {
        id = getIntent().getIntExtra("id",0);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        tvSave.setText("申请加入");

    }

    private void event() {
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCancel:
                this.finish();
                break;
            case R.id.tvSave:
                content = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)){
                    content = "";
                }
                setBodyParams(new String[]{"id", "content"}, new String[]{"" + id, "" + content});
                sendPost(Constants.base_url + "/api/club/base/savaSysUserNews.do", join, Constants.token);
                break;
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == join){
            MobclickAgent.onEvent(mContext,"event_teamApply");
            if (result.optInt("code")==0){
                Utils.toastShort(mContext,"申请成功");
                this.finish();
            }else{
                Utils.toastShort(mContext,result.optString("msg"));
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext,"网络异常");
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
