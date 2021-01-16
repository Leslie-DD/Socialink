package com.example.heshequ.activity.team;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.example.heshequ.adapter.listview.OtherSayAdapter;
import com.example.heshequ.adapter.recycleview.Tj_Adapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.entity.BuildingBean;
import com.example.heshequ.entity.TestBean;
import com.example.heshequ.utils.SpaceItemDecoration;
import com.example.heshequ.view.MyLv;
import com.example.heshequ.view.MyRecyclerview;
import com.example.heshequ.R;
import com.tencent.mm.opensdk.utils.Log;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeamDetailActivity extends NetWorkActivity implements View.OnClickListener {
    private TextView tvTitle, tvHall, tvStatement, tvTeam, tvManager, tvTeamName;
    private int status = -1;
    private MyRecyclerview recyclerview;
    private MyLv lv;
    private ArrayList<TestBean> testData;
    private OtherSayAdapter adapter;
    private ArrayList<BuildingBean> rvData;
    private Tj_Adapter tjAdapter;
    private int itemPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        init();
        event();
    }

    private void init() {

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("团队详情");
        tvHall = (TextView) findViewById(R.id.tvHall);
        tvStatement = (TextView) findViewById(R.id.tvStatement);
        tvTeam = (TextView) findViewById(R.id.tvTeam);
        tvManager = (TextView) findViewById(R.id.tvManager);
        tvTeamName = (TextView) findViewById(R.id.tvTeamName);
        lv = (MyLv) findViewById(R.id.lv);
        recyclerview = (MyRecyclerview) findViewById(R.id.ivRecycview);
        setTvBg(0);
        getData();
        lv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        tjAdapter = new Tj_Adapter(mContext, rvData);
        recyclerview.setAdapter(tjAdapter);
        recyclerview.addItemDecoration(new SpaceItemDecoration(context, 10));
    }

    private void getData() {
        testData = new ArrayList<>();
        rvData = new ArrayList<>();
        TestBean bean = new TestBean();
        ArrayList<String> imgs = new ArrayList<>();
        bean.setImgs(imgs);
        testData.add(bean);
        bean = new TestBean();
        imgs = new ArrayList<>();
        imgs.add("http://cuimg.zuyushop.com/cuxiaoPic/201511/2015110010091817554.jpg");
        imgs.add("http://desk.fd.zol-img.com.cn/t_s960x600c5/g5/M00/02/03/ChMkJlbKx2qIGStWAAePuU7wk_cAALHzQF9mKIAB4_R763.jpg");
        imgs.add("http://img1.imgtn.bdimg.com/it/u=3356331771,2093090619&fm=214&gp=0.jpg");
        imgs.add("http://img5.duitang.com/uploads/item/201405/12/20140512000053_axANX.thumb.700_0.jpeg");
        imgs.add("http://pic2.16pic.com/00/54/76/16pic_5476585_b.jpg");
        imgs.add("http://img.mp.sohu.com/upload/20170711/3f177d2be18143a48a9af1217e669855_th.png");
        imgs.add("http://img4.duitang.com/uploads/item/201509/26/20150926014223_BW8EG.jpeg");
        imgs.add("http://mvimg10.meitudata.com/569b9090af0526344.jpg");
        imgs.add("http://img.mp.sohu.com/upload/20170703/c8c1818222a547f78585f9b357c93613_th.png");
        bean.setImgs(imgs);
        testData.add(bean);


    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(this);
        tvHall.setOnClickListener(this);
        tvStatement.setOnClickListener(this);
        tvTeam.setOnClickListener(this);
        tvManager.setOnClickListener(this);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.tvHall:
                setTvBg(0);
                break;
            case R.id.tvStatement:
                setTvBg(1);
                break;
            case R.id.tvTeam:
                setTvBg(2);
                break;
            case R.id.tvManager:
                setTvBg(3);
                break;
        }
    }

    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        tvHall.setSelected(status == 0 ? true : false);
        tvStatement.setSelected(status == 1 ? true : false);
        tvTeam.setSelected(status == 2 ? true : false);
        tvManager.setSelected(status == 3 ? true : false);
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
        Log.e("this.getClass().getSimpleName()",this.getClass().getSimpleName());
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
