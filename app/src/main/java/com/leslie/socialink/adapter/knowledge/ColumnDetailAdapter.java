package com.leslie.socialink.adapter.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.knowledge.ArticleDetialActivity;
import com.leslie.socialink.bean.knowledge.ArticleSimpleBean;

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

    public void setData(List<ArticleSimpleBean> data) {
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

        private TextView tvApprove, tvTitle, tvComment, tvTime;

        public ViewHolder(View view, int type) {
            super(view);
            if (type == 0) {
                tvApprove = (TextView) view.findViewById(R.id.agree);
                tvTitle = view.findViewById(R.id.tvTitles);
                tvComment = view.findViewById(R.id.comment);
                tvTime = view.findViewById(R.id.time);
            }
        }

        public void setData(final int position) {

            tvApprove.setText("" + data.get(position).likeNum);

            tvTitle.setText(data.get(position).title == null ? "" : data.get(position).title);
            tvComment.setText("" + data.get(position).commentNum);
            tvTime.setText("" + data.get(position).createTime);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ArticleDetialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ArticleId", data.get(position).id);
                intent.putExtras(bundle);
                context.startActivity(intent);

            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
