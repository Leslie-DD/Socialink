package com.leslie.socialink.adapter.listview;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.oldsecond.GoodDetailActivity;
import com.leslie.socialink.bean.GoodsdiscussBean;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.view.CircleView;

import java.util.ArrayList;
import java.util.List;


public class GoodDisscussAdapter extends RecyclerView.Adapter {
    private GoodDetailActivity context;
    private List<GoodsdiscussBean> data = new ArrayList<>();
    private View views;

    public GoodDisscussAdapter(GoodDetailActivity context) {
        this.context = context;
    }

    public void setData(List<GoodsdiscussBean> data) {
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
            GoodsdiscussBean bean = data.get(position);
            if (bean != null) {
                Glide.with(context).load(WenConstans.BaseUrl + bean.header).asBitmap()
                        .fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                tvName.setText(bean.nn + "");
                tvTime.setText(bean.time + "");
                tvContent.setText(bean.content + "");
                tvResult.setText("回复 (" + bean.commentAmount + ")");
                tvDing.setText(bean.likeAmount + "顶");
                if (TextUtils.isEmpty(bean.isTop)) {
                    tvDing.setTextColor(Color.parseColor("#939393"));
                } else {
                    tvDing.setTextColor(Color.parseColor("#05bcff"));
                }
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.doSecond(position);
                }
            });
            tvDing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.doDing(position);
                }
            });
        }
    }
}
