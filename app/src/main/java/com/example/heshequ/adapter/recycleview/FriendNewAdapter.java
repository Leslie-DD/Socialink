package com.example.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.activity.friend.NewDetail;
import com.example.heshequ.activity.team.ImagePreviewActivity;
import com.example.heshequ.adapter.Adapter_GridView;
import com.example.heshequ.bean.FriendNewBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.P;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.view.MyGv;
import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by dell on 2020/3/30.
 */

public class FriendNewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<FriendNewBean> data = new ArrayList<>();
    private HashMap<String, List<FriendNewBean>> map = new HashMap<>();
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
    public void setData(List<FriendNewBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public FriendNewAdapter(Context context) {
        super();
        this.context = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            views = LayoutInflater.from(context).inflate(R.layout.item_friendnew, parent, false);
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
        private MyGv gv;
        private CircleView ivHead;
        private TextView tvName, tvCollegeName, tvTitle, tvTime, tvLove, tvNum;


        public ViewHolder(View view, int type) {
            super(view);
            if (type == 0) {
                ivImg = (ImageView) view.findViewById(R.id.ivImg);
                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvNum =(TextView) view.findViewById(R.id.tvNum);
                tvCollegeName = (TextView) view.findViewById(R.id.tvCollegeName);
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                tvTime = (TextView) view.findViewById(R.id.tvTime);
                tvLove = (TextView) view.findViewById(R.id.tvLoves);

                gv = (MyGv) view.findViewById(R.id.gv);
            } else {

            }
        }

        public void setData(final int position) {

            FriendNewBean bean = data.get(position);
            tvCollegeName.setText(data.get(position).college == null ? "" : data.get(position).college);
            tvTime.setText(data.get(position).date == null ? "" : data.get(position).date);

            tvLove.setText("" + data.get(position).likeamount);
            tvName.setText(data.get(position).name == null ? "" : data.get(position).name);
            if (TextUtils.isEmpty(data.get(position).headImg)) {
                ivHead.setImageResource(R.mipmap.head3);
            } else {
                Glide.with(context).load(Constants.base_url + "/info/file/pub.do?fileId="+data.get(position).headImg).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                Log.e("showheadimg",""+Constants.base_url +"/info/file/pub.do?fileId="+ data.get(position).headImg);
            }


//                if (TextUtils.isEmpty(data.get(position).userLike)) {
//                    ivImg.setImageResource(R.mipmap.sc);
//                    tvLoves.setTextColor(Color.parseColor("#ababb3"));
//                } else {
//                    ivImg.setImageResource(R.mipmap.saved);
//                    tvLoves.setTextColor(Color.parseColor("#00bbff"));
//                }
            if (data.get(position).content != null) {

                tvTitle.setText(data.get(position).content);
            } else {
                tvTitle.setText("");
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
                List<String> photoList = data.get(position).photos;
                for (int i = 0; i < bean.photos.size(); i++) {

                    strings.add(WenConstans.BaseUrl + photoList.get(i));
                }
                gv.setAdapter(new Adapter_GridView(context, strings));
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        List<String> photoList = data.get(position).photos;
                        ArrayList<String> list = new ArrayList<String>();
                        if (photoList != null && photoList.size() > 0) {
                            for (int j = 0; j < photoList.size(); j++) {
                                list.add(WenConstans.BaseUrl +photoList.get(j));
                            }
                        }
                        Intent intent = new Intent(context, ImagePreviewActivity.class);
                        intent.putStringArrayListExtra("imageList", list);
                        intent.putExtra(P.START_ITEM_POSITION, i);
                        intent.putExtra(P.START_IAMGE_POSITION, i);
                        intent.putExtra("isdel2",false);
                        context.startActivity(intent);
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MobclickAgent.onEvent(MeetApplication.getInstance(), "event_firstHotAsk");
                        if (Objects.equals(data.get(position).user_id, Constants.uid + "")) {
                            MobclickAgent.onEvent(MeetApplication.getInstance(), "event_myQuestionClick");
                        }
                        MobclickAgent.onEvent(MeetApplication.getInstance(), "event_commentController");

                        Intent intent = new Intent(context, NewDetail.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("FriendNew", data.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                });
//            llSave.setOnClickListener(new View.OnClickListener() {
                //@Override
                //  public void onClick(View v) {
//                    if (!TextUtils.isEmpty(data.get(position).userLike)){
//                        Utils.toastShort(context,"你已经收藏过了");
//                        return;
//                    }

                //    }
                // });

            }
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    private DoSaveListener mDoSaveListener;

    public interface DoSaveListener {
        void doSave(int position);
    }

    public void DoSaveListener(DoSaveListener mDoSaveListener) {
        this.mDoSaveListener = mDoSaveListener;
    }
}
