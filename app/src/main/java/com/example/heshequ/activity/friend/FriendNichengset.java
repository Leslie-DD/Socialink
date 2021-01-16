package com.example.heshequ.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class FriendNichengset extends NetWorkActivity implements View.OnClickListener{
    private EditText nicheng;
    public static int sex1;
    public static String school;
    public static String touxiang;
    public static int settingClub =1;
    public static int settingAsk =1;
    private static String nickname;
    private Button set;

    private boolean isSet = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_nichengset);
        Intent intent1 = getIntent();
        Log.e("sexchuanguolaidezhi",""+intent1.getStringExtra("sex"));
        sex1 = Integer.parseInt(intent1.getStringExtra("sex"));
        school = intent1.getStringExtra("school");
        touxiang = intent1.getStringExtra("file");

        /**
         * 定义未修改昵称时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i=new Intent();
        i.putExtra("nicheng", "");
        setResult(0,i);

        init();
        event();
    }
    private void init() {
        setText("昵称设置");
        nicheng = (EditText) findViewById(R.id.nicheng);
        set = (Button) findViewById(R.id.set);
    }
    private void event(){
        findViewById(R.id.ivBack).setOnClickListener(this);
        nicheng.setOnClickListener(this);
        set.setOnClickListener(this);
    }
    @Override
    protected void onFailure(String result, int where) {

    }
    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        isSet = true;
        if (ret == 0) {
            Utils.toastShort(mContext, result.optString("msg"));
            Intent i=new Intent();
            i.putExtra("nicheng", nicheng.getText().toString());
            setResult(1,i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
            Intent i=new Intent();
            i.putExtra("nicheng", "");
            setResult(2,i);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
//                Intent intent = new Intent(FriendNichengset.this,FriendSet.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.set:
                nickname=nicheng.getText().toString();
                getData();
                break;
        }
    }
    private void getData() {
        setBodyParams(new String[]{"file","nickname","sex","college", "settingClub","settingAsk"}, new String[]{""+touxiang,""+nickname,""+sex1,""+school,""+settingClub,""+settingAsk});
        sendPost(Constants.base_url+"/api/user/update.do", 100,WenConstans.token);
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
