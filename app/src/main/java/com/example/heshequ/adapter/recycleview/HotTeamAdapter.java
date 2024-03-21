

package com.example.heshequ.adapter.recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.heshequ.R;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.view.CircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dev06
 * 2016年7月4日
 */

public class HotTeamAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> data = new ArrayList<>();
    private View views;

    public HotTeamAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1 || position == 3 || position == 6) {
            return 2;
        }
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            views = LayoutInflater.from(context).inflate(R.layout.item_hot_team, parent, false);
        } else {
            views = LayoutInflater.from(context).inflate(R.layout.one_pic, parent, false);
        }
        return new ViewHolder(views, viewType);
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
        private ImageView ivImg;

        private CircleView ivHead;
        private TextView tvName, tvTitle, tvTz, tvNum;
//        private GridView gw;

        public ViewHolder(View view, int viewType) {
            super(view);
            type = viewType;
            if (viewType == 1) {
                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                tvTz = (TextView) view.findViewById(R.id.tvTz);
                tvNum = (TextView) view.findViewById(R.id.tvNum);
//                gw= (GridView) view.findViewById(R.id.gw);
            } else {
                ivImg = (ImageView) view.findViewById(R.id.ivImg);
            }
        }

        public void setData(final int position) {
            if (type == 1) {

            } else {
                ViewGroup.LayoutParams params = ivImg.getLayoutParams();
                params.height = ConsTants.screenW * 3 / 10;
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type == 1) {

                    } else {

                    }
                }
            });
        }
    }
}
