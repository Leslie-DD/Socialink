package com.example.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.activity.oldsecond.GoodDetailActivity;
import com.example.heshequ.bean.SecondhandgoodBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.view.MyGv;
import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2019/9/5.
 */

public class LabelsortAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<SecondhandgoodBean> data = new ArrayList<>();
    private View views;

    public LabelsortAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(List<SecondhandgoodBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
//            views = LayoutInflater.from(context).inflate(R.layout.item_secondhandgood, parent, false);
            views = LayoutInflater.from(context).inflate(R.layout.item_second_good, parent, false);
        }
        return new ViewHolder(views, viewType);
    }

    @Override
    public int getItemViewType(int position) {
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

        private LinearLayout llSave;// 收藏标志和人数
        private ImageView ivImg;    // 收藏标志
        private TextView tvLoves;   // 收藏人数

        private TextView tvNum;     // 评论人数

        private CircleView ivHead;  // 用户头像
        private TextView tvName;    // 昵称
        private TextView tvPrice;   // 价格

        private TextView tvTitle;   // 商品标题
        private TextView tvBelong;

        private MyGv gv;

        private ImageView ivImage; // 商品宣传图


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
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                tvLoves = (TextView) view.findViewById(R.id.tvLoves);
                gv = (MyGv) view.findViewById(R.id.gv);
                llSave = (LinearLayout) view.findViewById(R.id.llSave);
                ivImage = (ImageView) view.findViewById(R.id.ivImage);
            } else if (viewType == 2) {

            }

        }

        public void setData(final int position) {
            if (type == 1) {
                SecondhandgoodBean bean = data.get(position);
                if (bean != null) {
                    tvPrice.setText(bean.price == null ? "" : bean.price);
                    tvTitle.setText(bean.content == null ? "" : bean.content);
                    tvNum.setText(bean.commentAmount == null ? "" : bean.commentAmount);
                    tvLoves.setText(bean.likeAmount + "");
                    if (bean.anonymity == 0) {
                        tvName.setText(bean.nn == null ? "" : bean.nn);
                        if (TextUtils.isEmpty(bean.header)) {
                            ivHead.setImageResource(R.mipmap.head3);
                        } else {
                            Glide.with(context).load(Constants.base_url+bean.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
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
                    /**
                     * 弃用MyGirdView
                     * 改为只展示一张图片作为商品展示图
                     */
//                    if (bean.photos == null || bean.photos.size() == 0) {
//                        gv.setVisibility(View.GONE);
//                    } else {
//                        gv.setVisibility(View.VISIBLE);
//                        switch (bean.photos.size()) {
//                            case 1:
//                                gv.setNumColumns(1);
//                                break;
//                            case 2:
//                                gv.setNumColumns(2);
//                                break;
//                            case 4:
//                                gv.setNumColumns(2);
//                                break;
//                            default:
//                                gv.setNumColumns(3);
//                                break;
//                        }
//                        while (bean.photos.size() > 6) {
//                            bean.photos.remove(bean.photos.size() - 1);
//                        }
//                        ArrayList<String> strings = new ArrayList<>();
//                        for (int i = 0; i < bean.photos.size(); i++) {
//                            strings.add(WenConstans.BaseUrl + bean.photos.get(i).photoId);
//                        }
//                        gv.setAdapter(new Adapter_GridView(context, strings));
//                        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                List<SecondhandphotoBean> photoList = data.get(position).photos;
//                                ArrayList<String> list = new ArrayList<String>();
//                                if (photoList != null && photoList.size() > 0) {
//                                    for (int j = 0; j < photoList.size(); j++) {
//                                        list.add(WenConstans.BaseUrl + photoList.get(j).photoId);
//                                    }
//                                }
//                                Intent intent = new Intent(context, ImagePreviewActivity.class);
//                                intent.putStringArrayListExtra("imageList", list);
//                                intent.putExtra(P.START_ITEM_POSITION, i);
//                                intent.putExtra(P.START_IAMGE_POSITION, i);
//                                intent.putExtra("isdel2",false);
//                                context.startActivity(intent);
//                            }
//                        });
//                    }
                    /**
                     * 给商品展示图赋值
                     * 选取商品图片的第一张
                     */
                     if (bean.photos == null || bean.photos.size() == 0) {
                         ivImage.setImageResource(R.drawable.noimg);
                    } else {
                         Glide
                                 .with(context)
                                 .load(WenConstans.BaseUrl + bean.photos.get(0).photoId)
                                 .placeholder(R.drawable.noimg)//图片加载中显示
                                 .into(ivImage);
                     }

                    llSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mDoSaveListener != null) {
                                mDoSaveListener.doSave(position);
                            }
                        }
                    });

                }
            } else {
//                WenwenBean bean = data.get(position);
//                if (bean != null) {
//                    Glide.with(context).load(WenConstans.BaseUrl + bean.logoImage)
//                            .asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
//                    tvName.setText(bean.creatorName + "");
//                    tvTitle.setText(bean.introduction + "");
//                    tvTz.setText(bean.name + "");
//                    tvNum.setText(bean.memberNumber + "");
//                }

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        MobclickAgent.onEvent(MeetApplication.getInstance(),"event_commentController");
                        MobclickAgent.onEvent(MeetApplication.getInstance(),"event_collectionEnterQuestion");
                        Intent intent = new Intent(context, GoodDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Secondhandgood", data.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
//                        MobclickAgent.onEvent(MeetApplication.getInstance(),"event_collectionEnterHall");
//                        Intent intent = new Intent(context, TeamDetailActivity2.class);
//                        intent.putExtra("id",Integer.parseInt(data.get(position).id));
//                        context.startActivity(intent);
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
