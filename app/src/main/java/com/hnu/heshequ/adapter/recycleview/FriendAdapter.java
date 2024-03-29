package com.hnu.heshequ.adapter.recycleview;

import android.annotation.SuppressLint;
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
import com.hnu.heshequ.activity.friend.FriendDetail;
import com.hnu.heshequ.bean.FriendListBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.view.CircleView;
import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class FriendAdapter extends RecyclerView.Adapter {
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

    public FriendAdapter(Context context) {
        super();
        this.context = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            views = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
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
        private TextView tvName, tvCollege, tvSay;
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
            } else {

            }
        }

        public void setData(final int position) {


            tvCollege.setText(data.get(position).college == null ? "" : data.get(position).college);
            tvSay.setText(data.get(position).descroption == null ? "" : data.get(position).descroption);


            tvName.setText(data.get(position).nickname == null ? "" : data.get(position).nickname);
            if (TextUtils.isEmpty(data.get(position).header)) {
                ivHead.setImageResource(R.mipmap.head3);
            } else {
                Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId=" + data.get(position).header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("showfriend111", "" + Constants.base_url + "/info/file/pub.do?fileId=" + data.get(position).header);
            }


//                if (TextUtils.isEmpty(data.get(position).userLike)) {
//                    ivImg.setImageResource(R.mipmap.sc);
//                    tvLoves.setTextColor(Color.parseColor("#ababb3"));
//                } else {
//                    ivImg.setImageResource(R.mipmap.saved);
//                    tvLoves.setTextColor(getResources().getColor(R.color.colorPrimary, null));
//                }
            if (data.get(position).descroption != null) {

                tvSay.setText(data.get(position).descroption);
            } else {
                tvSay.setText("");
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onClick(View v) {

                    if (Objects.equals(data.get(position).user_id, Constants.uid + "")) {
                    }

                    Intent intent = new Intent(context, FriendDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Friend", data.get(position));
                    Log.e("FriendAdapter, data.size", data.size() + "");
                    Log.e("FriendAdapter, position", position + "");
                    Log.e("FriendAdapter", data.get(position).toString() + "");
                    intent.putExtras(bundle);
                    context.startActivity(intent);

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
