package com.hnu.heshequ.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.PersonalInformationActivity;
import com.hnu.heshequ.bean.AppliedMemberBean;
import com.hnu.heshequ.network.Constants;
import com.hnu.heshequ.view.CircleView;

import java.util.ArrayList;


public class AppliedMemberAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AppliedMemberBean> data = new ArrayList<>();
    public boolean isTeamOwner = false;

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
//            viewHolder.tvRole = view.findViewById(R.id.tvRole);
            viewHolder.tvTelephone = view.findViewById(R.id.tvTelephone);
            viewHolder.tvRealName = view.findViewById(R.id.tvRealName);
            viewHolder.tvCollege = view.findViewById(R.id.tvCollege);
            viewHolder.ivSex = view.findViewById(R.id.ivSex);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (!TextUtils.isEmpty(data.get(position).getHeader())) {
            Glide.with(context).load(Constants.base_url + data.get(position).getHeader()).asBitmap().into(viewHolder.ivHead);
        } else {
            viewHolder.ivHead.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.head3));
        }
        AppliedMemberBean bean = data.get(position);
        viewHolder.tvName.setText(bean.getNickname());

//        if (bean.getRole() == 1) {
//            viewHolder.tvRole.setText("团长");
//        } else if (bean.getRole() == 2) {
//            viewHolder.tvRole.setText("副团长");
//        } else if (bean.getRole() == 3) {
//            viewHolder.tvRole.setText("成员");
//        } else {
//            viewHolder.tvRole.setVisibility(View.GONE);
//        }

        if (isTeamOwner) {
            if (bean.getTelephone() != null && !bean.getTelephone().equals("null") && !bean.getTelephone().isEmpty()) {
                viewHolder.tvTelephone.setText(bean.getTelephone());
                viewHolder.tvTelephone.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvTelephone.setVisibility(View.GONE);
            }

            if (bean.getName() != null) {
                viewHolder.tvRealName.setVisibility(View.VISIBLE);
                viewHolder.tvRealName.setText("（" + bean.getName() + "）");
            } else {
                viewHolder.tvRealName.setVisibility(View.GONE);
            }
        }

        if (bean.getCollege() != null && !bean.getCollege().equals("null") && !bean.getCollege().isEmpty()) {
            viewHolder.tvCollege.setVisibility(View.VISIBLE);
            viewHolder.tvCollege.setText(bean.getCollege());
        } else {
            viewHolder.tvCollege.setVisibility(View.GONE);
        }

        if (bean.getSex() == 1) {   // 男
            viewHolder.ivSex.setImageResource(R.mipmap.me19);
            viewHolder.ivSex.setVisibility(View.VISIBLE);
        } else if (bean.getSex() == 2) {    // 女
            viewHolder.ivSex.setImageResource(R.mipmap.me36);
            viewHolder.ivSex.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivSex.setVisibility(View.GONE);
        }

        viewHolder.ivHead.setOnClickListener(v -> {
            context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", bean.getUid()));
        });

        return view;
    }

    public class ViewHolder {
        private CircleView ivHead;
        private TextView tvName, /*tvRole,*/
                tvRealName, tvCollege, tvTelephone;
        private ImageView ivSex;
    }
}
