package com.hnu.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.ImagePreviewActivity;
import com.hnu.heshequ.activity.team.StatementDetailActivity;
import com.hnu.heshequ.adapter.Adapter_GridView;
import com.hnu.heshequ.constans.P;
import com.hnu.heshequ.network.Constants;
import com.hnu.heshequ.network.entity.TeamBean;
import com.hnu.heshequ.view.CircleView;
import com.hnu.heshequ.view.MyGv;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dev06
 * 2016年7月4日
 */

public class OtherSayAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<TeamBean.SpeakBean> data = new ArrayList<>();
    private int type;  // 1 团言   2 他们说
    private DelItemListener listener;

    public OtherSayAdapter(Context context, ArrayList<TeamBean.SpeakBean> data, int type) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.type = type;
    }

    public void setData(List<TeamBean.SpeakBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setData2(List<TeamBean.SpeakBean> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.other_item, parent, false);

        return new ViewHolder(view);
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
        private MyGv gv;
        private CircleView ivHead;
        private TextView tvName, tvContent, tvTime, tvFabulousCount, tvCommentCount, tvDel;

        public ViewHolder(View view) {
            super(view);
            gv = (MyGv) view.findViewById(R.id.gv);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvFabulousCount = (TextView) view.findViewById(R.id.tvFabulousCount);
            tvCommentCount = (TextView) view.findViewById(R.id.tvCommentCount);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            tvDel = (TextView) view.findViewById(R.id.tvDel);
        }

        public void setData(final int position) {
            final TeamBean.SpeakBean bean = data.get(position);
            if (bean.getPresentor() == Constants.uid || Constants.isAdmin) {
                tvDel.setVisibility(View.VISIBLE);
            }
            tvName.setText(bean.getPresentorName());
            tvTime.setText(bean.getTime());
            tvContent.setText(bean.getContent());
            tvFabulousCount.setText(bean.getLikeAmount() + "");
            tvCommentCount.setText(bean.getCommentAmount() + "");
            if (TextUtils.isEmpty(bean.getHeader())) {
                ivHead.setImageResource(R.mipmap.head3);
            } else {
                Glide.with(context).load(Constants.base_url + bean.getHeader()).asBitmap().into(ivHead);
            }
            bean.setImgs();
            Log.e("DDQ", bean.getImgs().size() + "imgsize");
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
                gv.setAdapter(new Adapter_GridView(context, bean.getImgs()));
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(context, ImagePreviewActivity.class);
                        intent.putStringArrayListExtra("imageList", bean.getImgs());
                        intent.putExtra(P.START_ITEM_POSITION, i);
                        intent.putExtra(P.START_IAMGE_POSITION, i);
                        intent.putExtra("isdel2", false);
                        context.startActivity(intent);
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StatementDetailActivity.class);
                    TeamBean teamBean = new TeamBean();
                    teamBean.setSpeak(data.get(position));
                    intent.putExtra("bean", teamBean);
                    intent.putExtra("type", type);
                    context.startActivity(intent);
                }
            });

            tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Utils.toastShort(context,"删除");
                    if (listener != null) {
                        listener.onDel(position);
                    }
                    /*setBodyParams(new String[]{"speakId"}, new String[]{"" + speakId});
                    sendPost(Constants.base_url + "/api/club/speak/delete.do", delSpeak, Constants.token);*/
                }
            });

        }
    }

    public interface DelItemListener {
        void onDel(int position);
    }

    public void setDelItemListener(DelItemListener listener) {
        this.listener = listener;
    }


}
