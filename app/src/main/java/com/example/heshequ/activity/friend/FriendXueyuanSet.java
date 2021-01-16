package com.example.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dell on 2020/4/30.
 */

public class FriendXueyuanSet extends NetWorkActivity implements View.OnClickListener {
    private EditText xueyuan;
    private String jiaxiang;
    private int qingan;
    private String zhuanye;
    private String nianji;
    private String xueyuan1;
    private int id;
    private Button set;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_xueyuanset);
        Intent intent1 = getIntent();
        qingan = Integer.parseInt(intent1.getStringExtra("qingan"));
        jiaxiang = intent1.getStringExtra("jiaxiang");
        zhuanye = intent1.getStringExtra("zhuanye");
        nianji = intent1.getStringExtra("nianji");
//        id = WenConstans.id;
        id = Constants.uid;
        Toast.makeText(this, "用户 id:"+id, Toast.LENGTH_LONG ).show();

        /**
         * 定义未修改学院时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i=new Intent();
        i.putExtra("xueyuan", "");
        setResult(0,i);

        init();
        event();
    }
    private void init() {
        setText("学院设置");
        xueyuan = (EditText) findViewById(R.id.xueyuan);
        set = (Button) findViewById(R.id.set);
    }
    private void event(){
        findViewById(R.id.ivBack).setOnClickListener(this);
        xueyuan.setOnClickListener(this);
        set.setOnClickListener(this);
    }
    @Override
    protected void onFailure(String result, int where) {

    }
    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        if (ret == 0) {
            Utils.toastShort(mContext, result.optString("msg"));
            Intent i=new Intent();
            i.putExtra("xueyuan", xueyuan.getText().toString());
            setResult(1,i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
//                Intent intent = new Intent(FriendXueyuanSet.this,FriendSet.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.set:
                xueyuan1=xueyuan.getText().toString();
                getData();
                break;
        }
    }
    private void getData() {
        setBodyParams(new String[]{"hometown","emotion","academy","profession", "schoolgrade","user_id"}, new String[]{""+jiaxiang,""+qingan,""+xueyuan1,""+zhuanye,""+nianji,""+id});
        sendPost(Constants.base_url+"/api/social/updateinfor.do", 100, WenConstans.token);
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

