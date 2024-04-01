package com.leslie.socialink.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.activity.login.DefaultProblemActivity;
import com.leslie.socialink.activity.login.LabelSelectionActivity;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.entity.RefUserInfo;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.UserInfoBean;
import com.leslie.socialink.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingActivity extends NetWorkActivity {
    private TextView tvTitle, tvTeam, tvQuestion;
    private ImageView ivTeam, ivQuestion;
    private LinearLayout llChangePhone, llSetQuestion, llLable;
    private boolean canTeamSee = true, canQuestionSee = true;
    private final int settingClub = 1000;
    private final int settingAsk = 1001;
    private int ask, club;
    private ArrayList<UserInfoBean.UserLabelsBean> userLabelsBeans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ask = getIntent().getIntExtra("settingAsk", 0);
        club = getIntent().getIntExtra("settingClub", 0);
        userLabelsBeans = (ArrayList<UserInfoBean.UserLabelsBean>) getIntent().getSerializableExtra("userLabelsBeans");
        init();
        event();
    }

    private void init() {
        ivQuestion = (ImageView) findViewById(R.id.ivQuestion);
        ivTeam = (ImageView) findViewById(R.id.ivTeam);
        tvTeam = (TextView) findViewById(R.id.tvTeam);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        llChangePhone = (LinearLayout) findViewById(R.id.llChangePhone);
        llSetQuestion = (LinearLayout) findViewById(R.id.llSetQuestion);
        llLable = (LinearLayout) findViewById(R.id.llLable);
        setText("设置");
        canTeamSee = club == 0;
        ivTeam.setImageResource(club == 0 ? R.mipmap.dakai : R.mipmap.guanbi);
        tvTeam.setText(club == 0 ? "可见" : "不可见");
        canQuestionSee = ask == 0;
        ivQuestion.setImageResource(ask == 0 ? R.mipmap.dakai : R.mipmap.guanbi);
        tvQuestion.setText(ask == 0 ? "可见" : "不可见");
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        ivTeam.setOnClickListener(v -> {
            if (canTeamSee) {
                setBodyParams(new String[]{"settingClub"}, new String[]{"" + 1});
                sendPost(Constants.base_url + "/api/user/update.do", settingClub, Constants.token);
            } else {
                setBodyParams(new String[]{"settingClub"}, new String[]{"" + 0});
                sendPost(Constants.base_url + "/api/user/update.do", settingClub, Constants.token);
            }
        });
        ivQuestion.setOnClickListener(v -> {
            if (canQuestionSee) {
                setBodyParams(new String[]{"settingAsk"}, new String[]{"" + 1});
                sendPost(Constants.base_url + "/api/user/update.do", settingAsk, Constants.token);
            } else {
                setBodyParams(new String[]{"settingAsk"}, new String[]{"" + 0});
                sendPost(Constants.base_url + "/api/user/update.do", settingAsk, Constants.token);
            }
        });
        llChangePhone.setOnClickListener(v -> startActivity(new Intent(mContext, ChangePhoneNumActivity.class)));
        llSetQuestion.setOnClickListener(v -> startActivity(new Intent(mContext, DefaultProblemActivity.class).putExtra("type", 2)));

        llLable.setOnClickListener(v -> {
            startActivity(new Intent(mContext, LabelSelectionActivity.class)
                    .putExtra("type", 2)
                    .putExtra("userLabelsBeans", userLabelsBeans)
            );
            this.finish();
        });
        //隐藏标签设置
        llLable.setVisibility(View.GONE);
    }

    private void setUi() {
        ivTeam.setImageResource(canTeamSee ? R.mipmap.dakai : R.mipmap.guanbi);
        tvTeam.setText(canTeamSee ? "可见" : "不可见");
        ivQuestion.setImageResource(canQuestionSee ? R.mipmap.dakai : R.mipmap.guanbi);
        tvQuestion.setText(canQuestionSee ? "可见" : "不可见");
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        switch (where) {
            case settingClub:
                if (result.optInt("code") == 0) {
                    canTeamSee = !canTeamSee;
                    setUi();
                    //刷新相关界面
                    EventBus.getDefault().post(new RefUserInfo());
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case settingAsk:
                if (result.optInt("code") == 0) {
                    canQuestionSee = !canQuestionSee;
                    setUi();
                    //刷新相关界面
                    EventBus.getDefault().post(new RefUserInfo());
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "网络异常");
    }


}
