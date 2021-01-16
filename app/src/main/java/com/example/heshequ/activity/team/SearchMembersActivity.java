package com.example.heshequ.activity.team;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.heshequ.adapter.listview.TeamMemberAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefMembers;
import com.example.heshequ.entity.TeamMemberBean;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchMembersActivity extends NetWorkActivity implements View.OnClickListener {
    private ImageView ivBack;
    private ImageView ivSearch;
    private EditText etSearch;
    private ListView lv;
    private TextView tvNotFind;
    private ArrayList<TeamMemberBean> pData;
    private ArrayList<TeamMemberBean> Data;
    private TeamMemberAdapter adapter;
    private int teamId;
    private String searchName;
    private int searchData = 1000;
    private final int EditorName = 1001;
    private Gson gson = new Gson();
    private int editorPosition;
    private String editorName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_members);
        init();
        event();
    }

    private void init() {
        teamId = getIntent().getIntExtra("teamid",0);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);
        lv = (ListView) findViewById(R.id.lv);
        tvNotFind = (TextView) findViewById(R.id.tvNotFind);
        pData = new ArrayList<>();
        Data = new ArrayList<>();
        adapter = new TeamMemberAdapter(mContext, Data);
        lv.setAdapter(adapter);
    }


    private void event() {
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    // do some your things
                    searchName = etSearch.getText().toString().trim();
                    if (searchName.isEmpty()){
                        Utils.toastShort(SearchMembersActivity.this,"搜索内容不能为空");
                    }else {
                        setBodyParams(new String[]{"clubId"}, new String[]{"" + teamId});
                        sendPost(Constants.base_url + "/api/club/member/pglist.do", searchData, Constants.token);
                    }
                }
                return false;
            }
        });

        adapter.setOnItemEditorNameListener(new TeamMemberAdapter.OnItemEditorNameListener() {
            @Override
            public void ItemEditor(int position,String mark) {
                editorPosition = position;
                editorName = mark;
                setBodyParams(new String[]{"id","nickname"},new String[]{""+Data.get(position).getId(),""+mark});
                sendPost(Constants.base_url+"/api/club/member/update.do",EditorName,Constants.token);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivSearch:
                searchName = etSearch.getText().toString().trim();
                if (searchName.isEmpty()){
                    Utils.toastShort(this,"搜索内容不能为空");
                    return;
                }
                setBodyParams(new String[]{"clubId"},new String[]{""+teamId});
                sendPost(Constants.base_url+"/api/club/member/pglist.do",searchData,Constants.token);
                break;
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == searchData){
            switch (result.optInt("code")){
                case 0:
                    Data = new ArrayList<>();
                    pData = gson.fromJson(result.optString("data"),new TypeToken<List<TeamMemberBean>>() {}.getType());
                    if (pData!=null && pData.size() > 0){
                        for (int i = 0;i<pData.size();i++){
                            if (pData.get(i).getNickname().contains(searchName)){
                                Data.add(pData.get(i));
                            }
                        }
                    }
                    Data = Utils.getSortData(Data);
                    if (Data.size()>0){
                        tvNotFind.setVisibility(View.GONE);
                    }else{
                        tvNotFind.setVisibility(View.VISIBLE);
                    }
                    adapter.setData(Data);
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
        }else if (where == EditorName){
            switch (result.optInt("code")){
                case 0:
                    Data.get(editorPosition).setStatus(0);
                    Data.get(editorPosition).setNickname(editorName);
                    Data=Utils.getSortData((ArrayList<TeamMemberBean>) Data);
                    adapter.setData(Data);
                    EventBus.getDefault().post(new RefMembers());
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

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(this,"网络异常");
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
