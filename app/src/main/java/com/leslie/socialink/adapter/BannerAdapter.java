package com.leslie.socialink.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.leslie.socialink.R;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends LoopPagerAdapter {
    private List<String> data = new ArrayList<>();
    private final Context context;
    private onBanneritemClickListener listener;

    public BannerAdapter(RollPagerView viewPager, Context context) {
        super(viewPager);
        this.context = context;
    }

    public void setData(List<String> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setonBannerItemClickListener(onBanneritemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        ImageView view = new ImageView(container.getContext());
//        view.setTag(position);
        Glide.with(context).load(data.get(position) + "").asBitmap().centerCrop().error(R.mipmap.banner).into(view);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setOnClickListener(view1 -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getRealCount() {
        return data.size();
    }

    public interface onBanneritemClickListener {
        void onItemClick(int position);
    }
}
