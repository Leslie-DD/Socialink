package com.leslie.socialink.activity.team;

import static android.view.View.GONE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.CommentTeamAdapter;
import com.leslie.socialink.adapter.recycleview.QuestionsAdapter;
import com.leslie.socialink.adapter.recycleview.SecondhandgoodAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.MessageBean;
import com.leslie.socialink.bean.NoticeBean;
import com.leslie.socialink.bean.PullTheBlackBean;
import com.leslie.socialink.bean.SecondhandgoodBean;

import com.leslie.socialink.entity.RefMembers;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.QuestionBean;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.network.entity.UserInfoBean;
import com.leslie.socialink.utils.SharedPreferencesHelp;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.CircleView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalInformationActivity extends NetWorkActivity implements
        XRecyclerView.LoadingListener, CommentTeamAdapter.OnItemClickListener, QuestionsAdapter.DoSaveListener {

    private static final String TAG = "[PersonalInformationActivity]";

    private final int getUserCode = 1000;
    private final int GETDATA_T = 1001;
    private final int REFDATA_T = 1002;
    private final int LOADATA_T = 1003;
    private final int GETDATA_Q = 1004;
    private final int REFDATA_Q = 1005;
    private final int LOADATA_Q = 1006;

    private final int GETDATA_G = 1020;
    private final int REFDATA_G = 1021;
    private final int LOADATA_G = 1022;
    private final int CHECKATTENTION = 1102;
    private final int SETATTENTION = 1103;
    private final int DELETEATTENTION = 1104;
    private final int SETPULLTHEBLACK = 1105;
    private final int DELETETHEBLACK = 1106;
    private final int CHECKPULLTHEBLACK = 1107;
    private final int dosave = 1007;
    private final int changeName = 1011;
    private final int setAdministrator = 1008;
    private final int cancelAdministrator = 1009;
    private final int delCode = 1010;
    private String header;
    private MessageBean messageBean;
    private String hisnickname = null;
    private TextView tvName, tvDetail, tvLevel, tvSet, tvQu, tvTeam, /*tvDyn, tvPhoto, tvgood,*/
            tvTipContent, current, tvSchool, tvUserNikeName;
    private LinearLayout ll;
    private ImageView messages;
    private ImageView guanzhu;
    private TextView attention;
    private TextView pulltheblack;
    private XRecyclerView rv, rvQu, rvGood;
    private ImageView ivSex, ivMore;
    private CircleView ivHead;
    private View currentView;
    private int status = -1;
    private Boolean isblack = false;
    private Boolean isattention;
    private CommentTeamAdapter teamAdapter;
    private ArrayList<TeamBean> teamData;
    private QuestionsAdapter quAdapter;
    private SecondhandgoodAdapter goodAdapter;
    private ArrayList<SecondhandgoodBean> goodData;
    private ArrayList<SecondhandgoodBean> goodmoreData;
    private ArrayList<QuestionBean> quData;
    private ArrayList<QuestionBean> moreQUDate;
    private LinearLayout llTip;
    private int uid;
    private Gson gson = new Gson();
    private UserInfoBean userInfoBean;
    private int settingClub = -1; //团队可见性
    private int settingAsk = -1;  //问题可见性
    private int settingGood = 0; // 二手商品可见性 ，先设置可见
    private int role;  // 团员身份
    //我的团队
    private int teamType;  // 0 -> 初始化加载 ； 1 ->刷新；  2 -> 加载
    private int pnT = 1;
    private int psT = 0;   //总页数
    private JSONArray jsonArray;
    private TeamBean teamBean;
    //我的问问
    private int quType; // 0 -> 初始化加载 ； 1 ->刷新；  2 -> 加载
    private int pnQ = 1;
    private int psQ = 0;   //总页数
    private int clickPosition;
    private int id;
    private AlertDialog deldialog;
    private int teamid;

    //我的二手商品
    private int goodtype;
    private int png = 1;
    private int psg = 0;//总页数
    private int g_clickposition;
    private int g_id;


    //备注pop
    private PopupWindow pop;
    private View popView;
    private WindowManager.LayoutParams layoutParams;
    private Button btConfirm;
    private EditText etName;
    //三点pop
    private PopupWindow settingPop;
    private LinearLayout llEditor, llDel, llBlack, llBlack1;

    private RelativeLayout.LayoutParams lp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary, null));
        init();
        event();
    }

    private void init() {
        uid = getIntent().getIntExtra("uid", 0);
        role = getIntent().getIntExtra("role", 0);
        id = getIntent().getIntExtra("id", 0);
        teamid = getIntent().getIntExtra("teamid", 0);

        ivHead = (CircleView) findViewById(R.id.ivHead);
        ivMore = (ImageView) findViewById(R.id.ivMore);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDetail = (TextView) findViewById(R.id.tvDetail);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvSchool = findViewById(R.id.tvSchool);

        attention = (TextView) findViewById(R.id.attentions);
        messages = (ImageView) findViewById(R.id.messages);
        pulltheblack = (TextView) findViewById(R.id.pulltheblack);

        guanzhu = (ImageView) findViewById(R.id.guanzhu);
        ll = findViewById(R.id.ll);
        tvUserNikeName = findViewById(R.id.tvUserNikeName);
        tvSet = (TextView) findViewById(R.id.tvSet);
        tvQu = (TextView) findViewById(R.id.tvQu);
        tvTeam = (TextView) findViewById(R.id.tvTeam);
//        tvDyn = (TextView) findViewById(R.id.tvDyn);
//        tvgood = (TextView) findViewById(R.id.tvgood);
//        tvPhoto = (TextView) findViewById(R.id.tvPhoto);
        llTip = (LinearLayout) findViewById(R.id.llTip);
        ivSex = (ImageView) findViewById(R.id.ivSex);
        current = findViewById(R.id.current);
        currentView = findViewById(R.id.current);
        rv = (XRecyclerView) findViewById(R.id.rv);
        rvQu = (XRecyclerView) findViewById(R.id.rvQu);
        rvGood = (XRecyclerView) findViewById(R.id.rvGood);
        tvTipContent = (TextView) findViewById(R.id.tvTipContent);

        lp = new RelativeLayout.LayoutParams(tvSet.getLayoutParams());

        switch (role) {
            case 1:
                tvSet.setVisibility(GONE);
                break;
            case 2:
                tvSet.setText("取消设置管理员");
                break;
            case 3:
                tvSet.setText("设置为管理员");
                break;
        }

        //getUserInfo
        setBodyParams(new String[]{"uid"}, new String[]{"" + uid});
        sendPost(Constants.BASE_URL + "/api/user/info.do", getUserCode, Constants.token);
        setBodyParams(new String[]{"concerned"}, new String[]{"" + uid});
        sendPost(Constants.CHECK_ATTENTION, CHECKATTENTION, Constants.token);
        setBodyParams(new String[]{"black"}, new String[]{"" + uid});
        sendPost(Constants.CheckPullTheBlack, CHECKPULLTHEBLACK, Constants.token);
//
//        getattention();
        teamAdapter = new CommentTeamAdapter(mContext, new ArrayList<TeamBean>());
        quAdapter = new QuestionsAdapter(mContext);
        goodAdapter = new SecondhandgoodAdapter(mContext);

        quAdapter.setSaveListener(this);
        teamType = 0;
        pnT = 1;
        getTeamData(pnT, teamType);
        teamAdapter.setListener(this);
        /*quType = 0;
        pnQ = 1;
        getQuData(pnQ,quType);*/

        ConsTants.initXRecycleView(mContext, true, true, rv);
        ConsTants.initXRecycleView(mContext, true, true, rvQu);
        ConsTants.initXRecycleView(mContext, true, true, rvGood);
        rv.setLoadingListener(this);
        rvQu.setLoadingListener(this);
        rvGood.setLoadingListener(this);

        if (!Constants.isAdmin) {
            //llDel.setVisibility(View.GONE);
            tvSet.setVisibility(GONE);
        }

        initSPPop();
    }

    //关注设置。
//    private void setattention()
//    {
//        setBodyParams(new String[]{"followId"},new String[]{""+userInfoBean.getId()});
//        sendPost(Constants.DelecteAttention,ATTENTION2,SharedPreferencesHelp.getString("token",""));
//    }
//    private void getAttention()
//    {
//        setBodyParams(new String[]{"followId"},new String[]{""+userInfoBean.getId()});
//        sendPost(Constants.DelecteAttention,ATTENTION2,SharedPreferencesHelp.getString("token",""));
//    }
    private void initDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("要将" + userInfoBean.getNickname() + "踢出团队吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除
                setBodyParams(new String[]{"clubId", "uid"}, new String[]{"" + teamid, "" + uid});
                sendPost(Constants.BASE_URL + "/api/club/base/expel.do", delCode, Constants.token);
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


    private void initEditPop() {
        popView = LayoutInflater.from(context).inflate(R.layout.editor_teammember_name, null);
        pop = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        btConfirm = popView.findViewById(R.id.btConfirm);
        etName = popView.findViewById(R.id.etName);
        if (userInfoBean != null) {
            etName.setText(userInfoBean.getNickname());
            etName.setSelection(etName.getText().toString().trim().length());
        }
        layoutParams = getWindow().getAttributes();
        pop.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_bg));
        // 设置PopupWindow是否能响应外部点击事件
        pop.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        pop.setTouchable(true);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
                if (userInfoBean != null) {
                    setStarLine(current, userInfoBean.getNeedExperience(), userInfoBean.getTotalExperience(), userInfoBean.getExperience());
                }
            }
        });


        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mark = etName.getText().toString();
                if (TextUtils.isEmpty(mark)) {
                    Utils.toastShort(context, "请先输入备注名！");
                    return;
                }
                if (userInfoBean == null) {
                    Utils.toastShort(context, "获取用户信息失败！");
                    return;
                }
                setBodyParams(new String[]{"friendId", "aliasName"}, new String[]{"" + userInfoBean.getId(), mark});
                sendPost(Constants.BASE_URL + "/api/user/updateRemark.do", changeName, SharedPreferencesHelp.getString("token", ""));
                pop.dismiss();
                //onItemEditorNameListener.ItemEditor(position,mark);
            }
        });

        /*pop.showAtLocation(view, Gravity.CENTER,0,0);
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);*/
    }

    private void initSPPop() {
        //设置pop
        settingPop = new PopupWindow(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View spv = LayoutInflater.from(mContext).inflate(R.layout.pop_team_member, null);
        //ll_share = (LinearLayout) spv.findViewById(R.id.ll_share);
        llEditor = (LinearLayout) spv.findViewById(R.id.llEditor);
        llDel = (LinearLayout) spv.findViewById(R.id.llDel);
        llBlack = (LinearLayout) spv.findViewById(R.id.llBlack);
        llBlack1 = (LinearLayout) spv.findViewById(R.id.llBlack1);

//        if (isblack) {//已拉黑
//            llBlack1.setVisibility(View.VISIBLE);
//            llBlack.setVisibility(GONE);
//        } else {//拉黑
//            llBlack.setVisibility(View.VISIBLE);
//            llBlack1.setVisibility(GONE);
//        }


        llBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBodyParams(new String[]{"black"}, new String[]{"" + userInfoBean.getId()});
                sendPost(Constants.SetPullTheBlack, SETPULLTHEBLACK, Constants.token);
            }
        });

        llBlack1.setOnClickListener(v -> {
            setBodyParams(new String[]{"black"}, new String[]{"" + userInfoBean.getId()});
            sendPost(Constants.DeletePullTheBlack, DELETETHEBLACK, Constants.token);
        });

        llDel.setOnClickListener(v -> {
            settingPop.dismiss();
            deldialog.show();
        });

        llEditor.setOnClickListener(v -> {
            //showSpvPop();
            settingPop.dismiss();

            pop.showAtLocation(ivMore, Gravity.CENTER, 0, 0);
            layoutParams.alpha = 0.5f;
            getWindow().setAttributes(layoutParams);
        });

        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        settingPop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        settingPop.setOutsideTouchable(true);
        // 设置焦点
        settingPop.setFocusable(true);
        settingPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
                if (userInfoBean != null) {
                    setStarLine(current, userInfoBean.getNeedExperience(), userInfoBean.getTotalExperience(), userInfoBean.getExperience());
                }
            }
        });
        // 设置所在布局
        settingPop.setContentView(spv);
    }

    public void showSpvPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        settingPop.showAsDropDown(ivMore, Gravity.RIGHT, -10, 0);

    }


    private void getTeamData(int pn, int type) {
        switch (type) {
            case 0:
                setBodyParams(new String[]{"type", "pn", "ps", "uid"}, new String[]{"" + 3, "" + pn, "" + Constants.DEFAULT_PS, "" + uid});
                sendPost(Constants.BASE_URL + "/api/club/base/pglist.do", GETDATA_T, Constants.token);
                break;
            case 1:
                setBodyParams(new String[]{"type", "pn", "ps", "uid"}, new String[]{"" + 3, "" + pn, "" + Constants.DEFAULT_PS, "" + uid});
                sendPost(Constants.BASE_URL + "/api/club/base/pglist.do", REFDATA_T, Constants.token);
                break;
            case 2:
                setBodyParams(new String[]{"type", "pn", "ps", "uid"}, new String[]{"" + 3, "" + pn, "" + Constants.DEFAULT_PS, "" + uid});
                sendPost(Constants.BASE_URL + "/api/club/base/pglist.do", LOADATA_T, Constants.token);
                break;
        }
    }

    private void getQuData(int pnQ, int quType) {
        int type = 0; // 1 - 我的  2 - 别人的
        if (userInfoBean.getNickname().equals(SharedPreferencesHelp.getString("user", ""))) {
            type = 1;
        } else {
            type = 2;
        }
        switch (quType) {
            case 0:
                if (type == 1) {
                    setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + pnQ, "" + Constants.DEFAULT_PS, "" + type});
                    sendPost(Constants.BASE_URL + "/api/ask/base/myPglist.do", GETDATA_Q, Constants.token);
                } else if (type == 2) {
                    setBodyParams(new String[]{"pn", "ps", "type", "uid"}, new String[]{"" + pnQ, "" + Constants.DEFAULT_PS, "" + type, "" + uid});
                    sendPost(Constants.BASE_URL + "/api/ask/base/myPglist.do", GETDATA_Q, Constants.token);
                }
                break;
            case 1:
                if (type == 1) {
                    setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + pnQ, "" + Constants.DEFAULT_PS, "" + type});
                    sendPost(Constants.BASE_URL + "/api/ask/base/myPglist.do", REFDATA_Q, Constants.token);
                } else if (type == 2) {
                    setBodyParams(new String[]{"pn", "ps", "type", "uid"}, new String[]{"" + pnQ, "" + Constants.DEFAULT_PS, "" + type, "" + uid});
                    sendPost(Constants.BASE_URL + "/api/ask/base/myPglist.do", REFDATA_Q, Constants.token);
                }
                break;
            case 2:
                if (type == 1) {
                    setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + pnQ, "" + Constants.DEFAULT_PS, "" + type});
                    sendPost(Constants.BASE_URL + "/api/ask/base/myPglist.do", LOADATA_Q, Constants.token);
                } else if (type == 2) {
                    setBodyParams(new String[]{"pn", "ps", "type", "uid"}, new String[]{"" + pnQ, "" + Constants.DEFAULT_PS, "" + type, "" + uid});
                    sendPost(Constants.BASE_URL + "/api/ask/base/myPglist.do", LOADATA_Q, Constants.token);
                }
                break;
        }

    }

    private void getGoodData(int png, int goodType) {
        int type = 0; // 1 - 我的  2 - 别人的
        if (userInfoBean.getNickname().equals(SharedPreferencesHelp.getString("user", ""))) {
            type = 1;
        } else {
            type = 2;
        }
        switch (goodType) {
            case 0:
                if (type == 1) {
                    setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + png, "" + Constants.DEFAULT_PS, "" + type});
                    sendPost(Constants.BASE_URL + "/api/goods/base/myPglist.do", GETDATA_G, Constants.token);
                } else if (type == 2) {
                    setBodyParams(new String[]{"pn", "ps", "type", "uid"}, new String[]{"" + png, "" + Constants.DEFAULT_PS, "" + type, "" + uid});
                    sendPost(Constants.BASE_URL + "/api/goods/base/myPglist.do", GETDATA_G, Constants.token);
                }
                break;
            case 1:
                if (type == 1) {
                    setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + png, "" + Constants.DEFAULT_PS, "" + type});
                    sendPost(Constants.BASE_URL + "/api/goods/base/myPglist.do", REFDATA_G, Constants.token);
                } else if (type == 2) {
                    setBodyParams(new String[]{"pn", "ps", "type", "uid"}, new String[]{"" + png, "" + Constants.DEFAULT_PS, "" + type, "" + uid});
                    sendPost(Constants.BASE_URL + "/api/goods/base/myPglist.do", REFDATA_G, Constants.token);
                }
                break;
            case 2:
                if (type == 1) {
                    setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + png, "" + Constants.DEFAULT_PS, "" + type});
                    sendPost(Constants.BASE_URL + "/api/goods/base/myPglist.do", LOADATA_G, Constants.token);
                } else if (type == 2) {
                    setBodyParams(new String[]{"pn", "ps", "type", "uid"}, new String[]{"" + png, "" + Constants.DEFAULT_PS, "" + type, "" + uid});
                    sendPost(Constants.BASE_URL + "/api/goods/base/myPglist.do", LOADATA_G, Constants.token);
                }
                break;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setCurrent(0, 100);
        }
    }

    private void setCurrent(int current, int total) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) currentView.getLayoutParams();
        lp.width = Utils.dip2px(mContext, 130) * current / total;
        lp.height = lp.height;
        currentView.setLayoutParams(lp);
    }

    private void event() {
        tvTeam.setOnClickListener(v -> setTvBg(0));
        tvQu.setOnClickListener(v -> setTvBg(1));
//        tvDyn.setOnClickListener(this);
//        tvPhoto.setOnClickListener(this);
//        tvgood.setOnClickListener(this);
        findViewById(R.id.ivBack).setOnClickListener(v -> {
            Intent intents = new Intent();
            intents.putExtra("back_return", pulltheblack.getText().toString());
            intents.putExtra("attention_return", attention.getText().toString());
            setResult(1, intents);
            finish();
        });
        tvSet.setOnClickListener(v -> {
            if (role == 2) { //取消管理员
                setBodyParams(new String[]{"id", "op"}, new String[]{"" + id, "" + 2});
                sendPost(Constants.BASE_URL + "/api/club/member/setAdmin.do", cancelAdministrator, Constants.token);
            } else if (role == 3) { //设置管理员
                setBodyParams(new String[]{"id", "op"}, new String[]{"" + id, "" + 1});
                sendPost(Constants.BASE_URL + "/api/club/member/setAdmin.do", setAdministrator, Constants.token);
            }
        });
        ivMore.setOnClickListener(v -> showSpvPop());
        messages.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(PersonalInformationActivity.this, MessageActivity.class);
            intent.putExtra("hisid", userInfoBean.getId());
            intent.putExtra("nickname", hisnickname);
            intent.putExtra("myid", Constants.uid);
            intent.putExtra("myname", Constants.userName);
            intent.putExtra("myheader", header);
            startActivity(intent);
        });
        guanzhu.setOnClickListener(v -> {
            if (!isattention) {
                setBodyParams(new String[]{"concerned"}, new String[]{"" + userInfoBean.getId()});
                sendPost(Constants.SET_ATTENTION, SETATTENTION, Constants.token);
            } else {
                setBodyParams(new String[]{"concerned"}, new String[]{"" + userInfoBean.getId()});
                sendPost(Constants.DELETE_ATTENTION, DELETEATTENTION, Constants.token);
            }
        });
    }


    public void setTvBg(int status) {
        if (this.status == status) {
            return;
        }
        this.status = status;
        tvTeam.setSelected(status == 0 ? true : false);
        tvQu.setSelected(status == 1 ? true : false);
//        tvDyn.setSelected(status == 2 ? true : false);
//        tvPhoto.setSelected(status == 3 ? true : false);
//        tvgood.setSelected(status == 4 ? true : false);
        if (status == 0 && teamAdapter != null) {
            if (rv.getAdapter() != teamAdapter) {
                rv.setAdapter(teamAdapter);
            }
            if (userInfoBean != null && userInfoBean.getId() != Constants.uid) {
                if (settingClub == 0) {
                    rv.setVisibility(View.VISIBLE);
                    rvQu.setVisibility(GONE);
                    rvGood.setVisibility(GONE);
                    llTip.setVisibility(GONE);
                } else if (settingClub == 1) {
                    rv.setVisibility(GONE);
                    rvQu.setVisibility(GONE);
                    rvGood.setVisibility(GONE);
                    llTip.setVisibility(View.VISIBLE);
                    tvTipContent.setText("Ta设置了团队不可见");
                }
            } else {
                rv.setVisibility(View.VISIBLE);
                rvQu.setVisibility(GONE);
                rvGood.setVisibility(GONE);
            }

        }

        if (status == 1 && quAdapter != null) {
            if (rvQu.getAdapter() != quAdapter) {
                rvQu.setAdapter(quAdapter);
            }
            if (userInfoBean != null && userInfoBean.getId() != Constants.uid) {
                if (settingAsk == 0) {
                    rvQu.setVisibility(View.VISIBLE);
                    rv.setVisibility(GONE);
                    rvGood.setVisibility(GONE);
                    llTip.setVisibility(GONE);
                } else if (settingAsk == 1) {
                    rvQu.setVisibility(GONE);
                    rvGood.setVisibility(GONE);
                    rv.setVisibility(GONE);
                    llTip.setVisibility(View.VISIBLE);
                    tvTipContent.setText("Ta设置了问题不可见");
                }
            } else {
                rvQu.setVisibility(View.VISIBLE);
                rv.setVisibility(GONE);
                rvGood.setVisibility(GONE);
                // 这里没有使用碎片   而是使用 一个列表可见 则另外一个列表不可见的技术
            }
        }
        if (status == 4 && goodAdapter != null) {
            if (rvGood.getAdapter() != goodAdapter) {
                rvGood.setAdapter(goodAdapter);
            }
            if (userInfoBean != null && userInfoBean.getId() != Constants.uid) {
                if (settingAsk == 0) {
                    rvGood.setVisibility(View.VISIBLE);
                    rv.setVisibility(GONE);
                    rvQu.setVisibility(GONE);
                    llTip.setVisibility(GONE);
                } else if (settingAsk == 1) {
                    rvQu.setVisibility(GONE);
                    rv.setVisibility(GONE);
                    rvGood.setVisibility(GONE);
                    llTip.setVisibility(View.VISIBLE);
                    tvTipContent.setText("Ta设置了二手商品不可见");
                }
            } else {
                rvGood.setVisibility(View.VISIBLE);
                rv.setVisibility(GONE);
                rvQu.setVisibility(GONE);
            }
        }
    }

    //动态设置Textview长度
    private void setStarLine(final TextView tv, final int n, final int t, final int e) {
        tv.post(() -> {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            int maxLength = Utils.dip2px(mContext, 130);  //总长度
            int width = (int) (maxLength * (((e - t) * 1.0F) / n));
            if (width > 0 && width < 10) {
                lp.width = width + 10;
            } else {
                lp.width = width;
            }
            tv.setLayoutParams(lp);
        });
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        Log.d(TAG, "onSuccess where: " + where + ", result: " + result.toString());
        int resultType = result.optInt("code");
        switch (where) {
            case CHECKPULLTHEBLACK:
                PullTheBlackBean pullTheBlackBean = gson.fromJson(result.optString("data"), PullTheBlackBean.class);
                if (pullTheBlackBean == null) {
                    Log.w(TAG, "pullTheBlackBean null, data: " + result.optString("data"));
                    break;
                }
                if (pullTheBlackBean.isCheck()) {
                    isblack = true;
                    llBlack1.setVisibility(View.VISIBLE);
                    llBlack.setVisibility(GONE);
                } else {
                    llBlack.setVisibility(View.VISIBLE);
                    llBlack1.setVisibility(GONE);
                    isblack = false;
                }
                break;
            case SETPULLTHEBLACK:
                isblack = true;
                Utils.toastShort(this, "已拉黑");
                llBlack1.setVisibility(View.VISIBLE);
                llBlack.setVisibility(GONE);
                break;
            case DELETETHEBLACK:
                isblack = false;
                Utils.toastShort(this, "已取消拉黑");
                llBlack.setVisibility(View.VISIBLE);
                llBlack1.setVisibility(GONE);
                break;
            case CHECKATTENTION:
                NoticeBean noticeBean = gson.fromJson(result.optString("data"), NoticeBean.class);
                if (noticeBean == null) {
                    Log.w(TAG, "noticeBean null, data: " + result.optString("data"));
                    break;
                }
                if (noticeBean.isCheck()) {
                    isattention = true;
                    guanzhu.setImageResource(R.mipmap.yiguanzhu);
                } else {
                    isattention = false;
                    guanzhu.setImageResource(R.mipmap.guanzhu);
                }
                break;
            case SETATTENTION:
                isattention = true;
                guanzhu.setImageResource(R.mipmap.yiguanzhu);
                Utils.toastShort(this, "已关注");
                break;
            case DELETEATTENTION:
                isattention = false;
                guanzhu.setImageResource(R.mipmap.guanzhu);
                Utils.toastShort(this, "已取消关注");
                break;
            case getUserCode: //用户信息
                switch (result.optInt("code")) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            userInfoBean = gson.fromJson(result.optString("data"), UserInfoBean.class);
                            if (userInfoBean != null) {
                                initDialog();
                                initEditPop();
                                header = userInfoBean.getHeader();
                                if (userInfoBean.getId() != Constants.uid) {
                                    messages.setVisibility(View.VISIBLE);
                                    guanzhu.setVisibility(View.VISIBLE);
                                }

                                hisnickname = userInfoBean.getNickname();
                                tvName.setText(userInfoBean.getNickname());
                                String college = userInfoBean.getCollege();
                                if (college == null) {
                                    tvSchool.setText("学校未设置");
                                } else {
                                    tvSchool.setText(college);
                                }
                                if (userInfoBean.getUserNickName() == null || userInfoBean.getUserNickName().equals(userInfoBean.getNickname())) {
                                    tvUserNikeName.setVisibility(GONE);
                                    lp.setMargins(Utils.dip2px(mContext, 170), Utils.dip2px(mContext, 20), 0, 0);
                                    tvSet.setLayoutParams(lp);
                                } else {
                                    tvUserNikeName.setVisibility(View.VISIBLE);
                                    tvUserNikeName.setText("昵称：" + userInfoBean.getUserNickName());
                                    lp.setMargins(Utils.dip2px(mContext, 170), Utils.dip2px(mContext, 30), 0, 0);
                                    tvSet.setLayoutParams(lp);
                                }

                                if (userInfoBean.getHeader() != null && !userInfoBean.getHeader().isEmpty()) {
                                    Glide.with(this).load(Constants.BASE_URL + userInfoBean.getHeader()).asBitmap().error(R.mipmap.head3).into(ivHead);
                                } else {
                                    Glide.with(this).load(R.mipmap.head3).asBitmap().error(R.mipmap.head3).into(ivHead);
                                }
                                tvLevel.setText("LV " + userInfoBean.getGrade());
                                tvDetail.setText("经验值：" + (userInfoBean.getExperience() - userInfoBean.getTotalExperience()) + "/" + userInfoBean.getNeedExperience());
                                setStarLine(current, userInfoBean.getNeedExperience(), userInfoBean.getTotalExperience(), userInfoBean.getExperience());
                                settingClub = userInfoBean.getSettingClub();
                                settingAsk = userInfoBean.getSettingAsk();

                                if (userInfoBean.getSex() == 1) {
                                    ivSex.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.me19));
                                } else {
                                    ivSex.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.me36));
                                }

                                if (Constants.isAdmin) {
                                    //不能自己踢出自己
                                    if (userInfoBean.getId() == Constants.uid) {
                                        ivMore.setVisibility(GONE);
                                        tvSet.setVisibility(GONE);

                                    }
                                    //不能踢团长
                                    if (Constants.creatorId == userInfoBean.getId()) {
                                        llDel.setVisibility(GONE);
                                        tvSet.setVisibility(GONE);
                                    }
                                    //我是团长 团长无敌
                                    if (userInfoBean.getId() != Constants.uid && Constants.creatorId == Constants.uid) {
                                        llDel.setVisibility(View.VISIBLE);
                                        tvSet.setVisibility(View.VISIBLE);
                                    }
                                    //我是副团长
                                    if (userInfoBean.getId() != Constants.uid && Constants.creatorId != Constants.uid) {
                                        if (role == 1 || role == 2) {
                                            llDel.setVisibility(GONE);
                                            tvSet.setVisibility(GONE);
                                        } else if (role == 3) {
                                            llDel.setVisibility(View.VISIBLE);
                                            tvSet.setVisibility(GONE);
                                        }

                                    }
                                } else {
                                    llDel.setVisibility(GONE);
                                    if (userInfoBean.getId() == Constants.uid) {
                                        ivMore.setVisibility(GONE);
                                        llDel.setVisibility(GONE);
                                    }
                                }

                                if (role == 0 && id == 0 && teamid == 0) {
                                    llDel.setVisibility(GONE);
                                    tvSet.setVisibility(GONE);
                                }

                                if (userInfoBean.getId() != Constants.uid) {
                                    if (settingClub == 0) {
                                        rv.setVisibility(View.VISIBLE);
                                        llTip.setVisibility(GONE);
                                    } else if (settingClub == 1) {
                                        rv.setVisibility(GONE);
                                        llTip.setVisibility(View.VISIBLE);
                                        tvTipContent.setText("Ta设置了问题不可见");
                                    }
                                } else {
                                    rvQu.setVisibility(GONE);
                                    rv.setVisibility(View.VISIBLE);
                                }

                                setTvBg(0);


                                //获取问问
                                quType = 0;
                                pnQ = 1;
                                getQuData(pnQ, quType);
                            }
                        }
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
            case GETDATA_T: //获取团队
            case REFDATA_T: //刷新团队
                if (resultType == 0) {
                    if (!result.optString("data").isEmpty()) {
                        try {
                            psT = result.optJSONObject("data").optInt("totalPage");
                            jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                            if (jsonArray.length() > 0) {
                                teamData = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                    teamBean.setItemType(1);
                                    teamData.add(teamBean);
                                }
                                teamAdapter.setData(teamData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case LOADATA_T: //加载团队
                if (resultType == 0) {
                    if (!result.optString("data").isEmpty()) {
                        try {
                            psT = result.optJSONObject("data").optInt("totalPage");
                            jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                    teamBean.setItemType(1);
                                    teamData.add(teamBean);
                                }
                                teamAdapter.setData(teamData);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case GETDATA_Q: //获取问问
            case REFDATA_Q: //刷新问问
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        quData = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<QuestionBean>>() {
                                }.getType());
                        if (quData == null || quData.size() == 0) {
                            quData = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            psQ = data.getInt("totalPage");
                        }
                    }
                }
                quAdapter.setData(quData);
                break;
            case LOADATA_Q:  //加载问问
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        moreQUDate = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<QuestionBean>>() {
                                }.getType());
                        if (moreQUDate == null || moreQUDate.size() == 0) {
                            moreQUDate = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            psQ = data.getInt("totalPage");
                        }
                    }
                }
                quData.addAll(moreQUDate);
                quAdapter.setData(quData);
                break;
            case GETDATA_G: //获取商品
            case REFDATA_G: //刷新商品
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        goodData = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SecondhandgoodBean>>() {
                                }.getType());
                        if (goodData == null || goodData.size() == 0) {
                            goodData = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            psg = data.getInt("totalPage");
                        }
                    }
                }
                goodAdapter.setData(goodData);
                break;
            case LOADATA_G:  //加载商品
                if (result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if (data != null && data.has("list")) {
                        goodmoreData = gson.fromJson(data.getJSONArray("list").toString(),
                                new TypeToken<List<SecondhandgoodBean>>() {
                                }.getType());
                        if (goodmoreData == null || goodmoreData.size() == 0) {
                            goodmoreData = new ArrayList<>();
                        }
                        if (data.has("totalPage")) {
                            psg = data.getInt("totalPage");
                        }
                    }
                }
                goodData.addAll(goodmoreData);
                goodAdapter.setData(goodData);
                break;
            case dosave: //问问点赞
                Utils.toastShort(mContext, result.getString("msg") + "");
                int zan = quData.get(clickPosition).likeAmount;
                if (TextUtils.isEmpty(quData.get(clickPosition).userLike)) {
                    quData.get(clickPosition).userLike = "1";
                    zan = zan + 1;
                    quData.get(clickPosition).likeAmount = zan;
                } else {
                    quData.get(clickPosition).userLike = "";
                    zan = zan - 1;
                    quData.get(clickPosition).likeAmount = zan;
                }
                quAdapter.setData(quData);
                break;
            case setAdministrator:
                if (result.optInt("code") == 0) {
                    role = 2;
                    EventBus.getDefault().post(new RefMembers());
                    tvSet.setText("取消设置管理员");
                }
                Utils.toastShort(this, result.optString("msg"));
                break;
            case cancelAdministrator:
                if (result.optInt("code") == 0) {
                    role = 3;
                    EventBus.getDefault().post(new RefMembers());
                    tvSet.setText("设置为管理员");
                }
                Utils.toastShort(this, result.optString("msg"));
                break;
            case delCode:
                if (result.optInt("code") == 0) {
                    EventBus.getDefault().post(new RefMembers());
                    this.finish();
                }
                Utils.toastShort(this, result.optString("msg"));
                break;
            case changeName:
                if (result.optInt("code") == 0) {
                    setBodyParams(new String[]{"uid"}, new String[]{"" + uid});
                    sendPost(Constants.BASE_URL + "/api/user/info.do", getUserCode, Constants.token);
                    EventBus.getDefault().post(new RefMembers());
                }
                Utils.toastShort(this, result.optString("msg"));
                break;
        }

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            if (status == 0) {
                //团队
                pnT = 1;
                teamType = 1;
                getTeamData(pnT, teamType);
                rv.refreshComplete();
            } else if (status == 1) {
                //问题
                quType = 1;
                pnQ = 1;
                getQuData(pnQ, quType);
                rvQu.refreshComplete();
            } else if (status == 4) {
                //二手
                goodtype = 1;
                png = 1;
                getGoodData(png, goodtype);
                rvGood.refreshComplete();
            }

        }, 1000);

    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(() -> {
            if (status == 0) {
                //团队
                if (pnT < psT) {
                    pnT++;
                    teamType = 2;
                    getTeamData(pnT, teamType);
                }
                rv.loadMoreComplete();
            } else if (status == 1) {
                //问题
                if (pnQ < psQ) {
                    pnQ++;
                    quType = 2;
                    getQuData(pnQ, quType);
                }
                rvQu.loadMoreComplete();
            } else if (status == 4) {
                //二手
                if (png < psg) {
                    png++;
                    goodtype = 2;
                    getGoodData(png, goodtype);
                }
                rvGood.loadMoreComplete();
            }

        }, 1000);
    }

    @Override
    public void OnItemClick(int position) {
        int type = teamAdapter.getData().get(position).getItemType();
        if (type == 1) {
            Intent intent = new Intent(mContext, TeamDetailActivity.class);
            intent.putExtra("id", teamAdapter.getData().get(position).getId());
            startActivity(intent);
        }
    }

    @Override
    public void doSave(int position) {
        clickPosition = position;
        setBodyParams(new String[]{"id"}, new String[]{quData.get(position).id + ""});
        sendPost(Constants.QUESTION_LIKES, dosave, Constants.token);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        this.finish();
    }
}
