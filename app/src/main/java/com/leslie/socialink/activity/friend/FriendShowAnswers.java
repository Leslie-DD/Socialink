package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.leslie.socialink.R;
import com.leslie.socialink.activity.login.LoginActivity;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.FriendAddNewsBean;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.QuestionAnswerItem;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;


public class FriendShowAnswers extends NetWorkActivity {
    private int user_id;
    private QuestionAnswerItem questionAnswerItem1, questionAnswerItem2, questionAnswerItem3, questionAnswerItem4, questionAnswerItem5;
    private FriendAddNewsBean friendAddNewsBean;
    private Button ivaccept, ivreject;
    private String[] content = {"", "", "", "", ""};
    private String[] ans_content = {"", "", "", "", ""};
    private int ques_num = 0;
    private static int uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        uid = WenConstans.id;
        uid = Constants.uid;
        String ids = uid + "";
        if (ids.equals("0")) {
            Intent intents = new Intent(FriendShowAnswers.this, LoginActivity.class);

            startActivity(intents);
            Utils.toastShort(this, "查看好友回答页面，我们需要验证您的身份");
        }
        setContentView(R.layout.activity_showanswer);
        friendAddNewsBean = new FriendAddNewsBean();
        friendAddNewsBean = (FriendAddNewsBean) getIntent().getSerializableExtra("FriendAdd");
        user_id = friendAddNewsBean.getReplyId();
        init();
        event();
    }

    private void init() {
        setText("ta的试卷");
        questionAnswerItem1 = findViewById(R.id.questionAnswerItem1);
        questionAnswerItem2 = findViewById(R.id.questionAnswerItem2);
        questionAnswerItem3 = findViewById(R.id.questionAnswerItem3);
        questionAnswerItem4 = findViewById(R.id.questionAnswerItem4);
        questionAnswerItem5 = findViewById(R.id.questionAnswerItem5);
        ivaccept = (Button) findViewById(R.id.ivaccept);
        ivreject = (Button) findViewById(R.id.ivreject);
        getData();
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        ivreject.setOnClickListener(v -> OkHttpUtils.post(WenConstans.RejectFriend)
                .tag(this)
                .headers(Constants.Token_Header, WenConstans.token)
                .params("user_id", user_id + "")
                .params("id", "" + WenConstans.id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Log.e("gerenxinxi", "sresult:" + s);
                        try {
                            JSONObject result = new JSONObject(s);

                            String msg = result.getString("msg");
                            Utils.toastShort(mContext, result.optString("msg"));
                        } catch (JSONException e) {
                            Log.e("gerenxinxi", "JSONException: " + e.toString());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("gerenxinxi", "onError Exception: " + e.toString());
                    }
                }));
        ivaccept.setOnClickListener(v -> OkHttpUtils.post(WenConstans.AcceptFriend)
                .tag(this)
                .headers(Constants.Token_Header, WenConstans.token)
                .params("user_id", user_id + "")
//                        .params("id",""+WenConstans.id)
                .params("id", "" + Constants.uid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Log.e("gerenxinxi", "sresult:" + s);
                        try {
                            JSONObject result = new JSONObject(s);
                            Utils.toastShort(mContext, result.optString("msg"));
                        } catch (JSONException e) {
                            Log.e("gerenxinxi", "JSONException: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("gerenxinxi", "onError Exception: " + e.toString());
                    }
                }));
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
                content[i] = value.getString("ques_content");
                ans_content[i] = value.getString("ans_content");
            }
            if (content[0] != null) {
                questionAnswerItem1.setQuestion(content[0]);
                questionAnswerItem1.setAnswer(ans_content[0]);
                ques_num = ques_num + 1;
            }
            if (content[1] != null) {
                questionAnswerItem2.setQuestion(content[1]);
                questionAnswerItem2.setAnswer(content[1]);
                ques_num = ques_num + 1;
            }
            if (content[2] != null) {
                questionAnswerItem3.setQuestion(content[2]);
                questionAnswerItem3.setAnswer(ans_content[2]);
                ques_num = ques_num + 1;
            }
            if (content[3] != null) {
                questionAnswerItem4.setQuestion(content[3]);
                questionAnswerItem4.setAnswer(ans_content[4]);
                ques_num = ques_num + 1;
            }
            if (content[4] != null) {
                questionAnswerItem5.setQuestion(content[4]);
                questionAnswerItem4.setAnswer(ans_content[4]);
                ques_num = ques_num + 1;
            }
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    private void getData() {
        setBodyParams(new String[]{"user_id"}, new String[]{"" + user_id});
        sendPost(Constants.base_url + "/api/social/getPaper.do", 100, WenConstans.token);
    }


}
