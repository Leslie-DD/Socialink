package com.example.heshequ.activity.newsencond;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.taobao.accs.AccsClientConfig.getContext;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.example.heshequ.activity.WebActivity;
import com.example.heshequ.adapter.MyBannerAdapter;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.HomeBannerImgsBean;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.PicViewHolder> {

    private static final String TAG = PicViewHolder.class.getSimpleName();

    private Context mContext;

    private List<String> mList = new ArrayList<>();     // RecyclerView
    private List<String> mList2 = new ArrayList<>();    // 轮播图
    private List<HomeBannerImgsBean> mList3 = new ArrayList<>();    // 轮播图2

    private MyBannerAdapter bannerAdapter;

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

        bannerAdapter = new MyBannerAdapter(holder.rpSliderBanner, getContext());
        holder.rpSliderBanner.setAdapter(bannerAdapter);
        bannerAdapter.setData(mList2);
        holder.rpSliderBanner.setPlayDelay(3000);
        holder.rpSliderBanner.setAnimationDurtion(500);
        holder.rpSliderBanner.setHintView(new ColorPointHintView(getContext(), Color.parseColor("#00bbff"), Color.WHITE));
        bannerAdapter.setonBanneritemClickListener(new MyBannerAdapter.onBanneritemClickListener() {
            @Override
            public void onItemClick(int position) {
                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_firstBanner");
                startActivity(new Intent(getContext(), WebActivity.class)
                        .putExtra("url", mList3.get(position).getLinkUrl()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "你点击的是Banner", Toast.LENGTH_SHORT).show();
            }
        });
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