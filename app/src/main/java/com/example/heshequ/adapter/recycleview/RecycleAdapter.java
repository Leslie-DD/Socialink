/**
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */

package com.example.heshequ.adapter.recycleview;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.heshequ.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dev06
 *         2016年7月4日
 */

public class RecycleAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> data = new ArrayList<>();

    public RecycleAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_info, parent, false);
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
        private TextView tvKey;
        public ViewHolder(View view) {
            super(view);
            tvKey= (TextView) view.findViewById(R.id.tv_jiage1);
        }
        public void setData(final int position) {
            tvKey.setText(data.get(position));
        }
    }
}
