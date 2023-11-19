package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.bean.ConsTants;

import java.util.ArrayList;
import java.util.List;


/**
 * Hulk_Zhang on 2017/6/30 11:26
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class GwPictureAdapter extends BaseAdapter {
    private Context context;
    private List<String> data = new ArrayList<>();

    public GwPictureAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> data) {
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
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_picture, null);
            viewHolder = new ViewHolder();
            viewHolder.ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ViewGroup.LayoutParams p = viewHolder.ivPicture.getLayoutParams();
        p.height = ConsTants.screenW * 22 / 100;
        Glide.with(context).load(data.get(position) + "").asBitmap().fitCenter()
                .placeholder(R.mipmap.tjtp).into(viewHolder.ivPicture);
        return view;
    }

    public class ViewHolder {
        ImageView ivPicture;
    }
}
