package com.example.heshequ.activity.knowledge;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshequ.adapter.knowledge.ColumnDetailAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.knowledge.ArticleSimpleBean;
import com.example.heshequ.bean.knowledge.Author;
import com.example.heshequ.bean.knowledge.SubscriptionBean;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionDetailActivity extends NetWorkActivity implements View.OnClickListener , XRecyclerView.LoadingListener {

    private TextView tvTitle,tvName,tvSummary,tvRead;
    private CircleView ivHead;
    private Button btnUnsubscribe;
    private int subscriptionId;
    public static String title;
    public static String name;
    public static String avatar;
    private List<ArticleSimpleBean> allList;

    private ColumnDetailAdapter adapter;
    private XRecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        subscriptionId = (int) getIntent().getSerializableExtra("Subscription");
        init();
        event();
    }
    private void init() {
        setText("专栏");
        ivHead = (CircleView) findViewById(R.id.ivHeadSubscription);
        tvTitle = (TextView) findViewById(R.id.tvTitleSubscription);
        tvName = (TextView) findViewById(R.id.tvNameSubscription);
        tvSummary = findViewById(R.id.tvSummary);
        tvRead = findViewById(R.id.tvRead);
        btnUnsubscribe = findViewById(R.id.unsubscribe);
        rv = (XRecyclerView) findViewById(R.id.rv);
        ConsTants.initXrecycleView(mContext, true, true, rv);
        adapter = new ColumnDetailAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        getData();

    }
    private void event(){
        findViewById(R.id.ivBack).setOnClickListener(this);
        btnUnsubscribe.setOnClickListener(this);
    }
    @Override
    protected void onFailure(String result, int where) {

    }
    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == 100) {
            int ret = result.optInt("code");
            Log.e("SubscriptionDetail", "ret" + result );

            if (ret == 0) {
                JSONObject object = new JSONObject(result.optString("data"));
                SubscriptionBean subscriptionBean = new SubscriptionBean();
                subscriptionBean.createTime = object.getString("createTime");
                subscriptionBean.updateTime = object.getString("updateTime");
                subscriptionBean.authorId = object.getInt("authorId");
                subscriptionBean.name = object.getString("name");
                subscriptionBean.summary = object.getString("summary");
                subscriptionBean.price = object.getDouble("price");
                subscriptionBean.score = object.getDouble("score");
                subscriptionBean.id = object.getInt("id");
                subscriptionBean.status = object.getString("status");
                subscriptionBean.subscriptionNum = object.getInt("subscriptionNum");
                subscriptionBean.author = new Author();
                subscriptionBean.author.id = object.getJSONObject("author").getInt("id");
                subscriptionBean.author.header = object.getJSONObject("author").getString("header");
                subscriptionBean.author.nickname = object.getJSONObject("author").getString("nickname");

                Log.e("SubscriptionDetail", "subscriptionBean.name" + subscriptionBean.name );
                Log.e("SubscriptionDetail", "subscriptionBean.author.nickname" + subscriptionBean.author.nickname );
                tvTitle.setText(subscriptionBean.name == null ? "" : subscriptionBean.name);
                tvName.setText(subscriptionBean.author.nickname == null ? "" : subscriptionBean.author.nickname);
                tvSummary.setText(subscriptionBean.summary == null ? "" : subscriptionBean.summary);
                tvRead.setText(""+subscriptionBean.subscriptionNum);

                if (subscriptionBean.author.header != null) {
                    // 完善协议完善协议
                    //Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId=" + subscriptionBean.author.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                } else {
                    ivHead.setImageResource(R.mipmap.head3);
                }
                Gson gson = new Gson();
                JSONArray data = result.getJSONObject("data").getJSONArray("passages");
                allList = new ArrayList<>();
                if (data != null ) {
                    allList = gson.fromJson(data.toString(),
                            new TypeToken<List<ArticleSimpleBean>>() {
                            }.getType());
                    if (allList == null || allList.size() == 0) {
                        allList = new ArrayList<>();
                    }

                }
                adapter.setData(allList);
            }
        } else if (where == 200) {
            Toast.makeText(mContext,"退订成功",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.unsubscribe:
                sendConnection(HttpRequest.HttpMethod.POST,WenConstans.unsubscribe + "?id=" +subscriptionId,new String[]{},new String[]{},200, false, WenConstans.token);
                break;
        }
    }
    private void getData(){
        sendConnection(HttpRequest.HttpMethod.GET,WenConstans.getColumnDetail + "?id=" +subscriptionId,new String[]{},new String[]{},100, false, WenConstans.token);

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

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
