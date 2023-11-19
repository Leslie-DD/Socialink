package com.example.heshequ.activity.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.team.AddTeamActivity;
import com.example.heshequ.adapter.recycleview.MyTeamAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.TeamBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.entity.RefTeamChild1;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.CircleView;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyTeamActivity extends NetWorkActivity implements XRecyclerView.LoadingListener {
    private XRecyclerView rv;
    private TextView tvNoData;
    private ArrayList<TeamBean> data;
    private MyTeamAdapter adapter;
    private int pn = 1;
    private int ps = 0;   //总页数
    private final int GETDATA = 1000;
    private final int REFDATA = 1001;
    private final int LOADATA = 1002;
    private Gson gson = new Gson();
    private SharedPreferences sp;
    private TeamBean teamBean;
    private int type;  // 0 -> 初始化加载 ； 1 ->刷新；  2 -> 加载
    private JSONArray jsonArray;
    private WindowManager.LayoutParams layoutParams;
    private PopupWindow editorPop;
    private CircleView cvHead;
    private EditText etName;
    private Button btConfirm;
    private ImageView ivRight;
    private final int editorCode = 1003;
    private final int CanSeeCode = 1004;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        setText("我的团队");
        ivRight = findViewById(R.id.ivRight);
        ivRight.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.kj2));
        sp = MeetApplication.getInstance().getSharedPreferences();
        rv = (XRecyclerView) findViewById(R.id.rv);
        tvNoData = findViewById(R.id.tvNoData);
        ConsTants.initXrecycleView(mContext, true, true, rv);
        data = new ArrayList<>();
        adapter = new MyTeamAdapter(mContext, data);
        rv.setAdapter(adapter);
        rv.setLoadingListener(this);
        type = 0;
        pn = 1;
        getData(pn, type);
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyTeamActivity.this.finish();
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddTeamActivity.class));
            }
        });
        adapter.setListener(new MyTeamAdapter.OnClickEventListener() {
            @Override
            public void OnCanSee(int position) {
                if (adapter.getData().get(position).getSettingVisible() == 0) {
                    setBodyParams(new String[]{"id", "settingVisible"}, new String[]{"" + adapter.getData().get(position).getId(), "" + 1});
                    sendPost(Constants.base_url + "/api/club/base/setvisibility.do", CanSeeCode, Constants.token);
                } else {
                    setBodyParams(new String[]{"id", "settingVisible"}, new String[]{"" + adapter.getData().get(position).getId(), "" + 0});
                    sendPost(Constants.base_url + "/api/club/base/setvisibility.do", CanSeeCode, Constants.token);
                }
            }

            @Override
            public void OnEditor(int position) {
                initEditorPop(position);
            }
        });
    }

    public void initEditorPop(final int p) {
        editorPop = new PopupWindow(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        View editorview = LayoutInflater.from(this).inflate(R.layout.pop_myteam_editor, null);
        etName = (EditText) editorview.findViewById(R.id.etName);
        btConfirm = (Button) editorview.findViewById(R.id.btConfirm);
        etName.setText(adapter.getData().get(p).getName());
        etName.setSelection(etName.getText().toString().trim().length());
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        editorPop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击弹框外部，弹框消失
        editorPop.setOutsideTouchable(true);
        // 设置焦点
        editorPop.setFocusable(true);
        editorPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
            }
        });
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 20) {
                    Utils.toastShort(mContext, "已达最长字符，无法继续输入");
                }
            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().trim().isEmpty()) {
                    Utils.toastShort(mContext, "团队名称不能为空");
                    return;
                }
                String name = etName.getText().toString().trim();
                setBodyParams(new String[]{"id", "name"}, new String[]{"" + adapter.getData().get(p).getId(), "" + name});
                sendPost(Constants.base_url + "/api/club/base/updatebase.do", editorCode, Constants.token);
                editorPop.dismiss();
            }
        });
        editorPop.setContentView(editorview);
        showEditorPop();
    }

    private void showEditorPop() {
        layoutParams.alpha = 0.5f;
        getWindow().setAttributes(layoutParams);
        editorPop.showAtLocation(rv, Gravity.CENTER, 0, 0);
    }

    private void getData(int pn, int type) {
        switch (type) {
            case 0:
                setBodyParams(new String[]{"type", "pn", "ps", "uid"}, new String[]{"" + 6, "" + pn, "" + Constants.default_PS, Constants.uid + ""});
                sendPost(Constants.base_url + "/api/club/base/pglist.do", GETDATA, sp.getString("token", ""));
                break;
            case 1:
                setBodyParams(new String[]{"type", "pn", "ps", "uid"}, new String[]{"" + 6, "" + pn, "" + Constants.default_PS, Constants.uid + ""});
                sendPost(Constants.base_url + "/api/club/base/pglist.do", REFDATA, sp.getString("token", ""));
                break;
            case 2:
                setBodyParams(new String[]{"type", "pn", "ps", "uid"}, new String[]{"" + 6, "" + pn, "" + Constants.default_PS, Constants.uid + ""});
                sendPost(Constants.base_url + "/api/club/base/pglist.do", LOADATA, sp.getString("token", ""));
                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pn = 1;
                type = 1;
                getData(pn, type);
                rv.refreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pn < ps) {
                    pn++;
                    type = 2;
                    getData(pn, type);
                }
                rv.loadMoreComplete();
            }
        }, 1000);
    }

    public void setData(ArrayList<TeamBean> list) {
        if (list != null) {
            adapter.setData(list);
        } else {
            adapter.setData(new ArrayList<TeamBean>());
        }
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) {
        //Log.e(TAG,""+result);
        int resultType = result.optInt("code");
        switch (where) {
            case GETDATA:
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {
                                    tvNoData.setVisibility(View.GONE);
                                    data = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                        if (teamBean.getCreator() == Constants.uid) {
                                            teamBean.setItemType(1);
                                            data.add(teamBean);
                                        }
                                    }
                                    setData(data);
                                } else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                }
                break;
            case REFDATA:
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {
                                    data = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                        if (teamBean.getCreator() == Constants.uid) {
                                            teamBean.setItemType(1);
                                            data.add(teamBean);
                                        }
                                    }
                                    setData(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                }
                break;
            case LOADATA:
                switch (resultType) {
                    case 0:
                        if (!result.optString("data").isEmpty()) {
                            try {
                                ps = result.optJSONObject("data").optInt("totalPage");
                                jsonArray = new JSONArray(result.optJSONObject("data").optString("list"));
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        teamBean = gson.fromJson(jsonArray.getString(i), TeamBean.class);
                                        if (teamBean.getCreator() == Constants.uid) {
                                            teamBean.setItemType(1);
                                            data.add(teamBean);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        Utils.toastShort(this, result.optString("msg"));
                        break;
                }
                break;
            case editorCode:
                if (result.optInt("code") == 0) {
                    pn = 1;
                    type = 0;
                    getData(pn, type);
                }
                Utils.toastShort(mContext, result.optString("msg"));
                break;
            case CanSeeCode:
                if (result.optInt("code") == 0) {
                    pn = 1;
                    type = 0;
                    getData(pn, type);
                }
                Utils.toastShort(mContext, result.optString("msg"));
                break;
        }
    }

    @Override
    protected void onFailure(String result, int where) {
        Utils.toastShort(this, "网络异常");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRef(RefTeamChild1 teamChild1) {
        pn = 1;
        type = 0;
        getData(pn, type);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
