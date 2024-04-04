package com.leslie.socialink.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.bean.ItemBean;

import java.util.List;


public class ItemAdapter extends BaseAdapter {
    private Context context;
    private List<ItemBean> data;

    public ItemAdapter(Context context, List<ItemBean> data) {
        this.data = data;
        this.context = context;
    }

    public void setData(List<ItemBean> data) {
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
        ItemBean bean = data.get(position);
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.set_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = view.findViewById(R.id.ivIcon);
            viewHolder.tvName = view.findViewById(R.id.tvName);
            viewHolder.tvTip = view.findViewById(R.id.tvTip);
            viewHolder.v1 = view.findViewById(R.id.v1);
            viewHolder.v2 = view.findViewById(R.id.v2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (bean.getResId() == 0) {
            viewHolder.ivIcon.setVisibility(View.GONE);
        } else {
            viewHolder.ivIcon.setImageResource(bean.getResId());
            viewHolder.ivIcon.setVisibility(View.VISIBLE);
        }

        viewHolder.tvName.setText(bean.getName());
        viewHolder.tvTip.setText(bean.getTip());
        viewHolder.v1.setVisibility(bean.getStatus() == 0 ? View.VISIBLE : View.GONE);
        viewHolder.v2.setVisibility(bean.getStatus() == 0 ? View.GONE : View.VISIBLE);
        return view;
    }

    public class ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvTip;
        View v1, v2;
    }
}
