package com.hnu.heshequ.activity.newsencond;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.oldsecond.GoodDetailActivity;
import com.hnu.heshequ.bean.SecondhandgoodBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.view.CircleView;
import com.hnu.heshequ.view.MyGv;

import java.util.ArrayList;
import java.util.List;

public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.GoodViewHolder> {

    private static final String TAG = GoodAdapter.class.getSimpleName();

    private Context mContext;
    private List<SecondhandgoodBean> mList = new ArrayList<>();

    public GoodAdapter(Context context, List<SecondhandgoodBean> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_second_good_recyler, parent, false);
        return new GoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodViewHolder holder, final int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    @Override
    public long getItemId(int position) {
        return mList.get(position).getId();
    }

    class GoodViewHolder extends RecyclerView.ViewHolder {
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

        private ImageView ivImage;  // 商品宣传图

        public GoodViewHolder(View itemView) {
            super(itemView);
            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            ivHead = (CircleView) itemView.findViewById(R.id.ivHead);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvBelong = (TextView) itemView.findViewById(R.id.tvBelong);
            tvNum = (TextView) itemView.findViewById(R.id.tvNum);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvLoves = (TextView) itemView.findViewById(R.id.tvLoves);
            gv = (MyGv) itemView.findViewById(R.id.gv);
            llSave = (LinearLayout) itemView.findViewById(R.id.llSave);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

        }


        public void setData(final int position) {
            SecondhandgoodBean bean = mList.get(position);
            if (bean != null) {
                tvPrice.setText(bean.price == null ? "" : bean.price);
                tvTitle.setText(bean.content == null ? "" : bean.content);
                tvNum.setText(bean.commentAmount == null ? "" : bean.commentAmount);
                tvLoves.setText(bean.likeAmount + "");
                tvName.setText(bean.nn == null ? "" : bean.nn);
                if (TextUtils.isEmpty(bean.header)) {
                    ivHead.setImageResource(R.mipmap.head3);
                } else {
                    Glide.with(mContext).load(Constants.base_url + bean.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                }

                if (TextUtils.isEmpty(bean.userLike)) {
                    ivImg.setImageResource(R.mipmap.sc);
                    tvLoves.setTextColor(Color.parseColor("#ababb3"));
                } else {
                    ivImg.setImageResource(R.mipmap.saved);
                    tvLoves.setTextColor(Color.parseColor("#00bbff"));
                }

                /**
                 * 标签  暂时去除了
                 */
//                if (bean.labels != null && bean.labels.size() > 0) {
//                    String str = "";
//                    for (int i = 0; i < bean.labels.size(); i++) {
//                        if (i == 0) {
//                            str = "#" + bean.labels.get(i).name + "#";
//                        } else {
//                            str = str + "   #" + bean.labels.get(i).name + "#";
//                        }
//                    }
//                    tvBelong.setText(str);
//                } else {
//                    tvBelong.setText("");
//                }

                /**
                 * 给商品展示图赋值
                 * 选取商品图片的第一张
                 */
                if (bean.photos == null || bean.photos.size() == 0) {

                } else {
                    String imgurl = WenConstans.BaseUrl + bean.photos.get(0).photoId;
                    Glide.with(mContext)
                            .load(imgurl)
                            .asBitmap()
                            .fitCenter()
                            .placeholder(R.drawable.loading)
                            .into(ivImage);

                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Secondhandgood", mList.get(position));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
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