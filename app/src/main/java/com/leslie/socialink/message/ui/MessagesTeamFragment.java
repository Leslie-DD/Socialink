package com.leslie.socialink.message.ui;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.leslie.socialink.R;
import com.leslie.socialink.adapter.recycleview.TeamNewsAdapter;
import com.leslie.socialink.base.NetWorkFragment;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.MsgSayBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;


public class MessagesTeamFragment extends NetWorkFragment implements XRecyclerView.LoadingListener, IMessagesFragment {
    private View view;
    private ArrayList<MsgSayBean.SayBean> data;
    private TeamNewsAdapter adapter;
    private XRecyclerView rv;
    private TextView tvTips;

    private final int GetCode = 1000;
    private final int LoadMore = 1001;
    private final int DelMsg = 1002;
    private final int Refuse = 1003;
    private final int Agree = 1004;
    private int type;   // 1 - 刷新  2 加载
    private int pn;    //当前页数
    private int totalPage = 0;   //总页数
    private Gson gson = new Gson();
    private MsgSayBean msgSayBean;
    private AlertDialog deldialog;
    private int delp = 0;

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
        init();
        event();
        return view;
    }

    private void init() {
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        tvTips = view.findViewById(R.id.tvTips);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        pn = 1;
        type = 1;
        data = new ArrayList<>();
        adapter = new TeamNewsAdapter(mContext, data);
        rv.setAdapter(adapter);
        getData(pn, type);
        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("确定要删除这条消息吗？");
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            //删除
            setBodyParams(new String[]{"id"}, new String[]{"" + data.get(delp).getId()});
            sendPostConnection(Constants.BASE_URL + "/api/user/news/clearNews.do", DelMsg, Constants.token);
            deldialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> deldialog.dismiss());
        deldialog = builder.create();
        deldialog.setCancelable(false);
    }

    private void getData(int pn, int type) {
        if (type == 1) {
            setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + pn, "" + Constants.DEFAULT_PS, "" + 2});
            sendPostConnection(Constants.BASE_URL + "/api/user/news/pglist.do", GetCode, Constants.token);
        } else if (type == 2) {
            setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + pn, "" + Constants.DEFAULT_PS, "" + 2});
            sendPostConnection(Constants.BASE_URL + "/api/user/news/pglist.do", LoadMore, Constants.token);
        }

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                type = 1;
                pn = 1;
                getData(pn, type);
                rv.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void refreshData() {
        if (view == null) {
            return;
        }
        type = 1;
        pn = 1;
        getData(pn, type);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pn < totalPage) {
                    pn++;
                    type = 2;
                    getData(pn, type);
                }
                rv.loadMoreComplete();
            }
        }, 1000);
    }

    private void event() {
        rv.setLoadingListener(this);

        adapter.setItemEventListener(new TeamNewsAdapter.ItemEventListener() {
            @Override
            public void onRefuse(int position) {
                //同意  // "id":35,"replyId":2,"receiveId":4,"bizId":42
                setBodyParams(new String[]{"nid", "id", "userId", "op"},
                        new String[]{"" + msgSayBean.getData().get(position).getId(), "" + msgSayBean.getData().get(position).getBizId(), "" + msgSayBean.getData().get(position).getReplyId(), "" + 3});
                sendPostConnection(Constants.BASE_URL + "/api/club/base/apply.do", Agree, Constants.token);
            }

            @Override
            public void onAgree(int position) {
                //同意  // "id":35,"replyId":2,"receiveId":4,"bizId":42
                setBodyParams(new String[]{"nid", "id", "userId", "op"},
                        new String[]{"" + msgSayBean.getData().get(position).getId(), "" + msgSayBean.getData().get(position).getBizId(), "" + msgSayBean.getData().get(position).getReplyId(), "" + 1});
                sendPostConnection(Constants.BASE_URL + "/api/club/base/apply.do", Agree, Constants.token);
            }

            @Override
            public void onDel(int position) {
                //
                delp = position;
                deldialog.show();
            }
        });

    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        Log.e("ddq", result.toString());
        switch (where) {
            case GetCode:
                if (result.optInt("code") == 0) {
                    msgSayBean = gson.fromJson(result.optString("data"), MsgSayBean.class);
                    if (msgSayBean != null) {
                        totalPage = msgSayBean.getTotalPage();
                        data = msgSayBean.getData();
                        adapter.setData(data);
                        tvTips.setVisibility(data.size() > 0 ? View.GONE : View.VISIBLE);
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case LoadMore:
                if (result.optInt("code") == 0) {
                    msgSayBean = gson.fromJson(result.optString("data"), MsgSayBean.class);
                    if (msgSayBean != null) {
                        totalPage = msgSayBean.getTotalPage();
                        data = msgSayBean.getData();
                        adapter.setData2(data);
                    }
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case DelMsg:
                if (result.optInt("code") == 0) {
                    type = 1;
                    pn = 1;
                    getData(pn, type);
                    Utils.toastShort(mContext, "删除成功");
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case Agree:
                if (result.optInt("code") == 0) {
                    type = 1;
                    pn = 1;
                    getData(pn, type);
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
            case Refuse:
                if (result.optInt("code") == 0) {
                    type = 1;
                    pn = 1;
                    getData(pn, type);
                } else {
                    Utils.toastShort(mContext, result.optString("msg"));
                }
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(mContext, "网络异常");
    }
}
