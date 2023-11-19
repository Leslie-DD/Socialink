package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.activity.team.ImagePreviewActivity;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.P;

import java.util.ArrayList;

/**
 * Created by dell on 2020/5/12.
 */

public class FriendPictureAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> data = new ArrayList<>();
    private int items;

    public FriendPictureAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<String> data) {
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_picture, null);
            holder = new ViewHolder();
            holder.ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ViewGroup.LayoutParams p = holder.ivPicture.getLayoutParams();
        p.height = ConsTants.screenW * 10 / 22;
        Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId=" + data.get(position)).asBitmap()
                .fitCenter().placeholder(R.mipmap.mrtp).into(holder.ivPicture);

        holder.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImagePreviewActivity.class);
                intent.putStringArrayListExtra("imageList", data);
                intent.putExtra(P.START_ITEM_POSITION, 0);
                intent.putExtra(P.START_IAMGE_POSITION, 0);
                intent.putExtra("isdel2", false);
                context.startActivity(intent);
            }
        });
        return view;
    }

    public class ViewHolder {
        ImageView ivPicture;
    }
}
