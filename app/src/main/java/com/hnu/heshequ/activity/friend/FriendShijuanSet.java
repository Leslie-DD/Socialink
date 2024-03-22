package com.hnu.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FriendShijuanSet extends NetWorkActivity {
    private String question_1;
    private String question_2;
    private String question_3;
    private String question_4;
    private String question_5;
    private EditText question1, question2, question3, question4, question5;
    private JSONArray questionlist;
    private TextView tvCancel, tvSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shijuanset);
        init();
        event();
    }

    private void init() {
        question1 = (EditText) findViewById(R.id.question1);
        question2 = (EditText) findViewById(R.id.question2);
        question3 = (EditText) findViewById(R.id.question3);
        question4 = (EditText) findViewById(R.id.question4);
        question5 = (EditText) findViewById(R.id.question5);
    }

    private void event() {
        findViewById(R.id.tvCancel).setOnClickListener(v -> finish());
        findViewById(R.id.tvSave).setOnClickListener(v -> {
            question_1 = question1.getText().toString();
            question_2 = question2.getText().toString();
            question_3 = question3.getText().toString();
            question_4 = question4.getText().toString();
            question_5 = question5.getText().toString();
            JSONArray questionlist = new JSONArray();
            questionlist.put(getTerm(1, question_1 + ""));
            questionlist.put(getTerm(2, question_2 + ""));
            questionlist.put(getTerm(3, question_3 + ""));
            questionlist.put(getTerm(4, question_4 + ""));
            questionlist.put(getTerm(5, question_5 + ""));
            setData(questionlist);
            Intent intent2 = new Intent(FriendShijuanSet.this, FriendSet.class);
            startActivity(intent2);
            finish();
        });
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        if (ret == 0) {
            Utils.toastShort(mContext, result.optString("msg"));
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    JSONObject getTerm(int question_num, String content) {
        JSONObject Term = new JSONObject();
        try {


            Term.put("question_num", question_num);

            Term.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Term;

    }

    private void setData(JSONArray questionlist) {
        setBodyParams(new String[]{"questionlist"}, new String[]{"" + questionlist});
        sendPost(Constants.base_url + "/api/social/modifyquest.do", 100, WenConstans.token);
    }


}
