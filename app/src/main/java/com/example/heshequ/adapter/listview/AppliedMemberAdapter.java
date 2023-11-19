package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.activity.team.PersonalInformationActivity;
import com.example.heshequ.bean.AppliedMemberBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.view.CircleView;

import java.util.ArrayList;

/**
 * Created by Dengdongqi on 2018/7/9.
 * Copyright © 2018, 长沙豆子信息技术有限公司, All rights reserved.
 */

public class AppliedMemberAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AppliedMemberBean> data = new ArrayList<>();

    public AppliedMemberAdapter(Context context, ArrayList<AppliedMemberBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(ArrayList<AppliedMemberBean> data) {
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
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_applied_member, null);
            viewHolder = new ViewHolder();
            //int
            viewHolder.ivHead = (CircleView) view.findViewById(R.id.ivHead);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
            viewHolder.tvRole = view.findViewById(R.id.tvRole);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (!TextUtils.isEmpty(data.get(position).getHeader())) {
            Glide.with(context).load(Constants.base_url + data.get(position).getHeader()).asBitmap().into(viewHolder.ivHead);
        } else {
            viewHolder.ivHead.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.head3));
        }
        viewHolder.tvName.setText(data.get(position).getNickname());
        if (data.get(position).getRole() == 1) {
            viewHolder.tvRole.setText("团长");
        } else if (data.get(position).getRole() == 2) {
            viewHolder.tvRole.setText("副团长");
        } else if (data.get(position).getRole() == 3) {
            viewHolder.tvRole.setText("成员");
        }

        viewHolder.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", data.get(position).getUid()));
            }
        });

        return view;
    }

    public class ViewHolder {
        private CircleView ivHead;
        private TextView tvName, tvRole;
    }
}
