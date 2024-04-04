package com.leslie.socialink.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.PersonalInformationActivity;
import com.leslie.socialink.bean.AttentionBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.CircleView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;


public class AttentionAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<AttentionBean> data = new ArrayList<>();

    //初始化
    public AttentionAdapter(Context context, ArrayList<AttentionBean> data) {
        super();
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    //数据设置
    public void setData(ArrayList<AttentionBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    //布局文件设置
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.attention_item, parent, false);
        return new ViewHolder(view);
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

    //item控件设置
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nickname;
        private CircleView header;
        private ImageView sex;
        private LinearLayout noticeHome;
        public int cansee = 0;
        private ImageView notice;


        public ViewHolder(View view) {
            super(view);
            header = (CircleView) view.findViewById(R.id.mynoticephoto);
            nickname = (TextView) view.findViewById(R.id.mynoticename);
            notice = (ImageView) view.findViewById(R.id.mynoticedeletes);
            noticeHome = (LinearLayout) view.findViewById(R.id.noticeHome);
        }

        public void setData(final int position) {
            final AttentionBean bean = data.get(position);
            nickname.setText(bean.getNickname());
            if (!TextUtils.isEmpty(bean.getHeader())) {
                Glide.with(context).load(Constants.BASE_URL + bean.getHeader()).asBitmap().into(header);
            } else {
                header.setImageResource(R.mipmap.head3);
            }
            //设置监听器，点击按钮，取消关注
            notice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OkHttpUtils.post(Constants.DELETE_ATTENTION)
                            .tag(context)
                            .headers(Constants.TOKEN_HEADER, Constants.token)
                            .params("concerned", "" + data.get(position).getConcerned())
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    Utils.toastShort(context, "已取消关注");
                                    data.remove(position);
                                    AttentionAdapter.this.notifyDataSetChanged();
                                }
                            });
                }
            });
            //点击头像条状视图
            header.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", bean.getConcerned()));
                        }
                    });
            noticeHome.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, PersonalInformationActivity.class).putExtra("uid", bean.getConcerned()));
                        }
                    }
            );
        }
    }


}
