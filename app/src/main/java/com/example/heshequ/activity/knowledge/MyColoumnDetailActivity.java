package com.example.heshequ.activity.knowledge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heshequ.adapter.knowledge.ColumnDetailAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.knowledge.ArticleSimpleBean;
import com.example.heshequ.bean.knowledge.Author;
import com.example.heshequ.bean.knowledge.SubscriptionBean;
import com.example.heshequ.constans.Constants;
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

public class MyColoumnDetailActivity extends NetWorkActivity implements View.OnClickListener,XRecyclerView.LoadingListener {

    private TextView tvTitle,tvName,tvSummary,tvRead;
    private CircleView ivHead;
    private WebView webView;
    private TextView btnEdit;
    private int columnId;
    public static String title;
    public static String name;
    public static String avatar;
    private List<ArticleSimpleBean> allList;
    private LinearLayout linearLayout;

    private ColumnDetailAdapter adapter;
    private XRecyclerView rv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coloumn_detail);

        columnId = (int) getIntent().getSerializableExtra("columnId");
        Log.e("MyColoumnDetailActivity", ""+columnId);

        init();
        event();
    }
    private void init() {
        setText("专栏");
        ivHead = (CircleView) findViewById(R.id.ivHeadSubscriptionS);
        tvTitle = (TextView) findViewById(R.id.tvTitleSubscriptionS);
        tvName = (TextView) findViewById(R.id.tvNameSubscriptionS);
        tvSummary = findViewById(R.id.tvSummaryS);
        tvRead = findViewById(R.id.tvReadS);
        btnEdit = findViewById(R.id.btnEdit);
        linearLayout = findViewById(R.id.add);
        rv = (XRecyclerView) findViewById(R.id.rvS);
        ConsTants.initXrecycleView(mContext, true, true, rv);
        adapter = new ColumnDetailAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        getData(columnId);

    }
    private void event(){
        findViewById(R.id.ivBack).setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
    }
    @Override
    protected void onFailure(String result, int where) {

    }
    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.e("MyColoumnDetailActivity", "请求数据成功"+result);

        int ret = result.optInt("code");
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

            tvTitle.setText(subscriptionBean.name == null ? "" : subscriptionBean.name);
            tvName.setText(subscriptionBean.author.nickname == null ? "" : subscriptionBean.author.nickname);
            tvSummary.setText(subscriptionBean.summary == null ? "" : subscriptionBean.summary);
            tvRead.setText(""+subscriptionBean.subscriptionNum);
            if (subscriptionBean.author.header != null) {
                // 完善协议完善协议
                //Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId=" + subscriptionBean.author.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("showset", "" + Constants.base_url + "/info/file/pub.do?fileId=" + subscriptionBean.author.header);
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
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnEdit:
                break;
            case R.id.add:
                Intent intent = new Intent(context, CreateArticleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("specialColumnId", columnId);
                intent.putExtras(bundle);
                context.startActivity(intent);
                break;
        }
    }
    private void getData(int id){
        sendConnection(HttpRequest.HttpMethod.GET, WenConstans.getColumnDetail + "?id=" +id,new String[]{},new String[]{},100, false,false,WenConstans.token);

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
