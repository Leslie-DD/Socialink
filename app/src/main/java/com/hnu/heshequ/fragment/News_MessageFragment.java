package com.hnu.heshequ.fragment;

import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.recycleview.SayNewsAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.MsgSayBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 佳佳 on 2018/11/25.
 */

//私聊消息列表
public class News_MessageFragment extends NetWorkFragment implements XRecyclerView.LoadingListener {
    private View view;
    private ArrayList<MsgSayBean.SayBean> data;
    private SayNewsAdapter adapter;
    private SayNewsAdapter adapter2;
    private XRecyclerView rv;
    private TextView tvTips;

    private final int DeleteMessage = 999;
    private final int GetCode = 1000;
    private final int LoadMore = 1001;
    private final int DelMsg = 1002;
    private int type;   // 1 - 刷新  2 加载
    private int pn;    //当前页数
    private int totalPage = 0;   //总页数
    private Gson gson = new Gson();
    private MsgSayBean msgSayBean;

    private AlertDialog deldialog;
    private int delp = 0;


    @Override
    //初始化
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.only_rv_item, null);
        init();
        event();
        return view;
    }

    //控件初始化，设置删除的监听器
    private void init() {
        rv = (XRecyclerView) view.findViewById(R.id.rv);
        tvTips = view.findViewById(R.id.tvTips);
        ConsTants.initXRecycleView(mContext, true, true, rv);
        pn = 1;
        type = 1;
        getData(pn, type);
        data = new ArrayList<>();
        adapter = new SayNewsAdapter(mContext, data, 3);
        rv.setAdapter(adapter);
        initDialog();
    }

    //删除时弹窗
    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("确定要删除这条消息吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除
                setBodyParams(new String[]{"chatBoxId"}, new String[]{"" + data.get(delp).getId()});
                sendPostConnection(WenConstans.DeleteMessageBox, DelMsg, Constants.token);
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

    //type=1 更新
    //type=2 加载
    private void getData(int pn, int type) {
        if (type == 1) {
            setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + pn, "" + Constants.default_PS, "" + 3});
            sendPostConnection(Constants.base_url + "/api/chat/base/myChatBox.do", GetCode, Constants.token);
        } else if (type == 2) {
            setBodyParams(new String[]{"pn", "ps", "type"}, new String[]{"" + pn, "" + Constants.default_PS, "" + 3});
            sendPostConnection(Constants.base_url + "/api/chat/base/myChatBox.do", LoadMore, Constants.token);
        }

    }

    @Override
    //刷新
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

    public void refData() {
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

    //返回按钮监听器
    private void event() {
        rv.setLoadingListener(this);

        adapter.setListener(new SayNewsAdapter.ItemDelListener() {
            @Override
            public void onDel(int position) {
                //删除
                delp = position;
                deldialog.show();
            }
        });
    }

    @Override
    //网络请求成功后，数据添加到组件
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
                //删除操作，实体提醒
                if (result.optInt("code") == 0) {
                    type = 1;
                    pn = 1;
                    getData(pn, type);
                    Utils.toastShort(mContext, "删除成功");
                    onRefresh();
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
