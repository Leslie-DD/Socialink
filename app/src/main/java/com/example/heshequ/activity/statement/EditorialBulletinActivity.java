package com.example.heshequ.activity.statement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefTDteamEvent;
import com.example.heshequ.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class EditorialBulletinActivity extends NetWorkActivity implements View.OnClickListener {
    private int type;   // 1 = 创建； 2 = 编辑;
    private int id;
    private TextView tvCancel, tvSave;
    private EditText etTitle;
    private EditText etContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorial_bulletin);
        init();
        event();
    }

    private void init() {
        type = getIntent().getIntExtra("type", 0);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        if (type == 1) {
            setText("发布公告");
        } else if (type == 2) {
            setText("编辑公告");
            tvSave.setText("保存");
            id = getIntent().getIntExtra("id", 0);
            etTitle.setText(getIntent().getStringExtra("title"));
            etTitle.setSelection(etTitle.getText().toString().trim().length());
            etContent.setText(getIntent().getStringExtra("content"));
            etContent.setSelection(etContent.getText().toString().trim().length());
        }
    }

    private void event() {
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 32) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == 1000) {
            if (result.optInt("code") == 0) {
                if (type == 1) {
                    this.finish();
                    EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 3}));
                } else if (type == 2) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", etTitle.getText().toString().trim());
                    bundle.putString("content", etContent.getText().toString().trim());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    this.finish();
                    EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 3}));
                }
            }
            Utils.toastShort(this, result.optString("msg"));
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
            case R.id.tvSave:
                if (type == 1) {
                    String title = etTitle.getText().toString();
                    String content = etContent.getText().toString();
                    if (TextUtils.isEmpty(title)) {
                        Utils.toastShort(mContext, "标题不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(content)) {
                        Utils.toastShort(mContext, "内容不能为空");
                        return;
                    }
                    setBodyParams(new String[]{"clubId", "title", "content"}, new String[]{"" + Constants.clubId, title, content});
                    sendPost(Constants.base_url + "/api/club/notice/save.do", 1000, Constants.token);
                } else if (type == 2) {
                    String title = etTitle.getText().toString();
                    String content = etContent.getText().toString();
                    if (TextUtils.isEmpty(title)) {
                        Utils.toastShort(mContext, "标题不能为空");
                        return;
                    }
                    if (TextUtils.isEmpty(content)) {
                        Utils.toastShort(mContext, "内容不能为空");
                        return;
                    }
                    setBodyParams(new String[]{"id", "clubId", "title", "content"}, new String[]{"" + id, "" + Constants.clubId, title, content});
                    sendPost(Constants.base_url + "/api/club/notice/update.do", 1000, Constants.token);
                }
                break;
        }
    }


    @Override
    public void onPause() {
        super.onPause();

    }
}
