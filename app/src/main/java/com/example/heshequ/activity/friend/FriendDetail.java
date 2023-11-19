package com.example.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.activity.login.LoginActivity;
import com.example.heshequ.activity.team.MessageActivity;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.FriendListBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CircleView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by dell on 2020/3/29.
 */

public class FriendDetail extends NetWorkActivity implements View.OnClickListener {
    private CircleView ivHead;
    private ImageView xingbie;
    private TextView nicknametext, gexingqianming;
    private TextView friendsetTip3, friendsetTip4, friendsetTip5, friendsetTip6, friendsetTip7, friendsetTip9, friendsetTip10;
    private Button deletefriend, sendmessage;
    private static int hisid;
    private static int uid;
    private FriendListBean friendListBean;
    private String myheader;
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
        setContentView(R.layout.friend_myfrienddetail);
//        uid = WenConstans.id;
        uid = Constants.uid;
        friendListBean = new FriendListBean();
        friendListBean = (FriendListBean) getIntent().getSerializableExtra("Friend");
        String ids = uid + "";
        if (ids.equals("0")) {
            Intent intents = new Intent(FriendDetail.this, LoginActivity.class);

            startActivity(intents);
            Toast.makeText(this, "我们需要验证您的身份", Toast.LENGTH_LONG).show();
        }
        hisid = friendListBean.user_id;
        Log.e("得到hisid", hisid + "");
        OkHttpUtils.post(WenConstans.UserInfo)
                .tag(this)
                .headers(Constants.Token_Header, WenConstans.token)
                .params("id", uid + "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Log.e("gerenxinxi", "sresult:" + s);
                        try {
                            JSONObject result = new JSONObject(s);
                            JSONObject result1 = new JSONObject(result.optString("data"));
                            Log.e("gerenxinxi", "code:" + result1.optString("header"));
                            String touxiang = result1.optString("header");
                            myheader = touxiang;
                        } catch (JSONException e) {
                            Log.e("shanchufanhuixinxii", "JSONException: " + e.toString());
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {

                        super.onError(call, response, e);
                        Log.e("gerenxinxi", "onError Exception: " + e.toString());
                    }
                });
        init();
        event();
    }

    private void init() {
        setText("好友详情");
        friendsetTip3 = (TextView) findViewById(R.id.friendsetTip3);
        friendsetTip4 = (TextView) findViewById(R.id.friendsetTip4);
        friendsetTip5 = (TextView) findViewById(R.id.friendsetTip5);
        friendsetTip6 = (TextView) findViewById(R.id.friendsetTip6);
        friendsetTip7 = (TextView) findViewById(R.id.friendsetTip7);
        friendsetTip9 = (TextView) findViewById(R.id.friendsetTip9);
        friendsetTip10 = (TextView) findViewById(R.id.friendsetTip10);
        deletefriend = (Button) findViewById(R.id.deletefriend);
        sendmessage = (Button) findViewById(R.id.sendmessage);
        dongtai = (LinearLayout) findViewById(R.id.dongtai);
        ivHead = (CircleView) findViewById(R.id.ivHead);
        xingbie = (ImageView) findViewById(R.id.xingbie);
        nicknametext = (TextView) findViewById(R.id.nicknametext);
        gexingqianming = (TextView) findViewById(R.id.gexingqianming);
        getData();
    }

    private void event() {
        deletefriend.setOnClickListener(this);
        sendmessage.setOnClickListener(this);
        findViewById(R.id.ivBack).setOnClickListener(this);
        dongtai.setOnClickListener(this);
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.e("好友详情返回结果", result.toString());
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
                xingbie.setImageResource(R.mipmap.me19);
            } else {
                xingbie.setImageResource(R.mipmap.me36);
            }
            if (nicheng1 != null) {
                nicknametext.setText(nicheng1);
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
                gexingqianming.setText(gexing1);
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
                Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId=" + touxiang1).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("showset", "" + Constants.base_url + "/info/file/pub.do?fileId=" + touxiang1);
            } else {
                ivHead.setImageResource(R.mipmap.head3);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.dongtai:
                Intent intent1 = new Intent();
                intent1.putExtra("uid", "" + hisid);
                intent1.setClass(FriendDetail.this, OthersDynamic.class);
                startActivity(intent1);
                break;
            case R.id.sendmessage:
                Intent intent3 = new Intent();
                intent3.putExtra("hisid", hisid);
                intent3.putExtra("nickname", "" + nicheng1);
                intent3.putExtra("myheader", myheader);
                intent3.setClass(FriendDetail.this, MessageActivity.class);
                startActivity(intent3);
                break;
            case R.id.deletefriend:
                OkHttpUtils.post(WenConstans.DeleteFriend)
                        .tag(this)
                        .headers(Constants.Token_Header, WenConstans.token)
                        .params("user_id", hisid + "")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                Log.e("gerenxinxi", "sresult:" + s);
                                try {
                                    JSONObject result = new JSONObject(s);
                                    int ret = result.getInt("code");
                                    if (ret == 0) {
                                        //String msg = result.getString("setQues");
                                        //Log.e("shanchufanhuixinxi", "" + msg);

                                        Toast.makeText(FriendDetail.this, "删除成功", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(FriendDetail.this, "删除失败", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Log.e("shanchufanhuixinxii", "JSONException: " + e.toString());
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
        }
    }

    private void getData() {
        Log.e("uid: " + uid, "hisid: " + hisid);
        setBodyParams(new String[]{"uid", "hisid"}, new String[]{"" + uid, "" + hisid});
        sendPost(WenConstans.FriendInfo, 1, WenConstans.token);
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
