package com.leslie.socialink.activity.team;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.Label;
import com.leslie.socialink.entity.RefTeamDetailEvent;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.FlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChooseTeamLableActivity extends NetWorkActivity {
    private TextView tvTitle;
    private ImageView ivBack;
    private ArrayList<TeamBean.LabelsBean> ls;
    private Button btSave;
    private FlowLayout flowLayout;
    private ArrayList<Label> labels;
    private int id;
    private String name;
    private final int getLabelCode = 1000;
    private final int editCode = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_team_lable);
        inits();
        event();
    }

    private void inits() {
        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
        ls = (ArrayList<TeamBean.LabelsBean>) getIntent().getSerializableExtra("labels");
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("设置团队标签");
        ivBack = findViewById(R.id.ivBack);
        btSave = (Button) findViewById(R.id.btSave);
        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        //获取标签
        setBodyParams(new String[]{"type"}, new String[]{"label"});
        sendPost(Constants.BASE_URL + "/api/pub/category/list.do", getLabelCode, Constants.token);

    }

    private void event() {
        ivBack.setOnClickListener(v -> finish());
        btSave.setOnClickListener(v -> {
            if (labels == null) {
                Utils.toastShort(mContext, "标签获取失败，请保持网络正常再尝试");
                return;
            }
            String label = "";
            for (int i = 0; i < labels.size(); i++) {
                Label bean = labels.get(i);
                if (bean.getStatus() == 1) {
                    if (label.length() == 0) {
                        label = bean.getValue();
                    } else {
                        label = label + "," + bean.getValue();
                    }
                }
            }
            if (label.length() == 0) {
                Utils.toastShort(mContext, "请先选择团队的标签");
                return;
            }
            setBodyParams(new String[]{"id", "name", "labels"}, new String[]{"" + id, name, label});
            sendPost(Constants.BASE_URL + "/api/club/base/updatebase.do", editCode, Constants.token);
        });
    }

    private void setTvBg(TextView view, int status) {
        view.setBackgroundResource(status == 0 ? R.drawable.e6e6e6_17 : R.drawable.bg_2cd22b_17);
        String color = status == 0 ? "#999999" : "#ffffff";
        view.setTextColor(Color.parseColor(color));
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int code = result.optInt("code");
        Gson gs = new Gson();
        switch (where) {
            case getLabelCode:
                if (code == 0) {
                    labels = gs.fromJson(result.optString("data"), new TypeToken<List<Label>>() {
                    }.getType());


                    if (labels != null) {
                        if (ls != null && ls.size() > 0) {
                            for (int i = 0; i < labels.size(); i++) {
                                for (int j = 0; j < ls.size(); j++) {
                                    if (labels.get(i).getValue().equals(ls.get(j).getName())) {
                                        labels.get(i).setStatus(1);
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < labels.size(); i++) {
                            final TextView view = new TextView(this);
                            view.setText(labels.get(i).getValue());
                            setTvBg(view, labels.get(i).getStatus());
                            view.setHeight(Utils.dip2px(context, 34));
                            view.setPadding(Utils.dip2px(context, 17), 0, Utils.dip2px(context, 17), 0);
                            view.setGravity(Gravity.CENTER);
                            view.setTextSize(14);
                            //view.setBackgroundResource(R.drawable.e6e6e6_17);
                            // 设置点击事件
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String text = view.getText().toString();
                                    int index = -1;
                                    for (int j = 0; j < labels.size(); j++) {
                                        Label bean = labels.get(j);
                                        if (bean.getValue().equals(text)) {
                                            bean.setStatus(Math.abs(bean.getStatus() - 1));
                                            index = j;
                                            break;
                                        }
                                    }
                                    setTvBg(view, labels.get(index).getStatus());
                                }
                            });
                            flowLayout.addView(view);
                        }
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case editCode:
                if (code == 0) {
                    Utils.toastShort(mContext, "设置标签成功");
                    EventBus.getDefault().post(new RefTeamDetailEvent());
                    this.finish();
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }


}
