package com.example.heshequ.activity.team;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.Tj_Adapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.BuildingBean;
import com.example.heshequ.entity.RefTjEvent;
import com.example.heshequ.utils.SpaceItemDecoration;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PartyBuildingActivity extends NetWorkActivity implements View.OnClickListener, XRecyclerView.LoadingListener {
    private XRecyclerView rv;
    private ArrayList<BuildingBean> rvData;
    private TextView tvTitle;
    private Tj_Adapter tjAdapter;
    private ImageView ivAdd;
    private int teamid;
    private AlertDialog delTjDialog;
    private int delPosition;        //删除位置
    private int recommendPosition;  //推荐/取消推荐 位置
    private int recommend;          //是否推荐状态
    private Gson gson = new Gson();
    private boolean isref = true;
    private int pn = 1;
    private int allpn = 0;
    private final int getTjCode = 1000;
    private final int delTjCode = 1001;
    private final int hallCode = 1002;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_building);
        EventBus.getDefault().register(this);
        init();
        initDialog();
        event();
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(this);
        ivAdd.setOnClickListener(this);
    }

    private void init() {
        teamid = getIntent().getIntExtra("teamid", 0);
        rv = (XRecyclerView) findViewById(R.id.rv);
        ConsTants.initXrecycleView(this, true, true, rv);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("全部团建");
        ivAdd = (ImageView) findViewById(R.id.ivRight);
        ivAdd.setImageResource(R.mipmap.kj2);
        if (!Constants.isAdmin) {
            ivAdd.setVisibility(View.GONE);
        }
        pn = 1;
        isref = true;
        getData(pn);
        rvData = new ArrayList<>();
        tjAdapter = new Tj_Adapter(mContext, rvData, 2);
        rv.setAdapter(tjAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(mContext, 10, 1));
        rv.setLoadingListener(this);
        tjAdapter.setOnItemViewclick(new Tj_Adapter.OnItemViewclick() {
            @Override
            public void viewClick(int viewType, int position) {
                switch (viewType) {
                    case 1:
                        //编辑
                        startActivity(new Intent(mContext, AddTjActivity.class).putExtra("bean", rvData.get(position)).putExtra("type", 2));
                        break;
                    case 2:
                        //Utils.toastShort(PartyBuildingActivity.this,"删除");
                        delPosition = position;
                        delTjDialog.show();
                        break;
                    case 3:
                        //Utils.toastShort(PartyBuildingActivity.this,"推荐到大厅");
                        recommendPosition = position;
                        recommend = rvData.get(position).getRecommend();
                        if (recommend == 0) {
                            setBodyParams(new String[]{"tbId", "op"}, new String[]{"" + rvData.get(recommendPosition).getId(), "" + 1});
                            sendPost(Constants.base_url + "/api/club/tb/hall.do", hallCode, Constants.token);
                        } else if (recommend == 1) {
                            setBodyParams(new String[]{"tbId", "op"}, new String[]{"" + rvData.get(recommendPosition).getId(), "" + 0});
                            sendPost(Constants.base_url + "/api/club/tb/hall.do", hallCode, Constants.token);
                        }
                        break;
                }
            }
        });
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("要删除这条团建吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除
                setBodyParams(new String[]{"tbId"}, new String[]{"" + rvData.get(delPosition).getId()});
                sendPost(Constants.base_url + "/api/club/tb/delete.do", delTjCode, Constants.token);
                delTjDialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delTjDialog.dismiss();
            }
        });
        delTjDialog = builder.create();
        delTjDialog.setCancelable(false);
    }

    public void delTjDialogShow(int position) {

    }

    private void getData(int pn) {
        //获取推荐团建
        setBodyParams(new String[]{"type", "id", "pn", "pn"}, new String[]{"0", teamid + "", "" + pn, "" + Constants.default_PS});
        sendPost(Constants.base_url + "/api/club/tb/pglist.do", getTjCode, Constants.token);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == getTjCode) {
            switch (result.optInt("code")) {
                case 0:
                    if (result.optString("data") != null) {
                        allpn = result.optJSONObject("data").optInt("totalPage");
                        rvData = new ArrayList<>();
                        rvData = gson.fromJson(result.optJSONObject("data").optString("list"),
                                new TypeToken<List<BuildingBean>>() {
                                }.getType());
                        if (isref) {
                            tjAdapter.setData(rvData);
                        } else {
                            tjAdapter.setData2(rvData);
                        }

                    }
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
        } else if (where == delTjCode) {
            switch (result.optInt("code")) {
                case 0:
                    //刷新
                    pn = 1;
                    isref = true;
                    getData(pn);
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
        } else if (where == hallCode) {
            switch (result.optInt("code")) {
                case 0:
                    //刷新
                    if (recommend == 0) {
                        tjAdapter.changetjdt(recommendPosition, 1);
                    } else {
                        tjAdapter.changetjdt(recommendPosition, 0);
                    }
                    EventBus.getDefault().post(new RefTjEvent());
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.ivSearch:

                break;
            case R.id.ivRight:
                startActivity(new Intent(mContext, AddTjActivity.class).putExtra("teamid", teamid).putExtra("type", 1));
                break;
        }
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pn = 1;
                isref = true;
                getData(pn);
                rv.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pn < allpn) {
                    pn++;
                    isref = false;
                    getData(pn);
                }
                rv.loadMoreComplete();
            }
        }, 1000);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refTj(RefTjEvent event) {
        pn = 1;
        isref = true;
        getData(pn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
