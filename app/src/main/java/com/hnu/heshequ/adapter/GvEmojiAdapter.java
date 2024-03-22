package com.hnu.heshequ.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.utils.Utils;


public class GvEmojiAdapter extends BaseAdapter {
    private Context context;
    private String[] datas;

    public GvEmojiAdapter(Context context, String[] datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public Object getItem(int position) {
        return datas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_gvemoji, null);
            viewHolder = new ViewHolder();
            viewHolder.tvEmoji = (TextView) view.findViewById(R.id.tvEmoji);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvEmoji.setText(Utils.getEmoji(context, datas[position]));
        return view;
    }

    public class ViewHolder {
        TextView tvEmoji;
    }
}
