package com.leslie.socialink.activity.friend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class FriendNichengset extends NetWorkActivity {
    private EditText nicheng;
    public static int sex1;
    public static String school;
    public static String touxiang;
    public static int settingClub = 1;
    public static int settingAsk = 1;
    private static String nickname;
    private Button set;

    private boolean isSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_nichengset);
        Intent intent1 = getIntent();
        Log.e("sexchuanguolaidezhi", "" + intent1.getStringExtra("sex"));
        sex1 = Integer.parseInt(intent1.getStringExtra("sex"));
        school = intent1.getStringExtra("school");
        touxiang = intent1.getStringExtra("file");

        /**
         * 定义未修改昵称时返回的数据
         * 没有下面的程序，返回会报错
         */
        Intent i = new Intent();
        i.putExtra("nicheng", "");
        setResult(0, i);

        init();
        event();
    }

    private void init() {
        setText("昵称设置");
        nicheng = (EditText) findViewById(R.id.nicheng);
        set = (Button) findViewById(R.id.set);
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        set.setOnClickListener(v -> {
            nickname = nicheng.getText().toString();
            getData();
        });
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
            Intent i = new Intent();
            i.putExtra("nicheng", nicheng.getText().toString());
            setResult(1, i);
        } else {
            Utils.toastShort(mContext, result.optString("msg"));
            Intent i = new Intent();
            i.putExtra("nicheng", "");
            setResult(2, i);
        }
    }

    private void getData() {
        setBodyParams(new String[]{"file", "nickname", "sex", "college", "settingClub", "settingAsk"}, new String[]{"" + touxiang, "" + nickname, "" + sex1, "" + school, "" + settingClub, "" + settingAsk});
        sendPost(Constants.BASE_URL + "/api/user/update.do", 100, Constants.token);
    }


}
