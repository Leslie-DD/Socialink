package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.activity.team.PersonalInformationActivity;
import com.example.heshequ.bean.GoodsdiscussBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2019/8/27.
 */

public class GoodsecondAdapter extends RecyclerView.Adapter {
    private Context context;


    private List<GoodsdiscussBean> data = new ArrayList<>();
    private View views;
    private DelListener listener;

    public GoodsecondAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<GoodsdiscussBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public List<GoodsdiscussBean> getData() {
        return data;
    }

    public void setListener(DelListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        views = LayoutInflater.from(context).inflate(R.layout.item_good_second_discuss, parent, false);
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
        TextView tvName, tvTime, tvContent, tvDel;

        public ViewHolder(View view) {
            super(view);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            tvDel = view.findViewById(R.id.tvDel);
        }

        public void setData(final int position) {
            final GoodsdiscussBean bean = data.get(position);
            if (bean != null) {
                Glide.with(context).load(WenConstans.BaseUrl + bean.header).asBitmap()
                        .fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                tvName.setText(bean.nn + "");
                tvTime.setText(bean.time + "");
                tvContent.setText(bean.content + "");
                if (bean.uid.equals(Constants.uid + "")) {
                    tvDel.setVisibility(View.VISIBLE);
                } else {
                    tvDel.setVisibility(View.GONE);
                }
            }
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", Integer.parseInt(bean.uid)));
                }
            });
            tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnDel(position);
                    }
                }
            });
        }
    }

    public interface DelListener {
        void OnDel(int position);
    }
}
