/**
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */

package com.example.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.team.MessageActivity;
import com.example.heshequ.activity.team.PersonalInformationActivity;
import com.example.heshequ.activity.team.StatementDetailActivity;
import com.example.heshequ.activity.wenwen.WenwenDetailActivity;
import com.example.heshequ.bean.MsgSayBean;
import com.example.heshequ.bean.TeamBean;
import com.example.heshequ.bean.WenwenBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.CircleView;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author dev06
 * 2016年7月4日
 */
//设置聊天列表的适配器，，这个不是我写的，，不全是我写的。。。这是一期的人写的。。我添了一点。
public class SayNewsAdapter extends RecyclerView.Adapter {
    private int type;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MsgSayBean.SayBean> data = new ArrayList<>();
    private ItemDelListener listener;
    private Gson gson;
    private TeamBean bean;

    //初始化
    public SayNewsAdapter(Context context, ArrayList<MsgSayBean.SayBean> data, int type) {
        super();
        this.type = type;
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        gson = new Gson();
    }

    //数据设置，更新时调用
    public void setData(ArrayList<MsgSayBean.SayBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    //加载时调用
    public void setData2(ArrayList<MsgSayBean.SayBean> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public void setDate3(ArrayList<MsgSayBean.SayBean> data) {
        this.data = null;
        this.notifyDataSetChanged();
    }


    public void setListener(ItemDelListener listener) {
        this.listener = listener;
    }

    @Override
    //布局设置
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_say_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //item 控件初始化
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime, tvName, tvDesc, tvTip;
        private CircleView ivHead;
        private ImageView ivClose;

        public ViewHolder(View view) {
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            tvTip = (TextView) view.findViewById(R.id.tvTip);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            ivClose = (ImageView) view.findViewById(R.id.ivClose);
        }

        public void setData(final int position) {
            final MsgSayBean.SayBean sayBean = data.get(position);
            tvName.setText(sayBean.getNickName());
            if (!TextUtils.isEmpty(sayBean.getHeader())) {
                Glide.with(context).load(Constants.base_url + sayBean.getHeader()).asBitmap().into(ivHead);
            } else {
                ivHead.setImageResource(R.mipmap.head3);
            }
            tvTime.setText(sayBean.getTime());

            if (type == 3) {
                tvDesc.setText(sayBean.getSenderNickname() + ": " + sayBean.getContent());
            } else {
                tvDesc.setText(sayBean.getContent());
            }
            if (type == 1) {
                tvTip.setText("回复了您的团言");
            } else if (type == 2) {
                tvTip.setText("回复了您的问题");
            } else if (type == 3) {
                tvTip.setText(sayBean.getObjectNickname());
            } else if (type == 4) {
                tvTip.setText("TEST-HERE");
            }
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //删除
                    listener.onDel(position);
                }
            });
            //点击头像，跳转个人信息界面
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type != 3) {
                        context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", sayBean.getReplyId()));
                    } else {
                        context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", sayBean.getObjectId()));
                    }

                }
            });
            //点击item,跳到对应视图。设置
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        OkHttpUtils.post(Constants.base_url + "/api/club/speak/detail.do")
                                .tag(context)
                                .headers(Constants.Token_Header, Constants.token)
                                .params("id", "" + data.get(position).getBizId())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        JSONObject result = null;
                                        try {
                                            result = new JSONObject(s);
                                            if (result.optInt("code") == 0) {
                                                TeamBean.SpeakBean speakBean = gson.fromJson(result.optString("data"), TeamBean.SpeakBean.class);
                                                if (speakBean != null) {
                                                    bean = new TeamBean();
                                                    bean.setSpeak(speakBean);
                                                } else {
                                                    Utils.toastShort(context, "该团言已被删除");
                                                    return;
                                                }
                                                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_messageEnterTeamSay");
                                                context.startActivity(new Intent(context, StatementDetailActivity.class)
                                                        .putExtra("bean", bean)
                                                        .putExtra("type", 3));
                                            } else {
                                                Utils.toastShort(context, result.optString("msg"));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                    } else if (type == 2) {
                        OkHttpUtils.post(Constants.base_url + "/api/ask/base/askInfo.do")
                                .tag(context)
                                .headers(Constants.Token_Header, Constants.token)
                                .params("askid", "" + data.get(position).getBizId())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        JSONObject result = null;
                                        try {
                                            result = new JSONObject(s);
                                            if (result.optInt("code") == 0) {
                                                WenwenBean wenwenBean = gson.fromJson(result.optString("data"), WenwenBean.class);
                                                if (wenwenBean != null) {
                                                    MobclickAgent.onEvent(MeetApplication.getInstance(), "event_messageEnterQuestion");
                                                    MobclickAgent.onEvent(MeetApplication.getInstance(), "event_commentController");
                                                    Intent intent = new Intent(context, WenwenDetailActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("wenwen", wenwenBean);
                                                    intent.putExtras(bundle);
                                                    context.startActivity(intent);
                                                } else {
                                                    Utils.toastShort(context, "该问问已被删除");
                                                    return;
                                                }
                                            } else {
                                                Utils.toastShort(context, result.optString("msg"));
                                                //Utils.toastShort(context,"该问问已被删除");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                    } else if (type == 3) {
                        //直接跳转
                        context.startActivity(new Intent(context, MessageActivity.class).putExtra("hisid", sayBean.getObjectId()).putExtra("nickname", sayBean.getObjectNickname()));
                    } else if (type == 4) {
                        OkHttpUtils.post(Constants.base_url + "/api/ask/base/askInfo.do")
                                .tag(context)
                                .headers(Constants.Token_Header, Constants.token)
                                .params("askid", "" + data.get(position).getBizId())
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        JSONObject result = null;
                                        try {
                                            result = new JSONObject(s);
                                            if (result.optInt("code") == 0) {
                                                WenwenBean wenwenBean = gson.fromJson(result.optString("data"), WenwenBean.class);
                                                if (wenwenBean != null) {
                                                    MobclickAgent.onEvent(MeetApplication.getInstance(), "event_messageEnterQuestion");
                                                    MobclickAgent.onEvent(MeetApplication.getInstance(), "event_commentController");

                                                    Intent intent = new Intent(context, WenwenDetailActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("wenwen", wenwenBean);
                                                    intent.putExtras(bundle);
                                                    context.startActivity(intent);
                                                } else {
                                                    Utils.toastShort(context, "该问问已被删除");
                                                    return;
                                                }
                                            } else {
                                                Utils.toastShort(context, result.optString("msg"));
                                                //Utils.toastShort(context,"该问问已被删除");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                    }

                }
            });
        }
    }

    public interface ItemDelListener {
        void onDel(int position);
    }
}
