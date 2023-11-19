package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.activity.team.PersonalInformationActivity;
import com.example.heshequ.bean.DynamicComment;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2020/5/8.
 */

public class NewSecondAdapter extends RecyclerView.Adapter {
    private Context context;


    private List<DynamicComment> data = new ArrayList<>();
    private View views;
    private DelListener listener;

    public NewSecondAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DynamicComment> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public List<DynamicComment> getData() {
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
            final DynamicComment bean = data.get(position);
            if (bean != null) {
                Glide.with(context).load(WenConstans.BaseUrl + "").asBitmap()
                        .fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                tvName.setText(bean.criticsName + "");
                tvTime.setText(bean.gmt_create + "");
                tvContent.setText(bean.content + "");
                if ((bean.critics.toString()).equals(Constants.uid + "")) {
                    tvDel.setVisibility(View.VISIBLE);
                } else {
                    tvDel.setVisibility(View.GONE);
                }
            }
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", bean.critics));
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
