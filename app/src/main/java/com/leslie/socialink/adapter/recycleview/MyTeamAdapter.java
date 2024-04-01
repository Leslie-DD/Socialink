package com.leslie.socialink.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.TeamDetailActivity;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.TeamBean;
import com.leslie.socialink.view.CircleView;

import java.util.ArrayList;


public class MyTeamAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<TeamBean> data;
    private View views;

    private OnClickEventListener listener;


    public void setListener(OnClickEventListener listener) {
        this.listener = listener;
    }

    public MyTeamAdapter(Context context, ArrayList<TeamBean> data) {
        super();
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<TeamBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public ArrayList<TeamBean> getData() {
        return data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        views = LayoutInflater.from(context).inflate(R.layout.item_myteam, parent, false);
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

        private ImageView ivCanSee;
        private CircleView ivHead;
        private TextView tvName/*, tvEditor*/;

        public ViewHolder(View view, int viewType) {
            super(view);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            tvName = (TextView) view.findViewById(R.id.tvName);
            //tvEditor = (TextView) view.findViewById(R.id.tvEditor);
            ivCanSee = (ImageView) view.findViewById(R.id.ivCanSee);
        }

        public void setData(final int position) {
            if (data.get(position).getLogoImage() == null) {
                Glide.with(context).load(R.mipmap.head3).asBitmap().into(ivHead);
            } else if (data.get(position).getLogoImage().isEmpty()) {
                Glide.with(context).load(R.mipmap.head3).asBitmap().into(ivHead);
            } else {
                Glide.with(context).load(Constants.base_url + data.get(position).getLogoImage()).asBitmap().into(ivHead);
            }
            tvName.setText(data.get(position).getName());

            if (data.get(position).getCreator() == Constants.uid) {
                ivCanSee.setVisibility(View.VISIBLE);
                //tvEditor.setVisibility(View.VISIBLE);
            } else {
                ivCanSee.setVisibility(View.INVISIBLE);
                // tvEditor.setVisibility(View.INVISIBLE);
            }

            if (data.get(position).getSettingVisible() == 0) {
                ivCanSee.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.kj));
            } else if (data.get(position).getSettingVisible() == 1) {
                ivCanSee.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.bkj));
            }

            ivCanSee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnCanSee(position);
                    }
                }
            });

            /*tvEditor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.OnEditor(position);
                    }
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TeamDetailActivity.class);
                    intent.putExtra("id", data.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    public interface OnClickEventListener {
        void OnCanSee(int position);

        void OnEditor(int position);
    }
}
