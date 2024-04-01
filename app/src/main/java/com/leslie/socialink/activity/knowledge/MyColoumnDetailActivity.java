package com.leslie.socialink.activity.knowledge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.knowledge.ColumnDetailAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.knowledge.ArticleSimpleBean;
import com.leslie.socialink.bean.knowledge.Author;
import com.leslie.socialink.bean.knowledge.SubscriptionBean;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.view.CircleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyColoumnDetailActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {

    private TextView tvTitle, tvName, tvSummary, tvRead;
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
        Log.e("MyColoumnDetailActivity", "" + columnId);

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
        ConsTants.initXRecycleView(mContext, true, true, rv);
        adapter = new ColumnDetailAdapter(mContext);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        getData(columnId);

    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateArticleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("specialColumnId", columnId);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.e("MyColoumnDetailActivity", "请求数据成功" + result);

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
            tvRead.setText("" + subscriptionBean.subscriptionNum);
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
            if (data != null) {
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

    private void getData(int id) {
        sendGetConnection(WenConstans.getColumnDetail + "?id=" + id, new String[]{}, new String[]{}, 100, WenConstans.token);
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
