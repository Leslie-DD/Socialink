package com.hnu.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.friend.FriendShowAnswers;
import com.hnu.heshequ.bean.FriendAddNewsBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.view.CircleView;
import com.jude.rollviewpager.RollPagerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FriendAddNewsAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<FriendAddNewsBean> data = new ArrayList<>();
    private HashMap<String, List<FriendAddNewsBean>> map = new HashMap<>();
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
    public void setData(List<FriendAddNewsBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public FriendAddNewsAdapter(Context context) {
        super();
        this.context = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            views = LayoutInflater.from(context).inflate(R.layout.item_friendaddnew, parent, false);
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
        private TextView tvTime;
        private CircleView ivHead;
        private TextView tvName, tvDesc;

        private LinearLayout mainadd;
        private RollPagerView rollPagerView;
        private LinearLayout ll;

        public ViewHolder(View view, int type) {
            super(view);
            if (type == 0) {

                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                mainadd = (LinearLayout) view.findViewById(R.id.mainadd);
                tvDesc = (TextView) view.findViewById(R.id.tvDesc);
                tvTime = (TextView) view.findViewById(R.id.tvTime);
            } else {

            }
        }

        public void setData(final int position) {


            tvTime.setText(data.get(position).getTime() == null ? "" : data.get(position).getTime());
            tvName.setText(data.get(position).getnickName() == null ? "" : data.get(position).getnickName());
            if (TextUtils.isEmpty(data.get(position).getHeader())) {
                Log.e("shownearnull", "" + Constants.base_url + "/info/file/pub.do?fileId=" + data.get(position).getHeader());
                ivHead.setImageResource(R.mipmap.head3);
            } else {
                Glide.with(context).load(Constants.base_url + data.get(position).getHeader()).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("shownear", "" + Constants.base_url + "/info/file/pub.do?fileId=" + data.get(position).getHeader());
            }


//                if (TextUtils.isEmpty(data.get(position).userLike)) {
//                    ivImg.setImageResource(R.mipmap.sc);
//                    tvLoves.setTextColor(Color.parseColor("#ababb3"));
//                } else {
//                    ivImg.setImageResource(R.mipmap.saved);
//                    tvLoves.setTextColor(Color.parseColor("#2CD22B"));
//                }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    //                    if (Objects.equals(data.get(position).getReplyId(), Constants.uid + "")){
//                        //                    }
//                    
                    Intent intent = new Intent(context, FriendShowAnswers.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("FriendAdd", data.get(position));
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
