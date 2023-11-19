package com.example.heshequ.activity.statement;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.heshequ.R;
import com.example.heshequ.adapter.listview.AddVoteAdapter;
import com.example.heshequ.adapter.listview.VoteAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.Item;
import com.example.heshequ.entity.RefTDteamEvent;
import com.example.heshequ.entity.VoteBean;
import com.example.heshequ.entity.VoteJsonBean;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.MyLv;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PublishVoteActivity extends NetWorkActivity implements View.OnClickListener {
    private TextView tvCancel, tvSave;
    private EditText etTitle;
    private EditText etContent;
    private ExpandableListView eblv;
    private VoteAdapter adapter;
    private ArrayList<VoteBean.VoteItem> data;
    private View headView;
    private LinearLayout llTime;
    private TextView tvTime;
    private LinearLayout llAddVote;
    private WindowManager.LayoutParams layoutParams;
    private PopupWindow mPop;
    private LinearLayout llHead;
    private EditText etTheme;
    private LinearLayout llType;
    private TextView tvType;
    private MyLv lv;
    private Button btJoin;
    private ArrayList<Item> list;
    private AddVoteAdapter addVoteAdapter;
    private String theme;
    private int cType = 0;
    //时间选择器
    private TimePickerView pvTime;
    private String time;
    private OptionsPickerView pvOptions;
    private ArrayList<String> options;
    private final int postCode = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_vote);
        init();
        event();
        initPop();
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == postCode) {
            if (result.optInt("code") == 0) {
                this.finish();
                EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 1}));
            }
            Utils.toastShort(mContext, result.optString("msg"));
        }
    }

    @Override
    protected void onFailure(String result, int where) {
    }


    private void init() {
        setText("发布投票");
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        headView = LayoutInflater.from(mContext).inflate(R.layout.vote_head, null);
        etTitle = (EditText) headView.findViewById(R.id.etTitle);
        etContent = (EditText) headView.findViewById(R.id.etContent);
        llTime = (LinearLayout) headView.findViewById(R.id.llTime);
        llAddVote = (LinearLayout) findViewById(R.id.llAddVote);
        tvTime = (TextView) headView.findViewById(R.id.tvTime);
        eblv = (ExpandableListView) findViewById(R.id.eblv);
        data = new ArrayList<>();
        adapter = new VoteAdapter(mContext, data);
        eblv.setAdapter(adapter);
        expand();
        eblv.addHeaderView(headView);
        eblv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.set(startDate.get(Calendar.YEAR) + 2, startDate.get(Calendar.MONTH) + 1, startDate.get(Calendar.DAY_OF_MONTH));
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                try {
                    if (Utils.isPastDue(Utils.formatDate(date, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm")) {
                        tvTime.setText(Utils.formatDate(date, "yyyy-MM-dd HH:mm"));
                        time = Utils.formatDate(date, "yyyy-MM-dd HH:mm");
                    } else {
                        Utils.toastShort(mContext, "时间选择有误，请重新选择");
                        tvTime.setText("请选择时间");
                        time = "";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#00BBFF"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#00BBFF"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setRangDate(startDate, endDate)
                .setLabel("-", "-", " ", " :", "", "")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();


        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                tvType.setText(options.get(options1) + "");
                if (tvType.getText().toString().trim().contains("单选")) {
                    cType = 0;
                } else {
                    cType = 1;
                }
                showPop();
            }
        }).setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("投票类型")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#00BBFF"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#00BBFF"))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .build();
        options = new ArrayList<>();
        options.add("单选");
        options.add("多选");
        pvOptions.setPicker(options);
    }

    private void expand() {
        for (int i = 0; i < data.size(); i++) {
            eblv.expandGroup(i);
        }
    }

    private void initPop() {
        mPop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View pv = LayoutInflater.from(mContext).inflate(R.layout.vote_pop, null);
        llHead = (LinearLayout) pv.findViewById(R.id.llHead);
        lv = (MyLv) pv.findViewById(R.id.lv);
        etTheme = (EditText) pv.findViewById(R.id.etTheme);
        llType = (LinearLayout) pv.findViewById(R.id.llType);
        tvType = (TextView) pv.findViewById(R.id.tvType);
        btJoin = (Button) pv.findViewById(R.id.btJoin);
        llHead.setOnClickListener(this);
        llType.setOnClickListener(this);
        btJoin.setOnClickListener(this);
        list = new ArrayList<>();
        list.add(new Item());
        addVoteAdapter = new AddVoteAdapter(mContext, list);
        lv.setAdapter(addVoteAdapter);

        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        mPop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        mPop.setOutsideTouchable(true);
        // 设置焦点
        mPop.setFocusable(true);
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
            }
        });
        // 设置所在布局
        mPop.setContentView(pv);
    }

    private void event() {
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        llAddVote.setOnClickListener(this);
        tvTime.setOnClickListener(this);

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 32) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                this.finish();
                break;
            case R.id.llAddVote:
                showPop();
                break;
            case R.id.btJoin://加入投票
                theme = etTheme.getText().toString();
                if (theme.length() == 0) {
                    Utils.toastShort(mContext, "请先输入投票主题！");
                    return;
                }
                if (list.size() == 1) {
                    Utils.toastShort(mContext, "请先添加选项！");
                    return;
                }

                if (list.size() < 3) {
                    Utils.toastShort(mContext, "请至少添加两个选项！");
                    return;
                }

                for (int i = 0; i < list.size() - 1; i++) {
                    if (TextUtils.isEmpty(list.get(i).getName())) {
                        Utils.toastShort(mContext, "请先设置选项内容！");
                        return;
                    }
                }


                VoteBean bean = new VoteBean();
                VoteBean.VoteItem voteItem = bean.new VoteItem();
                voteItem.setTheme(theme);
                voteItem.setType(cType);
                list.remove(list.size() - 1);
                voteItem.setData(list);
                data.add(voteItem);
                adapter.notifyDataSetChanged();
                expand();
                mPop.dismiss();
                resetPop();

                break;
            case R.id.llType://选择类型
                mPop.dismiss();
                pvOptions.show();
                break;
            case R.id.llHead://选择类型
                mPop.dismiss();
                break;
            case R.id.tvSave:
                String title = etTitle.getText().toString();
                String introduction = etContent.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Utils.toastShort(mContext, "标题不能为空");
                    return;
                }
                if (TextUtils.isEmpty(introduction)) {
                    Utils.toastShort(mContext, "内容不能为空");
                    return;
                }
                if (TextUtils.isEmpty(time)) {
                    Utils.toastShort(mContext, "请选择截止时间");
                    pvTime.show();
                    return;
                }

                //data.get(i).getTheme()
                //data.get(i).getData().get(y)

                if (data.size() < 1) {
                    Utils.toastShort(mContext, "请先添加投票");
                    return;
                }

                ArrayList<VoteJsonBean> voteBeans = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    VoteJsonBean bean1 = new VoteJsonBean();
                    bean1.setCategory(data.get(i).getType());
                    bean1.setContent(data.get(i).getTheme());
                    ArrayList<VoteJsonBean.Option> options = new ArrayList<>();
                    for (int j = 0; j < data.get(i).getData().size(); j++) {
                        VoteJsonBean.Option option = new VoteJsonBean.Option();
                        option.setContent(data.get(i).getData().get(j).getName());
                        options.add(option);
                    }
                    bean1.setOptions(options);
                    voteBeans.add(bean1);
                }
                String content = new Gson().toJson(voteBeans);
                setBodyParams(new String[]{"clubId", "name", "introduction", "deadline", "content"},
                        new String[]{"" + Constants.clubId, title, introduction, time, content});
                sendPost(Constants.base_url + "/api/club/vote/save.do", postCode, Constants.token);

                break;
            case R.id.tvTime:
                pvTime.show();
                Utils.hideSoftInput(this);
                break;
        }
    }

    private void showPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        mPop.showAtLocation(llAddVote, Gravity.BOTTOM, 0, 0);
    }

    private void resetPop() {
        etTheme.setText("");
        list = new ArrayList<>();
        list.add(new Item());
        addVoteAdapter.setData(list);
        cType = 0;
        tvType.setText("单选");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPop.isShowing()) {
            mPop.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }
}
