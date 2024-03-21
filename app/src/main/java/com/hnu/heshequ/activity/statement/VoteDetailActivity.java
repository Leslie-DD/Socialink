package com.hnu.heshequ.activity.statement;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.listview.VoteAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.Item;
import com.hnu.heshequ.entity.RefTDteamEvent;
import com.hnu.heshequ.entity.TeamTestBean;
import com.hnu.heshequ.entity.VoteBean;
import com.hnu.heshequ.entity.VoteDataBean;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CircleView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class VoteDetailActivity extends NetWorkActivity {
    private int id;  //
    private TeamTestBean.ObjBean bean;
    private int isVote = -1;  // 0 - 未投票  1 - 已投票
    private int category = -1;  // 0 - 单选 ；  1 - 多选
    private ExpandableListView eblv;
    private VoteAdapter adapter;
    private ArrayList<VoteBean.VoteItem> data;
    private View headView;
    private View footView;
    private CircleView ivHead;
    private TextView tvName, tvTime, tvTitlt, tvContent, tvEndtime;
    private Button btStatus, btVote;
    private final int getData = 1000;
    private final int DelCode = 1001;
    private final int ovteCode = 1002;
    private VoteDataBean votes;
    private ImageView ivRight;

    //pop
    private PopupWindow pop;
    private WindowManager.LayoutParams layoutParams;
    private LinearLayout ll_editor, ll_del, ll_cacel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail);
        init();
        initPop();
        event();
    }

    private void init() {
        setText("投票详情");
        id = getIntent().getIntExtra("id", 0);
        bean = (TeamTestBean.ObjBean) getIntent().getSerializableExtra("bean");
        headView = LayoutInflater.from(mContext).inflate(R.layout.votedetailhead, null);
        footView = LayoutInflater.from(mContext).inflate(R.layout.votedetailfoot, null);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        btVote = (Button) footView.findViewById(R.id.btVote);
        ivHead = (CircleView) headView.findViewById(R.id.ivHead);
        tvName = (TextView) headView.findViewById(R.id.tvName);
        tvTime = (TextView) headView.findViewById(R.id.tvTime);
        tvTitlt = (TextView) headView.findViewById(R.id.tvVoteTitle);
        btStatus = (Button) headView.findViewById(R.id.btStatus);
        tvContent = (TextView) headView.findViewById(R.id.tvContent);
        tvEndtime = (TextView) headView.findViewById(R.id.tvEndTime);
        eblv = (ExpandableListView) findViewById(R.id.eblv);
        ivRight.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.more3));
        eblv.addHeaderView(headView);
        eblv.addFooterView(footView);
        if (bean != null) {
            setHeadUi(bean);
        }
        getData();

    }

    private void initPop() {
        //设置pop
        pop = new PopupWindow(260, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View spv = LayoutInflater.from(mContext).inflate(R.layout.pop_bullertin_detaol, null);
        //init event
        ll_editor = (LinearLayout) spv.findViewById(R.id.ll_editor);
        ll_editor.setVisibility(View.GONE);
        ll_del = (LinearLayout) spv.findViewById(R.id.ll_del);
        ll_cacel = (LinearLayout) spv.findViewById(R.id.ll_cacel);
        //ll_editor.setOnClickListener(this);
        ll_del.setOnClickListener(v -> {
            setBodyParams(new String[]{"voteId"}, new String[]{"" + bean.getId()});
            sendPost(Constants.base_url + "/api/club/vote/delete.do", DelCode, Constants.token);
        });
        ll_cacel.setOnClickListener(v -> pop.dismiss());

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

    private void getData() {
        setBodyParams(new String[]{"voteId"}, new String[]{"" + id});
        sendPost(Constants.base_url + "/api/club/vote/detail.do", getData, Constants.token);
    }

    private void setHeadUi(TeamTestBean.ObjBean objBean) {
        Glide.with(this).load(Constants.base_url + objBean.getHeader()).asBitmap().into(ivHead);
        tvName.setText(objBean.getPresentorName());
        tvTitlt.setText(objBean.getName());
        tvContent.setText(objBean.getIntroduction());
        tvEndtime.setText("截止时间：" + objBean.getDeadline());
        tvTime.setText(objBean.getTime());
        //isVote = objBean.getIsVote();
        category = objBean.getCategory();
        try {
            if (Utils.isPastDue(objBean.getDeadline(), "yyyy-MM-dd HH:mm")) {
                btStatus.setText("进行中");
                btStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.ing_bg));
            } else {
                btStatus.setText("已结束");
                btStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.end_bg));
                btVote.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setVoteData(VoteDataBean votes) {
        isVote = votes.getIsVote();
        if (isVote == 1) {
            btVote.setVisibility(View.INVISIBLE);
        }
        data = new ArrayList<>();
        for (VoteDataBean.QuestionsBean questionsBean : votes.getQuestions()) {
            VoteBean bean = new VoteBean();
            ArrayList<Item> list = new ArrayList<>();
            VoteBean.VoteItem item = bean.new VoteItem();
            item.setVoteId(questionsBean.getVoteId());
            item.setType(questionsBean.getCategory());
            item.setTheme(questionsBean.getContent());
            for (VoteDataBean.QuestionsBean.OptionsBean optionsBean : questionsBean.getOptions()) {
                int total = 0;
                boolean isfrist = true;
                if (isfrist) {
                    for (VoteDataBean.QuestionsBean.OptionsBean optionsBean1 : questionsBean.getOptions()) {
                        total = total + optionsBean1.getVoteAmount();
                    }
                    isfrist = false;
                }
                Item item1 = new Item();
                item1.setId(optionsBean.getId());
                item1.setCount(optionsBean.getVoteAmount());
                item1.setTotal(total);
                item1.setName(optionsBean.getContent());
                list.add(item1);
            }
            item.setData(list);
            data.add(item);
            adapter = new VoteAdapter(mContext, data);
            adapter.setType(isVote + 1);
            eblv.setAdapter(adapter);
            for (int i = 0; i < data.size(); i++) {
                eblv.expandGroup(i);
            }
        }

    }

    private void event() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        btVote.setOnClickListener(v -> {
            int voteId = 0;
            StringBuilder optionId = new StringBuilder();
            for (VoteBean.VoteItem voteItem : adapter.getData()) {
                voteId = voteItem.getVoteId();
                if (voteItem.getType() == 0) {  //单选
                    for (Item item : voteItem.getData()) {
                        if (item.getStatus() == 1) {
                            if (optionId.length() == 0) {
                                optionId = new StringBuilder(item.getId() + "");
                            } else {
                                optionId.append(",").append(item.getId());
                            }
                        }
                    }
                } else if (voteItem.getType() == 1) {  //多选
                    for (Item item : voteItem.getData()) {
                        if (item.getStatus() == 1) {
                            if (optionId.length() == 0) {
                                optionId = new StringBuilder(item.getId() + "");
                            } else {
                                optionId.append(",").append(item.getId());
                            }
                        }
                    }
                }
            }
            if (voteId == 0 || optionId.length() == 0) {
                Utils.toastShort(mContext, "请选择投票后再进行提交");
                return;
            }
            setBodyParams(new String[]{"voteId", "optionId"}, new String[]{"" + voteId, "" + optionId});
            sendPost(Constants.base_url + "/api/club/vote/comment.do", ovteCode, Constants.token);
        });
        ivRight.setOnClickListener(v -> showSpvPop());
        eblv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == getData) {
            if (result.optInt("code") == 0) {
                Gson gs = new Gson();
                votes = gs.fromJson(result.optString("data"), VoteDataBean.class);
                if (votes != null) {
                    setVoteData(votes);
                }
            }
        } else if (where == DelCode) {
            if (result.optInt("code") == 0) {
                EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 1}, true));
                this.finish();
            }
            Utils.toastShort(mContext, result.optString("msg"));
        } else if (where == ovteCode) {
            if (result.optInt("code") == 0) {
                //EventBus.getDefault().post(new RefTDteamEvent(new int[]{0,1}));
                //this.finish();
                getData();
                // 投票成功  == V
                //adapter.setType(2);
                EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 1}));
                //this.finish();
            }
            Utils.toastShort(mContext, result.optString("msg"));
        }

    }


    @Override
    protected void onFailure(String result, int where) {

    }

}
