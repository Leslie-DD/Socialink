package com.hnu.heshequ.activity.statement;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.RefTDteamEvent;
import com.hnu.heshequ.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class BullertinDetailActivity extends NetWorkActivity implements View.OnClickListener {
    private TextView tvTitle, tv_title, tvContent, tvInitiator, tvTime;
    private ImageView ivBack, ivRight;
    private String title, content, initiator, time;
    private int id;
    private int clubId;
    //pop
    private PopupWindow pop;
    private WindowManager.LayoutParams layoutParams;
    private LinearLayout ll_editor, ll_del, ll_cacel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bullertin_detail);
        init();
        initPop();
        event();
    }


    private void init() {
        id = getIntent().getIntExtra("id", 0);
        clubId = getIntent().getIntExtra("clubId", 0);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        initiator = getIntent().getStringExtra("initiator");
        time = getIntent().getStringExtra("time");
        Log.e("DDQ", id + "," + clubId + "," + title + "," + content + ",");
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("公告详情");
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        ivRight.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.more3));
        tv_title = (TextView) findViewById(R.id.tv_Title);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvInitiator = (TextView) findViewById(R.id.tv_initiator);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tv_title.setText(title);
        tvContent.setText(content);
        tvInitiator.setText("发布人：" + initiator);
        tvTime.setText("发布时间：" + time);
        if (Constants.isAdmin) {
            ivRight.setVisibility(View.VISIBLE);
        } else {
            ivRight.setVisibility(View.GONE);
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

    private void event() {
        ivBack.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivRight:
                // shou pop
                showSpvPop();
                break;
            case R.id.ll_editor:
                //编辑
                startActivityForResult(new Intent(this, EditorialBulletinActivity.class)
                                .putExtra("type", 2)
                                .putExtra("title", title)
                                .putExtra("content", content)
                                .putExtra("id", id)
                        , 1);
                pop.dismiss();
                break;
            case R.id.ll_del:
                //删除
                setBodyParams(new String[]{"id"}, new String[]{"" + id});
                sendPost(Constants.base_url + "/api/club/notice/delete.do", 1000, Constants.token);
                break;
            case R.id.ll_cacel:
                pop.dismiss();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refBulletin(RefTDteamEvent event) {
        for (int i : event.getRef()) {
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == 1000) {
            if (result.optInt("code") == 0) {
                Utils.toastShort(this, result.optString("msg"));
            }
            EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 3}, true));
            this.finish();
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "网络异常");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle b = data.getExtras();
            tv_title.setText(b.getString("title"));
            tvContent.setText(b.getString("content"));
        }
    }


}
