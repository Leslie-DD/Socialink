package com.leslie.socialink.adapter.recycleview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.TjDetailActivity;
import com.leslie.socialink.entity.BuildingBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author dev06
 * 2016年7月4日
 */

public class Tj_Adapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<BuildingBean> data = new ArrayList<>();
    private OnItemViewclick onItemViewclick = null;

    private int type;

    public Tj_Adapter(Context context, ArrayList<BuildingBean> data) {
        super();
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public Tj_Adapter(Context context, ArrayList<BuildingBean> data, int type) {
        super();
        this.context = context;
        this.data = data;
        this.type = type;
        this.inflater = LayoutInflater.from(context);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        this.notifyDataSetChanged();
    }

    public void setData(List<BuildingBean> data) {
        this.data.clear();
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setData2(List<BuildingBean> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (type == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.tj_item, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.tj_item2, parent, false);
        }
        return new ViewHolder(view, type);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        BuildingBean bean = data.get(position);
        if (type == 1) {
            setTime(viewHolder, bean);

            viewHolder.tvName.setText(bean.getTitle() + "");

            if (bean.getPhotos() != null && bean.getPhotos().size() != 0) {
                Glide.with(context).load(Constants.base_url + bean.getPhotos().get(0).getPhotoId()).centerCrop().into(viewHolder.ivPic);
            } else {
                viewHolder.ivPic.setImageResource(R.mipmap.mrtp);
            }
        } else if (type == 2) {
            setTime(viewHolder, bean);

            viewHolder.tvName.setText(bean.getTitle() + "");

            if (bean.getPhotos() != null && bean.getPhotos().size() != 0) {
                Glide.with(context).load(Constants.base_url + bean.getPhotos().get(0).getPhotoId()).centerCrop().into(viewHolder.ivPic);
            } else {
                viewHolder.ivPic.setImageResource(R.mipmap.mrtp);
            }

            if (bean.getRecommend() == 1) {
                viewHolder.tvRecommended.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_99999_rectangle));
                viewHolder.tvRecommended.setTextColor(Color.parseColor("#999999"));
                viewHolder.tvRecommended.setText("从大厅团建中撤销");
            }
            if (bean.getRecommend() == 0) {
                viewHolder.tvRecommended.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_2cd22b_15));
                viewHolder.tvRecommended.setTextColor(context.getResources().getColor(R.color.colorPrimary, null));
                viewHolder.tvRecommended.setText("推荐到大厅");
            }

            if (Constants.isAdmin) {
                //Log.e("ddq","Presentor = "+bean.getPresentor()+",uid = "+Constants.uid);
                viewHolder.tvEditor.setVisibility(View.VISIBLE);
                viewHolder.tvDel.setVisibility(View.VISIBLE);
                viewHolder.tvRecommended.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvEditor.setVisibility(View.GONE);
                viewHolder.tvDel.setVisibility(View.GONE);
                viewHolder.tvRecommended.setVisibility(View.GONE);
            }

            viewHolder.tvEditor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemViewclick.viewClick(1, position);
                }
            });

            viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemViewclick.viewClick(2, position);
                }
            });

            viewHolder.tvRecommended.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemViewclick.viewClick(3, position);
                }
            });
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.toastShort(context,"position = "+position);
                Intent intent = new Intent(context, TjDetailActivity.class);
                intent.putExtra("bean", data.get(position));
                context.startActivity(intent);
            }
        });

    }

    public void setOnItemViewclick(OnItemViewclick onItemViewclick) {
        this.onItemViewclick = onItemViewclick;
    }

    private void setTime(ViewHolder viewHolder, BuildingBean bean) {
        try {
            Date date = Utils.parseString(bean.getGmtCreate(), "yyyy-MM-dd HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            viewHolder.tvDay.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
            viewHolder.tvMonth.setText((calendar.get(calendar.MONTH) + 1) + "月");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void changetjdt(int position, int i) {
        data.get(position).setRecommend(i);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPic;
        private TextView tvDay, tvMonth, tvName, tvEditor, tvDel, tvRecommended;

        public ViewHolder(View view, int type) {
            super(view);
            if (type == 1) {
                ivPic = (ImageView) view.findViewById(R.id.ivPic);
                tvDay = (TextView) view.findViewById(R.id.tvDay);
                tvMonth = (TextView) view.findViewById(R.id.tvMonth);
                tvName = (TextView) view.findViewById(R.id.tvName);
            } else if (type == 2) {
                ivPic = (ImageView) view.findViewById(R.id.ivPic);
                tvDay = (TextView) view.findViewById(R.id.tvDay);
                tvMonth = (TextView) view.findViewById(R.id.tvMonth);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvRecommended = (TextView) view.findViewById(R.id.tvRecommended);
                tvEditor = (TextView) view.findViewById(R.id.tvEditor);
                tvDel = (TextView) view.findViewById(R.id.tvDel);
            }
        }

    }

    public interface OnItemViewclick {
        void viewClick(int viewType, int position);
    }
}
