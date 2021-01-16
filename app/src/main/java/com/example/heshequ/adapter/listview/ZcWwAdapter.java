package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.ZcBean;
import com.example.heshequ.constans.WenConstans;
import com.bumptech.glide.Glide;
import com.example.heshequ.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Hulk_Zhang on 2017/6/30 11:26
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class ZcWwAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ZcBean> data=new ArrayList<>();
    private int type;

    public ZcWwAdapter(Context context) {
        this.context=context;
    }
    public void setData(List<ZcBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_zc, parent, false);
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
        ImageView ivImg;
        TextView tvTitle, tvTime, tvName, tvJob, tvEnd;
        public ViewHolder(View view) {
            super(view);
            ivImg = (ImageView) view.findViewById(R.id.ivImg);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvJob = (TextView) view.findViewById(R.id.tvJob);
            tvEnd = (TextView) view.findViewById(R.id.tvEnd);
        }
        public void setData(final int position) {
            ViewGroup.LayoutParams p = ivImg.getLayoutParams();
            p.width= ConsTants.screenW*20/70;
            p.height= ConsTants.screenW*13/70;
            ZcBean bean=data.get(position);
            if (bean!=null){
                tvTitle.setText(data.get(position).title + "");
                Glide.with(context).load(WenConstans.BaseUrl+bean.coverImage).asBitmap().fitCenter()
                        .placeholder(R.mipmap.mrtp).into(ivImg);
                tvTime.setText(bean.time+"");
                tvName.setText(bean.presentorName+"");
                tvJob.setText(bean.presentorJob+"");
                if (type==3){
                    tvEnd.setText("查看详情");
                    tvEnd.setTextColor(Color.parseColor("#00bbff"));
                }else{
                    tvEnd.setText("截止时间: "+bean.endTime+"");
                    tvEnd.setTextColor(Color.parseColor("#666666"));
                }
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDoClickListener!=null){
                        mDoClickListener.doSave(position);
                    }
                }
            });
        }
    }
    private DoClickListener mDoClickListener;
    public interface DoClickListener{
        void doSave(int position);
    }
    public void DoSaveListener(DoClickListener mDoClickListener){
        this.mDoClickListener=mDoClickListener;
    }
}
