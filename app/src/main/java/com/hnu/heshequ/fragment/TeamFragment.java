package com.hnu.heshequ.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.TeamDetailActivity;
import com.hnu.heshequ.adapter.recycleview.TeamAdapter;
import com.hnu.heshequ.base.NetWorkFragment;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.TeamBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.RefTDteamEvent;
import com.hnu.heshequ.entity.TeamTestBean;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.MyXRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev06 on 2018/5/15.
 */
public class TeamFragment extends NetWorkFragment implements View.OnClickListener {
    private MyXRecyclerView lv;
    private TextView tvTips;
    private TextView tvNew, tvVote, tvActivity, tvNotice;
    private RelativeLayout rlNew, rlVote, rlActivity, rlNotice;
    private View ivNew, ivVote, ivActivity, ivNotice;
    private ArrayList<Integer> indexs;
    private ArrayList<Integer> totalPages;
    private ArrayList<ArrayList<TeamTestBean>> datas;
    private TeamDetailActivity mActivity;
    private int startY, endY;
    private int status = -1;
    private TeamAdapter adapter;
    private ArrayList<TeamTestBean> data;
    private View view;
    private ArrayList<TeamTestBean> voteData, activityData, noticeData;
    private boolean isrefresh;
    private final int DelCode = 1000;
    private final int ApplyCode = 1001;
    private boolean loadmore;

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        if (where == DelCode) {
            if (result.optInt("code") == 0) {
                EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 2}));
                Utils.toastShort(mContext, "删除成功");
            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        } else if (where == ApplyCode) {
            if (result.optInt("code") == 0) {
                EventBus.getDefault().post(new RefTDteamEvent(new int[]{0, 2}));
                Utils.toastShort(mContext, "报名成功");
            } else {
                Utils.toastShort(mContext, result.optString("msg"));
            }
        } else {
            Log.e("YSF", "测试的四句：" + where + "&&" + result.toString());
            int code = result.optInt("code");
            if (code != 0) {
                Utils.toastShort(mContext, result.optString("msg"));
                return;
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.optString("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject == null) {
                return;
            }
            if (isrefresh) {
                if (mActivity != null) {
                    mActivity.setFinish(0);
                }
//                lv.refreshComplete();
            } else {
                if (loadmore) {
                    loadmore = false;
                    if (mActivity != null) {
                        mActivity.setFinish(1);
                    }
                }
//                lv.loadMoreComplete();
            }
            Gson gs = new Gson();
            totalPages.set(where, jsonObject.optInt("totalPage"));
            if (indexs.get(where) == 1) {
                datas.set(where, new ArrayList<TeamTestBean>());
            }


            switch (where) {
                case 0:
                    ArrayList<TeamTestBean> list = gs.fromJson(jsonObject.optString("list"), new TypeToken<List<TeamTestBean>>() {
                    }.getType());
                    if (list != null) {
                        datas.get(where).addAll(list);
                    }
                    break;
                case 1:
                    ArrayList<TeamTestBean.ObjBean> votes = gs.fromJson(jsonObject.optString("list"), new TypeToken<List<TeamTestBean.ObjBean>>() {
                    }.getType());
                    if (votes != null) {
                        list = new ArrayList<>();
                        for (int i = 0; i < votes.size(); i++) {
                            TeamTestBean.ObjBean obj = votes.get(i);
                            TeamTestBean tt = new TeamTestBean();
                            tt.setObj(obj);
                            tt.setId(obj.getId());
                            tt.setType(1);
                            list.add(tt);
                        }
                        datas.get(where).addAll(list);
                    }
                    break;
                case 2:
                    votes = gs.fromJson(jsonObject.optString("list"), new TypeToken<List<TeamTestBean.ObjBean>>() {
                    }.getType());
                    if (votes != null) {
                        list = new ArrayList<>();
                        for (int i = 0; i < votes.size(); i++) {
                            TeamTestBean.ObjBean obj = votes.get(i);
                            TeamTestBean tt = new TeamTestBean();
                            tt.setObj(obj);
                            tt.setId(obj.getId());
                            tt.setType(2);
                            list.add(tt);
                        }
                        datas.get(where).addAll(list);
                    }
                    break;
                case 3:
                    votes = gs.fromJson(jsonObject.optString("list"), new TypeToken<List<TeamTestBean.ObjBean>>() {
                    }.getType());
                    if (votes != null) {
                        list = new ArrayList<>();
                        for (int i = 0; i < votes.size(); i++) {
                            TeamTestBean.ObjBean obj = votes.get(i);
                            TeamTestBean tt = new TeamTestBean();
                            tt.setObj(obj);
                            tt.setId(obj.getId());
                            tt.setType(3);
                            list.add(tt);
                        }
                        datas.get(where).addAll(list);
                    }
                    break;

            }

            if (where == status) {
                adapter.setData(datas.get(where));
                tvTips.setVisibility(datas.get(where).size() > 0 ? View.GONE : View.VISIBLE);
            }
        }

    }

    @Override
    protected void onFailure(String result, int where) {
        if (isrefresh) {
            if (mActivity != null) {
                mActivity.setFinish(0);
            }
//                lv.refreshComplete();
        } else {
            if (loadmore) {
                loadmore = false;
                if (mActivity != null) {
                    mActivity.setFinish(1);
                }
            }
//                lv.loadMoreComplete();
        }
        Utils.toastShort(mContext, "请求失败" + result);
    }

    @Override
    protected View createView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.teamfragment, null);
        mActivity = (TeamDetailActivity) getActivity();
        EventBus.getDefault().register(this);
        init();
        event();
        return view;
    }


    private void init() {
        lv = (MyXRecyclerView) view.findViewById(R.id.lv);
        tvTips = view.findViewById(R.id.tvTips);
        ConsTants.initXRecycleView(getActivity(), false, false, lv);
        lv.setNestedScrollingEnabled(false);
        tvNew = (TextView) view.findViewById(R.id.tvNew);
        tvVote = (TextView) view.findViewById(R.id.tvVote);
        tvActivity = (TextView) view.findViewById(R.id.tvActivity);
        tvNotice = (TextView) view.findViewById(R.id.tvNotice);

        ivNew = view.findViewById(R.id.ivNew);
        ivVote = view.findViewById(R.id.ivVote);
        ivActivity = view.findViewById(R.id.ivActivity);
        ivNotice = view.findViewById(R.id.ivNotice);

        rlNew = (RelativeLayout) view.findViewById(R.id.rlNew);
        rlVote = (RelativeLayout) view.findViewById(R.id.rlVote);
        rlActivity = (RelativeLayout) view.findViewById(R.id.rlActivity);
        rlNotice = (RelativeLayout) view.findViewById(R.id.rlNotice);

        ivNew.setVisibility(View.GONE);
        ivVote.setVisibility(View.GONE);
        ivActivity.setVisibility(View.GONE);
        ivNotice.setVisibility(View.GONE);
        rlActivity.setOnClickListener(this);
        rlNew.setOnClickListener(this);
        rlNotice.setOnClickListener(this);
        rlVote.setOnClickListener(this);
        data = new ArrayList<>();
        voteData = new ArrayList<>();
        activityData = new ArrayList<>();
        noticeData = new ArrayList<>();
        indexs = new ArrayList<>();
        datas = new ArrayList<>();
        totalPages = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            indexs.add(1);
            totalPages.add(1);
            datas.add(new ArrayList<TeamTestBean>());
        }
        adapter = new TeamAdapter(mContext);
        lv.setAdapter(adapter);

        setBg(0);
        for (int i = 0; i < 4; i++) {
            getData(i);
        }
//        getData(1);
    }

    private void getData(int status) {
        switch (status) {
            case 0:
                setBodyParams(new String[]{"id", "pn", "ps"}, new String[]{mActivity.id + "", indexs.get(status) + "", 2 + ""});
                sendPostConnection(Constants.base_url + "/api/club/release/pglist.do"
                        , status, Constants.token);
                break;
            case 1:
                setBodyParams(new String[]{"id", "pn", "ps"}, new String[]{mActivity.id + "", indexs.get(status) + "", Constants.default_PS + ""});
                sendPostConnection(Constants.base_url + "/api/club/vote/pglist.do"
                        , status, Constants.token);
                break;
            case 2:
                setBodyParams(new String[]{"id", "pn", "ps"}, new String[]{mActivity.id + "", indexs.get(status) + "", Constants.default_PS + ""});
                sendPostConnection(Constants.base_url + "/api/club/activity/pglist.do"
                        , status, Constants.token);
                break;
            case 3:
                setBodyParams(new String[]{"id", "pn", "ps"}, new String[]{mActivity.id + "", indexs.get(status) + "", Constants.default_PS + ""});
                sendPostConnection(Constants.base_url + "/api/club/notice/pglist.do"
                        , status, Constants.token);
                break;
        }

    }


    private void event() {
        adapter.setOnDelListener(new TeamAdapter.OnDelListener() {
            @Override
            public void del(int position, int id) {
                // 活动删除
                setBodyParams(new String[]{"id"}, new String[]{"" + id});
                sendPostConnection(Constants.base_url + "/api/club/activity/delete.do",
                        DelCode, Constants.token);
            }
        });

        adapter.setOnapplyListener(new TeamAdapter.OnapplyListener() {
            @Override
            public void apply(int position, int id, int status) {
                // 报名 或者 取消
                setBodyParams(new String[]{"id", "op"}, new String[]{"" + id, "" + 1});
                sendPostConnection(Constants.base_url + "/api/club/activity/apply.do",
                        ApplyCode, Constants.token);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlNew:
                setBg(0);
                break;
            case R.id.rlVote:
                setBg(1);
                break;
            case R.id.rlActivity:
                setBg(2);
                break;
            case R.id.rlNotice:
                setBg(3);
                break;
        }
    }

    private void setBg(int status) {
        if (status == this.status) {
            return;
        }
        this.status = status;
        tvNew.setTextColor(status == 0 ? Color.parseColor("#FAC23E") : Color.parseColor("#333333"));
        if (status == 0) {
            ivNew.setVisibility(View.GONE);
        }
        tvVote.setTextColor(status == 1 ? Color.parseColor("#FAC23E") : Color.parseColor("#333333"));
        if (status == 1) {
            ivVote.setVisibility(View.GONE);
        }
        tvActivity.setTextColor(status == 2 ? Color.parseColor("#FAC23E") : Color.parseColor("#333333"));
        if (status == 2) {
            ivActivity.setVisibility(View.GONE);
        }
        tvNotice.setTextColor(status == 3 ? Color.parseColor("#FAC23E") : Color.parseColor("#333333"));
        if (status == 3) {
            ivNotice.setVisibility(View.GONE);
        }
        if (adapter != null) {
            adapter.setData(datas.get(status));
            tvTips.setVisibility(datas.get(status).size() > 0 ? View.GONE : View.VISIBLE);
        }
    }


//    @Override
//    public void onRefresh() {
//        Log.e("YSF", "我触发了onRefresh" + status);
//        isrefresh = true;
//        indexs.set(status, 1);
//        getData(status);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refBulletin(RefTDteamEvent event) {
        for (int i : event.getRef()) {
            if (!event.isDel()) {
                if (status == 0) {
                    ivNew.setVisibility(View.VISIBLE);
                } else if (status == 1) {
                    ivVote.setVisibility(View.VISIBLE);
                } else if (status == 2) {
                    ivActivity.setVisibility(View.VISIBLE);
                } else if (status == 3) {
                    ivNotice.setVisibility(View.VISIBLE);
                }
            }

            //刷新
            isrefresh = true;
            indexs.set(i, 1);
            setBg(i);
            getData(status);
        }
    }

    public void refreshData() {
        isrefresh = true;
        indexs.set(status, 1);
        getData(status);
    }

    public void loadmoreData() {
        if (lv.isCanLoad() && Utils.isSlideToBottom(lv)) {

        } else {
            if (mActivity != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.setFinish(1);
                    }
                }, 800);
            }
//            lv.loadMoreComplete();
            return;
        }
        isrefresh = false;
        loadmore = true;
        if (indexs.get(status) < totalPages.get(status)) {
            indexs.set(status, indexs.get(status) + 1);
            getData(status);
        } else {
            if (mActivity != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.setFinish(1);
                    }
                }, 800);
            }
        }
    }


//    @Override
//    public void onLoadMore() {
//
//        if (lv.isCanLoad() && Utils.isSlideToBottom(lv)) {
//
//        } else {
//            lv.loadMoreComplete();
//            return;
//        }
//        isrefresh = false;
//        if (indexs.get(status) < totalPages.get(status)) {
//            indexs.set(status, indexs.get(status) + 1);
//            getData(status);
//        } else {
//            Utils.toastShort(mContext, "全部数据加载完毕");
//            lv.loadMoreComplete();
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private TeamBean cBean;

    public void setBean(TeamBean cBean) {
        this.cBean = cBean;
    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) lv.getLayoutManager();
//        int position = layoutManager.findFirstVisibleItemPosition();
//        Log.e("ying","position:"+position);
        View firstVisiableChildView = layoutManager.findViewByPosition(1);
        if (firstVisiableChildView == null) {
            return 9999999;
        }
        return firstVisiableChildView.getTop();
    }
}
