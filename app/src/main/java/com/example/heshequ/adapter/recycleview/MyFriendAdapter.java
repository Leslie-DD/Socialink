package com.example.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.activity.oldsecond.GoodDetailActivity;
import com.example.heshequ.bean.FriendListBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.view.CircleView;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by dell on 2020/5/1.
 */

public class MyFriendAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<FriendListBean> data = new ArrayList<>();
    private HashMap<String, List<FriendListBean>> map = new HashMap<>();
    private LayoutInflater inflater;
    private Gson gson = new Gson();
    private ArrayList<String> imgs;

    private View views;

    //对标签进行重选
//    public void setData(String labelName, List<FriendListBean> data) {
//        this.data = data;
//        map.put(labelName, data);
//        this.notifyDataSetChanged();
//    }
    public void setData(List<FriendListBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public MyFriendAdapter(Context context) {
        super();
        this.context = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            views = LayoutInflater.from(context).inflate(R.layout.item_friendlist, parent, false);
        } else {

        }
        return new ViewHolder(views, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FriendNearAdapter.ViewHolder viewHolder = (FriendNearAdapter.ViewHolder) holder;
        viewHolder.setData(position);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImg;

        private CircleView ivHead, FriendSex;
        private TextView tvName, tvCollege, tvSay, tvAge, tvDistance;

        private LinearLayout llSave;
        private RollPagerView rollPagerView;
        private LinearLayout ll;

        public ViewHolder(View view, int type) {
            super(view);
            if (type == 0) {
                ivImg = (ImageView) view.findViewById(R.id.ivImg);
                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                FriendSex = (CircleView) view.findViewById(R.id.FriendSex);
                tvCollege = (TextView) view.findViewById(R.id.tvCollege);
                tvSay = (TextView) view.findViewById(R.id.tvSay);
                tvAge = (TextView) view.findViewById(R.id.tvAge);
                tvDistance = (TextView) view.findViewById(R.id.tvDistance);

                llSave = (LinearLayout) view.findViewById(R.id.llSave);
            } else {

            }
        }

        public void setData(final int position) {


            tvCollege.setText(data.get(position).college == null ? "" : data.get(position).college);
            tvSay.setText(data.get(position).descroption == null ? "" : data.get(position).descroption);
            tvAge.setText("" + data.get(position).age);
            tvDistance.setText("" + data.get(position).distance);

            tvName.setText(data.get(position).nickname == null ? "" : data.get(position).nickname);
            if (TextUtils.isEmpty(data.get(position).url)) {
                ivHead.setImageResource(R.mipmap.head3);
            } else {
                Glide.with(context).load(Constants.base_url + data.get(position).url).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
            }


//                if (TextUtils.isEmpty(data.get(position).userLike)) {
//                    ivImg.setImageResource(R.mipmap.sc);
//                    tvLoves.setTextColor(Color.parseColor("#ababb3"));
//                } else {
//                    ivImg.setImageResource(R.mipmap.saved);
//                    tvLoves.setTextColor(Color.parseColor("#00bbff"));
//                }
            if (data.get(position).descroption != null) {

                tvSay.setText(data.get(position).descroption);
            } else {
                tvSay.setText("");
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Objects.equals(data.get(position).user_id, Constants.uid + "")) {
                    }

                    Intent intent = new Intent(context, GoodDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Secondhandgood", data.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });
            llSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (!TextUtils.isEmpty(data.get(position).userLike)){
//                        Utils.toastShort(context,"你已经收藏过了");
//                        return;
//                    }

                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    private SecondhandgoodAdapter.DoSaveListener mDoSaveListener;

    public interface DoSaveListener {
        void doSave(int position);
    }

    public void DoSaveListener(SecondhandgoodAdapter.DoSaveListener mDoSaveListener) {
        this.mDoSaveListener = mDoSaveListener;
    }
}
