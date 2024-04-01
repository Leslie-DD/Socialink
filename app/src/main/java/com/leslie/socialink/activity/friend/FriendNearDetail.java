package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.login.LoginActivity;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.FriendListBean;

import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.CircleView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;


public class FriendNearDetail extends NetWorkActivity {
    private FriendListBean friendListBean;
    private TextView friendsetTip1, friendsetTip2, friendsetTip3, friendsetTip4, friendsetTip5, friendsetTip6, friendsetTip7, friendsetTip8, friendsetTip9, friendsetTip10, friendsetTip11;
    private static int hisid;
    private static int uid;
    private CircleView ivHead;
    private Button set;
    private LinearLayout dongtai;
    public static int sex;
    public static String nianji1;
    public static String zhuanye1;
    public static String nicheng1;
    public static String jiaxiang1;
    public static String touxiang1;
    public static int qingan1;
    public static String gexing1;
    public static String daxue1;
    public static String shengri1;
    public static String xueyuan1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendneardetail);

        friendListBean = new FriendListBean();
        friendListBean = (FriendListBean) getIntent().getSerializableExtra("FriendNear");
//        uid = Constants.uid;
        uid = Constants.uid;
        String ids = uid + "";
        if (ids.equals("0")) {
            Intent intents = new Intent(FriendNearDetail.this, LoginActivity.class);

            startActivity(intents);
            Utils.toastShort(this, "用户详情页面，我们需要验证您的身份");
        }
        hisid = friendListBean.user_id;
        init();
        event();
    }

    private void init() {
        setText("用户详情");
        ivHead = (CircleView) findViewById(R.id.ivHead);
        friendsetTip1 = (TextView) findViewById(R.id.friendsetTip1);
        friendsetTip2 = (TextView) findViewById(R.id.friendsetTip2);
        friendsetTip3 = (TextView) findViewById(R.id.friendsetTip3);
        friendsetTip4 = (TextView) findViewById(R.id.friendsetTip4);
        friendsetTip5 = (TextView) findViewById(R.id.friendsetTip5);
        friendsetTip6 = (TextView) findViewById(R.id.friendsetTip6);
        friendsetTip7 = (TextView) findViewById(R.id.friendsetTip7);
        friendsetTip8 = (TextView) findViewById(R.id.friendsetTip8);
        friendsetTip9 = (TextView) findViewById(R.id.friendsetTip9);
        friendsetTip10 = (TextView) findViewById(R.id.friendsetTip10);
        friendsetTip11 = (TextView) findViewById(R.id.friendsetTip11);
        set = (Button) findViewById(R.id.set);
        getData();

    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> OkHttpUtils.post(Constants.JUDGE_SET_QUESTIONS)
                .tag(this)
                .headers(Constants.TOKEN_HEADER, Constants.token)
                .params("uid", hisid + "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("gerenxinxi", "sresult:" + s);
                        try {
                            JSONObject result = new JSONObject(s);
                            JSONObject dd = new JSONObject(result.optString("data"));
                            String setques = dd.getString("setQues");
                            Log.e("setques", "" + setques);
                            if (setques.equals("true")) {
                                Intent intent1 = new Intent();
                                intent1.putExtra("hisid", hisid + "");
                                intent1.setClass(FriendNearDetail.this, FriendAnswerQues.class);
                                startActivity(intent1);

                            } else {
                                Utils.toastShort(FriendNearDetail.this, "这位用户没有设置试卷，您不能加他为好友");
                            }
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
            JSONObject dd = new JSONObject(result.optString("data"));
            String token = dd.optString("token");
            sex = dd.optInt("sex");
            nianji1 = dd.optString("schoolgrade");
            zhuanye1 = dd.optString("profession");
            nicheng1 = dd.optString("nickname");
            jiaxiang1 = dd.optString("hometown");
            touxiang1 = dd.optString("header");
            qingan1 = dd.optInt("emotion");
            gexing1 = dd.optString("descroption");
            daxue1 = dd.optString("college");
            shengri1 = dd.optString("birthday");
            xueyuan1 = dd.optString("academy");
            if (sex == 1) {
                friendsetTip2.setText("男");
            } else {
                friendsetTip2.setText("女");
            }
            if (nicheng1 != null) {
                friendsetTip1.setText(nicheng1);
            }
            if (shengri1 != null) {
                friendsetTip3.setText(shengri1);
            }
            if (daxue1 != null) {
                friendsetTip4.setText(daxue1);
            }
            if (xueyuan1 != null) {
                friendsetTip5.setText(xueyuan1);
            }
            if (zhuanye1 != null) {
                friendsetTip6.setText(zhuanye1);
            }
            if (nianji1 != null) {
                friendsetTip7.setText(nianji1);
            }
            if (gexing1 != null) {
                friendsetTip8.setText(gexing1);
            }
            if (qingan1 == 0) {
                friendsetTip9.setText("保密");
            } else if (qingan1 == 1) {
                friendsetTip9.setText("单身");
            } else {
                friendsetTip9.setText("恋爱");
            }
            if (jiaxiang1 != null) {
                friendsetTip10.setText(jiaxiang1);
            }
            Log.e("gerenxinxitouxiang", "touxiang " + touxiang1);

            if (touxiang1 != null) {
                Glide.with(context).load(Constants.BASE_URL + "/info/file/pub.do?fileId=" + touxiang1).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("showset", "" + Constants.BASE_URL + "/info/file/pub.do?fileId=" + touxiang1);
            } else {
                ivHead.setImageResource(R.mipmap.head3);
            }
        }
    }

    private void getData() {
        setBodyParams(new String[]{"uid", "hisid"}, new String[]{"" + uid, "" + hisid});
        sendPost(Constants.FRIEND_INFO, 1, Constants.token);
    }


}
