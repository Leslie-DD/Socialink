package com.example.heshequ.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Hulk_Zhang on 2018/5/8 16:48
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class MyBannerAdapter extends LoopPagerAdapter {
    private List<String> data=new ArrayList<>();
    private Context context;
    private onBanneritemClickListener listener;
    public MyBannerAdapter(RollPagerView viewPager, Context context) {
        super(viewPager);
        this.context=context;
    }

    public void setData(List<String> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setonBanneritemClickListener(onBanneritemClickListener listener){
        this.listener = listener;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        ImageView view = new ImageView(container.getContext());
//        view.setTag(position);
        Glide.with(context).load(data.get(position)+"").asBitmap().centerCrop().error(R.mipmap.banner).into(view);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        //view.setImageResource(R.mipmap.ic_launcher);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("ying","点击的是第"+position+"张图片");
                if (listener!=null){
                    listener.onItemClick(position);
                }
            }
        });
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getRealCount() {
        return data.size();
    }

    public interface onBanneritemClickListener{
        void onItemClick(int position);
    }
}
