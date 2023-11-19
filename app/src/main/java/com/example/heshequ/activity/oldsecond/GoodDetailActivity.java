package com.example.heshequ.activity.oldsecond;

import static com.example.heshequ.MeetApplication.mTencent;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.activity.ReportActivity;
import com.example.heshequ.activity.team.PersonalInformationActivity;
import com.example.heshequ.adapter.listview.GoodDisscussAdapter;
import com.example.heshequ.adapter.listview.PictureAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.GoodsdiscussBean;
import com.example.heshequ.bean.SecondhandgoodBean;
import com.example.heshequ.bean.ShareBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.fragment.BottomShareFragment;
import com.example.heshequ.interfaces.BaseUiListener;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.view.MyLv;
import com.example.heshequ.view.XialaPop;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GoodDetailActivity extends NetWorkActivity implements View.OnClickListener, XRecyclerView.LoadingListener, BottomShareFragment.DoClickListener, WbShareCallback, Serializable {
    private int type = 0;
    private CircleView ivHead;
    private EditText etContent;
    private ImageView ivBq;
    private ImageView ivSend;
    private TextView tvName;
    private TextView tvTime;
    //这里并没有用上
    private TextView tvTitles;
    private TextView tvBelong;
    private TextView tvLoves;
    private TextView tvNum, tvContent;
    private MyLv lvPicture;
    private XRecyclerView lvDisscuss;
    private PictureAdapter pictureAdapter;
    private GoodDisscussAdapter goodDisscussAdapter;
    private ImageView ivRight;  // ivRight是商品详情页面右上角”操作“按钮
    private SecondhandgoodBean secondhandgoodBean;
    private LinearLayout llSave;
    private ImageView ivImg;
    private int dz;
    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;
    private List<GoodsdiscussBean> newList, moreList;
    private TextView tvTip;
    private int clickPosition;
    private String save = "收藏";
    private int resultPosition;
    private RefreshBrodcast brodcast;
    private AlertDialog deldialog;
    private BottomShareFragment shareFragment;
    private boolean isQQShare;
    private boolean isWBShare;
    private WbShareHandler wbShareHandler;

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (ResultUtils.isFail(result, this)) {
            return;
        }
        if (where == 1000) {
            Utils.toastShort(mContext, result.getString("msg") + "");

            int zan = secondhandgoodBean.likeAmount;
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            if (TextUtils.isEmpty(secondhandgoodBean.userLike)) {
                secondhandgoodBean.userLike = "1";
                zan = zan + 1;
                secondhandgoodBean.likeAmount = zan;
                ivImg.setImageResource(R.mipmap.saved);
                tvLoves.setTextColor(Color.parseColor("#00bbff"));
            } else {
                secondhandgoodBean.userLike = "";
                zan = zan - 1;
                secondhandgoodBean.likeAmount = zan;
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
            goodDisscussAdapter.setData(newList);
        } else if (where == 101) {
            lvDisscuss.loadMoreComplete();
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
            goodDisscussAdapter.setData(newList);
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
            goodDisscussAdapter.setData(newList);
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
                secondhandgoodBean = gson.fromJson(result.optString("data"), SecondhandgoodBean.class);
                dz = secondhandgoodBean.likeAmount;
                setListener();
                init();
                getDisscuss(100);
            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        } else if (where == 10010) {
            Utils.toastShort(mContext, "删除 " + result.toString());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetail);
        EventBus.getDefault().register(this);
        secondhandgoodBean = new SecondhandgoodBean();
        secondhandgoodBean = (SecondhandgoodBean) getIntent().getSerializableExtra("Secondhandgood");
        sendClickGoods();
        dz = secondhandgoodBean.likeAmount;
        setListener();
        init();
        getDisscuss(100);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        setTitleAndBack("商品详情");
        View headview = getLayoutInflater().inflate(R.layout.head_goodsdetail, null);
        tvTip = (TextView) headview.findViewById(R.id.tvTips);
        ivHead = (CircleView) headview.findViewById(R.id.ivHead);
        tvName = (TextView) headview.findViewById(R.id.tvName);
        tvTime = (TextView) headview.findViewById(R.id.tvTime);
        tvTitles = (TextView) headview.findViewById(R.id.tvTitles);
        tvBelong = (TextView) headview.findViewById(R.id.tvBelong);
        tvLoves = (TextView) headview.findViewById(R.id.tvLoves);
        tvNum = (TextView) headview.findViewById(R.id.tvNum);
        tvContent = (TextView) headview.findViewById(R.id.tvContent);
        etContent = (EditText) findViewById(R.id.etContent);
        llSave = (LinearLayout) headview.findViewById(R.id.llSave);
        llSave.setOnClickListener(this);
        ivImg = (ImageView) headview.findViewById(R.id.ivImg);
        ivBq = (ImageView) findViewById(R.id.ivBq);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setImageResource(R.mipmap.more3);
        ivRight.setOnClickListener(this);
        ivBq.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        ivHead.setOnClickListener(this);
        lvPicture = (MyLv) headview.findViewById(R.id.lvPicture);
        pictureAdapter = new PictureAdapter(this);
        lvPicture.setAdapter(pictureAdapter);
        lvDisscuss = (XRecyclerView) findViewById(R.id.lvDisscuss);
        ConsTants.initXrecycleView(this, true, true, lvDisscuss);
        goodDisscussAdapter = new GoodDisscussAdapter(this);
        lvDisscuss.setAdapter(goodDisscussAdapter);
        lvDisscuss.addHeaderView(headview);
        lvDisscuss.setLoadingListener(this);
        if (secondhandgoodBean != null) {
            if (secondhandgoodBean.anonymity == 0) {
                tvName.setText(secondhandgoodBean.nn == null ? "" : secondhandgoodBean.nn);
                if (TextUtils.isEmpty(secondhandgoodBean.header)) {
                    ivHead.setImageResource(R.mipmap.head3);
                } else {
                    Glide.with(context).load(Constants.base_url + secondhandgoodBean.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                }
            } else {
                tvName.setText("匿名用户");
                ivHead.setImageResource(R.mipmap.head3);
            }
            // tvTitles.setText(secondhandgoodBean.title + "");
            tvContent.setText(secondhandgoodBean.content + "");
            tvTime.setText(secondhandgoodBean.price + "");
            tvLoves.setText(secondhandgoodBean.likeAmount + "");
            tvNum.setText(secondhandgoodBean.commentAmount + "");
            if (secondhandgoodBean.labels != null && secondhandgoodBean.labels.size() > 0) {
                String str = "";
                for (int i = 0; i < secondhandgoodBean.labels.size(); i++) {
                    if (i == 0) {
                        str = "#" + secondhandgoodBean.labels.get(i).name + "#";
                    } else {
                        str = str + "   #" + secondhandgoodBean.labels.get(i).name + "#";
                    }
                }
                tvBelong.setText(str);
            } else {
                tvBelong.setText("");
            }
            if (TextUtils.isEmpty(secondhandgoodBean.userLike)) {
                ivImg.setImageResource(R.mipmap.sc);
                tvLoves.setTextColor(Color.parseColor("#ababb3"));
            } else {
                ivImg.setImageResource(R.mipmap.saved);
                tvLoves.setTextColor(Color.parseColor("#00bbff"));
            }
            if (TextUtils.isEmpty(secondhandgoodBean.isCollect)) {
                save = "收藏";
            } else {
                save = "取消收藏";
            }
            if (secondhandgoodBean.photos != null && secondhandgoodBean.photos.size() > 0) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < secondhandgoodBean.photos.size(); i++) {
                    list.add(WenConstans.BaseUrl + secondhandgoodBean.photos.get(i).photoId);
                }
                lvPicture.setVisibility(View.VISIBLE);
                pictureAdapter.setData(list);
            } else {
                lvPicture.setVisibility(View.GONE);
            }
        }

        initDialog();

        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();

    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("确定要删除这条商品吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setBodyParams(new String[]{"uid", "goodsid"}, new String[]{secondhandgoodBean.uid, secondhandgoodBean.id + ""});
                sendPost(WenConstans.SecondhandGoodDele, 10010, WenConstans.token);
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

    /**
     * 商品详情页面的信息不是通过调用goodsInfo接口获得的
     * 而是通过调用二手商品首页pglist接口中取出来传到商品详情页面的
     * 但是需要调用一次goodsInfo接口，给后台的“推荐”接口提供数据，获取用户推荐
     */
    private void sendClickGoods() {
        setBodyParams(new String[]{"goodsid"}, new String[]{secondhandgoodBean.id + ""});
        sendPost(WenConstans.SecondhandGoodsInfo, 123, WenConstans.token);
    }

    private void getDisscuss(int where) {
        if (where == 100) {
            pn = 1;
        }
        setBodyParams(new String[]{"goodsid", "pn", "ps"}
                , new String[]{secondhandgoodBean.id + "", pn + "", ps + ""});
        sendPost(WenConstans.SecondgooddiscussFirst, where, WenConstans.token);

    }

    private void sendDisscuss(String content) {
        setBodyParams(new String[]{"id", "type", "content"}
                , new String[]{secondhandgoodBean.id + "", 1 + "", content});
        sendPost(WenConstans.Secondgooddiscuss, 102, WenConstans.token);

    }

    private void saveWw(String type) {
        setBodyParams(new String[]{"id", "op"}
                , new String[]{secondhandgoodBean.id + "", type});
        sendPost(WenConstans.Secondgoodcollect, 104, WenConstans.token);

    }

    private void jbWw() {
        startActivity(new Intent(this, ReportActivity.class).putExtra("type", 1).putExtra("id", secondhandgoodBean.id));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBq:
                break;
            case R.id.ivHead:
                if (secondhandgoodBean.anonymity == 0) {
                    startActivity(new Intent(this, PersonalInformationActivity.class).putExtra("uid", Integer.parseInt(secondhandgoodBean.uid)));
                }
                break;
            case R.id.ivSend:
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
                break;
            case R.id.ivRight:
                XialaPop.showSelectPop(this, save, secondhandgoodBean.uid.equals(Constants.uid + ""), true, new XialaPop.TextListener() {
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
                            //ToastUtils.showShort("分享");
                            showShare();
                        }
                    }
                });
                break;
            case R.id.llSave:
                setBodyParams(new String[]{"goodsid"}, new String[]{secondhandgoodBean.id + ""});
                sendPost(WenConstans.Secondgoodlike, 1000, WenConstans.token);

                break;
        }

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
    public void refData(GoodsdiscussBean bean) {
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


    //可以仿照问问尝试对评论进行点赞
    public void doDing(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}
                , new String[]{newList.get(position).id + ""});
        sendPost(WenConstans.SecondgooddiscussDing, 103, WenConstans.token);
    }

    public void doSecond(int position) {
        resultPosition = position;
        Intent intent = new Intent(this, GoodsecondActivity.class);
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
        registerReceiver(brodcast, intentFilter);
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
                GoodsdiscussBean beans = (GoodsdiscussBean) intent.getSerializableExtra("beans");
                String resultSave = intent.getStringExtra("save");
                if (beans != null) {
                    newList.get(resultPosition).likeAmount = beans.likeAmount;
                    newList.get(resultPosition).commentAmount = beans.commentAmount;
                    newList.get(resultPosition).isTop = beans.isTop;
                    goodDisscussAdapter.setData(newList);
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
        if (shareBean.getName().equals("微信好友")) {
            if (!Utils.isWeChatAppInstalled(this)) {
                Utils.toastShort(mContext, "您还未安装微信客户端");
                return;
            }
            Utils.SendWeiXinShare(SendMessageToWX.Req.WXSceneSession, Constants.base_url + secondhandgoodBean.header, Constants.base_url + "AskInfo.html?id=" + secondhandgoodBean.id, secondhandgoodBean.content, secondhandgoodBean.content);
        } else if (shareBean.getName().equals("朋友圈")) {
            if (!Utils.isWeChatAppInstalled(this)) {
                Utils.toastShort(mContext, "您还未安装微信客户端");
                return;
            }
            Utils.SendWeiXinShare(SendMessageToWX.Req.WXSceneTimeline, Constants.base_url + secondhandgoodBean.header, Constants.base_url + "AskInfo.html?id=" + secondhandgoodBean.id, secondhandgoodBean.content, secondhandgoodBean.content);
        } else if (shareBean.getName().equals("微博")) {
            if (!Utils.isWeiboInstalled(this)) {
                Utils.toastShort(mContext, "您还未安新浪微博客户端");
                return;
            }
            isWBShare = true;
            Utils.shareToWeibo(wbShareHandler, Constants.base_url + secondhandgoodBean.header, secondhandgoodBean.content, secondhandgoodBean.content + Constants.base_url + "AskInfo.html?id=" + secondhandgoodBean.id);
            //ssoHandler.authorize(new SelfWbAuthListener(this));
        } else if (shareBean.getName().equals("QQ")) {
            if (!Utils.isQQClientInstalled(this)) {
                Utils.toastShort(mContext, "您还未安装QQ客户端");
                return;
            }
            isQQShare = true;
            Utils.sendQQShare(this, Constants.base_url + secondhandgoodBean.header, Constants.base_url + "AskInfo.html?id=" + secondhandgoodBean.id, secondhandgoodBean.content, secondhandgoodBean.content);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onWbShareSuccess() {
        Utils.toastShort(mContext, "微博分享成功");
    }

    @Override
    public void onWbShareCancel() {
        Utils.toastShort(mContext, "取消了微博分享");
    }

    @Override
    public void onWbShareFail() {
        Utils.toastShort(mContext, "微博分享失败");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent) {
            if (isQQShare) {
                mTencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener(this));
                isQQShare = false;
            }
        }
        if (isWBShare) {
            wbShareHandler.doResultIntent(data, this);
            isWBShare = false;
        }
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
