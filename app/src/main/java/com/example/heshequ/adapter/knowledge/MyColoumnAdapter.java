package com.example.heshequ.adapter.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.knowledge.MyColoumnDetailActivity;
import com.example.heshequ.bean.knowledge.SubscriptionItemBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.view.CircleView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyColoumnAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<SubscriptionItemBean> data = new ArrayList<>();
    private HashMap<String, List<SubscriptionItemBean>> map = new HashMap<>();
    private LayoutInflater inflater;
    private Gson gson = new Gson();
    private ArrayList<String> imgs;

    private View views;

    public void setData(List<SubscriptionItemBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public MyColoumnAdapter(Context context) {
        super();
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            views = LayoutInflater.from(context).inflate(R.layout.item_colomn, parent, false);
        }
        return new ViewHolder(views, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CircleView ivHead;
        private TextView tvName, tvTitle, tvContent, tvSub;

        public ViewHolder(View view, int type) {
            super(view);
            if (type == 0) {
                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvTitle = view.findViewById(R.id.tvTitles);
                tvContent = view.findViewById(R.id.tvContent);
                tvSub = view.findViewById(R.id.tvSub);
            }
        }

        public void setData(final int position) {


            if (TextUtils.isEmpty(data.get(position).author.header)) {
                Log.e("shownearnull", "" + Constants.base_url_tmp + "/api/payforkownledge/passage/feed.do?pageSize=10&pageNum=1&isAsc=asc&orderByColumn=id" + data.get(position).author.header);
                ivHead.setImageResource(R.mipmap.head3);
            } else {
                // 协议完善协议完善
                // Glide.with(context).load(Constants.base_url_tmp + "/info/file/pub.do?fileId="+data.get(position).author.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("shownear", "" + Constants.base_url_tmp + "/info/file/pub.do?fileId=" + data.get(position).author.header);
            }

            tvName.setText(data.get(position).author.nickname == null ? "" : data.get(position).author.nickname);

            tvTitle.setText(data.get(position).name == null ? "" : data.get(position).name);
            tvContent.setText(data.get(position).summary == null ? "" : data.get(position).summary);
            tvSub.setText("" + data.get(position).subscriptionNum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MobclickAgent.onEvent(MeetApplication.getInstance(), "event_firstHotAsk");
                    if (Objects.equals(data.get(position).author.id, Constants.uid + "")) {
                        MobclickAgent.onEvent(MeetApplication.getInstance(), "event_myQuestionClick");
                    }
                    MobclickAgent.onEvent(MeetApplication.getInstance(), "event_commentController");

                    Intent intent = new Intent(context, MyColoumnDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("columnId", data.get(position).id);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
