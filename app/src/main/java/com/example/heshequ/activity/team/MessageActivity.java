package com.example.heshequ.activity.team;

import static com.example.heshequ.constans.WenConstans.BaseUrl;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.heshequ.R;
import com.example.heshequ.adapter.recycleview.MessageAdapter;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.MessageBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.PhotoUtils;
import com.example.heshequ.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by 佳佳 on 2018/11/23.
 */


public class MessageActivity extends NetWorkActivity implements XRecyclerView.LoadingListener, View.OnClickListener {
    private XRecyclerView rv;
    private EditText msg;
    private ImageView imageView;
    private ImageView history;
    private MessageAdapter adapter;
    private ArrayList<MessageBean> testData = new ArrayList<>();
    private ArrayList<MessageBean> testData1 = new ArrayList<>();
    private MessageBean testData2;
    private WindowManager.LayoutParams layoutParams;
    private int status;
    private final int Loadmessage = 1000;
    private final int Sendmessage = 1001;
    private final int SENDPHOTO = 1002;
    private final int CHECKPULLTHEBLACK = 1003;
    private Gson gson = new Gson();
    private boolean isref = true;//刷新
    private int pn = 0;//分页
    private int allpn = 0;//全部页数
    private int hisid;//给与发送者id
    private int myid;//自己的id
    private int ps = 12;
    private String hisnickname;//昵称
    private String msgs;
    private String myheader;
    private ImageView send_photo;
    private String path;
    private int code;
    private ImageView iv_image;
    private PopupWindow mPopWindow;
    private Uri photoUri;
    private boolean Checkblack;
    private PopupWindow window;
    private String gmtCreate = "";//时间

    @Override
    //继承父类函数，设定布局文件和函数调用。
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_background);
        init();
        event();
    }

    //返回功能，退出当前页面
    private void event() {
        rv.setLoadingListener(this);
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageActivity.this.finish();
            }
        });
    }

    /*
     * 设定布局文件，控件初始化，适配器初始化，监听器添加
     * 调用网络请求
     * */
    private void init() {

        //接受来自个人信息界面，或聊天记录处发送的数据
        hisid = getIntent().getIntExtra("hisid", 0);
        hisnickname = getIntent().getStringExtra("nickname");
        myheader = getIntent().getStringExtra("myheader");
        setText(hisnickname);
        myid = Constants.uid;
        rv = (XRecyclerView) findViewById(R.id.rv);
        imageView = (ImageView) findViewById(R.id.msgSend2);
        msg = (EditText) findViewById(R.id.msgEditText2);
        send_photo = (ImageView) findViewById(R.id.sendother);

        history = (ImageView) findViewById(R.id.historyofchat);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(new ArrayList<MessageBean>(), mContext);
        //刷新，加载设置
        ConsTants.initXrecycleView(mContext, true, true, rv);
        rv.setAdapter(adapter);
        pn = 0;
        imageView.setOnClickListener(this);
        send_photo.setOnClickListener(this);
        history.setOnClickListener(this);
        rv.setLoadingListener(this);

        onRefresh();

//        setBodyParams(new String[]{"black"}, new String[]{"" + hisid});
//        sendPost(WenConstans.CheckPullTheBlack, CHECKPULLTHEBLACK, Constants.token);
    }

    private void showPopupWindow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_item, null);
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams = getWindow().getAttributes();
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        // 在底部显示
        window.showAtLocation(MessageActivity.this.findViewById(R.id.sendother), Gravity.BOTTOM, 0, 0);
        // 这里检验popWindow里的button是否可以点击
        Button first = (Button) view.findViewById(R.id.first);
        Button second = (Button) view.findViewById(R.id.second);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                path = PhotoUtils.startPhoto(MessageActivity.this);
            }
        });
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                PhotoUtils.showFileChooser(203, MessageActivity.this);
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                getWindow().setAttributes(layoutParams);
            }
        });
        window.setContentView(view);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 200:
                File temp = new File(path);
                if (temp.exists()) {
                    Luban.with(this)
                            .load(temp)
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onSuccess(final File file) {
                                    OkHttpUtils
                                            .post(WenConstans.SendMessage)
                                            .tag(context)
                                            .headers(Constants.Token_Header, Constants.token)
                                            .params("type", "" + 1)
                                            .params("receiver", hisid + "")
                                            .params("file", file)
                                            .params("content", "")
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    JSONObject result = null;
                                                    try {
                                                        result = new JSONObject(s);
                                                        code = result.optInt("code");
                                                        if (code == 3) {
                                                            Utils.toastShort(context, "你已被拉黑");
                                                        } else {
                                                            getData1(hisid);
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            }).launch();
                } else {
                    Log.e("DDQ", "something wrong");
                }
                break;
            case 203:
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context
                        .getContentResolver()
                        .query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                if (picturePath.substring(picturePath.length() - 3, picturePath.length()).equals("jpg")
                        || picturePath.substring(picturePath.length() - 3, picturePath.length()).equals("png")) {
                    cursor.close();
                    Luban.with(this)
                            .load(new File(picturePath))
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    OkHttpUtils.post(BaseUrl + "/api/chat/base/save.do")
                                            .tag(context)
                                            .headers(Constants.Token_Header, Constants.token)
                                            .params("file", file)
                                            .params("type", "1")
                                            .params("receiver", hisid + "")
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    JSONObject result = null;
                                                    try {
                                                        result = new JSONObject(s);
                                                        code = result.optInt("code");
                                                        if (code == 3) {
                                                            Utils.toastShort(context, "你已被拉黑");
                                                        } else {
                                                            getData1(hisid);
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            }).launch();
                } else {
                    Utils.toastShort(this, "图片格式暂不支持");
                }
                break;
        }
    }

    //发送按钮监听，
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msgSend2:
                //发送内容判定不为空则发送网络请求
                msgs = msg.getText().toString();
                if (msgs.length() > 127) Utils.toastShort(this, "字符过长");
                else if (msgs.length() == 0) Utils.toastShort(this, "输入为空");
                else {
                    setBodyParams(new String[]{"receiver", "content"}, new String[]{"" + hisid, "" + msgs});
                    sendPost(WenConstans.SendMessage, Sendmessage, Constants.token);
                }
                break;
            case R.id.sendother:
                if (code != 3) showPopupWindow();
                else {
                    Utils.toastShort(this, "你已被拉黑！");
                }
                break;
            case R.id.historyofchat:
                Intent intent = new Intent();
                intent.setClass(MessageActivity.this, ChatHistoryActivity.class);
                intent.putExtra("hisid", hisid);
                intent.putExtra("myid", myid);
//                context.startActivity(new Intent(context, ChatHistoryActivity.class));
                startActivity(intent);
                break;
        }
    }

    //加载聊天历史消息，
    private void getData(int hisid) {
        if (testData.size() != 0) {
            gmtCreate = testData.get(testData.size() - 1).getGmtCreate();
            if (gmtCreate != null) {
                setBodyParams(new String[]{"receiver", "pn", "ps", "gmtCreate"}, new String[]{"" + hisid, "" + pn, "" + ps, gmtCreate});
                sendPost(WenConstans.SearchMessage, Loadmessage, Constants.token);
                return;
            }
        }

        setBodyParams(new String[]{"receiver", "pn", "ps"}, new String[]{"" + hisid, "" + pn, "" + ps});
        sendPost(WenConstans.SearchMessage, Loadmessage, Constants.token);

    }

    //获取聊天历史记录第一页消息，在初始化和发送信息时调用
    private void getData1(int hisid) {
        setBodyParams(new String[]{"receiver", "pn", "ps"}, new String[]{"" + hisid, "" + 1, "" + ps});
        sendPost(WenConstans.SearchMessage, 1, Constants.token);
    }

    private void getData2(int hisid) {
        setBodyParams(new String[]{"receiver", "pn", "ps"}, new String[]{"" + hisid, "" + 1, "" + ps});
        sendPost(WenConstans.SearchMessage, 2, Constants.token);
    }

    @Override
    //下拉刷新功能，
    /*
     * if(pn==0&&allpn==0)  第一次进入聊天界面时加载历史消息调用
     * else if(pn==1 && allpn==1) 聊天历史小于分页大小时（即总页数为1），下拉实现刷新当前数据。
     * else if(pn<allpn) 下拉加载历史消息
     * else 再历史消息已被全部加载完成时，再次下拉，刷新直接结束。
     * */
    public void onRefresh() {
        if (pn == 0 && allpn == 0) {
            pn = 1;
            getData1(hisid);
        } else if (pn == 1 && allpn == 1) {
            getData(hisid);
        } else if (pn < allpn) {
            pn++;
            getData(hisid);
        } else {
            rv.refreshComplete();
        }
    }

    @Override
    /*
     * 上拉功能：
     * 继承类所必须继承的函数，在私聊界面并不需要使用。设置为上拉直接加载完成
     * */
    public void onLoadMore() {
        getData2(hisid);
    }

    @Override

    /*
     * 网络请求成功后，每个请求返回数据后在此处理
     * result为返回数据
     * where 判定网络请求类型
     * */
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {
        //第一次进入加载历史消息
        if (where == Loadmessage) {
            //刷新效果结束
            rv.refreshComplete();
            try {
                //json解析数据

                testData1 = new ArrayList<>();
                JSONObject data = new JSONObject(result.optString("data"));
                if (data != null) {
                    //将数据分组存储在test1Data1中
                    allpn = data.optInt("totalPage");
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
        //发送消息时调用，消息发送已成功，再次调用网络请求，将已发送消息取出并显示，并清空输入框。
        else if (where == Sendmessage) {
            code = result.optInt("code");
            if (code == 3) {
                Utils.toastShort(context, "你已被拉黑");
            } else {

                getData1(hisid);
            }


            msg.setText(""); // 清空输入框中的
        }
        //加载最新的聊天历史消息，在sendmessage后调用
        else if (where == 1) {
            code = result.optInt("code");
            if (code == 3) {
                Utils.toastShort(context, "你已被拉黑");
            } else {
                testData1 = new ArrayList<>();
                JSONObject data = new JSONObject(result.optString("data"));
                if (data != null) {
                    allpn = data.optInt("totalPage");
                    testData1 = gson.fromJson(data.optString("list"), new TypeToken<List<MessageBean>>() {
                    }.getType());
                    if (testData1 != null) {
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
                        pn = 1;
                        adapter.setData(testData);
                        rv.smoothScrollToPosition(adapter.getData().size());
                    }
                }
            }
        } else if (where == 2) {
            rv.loadMoreComplete();
            testData1 = new ArrayList<>();
            JSONObject data = new JSONObject(result.optString("data"));
            if (data != null) {
                allpn = data.optInt("totalPage");
                testData1 = gson.fromJson(data.optString("list"), new TypeToken<List<MessageBean>>() {
                }.getType());
                if (testData1 != null) {
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
                    pn = 1;
                    adapter.setData(testData);
                    rv.smoothScrollToPosition(adapter.getData().size());
                }
            }
        } else if (where == SENDPHOTO) {
            code = result.optInt("code");
            if (code == 3) {
                Utils.toastShort(this, "你已被拉黑");
            }
        }
    }

    @Override
    //网络请求失败后提示用户网络异常
    protected void onFailure(String result, int where) {
        if (where == Sendmessage) {

        } else if (where == Loadmessage) {
            if (isref) {
                rv.refreshComplete();
            } else {
                rv.loadMoreComplete();
            }
        }

        Utils.toastShort(this, "网络异常");
    }

    @Override
    //继承父类函数，实现加载后页面回复
    public void onResume() {
        super.onResume();
    }

    @Override
    //继承父类函数，实现加载时页面悬停
    public void onPause() {
        super.onPause();
    }
}
