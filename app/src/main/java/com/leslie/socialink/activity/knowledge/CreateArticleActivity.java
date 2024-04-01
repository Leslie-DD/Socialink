package com.leslie.socialink.activity.knowledge;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.knowledge.ArticleBean;
import com.leslie.socialink.bean.knowledge.Author;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.scrat.app.richtext.RichEditText;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

public class CreateArticleActivity extends NetWorkActivity {

    private static final int REQUEST_CODE_GET_CONTENT = 666;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 444;
    private RichEditText richEditText;
    private Button submit;
    private EditText tvTitle;
    private int articleId = -1;
    private int specialColumnId = -1;
    ArticleBean mArticle = new ArticleBean();
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);

        richEditText = (RichEditText) findViewById(R.id.rich_text);
        tvTitle = findViewById(R.id.tvTitle);
        submit = findViewById(R.id.submit);
        context = this;
        if (getIntent().getSerializableExtra("ArticleId") != null) {
            articleId = (int) getIntent().getSerializableExtra("ArticleId");
            getData(articleId);
        } else if (getIntent().getSerializableExtra("specialColumnId") != null) {
            specialColumnId = (int) getIntent().getSerializableExtra("specialColumnId");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (articleId == -1) {

                    OkHttpUtils.put(WenConstans.newArticle)
                            .tag(this)
                            .headers(Constants.Token_Header, Constants.token)
                            .params("specialColumnId", specialColumnId)
                            .params("title", tvTitle.getText().toString())
                            .params("content", richEditText.toHtml())
                            .params("label", "编程")
                            .params("price", 0.1)
                            .params("status", 1)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Utils.toastShort(context, "新增成功");
                                    CreateArticleActivity.this.finish();
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    Utils.toastShort(context, "新增失败");
                                }
                            });
                } else {

                    mArticle.updateTime = "2020-05-26 16:00:00";
                    mArticle.title = tvTitle.getText().toString();
                    mArticle.content = richEditText.toHtml();
                    OkHttpUtils.put(WenConstans.midifyArticle)
                            .tag(this)
                            .headers(Constants.Token_Header, Constants.token)
                            .params("createTime", mArticle.createTime)
                            .params("updateTime", mArticle.updateTime)
                            .params("id", mArticle.id)
                            .params("specialColumnId", mArticle.specialColumnId)
                            .params("title", mArticle.title)
                            .params("content", mArticle.content)
                            .params("label", mArticle.label)
                            .params("authorId", mArticle.authorId)
                            .params("price", mArticle.price)
                            .params("score", mArticle.score)
                            .params("likeNum", mArticle.likeNum)
                            .params("unlikeNum", mArticle.unlikeNum)
                            .params("viewNum", mArticle.viewNum)
                            .params("status", mArticle.status)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Utils.toastShort(context, "修改成功");
                                    CreateArticleActivity.this.finish();
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    Utils.toastShort(context, "修改失败");
                                }
                            });
                }
            }
        });


//        richEditText.fromHtml(
//                "<blockquote>Android 端的富文本编辑器</blockquote>" +
//                        "<ul>" +
//                        "<li>支持实时编辑</li>" +
//                        "<li>支持图片插入,加粗,斜体,下划线,删除线,列表,引用块,超链接,撤销与恢复等</li>" +
//                        "<li>使用<u>Glide 4</u>加载图片</li>" +
//                        "</ul>" +
//                        "<img src=\"http://biuugames.huya.com/221d89ac671feac1.gif\"><br><br>" +
//                        "<img src=\"http://biuugames.huya.com/5-160222145918.jpg\"><br><br>"
//        );
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

            mArticle = article;
            tvTitle.setText(article.title == null ? "" : article.title);
            richEditText.fromHtml(article.content == null ? "" : article.content);

        }

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    private void getData(int id) {
        sendGetConnection(WenConstans.getMyArticleDetail + "?id=" + id, new String[]{}, new String[]{}, 100, Constants.token);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_article, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // TODO: fix
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.ivBack:
//                finish();
//                break;
//            case R.id.undo:
//                richEditText.undo();
//                break;
//            case R.id.redo:
//                richEditText.redo();
//                break;
//            case R.id.export:
//                Log.e("xxx", richEditText.toHtml());
//                break;
//            default:
//                break;
//        }
//
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || data.getData() == null || requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            return;

        final Uri uri = data.getData();
        final int width = richEditText.getMeasuredWidth() - richEditText.getPaddingLeft() - richEditText.getPaddingRight();
        richEditText.image(uri, width);
    }

    /**
     * 加粗
     */
    public void setBold(View v) {
        richEditText.bold(!richEditText.contains(RichEditText.FORMAT_BOLD));
    }

    /**
     * 斜体
     */
    public void setItalic(View v) {
        richEditText.italic(!richEditText.contains(RichEditText.FORMAT_ITALIC));
    }

    /**
     * 下划线
     */
    public void setUnderline(View v) {
        richEditText.underline(!richEditText.contains(RichEditText.FORMAT_UNDERLINED));
    }

    /**
     * 删除线
     */
    public void setStrikethrough(View v) {
        richEditText.strikethrough(!richEditText.contains(RichEditText.FORMAT_STRIKETHROUGH));
    }

    /**
     * 序号
     */
    public void setBullet(View v) {
        richEditText.bullet(!richEditText.contains(RichEditText.FORMAT_BULLET));
    }

    /**
     * 引用块
     */
    public void setQuote(View v) {
        richEditText.quote(!richEditText.contains(RichEditText.FORMAT_QUOTE));
    }

    public void insertImg(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
        getImage.addCategory(Intent.CATEGORY_OPENABLE);
        getImage.setType("image/*");
        startActivityForResult(getImage, REQUEST_CODE_GET_CONTENT);
    }
}
