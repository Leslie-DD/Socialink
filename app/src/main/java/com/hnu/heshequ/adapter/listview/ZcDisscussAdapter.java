package com.hnu.heshequ.adapter.listview;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.wenwen.ZcQusetionActivity;
import com.hnu.heshequ.bean.ZcQuestionBean;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.network.Constants;
import com.hnu.heshequ.view.CircleView;
import com.hnu.heshequ.view.MyLv;

import java.util.ArrayList;
import java.util.List;


public class ZcDisscussAdapter extends RecyclerView.Adapter {
    private ZcQusetionActivity context;
    private List<ZcQuestionBean> data = new ArrayList<>();
    private View views;

    public ZcDisscussAdapter(ZcQusetionActivity context) {
        this.context = context;
    }

    public void setData(List<ZcQuestionBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        views = LayoutInflater.from(context).inflate(R.layout.item_zc_disscuss, parent, false);
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
        private TextView tvName, tvTime, tvDing, tvContent, tvResult, tvTo;
        private MyLv lv;
        private LinearLayout llBg;

        public ViewHolder(View view) {
            super(view);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvDing = (TextView) view.findViewById(R.id.tvDing);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            tvResult = (TextView) view.findViewById(R.id.tvResult);
            tvTo = (TextView) view.findViewById(R.id.tvTo);
            llBg = (LinearLayout) view.findViewById(R.id.llBg);
            lv = (MyLv) view.findViewById(R.id.lv);
        }

        public void setData(final int position) {
            ZcQuestionBean bean = data.get(position);
            if (bean != null) {
                if (bean.uid.equals("" + Constants.uid)) {
                    tvTo.setVisibility(View.GONE);
                } else {
                    tvTo.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(bean.anonymity)) {
                    Glide.with(context).load(WenConstans.BaseUrl + bean.header).asBitmap()
                            .fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                    tvName.setText(bean.nn + "");
                } else {
                    if (bean.anonymity.equals("1")) {
                        ivHead.setImageResource(R.mipmap.head3);
                        tvName.setText("匿名用户");
                    } else {
                        Glide.with(context).load(WenConstans.BaseUrl + bean.header).asBitmap()
                                .fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                        tvName.setText(bean.nn + "");
                    }

                }
                tvTime.setText(bean.time + "");
                tvContent.setText(bean.title + "");
                ZcSecondAdapter adapter = new ZcSecondAdapter(context, position);
                lv.setAdapter(adapter);


                if (bean.commentVos == null || bean.commentVos.size() == 0) {
                    llBg.setVisibility(View.GONE);
                    tvResult.setText("回复 (" + 0 + ")");
                } else {
                    llBg.setVisibility(View.VISIBLE);
                    tvResult.setText("回复 (" + bean.commentVos.size() + ")");
                    adapter.setData(bean.commentVos);
                }
                tvDing.setText(bean.likeAmount + "顶");
                if (TextUtils.isEmpty(bean.userLike)) {
                    tvDing.setTextColor(Color.parseColor("#939393"));
                } else {
                    tvDing.setTextColor(Color.parseColor("#05bcff"));
                }
            }
            tvTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.doSend(position);
                }
            });

            tvDing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.doDing(position);
                }
            });
        }
    }
}
