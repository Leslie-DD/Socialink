package com.hnu.heshequ.activity.team;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.MeetApplication;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.statement.ReleaseActivitiesActivity;
import com.hnu.heshequ.adapter.GvEmojiAdapter;
import com.hnu.heshequ.adapter.listview.ActiviteDateilAdapter;
import com.hnu.heshequ.adapter.listview.TeamGvAdapter;
import com.hnu.heshequ.adapter.recycleview.CommentAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.ShareBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.P;
import com.hnu.heshequ.entity.CommentBean;
import com.hnu.heshequ.entity.PhotosBean;
import com.hnu.heshequ.entity.RefTDteamEvent;
import com.hnu.heshequ.entity.TeamTestBean;
import com.hnu.heshequ.fragment.BottomShareFragment;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CircleView;
import com.hnu.heshequ.view.MyGv;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityDateilActivity extends NetWorkActivity implements View.OnClickListener, XRecyclerView.LoadingListener, BottomShareFragment.DoClickListener {
    private static final String TAG = "[ActivityDateilActivity]";
    private int activityId;
    private ImageView ivBack, ivShare, ivRight;
    private TextView tvTitle;
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
    private int pn;
    private int allPn;
    private int allps;
    private Gson gson;
    private View headView;
    private CircleView ivHead;
    private TextView tvName, tvTime, tvTitle2, tvStatus, tvApplyDeadline, tvContent, tvClubName, tvBm;
    private MyGv gv, gvMember;
    private ArrayList<String> imgs;
    private ArrayList<String> memberImgs;
    private LinearLayout ll_teamMembers, llComment, llBm;
    private TeamTestBean.ObjBean bean;
    //pop
    private PopupWindow pop;
    private WindowManager.LayoutParams layoutParams;
    private LinearLayout ll_editor, ll_del, ll_cacel;
    private BottomShareFragment shareFragment;
    private boolean isQQShare;
    private AlertDialog deldialog;
    private int delid;
    private final int initData = 1000;
    //private final int refData = 1001;
    private final int lodData = 1002;
    private final int sendComment = 1005;
    private final int delComment = 1006;
    private final int getheadCode = 1007;
    private final int DelCode = 1008;
    private final int ApplyCode = 1009;

    private boolean isTeamOwner = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateil);
        EventBus.getDefault().register(this);
        init();
        initPop();
        event();
        //监听软键盘高度px
        Utils.addOnSoftKeyBoardVisibleListener(this);
    }

    private void init() {
        activityId = getIntent().getIntExtra("id", 0);
        gson = new Gson();
        llComment = findViewById(R.id.llComment);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        //隐藏分享
        ivShare.setVisibility(View.GONE);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("活动详情");
        rv = (XRecyclerView) findViewById(R.id.rv);
        headView = LayoutInflater.from(this).inflate(R.layout.head_activity_detail, null, false);
        //init event
        ivHead = (CircleView) headView.findViewById(R.id.ivHead);
        tvName = (TextView) headView.findViewById(R.id.tvName);
        tvTime = (TextView) headView.findViewById(R.id.tvTime);
        tvTitle2 = (TextView) headView.findViewById(R.id.tvTitle);
        tvClubName = headView.findViewById(R.id.tvClubName);
        tvStatus = (TextView) headView.findViewById(R.id.tvStatus);
        tvApplyDeadline = (TextView) headView.findViewById(R.id.tvApplyDeadline);
        tvContent = (TextView) headView.findViewById(R.id.tvContent);
        gv = (MyGv) headView.findViewById(R.id.gv);
        gvMember = (MyGv) headView.findViewById(R.id.gvMember);
        ll_teamMembers = (LinearLayout) headView.findViewById(R.id.ll_teamMembers);
        llBm = headView.findViewById(R.id.llBm);
        tvBm = headView.findViewById(R.id.tvBm);
        getHeadData();
        ConsTants.initXRecycleView(context, true, true, rv);
        rv.setLoadingListener(this);
        rv.addHeaderView(headView);
        pn = 1;
        data = new ArrayList<>();
        commentAdapter = new CommentAdapter(mContext, data);
        rv.setAdapter(commentAdapter);
        getData(pn, 1);
        ivBq = (ImageView) findViewById(R.id.ivBq);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        etComment = (EditText) findViewById(R.id.etComment);
        etComment.requestFocus();
        flEmoji = (FrameLayout) findViewById(R.id.flEmoji);
        gvEmoji = (GridView) findViewById(R.id.gvEmoji);
        gvEmojiAdapter = new GvEmojiAdapter(this, Constants.emojis);
        gvEmoji.setAdapter(gvEmojiAdapter);
        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("确定要删除这条回复吗？");
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            //删除
            setBodyParams(new String[]{"id"}, new String[]{"" + delid});
            sendPost(Constants.base_url + "/api/club/activity/delcomment.do", delComment, Constants.token);
            deldialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> deldialog.dismiss());
        deldialog = builder.create();
        deldialog.setCancelable(false);
    }

    private void getHeadData() {
        setBodyParams(new String[]{"id"}, new String[]{"" + activityId});
        sendPost(Constants.base_url + "/api/club/activity/detail.do", getheadCode, Constants.token);
    }

    private void getData(int pn, int type) {
        // type = 1 初始化/刷新  2 加载
        if (type == 1) {
            setBodyParams(new String[]{"id", "pn", "ps"}, new String[]{"" + activityId, "" + pn, "" + Constants.default_PS});
            sendPost(Constants.base_url + "/api/club/activity/pgcomment.do", initData, Constants.token);
        } else if (type == 2) {
            setBodyParams(new String[]{"id", "pn", "ps"}, new String[]{"" + activityId, "" + pn, "" + Constants.default_PS});
            sendPost(Constants.base_url + "/api/club/activity/pgcomment.do", lodData, Constants.token);
        }

    }

    private void setDataToHeadUi(TeamTestBean.ObjBean bean) {
        if (bean != null) {
            Glide.with(this).load(Constants.base_url + bean.getHeader()).asBitmap().error(R.mipmap.head3).into(ivHead);
            tvClubName.setText(bean.getClubInfo().getName());
            tvName.setText(bean.getPresentorName());
            tvTime.setText(bean.getTime());
            tvTitle2.setText(bean.getTitle());
            tvApplyDeadline.setText("截止时间：" + bean.getApplyDeadline());
            tvContent.setText(bean.getContent());

            isTeamOwner = bean.getClubInfo() != null && bean.getClubInfo().isAdmin();

            try {
                if (Utils.isPastDue(bean.getApplyDeadline(), "yyyy-MM-dd HH:mm")) {
                    if (bean.getIsLike() == 1) {
                        tvBm.setText("已报名");
                        tvBm.setBackground(ContextCompat.getDrawable(this, R.drawable.tv_bg_e6e6e6_6));
                        tvBm.setEnabled(false);
                    } else {
                        tvBm.setText("我要报名");
                    }
                } else {
                    llBm.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (bean.getPhotos() == null || bean.getPhotos().size() == 0) {
                gv.setVisibility(View.GONE);
            } else {
                gv.setVisibility(View.VISIBLE);
                imgs = new ArrayList<>();
                for (PhotosBean photosBean : bean.getPhotos()) {
                    imgs.add(Constants.base_url + photosBean.getPhotoId());
                }

                gv.setNumColumns(1);
                gv.setAdapter(new ActiviteDateilAdapter(context, imgs));
                gv.setOnItemClickListener((adapterView, view, i, l) -> {
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    intent.putStringArrayListExtra("imageList", imgs);
                    intent.putExtra(P.START_ITEM_POSITION, i);
                    intent.putExtra(P.START_IAMGE_POSITION, i);
                    intent.putExtra("isdel2", false);
                    context.startActivity(intent);
                });
            }
            try {
                if (Utils.isPastDue(bean.getApplyDeadline(), "yyyy-MM-dd HH:mm")) {
                    tvStatus.setText("进行中");
                    tvStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.tv_bg_7bd8fa_6));
                } else {
                    tvStatus.setText("已结束");
                    tvStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.tv_bg_e6e6e6_6));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (bean.getLikes() == null || bean.getLikes().size() == 0) {
                ll_teamMembers.setVisibility(View.GONE);
            } else {
                ll_teamMembers.setVisibility(View.VISIBLE);
                memberImgs = new ArrayList<>();
                if (bean.getLikes().size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        memberImgs.add(Constants.base_url + bean.getLikes().get(i).getHeader());
                    }
                } else {
                    for (TeamTestBean.ObjBean.LikesBean bean1 : bean.getLikes()) {
                        memberImgs.add(Constants.base_url + bean1.getHeader());
                    }
                }
                if (memberImgs.size() == 1) {
                    memberImgs.add(0, "1");
                    memberImgs.add(0, "1");
                } else if (memberImgs.size() == 2) {
                    memberImgs.add(0, "1");
                }
                gvMember.setNumColumns(3);
                TeamGvAdapter teamAdapter = new TeamGvAdapter(mContext);
                teamAdapter.setData(memberImgs);
                gvMember.setAdapter(teamAdapter);
            }

            if (bean.getClubInfo() == null) {
                Constants.isJoin = bean.getIsLike() == 1;
            } else {
                Constants.isJoin = bean.getClubInfo().getIsJoin() == 1;
            }
            if (!Constants.isJoin) {
                llComment.setVisibility(View.GONE);
                ivRight.setVisibility(View.GONE);
            } else {
                if (bean.getPresentor() != Constants.uid) {
                    ivRight.setVisibility(View.GONE);
                }
            }

            ll_editor.setVisibility(bean.getPresentor() == Constants.uid ? View.VISIBLE : View.GONE);
            ll_del.setVisibility(bean.getPresentor() == Constants.uid ? View.VISIBLE : View.GONE);
        }
    }

    private void initPop() {
        //设置pop
        pop = new PopupWindow(260, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View spv = LayoutInflater.from(mContext).inflate(R.layout.pop_bullertin_detaol, null);
        //init event
        ll_editor = (LinearLayout) spv.findViewById(R.id.ll_editor);
        ll_del = (LinearLayout) spv.findViewById(R.id.ll_del);
        ll_cacel = (LinearLayout) spv.findViewById(R.id.ll_cacel);
        ll_editor.setOnClickListener(this);
        ll_del.setOnClickListener(this);
        ll_cacel.setOnClickListener(this);

        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        pop.setOutsideTouchable(true);
        // 设置焦点
        pop.setFocusable(true);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
            }
        });
        // 设置所在布局
        pop.setContentView(spv);
    }

    public void showSpvPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        pop.showAsDropDown(ivRight, Gravity.RIGHT, 0, 0);

    }

    public void showShare() {
        if (shareFragment == null) {
            shareFragment = new BottomShareFragment();
            shareFragment.setDoClickListener(this);
        }
        shareFragment.show(getSupportFragmentManager(), "Dialog");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onheadRef(RefTDteamEvent event) {
        getHeadData();
    }

    private void event() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        ll_teamMembers.setOnClickListener(this);
        ivBq.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        etComment.setOnClickListener(this);
        tvBm.setOnClickListener(this);
        //emoji 点击
        gvEmoji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etComment.setText(etComment.getText().toString().trim() +
                        Utils.getEmoji(ActivityDateilActivity.this, gvEmojiAdapter.getItem(position).toString()));
                etComment.setSelection(etComment.getText().toString().trim().length());
            }
        });

        commentAdapter.setDelListener(new CommentAdapter.DelListener() {
            @Override
            public void onDelListener(int id) {
                //Utils.toastShort(StatementDetailActivity.this,"点击了评论的ID为 ："+id);
                delid = id;
                deldialog.show();
            }

            @Override
            public void onHeadClick(int uid) {
                startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", uid));
            }
        });

        gvMember.setOnItemClickListener((parent, view, position, id) -> {
            //查看参与人员列表
            startActivity(new Intent(ActivityDateilActivity.this,
                    AppliedMemberActivity.class)
                    .putExtra("id", bean.getId())
                    .putExtra("isTeamOwner", isTeamOwner));
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvPl:
                Utils.showSoftInput(etComment);
                break;
            case R.id.etComment:
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
                break;
            case R.id.ivBq:
                if (Utils.isKeyBoarVisiableForLast) {
                    Utils.hideSoftInput(this);
                    //锁住RV高度
                    lockContentHeight();
                }
                //显示表情布局
                LinearLayout.LayoutParams Params = (LinearLayout.LayoutParams) flEmoji.getLayoutParams();
                Params.height = MeetApplication.getInstance().getSharedPreferences().getInt("keyboardHeight", 450);
                flEmoji.setLayoutParams(Params);
                flEmoji.setVisibility(View.VISIBLE);
                isEmojiShow = true;
                break;
            case R.id.ivSend:
                //Utils.toastShort(this, "发送评论");
                comment = etComment.getText().toString().trim();
                if (comment.isEmpty()) {
                    Utils.toastShort(this, "评论内容不能为空");
                    return;
                }
                setBodyParams(new String[]{"activityId", "content"}, new String[]{"" + activityId, "" + comment});
                sendPost(Constants.base_url + "/api/club/activity/comment.do", sendComment, Constants.token);
                break;
            case R.id.ll_teamMembers:
                //查看参与人员列表
                startActivity(new Intent(this, AppliedMemberActivity.class).putExtra("id", bean.getId())
                        .putExtra("isTeamOwner", isTeamOwner));
                break;
            case R.id.ivShare:
                //分享
                showShare();
                break;
            case R.id.ivRight:
                //更多
                showSpvPop();
                break;
            case R.id.ll_editor:
                //编辑
                startActivity(new Intent(mContext, ReleaseActivitiesActivity.class).putExtra("type", 2).putExtra("bean", bean));
                pop.dismiss();
                break;
            case R.id.ll_del:
                //删除
                setBodyParams(new String[]{"id"}, new String[]{"" + bean.getId()});
                sendPost(Constants.base_url + "/api/club/activity/delete.do", DelCode, Constants.token);
                break;
            case R.id.ll_cacel:
                pop.dismiss();
                break;
            case R.id.tvBm:
                setBodyParams(new String[]{"id", "op"}, new String[]{"" + bean.getId(), "" + 1});
                sendPost(Constants.base_url + "/api/club/activity/apply.do", ApplyCode, Constants.token);
                break;

        }
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
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pn = 1;
                getData(pn, 1);
                getHeadData();
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

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.i(TAG, "onSuccess where: " + where + ", " + result.toString());
        switch (where) {
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
            case sendComment:
                switch (result.optInt("code")) {
                    case 0:
                        pn = 1;
                        getData(pn, 1);
                        Utils.toastShort(this, result.optString("msg"));
                        //EventBus.getDefault().post(new RefStatementEvent());
                        etComment.setText("");
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
                        //EventBus.getDefault().post(new RefStatementEvent());
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
            case getheadCode:
                switch (result.optInt("code")) {
                    case 0:
                        bean = gson.fromJson(result.optString("data"), TeamTestBean.ObjBean.class);
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
            case DelCode:
                if (result.optInt("code") == 0) {
                    EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 2}, true));
                    Utils.toastShort(mContext, "删除成功");
                    this.finish();
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case ApplyCode:
                if (result.optInt("code") == 0) {
                    tvBm.setText("已报名");
                    tvBm.setBackground(ContextCompat.getDrawable(this, R.drawable.tv_bg_e6e6e6_6));
                    tvBm.setEnabled(false);
                    Utils.toastShort(mContext, "报名成功");
                    getHeadData();
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(this, "网络异常");
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
