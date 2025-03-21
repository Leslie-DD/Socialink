package com.leslie.socialink.activity.team;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.Adapter_GridView;
import com.leslie.socialink.adapter.recycleview.CommentAdapter;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.constans.P;
import com.leslie.socialink.entity.CommentBean;
import com.leslie.socialink.entity.StatementBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.view.CircleView;
import com.leslie.socialink.view.MyGv;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SayDetailActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private View headView;
    private XRecyclerView rv;
    private ArrayList<CommentBean> data;
    private CommentAdapter commentAdapter;
    private TextView tvTitle;
    private EditText etComment;
    private LinearLayout llComment;
    private ImageView ivBq, ivSend;
    private StatementBean bean;
    private TextView tvName, tvTeamName, tvTime, tvTt, tvContent;
    private CircleView ivHead;
    private MyGv gv;
    private AlertDialog deldialog;
    private int delid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_say_detail);
        bean = (StatementBean) getIntent().getSerializableExtra("bean");
        init();
        event();
    }

    private void init() {
        headView = LayoutInflater.from(mContext).inflate(R.layout.say_head, null);
        if (bean != null) {
            setUi();
        }
        llComment = findViewById(R.id.llComment);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("他们说");
        rv = (XRecyclerView) findViewById(R.id.rv);
        ConsTants.initXRecycleView(context, true, true, rv);
        rv.setLoadingListener(this);
        rv.addHeaderView(headView);
        getData();
        commentAdapter = new CommentAdapter(mContext, data);
        rv.setAdapter(commentAdapter);
        ivBq = (ImageView) findViewById(R.id.ivBq);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        etComment = (EditText) findViewById(R.id.etComment);
        if (!Constants.isJoin) {
            llComment.setVisibility(View.GONE);
        }
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("要删除这条回复吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除
                /*setBodyParams(new String[]{"id"}, new String[]{"" + delid});
                sendPost(Constants.base_url + "/api/club/activity/delcomment.do", delComment, Constants.token);*/
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
        if (TextUtils.isEmpty(bean.getHeader())) {
            ivHead.setImageResource(R.mipmap.head3);
        } else {
            Glide.with(context).load(Constants.BASE_URL + bean.getHeader()).asBitmap().into(ivHead);
        }

        if (bean.getImgs() == null || bean.getImgs().size() == 0) {
            gv.setVisibility(View.GONE);
        } else {
            gv.setVisibility(View.VISIBLE);
            switch (bean.getImgs().size()) {
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
            gv.setAdapter(new Adapter_GridView(context, bean.getImgs()));
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    intent.putStringArrayListExtra("imageList", bean.getImgs());
                    intent.putExtra(P.START_ITEM_POSITION, i);
                    intent.putExtra(P.START_IAMGE_POSITION, i);
                    intent.putExtra("isdel2", false);
//                ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation(mContext, imageView, imageView.getTransitionName());
                    context.startActivity(intent);
                }
            });
        }
    }

    private void getData() {
        data = new ArrayList<>();
    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


}
