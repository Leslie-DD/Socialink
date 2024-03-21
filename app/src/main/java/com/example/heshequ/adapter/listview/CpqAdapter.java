package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.heshequ.R;
import com.example.heshequ.bean.QuestionBean;

import java.util.ArrayList;



public class CpqAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<QuestionBean> data = new ArrayList<>();

    public CpqAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<QuestionBean> data) {
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
            view = LayoutInflater.from(context).inflate(R.layout.cpq_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvContent = view.findViewById(R.id.tvContent);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvContent.setText(data.get(position).getContent());
        return view;
    }

    public class ViewHolder {
        TextView tvContent;
    }

}
