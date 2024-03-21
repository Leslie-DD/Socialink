package com.hnu.heshequ.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.TeamMembersActivity;
import com.hnu.heshequ.activity.team.TeamDetailActivity;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.TeamBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.RefStatementEvent;
import com.hnu.heshequ.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

/**
 * Created by dev06 on 2018/5/15.
 */
public class ManagerFragment extends NetWorkFragment implements View.OnClickListener {

    private View view;
    private TextView tvTip, tvActivityTip, tvNoticeTip, tvVoteTip, tvNoAdmin, tvComment;
    private ImageView ivStatus, ivActivity, ivNotice, ivVote, ivComment;
    private LinearLayout llManager, llAdmin;
    private int s1, s2 = 1, s3 = 1, s4 = 1, s5 = 1;
    private TeamDetailActivity mActivity;
    private TeamBean cBean;
    private int status;
    private final int getCode = 1000;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (where == getCode) {
            switch (status) {
                case 0:
                    cBean.setSettingSpeak(Math.abs(cBean.getSettingSpeak() - 1));
                    EventBus.getDefault().post(new RefStatementEvent());
                    break;
                case 1:
                    cBean.setSettingActivity(Math.abs(cBean.getSettingActivity() - 1));
                    break;
                case 2:
                    cBean.setSettingNotice(Math.abs(cBean.getSettingNotice() - 1));
                    break;
                case 3:
                    cBean.setSettingVote(Math.abs(cBean.getSettingVote() - 1));
                    break;
                case 4:
                    cBean.setSettingSpeakComment(Math.abs(cBean.getSettingSpeakComment() - 1));
            }
            setUi();
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "操作失败,请重试");
    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.managerfragment, null);
        EventBus.getDefault().register(this);
        init();
        event();

        Log.e("token: ", Constants.token);
        return view;
    }

    private void init() {
        llAdmin = (LinearLayout) view.findViewById(R.id.llAdmin);
        tvNoAdmin = view.findViewById(R.id.tvNoAdmin);
        tvTip = (TextView) view.findViewById(R.id.tvTip);
        tvActivityTip = (TextView) view.findViewById(R.id.tvActivityTip);
        tvNoticeTip = (TextView) view.findViewById(R.id.tvNoticeTip);
        tvVoteTip = (TextView) view.findViewById(R.id.tvVoteTip);
        tvComment = (TextView) view.findViewById(R.id.tvComment);
        mActivity = (TeamDetailActivity) getActivity();
        ivStatus = (ImageView) view.findViewById(R.id.ivStatus);
        ivActivity = (ImageView) view.findViewById(R.id.ivActivity);
        ivNotice = (ImageView) view.findViewById(R.id.ivNotice);
        ivVote = (ImageView) view.findViewById(R.id.ivVote);
        ivComment = (ImageView) view.findViewById(R.id.ivComment);
        llManager = (LinearLayout) view.findViewById(R.id.llManager);
        setUi();

    }

    public void setBean(TeamBean bean) {
        cBean = bean;
        setUi();
    }

    private void setUi() {
        if (cBean == null) {
            return;
        }
        if (view == null) {
            return;
        }
        ivStatus.setImageResource(cBean.getSettingSpeak() == 1 ? R.mipmap.guanbi : R.mipmap.dakai);
        tvTip.setText(cBean.getSettingSpeak() == 1 ? "不可见" : "可见");
        ivComment.setImageResource(cBean.getSettingSpeakComment() == 1 ? R.mipmap.guanbi : R.mipmap.dakai);
        tvComment.setText(cBean.getSettingSpeakComment() == 1 ? "不可评论" : "可评论");
        ivVote.setImageResource(cBean.getSettingVote() == 1 ? R.mipmap.guanbi : R.mipmap.dakai);
        tvVoteTip.setText(cBean.getSettingVote() == 1 ? "不可见" : "可见");
        ivActivity.setImageResource(cBean.getSettingActivity() == 1 ? R.mipmap.guanbi : R.mipmap.dakai);
        tvActivityTip.setText(cBean.getSettingActivity() == 1 ? "不可见" : "可见");
        ivNotice.setImageResource(cBean.getSettingNotice() == 1 ? R.mipmap.guanbi : R.mipmap.dakai);
        tvNoticeTip.setText(cBean.getSettingNotice() == 1 ? "不可见" : "可见");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void oncklsjadfjkhdagljn(TeamBean event) {
        if (Constants.isAdmin) {
            llAdmin.setVisibility(View.VISIBLE);
            tvNoAdmin.setVisibility(View.INVISIBLE);
        } else {
            tvNoAdmin.setVisibility(View.VISIBLE);
            llAdmin.setVisibility(View.INVISIBLE);
            llManager.setVisibility(View.INVISIBLE);
        }
    }

    private void event() {
        ivStatus.setOnClickListener(this);
        ivActivity.setOnClickListener(this);
        ivNotice.setOnClickListener(this);
        ivVote.setOnClickListener(this);
        ivComment.setOnClickListener(this);
        llManager.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivStatus:
                sendPost(0);
                break;
            case R.id.ivVote:
                sendPost(3);
                break;
            case R.id.ivActivity:
                sendPost(1);
                break;
            case R.id.ivNotice:
                sendPost(2);
                break;
            case R.id.ivComment://团言评论权限
                sendPost(4);
                break;
            case R.id.llManager:
                startActivity(new Intent(mContext, TeamMembersActivity.class)
                        .putExtra("id", mActivity.id));
                break;
        }
    }

    private void sendPost(int type) {
        if (cBean == null) {
            return;
        }
        this.status = type;
        switch (type) {
            case 0: //团言可见
                setBodyParams(new String[]{"id", "settingSpeak"}, new String[]{mActivity.id + "", Math.abs(cBean.getSettingSpeak() - 1) + ""});
                break;
            case 1: //活动
                setBodyParams(new String[]{"id", "settingActivity"}, new String[]{mActivity.id + "", Math.abs(cBean.getSettingActivity() - 1) + ""});
                break;
            case 2: //公告
                setBodyParams(new String[]{"id", "settingNotice"}, new String[]{mActivity.id + "", Math.abs(cBean.getSettingNotice() - 1) + ""});
                break;
            case 3: //投票
                setBodyParams(new String[]{"id", "settingVote"}, new String[]{mActivity.id + "", Math.abs(cBean.getSettingVote() - 1) + ""});
                break;
            case 4://团言评论
                setBodyParams(new String[]{"id", "settingSpeakComment"}, new String[]{mActivity.id + " ", Math.abs(cBean.getSettingSpeakComment() - 1) + ""});

        }
        sendPostConnection(Constants.base_url + "/api/club/base/setvisibility.do", getCode, Constants.token);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
