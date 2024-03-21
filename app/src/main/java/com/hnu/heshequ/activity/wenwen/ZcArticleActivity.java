package com.hnu.heshequ.activity.wenwen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.listview.ZcAnswerAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.ZcAnswerBean;
import com.hnu.heshequ.bean.ZcBean;
import com.hnu.heshequ.bean.ZcSecondBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.ResultUtils;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.NoScrollWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ZcArticleActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private ImageView ivImg;
    private ImageView ivSelect;
    private ImageView ivSend;
    private TextView tvTitles;
    private TextView tvZan;
    private TextView tvName;
    private TextView tvJob;
    private TextView tvTime;
    private EditText etContent;
    private XRecyclerView lv;
    private ZcAnswerAdapter adapter;
    private ZcBean bean;
    private LinearLayout llZan;
    private ImageView ivZan;
    private int niming;
    private int pn = 1;
    private int ps = 20;
    private int totalPage = 1;
    private boolean hasRefresh;
    private List<ZcAnswerBean> newList, moreList;
    private TextView tvTip;
    private boolean hasclick;
    private int sendId;
    private String askid;
    private String content;
    private NoScrollWebView webView;
    private AlertDialog deldialog;
    private String delid;
    private int firstItem;
    private int secondItem;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            return;
        }
        Gson gson = new Gson();
        if (where == 1) {
            etContent.setText("");
            Utils.toastShort(mContext, "评论成功");
            getList(100);
        } else if (where == 2) {
            etContent.setText("");
            Utils.toastShort(mContext, "评论成功");
            if (result.has("data")) {
                JSONObject obj = result.getJSONObject("data");
                ZcSecondBean beans = new ZcSecondBean();
                beans.id = obj.getString("id");
                beans.nn = Constants.userName + "";
                beans.uid = Constants.uid + "";
                beans.content = content;
                if (niming == 1) {
                    beans.anonymity = 1;
                }
                if (newList.get(sendId).commentVos == null) {
                    newList.get(sendId).commentVos = new ArrayList<>();
                }
                newList.get(sendId).commentVos.add(0, beans);
                adapter.setData(newList);
            }
        } else if (where == 100) {
            newList = new ArrayList<>();
            if (hasRefresh) {
                hasRefresh = false;
                lv.refreshComplete();
            }
            if (result.has("data")) {
                JSONObject obj = result.getJSONObject("data");
                if (obj != null && obj.has("list")) {
                    totalPage = obj.getInt("totalPage");
                    newList = gson.fromJson(obj.getJSONArray("list").toString(),
                            new TypeToken<List<ZcAnswerBean>>() {
                            }.getType());
                    if (newList == null) {
                        newList = new ArrayList<>();
                    }
                }
            }
            if (newList.size() == 0) {
                tvTip.setVisibility(View.VISIBLE);
            } else {
                tvTip.setVisibility(View.GONE);
            }
            adapter.setData(newList);
        } else if (where == 101) {
            moreList = new ArrayList<>();
            lv.loadMoreComplete();
            if (result.has("data")) {
                JSONObject obj = result.getJSONObject("data");
                if (obj != null && obj.has("list")) {
                    totalPage = obj.getInt("totalPage");
                    moreList = gson.fromJson(obj.getJSONArray("list").toString(),
                            new TypeToken<List<ZcAnswerBean>>() {
                            }.getType());
                    if (moreList == null) {
                        moreList = new ArrayList<>();
                    }
                }
            }
            if (newList.size() == 0) {
                tvTip.setVisibility(View.VISIBLE);
            } else {
                tvTip.setVisibility(View.GONE);
            }
            newList.addAll(moreList);
            adapter.setData(newList);
        } else if (where == 1000) {
            int zan = bean.likeAmount;
            if (TextUtils.isEmpty(bean.isLike)) {
                bean.isLike = "1";
                bean.likeAmount = zan + 1;
                Utils.toastShort(mContext, "点赞成功");
                tvZan.setText(zan + 1 + "");
            } else {
                bean.isLike = "";
                bean.likeAmount = zan - 1;
                Utils.toastShort(mContext, "取消点赞成功");
                tvZan.setText(zan - 1 + "");
            }
            if (TextUtils.isEmpty(bean.isLike)) {
                ivZan.setImageResource(R.mipmap.zan);
                tvZan.setTextColor(Color.parseColor("#ababb3"));
            } else {
                ivZan.setImageResource(R.mipmap.zan2);
                tvZan.setTextColor(Color.parseColor("#00bbff"));
            }
            Intent intent = new Intent();
            intent.setAction("zcRefresh");
            intent.putExtra("refresh", true);
            sendBroadcast(intent);
        } else if (where == 1001) {
            Utils.toastShort(mContext, "删除成功");
            newList.get(firstItem).commentVos.remove(secondItem);
            adapter.setData(newList);
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_zc_article);
        init();
    }

    private void init() {
        bean = (ZcBean) getIntent().getSerializableExtra("beans");
        setTitleAndBack("文章问答");
        View headview = getLayoutInflater().inflate(R.layout.head_zc_aritice, null);
        tvTip = (TextView) headview.findViewById(R.id.tvTips);
        ivImg = (ImageView) headview.findViewById(R.id.ivImg);
        ViewGroup.LayoutParams p = ivImg.getLayoutParams();
        p.height = ConsTants.screenW * 11 / 20;
        tvTitles = (TextView) headview.findViewById(R.id.tvTitles);
        llZan = (LinearLayout) headview.findViewById(R.id.llZan);
        llZan.setOnClickListener(v -> {
            setBodyParams(new String[]{"id"}
                    , new String[]{bean.id + ""});
            sendPost(WenConstans.ZcZan, 1000, WenConstans.token);
        });
        tvZan = (TextView) headview.findViewById(R.id.tvZan);
        ivZan = (ImageView) headview.findViewById(R.id.ivZan);
        tvName = (TextView) headview.findViewById(R.id.tvName);
        tvJob = (TextView) headview.findViewById(R.id.tvJob);
        tvTime = (TextView) headview.findViewById(R.id.tvTime);
        etContent = (EditText) findViewById(R.id.etContent);
        //tvContent = (TextView) headview.findViewById(R.id.tvContent);
        webView = (NoScrollWebView) headview.findViewById(R.id.webView);
        ivSelect = (ImageView) findViewById(R.id.ivSelect);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        ivSelect.setOnClickListener(v -> {
            if (niming == 1) {
                niming = 0;
                ivSelect.setImageResource(R.mipmap.unselected);
            } else {
                niming = 1;
                ivSelect.setImageResource(R.mipmap.selected2);
            }
        });
        ivSend.setOnClickListener(v -> {
            content = etContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Utils.toastShort(mContext, "您还没有输入任何内容");
                return;
            }
            if (content.length() > 100) {
                Utils.toastShort(mContext, "最多评论100个字符");
                return;
            }
            if (hasclick) {
                hasclick = false;
                setBodyParams(new String[]{"id", "content", "anonymity", "type", "askid"}
                        , new String[]{bean.id + "", content, niming + "", 2 + "", askid + ""});
                sendPost(WenConstans.ZcSendDisscuss, 2, WenConstans.token);
            } else {
                setBodyParams(new String[]{"id", "content", "anonymity"}
                        , new String[]{bean.id + "", content, niming + ""});
                sendPost(WenConstans.ZcDisscuss, 1, WenConstans.token);
            }
            hideInput();

        });
        lv = (XRecyclerView) findViewById(R.id.lv);
        ConsTants.initXRecycleView(this, true, true, lv);
        adapter = new ZcAnswerAdapter(this);
        lv.setAdapter(adapter);
        lv.addHeaderView(headview);
        lv.setLoadingListener(this);
        if (bean != null) {
            tvTitles.setText(bean.title + "");
            Glide.with(context).load(WenConstans.BaseUrl + bean.coverImage).asBitmap().fitCenter()
                    .placeholder(R.mipmap.mrtp).into(ivImg);
            tvTime.setText(bean.time + "");
            tvName.setText(bean.presentorName + "");
            tvJob.setText(bean.presentorJob + "");

            //tvContent.setText(Html.fromHtml(bean.introduction+"", imgGetter,null));
            webView.loadDataWithBaseURL(null, Utils.getNewContent(bean.introduction), "text/html", "utf-8", null);
            if (TextUtils.isEmpty(bean.isLike)) {
                ivZan.setImageResource(R.mipmap.zan);
                tvZan.setTextColor(Color.parseColor("#ababb3"));
            } else {
                ivZan.setImageResource(R.mipmap.zan2);
                tvZan.setTextColor(Color.parseColor("#00bbff"));
            }
            tvZan.setText(bean.likeAmount + "");
        }
        getList(100);
        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("要删除这条回复吗？");
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            //删除
           /* setBodyParams(new String[]{"id"}, new String[]{"" + delid});
            sendPost(Constants.base_url + "/api/club/activity/delcomment.do", delComment, Constants.token);*/

            setBodyParams(new String[]{"id"}, new String[]{delid + ""});
            sendPost(WenConstans.ZcDeleteDisscuss, 1001, WenConstans.token);
            deldialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> deldialog.dismiss());
        deldialog = builder.create();
        deldialog.setCancelable(false);
    }

    private void getList(int where) {
        if (where == 100) {
            pn = 1;
        }
        setBodyParams(new String[]{"id", "pn", "ps"}
                , new String[]{bean.id + "", pn + "", ps + ""});
        sendPost(WenConstans.ZcProblemsList, where, WenConstans.token);
    }


    @Override
    public void onRefresh() {
        pn = 1;
        hasRefresh = true;
        getList(100);
    }

    @Override
    public void onLoadMore() {
        pn++;
        if (pn > totalPage) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lv.loadMoreComplete();
                }
            }, 1000);
        } else {
            getList(101);
        }
    }

    public void doSend(int item) {
        sendId = item;
        hasclick = true;
        askid = newList.get(item).id;
        showInput();
    }

    private void showInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etContent, InputMethodManager.SHOW_FORCED);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        etContent.requestFocus();
    }

    private void hideInput() {
        etContent.setText("");
        etContent.setHint("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etContent,InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
    }

    public void doDelete(String askId, int pofirst, int posecond) {
        firstItem = pofirst;
        secondItem = posecond;
        delid = askId;
        deldialog.show();
    }

    //webview使用时，退出及时清理
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }
        if (deldialog != null) {
            deldialog.cancel();
            deldialog = null;
        }
    }


}
