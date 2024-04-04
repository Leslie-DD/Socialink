package com.leslie.socialink.activity.knowledge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.knowledge.ArticleBean;
import com.leslie.socialink.bean.knowledge.Author;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.view.CircleView;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleDetialActivity extends NetWorkActivity {

    private TextView tvTitle, tvName, tvColumn;
    private CircleView ivHead;
    private WebView webView;
    private int articleId = 0;
    private int columnId = 0;
    public static String title;
    public static String name;
    public static String avatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detial);
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
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        tvColumn.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubscriptionDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Subscription", columnId);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int ret = result.optInt("code");

        if (ret == 0) {
            JSONObject object = result.getJSONObject("data");

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
            Log.e("ArticleDetialActivity", "article.title: " + (article.title == null));
            columnId = article.specialColumnId;
            tvTitle.setText(article.title == null ? "" : article.title);
            tvName.setText(article.author.nickname == null ? "" : article.author.nickname);
            if (article.author.header != null) {
                // 完善协议完善协议
                Glide.with(context).load(Constants.BASE_URL + "/info/file/pub.do?fileId=" + article.author.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("showset", "" + Constants.BASE_URL + "/info/file/pub.do?fileId=" + article.author.header);
            } else {
                ivHead.setImageResource(R.mipmap.head3);
            }

            webView.loadData(article.content, "text/html", "UTF-8");

        }
    }

    private void getData(int id) {
        sendGetConnection(Constants.ARTICLE_DETAIL + "?id=" + id, new String[]{}, new String[]{}, 100, Constants.token);
    }


}
