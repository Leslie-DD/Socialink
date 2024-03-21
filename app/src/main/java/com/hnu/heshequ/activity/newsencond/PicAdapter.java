package com.hnu.heshequ.activity.newsencond;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.WebActivity;
import com.hnu.heshequ.adapter.BannerAdapter;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.bean.HomeBannerImgsBean;
import com.hnu.heshequ.utils.Utils;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.PicViewHolder> {

    private static final String TAG = PicViewHolder.class.getSimpleName();

    private Context mContext;

    private List<String> mList = new ArrayList<>();     // RecyclerView
    private List<String> mList2 = new ArrayList<>();    // 轮播图
    private List<HomeBannerImgsBean> mList3 = new ArrayList<>();    // 轮播图2

    private BannerAdapter bannerAdapter;

    public PicAdapter(Context context, List<String> list, List<String> list2, List<HomeBannerImgsBean> list3) {
        mContext = context;
        mList = list;
        mList2 = list2;
        mList3 = list3;
    }

    @NonNull
    @Override
    public PicAdapter.PicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.slide_banner_include_item, parent, false);
        return new PicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicAdapter.PicViewHolder holder, int position) {

        bannerAdapter = new BannerAdapter(holder.rpSliderBanner, mContext);
        holder.rpSliderBanner.setAdapter(bannerAdapter);
        bannerAdapter.setData(mList2);
        holder.rpSliderBanner.setPlayDelay(3000);
        holder.rpSliderBanner.setAnimationDurtion(500);
        holder.rpSliderBanner.setHintView(new ColorPointHintView(mContext, Color.parseColor("#00bbff"), Color.WHITE));
        bannerAdapter.setonBannerItemClickListener(position1 ->
                mContext.startActivity(new Intent(mContext, WebActivity.class).putExtra("url", mList3.get(position1).getLinkUrl()))
        );

        holder.itemView.setOnClickListener(v -> Utils.toastShort(mContext, "你点击的是Banner"));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class PicViewHolder extends RecyclerView.ViewHolder {

        RollPagerView rpSliderBanner;

        public PicViewHolder(View itemView) {
            super(itemView);
            rpSliderBanner = itemView.findViewById(R.id.rp_slider_banner);
            // 设置轮播图的高度
            ViewGroup.LayoutParams params = rpSliderBanner.getLayoutParams();
            params.height = ConsTants.screenW * 10 / 22;

        }
    }

}