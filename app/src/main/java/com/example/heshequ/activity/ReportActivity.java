package com.example.heshequ.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportActivity extends NetWorkActivity implements View.OnClickListener {
    private int type = 0;  //1 - 问问   2 - 团队
    private String id;
    private TextView tvTitle, tvSave;
    private EditText etContent;
    private String contennt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        init();
        event();
    }

    private void init() {
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getStringExtra("id");
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(type == 2 ? "举报团队" : "举报问问");
        tvSave = findViewById(R.id.tvSave);
        tvSave.setVisibility(View.GONE);
        etContent = findViewById(R.id.etContent);

    }

    private void event() {
        findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.btJb).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                this.finish();
                break;
            case R.id.btJb:
                contennt = etContent.getText().toString().trim();
                if (contennt.isEmpty()) {
                    Utils.toastShort(mContext, "请输入举报内容后再提交");
                    return;
                }
                setBodyParams(new String[]{"type", "id", "content"}
                        , new String[]{type + "", id, contennt});
                sendPost(WenConstans.WwJuBao, 100, WenConstans.token);
                break;
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == 100) {
            Utils.toastShort(mContext, "举报已提交");
            this.finish();
        }
    }

    @Override
    protected void onFailure(String result, int where) {

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
