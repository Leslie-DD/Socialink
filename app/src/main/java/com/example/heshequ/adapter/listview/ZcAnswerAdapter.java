package com.example.heshequ.adapter.listview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heshequ.activity.wenwen.ZcArticleActivity;
import com.example.heshequ.bean.ZcAnswerBean;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.view.MyLv;
import com.bumptech.glide.Glide;
import com.example.heshequ.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Hulk_Zhang on 2017/6/30 11:26
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class ZcAnswerAdapter extends RecyclerView.Adapter {
    private ZcArticleActivity context;
    private List<ZcAnswerBean> data=new ArrayList<>();
    private View views;
    public ZcAnswerAdapter(ZcArticleActivity context) {
        this.context=context;
    }
    public void setData(List<ZcAnswerBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        views = LayoutInflater.from(context).inflate(R.layout.item_zc_answer, parent, false);
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
        private TextView tvName,tvTime,tvContent,tvResult,tvTo;
        private MyLv lv;
        private LinearLayout llBg;

        public ViewHolder(View view) {
            super(view);
            ivHead= (CircleView) view.findViewById(R.id.ivHead);
            tvName= (TextView) view.findViewById(R.id.tvName);
            tvTime= (TextView) view.findViewById(R.id.tvTime);
            tvContent= (TextView) view.findViewById(R.id.tvContent);
            tvResult= (TextView) view.findViewById(R.id.tvResult);
            tvTo= (TextView) view.findViewById(R.id.tvTo);
            llBg= (LinearLayout) view.findViewById(R.id.llBg);
            lv= (MyLv) view.findViewById(R.id.lv);
        }
        public void setData(final int position) {
            ZcAnswerBean bean=data.get(position);
            if (bean!=null){
                if (bean.anonymity==0){
                    Glide.with(context).load(WenConstans.BaseUrl+bean.header)
                            .asBitmap().fitCenter().placeholder(R.mipmap.head3)
                            .into(ivHead);
                    tvName.setText(bean.nn+"");
                }else{
                    ivHead.setImageResource(R.mipmap.head3);
                    tvName.setText("匿名用户");
                }
                tvTime.setText(bean.time+"");
                tvContent.setText(bean.title+"");
                ZcSecondAdapter1 adapter=new ZcSecondAdapter1(context,position);
                lv.setAdapter(adapter);
                if (bean.commentVos==null||bean.commentVos.size()==0){
                    llBg.setVisibility(View.GONE);
                    tvResult.setText("回复 ("+0+")");
                }else{
                    llBg.setVisibility(View.VISIBLE);
                    tvResult.setText("回复 ("+bean.commentVos.size()+")");
                    adapter.setData(bean.commentVos);
                }
                tvTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.doSend(position);
                    }
                });
                ivHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //context.startActivity(new Intent(context,PersonalInformationActivity.class).putExtra("uid",data.get(position).uid));
                    }
                });
            }
        }
    }
}
