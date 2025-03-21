package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class Friendxingbieset extends NetWorkActivity {
    private RadioGroup nRg1;

    public static String school;
    public static String touxiang;
    public static String nickname;
    public static String sexcontent;
    public static int settingClub = 1;
    public static int settingAsk = 1;
    private int sexnum;
    private Button set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_xingbieset);
        nRg1 = findViewById(R.id.rg_1);
        nRg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                sexcontent = radioButton.getText().toString();
                Utils.toastShort(Friendxingbieset.this, sexcontent);
            }
        });
        Intent intent2 = getIntent();
        school = intent2.getStringExtra("school");
        touxiang = intent2.getStringExtra("file");
        nickname = intent2.getStringExtra("nickname");

        /**
         * 定义未修改性别时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("xingbie", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("性别设置");
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            Log.e("sexcontent", "" + sexcontent);
            if (sexcontent.equals("男")) {
                sexnum = 1;
            } else if (sexcontent.equals("女")) {
                sexnum = 2;
            }
            Log.e("sexnum", "" + sexnum);
            getData();
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
            Intent i = new Intent();
            i.putExtra("xingbie", sexnum);
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
            Intent i = new Intent();
            i.putExtra("xingbie", sexnum);
            setResult(2, i);
        }
    }

    private void getData() {
        setBodyParams(new String[]{"file", "nickname", "sex", "college", "settingClub", "settingAsk"}, new String[]{"" + touxiang, "" + nickname, "" + sexnum, "" + school, "" + settingClub, "" + settingAsk});
        sendPost(Constants.BASE_URL + "/api/user/update.do", 100, Constants.token);
    }

}
