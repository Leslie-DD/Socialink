package com.leslie.socialink.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.PersonalInformationActivity;
import com.leslie.socialink.bean.MsgSayBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.view.CircleView;

import java.util.ArrayList;

/**
 * @author dev06
 * 2016年7月4日
 */

public class TeamNewsAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MsgSayBean.SayBean> data = new ArrayList<>();
    private ItemEventListener listener;

    public TeamNewsAdapter(Context context, ArrayList<MsgSayBean.SayBean> data) {
        super();
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<MsgSayBean.SayBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setData2(ArrayList<MsgSayBean.SayBean> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public void setItemEventListener(ItemEventListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_team_item, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime, tvName, tvTeamName, tvDesc, tvRefuse, tvAgree, tvTip, Tvjoin;
        private CircleView ivHead;
        private ImageView ivClose;

        public ViewHolder(View view) {
            super(view);
            Tvjoin = (TextView) view.findViewById(R.id.Tvjoin);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTeamName = (TextView) view.findViewById(R.id.tvTeamName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            tvRefuse = (TextView) view.findViewById(R.id.tvRefuse);
            tvAgree = (TextView) view.findViewById(R.id.tvAgree);
            tvTip = (TextView) view.findViewById(R.id.tvTip);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            ivClose = (ImageView) view.findViewById(R.id.ivClose);
        }

        public void setData(final int position) {
            final MsgSayBean.SayBean sayBean = data.get(position);
            if (sayBean != null) {
                if (sayBean.getContent() != null) {
                    if (sayBean.getNickName().equals("admin") || sayBean.getContent().contains("解封") || sayBean.getContent().contains("封禁")) {
                        tvName.setText(sayBean.getNickName());
                        tvTip.setText("");
                        Tvjoin.setText("");
                        tvTeamName.setText("");
                        tvTime.setText(sayBean.getTime());
                        tvDesc.setText(sayBean.getContent());
                        tvRefuse.setVisibility(View.GONE);
                        tvAgree.setVisibility(View.GONE);
                        ivClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listener.onDel(position);
                            }
                        });
                        return;
                    }
                }

                if (sayBean.getNickName().equals(Constants.userName)) {
                    tvName.setText("你");
                } else {
                    tvName.setText(sayBean.getNickName());
                }
                if (!TextUtils.isEmpty(sayBean.getHeader())) {
                    Glide.with(context).load(Constants.base_url + sayBean.getHeader()).asBitmap().into(ivHead);
                } else {
                    ivHead.setImageResource(R.mipmap.head3);
                }
                tvTime.setText(sayBean.getTime());
                tvDesc.setText(sayBean.getContent());
                if (sayBean.getClubName() != null) {
                    tvTeamName.setText(sayBean.getClubName());
                }
                tvRefuse.setVisibility(sayBean.getStatus() == 0 ? View.VISIBLE : View.GONE);
                tvAgree.setVisibility(sayBean.getStatus() == 0 ? View.VISIBLE : View.GONE);
                tvTip.setVisibility(sayBean.getStatus() == 0 ? View.GONE : View.VISIBLE);
                tvTip.setText(sayBean.getStatus() == 2 ? "已同意" : "已拒绝");
                if (sayBean.getStatus() == 5) {
                    tvName.setVisibility(View.GONE);
                    Tvjoin.setVisibility(View.GONE);
                    tvName.setText("你");
                    Tvjoin.setText("已被踢出");
                    tvTip.setVisibility(View.GONE);
                }
                tvTip.setTextColor(Color.parseColor(sayBean.getStatus() == 2 ? "#2CD22B" : "@color/light_gray"));
                tvRefuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onRefuse(position);
                    }
                });

                tvAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onAgree(position);
                    }
                });

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onDel(position);
                    }
                });

                ivHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", sayBean.getReplyId()));
                    }
                });
            }
        }
    }

    public interface ItemEventListener {
        void onRefuse(int position);

        void onAgree(int position);

        void onDel(int position);
    }
}
