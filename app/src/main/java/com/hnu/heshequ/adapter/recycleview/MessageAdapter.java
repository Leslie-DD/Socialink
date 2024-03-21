package com.hnu.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.ImagePreviewActivity;
import com.hnu.heshequ.activity.team.PersonalInformationActivity;
import com.hnu.heshequ.bean.MessageBean;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.constans.P;
import com.hnu.heshequ.constans.WenConstans;
import com.hnu.heshequ.view.CircleView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by 佳佳 on 2018/11/20.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private int sss = 0;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MessageBean> data = new ArrayList<>();
    private MessageBean data2 = new MessageBean();
    private MessageBean messageBean;
    private String header;
    public int type = 0;
    private static ArrayList<String> picture = new ArrayList<>();
    private int count = 0;
    private int items[] = new int[1000];
    private int currentPostation = 0;


    //初始化
    public MessageAdapter(ArrayList<MessageBean> data, Context context) {

        this.context = context;
        this.data = data;
    }

    //刷新消息时调用
    public void setData(ArrayList<MessageBean> data) {
        this.count = 0;
        this.items = new int[1000];
        this.data.clear();
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    //加载历史消息，并将其添加到原有消息的头部
    public void setData1(ArrayList<MessageBean> data) {
        this.count = 0;
        this.items = new int[1000];
        data.addAll(this.data);
        this.data.clear();
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public void setData2(ArrayList<MessageBean> data) {
        this.count = 0;
        this.items = new int[1000];
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    //    public void setData2(ArrayList<MessageBean> data) {
//        this.data.addAll(data);
//        this.notifyDataSetChanged();
//    }
    //返回由MessageTryActivity1处得到的值
    public ArrayList<MessageBean> getData() {
        return data;
    }

    //继承父类的函数，设置item的布局文件
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_message, parent, false);
        return new ViewHolder(view);
    }

    //返回item的总条数
    public int getItemCount() {
        return data.size();
    }

    private void charge(int position) {
        picture.clear();
        count = 0;
        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i).getType() == 1) {
                if (position == i)
                    currentPostation = count;
                picture.add(WenConstans.BaseUrl + this.data.get(i).getContent());
                data.get(i).setIndexofpicture(count);
                count++;
            }
        }
    }


    //根据type设置聊天时数据的显示界面，左方接收消息界面，右方发送消息界面。//并设置头像
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MessageBean messageBean = data.get(position);
        //接收消息
        if (messageBean.getSor() == 1) {
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(messageBean.getHeader())) {
                Glide.with(context).load(Constants.base_url + messageBean.getHeader()).asBitmap().into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.mipmap.head3);
            }
            if (messageBean.getType() == 0) {
                holder.il.setVisibility(View.VISIBLE);
                holder.receivephoto.setVisibility(View.GONE);
                holder.textViewl.setText(messageBean.getContent());
            } else {
                holder.receivephoto.setVisibility(View.VISIBLE);
                holder.il.setVisibility(View.GONE);
                Uri uri = Uri.parse(WenConstans.BaseUrl + messageBean.getcontent());
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setAutoPlayAnimations(true)
                        .build();
                holder.receivephoto.setController(controller);
                holder.receivephoto.getHierarchy();
            }
        } else {
            if (!TextUtils.isEmpty(messageBean.getHeader())) {
                Glide.with(context).load(Constants.base_url + messageBean.getHeader()).asBitmap().into(holder.imageViewr);
            } else {
                holder.imageViewr.setImageResource(R.mipmap.head3);
            }
            holder.right.setVisibility(View.VISIBLE);
            holder.left.setVisibility(View.GONE);
            //表情或文字
            if (messageBean.getType() == 0) {
                holder.li.setVisibility(View.VISIBLE);
                holder.sendphoto.setVisibility(View.GONE);
                holder.textViewr.setText(messageBean.getContent());
                holder.read2.setVisibility(View.GONE);
                holder.read.setVisibility(View.GONE);
                if (!messageBean.isRead()) {
                    holder.read.setVisibility(View.VISIBLE);
                }
            } else//图片
            {
                holder.sendphoto.setVisibility(View.VISIBLE);
                holder.li.setVisibility(View.GONE);
                Uri uri = Uri.parse(WenConstans.BaseUrl + messageBean.getcontent());
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        //动画支持，
                        .setAutoPlayAnimations(true)
                        .build();
                holder.sendphoto.setController(controller);
                holder.sendphoto.getHierarchy();
                holder.read.setVisibility(View.GONE);
                holder.read2.setVisibility(View.GONE);
                if (!messageBean.isRead()) {
                    holder.read2.setVisibility(View.VISIBLE);
                }
            }
        }
        //头像点击
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", messageBean.getSender()));
            }
        });
        holder.imageViewr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", messageBean.getSender()));
            }
        });
        //点击接收发送的图片
        holder.sendphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charge(position);
                Intent intent = new Intent(context, ImagePreviewActivity.class);
                intent.putStringArrayListExtra("imageList", picture);
                intent.putExtra(P.START_ITEM_POSITION, currentPostation);
                intent.putExtra(P.START_IAMGE_POSITION, currentPostation);
                intent.putExtra("count", messageBean.getIndexofpicture());
                intent.putExtra("isdel2", false);
                context.startActivity(intent);
            }
        });

        holder.receivephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charge(position);
                Intent intent = new Intent(context, ImagePreviewActivity.class);
                intent.putStringArrayListExtra("imageList", picture);
                intent.putExtra(P.START_ITEM_POSITION, currentPostation);
                intent.putExtra(P.START_IAMGE_POSITION, currentPostation);
                intent.putExtra("count", messageBean.getIndexofpicture());
                intent.putExtra("isdel2", false);
                context.startActivity(intent);
            }
        });

    }

    //    内部类，实现item的控件初始化
    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleView imageView;
        private TextView textViewl;
        private TextView textViewr;
        private LinearLayout left;
        private RelativeLayout right;
        private LinearLayout right1;
        private LinearLayout li;
        private RelativeLayout il;
        private CircleView imageViewr;
        private TextView read;
        private TextView read2;
        private SimpleDraweeView sendphoto;
        private SimpleDraweeView receivephoto;


        public ViewHolder(View view) {
            super(view);
            imageView = (CircleView) view.findViewById(R.id.messageimageViewleft);
            imageViewr = (CircleView) view.findViewById(R.id.messageimageViewr);

            textViewl = (TextView) view.findViewById(R.id.messagetextViewleft);
            textViewr = (TextView) view.findViewById(R.id.messagetextViewr);

            left = (LinearLayout) view.findViewById(R.id.left);
            right = (RelativeLayout) view.findViewById(R.id.right);

            read = (TextView) view.findViewById(R.id.read);
            read2 = (TextView) view.findViewById(R.id.read2);

            li = (LinearLayout) view.findViewById(R.id.li);
            il = (RelativeLayout) view.findViewById(R.id.il);

            sendphoto = (SimpleDraweeView) view.findViewById(R.id.sendphoto);
            receivephoto = (SimpleDraweeView) view.findViewById(R.id.receivephoto);
        }
    }
}