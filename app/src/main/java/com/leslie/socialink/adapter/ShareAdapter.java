package com.leslie.socialink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.bean.ShareBean;

import java.util.ArrayList;

/**
 * @author dev06
 * 2016年7月4日
 */
public class ShareAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ShareBean> data;

    public ShareAdapter(Context context, ArrayList<ShareBean> data) {
        super();
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<ShareBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        ShareBean sb = data.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.share_item, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(sb.getName());
        holder.ivIcon.setImageResource(sb.getResid());

        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        ImageView ivIcon;
    }
}
