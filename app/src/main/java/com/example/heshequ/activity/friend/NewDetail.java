package com.example.heshequ.activity.friend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.activity.login.LoginActivity;
import com.example.heshequ.activity.team.PersonalInformationActivity;
import com.example.heshequ.adapter.listview.FriendPictureAdapter;
import com.example.heshequ.adapter.listview.NewDisscussAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.DynamicComment;
import com.example.heshequ.bean.FriendNewBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.ResultUtils;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.fragment.BottomShareFragment;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.view.MyLv;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2020/4/24.
 * 动态详情页面
 */

public class NewDetail extends NetWorkActivity implements View.OnClickListener {
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
    private FriendPictureAdapter pictureAdapter;

    private ImageView ivRight;

    private LinearLayout llSave;
    private ImageView ivImg;
    private int dz;
    private int pn = 1;
    private int ps = 20;
    private boolean hasRefresh;
    private int totalPage = 1;

    private TextView tvTip;
    private int clickPosition;

    private List<DynamicComment> newList, moreList;
    private FriendNewBean friendNewBean;
    private BottomShareFragment shareFragment;
    private boolean isQQShare;
    private boolean isWBShare;
    private NewDisscussAdapter newDisscussAdapter;

    private String save = "收藏";
    private int resultPosition;
    private RefreshBrodcast brodcast;
    private AlertDialog deldialog;
    private int uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_newdetail);
        //EventBus.getDefault().register(this);
        uid = WenConstans.id;
        String ids = uid + "";
        if (ids.equals("0")) {
            Intent intents = new Intent(NewDetail.this, LoginActivity.class);

            startActivity(intents);
            Toast.makeText(this, "我们需要验证您的身份", Toast.LENGTH_LONG).show();
        }
        friendNewBean = new FriendNewBean();
        friendNewBean = (FriendNewBean) getIntent().getSerializableExtra("FriendNew");
        setListener();
        init();
        event();
        getDisscuss(100);
    }

    private void init() {
        setTitleAndBack("动态详情");
        View headview = getLayoutInflater().inflate(R.layout.head_newdetail, null);
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
        pictureAdapter = new FriendPictureAdapter(this);
        lvPicture.setAdapter(pictureAdapter);
        lvDisscuss = (XRecyclerView) findViewById(R.id.lvDisscuss);
        ConsTants.initXrecycleView(this, true, true, lvDisscuss);
        newDisscussAdapter = new NewDisscussAdapter(this);
        lvDisscuss.setAdapter(newDisscussAdapter);
        lvDisscuss.addHeaderView(headview);
        //lvDisscuss.setLoadingListener(this);
        if (friendNewBean != null) {

            tvName.setText(friendNewBean.name == null ? "" : friendNewBean.name);
            if (TextUtils.isEmpty(friendNewBean.headImg)) {
                ivHead.setImageResource(R.mipmap.head3);
            } else {
                Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId=" + friendNewBean.headImg).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("showheadimg", "" + Constants.base_url + "/info/file/pub.do?fileId=" + friendNewBean.headImg);
            }

            // tvTitles.setText(secondhandgoodBean.title + "");
            tvContent.setText(friendNewBean.content + "");
            tvTime.setText(friendNewBean.date + "");
            tvLoves.setText(friendNewBean.likeamount + "");
            // tvNum.setText( "");

            if (!friendNewBean.islike) {
                ivImg.setImageResource(R.mipmap.sc);
                tvLoves.setTextColor(Color.parseColor("#ababb3"));
            } else {
                ivImg.setImageResource(R.mipmap.saved);
                tvLoves.setTextColor(Color.parseColor("#00bbff"));
            }

            if (friendNewBean.photos != null && friendNewBean.photos.size() > 0) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < friendNewBean.photos.size(); i++) {
                    // list.add(WenConstans.BaseUrl + friendNewBean.photos.get(i).photoId);
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
        builder.setMessage("确定要删除这条动态吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除接口还没开始写
                setBodyParams(new String[]{"dynamic_id"}, new String[]{friendNewBean.dtid + ""});
                sendPost(Constants.base_url + "/api/social/deleteDynamic.do", 10010, WenConstans.token);
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

    private void event() {

    }

    private void getDisscuss(int where) {
        if (where == 100) {
            pn = 1;
        }
        setBodyParams(new String[]{"dynamic_id"}
                , new String[]{friendNewBean.dtid + ""});
        sendPost(WenConstans.GetSingleDynamic, where, WenConstans.token);

    }

    private class RefreshBrodcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                DynamicComment beans = (DynamicComment) intent.getSerializableExtra("beans");
                String resultSave = intent.getStringExtra("save");
                if (beans != null) {

                    newDisscussAdapter.setData(newList);
                }
                if (!TextUtils.isEmpty(resultSave)) {
                    save = resultSave;
                }
            }
        }
    }

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

            int zan = friendNewBean.likeamount;
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(zan + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            if (!friendNewBean.islike) {
                friendNewBean.islike = true;
                zan = zan + 1;
                friendNewBean.likeamount = zan;
                ivImg.setImageResource(R.mipmap.saved);
                tvLoves.setTextColor(Color.parseColor("#00bbff"));
            } else {
                friendNewBean.islike = false;
                zan = zan - 1;
                friendNewBean.likeamount = zan;
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
                Log.e("pinglingliebiao", "" + obj.toString());
                totalPage = obj.getInt("totalPage");
                if (obj != null && obj.has("dynamicCommentList")) {
                    newList = gson.fromJson(obj.getJSONArray("dynamicCommentList").toString(),
                            new TypeToken<List<DynamicComment>>() {
                            }.getType());
                    Log.e("newlist", newList + "");
                    if (newList == null) {
                        newList = new ArrayList<>();
                        Log.e("newlist", newList + "");
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
            newDisscussAdapter.setData(newList);
        } else if (where == 101) {
            lvDisscuss.loadMoreComplete();
            Gson gson = new Gson();
            if (result.has("data")) {
                JSONObject obj = result.getJSONObject("data");
                if (obj != null && obj.has("dynamicCommentList")) {
                    moreList = gson.fromJson(obj.getJSONArray("dynamicCommentList").toString(),
                            new TypeToken<List<DynamicComment>>() {
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
            newDisscussAdapter.setData(newList);
        } else if (where == 102) {
            etContent.setText("");
            Utils.toastShort(mContext, "评论成功");
            Intent intent = new Intent();
            intent.setAction("fragment.listener");
            intent.putExtra("item", 2);
            sendBroadcast(intent);
            getDisscuss(100);
            //tvNum.setText((Integer.parseInt(tvNum.getText().toString().trim()) + 1) + "");
        } else if (where == 103) {

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
                friendNewBean = gson.fromJson(result.optString("data"), FriendNewBean.class);
                dz = friendNewBean.likeamount;
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
                Utils.toastShort(mContext, "删除动态成功");
                this.finish();
            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        }
    }

    private void setListener() {
        brodcast = new RefreshBrodcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refresh.data");
        registerReceiver(brodcast, intentFilter);
    }

    private void sendDisscuss(String content) {
        setBodyParams(new String[]{"dynamic_id", "type", "content", "presentor"}
                , new String[]{friendNewBean.dtid + "", 1 + "", content, "" + friendNewBean.user_id});
        sendPost(WenConstans.FriendDiscuss, 102, WenConstans.token);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivHead:
                startActivity(new Intent(this, PersonalInformationActivity.class).putExtra("uid", friendNewBean.user_id));
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
            case R.id.llSave:
                Log.e("NewDetail.java Wen.id", "" + WenConstans.id);
                setBodyParams(new String[]{"user_id", "dynamic_id"}, new String[]{WenConstans.id + "", "" + friendNewBean.dtid});
                // 修改用Constants.uid而不用WenConstans.id，不然返回
//                setBodyParams(new String[]{"user_id","dynamic_id"}, new String[]{Constants.uid+ "",""+friendNewBean.dtid});
                sendPost(WenConstans.FriendNewZan, 1000, WenConstans.token);
        }

    }


    public void doSecond(int position) {
        resultPosition = position;
        Intent intent = new Intent(this, NewSecondActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", newList.get(position));
        intent.putExtra("save", save);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }


}
