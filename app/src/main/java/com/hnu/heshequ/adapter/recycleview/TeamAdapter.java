package com.hnu.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.statement.BullertinDetailActivity;
import com.hnu.heshequ.activity.statement.VoteDetailActivity;
import com.hnu.heshequ.activity.team.ActivityDateilActivity;
import com.hnu.heshequ.activity.team.ImagePreviewActivity;
import com.hnu.heshequ.adapter.Adapter_GridView;
import com.hnu.heshequ.constans.P;
import com.hnu.heshequ.entity.TeamTestBean;
import com.hnu.heshequ.network.Constants;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.MyGv;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dev06
 * 2016年7月4日
 */

public class TeamAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<TeamTestBean> data = new ArrayList<>();
    private OnDelListener onDelListener;
    private OnapplyListener onapplyListener;

    public TeamAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<TeamTestBean> data) {
        this.data = data;
        Log.e("YSF", "data。seze" + data.size());
        this.notifyDataSetChanged();
    }

    public void setOnDelListener(OnDelListener listener) {
        onDelListener = listener;
    }

    public void setOnapplyListener(OnapplyListener listener) {
        onapplyListener = listener;
    }


    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getType() == 2) {//活动
            return 1;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
            return new ActivityViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.team_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ActivityViewHolder) {
            ActivityViewHolder holder1 = (ActivityViewHolder) holder;
            holder1.setData(position);
        } else {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.setData(position);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvType, tvName, tvInitiator, tvTime;
        private Button btStatus;

        public ViewHolder(View view) {
            super(view);
            tvType = (TextView) view.findViewById(R.id.tvType);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvInitiator = (TextView) view.findViewById(R.id.tvInitiator);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            btStatus = (Button) view.findViewById(R.id.btStatus);
        }

        public void setData(final int position) {
            final TeamTestBean bean = data.get(position);
            String color = "#2CD22B";
            String content = "[投票]";
            btStatus.setVisibility(View.VISIBLE);
            switch (bean.getType()) {
                case 1: //投票
                    color = "#2CD22B";
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //context.startActivity(new Intent(context, VoteDetailActivity.class));
                        }
                    });
                    if (bean.getObj() != null) {
                        try {
                            if (Utils.isPastDue(bean.getObj().getDeadline(), "yyyy-MM-dd HH:mm")) {
                                btStatus.setText("进行中");
                                btStatus.setBackgroundResource(R.drawable.ing_bg);
                            } else {
                                btStatus.setText("已结束");
                                btStatus.setBackgroundResource(R.drawable.end_bg);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tvTime.setText("截止时间：" + bean.getObj().getDeadline());
                        tvName.setText(bean.getObj().getName());
                        tvInitiator.setText(bean.getObj().getPresentorName());
                    }
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //投票详情
                            context.startActivity(new Intent(context, VoteDetailActivity.class)
                                    .putExtra("id", bean.getObj().getId())
                                    .putExtra("bean", bean.getObj())
                            );
                        }
                    });
                    break;
                case 3:
                    color = "#F05252";
                    content = "[公告]";
                    final String title = bean.getObj() == null ? "" : bean.getObj().getTitle();
                    final String content2 = bean.getObj() == null ? "" : bean.getObj().getContent();
                    final int id = bean.getObj() == null ? 0 : bean.getObj().getId();
                    final int clubId = bean.getObj() == null ? 0 : bean.getId();
                    btStatus.setVisibility(View.GONE);

                    if (bean.getObj() != null) {
                        tvTime.setText("发布时间：" + bean.getObj().getGmtCreate());
                        tvName.setText(bean.getObj().getTitle());
                        tvInitiator.setText(bean.getObj().getPresentorName());
                    }
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //公告详情
                            context.startActivity(new Intent(context, BullertinDetailActivity.class)
                                    .putExtra("title", title)
                                    .putExtra("content", content2)
                                    .putExtra("id", id)
                                    .putExtra("clubId", clubId)
                                    .putExtra("initiator", bean.getObj().getPresentorName())
                                    .putExtra("time", bean.getObj().getTime())
                            );
                        }
                    });
                    break;
            }
            tvType.setText(content);
            tvType.setTextColor(Color.parseColor(color));


        }
    }


    class ActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView tvActivityName, tvName, tvTime, tvDel;
        private MyGv gv;
        private Button btStatus;

        public ActivityViewHolder(View view) {
            super(view);
            tvActivityName = (TextView) view.findViewById(R.id.tvActivityName);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDel = (TextView) view.findViewById(R.id.tvDel);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            btStatus = (Button) view.findViewById(R.id.btStatus);
            gv = (MyGv) view.findViewById(R.id.gv);
        }

        public void setData(final int position) {
            final TeamTestBean bean = data.get(position);
            btStatus.setVisibility(View.VISIBLE);
            Log.e("YSF", "我是adapter：" + position);
            if (bean.getObj() == null) {
                return;
            }
            if (bean.getObj().getIsLike() == 1)//已报名
            {
                btStatus.setText("已报名");
                btStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary, null));
                btStatus.setBackgroundResource(R.drawable.bg_2cd22b_13);
            } else {
                /*btStatus.setText("我要报名");
                btStatus.setTextColor(Color.parseColor("#ffffff"));
                btStatus.setBackgroundResource(R.drawable.bg_00bbff_solid_13);*/
                try {
                    if (Utils.isPastDue(bean.getObj().getApplyDeadline(), "yyyy-MM-dd HH:mm")) {
                        btStatus.setText("我要报名");
                        btStatus.setTextColor(Color.parseColor("#ffffff"));
                        btStatus.setBackgroundResource(R.drawable.bg_2cd22b_solid_13);
                    } else {
                        btStatus.setText("我要报名");
                        btStatus.setTextColor(Color.parseColor("#ffffff"));
                        btStatus.setBackgroundResource(R.drawable.end_bg);
                        //btStatus.setEnabled(false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            tvActivityName.setText(bean.getObj().getTitle());
            tvName.setText(bean.getObj().getPresentorName());
            tvTime.setText("报名截止时间：" + bean.getObj().getApplyDeadline());

            bean.setImgs();
            if (bean.getImgs() == null || bean.getImgs().size() == 0) {
                gv.setVisibility(View.GONE);
            } else {
                gv.setVisibility(View.VISIBLE);
                switch (bean.getImgs().size()) {
                    case 1:
                        gv.setNumColumns(1);
                        break;
                    case 2:
                        gv.setNumColumns(2);
                        break;
                    case 4:
                        gv.setNumColumns(2);
                        break;
                    default:
                        gv.setNumColumns(3);
                        break;
                }
                while (bean.getImgs().size() > 6) {
                    bean.getImgs().remove(bean.getImgs().size() - 1);
                }
                gv.setAdapter(new Adapter_GridView(context, bean.getImgs()));
                gv.setOnItemClickListener((adapterView, view, i, l) -> {
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    intent.putStringArrayListExtra("imageList", bean.getImgs());
                    intent.putExtra(P.START_ITEM_POSITION, i);
                    intent.putExtra(P.START_IAMGE_POSITION, i);
                    intent.putExtra("isdel2", false);
                    context.startActivity(intent);
                });
            }

            if (bean.getObj().getPresentor() != Constants.uid) {
                tvDel.setVisibility(View.GONE);
            }

            tvDel.setOnClickListener(v -> onDelListener.del(position, bean.getObj().getId()));

            btStatus.setOnClickListener(v -> {
                try {
                    if (Utils.isPastDue(bean.getObj().getApplyDeadline(), "yyyy-MM-dd HH:mm")) {
                        onapplyListener.apply(position, bean.getObj().getId(), bean.getObj().getStatus());
                    } else {
                        Utils.toastShort(context, "活动已截止报名");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            });

            itemView.setOnClickListener(v -> {
                //进入详情
                context.startActivity(new Intent(context, ActivityDateilActivity.class).putExtra("id", bean.getObj().getId()));
            });
        }
    }

    public interface OnDelListener {
        void del(int position, int id);
    }

    public interface OnapplyListener {
        void apply(int position, int id, int status);
    }
}
