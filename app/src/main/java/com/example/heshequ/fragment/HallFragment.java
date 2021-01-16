package com.example.heshequ.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshequ.activity.team.OtherSayActivity;
import com.example.heshequ.activity.team.PartyBuildingActivity;
import com.example.heshequ.activity.team.TeamDetailActivity2;
import com.example.heshequ.activity.team.TeamIntroduceActivity;
import com.example.heshequ.adapter.listview.OtherSayAdapter;
import com.example.heshequ.adapter.recycleview.Tj_Adapter;
import com.example.heshequ.base.NetWorkFragment;
import com.example.heshequ.bean.TeamBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.BuildingBean;
import com.example.heshequ.entity.EventBean;
import com.example.heshequ.entity.RefStatementEvent;
import com.example.heshequ.entity.RefTjEvent;
import com.example.heshequ.utils.SpaceItemDecoration;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.MyLv;
import com.example.heshequ.view.MyRecyclerview;
import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev06 on 2018/5/15.
 */
public class HallFragment extends NetWorkFragment implements View.OnClickListener {
    private View view;
    private MyRecyclerview recyclerview;
    private MyLv lv;
    private ArrayList<TeamBean.SpeakBean> testData;
    private OtherSayAdapter adapter;
    private ArrayList<BuildingBean> rvData;
    private Tj_Adapter tjAdapter;
    private TextView tvOtherMore;
    private TextView tvPicEditor, tvTeamEditor;
    private TextView tvIntroduction, tvTjMore;
    private TeamDetailActivity2 mActivity;
    public int types;
    private TeamBean cBean;
    private final int getSayCode = 1000;
    private final int getTjCode = 1001;
    private final int DelCode = 1002;
    private ImageView ivPic;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Gson gs = new Gson();
        switch (where) {
            case getSayCode:
                Log.e("YSF", "团言：" + result.toString());
                try {
                    testData = new ArrayList<>();
                    JSONObject data = new JSONObject(result.optString("data"));
                    if (data != null) {
                        testData = gs.fromJson(data.optString("list"), new TypeToken<List<TeamBean.SpeakBean>>() {
                        }.getType());
                        if (testData != null) {
                            adapter.setData(1, testData);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case getTjCode:
                try {
                    JSONObject data = new JSONObject(result.optString("data"));
                    if (data != null) {
                        rvData = gs.fromJson(data.optString("list"), new TypeToken<List<BuildingBean>>() {
                        }.getType());
                        if (rvData != null) {
                            tjAdapter.setData(rvData);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("YSF", "团建：" + result.toString());
                break;
            case DelCode:
                if (result.optInt("code") == 0) {
                    EventBus.getDefault().post(new RefStatementEvent());
                    Utils.toastShort(mContext, "删除成功");
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }

                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.hallfragment, null);
        ivPic = (ImageView) view.findViewById(R.id.ivPic);
        tvPicEditor = (TextView) view.findViewById(R.id.tvPicEditor);
        tvTeamEditor = (TextView) view.findViewById(R.id.tvTeamEditor);
        tvIntroduction = (TextView) view.findViewById(R.id.tvIntroduction);
        tvTjMore = (TextView) view.findViewById(R.id.tvTjMore);
        lv = (MyLv) view.findViewById(R.id.lv);
        recyclerview = (MyRecyclerview) view.findViewById(R.id.ivRecycview);
        getData();
        tvOtherMore = (TextView) view.findViewById(R.id.tvOtherMore);
        adapter = new OtherSayAdapter(mContext, testData);
        lv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        tjAdapter = new Tj_Adapter(mContext, rvData, 1);
        recyclerview.setAdapter(tjAdapter);
        recyclerview.addItemDecoration(new SpaceItemDecoration(mContext, 10));

        EventBus.getDefault().register(this);
        mActivity = (TeamDetailActivity2) getActivity();
        if (mActivity.id != 0) {
            //获取他们说
            setBodyParams(new String[]{"type", "id"}, new String[]{"3", mActivity.id + ""});
            sendConnection(HttpRequest.HttpMethod.POST, Constants.base_url + "/api/club/speak/pglist.do", getSayCode, Constants.token);
            //获取推荐团建
            setBodyParams(new String[]{"type", "id"}, new String[]{"1", mActivity.id + ""});
            sendConnection(HttpRequest.HttpMethod.POST, Constants.base_url + "/api/club/tb/pglist.do", getTjCode, Constants.token);

        }
        if (cBean != null) {
            setUi();
        }
        lv.setFocusable(false);

        if (Constants.isJoin && Constants.isAdmin){
            tvPicEditor.setVisibility(View.VISIBLE);
            tvTeamEditor.setVisibility(View.VISIBLE);
        }

        event();
        return view;
    }

    private void setUi() {
        Log.e("DDQ", "CoverImage：" + cBean.getCoverImage());
        Utils.setImg(mContext, cBean.getCoverImage(), ivPic, R.mipmap.mrtp);
        tvIntroduction.setText(cBean.getIntroduction());
        mActivity.setTitleName(cBean.getName());
        ArrayList<TeamBean.UsersBean> users = cBean.getUsers();
        ArrayList<String> imgs = new ArrayList<>();
        if (users != null && users.size() != 0) {
            for (int i = 0; i < users.size(); i++) {
                TeamBean.UsersBean user = users.get(i);

                if (i == 0) {

                } else {
                    if (i <= 3) {
                        imgs.add(user.getHeader());
                    }
                }
            }
        }
    }

    public void setivPic(String path){
        Glide.with(this).load(path).asBitmap().into(ivPic);
    }

    public void setBean(TeamBean bean) {
        this.cBean = bean;
        if (cBean != null && view != null) {
            setUi();
        }
    }

    private void event() {
        tvOtherMore.setOnClickListener(this);
        tvPicEditor.setOnClickListener(this);
        tvTeamEditor.setOnClickListener(this);
        tvTjMore.setOnClickListener(this);
        ivPic.setOnClickListener(this);
        adapter.setDelItemListener(new OtherSayAdapter.DelItemListener() {
            @Override
            public void onDel(int position) {
                setBodyParams(new String[]{"speakId"}, new String[]{"" + testData.get(position).getId()});
                sendPost(Constants.base_url + "/api/club/speak/delete.do", DelCode, Constants.token);
            }
        });
    }

    @Subscribe
    public void changeIntroduce(EventBean bean) {
        if (bean != null && bean.getIntroduce() != null) {
            tvIntroduction.setText(bean.getIntroduce());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOtherMore:
                startActivity(new Intent(mContext, OtherSayActivity.class).putExtra("teamid", mActivity.id));
                break;
            case R.id.tvTjMore: //团建
                startActivity(new Intent(mContext, PartyBuildingActivity.class).putExtra("teamid", mActivity.id));
                break;
            case R.id.tvPicEditor: //编辑团队封面
                mActivity.showPop();
                break;
            case R.id.tvTeamEditor:
                Intent intent = new Intent(mContext, TeamIntroduceActivity.class);
                intent.putExtra("introduce", tvIntroduction.getText());
                intent.putExtra("teamId", cBean.getId());
                startActivity(intent);
                break;
            case R.id.ivPic:

                /*ArrayList<String> imgs = new ArrayList<>();
                imgs.add("");
                startActivity(new Intent(mContext, ImagePreviewActivity.class)
                        .putStringArrayListExtra("imageList", imgs)
                .putExtra(P.START_ITEM_POSITION, 0)
                .putExtra(P.START_IAMGE_POSITION, 0)
                .putExtra("isdel2", false));*/
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addRefresh(RefStatementEvent event) {
        //获取他们说
        setBodyParams(new String[]{"type", "id"}, new String[]{"3", mActivity.id + ""});
        sendConnection(HttpRequest.HttpMethod.POST, Constants.base_url + "/api/club/speak/pglist.do", getSayCode, Constants.token);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refTj(RefTjEvent event) {
        //获取推荐团建
        setBodyParams(new String[]{"type", "id"}, new String[]{"1", mActivity.id + ""});
        sendConnection(HttpRequest.HttpMethod.POST, Constants.base_url + "/api/club/tb/pglist.do", getTjCode, Constants.token);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getData() {
        testData = new ArrayList<>();
        rvData = new ArrayList<>();
    }
}
