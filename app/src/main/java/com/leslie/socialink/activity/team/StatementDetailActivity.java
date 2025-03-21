package com.leslie.socialink.activity.team;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.Adapter_GridView;
import com.leslie.socialink.adapter.GvEmojiAdapter;
import com.leslie.socialink.adapter.recycleview.CommentAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.constans.P;
import com.leslie.socialink.entity.CommentBean;
import com.leslie.socialink.entity.RefStatementEvent;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.utils.SharedPreferencesHelp;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.CircleView;
import com.leslie.socialink.view.MyGv;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class StatementDetailActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private int type = 0;
    private CircleView ivHead;
    private ImageView ivBack;
    private TextView tvTitle, tvName, tvContent, tvDel, tvDate, tvZan, tvPl, llRecommend_tv, tvClubName, tvLikes;
    private TeamBean bean;
    private int speakId;
    private View headView;
    private LinearLayout llRecommend, llLikes, llComment;
    private ArrayList<TeamBean.SpeakBean.LikesBean> likesBeans;
    private int itsaidFlag;
    private MyGv gv;
    private ArrayList<String> imgs;
    private XRecyclerView rv;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentBean> data;
    private EditText etComment;
    private String comment;
    private ImageView ivBq, ivSend;
    private FrameLayout flEmoji;
    private boolean isEmojiShow = false;
    private GridView gvEmoji;
    private GvEmojiAdapter gvEmojiAdapter;
    private int commentAmount;
    private int pn;
    private int allPn;
    private int allps;
    private Gson gson;
    private AlertDialog delSpeakdialog;
    private boolean isZan;
    private int likeAmount;
    private final int initData = 1000;
    //private final int refreshData = 1001;
    private final int lodData = 1002;
    private final int delSpeak = 1001;
    private final int itsaid = 1003;
    private final int like = 1004;
    private final int sendComment = 1005;
    private final int delComment = 1006;
    private final int getbean = 1007;
    private final int getheadCode = 1008;
    private final int ISCOMMENTABLE = 1009;
    private AlertDialog deldialog;
    private int delid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_detail);
        init();
        event();
        //监听软键盘高度px
        Utils.addOnSoftKeyBoardVisibleListener(this);
        isCommentAble();
    }

    private void init() {
        gson = new Gson();
        headView = LayoutInflater.from(mContext).inflate(R.layout.statement_detail_head, null);
        ivHead = (CircleView) headView.findViewById(R.id.ivHead);
        tvClubName = headView.findViewById(R.id.tvClubName);
        tvName = (TextView) headView.findViewById(R.id.tvName);
        tvContent = (TextView) headView.findViewById(R.id.tvContent);
        tvDel = (TextView) headView.findViewById(R.id.tvDel);
        tvDate = (TextView) headView.findViewById(R.id.tvDate);
        tvZan = (TextView) headView.findViewById(R.id.tvZan);
        tvPl = (TextView) headView.findViewById(R.id.tvPl);
        llRecommend = (LinearLayout) headView.findViewById(R.id.llRecommend);
        llRecommend_tv = (TextView) headView.findViewById(R.id.llRecommend_tv);
        tvLikes = (TextView) headView.findViewById(R.id.tvLikes);
        llLikes = (LinearLayout) headView.findViewById(R.id.llLikes);
        gv = (MyGv) headView.findViewById(R.id.gv);
        rv = (XRecyclerView) findViewById(R.id.rv);
        ivBq = (ImageView) findViewById(R.id.ivBq);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        etComment = (EditText) findViewById(R.id.etComment);
        llComment = findViewById(R.id.llComment);
        etComment.requestFocus();
        flEmoji = (FrameLayout) findViewById(R.id.flEmoji);
        gvEmoji = (GridView) findViewById(R.id.gvEmoji);
        initDialog();
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ConsTants.initXRecycleView(context, true, true, rv);
        rv.setLoadingListener(this);
        rv.addHeaderView(headView);
        pn = 1;
        data = new ArrayList<>();
        commentAdapter = new CommentAdapter(mContext, data);
        rv.setAdapter(commentAdapter);
        gvEmojiAdapter = new GvEmojiAdapter(this, Constants.emojis);
        gvEmoji.setAdapter(gvEmojiAdapter);
        type = getIntent().getIntExtra("type", 0);
        if (type == 1 || type == 3) {
            tvTitle.setText("团言详情");
        } else if (type == 2) {
            tvTitle.setText("他们说");
        }
        if (!Constants.isJoin) {
            llComment.setVisibility(View.GONE);
        }
        bean = (TeamBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            getHeadData();
        }
        getData(pn, 1);

        initDelDialog();
    }

    private void getHeadData() {
        setBodyParams(new String[]{"id"}, new String[]{"" + bean.getSpeak().getId()});
        sendPost(Constants.BASE_URL + "/api/club/speak/detail.do", getheadCode, Constants.token);
    }

    public void isCommentAble() {
        setBodyParams(new String[]{"id"}, new String[]{"" + Constants.clubId});
        sendPost(Constants.BASE_URL + "/api/club/speak/commentjudge.do", ISCOMMENTABLE, Constants.token);
    }

    private void initDelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("要删除这条回复吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除
                setBodyParams(new String[]{"id"}, new String[]{"" + delid});
                sendPost(Constants.BASE_URL + "/api/club/speak/delcomment.do", delComment, Constants.token);
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

    private void setDataToHeadUi(TeamBean.SpeakBean bean) {
        speakId = bean.getId();
        if (bean.getHeader() != null && !bean.getHeader().isEmpty()) {
            Glide.with(this).load(Constants.BASE_URL + bean.getHeader())
                    .asBitmap().error(R.mipmap.head3).into(ivHead);
        } else {
            ivHead.setImageResource(R.mipmap.head3);
        }
        if (bean.getPhotos() == null || bean.getPhotos().size() == 0) {
            gv.setVisibility(View.GONE);
        } else {
            gv.setVisibility(View.VISIBLE);
            imgs = new ArrayList<>();
            for (TeamBean.SpeakBean.PhotosBean photosBean : bean.getPhotos()) {
                imgs.add(Constants.BASE_URL + photosBean.getPhotoId());
            }
            switch (imgs.size()) {
                case 1:
                    gv.setNumColumns(1);
                    break;
                case 2:
                    gv.setNumColumns(2);
                    break;
                case 4:
                    gv.setNumColumns(2);
                    break;
                default:
                    gv.setNumColumns(3);
                    break;
            }
            gv.setAdapter(new Adapter_GridView(context, imgs));
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    intent.putStringArrayListExtra("imageList", imgs);
                    intent.putExtra(P.START_ITEM_POSITION, i);
                    intent.putExtra(P.START_IAMGE_POSITION, i);
                    intent.putExtra("isdel2", false);
                    context.startActivity(intent);
                }
            });
        }
        tvClubName.setText(bean.getClubName());
        tvName.setText(bean.getPresentorName());
        tvContent.setText(bean.getContent());
        tvDate.setText(bean.getTime());
        tvZan.setText("" + bean.getLikeAmount());
        tvPl.setText("" + bean.getCommentAmount());
        likeAmount = bean.getLikeAmount();
        commentAmount = bean.getCommentAmount();
        Drawable drawable = null;
        if (bean.getIsLike() == 0) {
            isZan = false;
            drawable = getResources().getDrawable(R.mipmap.zan);
        } else if (bean.getIsLike() == 1) {
            isZan = true;
            drawable = getResources().getDrawable(R.mipmap.zan2);
        }
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvZan.setCompoundDrawables(drawable, null, null, null);
        if (!Constants.isAdmin) {
            llRecommend.setVisibility(View.GONE);
        }
        if (bean.getPresentor() == Constants.uid || Constants.isAdmin) {
            tvDel.setVisibility(View.VISIBLE);
        }
        if (bean.getItsaidFlag() == 0) { //他们说
            itsaidFlag = 0;
            llRecommend.setBackground(ContextCompat.getDrawable(this, R.drawable.ll_bj_05bcff));
            llRecommend_tv.setText("推荐为他们说");
            llRecommend_tv.setTextColor(Color.parseColor("#05bcff"));
        } else if (bean.getItsaidFlag() == 1) {
            llRecommend.setBackground(ContextCompat.getDrawable(this, R.drawable.ll_bj_999999));
            llRecommend_tv.setText("取消设置为他们说");
            llRecommend_tv.setTextColor(Color.parseColor("#999999"));
            itsaidFlag = 1;
        }
        likesBeans = bean.getLikes();
        if (likesBeans != null && likesBeans.size() > 0) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < likesBeans.size(); i++) {
                if (i == 0) {
                    s = new StringBuilder(likesBeans.get(i).getPresentorName());
                } else {
                    s.append("、").append(likesBeans.get(i).getPresentorName());
                }
            }
            tvLikes.setText(s.toString());
        } else {
            llLikes.setVisibility(View.GONE);
        }
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("要删除这条团言吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e("DDQ", "shanchu  ");
                //删除
                setBodyParams(new String[]{"speakId"}, new String[]{"" + speakId});
                sendPost(Constants.BASE_URL + "/api/club/speak/delete.do", delSpeak, Constants.token);
                delSpeakdialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delSpeakdialog.dismiss();
            }
        });
        delSpeakdialog = builder.create();
        delSpeakdialog.setCancelable(false);
    }

    private void getData(int pn, int type) {
        // type = 1 初始化/刷新  2 加载
        if (type == 1) {
            setBodyParams(new String[]{"speakId", "pn", "ps"}, new String[]{"" + bean.getSpeak().getId(), "" + pn, "" + Constants.DEFAULT_PS});
            sendPost(Constants.BASE_URL + "/api/club/speak/pgcomment.do", initData, Constants.token);
        } else if (type == 2) {
            setBodyParams(new String[]{"speakId", "pn", "ps"}, new String[]{"" + bean.getSpeak().getId(), "" + pn, "" + Constants.DEFAULT_PS});
            sendPost(Constants.BASE_URL + "/api/club/speak/pgcomment.do", lodData, Constants.token);
        }
    }

    private void event() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
        ivBq.setOnClickListener(v -> {
            Utils.hideSoftInput(this);
            if (Utils.isKeyBoarVisiableForLast) {
                //锁住RV高度
                lockContentHeight();
            }
            //显示表情布局
            LinearLayout.LayoutParams Params = (LinearLayout.LayoutParams) flEmoji.getLayoutParams();
            Params.height = SharedPreferencesHelp.getInt("keyboardHeight", 450);
            flEmoji.setLayoutParams(Params);
            flEmoji.setVisibility(View.VISIBLE);
            isEmojiShow = true;
        });
        ivSend.setOnClickListener(v -> {
            //Utils.toastShort(this, "发送评论");
            comment = etComment.getText().toString().trim();
            if (comment.isEmpty()) {
                Utils.toastShort(this, "评论内容不能为空");
                return;
            }
            ivSend.setClickable(false);
            setBodyParams(new String[]{"speakId", "content"}, new String[]{"" + speakId, "" + comment});
            sendPost(Constants.BASE_URL + "/api/club/speak/comment.do", sendComment, Constants.token);
        });
        etComment.setOnClickListener(v -> {
            if (isEmojiShow) {
                //unlockContentHeightDelayed();
                flEmoji.setVisibility(View.GONE);
                isEmojiShow = false;
            }
            //显示键盘后再解锁
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            unlockContentHeightDelayed();
                        }
                    });
                }
            }, 100);
        });
        ivHead.setOnClickListener(v -> startActivity(new Intent(this, PersonalInformationActivity.class).putExtra("uid", bean.getSpeak().getPresentor())));
        tvDel.setOnClickListener(v -> delSpeakdialog.show());
        llRecommend.setOnClickListener(v -> {
            setBodyParams(new String[]{"speakId", "op"}, new String[]{"" + speakId, "" + Math.abs(itsaidFlag - 1)});
            sendPost(Constants.BASE_URL + "/api/club/speak/itsaid.do", itsaid, Constants.token);
        });
        tvZan.setOnClickListener(v -> {
            int op;
            if (isZan) {
                op = 2;
            } else {
                op = 1;
            }
            setBodyParams(new String[]{"speakId", "op"}, new String[]{"" + speakId, "" + op});
            sendPost(Constants.BASE_URL + "/api/club/speak/like.do", like, Constants.token);
        });
        tvPl.setOnClickListener(v -> {
            if (Constants.isJoin) {
                Utils.showSoftInput(etComment);
            }
        });
        //emoji 点击
        gvEmoji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etComment.setText(etComment.getText().toString().trim() +
                        Utils.getEmoji(StatementDetailActivity.this, gvEmojiAdapter.getItem(position).toString()));
                etComment.setSelection(etComment.getText().toString().trim().length());
            }
        });
        commentAdapter.setDelListener(new CommentAdapter.DelListener() {
            @Override
            public void onDelListener(int id) {
                delid = id;
                deldialog.show();
            }

            @Override
            public void onHeadClick(int uid) {
                startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", uid));
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pn = 1;
                getData(pn, 1);
                rv.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (data.size() < allps && pn < allPn) {
                    pn++;
                    getData(pn, 2);
                }
                rv.loadMoreComplete();
            }
        }, 1000);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.e("DDQ", result + "");
        switch (where) {

            case ISCOMMENTABLE:
                Log.e("DDQ", "" + Constants.clubId);
                int isJoin = 0;

                int settingSpeakComment = 1;
                JSONObject resultObject = result.optJSONObject("data");
                if (resultObject == null) {
                    break;
                }
                isJoin = resultObject.optInt("isJoin");
                settingSpeakComment = result.optJSONObject("data").optInt("settingSpeakComment");
                if (isJoin == 0 && settingSpeakComment == 1) {
                    llComment.setVisibility(View.GONE);
                } else {
                    llComment.setVisibility(View.VISIBLE);
                }
                break;

            case initData:
                switch (result.optInt("code")) {
                    case 0:
                        data = new ArrayList<>();
                        allps = result.optJSONObject("data").optInt("count");
                        allPn = result.optJSONObject("data").optInt("totalPage");
                        if (!result.optString("data").isEmpty()) {
                            data = gson.fromJson(result.optJSONObject("data").optString("list"), new TypeToken<ArrayList<CommentBean>>() {
                            }.getType());
                            if (data != null && data.size() > 0) {
                            } else {
                                data = new ArrayList<>();
                            }
                        }
                        commentAdapter.setData(data);
                        break;
                    case 1:
                        Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该操作权限");
                        break;
                }
                break;
            case lodData:
                switch (result.optInt("code")) {
                    case 0:
                        allps = result.optJSONObject("data").optInt("count");
                        allPn = result.optJSONObject("data").optInt("totalPage");
                        if (!result.optString("data").isEmpty()) {
                            data = gson.fromJson(result.optJSONObject("data").optString("list"), new TypeToken<ArrayList<CommentBean>>() {
                            }.getType());
                        }
                        commentAdapter.setData2(data);
                        break;
                    case 1:
                        Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该操作权限");
                        break;
                }
                break;
            case delSpeak:
                switch (result.optInt("code")) {
                    case 0:
                        Utils.toastShort(this, result.optString("msg"));
                        EventBus.getDefault().post(new RefStatementEvent());
                        this.finish();
                        break;
                    case 1:
                        Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该操作权限");
                        break;

                }
                break;
            case itsaid:
                switch (result.optInt("code")) {
                    case 0:
                        Utils.toastShort(this, result.optString("msg"));
                        if (itsaidFlag == 0) {
                            llRecommend.setBackground(ContextCompat.getDrawable(this, R.drawable.ll_bj_999999));
                            llRecommend_tv.setText("取消设置为'他们说'");
                            llRecommend_tv.setTextColor(Color.parseColor("#999999"));
                            itsaidFlag = 1;
                        } else if (itsaidFlag == 1) {
                            llRecommend.setBackground(ContextCompat.getDrawable(this, R.drawable.ll_bj_05bcff));
                            llRecommend_tv.setText("推荐为'他们说'");
                            llRecommend_tv.setTextColor(Color.parseColor("#05bcff"));
                            itsaidFlag = 0;
                        }
                        EventBus.getDefault().post(new RefStatementEvent());
                        break;
                    case 1:
                        Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该操作权限");
                        break;

                }
                break;
            case like:
                switch (result.optInt("code")) {
                    case 0:
                        String username = SharedPreferencesHelp.getString("user", "");
                        if (isZan) {
                            likeAmount = likeAmount - 1;
                            tvZan.setText("" + likeAmount);
                            if (likesBeans != null && likesBeans.size() > 0) {
                                for (int i = 0; i < likesBeans.size(); i++) {
                                    if (likesBeans.get(i).getPresentorName().contains(username)) {
                                        likesBeans.remove(i);
                                    }
                                }
                            }
                            if (likesBeans != null && likesBeans.size() > 0) {
                                StringBuilder s = new StringBuilder();
                                for (int i = 0; i < likesBeans.size(); i++) {
                                    if (i == 0) {
                                        s = new StringBuilder(likesBeans.get(i).getPresentorName());
                                    } else {
                                        s.append("、").append(likesBeans.get(i).getPresentorName());
                                    }
                                }
                                tvLikes.setText(s.toString());
                            } else {
                                tvLikes.setText("");
                                llLikes.setVisibility(View.GONE);
                            }
                        } else {
                            likeAmount = likeAmount + 1;
                            tvZan.setText("" + likeAmount);
                            if (likesBeans != null && likesBeans.size() > 0) {
                                tvLikes.setText(tvLikes.getText().toString().trim() + "、" + username);
                            } else {
                                tvLikes.setText(tvLikes.getText().toString().trim() + username);
                            }
                            llLikes.setVisibility(View.VISIBLE);
                        }
                        isZan = !isZan;
                        zanChange();
                        break;
                    case 1:
                        Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该操作权限");
                        break;
                }
                break;
            case sendComment:
                ivSend.setClickable(true);
                switch (result.optInt("code")) {
                    case 0:
                        //Utils.toastShort(this, result.optString("msg"));
                        pn = 1;
                        getData(pn, 1);
                        Utils.toastShort(this, result.optString("msg"));
                        EventBus.getDefault().post(new RefStatementEvent());
                        etComment.setText("");
                        commentAmount++;
                        tvPl.setText(commentAmount + "");
                        break;
                    case 1:
                        Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该操作权限");
                        break;
                }
                break;
            case delComment:
                switch (result.optInt("code")) {
                    case 0:
                        pn = 1;
                        getData(pn, 1);
                        Utils.toastShort(this, result.optString("msg"));
                        EventBus.getDefault().post(new RefStatementEvent());
                        break;
                    case 1:
                        Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该操作权限");
                        break;
                }
                break;
           /* case getbean:
                Log.e("DDQ",result+"");
                if (result.optInt("code") == 0){
                    TeamBean.SpeakBean speakBean = gson.fromJson(result.optString("data"),TeamBean.SpeakBean.class);
                    if (speakBean!=null) {
                        bean = new TeamBean();
                        bean.setSpeak(speakBean);
                    }else{
                        this.finish();
                        Utils.toastShort(mContext,"该团言已被删除");
                        return;
                    }
                }else{
                    Utils.toastShort(mContext,result.optString("msg"));
                }
                if (bean != null) {
                    setUi();
                }
                getData(pn, 1);
                break;*/
            case getheadCode:
                switch (result.optInt("code")) {
                    case 0:
                        //Log.e("DDQ110",result.optString("data"));  TeamBean
                        TeamBean.SpeakBean bean = gson.fromJson(result.optString("data"), TeamBean.SpeakBean.class);
                        setDataToHeadUi(bean);
                        break;
                    case 1:
                        Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该操作权限");
                        break;
                }
                break;
        }
    }

    //点赞发生变化
    private void zanChange() {
        Drawable drawable;
        if (isZan) {
            drawable = getResources().getDrawable(R.mipmap.zan2);
        } else {
            drawable = getResources().getDrawable(R.mipmap.zan);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvZan.setCompoundDrawables(drawable, null, null, null);
        EventBus.getDefault().post(new RefStatementEvent());
    }

    /**
     * 锁定RV高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rv.getLayoutParams();
        params.height = rv.getHeight();
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed() {
        ((LinearLayout.LayoutParams) rv.getLayoutParams()).weight = 1.0F;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isEmojiShow) {
                //解锁RV高度
                unlockContentHeightDelayed();
                //隐藏
                flEmoji.setVisibility(View.GONE);
                isEmojiShow = false;
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onFailure(String result, int where) {
        if (where == sendComment) {
            ivSend.setClickable(true);
        }
        Utils.toastShort(this, "网络异常");
    }


}
