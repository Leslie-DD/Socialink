package com.leslie.socialink.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;

import java.util.ArrayList;


public class ActiviteDateilAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> data = new ArrayList<>();

    public ActiviteDateilAdapter(Context context, ArrayList<String> data) {
        this.data = data;
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
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_activity_dateil, null);
            viewHolder = new ViewHolder();
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(data.get(position)).asBitmap().into(viewHolder.iv);

        return convertView;
    }


    public class ViewHolder {
        ImageView iv;
    }
}
