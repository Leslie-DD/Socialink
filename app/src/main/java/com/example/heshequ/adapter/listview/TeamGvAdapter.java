package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.CircleView;

import java.util.ArrayList;
import java.util.List;



public class TeamGvAdapter extends BaseAdapter {
    private Context context;
    private List<String> data = new ArrayList<>();

    public TeamGvAdapter(Context context) {
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
        CircleView circleView = new CircleView(context);
        circleView.setLayoutParams(new ViewGroup.LayoutParams(Utils.dip2px(context, 34), Utils.dip2px(context, 34)));
        Log.e("TeamGvAdapter", data.get(position));
        if (TextUtils.isEmpty(data.get(position))) {
            circleView.setImageResource(R.mipmap.head3);
        } else {
            Glide.with(context).load(data.get(position)).asBitmap().into(circleView);
        }
        return circleView;
    }

    public class ViewHolder {

    }
}
