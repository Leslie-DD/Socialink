package com.hnu.heshequ.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnu.heshequ.bean.FriendBean;

import java.util.ArrayList;


public class FriendfiltrateAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FriendBean> data = new ArrayList<>();

    public FriendfiltrateAdapter(Context context, ArrayList<FriendBean> data) {
        this.data = data;
        this.context = context;
    }

    public void setData(ArrayList<FriendBean> data) {
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
        FriendBean bean = data.get(position);
        ViewHolder viewHolder;

        return view;
    }

    public class ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvTip;
        View v1, v2;
    }
}
