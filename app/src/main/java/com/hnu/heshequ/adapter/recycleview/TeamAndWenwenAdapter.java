package com.hnu.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.ImagePreviewActivity;
import com.hnu.heshequ.activity.team.TeamDetailActivity;
import com.hnu.heshequ.activity.wenwen.WenwenDetailActivity;
import com.hnu.heshequ.adapter.Adapter_GridView;
import com.hnu.heshequ.bean.WenwenBean;
import com.hnu.heshequ.bean.WwPhotoBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.P;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.view.CircleView;
import com.hnu.heshequ.view.MyGv;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dev06
 * 2016年7月4日
 */

public class TeamAndWenwenAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<WenwenBean> data = new ArrayList<>();
    private View views;

    public TeamAndWenwenAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<WenwenBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            views = LayoutInflater.from(context).inflate(R.layout.item_hot_ww, parent, false);
        } else if (viewType == 2) {
            views = LayoutInflater.from(context).inflate(R.layout.item_hot_team, parent, false);
        }
        return new ViewHolder(views, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).category == 2) {
            return 2;
        }
        return 1;
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
        private MyGv gv;
        private CircleView ivHead;
        private TextView tvName, tvTitle, tvBelong, tvNum, tvTime;
        private TextView tvLoves;
        private LinearLayout llSave;


        private TextView tvTz;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.type = viewType;
            if (viewType == 1) {
                ivImg = (ImageView) view.findViewById(R.id.ivImg);
                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                tvBelong = (TextView) view.findViewById(R.id.tvBelong);
                tvNum = (TextView) view.findViewById(R.id.tvNum);
                tvTime = (TextView) view.findViewById(R.id.tvTime);
                tvLoves = (TextView) view.findViewById(R.id.tvLoves);
                gv = (MyGv) view.findViewById(R.id.gv);
                llSave = (LinearLayout) view.findViewById(R.id.llSave);
            } else if (viewType == 2) {
                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                tvTz = (TextView) view.findViewById(R.id.tvTz);
                tvNum = (TextView) view.findViewById(R.id.tvNum);
            }

        }

        public void setData(final int position) {
            if (type == 1) {
                WenwenBean bean = data.get(position);
                if (bean != null) {
                    tvTime.setText(bean.time == null ? "" : bean.time);
                    tvTitle.setText(bean.title == null ? "" : bean.title);
                    tvNum.setText(bean.commentAmount == null ? "" : bean.commentAmount);
                    tvLoves.setText(bean.likeAmount + "");
                    if (bean.anonymity == 0) {
                        tvName.setText(bean.nn == null ? "" : bean.nn);
                        if (TextUtils.isEmpty(bean.header)) {
                            ivHead.setImageResource(R.mipmap.head3);
                        } else {
                            Glide.with(context).load(Constants.base_url + bean.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                        }
                    } else {
                        tvName.setText("匿名用户");
                        ivHead.setImageResource(R.mipmap.head3);
                    }

                    if (TextUtils.isEmpty(bean.userLike)) {
                        ivImg.setImageResource(R.mipmap.sc);
                        tvLoves.setTextColor(Color.parseColor("#ababb3"));
                    } else {
                        ivImg.setImageResource(R.mipmap.saved);
                        tvLoves.setTextColor(Color.parseColor("#00bbff"));
                    }
                    if (bean.labels != null && bean.labels.size() > 0) {
                        String str = "";
                        for (int i = 0; i < bean.labels.size(); i++) {
                            if (i == 0) {
                                str = "#" + bean.labels.get(i).name + "#";
                            } else {
                                str = str + "   #" + bean.labels.get(i).name + "#";
                            }
                        }
                        tvBelong.setText(str);
                    } else {
                        tvBelong.setText("");
                    }
                    if (bean.photos == null || bean.photos.size() == 0) {
                        gv.setVisibility(View.GONE);
                    } else {
                        gv.setVisibility(View.VISIBLE);
                        switch (bean.photos.size()) {
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
                        while (bean.photos.size() > 6) {
                            bean.photos.remove(bean.photos.size() - 1);
                        }
                        ArrayList<String> strings = new ArrayList<>();
                        for (int i = 0; i < bean.photos.size(); i++) {
                            strings.add(WenConstans.BaseUrl + bean.photos.get(i).photoId);
                        }
                        gv.setAdapter(new Adapter_GridView(context, strings));
                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                List<WwPhotoBean> photoList = data.get(position).photos;
                                ArrayList<String> list = new ArrayList<String>();
                                if (photoList != null && photoList.size() > 0) {
                                    for (int j = 0; j < photoList.size(); j++) {
                                        list.add(WenConstans.BaseUrl + photoList.get(j).photoId);
                                    }
                                }
                                Intent intent = new Intent(context, ImagePreviewActivity.class);
                                intent.putStringArrayListExtra("imageList", list);
                                intent.putExtra(P.START_ITEM_POSITION, i);
                                intent.putExtra(P.START_IAMGE_POSITION, i);
                                intent.putExtra("isdel2", false);
                                context.startActivity(intent);
                            }
                        });
                    }
                    llSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                    }
                            if (mDoSaveListener != null) {
                                mDoSaveListener.doSave(position);
                            }
                        }
                    });

                }
            } else {
                WenwenBean bean = data.get(position);
                if (bean != null) {
                    Glide.with(context).load(WenConstans.BaseUrl + bean.logoImage)
                            .asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                    tvName.setText(bean.creatorName + "");
                    tvTitle.setText(bean.introduction + "");
                    tvTz.setText(bean.name + "");
                    tvNum.setText(bean.memberNumber + "");
                }

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        Intent intent = new Intent(context, WenwenDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("wenwen", data.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, TeamDetailActivity.class);
                        intent.putExtra("id", Integer.parseInt(data.get(position).id));
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

    private DoSaveListener mDoSaveListener;

    public interface DoSaveListener {
        void doSave(int position);
    }

    public void DoSaveListener(DoSaveListener mDoSaveListener) {
        this.mDoSaveListener = mDoSaveListener;
    }
}
