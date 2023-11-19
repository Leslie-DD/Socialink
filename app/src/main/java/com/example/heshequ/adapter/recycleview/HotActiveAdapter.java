/**
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */

package com.example.heshequ.adapter.recycleview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.team.ActivityDateilActivity;
import com.example.heshequ.activity.team.ImagePreviewActivity;
import com.example.heshequ.adapter.Adapter_GridView;
import com.example.heshequ.bean.HotAvtivityBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.P;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CircleView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dev06
 * 2016年7月4日
 */

public class HotActiveAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HotAvtivityBean.HotBean> data = new ArrayList<>();
    private View views;

    public HotActiveAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<HotAvtivityBean.HotBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setData2(ArrayList<HotAvtivityBean.HotBean> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        views = LayoutInflater.from(context).inflate(R.layout.item_hot_active, parent, false);
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

        private ImageView ivImg;

        private CircleView ivHead;
        private TextView tvName, tvTitle, tvTz, tvNum, tvFqr, tvTime, tvCollege;
        private GridView gw;

        public ViewHolder(View view) {
            super(view);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTz = (TextView) view.findViewById(R.id.tvTz);
            tvNum = (TextView) view.findViewById(R.id.tvNum);
            tvFqr = (TextView) view.findViewById(R.id.tvFqr);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvCollege = (TextView) view.findViewById(R.id.tvCollege);
            gw = (GridView) view.findViewById(R.id.gw);
        }

        @SuppressLint("SetTextI18n")
        public void setData(final int position) {
            final HotAvtivityBean.HotBean hotBean = data.get(position);

            if (!TextUtils.isEmpty(hotBean.getClubInfo().getLogoImage())) {
                Glide.with(context).load(Constants.base_url + hotBean.getClubInfo().getLogoImage()).asBitmap().into(ivHead);
            } else {
                Glide.with(context).load(R.mipmap.head3).asBitmap().into(ivHead);
            }
            tvName.setText("" + hotBean.getClubInfo().getName());
            tvCollege.setText("" + hotBean.getClubInfo().getCollege());
            tvTitle.setText(hotBean.getTitle());
            tvFqr.setText(hotBean.getPresentorName());
            if (hotBean.getPhotos() != null && hotBean.getPhotos().size() > 0) {
                gw.setVisibility(View.VISIBLE);
                switch (hotBean.getPhotos().size()) {
                    case 1:
                        gw.setNumColumns(1);
                        break;
                    case 2:
                        gw.setNumColumns(2);
                        break;
                    case 4:
                        gw.setNumColumns(2);
                        break;
                    default:
                        gw.setNumColumns(3);
                        break;
                }
                ArrayList<String> strings = new ArrayList<>();
                for (int i = 0; i < hotBean.getPhotos().size(); i++) {
                    strings.add(WenConstans.BaseUrl + hotBean.getPhotos().get(i).getPhotoId());
                }
                gw.setAdapter(new Adapter_GridView(context, strings));
                gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        List<HotAvtivityBean.HotBean.PhotosBean> photoList = data.get(position).getPhotos();
                        ArrayList<String> list = new ArrayList<String>();
                        if (photoList != null && photoList.size() > 0) {
                            for (int j = 0; j < photoList.size(); j++) {
                                list.add(WenConstans.BaseUrl + photoList.get(j).getPhotoId());
                            }
                        }
                        Intent intent = new Intent(context, ImagePreviewActivity.class);
                        intent.putStringArrayListExtra("imageList", list);
                        intent.putExtra(P.START_ITEM_POSITION, i);
                        intent.putExtra(P.START_IAMGE_POSITION, i);
                        intent.putExtra("isdel2", false);
                        context.startActivity(intent);
                    }
                });
            } else {
                gw.setVisibility(View.GONE);
            }
            tvTime.setText("报名截止时间:" + hotBean.getApplyDeadline());
            if (hotBean.getClubInfo() != null) {
                if (hotBean.getClubInfo().getCreatorName() != null) {
                    tvTz.setText(hotBean.getClubInfo().getCreatorName() + "");
                }

                tvNum.setText(hotBean.getClubInfo().getMemberNumber() + "");

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(MeetApplication.getInstance(), "event_firstHotActivity");
                    context.startActivity(
                            new Intent(context, ActivityDateilActivity.class)
                                    .putExtra("id", hotBean.getId())
                    );
                }
            });
        }
    }
}
