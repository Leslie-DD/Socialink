package com.example.heshequ.adapter.recycleview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshequ.R;
import com.example.heshequ.activity.oldsecond.SecondFragment;
import com.example.heshequ.bean.GoodLabel;

import java.util.List;

/**
 * Created by dell on 2019/8/30.
 */

public class LableAdapter extends RecyclerView.Adapter<LableAdapter.ViewHolder> {
    private Context context;
    //    private SecondhandActivity activity;
    private SecondFragment fragment;
    private List<GoodLabel> mLabelList;
    private String label = "";
    private String temp;
    private int pn = 1;
    private int ps = 20;
    private XXListener mXXListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View labelview;
        TextView labelName;

        public ViewHolder(View view) {
            super(view);
            labelview = view;
            labelName = (TextView) view.findViewById(R.id.labelName);
        }
    }

    public LableAdapter(List<GoodLabel> labelList, Context context) {

        mLabelList = labelList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.label_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.labelview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                GoodLabel label = mLabelList.get(position);
                Toast.makeText(v.getContext(), "you	clicked	image	" + label.getName(), Toast.LENGTH_SHORT).show();
                temp = label.getName();
                setLabel(temp);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        GoodLabel goodLabel = mLabelList.get(position);
        holder.labelName.setText(goodLabel.getName());

        holder.labelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ddd", position + "");
//                ((SecondhandActivity)context).setTvBg(position);
//                (fragment).setTvBg(position);

            }
        });
    }

    public interface XXListener {
        public void onXXClick();
    }

    public void setOnXXClickListener(XXListener XXListener) {
        this.mXXListener = XXListener;
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
