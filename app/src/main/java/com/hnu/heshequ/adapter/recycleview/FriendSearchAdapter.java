package com.hnu.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.friend.FriendNearDetail;
import com.hnu.heshequ.bean.FriendListBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.view.CircleView;
import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class FriendSearchAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<FriendListBean> data = new ArrayList<>();
    private HashMap<String, List<FriendListBean>> map = new HashMap<>();
    private LayoutInflater inflater;
    private Gson gson = new Gson();
    private ArrayList<String> imgs;

    private View views;

    //对标签进行重选
    public void setData(String labelName, List<FriendListBean> data) {
        this.data = data;
        map.put(labelName, data);
        this.notifyDataSetChanged();
    }

    public void setData(List<FriendListBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public FriendSearchAdapter(Context context) {
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
        ViewHolder viewHolder = (ViewHolder) holder;
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
            if (TextUtils.isEmpty(data.get(position).header)) {
                ivHead.setImageResource(R.mipmap.head3);
            } else {
                Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId=" + data.get(position).header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("shownear", "" + Constants.base_url + "/info/file/pub.do?fileId=" + data.get(position).header);
            }
            if (data.get(position).sex == 1) {
                FriendSex.setImageResource(R.mipmap.me19);
            } else if (data.get(position).sex == 2) {
                FriendSex.setImageResource(R.mipmap.me36);
            }


//                if (TextUtils.isEmpty(data.get(position).userLike)) {
//                    ivImg.setImageResource(R.mipmap.sc);
//                    tvLoves.setTextColor(Color.parseColor("#ababb3"));
//                } else {
//                    ivImg.setImageResource(R.mipmap.saved);
//                    tvLoves.setTextColor(Color.parseColor("#2CD22B"));
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

                    Intent intent = new Intent(context, FriendNearDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("FriendNear", data.get(position));
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
