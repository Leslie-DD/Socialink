package com.leslie.socialink.adapter.listview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.entity.TestBean;

import java.util.ArrayList;
import java.util.List;


public class TjAdapter extends BaseAdapter {
    private Context context;
    private List<TestBean> data = new ArrayList<>();

    public TjAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<TestBean> data) {
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
        TestBean bean = data.get(position);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.tj_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ivPic = (ImageView) view.findViewById(R.id.ivPic);
            viewHolder.tvDay = (TextView) view.findViewById(R.id.tvDay);
            viewHolder.tvMonth = (TextView) view.findViewById(R.id.tvMonth);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvDay.setText(bean.getDay() + "");
        viewHolder.tvMonth.setText(bean.getMonth() + "月");
        viewHolder.tvName.setText(bean.getName() + "");
        if (TextUtils.isEmpty(bean.getUrl())) {
            Glide.with(context).load(bean.getUrl()).centerCrop().into(viewHolder.ivPic);
            viewHolder.ivPic.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivPic.setVisibility(View.GONE);
        }

        return view;
    }

    public class ViewHolder {
        private ImageView ivPic;
        private TextView tvDay, tvMonth, tvName;
    }
}
