package com.example.heshequ.adapter.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.activity.knowledge.ArticleDetialActivity;
import com.example.heshequ.bean.knowledge.ArticleSimpleBean;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColumnDetailAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ArticleSimpleBean> data = new ArrayList<>();
    private HashMap<String, List<ArticleSimpleBean>> map = new HashMap<>();
    private LayoutInflater inflater;
    private Gson gson = new Gson();
    private ArrayList<String> imgs;

    private View views;

    public void setData(List<ArticleSimpleBean> data){
        this.data = data;
        this.notifyDataSetChanged();
    }

    public ColumnDetailAdapter(Context context) {
        super();
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            views = LayoutInflater.from(context).inflate(R.layout.item_article_simple, parent, false);
        }
        return new ViewHolder(views, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);

    }
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvApprove,tvTitle, tvComment, tvTime;

        public ViewHolder(View view,int type) {
            super(view);
            if (type == 0) {
                tvApprove = (TextView) view.findViewById(R.id.agree);
                tvTitle = view.findViewById(R.id.tvTitles);
                tvComment = view.findViewById(R.id.comment);
                tvTime = view.findViewById(R.id.time);
            }
        }

        public void setData(final int position) {

            tvApprove.setText(""+data.get(position).likeNum);

            tvTitle.setText(data.get(position).title == null ? "" : data.get(position).title);
            tvComment.setText(""+data.get(position).commentNum);
            tvTime.setText(""+data.get(position).createTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MobclickAgent.onEvent(MeetApplication.getInstance(),"event_firstHotAsk");
                    MobclickAgent.onEvent(MeetApplication.getInstance(),"event_commentController");

                    Intent intent = new Intent(context, ArticleDetialActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ArticleId", data.get(position).id);
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
