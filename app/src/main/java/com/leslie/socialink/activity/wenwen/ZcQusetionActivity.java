package com.leslie.socialink.activity.wenwen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.listview.ZcDisscussAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.ZcBean;
import com.leslie.socialink.bean.ZcQuestionBean;
import com.leslie.socialink.bean.ZcSecondBean;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.NoScrollWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ZcQusetionActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
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
    private ZcDisscussAdapter adapter;
    private long timeHint;
    private boolean hasclick;
    private ZcBean bean;
    private int niming;
    private int pn = 1;
    private int ps = 20;
    private int totalPage = 1;
    private boolean hasRefresh;
    private List<ZcQuestionBean> newList, moreList;
    private TextView tvTip;
    private TextView tvBlueTime;
    private String askid;
    private int dingPosition;
    private int sendId;
    private String content;
    private NoScrollWebView webView;
    private int firstItem;
    private int secondItem;
    private String delid;

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
                            new TypeToken<List<ZcQuestionBean>>() {
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
                            new TypeToken<List<ZcQuestionBean>>() {
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
            int nums = newList.get(dingPosition).likeAmount;
            if (TextUtils.isEmpty(newList.get(dingPosition).userLike)) {
                newList.get(dingPosition).userLike = "1";
                nums = nums + 1;
                Utils.toastShort(mContext, "顶成功");
            } else {
                newList.get(dingPosition).userLike = "";
                nums = nums - 1;
                Utils.toastShort(mContext, "取消顶成功");
            }
            newList.get(dingPosition).likeAmount = nums;
            adapter.setData(newList);
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
        setContentView(R.layout.activity_zc_question);
        init();
    }

    private void init() {
        bean = (ZcBean) getIntent().getSerializableExtra("beans");
        setTitleAndBack("专场提问");
        View headview = getLayoutInflater().inflate(R.layout.head_zc_question, null);
        tvBlueTime = (TextView) headview.findViewById(R.id.tvBlueTime);
        tvTip = (TextView) headview.findViewById(R.id.tvTips);
        ivImg = (ImageView) headview.findViewById(R.id.ivImg);
        ViewGroup.LayoutParams p = ivImg.getLayoutParams();
        p.height = ConsTants.screenW * 11 / 20;
        tvTitles = (TextView) headview.findViewById(R.id.tvTitles);
        tvZan = (TextView) headview.findViewById(R.id.tvZan);
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
                setQuestion(2, content);
            } else {
                setQuestion(1, content);
            }
            hideInput();
        });
        lv = (XRecyclerView) findViewById(R.id.lv);
        ConsTants.initXRecycleView(this, true, true, lv);
        adapter = new ZcDisscussAdapter(this);
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
            //tvContent.setText(Html.fromHtml(bean.introduction + "",imgGetter,null));
            webView.loadDataWithBaseURL(null, Utils.getNewContent(bean.introduction), "text/html", "utf-8", null);
            tvBlueTime.setText(bean.endTime + "");
        }
        initDialog();
        getList(100);
    }

    private void getList(int where) {
        if (where == 100) {
            pn = 1;
        }
        setBodyParams(new String[]{"id", "pn", "ps"}
                , new String[]{bean.id + "", pn + "", ps + ""});
        sendPost(WenConstans.ZcProblemsList, where, Constants.token);
    }

    private void setQuestion(int type, String content) {
        if (type == 1) {
            setBodyParams(new String[]{"id", "content", "anonymity", "type"}
                    , new String[]{bean.id + "", content, niming + "", type + ""});
            sendPost(WenConstans.ZcSendDisscuss, 1, Constants.token);
        } else {
            setBodyParams(new String[]{"id", "content", "anonymity", "type", "askid"}
                    , new String[]{bean.id + "", content, niming + "", type + "", askid + ""});
            sendPost(WenConstans.ZcSendDisscuss, 2, Constants.token);
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

    public void doDing(int position) {
        dingPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{newList.get(position).id + ""});
        sendPost(WenConstans.WwLike, 1000, Constants.token);
    }

    public void doDelete(String askId, int pofirst, int posecond) {
        /*setBodyParams(new String[]{"id"}, new String[]{delid + ""});
        sendPost(WenConstans.ZcDeleteDisscuss, 1001, Constants.token);*/
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


    private AlertDialog deldialog;

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("要删除这条回复吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除
                setBodyParams(new String[]{"id"}, new String[]{delid + ""});
                sendPost(WenConstans.ZcDeleteDisscuss, 1001, Constants.token);
                deldialog.dismiss();
            }


        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deldialog.dismiss();
            }
        });
        deldialog = builder.create();
        deldialog.setCancelable(false);
    }


}
