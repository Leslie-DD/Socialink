package com.hnu.heshequ.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.bean.ItemBean;

import java.util.ArrayList;

public class BaseInfoItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ItemBean> data = new ArrayList<>();

    public BaseInfoItemAdapter(Context context, ArrayList<ItemBean> data) {
        this.data = data;
        this.context = context;
    }

    public void setData(ArrayList<ItemBean> data) {
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
        BaseInfoItemAdapter.ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_base_info, null);
            viewHolder = new BaseInfoItemAdapter.ViewHolder();
            viewHolder.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
            viewHolder.tvTip = (TextView) view.findViewById(R.id.tvTip);
            viewHolder.v1 = view.findViewById(R.id.v1);
            viewHolder.v2 = view.findViewById(R.id.v2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (BaseInfoItemAdapter.ViewHolder) view.getTag();
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
