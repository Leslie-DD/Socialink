package com.leslie.socialink.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.TeamDetailActivity;
import com.leslie.socialink.bean.SearchTeamBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.view.CircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dev06
 * 2016年7月4日
 */

public class SearchTeamAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<SearchTeamBean> data = new ArrayList<>();
    private View views;

    public SearchTeamAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<SearchTeamBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        views = LayoutInflater.from(context).inflate(R.layout.item_hot_team, parent, false);
        return new ViewHolder(views);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private int type;
//        private ImageView ivImg;

        private CircleView ivHead;
        private TextView tvName, tvTitle, tvTz, tvNum;
//        private GridView gw;

        public ViewHolder(View view) {
            super(view);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTz = (TextView) view.findViewById(R.id.tvTz);
            tvNum = (TextView) view.findViewById(R.id.tvNum);
        }

        public void setData(final int position) {
            SearchTeamBean bean = data.get(position);
            if (bean != null) {
                Glide.with(context).load(Constants.BASE_URL + bean.logoImage)
                        .asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                tvName.setText(bean.name + "");
                tvTitle.setText(bean.introduction + "");
                tvTz.setText(bean.creatorName + "");
                tvNum.setText(bean.memberNumber + "");
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TeamDetailActivity.class);
                    intent.putExtra("id", Integer.parseInt(data.get(position).id));
                    context.startActivity(intent);
                }
            });
        }
    }
}
