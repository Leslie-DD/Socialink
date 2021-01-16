package com.example.heshequ.adapter.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshequ.activity.wenwen.ZcQusetionActivity;
import com.example.heshequ.bean.ZcSecondBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Hulk_Zhang on 2017/6/30 11:26
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class ZcSecondAdapter extends BaseAdapter {
    private ZcQusetionActivity context;
    private List<ZcSecondBean> data=new ArrayList<>();
    private int clum;
    public ZcSecondAdapter(ZcQusetionActivity context,int clum) {
        this.context=context;
        this.clum=clum;
    }
    public void setData(List<ZcSecondBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_zc_second,null);
            viewHolder = new ViewHolder();
            viewHolder.tvName= (TextView) view.findViewById(R.id.tvName);
            viewHolder.tvDelete= (TextView) view.findViewById(R.id.tvdelete);
            viewHolder.tvContent= (TextView) view.findViewById(R.id.tvContent);
            viewHolder.ivZj = (ImageView) view.findViewById(R.id.ivZj);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        ZcSecondBean bean=data.get(position);
        viewHolder.tvDelete.setTag(position);
        if (bean!=null){
            viewHolder.tvContent.setText(bean.content+"");
            if (bean.uid!=null){
                if (bean.uid.equals(Constants.uid+"")){
                    viewHolder.tvDelete.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.tvDelete.setVisibility(View.GONE);
                }
            }
            if (bean.anonymity==0){
                viewHolder.tvName.setText(bean.nn+"");
            }else{
                viewHolder.tvName.setText("匿名用户");
            }
            if (bean.roleId!=null) {
                try {
                    if (bean.anonymity == 0) {
                        if (Integer.parseInt(bean.roleId) == 20) {
                            viewHolder.ivZj.setVisibility(View.VISIBLE);
                        }else{
                            viewHolder.ivZj.setVisibility(View.GONE);
                        }
                    }else{
                        viewHolder.ivZj.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                viewHolder.ivZj.setVisibility(View.GONE);
            }
        }
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item= (int) v.getTag();
                context.doDelete(data.get(item).id+"",clum,item);
//                data.remove(item);
//                notifyDataSetChanged();
            }
        });
        return view;
    }
    public class ViewHolder{
        TextView tvName,tvDelete,tvContent;
        ImageView ivZj;
    }
}
