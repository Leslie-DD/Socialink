package com.hnu.heshequ.activity.team;

/**
 * Created by 佳佳 on 2018/12/23.
 */


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.recycleview.MessageAdapter;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.MessageBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zkk.view.rulerview.RulerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import com.zkk.rulerview.R;

public class ChatHistoryActivity extends NetWorkActivity implements XRecyclerView.LoadingListener{
    private RulerView ruler_height;   //身高的view
    private TextView tv_register_info_height_value;
    private Calendar calendar = Calendar.getInstance();
    private static final int start = 0;
    private static int end = 0;
    private static int today = 0;
    private static int nextday = 1;
    private static int hisid;
    private static int myid;
    private XRecyclerView rv;
    private static ImageView historyback;
    private static EditText historyedit;
    private static ImageView historysearch;
    private static MessageAdapter adapter;
    private int pn = 0;
    private int ps = 12;
    private int allpn = 0;
    private String message;
    private final int refresh = 1001;
    private final int loadmore = 1002;
    private final int search = 1003;
    private ArrayList<MessageBean> testData = new ArrayList<>();
    private ArrayList<MessageBean> testData1 = new ArrayList<>();
    private MessageBean testData2;
    private Gson gson = new Gson();
    private String[] date = new String[3000];
    //获取系统的日期
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);


    private static Boolean isYun(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        }
        return false;
    }

    private static int getdays(int year, int month, int day) {
        int days = 0;
        for (int i = 2018; i <= year; i++) {
            if (isYun(i) && year - i >= 1) {
                days += 366;
            } else if ((!isYun(i)) && year - i >= 1) {
                days += 365;
            } else {
                for (int j = 1; j <= month; j++) {
                    if (j == 1 || j == 3 || j == 5 || j == 7 || j == 8 || j == 10 || j == 12) {
                        if (j < month) {
                            days += 31;
                        } else {
                            days += day;
                            break;
                        }
                    } else if (j == 4 || j == 6 || j == 9 || j == 11) {
                        if (j < month) {
                            days += 30;
                        } else {
                            days += day;
                            break;
                        }
                    } else {
                        if (isYun(i)) {
                            if (j < month) {
                                days += 29;
                            } else {
                                days += day;
                                break;
                            }
                        } else {
                            if (j < month) {
                                days += 28;
                            } else {
                                days += day;
                                break;
                            }
                        }
                    }
                }
            }

        }
        return days;
    }

    private static void getdate(int days, String[] date) {
        date[0] = "2018-1-1";
        int year = 2018;
        int month = 1;
        int day = 1;
        for (int i = 1; i < days; i++) {
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (day < 31) {
                    day++;
                } else {
                    day = 1;
                    if (month == 12) {
                        month = 1;
                        year++;
                        day = 1;
                    } else {
                        month++;
                        day = 1;
                    }
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day < 30) {
                    day++;
                } else {
                    day = 1;
                    month++;
                }
            } else {
                if (isYun(year)) {
                    if (day < 29) {
                        day++;
                    } else {
                        day = 1;
                        month++;
                    }
                } else {
                    if (day < 28) {
                        day++;
                    } else {
                        day = 1;
                        month++;
                    }
                }
            }
            date[i] = year + "-" + month + "-" + day;
        }
        for (int x = 0; x < 400; x++) {
            Log.e("DDQ", date[x] + "-" + x + "-");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_of_chat);
        init();
        event();
    }

    public void init() {   //对方id 我的id ,我的id其实并不需要传过来。。
        hisid = getIntent().getIntExtra("hisid", 0);
        myid = getIntent().getIntExtra("myid", 0);
        //根据年月日计算到2018年1月1日的天数 1-1 到1-1也为1天
        end = getdays(year, month, day);
        today = end;
        //根据天数设置每一天的日期 存到数组中
        getdate(today, date);
        rv = (XRecyclerView) findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rv.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(new ArrayList<MessageBean>(), mContext);
        ConsTants.initXRecycleView(mContext, true, true, rv);

        rv.setAdapter(adapter);
        ruler_height = (RulerView) findViewById(R.id.ruler_height);


        tv_register_info_height_value = (TextView) findViewById(R.id.tv_register_info_height_value);
        tv_register_info_height_value.setText("" + year + "-" + month + "-" + day);
        historyedit = (EditText) findViewById(R.id.historyedit);
        historysearch = (ImageView) findViewById(R.id.historysearch);
        historysearch.setOnClickListener(v -> {
            message = historyedit.getText().toString();
            if (message.length() > 127) Utils.toastShort(this, "字符过长");
            else if (message.length() == 0) Utils.toastShort(this, "搜索不能为空");
            else {
                pn = 1;
                historysearch.setImageResource(R.mipmap.historysearching);
                tv_register_info_height_value.setTextColor(Color.parseColor("#ababab"));
                setBodyParams(new String[]{"receiver", "pn", "ps", "content"}, new String[]{"" + hisid, "" + 1, "" + ps, message});
                sendPost(WenConstans.SearchMessage, refresh, Constants.token);
            }
        });


        //滚动设置 显示
        ruler_height.setOnValueChangeListener(value -> {
            pn = 0;
            historysearch.setImageResource(R.mipmap.historysearch);
            tv_register_info_height_value.setTextColor(Color.parseColor("#63B8FF"));
            tv_register_info_height_value.setText(date[(int) value]);
            today = (int) value;
        });


        ruler_height.setValue(end - 1, start, end - 1, 1);
    }

    //返回按钮 加载监听
    private void event() {
        rv.setLoadingListener(this);
        findViewById(R.id.historyback).setOnClickListener(view -> ChatHistoryActivity.this.finish());
    }

    private void getData(int hisid, int pn, int type) {
        if (type == 1) {
            setBodyParams(new String[]{"receiver", "pn", "ps", "time"}, new String[]{"" + hisid, "" + pn, "" + ps, tv_register_info_height_value.getText().toString()});
            sendPost(WenConstans.SearchMessage, refresh, Constants.token);
        } else if (type == 2) {
            setBodyParams(new String[]{"receiver", "pn", "ps", "time"}, new String[]{"" + hisid, "" + pn, "" + ps, tv_register_info_height_value.getText().toString()});
            sendPost(WenConstans.SearchMessage, loadmore, Constants.token);
        } else if (type == 3) {
            setBodyParams(new String[]{"receiver", "pn", "ps", "content"}, new String[]{"" + hisid, "" + pn, "" + ps, message});
            sendPost(WenConstans.SearchMessage, refresh, Constants.token);
        } else {
            setBodyParams(new String[]{"receiver", "pn", "ps", "content"}, new String[]{"" + hisid, "" + pn, "" + ps, message});
            sendPost(WenConstans.SearchMessage, loadmore, Constants.token);
        }

    }

    public void onRefresh() {
        message = historyedit.getText().toString();
        if (!message.isEmpty()) {
            if (allpn == 1) {
                rv.refreshComplete();
            } else if (pn < allpn) {
                pn++;
                getData(hisid, pn, 4);
            } else {
                rv.refreshComplete();
            }

        } else {
            if (pn == 0) {
                pn = 1;
                getData(hisid, pn, 1);
            } else if (allpn == 1) {
                rv.refreshComplete();
            } else if (pn < allpn) {
                pn++;
                getData(hisid, pn, 2);
            } else {
                rv.refreshComplete();
            }
        }
    }

    public void onLoadMore() {
        rv.loadMoreComplete();
    }

    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        if (where == refresh) {
            rv.refreshComplete();
            try {
                //json解析数据
                testData1 = new ArrayList<>();
                JSONObject data = new JSONObject(result.optString("data"));
                if (data != null) {
                    //将数据分组存储在test1Data1中
                    allpn = data.optInt("totalPage");
                    if (allpn == 0) Utils.toastShort(this, "无消息记录");
                    testData1 = gson.fromJson(data.optString("list"), new TypeToken<List<MessageBean>>() {
                    }.getType());
                    if (testData1 != null) {
                        //pn=1，进入页面时或聊天历史记录过少时刷新
                        if (pn == 1) {
                            testData.clear();
                            int size = testData1.size();
                            for (int i = 0; i < size; i++) {
                                testData2 = new MessageBean();
                                testData2 = testData1.get(i);
                                //判定双方id,设置type,type=0,设置聊天右方发送窗口，否则设置左方接收窗口。
                                if (testData2.getSender() == myid) {
                                    testData2.setSor(0);
                                    testData.add(0, testData2);
                                } else {
                                    testData2.setSor(1);
                                    testData.add(0, testData2);
                                }
                            }
                            adapter.setData(testData);
                            rv.scrollToPosition(adapter.getData().size() + 1);
                        }
                        //pn不为1，加载聊天历史
                        else {
                            testData.clear();
                            int size = testData1.size();
                            for (int i = 0; i < size; i++) {
                                testData2 = new MessageBean();
                                testData2 = testData1.get(i);
                                if (testData2.getSender() == myid) {
                                    testData2.setSor(0);
                                    testData.add(0, testData2);
                                } else {
                                    testData2.setSor(1);
                                    testData.add(0, testData2);
                                }
                            }
                            //将数据增添到布局中，并将布局定位到添加信息数+9，位置处，实现下拉时，加载历史消息，并讲历史消息的最下面一行显示在当前窗口顶部。
                            adapter.setData(testData);
                            rv.scrollToPosition(testData1.size() + 9);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (where == loadmore) {
            try {
                rv.refreshComplete();
                //json解析数据

                testData1 = new ArrayList<>();
                JSONObject data = new JSONObject(result.optString("data"));
                if (data != null) {
                    //将数据分组存储在test1Data1中
//                    allpn = data.optInt("totalPage");
                    testData1 = gson.fromJson(data.optString("list"), new TypeToken<List<MessageBean>>() {
                    }.getType());
                    if (testData1 != null) {
                        //pn=1，进入页面时或聊天历史记录过少时刷新
                        if (pn == 1) {
                            testData.clear();
                            int size = testData1.size();
                            for (int i = 0; i < size; i++) {
                                testData2 = new MessageBean();
                                testData2 = testData1.get(i);
                                //判定双方id,设置type,type=0,设置聊天右方发送窗口，否则设置左方接收窗口。
                                if (testData2.getSender() == myid) {
                                    testData2.setSor(0);
                                    testData.add(0, testData2);
                                } else {
                                    testData2.setSor(1);
                                    testData.add(0, testData2);
                                }
                            }
                            adapter.setData(testData);
                            rv.scrollToPosition(adapter.getData().size() + 1);
                        }
                        //pn不为1，加载聊天历史
                        else {
                            testData.clear();
                            int size = testData1.size();
                            for (int i = 0; i < size; i++) {
                                testData2 = new MessageBean();
                                testData2 = testData1.get(i);
                                if (testData2.getSender() == myid) {
                                    testData2.setSor(0);
                                    testData.add(0, testData2);
                                } else {
                                    testData2.setSor(1);
                                    testData.add(0, testData2);
                                }
                            }
                            //将数据增添到布局中，并将布局定位到添加信息数+9，位置处，实现下拉时，加载历史消息，并讲历史消息的最下面一行显示在当前窗口顶部。
                            adapter.setData1(testData);
                            rv.scrollToPosition(testData1.size() + 9);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    protected void onFailure(String result, int where) {
        rv.refreshComplete();
        Utils.toastShort(this, "网络异常");
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    //继承父类函数，实现加载时页面悬停
    public void onPause() {
        super.onPause();
    }

}
