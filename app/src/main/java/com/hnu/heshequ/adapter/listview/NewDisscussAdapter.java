package com.hnu.heshequ.adapter.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.friend.NewDetail;
import com.hnu.heshequ.bean.DynamicComment;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.view.CircleView;

import java.util.ArrayList;
import java.util.List;


public class NewDisscussAdapter extends RecyclerView.Adapter {
    private NewDetail context;
    private List<DynamicComment> data = new ArrayList<>();
    private View views;

    public NewDisscussAdapter(NewDetail context) {
        this.context = context;
    }

    public void setData(List<DynamicComment> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        views = LayoutInflater.from(context).inflate(R.layout.item_good_discuss, parent, false);
        return new ViewHolder(views);
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

        private CircleView ivHead;
        TextView tvName, tvTime, tvDing, tvContent, tvResult;

        public ViewHolder(View view) {
            super(view);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvDing = (TextView) view.findViewById(R.id.tvDing);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            tvResult = (TextView) view.findViewById(R.id.tvResult);
        }

        public void setData(final int position) {
            DynamicComment bean = data.get(position);
            if (bean != null) {
                Glide.with(context).load(WenConstans.BaseUrl + "/info/file/pub.do?fileId=ai/25_20180729071859_904.jpg").asBitmap()
                        .fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                tvName.setText(bean.criticsName + "");
                tvTime.setText(bean.gmt_create + "");
                tvContent.setText(bean.content + "");
                tvResult.setText("回复 ");
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.doSecond(position);
                }
            });
        }
    }
}
