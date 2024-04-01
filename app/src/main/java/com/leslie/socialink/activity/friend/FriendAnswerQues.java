package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;

import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;


public class FriendAnswerQues extends NetWorkActivity {
    private int id;
    private String answer_1;
    private String answer_2;
    private String answer_3;
    private String answer_4;
    private String answer_5;
    private TextView question1, question2, question3, question4, question5;
    private EditText answer1, answer2, answer3, answer4, answer5;
    private TextView tvCancel, tvSave;
    private String hisid;
    private String[] content = {"", "", "", "", ""};
    private int ques_num = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendanswerques);
        Intent intent1 = getIntent();
        hisid = intent1.getStringExtra("hisid");
        id = Constants.uid;
        init();
        event();
    }

    private void init() {
        setText("ta的试卷");
        question1 = (TextView) findViewById(R.id.question1);
        question2 = (TextView) findViewById(R.id.question2);
        question3 = (TextView) findViewById(R.id.question3);
        question4 = (TextView) findViewById(R.id.question4);
        question5 = (TextView) findViewById(R.id.question5);
        answer1 = (EditText) findViewById(R.id.answer1);
        answer2 = (EditText) findViewById(R.id.answer2);
        answer3 = (EditText) findViewById(R.id.answer3);
        answer4 = (EditText) findViewById(R.id.answer4);
        answer5 = (EditText) findViewById(R.id.answer5);
        getData();
    }

    private void event() {
        findViewById(R.id.tvCancel).setOnClickListener(v -> finish());
        findViewById(R.id.tvSave).setOnClickListener(v -> {
            answer_1 = answer1.getText().toString();
            answer_2 = answer2.getText().toString();
            answer_3 = answer3.getText().toString();
            answer_4 = answer4.getText().toString();
            answer_5 = answer5.getText().toString();
            String answer_list[] = {answer_1, answer_2, answer_3, answer_4, answer_5};
            JSONArray answerlist = new JSONArray();
            for (int i = 0; i < ques_num; i++) {
                answerlist.put(getTerm("" + hisid, content[i] + "", "" + answer_list[i]));
            }
//                answerlist.put(getTerm(""+hisid,content[0]+"",""+answer_1));
//                answerlist.put(getTerm(""+hisid,content[1]+"",""+answer_2));
//                answerlist.put(getTerm(""+hisid,content[2]+"",""+answer_3));
//                answerlist.put(getTerm(""+hisid,content[3]+"",""+answer_4));
//                answerlist.put(getTerm(""+hisid,content[4]+"",""+answer_5));
            OkHttpUtils.post(Constants.ANSWER_SAVE)
                    .tag(this)
                    .headers(Constants.TOKEN_HEADER, Constants.token)
                    .params("answerlist", answerlist + "")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                            Log.e("gerenxinxi", "sresult:" + s);
                            try {
                                JSONObject result = new JSONObject(s);

                                String msg = result.getString("msg");
                                Utils.toastShort(FriendAnswerQues.this, msg);
                            } catch (JSONException e) {
                                Log.e("gerenxinxi", "JSONException: " + e.toString());
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {

                            super.onError(call, response, e);
                            Log.e("个人信息", "onError Exception: " + e.toString());
                        }
                    });
        });

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        if (ret == 0) {
            JSONArray data = result.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject value = data.getJSONObject(i);
                //获取到title值
                content[i] = value.getString("content");
            }
            if (content[0] != null) {
                question1.setText(content[0]);
                ques_num = ques_num + 1;
            }
            if (content[1] != null) {
                question2.setText(content[1]);
                ques_num = ques_num + 1;
            }
            if (content[2] != null) {
                question3.setText(content[2]);
                ques_num = ques_num + 1;
            }
            if (content[3] != null) {
                question4.setText(content[3]);
                ques_num = ques_num + 1;
            }
            if (content[4] != null) {
                question5.setText(content[4]);
                ques_num = ques_num + 1;
            }
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    JSONObject getTerm(String hisid, String quescontent, String answercontent) {
        JSONObject Term = new JSONObject();
        try {


            Term.put("queid", hisid);
            Term.put("ques_content", quescontent);
            Term.put("ans_content", answercontent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Term;

    }

    private void getData() {
        setBodyParams(new String[]{"user_id"}, new String[]{"" + hisid});
        sendPost(Constants.BASE_URL + "/api/social/getquest.do", 100, Constants.token);
    }
}

