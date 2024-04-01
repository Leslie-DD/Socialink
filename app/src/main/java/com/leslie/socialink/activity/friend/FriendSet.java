package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.login.LoginActivity;
import com.leslie.socialink.adapter.listview.FriendfiltrateAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.FriendBean;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.UserInfoBean;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.CircleView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;


public class FriendSet extends NetWorkActivity {
    private CircleView ivHead;
    private FriendfiltrateAdapter adapter;
    private FriendBean bean1;
    private WindowManager.LayoutParams layoutParams;
    private PopupWindow modifyPop;
    public String academy;
    public String nickname;
    public String birthday;
    public String college;
    public static int id;
    private PopupWindow pop;
    private UserInfoBean userInfoBean;
    public String descroption;
    public int emotion;
    public String header;
    public String hometown;
    public String profession;
    public String schoolgrate;
    public static String touxiang1 = "";
    private Gson gson;
    private final int jibenxinxi = 1001;
    private final int uesrxinxi = 1000;
    private LinearLayout nicheng, xingbie, shengri, xuexiao, xueyuan, zhuanye, nianji, gexing, qingan, jiaxiang, shijuan, dongtai;
    private TextView friendsetTip1, friendsetTip2, friendsetTip3, friendsetTip4, friendsetTip5, friendsetTip6, friendsetTip7, friendsetTip8, friendsetTip9, friendsetTip10, friendsetTip11;
    public static int sex;
    public static String nianji1;
    public static String zhuanye1;
    public static String nicheng1;
    public static String jiaxiang1;

    public static int qingan1;
    public static String gexing1;
    public static String daxue1;
    public static String shengri1;
    public static String xueyuan1;
    private static Double longitude;
    private static Double latitude;
    //    public static String nicheng1,shengri1,xuehao1,xueyuan1,zhuanye1,nianji1,gexing1,jiaxiang1;
//    public static int sex1,qingan1;
    private static Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        Intent intentt = getIntent();
//        id = Constants.uid;  //这里取不到正确的用户id  导致会跳转到登陆界面
        id = Constants.uid;
        longitude = intentt.getDoubleExtra("longitude", 0);
        latitude = intentt.getDoubleExtra("latitude", 0);
        String ids = id + "";
        Log.e("ids", ids + "");
        if (ids.equals("0")) {
            Intent intents = new Intent(FriendSet.this, LoginActivity.class);
            startActivity(intents);
            Utils.toastShort(this, "我们需要验证您的身份");
        }
        Log.e("经纬度", "" + latitude + "+" + longitude);
        init();
        event();
    }

    private void init() {
        bean1 = new FriendBean();
        MyThread myThread = new MyThread();
        myThread.start();
        try {
            myThread.join();
        } catch (Exception e) {
            Log.e("抛出错误", "" + e.toString());
        }
        //
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpUtils.post(WenConstans.InitUserinfo)
//                        .tag(this)
//                        .headers(Constants.Token_Header, Constants.token)
//                        .params("id", id+"")
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(String s, Call call, Response response) {
//                                Log.e("gerenxinxi", "sresult:" + s);
//                                try {
//                                    JSONObject result = new JSONObject(s);
//                                    JSONObject result1 = new JSONObject(result.optString("data"));
//                                    Log.e("gerenxinxi", "code:" + result1.optString("header"));
//                                    String touxiang = result1.optString("header");
//                                    touxiang1 = touxiang;
//                                    bean1.SetHeader(touxiang);
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
//            }
//        }).start();
        //
        ivHead = (CircleView) findViewById(R.id.ivHead);
        nicheng = (LinearLayout) findViewById(R.id.nicheng);
        xingbie = (LinearLayout) findViewById(R.id.xingbie);
        shengri = (LinearLayout) findViewById(R.id.shengri);
        xuexiao = (LinearLayout) findViewById(R.id.xuexiao);
        xueyuan = (LinearLayout) findViewById(R.id.xueyuan);
        zhuanye = (LinearLayout) findViewById(R.id.zhuanye);
        nianji = (LinearLayout) findViewById(R.id.nianji);
        gexing = (LinearLayout) findViewById(R.id.gexing);
        qingan = (LinearLayout) findViewById(R.id.qingan);
        jiaxiang = (LinearLayout) findViewById(R.id.jiaxiang);
        shijuan = (LinearLayout) findViewById(R.id.shijuan);
        dongtai = (LinearLayout) findViewById(R.id.dongtai);
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
        setText("交友信息设置");
        getData();
        event();
    }

    public static class MyThread extends Thread {
        @Override
        public void run() {
            OkHttpUtils.post(WenConstans.InitUserinfo)
                    .tag(this)
                    .headers(Constants.Token_Header, Constants.token)
                    .params("id", id + "")
                    .execute(new StringCallback() {
                        @Override               // 重写AbsCallback<String>的onSuccess方法
                        public void onSuccess(String s, Call call, Response response) {
                            Log.e("个人信息", "sresult:" + s);
                            try {
                                JSONObject result = new JSONObject(s);
                                JSONObject result1 = new JSONObject(result.optString("data"));
                                String touxiang = result1.optString("header");
                                touxiang1 = touxiang;
                                Log.e("个人信息头像", "code:" + touxiang1);
                            } catch (JSONException e) {
                                Log.e("个人信息抛出错误", "JSONException: " + e.toString());
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            Log.e("个人信息抛出错误", "onError Exception: " + e.toString());
                        }
                    });
        }
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        nicheng.setOnClickListener(v -> {
            Log.e("sexdezhi", "" + sex);
            Intent intent1 = new Intent();
            intent1.putExtra("file", touxiang1 + "");
            intent1.putExtra("sex", sex + "");
            intent1.putExtra("school", daxue1 + "");
            intent1.setClass(FriendSet.this, FriendNichengset.class);
            startActivityForResult(intent1, 0);
        });
        xingbie.setOnClickListener(v -> {
            Intent intent2 = new Intent();
            intent2.putExtra("file", touxiang1 + "");
            intent2.putExtra("school", "" + daxue1);
            intent2.putExtra("nickname", nicheng1 + "");
            intent2.setClass(FriendSet.this, Friendxingbieset.class);
            startActivityForResult(intent2, 1);
        });
        shengri.setOnClickListener(v -> {
            Intent intent3 = new Intent();
            intent3.putExtra("gexing", "" + gexing1);
            intent3.putExtra("id", "" + id);
            intent3.setClass(FriendSet.this, FriendShengriSet.class);
            startActivityForResult(intent3, 2);
        });
        xuexiao.setOnClickListener(v -> {
            Intent intent4 = new Intent();
            intent4.putExtra("file", touxiang1 + "");
            intent4.putExtra("sex", sex + "");
            intent4.putExtra("nickname", nicheng1 + "");
            intent4.setClass(FriendSet.this, FriendXuexiaoSet.class);
            startActivityForResult(intent4, 3);
        });
        xueyuan.setOnClickListener(v -> {
            Intent intent5 = new Intent();
            intent5.putExtra("jiaxiang", "" + jiaxiang1);
            intent5.putExtra("qingan", "" + qingan1);
            intent5.putExtra("zhuanye", "" + zhuanye1);
            intent5.putExtra("nianji", "" + nianji1);
            intent5.putExtra("id", "" + id);
            intent5.setClass(FriendSet.this, FriendXueyuanSet.class);
            startActivityForResult(intent5, 4);
        });
        zhuanye.setOnClickListener(v -> {
            Intent intent6 = new Intent();
            intent6.putExtra("jiaxiang", "" + jiaxiang1);
            intent6.putExtra("qingan", "" + qingan1);
            intent6.putExtra("xueyuan", "" + xueyuan1);
            intent6.putExtra("nianji", nianji1);
            intent6.putExtra("id", "" + id);
            intent6.setClass(FriendSet.this, FriendZhuanyeSet.class);
            startActivityForResult(intent6, 5);
        });
        nianji.setOnClickListener(v -> {
            Intent intent7 = new Intent();
            intent7.putExtra("jiaxiang", "" + jiaxiang1);
            intent7.putExtra("qingan", "" + qingan1);
            intent7.putExtra("zhuanye", "" + zhuanye1);
            intent7.putExtra("xueyuan", xueyuan1);
            intent7.putExtra("id", "" + id);
            intent7.setClass(FriendSet.this, FriendNianjiSet.class);
        });
        gexing.setOnClickListener(v -> {
            Intent intent8 = new Intent();
            intent8.putExtra("shengri", "" + shengri1);
            intent8.putExtra("id", "" + id);
            intent8.setClass(FriendSet.this, FriendGexingSet.class);
            startActivityForResult(intent8, 7);
        });
        qingan.setOnClickListener(v -> {
            Intent intent9 = new Intent();
            intent9.putExtra("jiaxiang", "" + jiaxiang1);
            intent9.putExtra("nianji", "" + nianji1);
            intent9.putExtra("zhuanye", "" + zhuanye1);
            intent9.putExtra("xueyuan", xueyuan1);
            intent9.putExtra("id", "" + id);
            intent9.setClass(FriendSet.this, FriendQinganSet.class);
            startActivityForResult(intent9, 8);
        });
        jiaxiang.setOnClickListener(v -> {
            Intent intent10 = new Intent();
            intent10.putExtra("nianji", "" + nianji1);
            intent10.putExtra("qingan", "" + qingan1);
            intent10.putExtra("zhuanye", "" + zhuanye1);
            intent10.putExtra("xueyuan", xueyuan1);
            intent10.putExtra("id", "" + id);
            intent10.setClass(FriendSet.this, FriendJiaxiangSet.class);
            startActivityForResult(intent10, 9);
        });
        shijuan.setOnClickListener(v -> {
            Intent intent11 = new Intent(FriendSet.this, FriendShijuanShow.class);
            intent11.putExtra("id", id + "");
            intent11.setClass(FriendSet.this, FriendShijuanShow.class);
            startActivity(intent11);
        });
        dongtai.setOnClickListener(v -> {
            Intent intent12 = new Intent();
            intent12.setClass(FriendSet.this, MyDynamic.class);
            startActivity(intent12);
        });

    }

    private void initPop() {
        gson = new Gson();
        pop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);

        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        pop.setOutsideTouchable(true);
        // 设置焦点
        pop.setFocusable(true);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
            }
        });
        // 设置所在布局
        pop.setContentView(pv);
        modifyPop = new PopupWindow(Constants.screenW - Utils.dip2px(mContext, 80), WindowManager.LayoutParams.WRAP_CONTENT);
        View v = LayoutInflater.from(mContext).inflate(R.layout.tklayout, null);
        v.findViewById(R.id.ivHead).setVisibility(View.GONE);

        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        modifyPop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        modifyPop.setOutsideTouchable(true);
        // 设置焦点
        modifyPop.setFocusable(true);
        modifyPop.setOnDismissListener(() -> {
            layoutParams.alpha = 1f;
            getWindow().setAttributes(layoutParams);
        });

        // 设置所在布局
        modifyPop.setContentView(v);
    }

    @Override
    protected void onFailure(String result, int where) {
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        switch (where) {
            case jibenxinxi:
                Log.e("result", " " + result.toString());
                int ret = result.optInt("code");
                Log.e("result.optInt(code)", "" + ret);
                if (ret == 0) {
                    Log.e("result.optString()", result.optString("data") + "");
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
                    Log.e("个人信息头像header", "" + bean1.getHeader());
                    Log.e("个人信息头像touxiang1", "" + touxiang1);

                    if (touxiang1 != null) {
                        Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId=" + touxiang1).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                        Log.e("showset", "" + Constants.base_url + "/info/file/pub.do?fileId=" + touxiang1);
                    } else {
                        ivHead.setImageResource(R.mipmap.head3);
                    }

                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }

        }
    }

    private void getData() {
        setBodyParams(new String[]{"uid", "hisid"}, new String[]{"" + id, "" + id});
        sendPost(WenConstans.FriendInfo, jibenxinxi, Constants.token);
    }

    /**
     * 监听每个设置页面是否修改了个人信息
     * 如果有修改，更新此页面的信息展示
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == 0) {
                String newNiCheng = data.getStringExtra("nicheng");
//            Utils.toastShort(mContext, "未修改昵称：" + newNiCheng);
            } else if (resultCode == 1) {
                String newNiCheng = data.getStringExtra("nicheng");
//            Utils.toastShort(mContext, "昵称修改成功：" + newNiCheng);
                friendsetTip1.setText(newNiCheng);
            } else {// if(requestCode==0 && (resultCode == 2 || resultCode == 1))
                String newNiCheng = data.getStringExtra("nicheng");
//            Utils.toastShort(mContext, "昵称未修改或修改失败");
            }
        } else if (requestCode == 1) {
            if (resultCode == 0) {
//            Utils.toastShort(mContext, "未修改性别：" + newNiCheng);
            } else if (resultCode == 1) {
                int newXingbie = data.getIntExtra("xingbie", 0);
                if (newXingbie == 1) {
                    friendsetTip2.setText("男");
                } else if (newXingbie == 2) {
                    friendsetTip2.setText("女");
                } else {
                    friendsetTip2.setText("未知");
                }
//            Utils.toastShort(mContext, "性别修改成功：" + newXingbie);
            } else {
//                int newXingbie = data.getIntExtra("xingbie", 0);
//                Utils.toastShort(mContext, "性别未修改或修改失败");
            }
        } else if (requestCode == 2 && resultCode == 1) {
            String newShengri = data.getStringExtra("shengri");
            friendsetTip3.setText(newShengri);
        } else if (requestCode == 3 && resultCode == 1) {
            String newXuexiao = data.getStringExtra("xuexiao");
            friendsetTip4.setText(newXuexiao);
        } else if (requestCode == 4 && resultCode == 1) {
            String newXueyuan = data.getStringExtra("xueyuan");
            friendsetTip5.setText(newXueyuan);
        } else if (requestCode == 5 && resultCode == 1) {
            String newZhuanye = data.getStringExtra("zhuanye");
            friendsetTip6.setText(newZhuanye);
        } else if (requestCode == 6 && resultCode == 1) {
            String newNianji = data.getStringExtra("nianji");
            friendsetTip7.setText(newNianji);
        } else if (requestCode == 7 && resultCode == 1) {
            String newGexing = data.getStringExtra("gexing");
            friendsetTip8.setText(newGexing);
        } else if (requestCode == 8 && resultCode == 1) {
            int newQinggan = data.getIntExtra("qinggan", 0);
            if (newQinggan == 0) {
                friendsetTip9.setText("保密");
            } else if (newQinggan == 1) {
                friendsetTip9.setText("单身");
            } else {
                friendsetTip9.setText("恋爱");
            }
        } else if (requestCode == 9 && resultCode == 1) {
            String newJiaxiang = data.getStringExtra("jiaxiang");
            friendsetTip10.setText(newJiaxiang);
        }


        if (resultCode == 1) {
            Log.e("已收藏页面", "监听到 未收藏页面，添加收藏");
        }
    }
}
