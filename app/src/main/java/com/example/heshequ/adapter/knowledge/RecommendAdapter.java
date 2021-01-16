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
import com.example.heshequ.activity.knowledge.ArticleDetialActivity;
import com.example.heshequ.bean.knowledge.RecommendItemBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RecommendAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<RecommendItemBean> data = new ArrayList<>();
    private HashMap<String, List<RecommendItemBean>> map = new HashMap<>();
    private LayoutInflater inflater;
    private Gson gson = new Gson();
    private ArrayList<String> imgs;

    private View views;

    public void setData(List<RecommendItemBean> data){
        this.data = data;
        this.notifyDataSetChanged();
    }

    public RecommendAdapter(Context context) {
        super();
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            views = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        }
        else{

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
        private TextView tvName,tvTitle,tvContent,tvAgree,tvComment;

        public ViewHolder(View view,int type) {
            super(view);
            if (type == 0) {
                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvTitle = view.findViewById(R.id.tvTitles);
                tvContent = view.findViewById(R.id.tvContent);
                tvAgree = view.findViewById(R.id.agree);
                tvComment = view.findViewById(R.id.comment);
            }
        }

        public void setData(final int position) {


            Log.e("RecommendAdapter", data.get(position).toString());
            if (TextUtils.isEmpty(data.get(position).author.header)) {
                //Log.e("shownearnull",""+ Constants.base_url_tmp +"/api/payforkownledge/passage/feed.do?pageSize=10&pageNum=1&isAsc=asc&orderByColumn=id"+ data.get(position).author.header);
                ivHead.setImageResource(R.mipmap.head3);
            } else {
                // 协议完善协议完善
                // Glide.with(context).load(Constants.base_url_tmp + "/info/file/pub.do?fileId="+data.get(position).author.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                //Log.e("shownear",""+Constants.base_url_tmp +"/info/file/pub.do?fileId="+ data.get(position).author.header);
            }

            tvName.setText(data.get(position).author.nickname == null ? "" : data.get(position).author.nickname);

            tvTitle.setText(data.get(position).title == null ? "" : data.get(position).title);
            tvContent.setText(data.get(position).content == null ? "" : data.get(position).content);
            tvAgree.setText(""+data.get(position).likeNum);
            tvComment.setText(""+data.get(position).commentNum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MobclickAgent.onEvent(MeetApplication.getInstance(),"event_firstHotAsk");
                    if (Objects.equals(data.get(position).author.id, Constants.uid + "")){
                        MobclickAgent.onEvent(MeetApplication.getInstance(),"event_myQuestionClick");
                    }
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
