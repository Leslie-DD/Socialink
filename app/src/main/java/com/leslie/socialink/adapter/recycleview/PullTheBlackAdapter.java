package com.leslie.socialink.adapter.recycleview;

import android.app.Activity;
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
import com.leslie.socialink.bean.PullTheBlackBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.CircleView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;


public class PullTheBlackAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PullTheBlackBean> datas = new ArrayList<>();
    private Boolean deleteornot[] = new Boolean[1000];
    private int po = 0;

    //初始化
    public PullTheBlackAdapter(Context context, ArrayList<PullTheBlackBean> data) {
        super();
        this.context = context;
        this.datas = data;
        this.inflater = LayoutInflater.from(context);
    }

    //数据设置
    public void setData(ArrayList<PullTheBlackBean> data) {
        this.datas = data;
        this.notifyDataSetChanged();
    }

    @Override
    //布局文件设置
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.pulltheblack_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
    //item控件设置


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nickname;
        private ImageView delete;
        private CircleView header;
        private LinearLayout pullblackHome;


        public ViewHolder(View view) {
            super(view);
            header = (CircleView) view.findViewById(R.id.mypullbalckphoto);
            nickname = (TextView) view.findViewById(R.id.mypullbalckname);
            delete = (ImageView) view.findViewById(R.id.mypulltheblackdelete);
            pullblackHome = (LinearLayout) view.findViewById(R.id.pullbalckHome);
        }

        public void setData(final int position) {
            po = position;
            final PullTheBlackBean bean = datas.get(position);
            //nickname.setText(bean.getNickname());
            nickname.setText(bean.getBlacknickname());
            if (!TextUtils.isEmpty(bean.getHeader())) {
                Glide.with(context).load(Constants.BASE_URL + bean.getHeader()).asBitmap().into(header);
            } else {
                header.setImageResource(R.mipmap.head3);
            }
            //设置监听器，点击按钮，取消关注
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OkHttpUtils.post(Constants.DeletePullTheBlack)
                            .tag(context)
                            .headers(Constants.TOKEN_HEADER, Constants.token)
                            .params("black", "" + datas.get(position).getBlackId())
                            .execute(new StringCallback() {
                                @Override

                                public void onSuccess(String s, Call call, Response response) {
                                    Utils.toastShort(context, "拉黑");
//                                    context.startActivity(new Intent(context, MyAttentiion.class));
                                    datas.remove(position);
                                    PullTheBlackAdapter.this.notifyDataSetChanged();
                                }
                            });

//                    data.clear();
                }


            });
            //点击头像条状视图
            header.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, PersonalInformationActivity.class);
                            intent.putExtra("uid", bean.getBlackId());
                            ((Activity) context).startActivityForResult(intent, 1);
                        }
                    });
            pullblackHome.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, PersonalInformationActivity.class);
                            intent.putExtra("uid", bean.getBlackId());
                            context.startActivity(intent);
                        }
                    }
            );

        }


    }


}
