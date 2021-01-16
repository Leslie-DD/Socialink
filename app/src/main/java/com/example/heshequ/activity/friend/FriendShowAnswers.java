package com.example.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshequ.activity.login.LoginActivity;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.FriendAddNewsBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.R;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dell on 2020/5/11.
 */

public class FriendShowAnswers extends NetWorkActivity implements View.OnClickListener {
    private int user_id;
    private String answer_1;
    private String answer_2;
    private String answer_3;
    private String answer_4;
    private String answer_5;
    private TextView question1,question2,question3,question4,question5;
    private TextView answer1,answer2,answer3,answer4,answer5;
    private FriendAddNewsBean friendAddNewsBean;
    private Button ivaccept,ivreject;
    private String[] content={"","","","",""};
    private String[] ans_content={"","","","",""};
    private int ques_num=0;
    private static int uid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        uid = WenConstans.id;
        uid = Constants.uid;
        String ids = uid+"";
        if(ids.equals("0")){
            Intent intents  = new Intent(FriendShowAnswers.this, LoginActivity.class);

            startActivity(intents);
            Toast.makeText(this, "查看好友回答页面，我们需要验证您的身份", Toast.LENGTH_LONG ).show();
        }
        setContentView(R.layout.activity_showanswer);
        friendAddNewsBean = new FriendAddNewsBean();
        friendAddNewsBean= (FriendAddNewsBean) getIntent().getSerializableExtra("FriendAdd");
        user_id = friendAddNewsBean.getReplyId();
        init();
        event();
    }
    private void init() {
        setText("ta的试卷");
        question1 = (TextView)findViewById(R.id.question1);
        question2 =(TextView) findViewById(R.id.question2);
        question3 = (TextView)findViewById(R.id.question3);
        question4 = (TextView) findViewById(R.id.question4);
        question5=(TextView) findViewById(R.id.question5);
        answer1 = (TextView)findViewById(R.id.answer1);
        answer2 =(TextView) findViewById(R.id.answer2);
        answer3 = (TextView)findViewById(R.id.answer3);
        answer4 = (TextView) findViewById(R.id.answer4);
        answer5=(TextView) findViewById(R.id.answer5);
        ivaccept = (Button)findViewById(R.id.ivaccept);
        ivreject =(Button)findViewById(R.id.ivreject);
        getData();
    }
    private void event(){
        findViewById(R.id.ivBack).setOnClickListener(this);
        ivreject.setOnClickListener(this);
        ivaccept.setOnClickListener(this);
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
            if(content[0]!=null){
                question1.setText(content[0]);
                answer1.setText(ans_content[0]);
                ques_num=ques_num+1;
            }
            if(content[1]!=null){
                question2.setText(content[1]);
                answer2.setText(ans_content[1]);
                ques_num=ques_num+1;
            }
            if(content[2]!=null){
                question3.setText(content[2]);
                answer3.setText(ans_content[2]);
                ques_num=ques_num+1;
            }
            if(content[3]!=null){
                question4.setText(content[3]);
                answer4.setText(ans_content[4]);
                ques_num=ques_num+1;
            }
            if(content[4]!=null){
                question5.setText(content[4]);
                answer5.setText(ans_content[4]);
                ques_num=ques_num+1;
            }
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivaccept:
                OkHttpUtils.post(WenConstans.AcceptFriend)
                        .tag(this)
                        .headers(Constants.Token_Header, WenConstans.token)
                        .params("user_id", user_id+"")
//                        .params("id",""+WenConstans.id)
                        .params("id",""+Constants.uid)
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
                        });
                break;
            case R.id.ivreject:
                OkHttpUtils.post(WenConstans.RejectFriend)
                        .tag(this)
                        .headers(Constants.Token_Header, WenConstans.token)
                        .params("user_id", user_id+"")
                        .params("id",""+WenConstans.id)
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
                        });
                break;

//                String answer_list[] = {answer_1,answer_2,answer_3,answer_4,answer_5};
//                JSONArray answerlist = new JSONArray();
//                for(int i =0;i<ques_num;i++){
//                    answerlist.put(getTerm(""+hisid,content[i]+"",""+answer_list[i]));
//                }
////                answerlist.put(getTerm(""+hisid,content[0]+"",""+answer_1));
////                answerlist.put(getTerm(""+hisid,content[1]+"",""+answer_2));
////                answerlist.put(getTerm(""+hisid,content[2]+"",""+answer_3));
////                answerlist.put(getTerm(""+hisid,content[3]+"",""+answer_4));
////                answerlist.put(getTerm(""+hisid,content[4]+"",""+answer_5));
//                OkHttpUtils.post(WenConstans.AnswerSave)
//                        .tag(this)
//                        .headers(Constants.Token_Header, WenConstans.token)
//                        .params("answerlist", answerlist+"")
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(String s, Call call, Response response) {
//
//                                Log.e("gerenxinxi", "sresult:" + s);
//                                try {
//                                    JSONObject result = new JSONObject(s);
//
//                                    String msg = result.getString("msg");
//
//                                } catch (JSONException e) {
//                                    Log.e("gerenxinxi", "JSONException: " + e.toString());
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                            @Override
//                            public void onError(Call call, Response response, Exception e) {
//
//                                super.onError(call, response, e);
//                                Log.e("gerenxinxi", "onError Exception: " + e.toString());
//                            }
//                        });
//                break;

        }
    }

    private void getData() {
        setBodyParams(new String[]{"user_id"}, new String[]{""+user_id});
        sendPost(Constants.base_url+"/api/social/getPaper.do", 100, WenConstans.token);
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
