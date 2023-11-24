package com.example.heshequ.adapter.recycleview;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.heshequ.R;
import com.example.heshequ.bean.GoodLabel;

import java.util.List;

/**
 * Created by dell on 2019/8/30.
 */

public class LableAdapter extends RecyclerView.Adapter<LableAdapter.ViewHolder> {
    private final List<GoodLabel> mLabelList;
    private String label = "";
    private String temp;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View labelview;
        TextView labelName;

        public ViewHolder(View view) {
            super(view);
            labelview = view;
            labelName = (TextView) view.findViewById(R.id.labelName);
        }
    }

    public LableAdapter(List<GoodLabel> labelList) {
        mLabelList = labelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.labelview.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            GoodLabel label = mLabelList.get(position);
            temp = label.getName();
            setLabel(temp);
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        GoodLabel goodLabel = mLabelList.get(position);
        holder.labelName.setText(goodLabel.getName());
        holder.labelName.setOnClickListener(view -> Log.e("ddd", position + ""));
    }

    @Override
    public int getItemCount() {
        return mLabelList.size();
    }

    public void setLabel(String temp) {
        label = temp;
    }

    public String getLabel() {
        return label;
    }
}
