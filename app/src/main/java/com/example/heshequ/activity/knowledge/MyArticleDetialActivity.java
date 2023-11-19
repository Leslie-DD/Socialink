package com.example.heshequ.activity.knowledge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.knowledge.ArticleBean;
import com.example.heshequ.bean.knowledge.Author;
import com.example.heshequ.bean.knowledge.RecommendItemBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CircleView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class MyArticleDetialActivity extends NetWorkActivity implements View.OnClickListener {

    private RecommendItemBean RecommendItemBean;
    private TextView tvTitle, tvName, tvColumn;
    private CircleView ivHead;
    private int articleId = 0;
    private WebView webView;
    public static String title;
    public static String name;
    public static String avatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_article_detail);

        articleId = (int) getIntent().getSerializableExtra("ArticleId");
        init();
        event();
    }

    private void init() {
        setText("文章");
        ivHead = (CircleView) findViewById(R.id.ivHead);
        tvTitle = (TextView) findViewById(R.id.tvTitleArticle);
        tvName = (TextView) findViewById(R.id.tvName);
        tvColumn = (TextView) findViewById(R.id.tvColumn);
        webView = findViewById(R.id.content);
        getData(articleId);

    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(this);
        tvColumn.setOnClickListener(this);
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");
        if (ret == 0) {
            JSONObject object = new JSONObject(result.optString("data"));
            ArticleBean article = new ArticleBean();
            article.createTime = object.getString("createTime");
            article.updateTime = object.getString("updateTime");
            article.authorId = object.getInt("authorId");
            article.commentNum = object.getInt("commentNum");
            article.content = object.getString("content");
            article.id = object.getInt("id");
            article.label = object.getString("label");
            article.likeNum = object.getInt("likeNum");
            article.price = object.getDouble("price");
            article.score = object.getDouble("score");
            article.specialColumnId = object.getInt("specialColumnId");
            article.status = object.getString("status");
            article.title = object.getString("title");
            article.unlikeNum = object.getInt("unlikeNum");
            article.viewNum = object.getInt("viewNum");
            article.author = new Author();
            article.author.id = object.getJSONObject("author").getInt("id");
            article.author.header = object.getJSONObject("author").getString("header");
            article.author.nickname = object.getJSONObject("author").getString("nickname");

            tvTitle.setText(article.title == null ? "" : article.title);
            tvName.setText(article.author.nickname == null ? "" : article.author.nickname);
            if (article.author.header != null) {
                // 完善协议完善协议
                Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId=" + article.author.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("showset", "" + Constants.base_url + "/info/file/pub.do?fileId=" + article.author.header);
            } else {
                ivHead.setImageResource(R.mipmap.head3);
            }
            webView.loadData(article.content, "text/html", "UTF-8");

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvColumn:
                Intent intent = new Intent(context, CreateArticleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ArticleId", articleId);
                intent.putExtras(bundle);
                context.startActivity(intent);
                break;
        }
    }

    private void getData(int id) {
        sendGetConnection(WenConstans.getMyArticleDetail + "?id=" + id, new String[]{}, new String[]{}, 100, WenConstans.token);

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
