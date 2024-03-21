package com.hnu.heshequ.activity.team;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.ReportActivity;
import com.hnu.heshequ.activity.TeamMembersActivity;
import com.hnu.heshequ.activity.statement.EditorialBulletinActivity;
import com.hnu.heshequ.activity.statement.PublishVoteActivity;
import com.hnu.heshequ.activity.statement.ReleaseActivitiesActivity;
import com.hnu.heshequ.adapter.MyFragmentPagerAdapter;
import com.hnu.heshequ.adapter.recycleview.RecycleAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.ShareBean;
import com.hnu.heshequ.bean.TeamBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.entity.RefCollect;
import com.hnu.heshequ.entity.RefTeamChild1;
import com.hnu.heshequ.entity.RefTeamDetailEvent;
import com.hnu.heshequ.entity.ShowBean;
import com.hnu.heshequ.fragment.BottomShareFragment;
import com.hnu.heshequ.fragment.HallFragment;
import com.hnu.heshequ.fragment.ManagerFragment;
import com.hnu.heshequ.fragment.StatementFragment;
import com.hnu.heshequ.fragment.TeamFragment;
import com.hnu.heshequ.utils.PhotoUtils;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CircleView;
import com.hnu.heshequ.view.CustomViewPager;
import com.hnu.heshequ.view.DragImageView;
import com.hnu.heshequ.view.MyXRecyclerView;
import com.hnu.heshequ.view.PayPasswordView;
import com.githang.statusbar.StatusBarCompat;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class TeamDetailActivity extends NetWorkActivity implements XRecyclerView.LoadingListener, BottomShareFragment.DoClickListener {
    private static final String TAG = "[TeamDetailActivity]";
    private ImageView ivAdd2, ivAdd3, ivAdd4, ivAdd5, ivAddBj;
    private boolean LockMenu = false;
    private DragImageView ivAdd;
    private View headView;
    private TextView tvTitle, tvHall, tvStatement, tvTeam, tvManager, tvTeamName, tvSchool;
    private TextView tvHall2, tvStatement2, tvTeam2, tvManager2;
    public LinearLayout llVis, llInvis, llHead;
    public CustomViewPager vp;
    private ArrayList<Fragment> fragmentList;
    private MyFragmentPagerAdapter pagerAdapter;
    private int status = -1;
    private ImageView ivEWM;
    private CircleView ivTeamHead, ivGroupHead;
    public int id;
    private final int getCode = 1000;
    private final int uptdfm = 1001;
    private final int collect = 1002;
    private final int notCollect = 1003;
    private final int visible = 1004;
    private final int inVisible = 1005;
    private final int join = 1006;
    private final int out = 1007;
    private final int editor = 1008;
    private TeamBean cBean;
    private TextView tvMInfo;
    private TextView tvName, tvContent;
    private HallFragment hallfragment;
    private StatementFragment statementFragment;
    private TeamFragment teamFragment;
    private ManagerFragment managerFragment;
    private PopupWindow pop;
    private TextView tvUp, tvPic;
    private WindowManager.LayoutParams layoutParams;
    private String path;
    private Uri photoUri;
    private int teamId = 0;
    private ImageView ivMore;
    private PopupWindow settingPop;
    private LinearLayout ll_share, ll_collect, ll_invisible, ll_joins, ll_editor, ll_label, ll_jb, ll_st, ll_st_o, ll_st_c, ll_st_join;
    private ImageView ivCollect, ivInvisible, ivJoin;
    private TextView tvCollect, tvInvisible, tvJoin;
    private boolean isFavorite, isVisible, isJoin;
    private PopupWindow editorPop;
    private CircleView cvHead;
    private EditText etName;
    private Button btConfirm;
    private boolean iseditor;
    private File editorFile;
    public Map<Integer, Boolean> map;
    private CircleView iv1, iv2, iv3;
    private MyXRecyclerView rv;
    private LinearLayout ll_team_num;
    private boolean hasRefresh;
    private AlertDialog deldialog;
    private BottomShareFragment shareFragment;
    private boolean isQQShare;
    private boolean isWBShare;
    private String teamName;

    public int IDENTIFY = 3;//普通团员 2-> 管理员 1-> 团长
    private String newSecret;
    //开启密码加团
    private PopupWindow secret;
    private View settingSecret;
    private WindowManager.LayoutParams secretLayoutParams;
    private Button setSecret;
    private Button setNoSecret;


    private boolean openSecret = false;
    private String teamSt = "查找密码ing ...";//团队密码

    //更改密码设置或关闭密码
    public PopupWindow changingSecret;
    private View changingSecretView;
    private WindowManager.LayoutParams changeSecretLayoutParams;
    private Button changeSecret;
    private Button closeSecret;
    private TextView teamSecret;

    public int customer = 0;//用户

    private PayPasswordView payPasswordView;//= new PayPasswordView(this);
    // payPasswordView.setActivity(this);
    private BottomSheetDialog bottomSheetDialog;

    private final int DELETEJOINSECRET = 1101;
    private final int CHECKJOINSECRET = 1102;
    private final int SETJOINSECRET = 1103;
    private final int TEAMJOINSECRET = 1104;
    private final int SEARCHJOINSECRET = 1105;
    private final int CLOSESECRET = 1106;
    private final int CHECKJOINSECRET2 = 1107;
    private final int CHECKJOINSECRET3 = 1108;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail2);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#00BBFF"));
        id = getIntent().getIntExtra("id", 0);
        Log.e(TAG, "我是获得" + id);
        init();
        initPop();
        event();
        setTvBg(0);
    }

    private void init() {
        rv = findViewById(R.id.rv);
        ConsTants.initXRecycleView(this, true, true, rv);
        rv.setLoadingListener(this);
        map = new HashMap();
        map.put(0, false);
        map.put(1, false);
        map.put(2, false);
        map.put(3, false);
        EventBus.getDefault().register(this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("团队详情");
        ivMore = (ImageView) findViewById(R.id.ivMore);
        llInvis = (LinearLayout) findViewById(R.id.llInVis);
        tvHall2 = (TextView) findViewById(R.id.tvHall2);
        tvStatement2 = (TextView) findViewById(R.id.tvStatement2);
        tvTeam2 = (TextView) findViewById(R.id.tvTeam2);
        tvManager2 = (TextView) findViewById(R.id.tvManager2);
        ivAdd = (DragImageView) findViewById(R.id.ivAdd);
        ivAdd2 = (ImageView) findViewById(R.id.ivAdd2);
        ivAdd3 = (ImageView) findViewById(R.id.ivAdd3);
        ivAdd4 = (ImageView) findViewById(R.id.ivAdd4);
        ivAdd5 = (ImageView) findViewById(R.id.ivAdd5);
        ivAddBj = (ImageView) findViewById(R.id.ivAddBj);
        headView = LayoutInflater.from(mContext).inflate(R.layout.team_head_view, null);
        tvSchool = headView.findViewById(R.id.tvSchool);
        ivEWM = (ImageView) headView.findViewById(R.id.ivEWM);
        ivTeamHead = (CircleView) headView.findViewById(R.id.ivTeamHead);
        ivGroupHead = (CircleView) headView.findViewById(R.id.ivGroupHead);
        tvTeamName = (TextView) headView.findViewById(R.id.tvTeamName);
        tvMInfo = (TextView) headView.findViewById(R.id.tvMInfo);
        tvName = (TextView) headView.findViewById(R.id.tvName);
        tvContent = (TextView) headView.findViewById(R.id.tvContent);
        llVis = (LinearLayout) headView.findViewById(R.id.llVis);
        llHead = (LinearLayout) headView.findViewById(R.id.llHead);
        tvHall = (TextView) headView.findViewById(R.id.tvHall);
        tvStatement = (TextView) headView.findViewById(R.id.tvStatement);
        tvTeam = (TextView) headView.findViewById(R.id.tvTeam);
        tvManager = (TextView) headView.findViewById(R.id.tvManager);
        ll_team_num = headView.findViewById(R.id.ll_teamMembers);
        ll_team_num.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, TeamMembersActivity.class).putExtra("id", id);
            intent.putExtra("id", id);
            startActivity(intent);
        });
        iv1 = headView.findViewById(R.id.iv1);
        iv2 = headView.findViewById(R.id.iv2);
        iv3 = headView.findViewById(R.id.iv3);
        fragmentList = new ArrayList<>();
        hallfragment = new HallFragment();
        fragmentList.add(hallfragment);
        statementFragment = new StatementFragment();
        fragmentList.add(statementFragment);
        teamFragment = new TeamFragment();
        fragmentList.add(teamFragment);
        vp = headView.findViewById(R.id.vp);
        vp.setCanScroll(true);
        rv.setAdapter(new RecycleAdapter(this));
        rv.addHeaderView(headView);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (getScollYDistance() + llVis.getTop() < 0) {
                    llInvis.setVisibility(View.VISIBLE);
                } else {
                    llInvis.setVisibility(View.GONE);
                }
            }
        });
        //获取团队详情数据
        setBodyParams(new String[]{"id"}, new String[]{id + ""});
        sendPost(Constants.base_url + "/api/club/base/detail.do", getCode, Constants.token);
    }

    public void setTitleName(String name) {
        teamName = name;
        tvTitle.setText(name);
    }

    private void initPop() {
        pop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.upheadlayout, null);
        tvPic = (TextView) pv.findViewById(R.id.tvPic);
        tvUp = (TextView) pv.findViewById(R.id.tvUp);
        tvPic.setOnClickListener(v -> {
            path = PhotoUtils.startPhoto(this);
            pop.dismiss();
        });
        tvUp.setOnClickListener(v -> {
            PhotoUtils.choosePhoto(202, this);
            pop.dismiss();
        });
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        pop.setOutsideTouchable(true);
        // 设置焦点
        pop.setFocusable(true);
        pop.setOnDismissListener(() -> {
            layoutParams.alpha = 1f;
            getWindow().setAttributes(layoutParams);
        });
        // 设置所在布局
        pop.setContentView(pv);
    }

    private void initSPPop() {
        //设置pop
        settingPop = new PopupWindow(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View spv = LayoutInflater.from(mContext).inflate(R.layout.popup_teamdetail, null);
        ll_share = (LinearLayout) spv.findViewById(R.id.ll_share);
        //隐藏分享
        ll_share.setVisibility(View.GONE);
        ll_collect = (LinearLayout) spv.findViewById(R.id.ll_collect);
        ivCollect = (ImageView) spv.findViewById(R.id.ivCollect);
        tvCollect = (TextView) spv.findViewById(R.id.tvCollect);
        ll_invisible = (LinearLayout) spv.findViewById(R.id.ll_invisible);
        ivInvisible = (ImageView) spv.findViewById(R.id.ivInvisible);
        tvInvisible = (TextView) spv.findViewById(R.id.tvInVisible);
        ll_joins = (LinearLayout) spv.findViewById(R.id.ll_joins);
        ivJoin = (ImageView) spv.findViewById(R.id.ivJoin);
        tvJoin = (TextView) spv.findViewById(R.id.tvJoin);
        ll_editor = (LinearLayout) spv.findViewById(R.id.ll_editor);
        ll_label = spv.findViewById(R.id.ll_label);
        ll_jb = spv.findViewById(R.id.ll_jb);
        ll_st = spv.findViewById(R.id.ll_st);
        ll_st_o = spv.findViewById(R.id.ll_st_o);
        ll_st_c = spv.findViewById(R.id.ll_st_loading);
        ll_st_join = spv.findViewById(R.id.ll_st_join);
        // cBean
        if (cBean.getIsFavorite() == 0) {
            isFavorite = false;
        } else if (cBean.getIsFavorite() == 1) {
            isFavorite = true;
            Glide.with(this).load(R.mipmap.saved).into(ivCollect);
            tvCollect.setText("取消收藏");
        }
        if (cBean.getSettingVisible() == 0) {
            isVisible = true;
        } else if (cBean.getSettingVisible() == 1) {
            isVisible = false;
            Glide.with(this).load(R.mipmap.kj).into(ivInvisible);
            tvInvisible.setText("设置可见");
        }
        if (cBean.getIsJoin() == 0) {
            isJoin = false;
            ll_collect.setVisibility(View.VISIBLE);
            checkSecret2();
        } else if (cBean.getIsJoin() == 1) {
            isJoin = true;
            ll_collect.setVisibility(View.GONE);
            //旋转45度
            ivJoin.animate().rotation(45);
            tvJoin.setText("退出团队");
        }
        //身份处理
        for (int i = 0; i < cBean.getUsers().size(); i++) {
            if (cBean.getUsers().get(i).getMemberId() == Constants.uid) {
                Log.e(TAG, "token: " + WenConstans.token);
                if (cBean.getUsers().get(i).getRole() == 1) {//群主
                    Log.i(TAG, "团长 memberId: " + cBean.getUsers().get(i).getMemberId() + "Constants.uid: " + Constants.uid);
                    IDENTIFY = 1;
                    ll_jb.setVisibility(View.GONE);
                    ll_joins.setVisibility(View.GONE);
                    ll_editor.setVisibility(View.VISIBLE);
                    ll_label.setVisibility(View.VISIBLE);
                    ll_invisible.setVisibility(View.VISIBLE);
                    ll_st_c.setVisibility(View.VISIBLE);
                    ll_st_o.setVisibility(View.GONE);
                    ll_st.setVisibility(View.GONE);
                } else if (cBean.getUsers().get(i).getRole() == 2) {//管理员
                    IDENTIFY = 2;
                    ll_joins.setVisibility(View.VISIBLE);
                    //ll_editor.setVisibility(View.VISIBLE);
                    ll_invisible.setVisibility(View.GONE);
                    ll_editor.setVisibility(View.GONE);
                    ll_label.setVisibility(View.GONE);
                    ll_st_c.setVisibility(View.VISIBLE);
                    ll_st_o.setVisibility(View.GONE);
                    ll_st.setVisibility(View.GONE);
                } else if (cBean.getUsers().get(i).getRole() == 3) {//普通团员
                    IDENTIFY = 3;
                    ll_joins.setVisibility(View.VISIBLE);
                    ll_editor.setVisibility(View.GONE);
                    ll_label.setVisibility(View.GONE);
                    ll_invisible.setVisibility(View.GONE);
                }
            }
        }
        ll_share.setOnClickListener(v -> {
            //分享
            settingPop.dismiss();
            showShare();
        });
        ll_collect.setOnClickListener(v -> {
            //收藏
            if (isFavorite) {
                setBodyParams(new String[]{"id", "op"}, new String[]{"" + cBean.getId(), "" + 2});
                sendPost(Constants.base_url + "/api/club/base/favorite.do", notCollect, Constants.token);
            } else {
                setBodyParams(new String[]{"id", "op"}, new String[]{"" + cBean.getId(), "" + 1});
                sendPost(Constants.base_url + "/api/club/base/favorite.do", collect, Constants.token);
            }
            settingPop.dismiss();
        });
        ll_invisible.setOnClickListener(v -> {
            //设置可见性
            if (isVisible) {
                setBodyParams(new String[]{"id", "settingVisible"}, new String[]{"" + cBean.getId(), "" + 1});
                sendPost(Constants.base_url + "/api/club/base/setvisibility.do", inVisible, Constants.token);
            } else {
                setBodyParams(new String[]{"id", "settingVisible"}, new String[]{"" + cBean.getId(), "" + 0});
                sendPost(Constants.base_url + "/api/club/base/setvisibility.do", visible, Constants.token);
            }
            settingPop.dismiss();
        });
        ll_joins.setOnClickListener(v -> {
            //加/退
            if (isJoin) {
                deldialog.show();
            } else {
                startActivity(new Intent(this, ApplyJoinTDActivity.class).putExtra("id", id));
            }
            settingPop.dismiss();
        });
        ll_editor.setOnClickListener(v -> {
            //编辑
            settingPop.dismiss();
            showEditorPop();
        });
        ll_label.setOnClickListener(v -> {
            settingPop.dismiss();
            startActivity(new Intent(this, ChooseTeamLableActivity.class)
                    .putExtra("id", id)
                    .putExtra("name", teamName)
                    .putExtra("labels", cBean.getLabels())
            );
        });
        ll_jb.setOnClickListener(v -> {
            // 举报
            settingPop.dismiss();
            startActivity(new Intent(this, ReportActivity.class).putExtra("type", 2).putExtra("id", cBean.getId() + ""));

        });
        ll_st.setOnClickListener(v -> {
            settingPop.dismiss();
            showSecretPop();
        });
        ll_st_o.setOnClickListener(v -> {
            settingPop.dismiss();
            showSecretChangePop();
        });
        ll_st_join.setOnClickListener(v -> {
            settingPop.dismiss();
            checkSecret3();
        });

        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        settingPop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        settingPop.setOutsideTouchable(true);
        // 设置焦点
        settingPop.setFocusable(true);
        settingPop.setOnDismissListener(() -> {
            layoutParams.alpha = 1f;
            getWindow().setAttributes(layoutParams);
        });
        // 设置所在布局
        settingPop.setContentView(spv);
    }

    private void openPayPasswordDialog() {
        if (changingSecret != null) {
            changingSecret.dismiss();
        }
        if (secret != null) {
            secret.dismiss();
        }
        payPasswordView = new PayPasswordView(this);
        payPasswordView.setActivity(this);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(payPasswordView);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }

    public void closePasswordDialog() {
        bottomSheetDialog.dismiss();
    }

    /**
     * 设置团队密码
     */
    private void initSetSecret() {
        secret = new PopupWindow(settingSecret, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        settingSecret = LayoutInflater.from(context).inflate(R.layout.editor_open_team_secret, null);
        setNoSecret = settingSecret.findViewById(R.id.noSecret);
        setSecret = settingSecret.findViewById(R.id.withSecret);
        secretLayoutParams = getWindow().getAttributes();
        secret.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_bg));
        //设置secret 能够响应外部点击事件
        secret.setOutsideTouchable(true);
        secret.setTouchable(true);
        secret.setFocusable(true);

        setNoSecret.setOnClickListener(v -> {
            setBodyParams(new String[]{"id"}, new String[]{id + ""});
            sendPost(WenConstans.DELETEJOINSECRET, DELETEJOINSECRET, Constants.token);
        });

        setSecret.setOnClickListener(v -> openPayPasswordDialog());
        secret.setOnDismissListener(() -> {
            secretLayoutParams.alpha = 1f;
            getWindow().setAttributes(secretLayoutParams);
        });
        secret.setContentView(settingSecret);
    }

    /**
     * 更改团队密码
     * 关闭密码加团
     */
    private void initChangeSecret() {
        changingSecret = new PopupWindow(settingSecret, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        changingSecretView = LayoutInflater.from(context).inflate(R.layout.editor_set_team_secret, null);
        changeSecret = changingSecretView.findViewById(R.id.changeSecret);
        closeSecret = changingSecretView.findViewById(R.id.closeSecret);
        teamSecret = changingSecretView.findViewById(R.id.teamSecret);
        teamSecret.setText(teamSt);
        changeSecretLayoutParams = getWindow().getAttributes();
        changingSecret.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_bg));
        //设置secret 能够响应外部点击事件
        changingSecret.setOutsideTouchable(true);
        changingSecret.setTouchable(true);
        changingSecret.setFocusable(true);

        changeSecret.setOnClickListener(v -> openPayPasswordDialog());

        closeSecret.setOnClickListener(v -> {//关闭团队密码
            setBodyParams(new String[]{"id",}, new String[]{id + ""});
            sendPost(WenConstans.CLOSEJOINSECRET, CLOSESECRET, Constants.token);

            Utils.toastShort(context, "你点击了关闭密码按钮");
        });
        changingSecret.setOnDismissListener(() -> {
            changeSecretLayoutParams.alpha = 1f;
            getWindow().setAttributes(changeSecretLayoutParams);
        });
        changingSecret.setContentView(changingSecretView);
    }

    public void initEditorPop() {
        editorPop = new PopupWindow(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View editorview = LayoutInflater.from(this).inflate(R.layout.pop_editor, null);
        cvHead = (CircleView) editorview.findViewById(R.id.cvHead);
        etName = (EditText) editorview.findViewById(R.id.etName);
        btConfirm = (Button) editorview.findViewById(R.id.btConfirm);
        if (cBean.getLogoImage() != null && !cBean.getLogoImage().isEmpty()) {
            Glide.with(this).load(Constants.base_url + cBean.getLogoImage()).asBitmap().into(cvHead);
        }
        etName.setText(cBean.getName());
        etName.setSelection(etName.getText().toString().trim().length());
        cvHead.setOnClickListener(v -> {
            iseditor = true;
            PhotoUtils.choosePhoto(202, this);
        });
        btConfirm.setOnClickListener(v -> {
            editorPop.dismiss();
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                Utils.toastShort(this, "团队名称不能为空");
                return;
            }
            if (editorFile == null) {
                setBodyParams(new String[]{"id", "name"}, new String[]{"" + cBean.getId(), "" + name});
                sendPost(Constants.base_url + "/api/club/base/updatebase.do", editor, Constants.token);
            } else {
                setBodyParams(new String[]{"id", "name"}, new String[]{"" + cBean.getId(), "" + name});
                setFileBodyParams(new String[]{"file"}, new File[]{editorFile});
                sendPost(Constants.base_url + "/api/club/base/updatebase.do", editor, Constants.token);
            }
        });
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        editorPop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        editorPop.setOutsideTouchable(true);
        // 设置焦点
        editorPop.setFocusable(true);
        editorPop.setOnDismissListener(() -> {
            layoutParams.alpha = 1f;
            getWindow().setAttributes(layoutParams);
        });

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 20) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });
        editorPop.setContentView(editorview);
    }

    public void showPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        pop.showAtLocation(llHead, Gravity.BOTTOM, 0, 0);
    }

    public void showSpvPop() {
        if (IDENTIFY != 3) {//团长或管理员打开菜单栏 检索密码
            checkSecret();
        }
        if (settingPop != null) {
            layoutParams.alpha = 0.5f;
            getWindow().setAttributes(layoutParams);
            settingPop.showAsDropDown(ivMore, Gravity.RIGHT, 0, 0);
        }
    }

    private void showEditorPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        editorPop.showAtLocation(ivMore, Gravity.CENTER, 0, 0);
    }

    private void showSecretPop() {
        secretLayoutParams.alpha = 0.5f;
        getWindow().setAttributes(secretLayoutParams);
        secret.showAtLocation(ivMore, Gravity.CENTER, 0, 0);
    }

    private void showSecretChangePop() {
        teamSecret.setText("检索密码中");
        searchSecret();//检索密码
        changeSecretLayoutParams.alpha = 0.5f;
        getWindow().setAttributes(changeSecretLayoutParams);
        changingSecret.showAtLocation(ivMore, Gravity.CENTER, 0, 0);
    }

    public void showShare() {
        if (shareFragment == null) {
            shareFragment = new BottomShareFragment();
            shareFragment.setDoClickListener(this);
        }
        shareFragment.show(getSupportFragmentManager(), "Dialog");
    }

//    private void initSectorMenuButton() {
//        menuButton = (SectorMenuButton) findViewById(R.id.center_sector_menu);
//        final List<ButtonData> buttonDatas = new ArrayList<>();
//        int[] drawable = {R.mipmap.chuangjian, R.mipmap.cjtp,
//                R.mipmap.cjhd, R.mipmap.cjgg};
//        for (int i = 0; i < 4; i++) {
//            ButtonData buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
//            //buttonData.setBackgroundColorId(this, R.color.colorAccent);
//            buttonDatas.add(buttonData);
//        }
//        menuButton.setButtonDatas(buttonDatas);
//        setListener(menuButton);
//    }


//    private void setListener(final SectorMenuButton button) {
//        button.setButtonEventListener(new ButtonEventListener() {
//            @Override
//            public void onButtonClicked(int index) {
//                Utils.toastShort(mContext,"button" + index);
//                switch (index) {
//                    case 1:
//                        //                        startActivity(new Intent(mContext, PublishVoteActivity.class));
//                        break;
//                    case 2:
//                        startActivity(new Intent(mContext, ReleaseActivitiesActivity.class).putExtra("type", 1));
//                        break;
//                    case 3:
//                        //                        startActivity(new Intent(mContext, EditorialBulletinActivity.class).putExtra("type", 1));
//                        break;
//                }
//            }
//
//            @Override
//            public void onExpand() {
//                Utils.toastShort(mContext,"onExpand");
//                menuButton.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCollapse() {
//                Utils.toastShort(mContext,"onCollapse");
//            }
//        });
//    }


    @Subscribe
    public void canAdd(ShowBean bean) {
        Log.e(TAG, "canadd" + bean.isShow());
    }

    @Subscribe
    public void refTeamData(RefTeamDetailEvent bean) {
        refData();
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        ivEWM.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, QRcodeActivity.class);
            if (cBean != null) {
                intent.putExtra("bean", cBean);
            }
            startActivity(intent);
        });
        tvHall.setOnClickListener(v -> setTvBg(0));
        tvStatement.setOnClickListener(v -> setTvBg(1));
        tvTeam.setOnClickListener(v -> setTvBg(2));
        tvManager.setOnClickListener(v -> setTvBg(3));
        tvHall2.setOnClickListener(v -> setTvBg(0));
        tvStatement2.setOnClickListener(v -> setTvBg(1));
        tvTeam2.setOnClickListener(v -> setTvBg(2));
        tvManager2.setOnClickListener(v -> {
            setTvBg(3);
        });
        ivAdd.setOnClickListener(v -> startActivity(new Intent(this, StatementActivity.class).putExtra("teamId", teamId)));
        ivAdd2.setOnClickListener(v -> {
            if (!LockMenu) {
                LockMenu = true;
                openAnim(ivAdd3, 0, 5, 300);
                openAnim(ivAdd4, 1, 5, 300);
                openAnim(ivAdd5, 2, 5, 300);
                ivAddBj.setVisibility(View.VISIBLE);
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#CBF0FE"));
            } else {
                close();
            }
        });
        ivAddBj.setOnClickListener(v -> close());
        ivAdd3.setOnClickListener(v -> {
            close();
            startActivity(new Intent(mContext, EditorialBulletinActivity.class).putExtra("type", 1));
        });
        ivAdd4.setOnClickListener(v -> {
            close();
            startActivity(new Intent(mContext, ReleaseActivitiesActivity.class).putExtra("type", 1));
        });
        ivAdd5.setOnClickListener(v -> {
            close();
            startActivity(new Intent(mContext, PublishVoteActivity.class));
        });
        ivMore.setOnClickListener(v -> {
            showSpvPop();
        });
    }

    private void checkSecret() {
        //团长或管理员检测是否有设置团队密码
        setBodyParams(new String[]{"id"}, new String[]{id + ""});
        sendPost(WenConstans.CHECKJOINSECRET, CHECKJOINSECRET, Constants.token);
    }

    private void checkSecret2() {
        //检测是否有设置团队密码
        setBodyParams(new String[]{"id"}, new String[]{id + ""});
        sendPost(WenConstans.CHECKJOINSECRET, CHECKJOINSECRET2, Constants.token);
    }

    private void checkSecret3() {
        //检测是否有设置团队密码
        setBodyParams(new String[]{"id"}, new String[]{id + ""});
        sendPost(WenConstans.CHECKJOINSECRET, CHECKJOINSECRET3, Constants.token);
    }

    public void joinWithSecret() {
        //检测是否有设置团队密码
        setBodyParams(new String[]{"id", "pwd"}, new String[]{id + "", getNewSecret()});
        sendPost(WenConstans.TEAMJOINSECRET, 2019, Constants.token);
    }

    private void searchSecret() {
        //检测是否有设置团队密码
        setBodyParams(new String[]{"id"}, new String[]{id + ""});
        sendPost(WenConstans.CHECKJOINSECRET, SEARCHJOINSECRET, Constants.token);
    }

    public void changeSecret() {
        //设置团队密码
        setBodyParams(new String[]{"id", "pwd"}, new String[]{id + "", getNewSecret()});
        sendPost(WenConstans.SETJOINSECRET, SETJOINSECRET, Constants.token);
    }

    public void joinTeam() {
        //设置团队密码
        setBodyParams(new String[]{"id"}, new String[]{id + ""});
        sendPost(WenConstans.JOINWITHNOSECRET, 2018, Constants.token);
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        int code = result.optInt("code");
        Gson gs = new Gson();

        switch (where) {
            case 2019:
                if (code == 0) {
                    Utils.toastShort(context, "已加团");
                    refData();
                } else {
                    Utils.toastShort(context, "加团失败");
                }
                break;
            case 2018:
                if (code == 0) {
                    Utils.toastShort(context, "已加团");
                } else {
                    Utils.toastShort(context, "操作异常");
                }
                break;
            case CHECKJOINSECRET3:
                settingPop.dismiss();
                if (code == 0) {
                    if (result.optJSONObject("data").optInt("pwdflag") != 1) {
                        Utils.toastShort(context, "操作异常");
                        break;
                    } else {
                        if (result.optJSONObject("data").optInt("pwd") == -1) {
                            //直接加团
                            joinTeam();
                        } else {
                            openPayPasswordDialog();
                        }
                    }
                }

                break;
            case CHECKJOINSECRET2:
                if (code == 0) {
                    if (result.optJSONObject("data").optInt("pwdflag") == 1) {//开启了密码加团
                        ll_st_join.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case SETJOINSECRET:
                if (code == 0) {
                    Utils.toastShort(context, "已重置密码为" + getNewSecret());
                } else {
                    Utils.toastShort(context, "操作失败");
                }
                break;
            case CLOSESECRET:
                changingSecret.dismiss();
                if (result.optInt("code") == 0) {
                    Utils.toastShort(context, "已关闭密码加团");
                } else {
                    Utils.toastShort(context, "操作失败");
                }

                break;
            case SEARCHJOINSECRET:
                if (code == 0) {
                    int pwdflag = result.optJSONObject("data").optInt("pwdflag");
                    ll_st_c.setVisibility(View.GONE);
                    if (pwdflag == 0) {
                        Utils.toastShort(context, "抱歉，密码加团已关闭");
                    } else {
                        String pwd = result.optJSONObject("data").getString("pwd");
                        if (pwd == null) {//什么也不做
                            break;
                        } else if (pwd.equals("-1")) {
                            teamSecret.setText("无密码");
                        } else {
                            teamSecret.setText(pwd);
                        }

                    }
                } else {
                    Utils.toastShort(context, "操作失败");
                }

                break;

            case CHECKJOINSECRET:
                if (code == 0) {
                    int pwdflag = result.optJSONObject("data").optInt("pwdflag");
                    ll_st_c.setVisibility(View.GONE);
                    if (pwdflag == 0) {
                        ll_st.setVisibility(View.VISIBLE);
                        ll_st_o.setVisibility(View.GONE);
                    } else {
                        ll_st_o.setVisibility(View.VISIBLE);
                        ll_st.setVisibility(View.GONE);
                    }
                } else {
                    Utils.toastShort(context, "操作失败");
                }
                break;

            case DELETEJOINSECRET:
                if (code == 0) {
                    Utils.toastShort(context, "已删除密码");
                    secret.dismiss();
                } else {
                    Utils.toastShort(context, "操作失败");
                    secret.dismiss();
                }
                break;

            case getCode:
                Log.i(TAG, "detail.do response: " + result);
                switch (code) {
                    case 0:
                        if (hasRefresh) {
                            rv.refreshComplete();
                        }
                        cBean = gs.fromJson(result.optString("data"), TeamBean.class);
                        if (cBean != null) {
                            setUi();
                            if (!hasRefresh) {
                                initSPPop();
                                initEditorPop();
                                initSetSecret();
                                initChangeSecret();
                            }
                            if (cBean != null) {
                                hallfragment.setBean(cBean);
                            }
//                                statementFragment.setBean(cBean);
//                                teamFragment.setBean(cBean);
                            teamId = cBean.getId();
                            Constants.clubId = cBean.getId();
                            Constants.isAdmin = cBean.isAdmin();
                            if (Constants.isAdmin) {
                                if (!hasRefresh) {
                                    managerFragment = new ManagerFragment();
                                    fragmentList.add(managerFragment);
                                }
                                if (cBean != null) {
                                    managerFragment.setBean(cBean);
                                }
                            } else {
                                tvManager.setVisibility(View.GONE);
                                tvManager2.setVisibility(View.GONE);
                            }
                            EventBus.getDefault().post(new TeamBean());
                            Constants.isJoin = cBean.getIsJoin() == 1;
                            Constants.creatorId = cBean.getCreator();
                            if (!hasRefresh) {
                                pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
                                vp.setAdapter(pagerAdapter);
                                vp.setOffscreenPageLimit(3);
                                vp.setId(fragmentList.get(0).hashCode());
                                vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        Log.d(TAG, "onPageSelected: " + position);
                                        vp.resetHeight(position);
                                        setTvBg(position);
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {
                                    }
                                });
                            }
                        }
                        hasRefresh = false;
                        initDialog();
                        break;
                    case 1:
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该功能操作权限");
                        break;
                }
                break;
            case uptdfm:
                switch (result.optInt("code")) {
                    case 0:

                        break;
                    case 1:
                        Utils.toastShort(this, "您还没有登录或登录已过期，请重新登录");
                        break;
                    case 2:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                    case 3:
                        Utils.toastShort(this, "您没有该功能操作权限");
                        break;
                }
                break;
            case collect:
                if (result.optInt("code") == 0) {
                    Glide.with(this).load(R.mipmap.saved).into(ivCollect);
                    tvCollect.setText("取消收藏");
                    isFavorite = true;
                    refData();
                    //
                    Utils.toastShort(this, "收藏成功");
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                EventBus.getDefault().postSticky(new RefCollect());
                break;
            case notCollect:
                if (result.optInt("code") == 0) {
                    Glide.with(this).load(R.mipmap.savess).into(ivCollect);
                    tvCollect.setText("收藏");
                    isFavorite = false;
                    refData();
                    EventBus.getDefault().postSticky(new RefCollect());
                    Utils.toastShort(this, "取消收藏成功");
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case inVisible:
                if (result.optInt("code") == 0) {
                    isVisible = false;
                    Glide.with(this).load(R.mipmap.kj).into(ivInvisible);
                    tvInvisible.setText("设置可见");
                    //
                    refData();
                    Utils.toastShort(this, "设置成功");
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case visible:
                if (result.optInt("code") == 0) {
                    isVisible = true;
                    Glide.with(this).load(R.mipmap.bkj).into(ivInvisible);
                    tvInvisible.setText("设置不可见");
                    //
                    refData();
                    Utils.toastShort(this, "设置成功");
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case join:

                break;
            case out:
                if (result.optInt("code") == 0) {
                    isJoin = false;
                    //旋转45度
                    ivJoin.animate().rotation(45);
                    tvJoin.setText("加入团队");
                    //刷新数据
                    EventBus.getDefault().post(new RefTeamChild1());
                    refData();
                    Utils.toastShort(this, "退出成功");
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
            case editor:
                if (result.optInt("code") == 0) {
                    //
                    refData();
                    Utils.toastShort(this, "编辑成功");
                } else {
                    Utils.toastShort(this, result.optString("msg"));
                }
                break;
        }
    }

    //刷新团队详情数据
    private void refData() {
        setBodyParams(new String[]{"id"}, new String[]{String.valueOf(id)});
        sendPost(Constants.base_url + "/api/club/base/detail.do", getCode, Constants.token);
    }

    private void setUi() {
        Glide.with(mContext).load(Constants.base_url + cBean.getLogoImage()).asBitmap().error(R.mipmap.head3).placeholder(R.mipmap.head3).into(ivTeamHead);
        tvTeamName.setText(cBean.getName());
        tvMInfo.setText("成员：" + cBean.getMemberNumber() + " " + "昨日来访：" + cBean.getYesterdayTraffic());
        if (cBean.getNotice() != null) {
            tvContent.setText("公告：" + cBean.getNotice().getTitle());
        } else {
            tvContent.setVisibility(View.GONE);
            //tvContent.setText("公告：欢迎大家来到高校联盟，一起加油！");
        }
        ArrayList<TeamBean.UsersBean> users = cBean.getUsers();
        ArrayList<String> imgs = new ArrayList<>();
        if (users != null && users.size() != 0) {
            for (int i = 0; i < users.size(); i++) {
                TeamBean.UsersBean user = users.get(i);
                if (i == 0) {
                    tvName.setText(user.getNickname());
                    if (TextUtils.isEmpty(user.getHeader())) {
                        ivGroupHead.setImageResource(R.mipmap.head3);
                    } else {
                        Glide.with(mContext).load(Constants.base_url + user.getHeader()).asBitmap().error(R.mipmap.head3).placeholder(R.mipmap.head3).into(ivGroupHead);
                    }
                } else {
                    if (i <= 3) {
                        imgs.add(user.getHeader());
                    }
                }
            }
        }
        if (imgs.size() == 1) {
            iv1.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(Constants.base_url + imgs.get(0)).asBitmap().error(R.mipmap.head3).placeholder(R.mipmap.head3).into(iv1);
            iv2.setVisibility(View.GONE);
            iv3.setVisibility(View.GONE);
        } else if (imgs.size() == 2) {
            iv1.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(Constants.base_url + imgs.get(0)).asBitmap().error(R.mipmap.head3).placeholder(R.mipmap.head3).into(iv1);
            Glide.with(mContext).load(Constants.base_url + imgs.get(1)).asBitmap().error(R.mipmap.head3).placeholder(R.mipmap.head3).into(iv2);
            iv3.setVisibility(View.GONE);
        } else if (imgs.size() == 3) {
            iv1.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.VISIBLE);
            iv3.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(Constants.base_url + imgs.get(0)).asBitmap().error(R.mipmap.head3).placeholder(R.mipmap.head3).into(iv1);
            Glide.with(mContext).load(Constants.base_url + imgs.get(1)).asBitmap().error(R.mipmap.head3).placeholder(R.mipmap.head3).into(iv2);
            Glide.with(mContext).load(Constants.base_url + imgs.get(2)).asBitmap().error(R.mipmap.head3).placeholder(R.mipmap.head3).into(iv3);
        }
        if (cBean.getCollege() != null) {
            if (!cBean.getCollege().isEmpty()) {
                tvSchool.setText("所属学校：" + cBean.getCollege());
            }
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(context, "操作异常");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("您确定要退出“" + cBean.getName() + "”团队吗？");
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            //退出团队
            setBodyParams(new String[]{"nid", "id", "op"}, new String[]{0 + "", "" + cBean.getId(), "" + 2});
            sendPost(Constants.base_url + "/api/club/base/join.do", out, Constants.token);
            deldialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> deldialog.dismiss());
        deldialog = builder.create();
        deldialog.setCancelable(false);
    }

    public void setTvBg(int status) {
        rv.smoothScrollToPosition(0);
        if (this.status == status) {
            return;
        }
        this.status = status;
        if (vp != null) {
            vp.setCurrentItem(status);
        }
//        llHead.setVisibility(status == 0 ? View.VISIBLE : View.GONE);
        tvHall.setSelected(status == 0);
        tvHall2.setSelected(status == 0);
        tvStatement.setSelected(status == 1);
        tvStatement2.setSelected(status == 1);
        tvTeam.setSelected(status == 2);
        tvTeam2.setSelected(status == 2);
        tvManager.setSelected(status == 3);
        tvManager2.setSelected(status == 3);

        if (status == 2) {
            if (Constants.isJoin) {
                ivAdd2.setVisibility(View.VISIBLE);
                ivAdd3.setVisibility(View.VISIBLE);
                ivAdd4.setVisibility(View.VISIBLE);
                ivAdd5.setVisibility(View.VISIBLE);
            }
        } else {
            ivAdd2.setVisibility(View.GONE);
            ivAdd3.setVisibility(View.GONE);
            ivAdd4.setVisibility(View.GONE);
            ivAdd5.setVisibility(View.GONE);
        }
        if (status == 1) {
            if (Constants.isJoin) {
                ivAdd.setVisibility(View.VISIBLE);
            }
        } else {
            ivAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 200:
                break;
            case 202:
                if (data != null) {
                    photoUri = null;
                    photoUri = data.getData();
                    try {
                        ContentResolver resolver = getContentResolver();
                        Uri originalUri = data.getData(); // 获得图片的uri
                        photoUri = originalUri;
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = resolver.query(originalUri, proj, null, null, null);
                        if (cursor == null) {
                            this.path = photoUri.getPath();
                        } else {
                            if (cursor.moveToFirst()) {
                                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                this.path = cursor.getString(column_index);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Luban.with(this).load(new File(path)).
                        setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onSuccess(File file) {
                                if (iseditor) {
                                    editorFile = file;
                                    iseditor = false;
                                    Glide.with(context).load(path).asBitmap().into(cvHead);
                                    return;
                                } else {
                                    hallfragment.setivPic(path);
                                }
                                setBodyParams(new String[]{"id"}, new String[]{"" + id});
                                setFileBodyParams(new String[]{"file"}, new File[]{file});
                                sendPost(Constants.base_url + "/api/club/base/update.do", uptdfm, Constants.token);
                            }
                        }).launch();
                break;
        }

    }

    @Override
    public void onRefresh() {
        if (status == 0) {
            hasRefresh = true;
            setBodyParams(new String[]{"id"}, new String[]{String.valueOf(id)});
            sendPost(Constants.base_url + "/api/club/base/detail.do", getCode, Constants.token);
        } else if (status == 1) {
            StatementFragment s1 = (StatementFragment) pagerAdapter.getItem(1);
            s1.refreshData();
        } else if (status == 2) {
            TeamFragment t1 = (TeamFragment) pagerAdapter.getItem(2);
            t1.refreshData();
        } else if (status == 3) {
            hasRefresh = true;
            setBodyParams(new String[]{"id"}, new String[]{String.valueOf(id)});
            sendPost(Constants.base_url + "/api/club/base/detail.do", getCode, Constants.token);
        }
    }

    @Override
    public void onLoadMore() {
        if (status == 0) {
            rv.loadMoreComplete();
        } else if (status == 1) {
            StatementFragment s1 = (StatementFragment) pagerAdapter.getItem(1);
            s1.loadmoreData();
        } else if (status == 2) {
            TeamFragment t1 = (TeamFragment) pagerAdapter.getItem(2);
            t1.loadmoreData();
        } else if (status == 3) {
            rv.loadMoreComplete();
        }
    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        return firstVisiableChildView.getTop();
    }

    public void setFinish(int types) {
        if (rv != null) {
            if (types == 0) {
                rv.refreshComplete();
            } else {
                rv.loadMoreComplete();
            }
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        setBodyParams(new String[]{"id"}, new String[]{String.valueOf(id)});
//        sendPost(Constants.base_url + "/api/club/base/detail.do", getCode, Constants.token);
//    }

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

    /**
     * 打开菜单
     * view:动画控件
     * index：第几个控件
     * num:有几个控件
     * radius：扇形半径
     */
    public void openAnim(View view, int index, int num, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double angle = Math.toRadians(180) / (num - 1) * index;
        int translationX = -(int) (radius * Math.cos(angle));
        int translationY = -(int) (radius * Math.sin(angle));

        ObjectAnimator one = ObjectAnimator.ofFloat(view, "translationX", 0, translationX);
        ObjectAnimator two = ObjectAnimator.ofFloat(view, "translationY", 0, translationY);
        ObjectAnimator three = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
        ObjectAnimator four = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        ObjectAnimator five = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        ObjectAnimator six = ObjectAnimator.ofFloat(view, "alpha", 0f, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(one, two, three, four, five, six);
        set.setDuration(2000);
        //回弹效果
        set.setInterpolator(new BounceInterpolator());
        set.start();
    }

    //关闭菜单
    public void closeAnim(View view, int index, int num, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double angle = Math.toRadians(180) / (num - 1) * index;
        Log.e("angle", String.valueOf(angle));
        int translationX = -(int) (radius * Math.cos(angle));
        int translationY = -(int) (radius * Math.sin(angle));

        ObjectAnimator one = ObjectAnimator.ofFloat(view, "translationX", translationX, 0);
        ObjectAnimator two = ObjectAnimator.ofFloat(view, "translationY", translationY, 0);
        ObjectAnimator three = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
        ObjectAnimator four = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        ObjectAnimator five = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
        ObjectAnimator six = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(one, two, three, four, five, six);
        set.setDuration(2000);
        //回弹效果
        set.setInterpolator(new BounceInterpolator());
        set.start();
    }

    public void close() {
        LockMenu = false;
        closeAnim(ivAdd3, 0, 5, 300);
        closeAnim(ivAdd4, 1, 5, 300);
        closeAnim(ivAdd5, 2, 5, 300);
        ivAddBj.setVisibility(View.GONE);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#00BBFF"));
    }

    public String getNewSecret() {
        return newSecret;
    }

    public void setNewSecret(String newSecret) {
        this.newSecret = newSecret;
    }
}
