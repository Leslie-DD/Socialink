package com.hnu.heshequ.activity.oldsecond;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.PersonalInformationActivity;
import com.hnu.heshequ.adapter.listview.GoodsecondAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.GoodsdiscussBean;
import com.hnu.heshequ.constans.ResultUtils;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.network.Constants;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CircleView;
import com.hnu.heshequ.view.XialaPop;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GoodsecondActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private TextView tvName;
    private TextView tvTime;
    private TextView tvDing;
    private TextView tvContent;
    private TextView tvResult;
    private EditText etContent;
    private ImageView ivBq;
    private ImageView ivSend;
    private XRecyclerView lv;
    private GoodsecondAdapter adapter;
    private ImageView ivRight;
    private GoodsdiscussBean bean;
    private CircleView ivHead;
    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<GoodsdiscussBean> newList, moreList;
    private TextView tvTip;
    private String save = "收藏";
    private boolean hasDiss;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            return;
        }
        if (where == 100) {
            if (hasRefresh) {
                hasRefresh = false;
                lv.refreshComplete();
            }
            Gson gson = new Gson();
            if (result.has("data")) {
                JSONObject obj = result.getJSONObject("data");
                totalPage = obj.getInt("totalPage");
                if (obj != null && obj.has("list")) {
                    newList = gson.fromJson(obj.getJSONArray("list").toString(),
                            new TypeToken<List<GoodsdiscussBean>>() {
                            }.getType());
                    if (newList == null) {
                        newList = new ArrayList<>();
                    }
                } else {
                    newList = new ArrayList<>();
                }
            } else {
                newList = new ArrayList<>();
            }
            if (newList.size() == 0) {
                tvTip.setVisibility(View.VISIBLE);
            } else {
                tvTip.setVisibility(View.GONE);
            }
            adapter.setData(newList);
            tvResult.setText("回复 (" + newList.size() + ")");
            if (hasDiss) {
                bean.commentAmount = newList.size();
                hasDiss = false;
                Intent intent = new Intent();
                intent.setAction("refresh.data");
                Bundle bundle = new Bundle();
                bundle.putSerializable("beans", bean);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        } else if (where == 101) {
            lv.loadMoreComplete();
            Gson gson = new Gson();
            if (result.has("data")) {
                JSONObject obj = result.getJSONObject("data");
                if (obj != null && obj.has("list")) {
                    moreList = gson.fromJson(obj.getJSONArray("list").toString(),
                            new TypeToken<List<GoodsdiscussBean>>() {
                            }.getType());
                    if (moreList == null) {
                        moreList = new ArrayList<>();
                    }
                } else {
                    moreList = new ArrayList<>();
                }
            } else {
                moreList = new ArrayList<>();
            }
            newList.addAll(moreList);
            if (newList.size() == 0) {
                tvTip.setVisibility(View.VISIBLE);
            } else {
                tvTip.setVisibility(View.GONE);
            }
            adapter.setData(newList);
            tvResult.setText("回复 (" + newList.size() + ")");
        } else if (where == 102) {
            etContent.setText("");
            Utils.toastShort(mContext, "评论成功");
            hasDiss = true;
            getDisscuss(100);
        } else if (where == 103) {
            int nums = bean.likeAmount;
            if (TextUtils.isEmpty(bean.isTop)) {
                Utils.toastShort(mContext, "顶成功");
                tvDing.setText(nums + 1 + "顶");
                bean.likeAmount = nums + 1;
                bean.isTop = "1";
                tvDing.setTextColor(Color.parseColor("#05bcff"));
            } else {
                Utils.toastShort(mContext, "取消顶成功");
                tvDing.setText(nums - 1 + "顶");
                bean.likeAmount = nums - 1;
                bean.isTop = "";
                tvDing.setTextColor(Color.parseColor("#939393"));
            }
            Intent intent = new Intent();
            intent.setAction("refresh.data");
            Bundle bundle = new Bundle();
            bundle.putSerializable("beans", bean);
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (where == 104) {
            if (save.equals("收藏")) {
                Utils.toastShort(mContext, "收藏成功");
                save = "取消收藏";
            } else {
                Utils.toastShort(mContext, "取消收藏成功");
                save = "收藏";
            }
            Intent intent = new Intent();
            intent.setAction("refresh.data");
            intent.putExtra("save", save);
            sendBroadcast(intent);
        } else if (where == 105) {
            Utils.toastShort(mContext, "举报已提交");
        } else if (where == 10086) {
            if (result.optInt("code") == 0) {
                EventBus.getDefault().post(new GoodsdiscussBean());
                Utils.toastShort(mContext, "删除评论成功");
                this.finish();
            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        } else if (where == 10010) {
            if (result.optInt("code") == 0) {
                hasRefresh = true;
                pn = 1;
                getDisscuss(100);
                Utils.toastShort(mContext, "删除评论成功");

            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsecond);
        bean = (GoodsdiscussBean) getIntent().getSerializableExtra("bean");
        save = getIntent().getStringExtra("save");
        init();
        getDisscuss(100);
    }

    private void init() {
        setTitleAndBack("评论");
        View headview = getLayoutInflater().inflate(R.layout.head_ww_second, null);
        tvTip = (TextView) headview.findViewById(R.id.tvTips);
        tvName = (TextView) headview.findViewById(R.id.tvName);
        tvTime = (TextView) headview.findViewById(R.id.tvTime);
        tvDing = (TextView) headview.findViewById(R.id.tvDing);
        tvDing.setOnClickListener(v -> {
            setBodyParams(new String[]{"id"}, new String[]{bean.id + ""});
            sendPost(WenConstans.SecondgooddiscussDing, 103, WenConstans.token);
        });
        tvContent = (TextView) headview.findViewById(R.id.tvContent);
        tvResult = (TextView) headview.findViewById(R.id.tvResult);
        etContent = (EditText) findViewById(R.id.etContent);
        ivBq = (ImageView) findViewById(R.id.ivBq);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setImageResource(R.mipmap.more3);
        ivRight.setOnClickListener(v -> {
            XialaPop.showSelectPop(this, save, bean.uid.equals(Constants.uid + ""), false, new XialaPop.TextListener() {
                @Override
                public void selectPosition(int num) {
                    if (num == 0) {    //收藏相关
                        if (save.equals("收藏")) {
                            saveWw("1");
                        } else {
                            saveWw("2");
                        }
                    } else if (num == 1) {          //举报
                        jbWw();
                    } else if (num == 2) {
                        jbDel();
                    }
                }
            });
        });
        ivHead = (CircleView) headview.findViewById(R.id.ivHead);
        ivHead.setOnClickListener(v -> startActivity(new Intent(this, PersonalInformationActivity.class).putExtra("uid", Integer.parseInt(bean.uid))));
        ivSend.setOnClickListener(v -> {
            String content = etContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Utils.toastShort(mContext, "您还没有输入任何内容");
                return;
            }
            if (content.length() > 100) {
                Utils.toastShort(mContext, "最多评论100个字符");
                return;
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etContent,InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0); //强制隐藏键盘
            sendDisscuss(content);
        });
        lv = (XRecyclerView) findViewById(R.id.lv);
        ConsTants.initXRecycleView(this, false, false, lv);
        adapter = new GoodsecondAdapter(this);
        lv.setAdapter(adapter);
        lv.addHeaderView(headview);
        lv.setLoadingListener(this);
        if (bean != null) {
            Glide.with(this).load(WenConstans.BaseUrl + bean.header).asBitmap()
                    .fitCenter().placeholder(R.mipmap.head3).into(ivHead);
            tvName.setText(bean.nn + "");
            tvTime.setText(bean.time + "");
            tvContent.setText(bean.content + "");
            tvResult.setText("回复 (" + bean.commentAmount + ")");
            tvDing.setText(bean.likeAmount + "顶");
            if (TextUtils.isEmpty(bean.isTop)) {
                tvDing.setTextColor(Color.parseColor("#939393"));
            } else {
                tvDing.setTextColor(Color.parseColor("#05bcff"));
            }
        }

        adapter.setListener(position -> jbDel2(position));
    }

    private void jbDel2(int p) {
//        setBodyParams(new String[]{"uid", "type", "id"}
//                , new String[]{adapter.getData().get(p).uid, 2 + "", adapter.getData().get(p).id + ""});
//        sendPost(Constants.base_url + "/api/ask/base/deleteMyComment.do", 10010, WenConstans.token);
    }

    private void jbDel() {
//        setBodyParams(new String[]{"uid", "type", "id"}
//                , new String[]{bean.uid, 1 + "", bean.id + ""});
//        sendPost(Constants.base_url + "/api/ask/base/deleteMyComment.do", 10086, WenConstans.token);
    }

    private void getDisscuss(int where) {
        setBodyParams(new String[]{"firstid", "pn", "ps"}
                , new String[]{bean.id + "", pn + "", ps + ""});
        sendPost(WenConstans.SecondgooddiscussSecond, where, WenConstans.token);
    }

    private void sendDisscuss(String content) {
        setBodyParams(new String[]{"id", "type", "content"}
                , new String[]{bean.id + "", 2 + "", content});
        sendPost(WenConstans.Secondgooddiscuss, 102, WenConstans.token);
    }

    private void saveWw(String type) {
        setBodyParams(new String[]{"id", "op"}
                , new String[]{bean.id + "", type});
        sendPost(WenConstans.Secondgoodcollect, 104, WenConstans.token);
    }

    private void jbWw() {
//        setBodyParams(new String[]{"id"}
//                , new String[]{bean.askId + ""});
//        sendPost(WenConstans.WwJuBao, 105, WenConstans.token);
    }

    @Override
    public void onRefresh() {
        hasRefresh = true;
        pn = 1;
        getDisscuss(100);
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
            getDisscuss(101);
        }
    }


}
