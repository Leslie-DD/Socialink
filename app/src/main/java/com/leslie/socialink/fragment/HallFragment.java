package com.leslie.socialink.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.OtherSayActivity;
import com.leslie.socialink.activity.team.PartyBuildingActivity;
import com.leslie.socialink.activity.team.TeamDetailActivity;
import com.leslie.socialink.activity.team.TeamIntroduceActivity;
import com.leslie.socialink.adapter.listview.OtherSayAdapter;
import com.leslie.socialink.adapter.recycleview.Tj_Adapter;
import com.leslie.socialink.base.NetWorkFragment;
import com.leslie.socialink.entity.BuildingBean;
import com.leslie.socialink.entity.EventBean;
import com.leslie.socialink.entity.RefStatementEvent;
import com.leslie.socialink.entity.RefTjEvent;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.utils.SpaceItemDecoration;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.MyLv;
import com.leslie.socialink.view.MyRecyclerview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HallFragment extends NetWorkFragment {
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
    private TeamDetailActivity mActivity;
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
        mActivity = (TeamDetailActivity) getActivity();
        if (mActivity.id != 0) {
            //获取他们说
            setBodyParams(new String[]{"type", "id"}, new String[]{"3", mActivity.id + ""});
            sendPostConnection(Constants.BASE_URL + "/api/club/speak/pglist.do", getSayCode, Constants.token);
            //获取推荐团建
            setBodyParams(new String[]{"type", "id"}, new String[]{"1", mActivity.id + ""});
            sendPostConnection(Constants.BASE_URL + "/api/club/tb/pglist.do", getTjCode, Constants.token);

        }
        if (cBean != null) {
            setUi();
        }
        lv.setFocusable(false);

        if (Constants.isJoin && Constants.isAdmin) {
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

    public void setivPic(String path) {
        Glide.with(this.mContext).load(path).asBitmap().into(ivPic);
    }

    public void setBean(TeamBean bean) {
        this.cBean = bean;
        if (cBean != null && view != null) {
            setUi();
        }
    }

    private void event() {
        tvOtherMore.setOnClickListener(v -> startActivity(new Intent(mContext, OtherSayActivity.class).putExtra("teamid", mActivity.id)));
        tvPicEditor.setOnClickListener(v -> mActivity.showPop());
        tvTeamEditor.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, TeamIntroduceActivity.class);
            intent.putExtra("introduce", tvIntroduction.getText());
            intent.putExtra("teamId", cBean.getId());
            startActivity(intent);
        });
        tvTjMore.setOnClickListener(v -> startActivity(new Intent(mContext, PartyBuildingActivity.class).putExtra("teamid", mActivity.id)));
        adapter.setDelItemListener(position -> {
            setBodyParams(new String[]{"speakId"}, new String[]{"" + testData.get(position).getId()});
            sendPostConnection(Constants.BASE_URL + "/api/club/speak/delete.do", DelCode, Constants.token);
        });
    }

    @Subscribe
    public void changeIntroduce(EventBean bean) {
        if (bean != null && bean.getIntroduce() != null) {
            tvIntroduction.setText(bean.getIntroduce());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addRefresh(RefStatementEvent event) {
        //获取他们说
        setBodyParams(new String[]{"type", "id"}, new String[]{"3", mActivity.id + ""});
        sendPostConnection(Constants.BASE_URL + "/api/club/speak/pglist.do", getSayCode, Constants.token);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refTj(RefTjEvent event) {
        //获取推荐团建
        setBodyParams(new String[]{"type", "id"}, new String[]{"1", mActivity.id + ""});
        sendPostConnection(Constants.BASE_URL + "/api/club/tb/pglist.do", getTjCode, Constants.token);
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
