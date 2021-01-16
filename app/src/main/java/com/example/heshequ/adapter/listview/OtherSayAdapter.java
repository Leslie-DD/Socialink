package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.heshequ.bean.TeamBean;
import com.example.heshequ.activity.team.ImagePreviewActivity;
import com.example.heshequ.activity.team.StatementDetailActivity;
import com.example.heshequ.adapter.Adapter_GridView;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.P;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.MeetApplication;
import com.example.heshequ.view.MyGv;
import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * Hulk_Zhang on 2017/6/30 11:26
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class OtherSayAdapter extends BaseAdapter {
    private Context context;
    private List<TeamBean.SpeakBean> data = new ArrayList<>();
    private TeamBean teamBean;
    private DelItemListener listener;

    public OtherSayAdapter(Context context, ArrayList<TeamBean.SpeakBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(int type ,List<TeamBean.SpeakBean> data) {
        if (type == 1) { //大厅他们说
            this.data.clear();
            for (int i = 0; i < data.size(); i++) {
                if (i < 2) {
                    this.data.add(data.get(i));
                }
            }
        }else if (type == 2){// 更多他们说
            this.data = data;
        }
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
        final TeamBean.SpeakBean bean = data.get(position);
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.other_item, null);
            viewHolder = new ViewHolder();
            viewHolder.gv = (MyGv) view.findViewById(R.id.gv);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
            viewHolder.tvContent = (TextView) view.findViewById(R.id.tvContent);
            viewHolder.tvTime = (TextView) view.findViewById(R.id.tvTime);
            viewHolder.tvFabulousCount = (TextView) view.findViewById(R.id.tvFabulousCount);
            viewHolder.ivZan = (ImageView) view.findViewById(R.id.ivZan);
            viewHolder.tvCommentCount = (TextView) view.findViewById(R.id.tvCommentCount);
            viewHolder.ivHead = (CircleView) view.findViewById(R.id.ivHead);
            viewHolder.tvDel = (TextView) view.findViewById(R.id.tvDel);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (!TextUtils.isEmpty(bean.getHeader())){
            Glide.with(context).load(Constants.base_url+bean.getHeader()).asBitmap().into(viewHolder.ivHead);
        }else{
            viewHolder.ivHead.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.head3));
        }

        viewHolder.tvName.setText(bean.getPresentorName());
        viewHolder.tvTime.setText(bean.getTime());
        viewHolder.tvContent.setText(bean.getContent());
        viewHolder.tvFabulousCount.setText(bean.getLikeAmount() + "");
        viewHolder.tvCommentCount.setText(bean.getCommentAmount() + "");
        if (bean.getIsLike() == 0){
            Glide.with(context).load(R.mipmap.zan).asBitmap().into(viewHolder.ivZan);
        }else{
            Glide.with(context).load(R.mipmap.zan2).asBitmap().into(viewHolder.ivZan);
        }
        if (bean.getPhotos() == null || bean.getPhotos().size() == 0) {
            viewHolder.gv.setVisibility(View.GONE);
        } else {
            viewHolder.gv.setVisibility(View.VISIBLE);
            switch (bean.getPhotos().size()) {
                case 1:
                    viewHolder.gv.setNumColumns(1);
                    break;
                case 2:
                    viewHolder.gv.setNumColumns(2);
                    break;
                case 4:
                    viewHolder.gv.setNumColumns(2);
                    break;
                default:
                    viewHolder.gv.setNumColumns(3);
                    break;
            }

            bean.setImgs();
            viewHolder.gv.setAdapter(new Adapter_GridView(context, bean.getImgs()));
            viewHolder.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    intent.putStringArrayListExtra("imageList", bean.getImgs());
                    intent.putExtra(P.START_ITEM_POSITION, i);
                    intent.putExtra(P.START_IAMGE_POSITION, i);
                    intent.putExtra("isdel2",false);
                    context.startActivity(intent);
                }
            });
        }

        viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDel(position);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onEvent(MeetApplication.getInstance(),"event_commentController");
                Intent intent = new Intent(context, StatementDetailActivity.class);
                teamBean = new TeamBean();
                teamBean.setSpeak(data.get(position));
                intent.putExtra("bean",teamBean);
                intent.putExtra("type",2);
                context.startActivity(intent);
            }
        });
        return view;
    }

    public class ViewHolder {
        private MyGv gv;
        private CircleView ivHead;
        private ImageView ivZan;
        private TextView tvName, tvContent, tvTime, tvFabulousCount, tvCommentCount,tvDel;

    }

    public interface DelItemListener{
        void onDel(int position);
    }

    public void setDelItemListener(DelItemListener listener){
        this.listener = listener;
    }
}
