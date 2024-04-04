package com.leslie.socialink.adapter.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.bean.ZcBean;
import com.leslie.socialink.network.Constants;

import java.util.ArrayList;
import java.util.List;


public class ZcWwAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ZcBean> data = new ArrayList<>();
    private int type;

    public ZcWwAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ZcBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setType(int type) {
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
            p.width = ConsTants.screenW * 20 / 70;
            p.height = ConsTants.screenW * 13 / 70;
            ZcBean bean = data.get(position);
            if (bean != null) {
                tvTitle.setText(data.get(position).title + "");
                Glide.with(context).load(Constants.BASE_URL + bean.coverImage).asBitmap().fitCenter()
                        .placeholder(R.mipmap.mrtp).into(ivImg);
                tvTime.setText(bean.time + "");
                tvName.setText(bean.presentorName + "");
                tvJob.setText(bean.presentorJob + "");
                if (type == 3) {
                    tvEnd.setText("查看详情");
                    tvEnd.setTextColor(context.getResources().getColor(R.color.colorPrimary, null));
                } else {
                    tvEnd.setText("截止时间: " + bean.endTime + "");
                    tvEnd.setTextColor(Color.parseColor("#666666"));
                }
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDoClickListener != null) {
                        mDoClickListener.doSave(position);
                    }
                }
            });
        }
    }

    private DoClickListener mDoClickListener;

    public interface DoClickListener {
        void doSave(int position);
    }

    public void DoSaveListener(DoClickListener mDoClickListener) {
        this.mDoClickListener = mDoClickListener;
    }
}
