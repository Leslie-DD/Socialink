package com.leslie.socialink.activity.wenwen;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.ReportActivity;
import com.leslie.socialink.activity.team.PersonalInformationActivity;
import com.leslie.socialink.adapter.listview.PictureAdapter;
import com.leslie.socialink.adapter.listview.WwDisscussAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.ShareBean;
import com.leslie.socialink.bean.WwDisscussBean;
import com.leslie.socialink.constans.ResultUtils;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.fragment.BottomShareFragment;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.QuestionBean;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.CircleView;
import com.leslie.socialink.view.MyLv;
import com.leslie.socialink.view.XialaPop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WenwenDetailActivity extends NetWorkActivity implements XRecyclerView.LoadingListener, BottomShareFragment.DoClickListener {
    private int type = 0;
    private CircleView ivHead;
    private EditText etContent;
    private ImageView ivBq;
    private ImageView ivSend;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvTitles;
    private TextView tvBelong;
    private TextView tvLoves;
    private TextView tvNum, tvContent;
    private MyLv lvPicture;
    private XRecyclerView lvDisscuss;
    private PictureAdapter pictureAdapter;
    private WwDisscussAdapter wwDisscussAdapter;
    private ImageView ivRight;
    private QuestionBean wenwenBean;
    private LinearLayout llSave;
    private ImageView ivImg;
    private int dz;
    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<WwDisscussBean> newList, moreList;
    private TextView tvTip;
    private int clickPosition;
    private String save = "收藏";
    private int resultPosition;
    private RefreshBrodcast brodcast;
    private AlertDialog deldialog;
    private BottomShareFragment shareFragment;
    private boolean isQQShare;
    private boolean isWBShare;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            if (result.has("msg")) {
                Utils.toastShort(context, result.getString("msg"));
            }
            return;
        }
        if (where == 1000) {
            Utils.toastShort(mContext, result.getString("msg") + "");
            int zan = wenwenBean.likeAmount;
            if (TextUtils.isEmpty(wenwenBean.userLike)) {
                wenwenBean.userLike = "1";
                zan = zan + 1;
                wenwenBean.likeAmount = zan;
                ivImg.setImageResource(R.mipmap.saved);
                tvLoves.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            } else {
                wenwenBean.userLike = "";
                zan = zan - 1;
                wenwenBean.likeAmount = zan;
                ivImg.setImageResource(R.mipmap.sc);
                tvLoves.setTextColor(Color.parseColor("#ababb3"));
            }
            tvLoves.setText(zan + "");
            if (dz != zan) {
                Intent intent = new Intent();
                intent.setAction("fragment.listener");
                intent.putExtra("item", 2);
                sendBroadcast(intent);
            }
        } else if (where == 100) {
            if (hasRefresh) {
                hasRefresh = false;
                lvDisscuss.refreshComplete();
            }
            Gson gson = new Gson();
            if (result.has("data")) {
                JSONObject obj = result.getJSONObject("data");
                totalPage = obj.getInt("totalPage");
                if (obj != null && obj.has("list")) {
                    newList = gson.fromJson(obj.getJSONArray("list").toString(),
                            new TypeToken<List<WwDisscussBean>>() {
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
            wwDisscussAdapter.setData(newList);
        } else if (where == 101) {
            lvDisscuss.loadMoreComplete();
            Gson gson = new Gson();
            if (result.has("data")) {
                JSONObject obj = result.getJSONObject("data");
                if (obj != null && obj.has("list")) {
                    moreList = gson.fromJson(obj.getJSONArray("list").toString(),
                            new TypeToken<List<WwDisscussBean>>() {
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
            wwDisscussAdapter.setData(newList);
        } else if (where == 102) {
            etContent.setText("");
            Utils.toastShort(mContext, "评论成功");
            Intent intent = new Intent();
            intent.setAction("fragment.listener");
            intent.putExtra("item", 2);
            sendBroadcast(intent);
            getDisscuss(100);
            tvNum.setText((Integer.parseInt(tvNum.getText().toString().trim()) + 1) + "");
        } else if (where == 103) {
            int nums = newList.get(clickPosition).likeAmount;
            if (TextUtils.isEmpty(newList.get(clickPosition).isTop)) {
                newList.get(clickPosition).isTop = "1";
                nums = nums + 1;
                Utils.toastShort(mContext, "顶成功");
            } else {
                newList.get(clickPosition).isTop = "";
                nums = nums - 1;
                Utils.toastShort(mContext, "取消顶成功");
            }
            newList.get(clickPosition).likeAmount = nums;
            wwDisscussAdapter.setData(newList);
        } else if (where == 104) {
            if (save.equals("收藏")) {
                Intent intent = new Intent();
                intent.putExtra("item", 2);
                intent.setAction("fragment.listener");
                sendBroadcast(intent);
                Utils.toastShort(mContext, "收藏成功");
                save = "取消收藏";
            } else {
                Intent intent = new Intent();
                intent.putExtra("item", 2);
                intent.setAction("fragment.listener");
                sendBroadcast(intent);
                Utils.toastShort(mContext, "取消收藏成功");
                save = "收藏";
            }
        } else if (where == 105) {
            Utils.toastShort(mContext, "举报已提交");
        } else if (where == 10086) {
            if (result.optInt("code") == 0) {
                Gson gson = new Gson();
                wenwenBean = gson.fromJson(result.optString("data"), QuestionBean.class);
                dz = wenwenBean.likeAmount;
                setListener();
                init();
                getDisscuss(100);
            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        } else if (where == 10010) {
            if (result.optInt("code") == 0) {
                Intent intent = new Intent();
                intent.putExtra("item", 2);
                intent.setAction("fragment.listener");
                sendBroadcast(intent);
                Utils.toastShort(mContext, "删除问问成功");
                this.finish();
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
        setContentView(R.layout.activity_ww_detail);
        EventBus.getDefault().register(this);
        wenwenBean = (QuestionBean) getIntent().getSerializableExtra("wenwen");
        dz = wenwenBean.likeAmount;
        setListener();
        init();
        getDisscuss(100);

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        setTitleAndBack("问题详情");
        View headview = getLayoutInflater().inflate(R.layout.head_ww_detail, null);
        tvTip = (TextView) headview.findViewById(R.id.tvTips);
        ivHead = (CircleView) headview.findViewById(R.id.ivHead);
        tvName = (TextView) headview.findViewById(R.id.tvName);
        tvTime = (TextView) headview.findViewById(R.id.tvTime);
        tvTitles = (TextView) headview.findViewById(R.id.tvTitles);
        tvBelong = (TextView) headview.findViewById(R.id.tvBelong);
        tvLoves = (TextView) headview.findViewById(R.id.tvLoves);
        tvNum = (TextView) headview.findViewById(R.id.tvNum);
        tvContent = (TextView) headview.findViewById(R.id.tvContent);
        tvContent.setOnClickListener(v -> {
            if (tvContent.getLineCount() >= 5) {
                tvContent.setEllipsize(null);
                tvContent.setSingleLine(false);
                lvPicture.setVisibility(View.VISIBLE);
            }
        });
        etContent = (EditText) findViewById(R.id.etContent);
        llSave = (LinearLayout) headview.findViewById(R.id.llSave);
        llSave.setOnClickListener(v -> {
            setBodyParams(new String[]{"id"}, new String[]{wenwenBean.id + ""});
            sendPost(WenConstans.WwLike, 1000, Constants.token);
        });
        ivImg = (ImageView) headview.findViewById(R.id.ivImg);
        ivBq = (ImageView) findViewById(R.id.ivBq);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setImageResource(R.mipmap.more3);
        ivRight.setOnClickListener(v -> {
            XialaPop.showSelectPop(this, save, wenwenBean.uid.equals(Constants.uid + ""), true, new XialaPop.TextListener() {
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
                        DelWw();
                    } else if (num == 3) {
                        //Utils.toastShort("分享");
                        showShare();
                    }
                }
            });
        });
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
        ivHead.setOnClickListener(v -> {
            if (wenwenBean.anonymity == 0) {
                startActivity(new Intent(this, PersonalInformationActivity.class).putExtra("uid", Integer.parseInt(wenwenBean.uid)));
            }
        });
        lvPicture = (MyLv) headview.findViewById(R.id.lvPicture);
        pictureAdapter = new PictureAdapter(this);
        lvPicture.setAdapter(pictureAdapter);
        lvDisscuss = (XRecyclerView) findViewById(R.id.lvDisscuss);
        ConsTants.initXRecycleView(this, true, true, lvDisscuss);
        wwDisscussAdapter = new WwDisscussAdapter(this);
        lvDisscuss.setAdapter(wwDisscussAdapter);
        lvDisscuss.addHeaderView(headview);
        lvDisscuss.setLoadingListener(this);
        if (wenwenBean != null) {
            if (wenwenBean.anonymity == 0) {
                tvName.setText(wenwenBean.nn == null ? "" : wenwenBean.nn);
                if (TextUtils.isEmpty(wenwenBean.header)) {
                    ivHead.setImageResource(R.mipmap.head3);
                } else {
                    Glide.with(context).load(Constants.base_url + wenwenBean.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                }
            } else {
                tvName.setText("匿名用户");
                ivHead.setImageResource(R.mipmap.head3);
            }
            tvTitles.setText(wenwenBean.title + "");
            tvContent.setText(wenwenBean.content + "");
            tvContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回调会调用多次，获取完行数记得注销监听
                    tvContent.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (tvContent.getLineCount() >= 5) {
                        lvPicture.setVisibility(View.GONE);
                        return false;
                    }
                    return false;
                }
            });
            tvTime.setText(wenwenBean.time + "");
            tvLoves.setText(wenwenBean.likeAmount + "");
            tvNum.setText(wenwenBean.commentAmount + "");
            if (wenwenBean.labels != null && wenwenBean.labels.size() > 0) {
                String str = "";
                for (int i = 0; i < wenwenBean.labels.size(); i++) {
                    if (i == 0) {
                        str = "#" + wenwenBean.labels.get(i).name + "#";
                    } else {
                        str = str + "   #" + wenwenBean.labels.get(i).name + "#";
                    }
                }
                tvBelong.setText(str);
            } else {
                tvBelong.setText("");
            }
            if (TextUtils.isEmpty(wenwenBean.userLike)) {
                ivImg.setImageResource(R.mipmap.sc);
                tvLoves.setTextColor(Color.parseColor("#ababb3"));
            } else {
                ivImg.setImageResource(R.mipmap.saved);
                tvLoves.setTextColor(Color.parseColor("#00bbff"));
            }
            if (TextUtils.isEmpty(wenwenBean.isCollect)) {
                save = "收藏";
            } else {
                save = "取消收藏";
            }
            if (wenwenBean.photos != null && wenwenBean.photos.size() > 0) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < wenwenBean.photos.size(); i++) {
                    list.add(WenConstans.BaseUrl + wenwenBean.photos.get(i).photoId);
                }
                lvPicture.setVisibility(View.VISIBLE);
                pictureAdapter.setData(list);
            } else {
                lvPicture.setVisibility(View.GONE);
            }
        }

        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("确定要删除这条问问吗？");
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            //删除
            setBodyParams(new String[]{"uid", "askid"}, new String[]{wenwenBean.uid, wenwenBean.id + ""});
            sendPost(Constants.base_url + "/api/ask/base/deleteMyAsk.do", 10010, Constants.token);
            deldialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> deldialog.dismiss());
        deldialog = builder.create();
        deldialog.setCancelable(false);
    }

    private void getDisscuss(int where) {
        if (where == 100) {
            pn = 1;
        }
        setBodyParams(new String[]{"askid", "pn", "ps"}, new String[]{wenwenBean.id + "", pn + "", ps + ""});
        sendPost(WenConstans.WwFirst, where, Constants.token);
    }

    private void sendDisscuss(String content) {
        setBodyParams(new String[]{"id", "type", "content"}, new String[]{wenwenBean.id + "", 1 + "", content});
        sendPost(WenConstans.WwDisscuss, 102, Constants.token);
    }

    private void saveWw(String type) {
        setBodyParams(new String[]{"id", "op"}, new String[]{wenwenBean.id + "", type});
        sendPost(WenConstans.WwDSave, 104, Constants.token);
    }

    private void jbWw() {
        /*setBodyParams(new String[]{"id"}
                ,new String[]{wenwenBean.id+""});
        sendPost(WenConstans.WwJuBao,105,Constants.token);*/
        startActivity(new Intent(this, ReportActivity.class).putExtra("type", 1).putExtra("id", wenwenBean.id));
    }

    private void DelWw() {
        deldialog.show();
    }

    @Override
    public void onRefresh() {
        hasRefresh = true;
        pn = 1;
        getDisscuss(100);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refData(WwDisscussBean bean) {
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
                    lvDisscuss.loadMoreComplete();
                }
            }, 1000);
        } else {
            getDisscuss(101);
        }
    }

    public void doDing(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}
                , new String[]{newList.get(position).id + ""});
        sendPost(WenConstans.WwDing, 103, Constants.token);
    }

    public void doSecond(int position) {
        resultPosition = position;
        Intent intent = new Intent(this, WwSecondActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", newList.get(position));
        intent.putExtra("save", save);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    private void setListener() {
        brodcast = new RefreshBrodcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refresh.data");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(brodcast, intentFilter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(brodcast, intentFilter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(brodcast);
    }

    private class RefreshBrodcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                WwDisscussBean beans = (WwDisscussBean) intent.getSerializableExtra("beans");
                String resultSave = intent.getStringExtra("save");
                if (beans != null) {
                    newList.get(resultPosition).likeAmount = beans.likeAmount;
                    newList.get(resultPosition).commentAmount = beans.commentAmount;
                    newList.get(resultPosition).isTop = beans.isTop;
                    wwDisscussAdapter.setData(newList);
                }
                if (!TextUtils.isEmpty(resultSave)) {
                    save = resultSave;
                }
            }
        }
    }

    public void showShare() {
        if (shareFragment == null) {
            shareFragment = new BottomShareFragment();
            shareFragment.setDoClickListener(this);
        }
        shareFragment.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void clickPosition(int position) {
        ShareBean shareBean = shareFragment.getGvData().get(position);
        // TODO: support
        Utils.toastShort(mContext, "尚不支持");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

}
