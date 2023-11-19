package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.bean.ItemBean;

import java.util.ArrayList;


/**
 * Hulk_Zhang on 2017/6/30 11:26
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class ItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemBean> data = new ArrayList<>();

    public ItemAdapter(Context context, ArrayList<ItemBean> data) {
        this.data = data;
        this.context = context;
    }

    public void setData(ArrayList<ItemBean> data) {
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
        ItemBean bean = data.get(position);
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.set_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
            viewHolder.tvTip = (TextView) view.findViewById(R.id.tvTip);
            viewHolder.v1 = view.findViewById(R.id.v1);
            viewHolder.v2 = view.findViewById(R.id.v2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (bean.getResId() == 0) {
            viewHolder.ivIcon.setVisibility(View.GONE);
        } else {
            viewHolder.ivIcon.setImageResource(bean.getResId());
            viewHolder.ivIcon.setVisibility(View.VISIBLE);
        }

        viewHolder.tvName.setText(bean.getName());
        viewHolder.tvTip.setText(bean.getTip());
        viewHolder.v1.setVisibility(bean.getStatus() == 0 ? View.VISIBLE : View.GONE);
        viewHolder.v2.setVisibility(bean.getStatus() == 0 ? View.GONE : View.VISIBLE);
        return view;
    }

    public class ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvTip;
        View v1, v2;
    }
}
