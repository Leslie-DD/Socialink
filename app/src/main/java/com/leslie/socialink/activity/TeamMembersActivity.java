package com.leslie.socialink.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.SearchMembersActivity;
import com.leslie.socialink.adapter.listview.TeamMemberAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.entity.RefMembers;
import com.leslie.socialink.entity.TeamMemberBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamMembersActivity extends NetWorkActivity {
    private ImageView ivBack;
    private EditText etSearch;
    private ListView lv;
    private ArrayList<TeamMemberBean> pData;
    private TeamMemberAdapter adapter;
    private int teamId;
    private final int getData = 1000;
    private final int EditorName = 1001;
    private int editorPosition;
    private String editorName;
    private Gson gson = new Gson();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_members);
        EventBus.getDefault().register(this);
        init();
        event();
    }

    private void init() {
        teamId = getIntent().getIntExtra("id", 0);
        lv = (ListView) findViewById(R.id.lv);
        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.setFocusable(false);
        pData = new ArrayList<>();
        adapter = new TeamMemberAdapter(mContext, pData);
        adapter.getTeamId(teamId);
        lv.setAdapter(adapter);
        getData();
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        etSearch.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchMembersActivity.class).putExtra("teamid", teamId));
        });
        adapter.setOnItemEditorNameListener((position, mark) -> {
            editorPosition = position;
            editorName = mark;
            setBodyParams(new String[]{"id", "nickname"}, new String[]{"" + pData.get(position).getId(), "" + mark});
            sendPost(Constants.BASE_URL + "/api/club/member/update.do", EditorName, Constants.token);
        });

    }

    private void getData() {
        setBodyParams(new String[]{"clubId"}, new String[]{"" + teamId});
        sendPost(Constants.BASE_URL + "/api/club/member/pglist.do", getData, Constants.token);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == getData) {
            switch (result.optInt("code")) {
                case 0:
                    pData = gson.fromJson(result.optString("data"), new TypeToken<List<TeamMemberBean>>() {
                    }.getType());
                    pData = Utils.getSortData(pData);
                    adapter.setData(pData);
                    break;
                case 1:
                    Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                    break;
                case 2:
                    Utils.toastShort(this, result.optString("msg"));
                    break;
                case 3:
                    Utils.toastShort(this, "您没有该功能操作权限");
                    break;

            }
        } else if (where == EditorName) {
            switch (result.optInt("code")) {
                case 0:
                    pData.get(editorPosition).setStatus(0);
                    pData.get(editorPosition).setNickname(editorName);
                    pData = Utils.getSortData((ArrayList<TeamMemberBean>) pData);
                    adapter.setData(pData);

                    break;
                case 1:
                    Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                    break;
                case 2:
                    Utils.toastShort(this, result.optString("msg"));
                    break;
                case 3:
                    Utils.toastShort(this, "您没有该功能操作权限");
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refMembers(RefMembers refMembers) {
        getData();
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "网络异常");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
