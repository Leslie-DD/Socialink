package com.leslie.socialink.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportActivity extends NetWorkActivity {
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
        findViewById(R.id.tvCancel).setOnClickListener(v -> finish());
        findViewById(R.id.btJb).setOnClickListener(v -> {
            contennt = etContent.getText().toString().trim();
            if (contennt.isEmpty()) {
                Utils.toastShort(mContext, "请输入举报内容后再提交");
                return;
            }
            setBodyParams(new String[]{"type", "id", "content"}
                    , new String[]{type + "", id, contennt});
            sendPost(WenConstans.WwJuBao, 100, Constants.token);
        });
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

}
