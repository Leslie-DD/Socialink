package com.hnu.heshequ.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.bean.ConsTants;

import java.util.ArrayList;
import java.util.List;


public class GwPictureAdapter extends BaseAdapter {
    private Context context;
    private List<String> data = new ArrayList<>();

    public GwPictureAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_picture, null);
            viewHolder = new ViewHolder();
            viewHolder.ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ViewGroup.LayoutParams p = viewHolder.ivPicture.getLayoutParams();
        p.height = ConsTants.screenW * 22 / 100;
        Glide.with(context).load(data.get(position) + "").asBitmap().fitCenter()
                .placeholder(R.mipmap.tjtp).into(viewHolder.ivPicture);
        return view;
    }

    public class ViewHolder {
        ImageView ivPicture;
    }
}
