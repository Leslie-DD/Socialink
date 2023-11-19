package com.example.heshequ.activity.team;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.adapter.Adapter_GridView;
import com.example.heshequ.adapter.GvEmojiAdapter;
import com.example.heshequ.adapter.recycleview.CommentAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.P;
import com.example.heshequ.entity.BuildingBean;
import com.example.heshequ.entity.CommentBean;
import com.example.heshequ.entity.RefTjEvent;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.view.MyGv;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TjDetailActivity extends NetWorkActivity implements View.OnClickListener, XRecyclerView.LoadingListener {
    private CircleView ivHead;
    private ImageView ivBack;
    private TextView tvTitle, tvName, tvContent, tvDel, tvDate, tvZan, tvPl, tvLikes, tvTitle2;
    private BuildingBean bean;
    private int speakId;
    private View headView;
    private LinearLayout llLikes, llComment;
    private ArrayList<BuildingBean.LikesBean> likesBeans;
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
    private AlertDialog deldialog;
    private int delid;
    private final int initData = 1000;
    private final int lodData = 1002;
    private final int delSpeak = 1001;
    private final int itsaid = 1003;
    private final int like = 1004;
    private final int sendComment = 1005;
    private final int delComment = 1006;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_detail);
        init();
        event();
        //监听软键盘高度px
        Utils.addOnSoftKeyBoardVisibleListener(this);
    }

    private void init() {
        gson = new Gson();
        bean = (BuildingBean) getIntent().getSerializableExtra("bean");
        speakId = bean.getId();
        tvTitle2 = (TextView) findViewById(R.id.tvTitle);
        tvTitle2.setText("团建详情");
        headView = LayoutInflater.from(mContext).inflate(R.layout.tj_detail_head, null);
        ivHead = (CircleView) headView.findViewById(R.id.ivHead);
        tvName = (TextView) headView.findViewById(R.id.tvName);
        llComment = findViewById(R.id.llComment);
        tvContent = (TextView) headView.findViewById(R.id.tvContent);
        tvDel = (TextView) headView.findViewById(R.id.tvDel);
        tvDate = (TextView) headView.findViewById(R.id.tvDate);
        tvZan = (TextView) headView.findViewById(R.id.tvZan);
        tvPl = (TextView) headView.findViewById(R.id.tvPl);
        tvLikes = (TextView) headView.findViewById(R.id.tvLikes);
        llLikes = (LinearLayout) headView.findViewById(R.id.llLikes);
        tvTitle = (TextView) headView.findViewById(R.id.tvTitle);
        gv = (MyGv) headView.findViewById(R.id.gv);
        if (bean != null) {
            setUi();
        }
        rv = (XRecyclerView) findViewById(R.id.rv);
        ConsTants.initXrecycleView(context, true, true, rv);
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
        if (!Constants.isJoin) {
            llComment.setVisibility(View.GONE);
        }

        initDelDialog();
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
                sendPost(Constants.base_url + "/api/club/tb/delcomment.do", delComment, Constants.token);
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

    private void setUi() {
        if (!Constants.isAdmin) {
            tvDel.setVisibility(View.GONE);
        }
        if (bean.getHeader() != null && !bean.getHeader().isEmpty()) {
            Glide.with(this).load(Constants.base_url + bean.getHeader()).asBitmap().into(ivHead);
        } else {
            ivHead.setImageResource(R.mipmap.head3);
        }
        if (bean.getPhotos() == null || bean.getPhotos().size() == 0) {
            gv.setVisibility(View.GONE);
        } else {
            gv.setVisibility(View.VISIBLE);
            imgs = new ArrayList<>();
            for (BuildingBean.PhotosBean photosBean : bean.getPhotos()) {
                imgs.add(Constants.base_url + photosBean.getPhotoId());
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
        tvName.setText(bean.getPresentorName());
        tvTitle.setText(bean.getTitle());
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

        tvDel.setOnClickListener(this);
        tvZan.setOnClickListener(this);
        tvPl.setOnClickListener(this);
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("要删除这条团建吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除
                setBodyParams(new String[]{"tbId"}, new String[]{"" + bean.getId()});
                sendPost(Constants.base_url + "/api/club/tb/delete.do", delSpeak, Constants.token);
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
            setBodyParams(new String[]{"tbId", "pn", "ps"}, new String[]{"" + bean.getId(), "" + pn, "" + Constants.default_PS});
            sendPost(Constants.base_url + "/api/club/tb/pgcomment.do", initData, Constants.token);
        } else if (type == 2) {
            setBodyParams(new String[]{"tbId", "pn", "ps"}, new String[]{"" + bean.getId(), "" + pn, "" + Constants.default_PS});
            sendPost(Constants.base_url + "/api/club/tb/pgcomment.do", lodData, Constants.token);
        }

    }

    private void event() {
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        ivBq.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        etComment.setOnClickListener(this);
        //emoji 点击
        gvEmoji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etComment.setText(etComment.getText().toString().trim() +
                        Utils.getEmoji(TjDetailActivity.this, gvEmojiAdapter.getItem(position).toString()));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvDel:
                delSpeakdialog.show();
                break;
            case R.id.tvZan:
                int op;
                if (isZan) {
                    op = 2;
                } else {
                    op = 1;
                }
                setBodyParams(new String[]{"tbId", "op"}, new String[]{"" + speakId, "" + op});
                sendPost(Constants.base_url + "/api/club/tb/like.do", like, Constants.token);
                break;
            case R.id.tvPl:
                if (Constants.isJoin) {
                    Utils.showSoftInput(etComment);
                }
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
                    //锁住RV高度
                    lockContentHeight();
                    Utils.hideSoftInput(etComment);
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
                setBodyParams(new String[]{"tbId", "content"}, new String[]{"" + bean.getId(), "" + comment});
                sendPost(Constants.base_url + "/api/club/tb/comment.do", sendComment, Constants.token);
                break;
        }
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
                        pn = 1;
                        getData(pn, 1);
                        Utils.toastShort(this, result.optString("msg"));
                        EventBus.getDefault().post(new RefTjEvent());
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
            case like:
                switch (result.optInt("code")) {
                    case 0:
                        String username = MeetApplication.getInstance().getSharedPreferences().getString("user", "");
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
                switch (result.optInt("code")) {
                    case 0:
                        //Utils.toastShort(this, result.optString("msg"));
                        pn = 1;
                        getData(pn, 1);
                        Utils.toastShort(this, result.optString("msg"));
                        EventBus.getDefault().post(new RefTjEvent());
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
                        EventBus.getDefault().post(new RefTjEvent());
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
        EventBus.getDefault().post(new RefTjEvent());
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
        Utils.toastShort(this, "网络异常");
    }


}
