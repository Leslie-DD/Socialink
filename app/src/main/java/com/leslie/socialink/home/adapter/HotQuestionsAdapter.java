package com.leslie.socialink.home.adapter;

import android.annotation.SuppressLint;
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
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.ImagePreviewActivity;
import com.leslie.socialink.activity.wenwen.WenwenDetailActivity;
import com.leslie.socialink.adapter.Adapter_GridView;
import com.leslie.socialink.constans.P;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.QuestionBean;
import com.leslie.socialink.network.entity.QuestionPhotoBean;
import com.leslie.socialink.view.CircleView;
import com.leslie.socialink.view.MyGv;

import java.util.ArrayList;
import java.util.List;

public class HotQuestionsAdapter extends RecyclerView.Adapter<HotQuestionsAdapter.ViewHolder> {
    private final Context context;
    private List<QuestionBean> data = new ArrayList<>();

    public HotQuestionsAdapter(Context context) {
        super();
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<QuestionBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void update(int position, @NonNull QuestionBean questionBean) {
        if (position < 0 || position >= data.size()) {
            return;
        }
        data.set(position, questionBean);
        // 因为 HotQuestionsFragment 中 recyclerview 用的是 XRecyclerView，添加了 Header，所以 position 应该 +1
        notifyItemChanged(position + 1);
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hot_ww, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivImg;
        private final MyGv gv;
        private final CircleView ivHead;
        private final TextView tvName;
        private final TextView tvCollege;
        private final TextView tvTitle;
        private final TextView tvBelong;
        private final TextView tvNum;
        private final TextView tvTime;
        private final TextView tvLoves;
        private final LinearLayout llSave;

        public ViewHolder(View view) {
            super(view);
            ivImg = view.findViewById(R.id.ivImg);
            ivHead = view.findViewById(R.id.ivHead);
            tvName = view.findViewById(R.id.tvName);
            tvCollege = view.findViewById(R.id.tvCollege);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvBelong = view.findViewById(R.id.tvBelong);
            tvNum = view.findViewById(R.id.tvNum);
            tvTime = view.findViewById(R.id.tvTime);
            tvLoves = view.findViewById(R.id.tvLoves);
            gv = view.findViewById(R.id.gv);
            llSave = view.findViewById(R.id.llSave);
        }

        public void setData(final int position) {
            QuestionBean question = data.get(position);
            tvTime.setText(question.time == null ? "" : question.time);
            if (TextUtils.isEmpty(question.college)) {
                tvCollege.setVisibility(View.GONE);
            } else {
                tvCollege.setVisibility(View.VISIBLE);
                tvCollege.setText(question.college);
            }
            tvTitle.setText(question.title == null ? "" : question.title);
            tvNum.setText(question.commentAmount == null ? "" : question.commentAmount);
            tvLoves.setText(String.valueOf(question.likeAmount));
            if (question.anonymity == 0) {
                tvName.setText(question.nn == null ? "" : question.nn);
                if (TextUtils.isEmpty(question.header)) {
                    ivHead.setImageResource(R.mipmap.head3);
                } else {
                    Glide.with(context).load(Constants.base_url + question.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                }
            } else {
                tvName.setText("匿名用户");
                ivHead.setImageResource(R.mipmap.head3);
            }

            if (TextUtils.isEmpty(question.userLike)) {
                ivImg.setImageResource(R.mipmap.sc);
                tvLoves.setTextColor(Color.parseColor("#ababb3"));
            } else {
                ivImg.setImageResource(R.mipmap.saved);
                tvLoves.setTextColor(context.getResources().getColor(R.color.colorPrimary, null));
            }
            if (question.labels != null && !question.labels.isEmpty()) {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < question.labels.size(); i++) {
                    if (i == 0) {
                        str = new StringBuilder("#" + question.labels.get(i).name + "#");
                    } else {
                        str.append("   #").append(question.labels.get(i).name).append("#");
                    }
                }
                tvBelong.setText(str.toString());
            } else {
                tvBelong.setText("");
            }
            if (question.photos == null || question.photos.isEmpty()) {
                gv.setVisibility(View.GONE);
            } else {
                gv.setVisibility(View.VISIBLE);
                switch (question.photos.size()) {
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
                ArrayList<String> strings = new ArrayList<>();
                for (int i = 0; i < question.photos.size(); i++) {
                    strings.add(WenConstans.BaseUrl + question.photos.get(i).photoId);
                }
                gv.setAdapter(new Adapter_GridView(context, strings));
                gv.setOnItemClickListener((adapterView, view, i, l) -> {
                    List<QuestionPhotoBean> photoList = question.photos;
                    ArrayList<String> list = new ArrayList<>();
                    if (photoList != null && !photoList.isEmpty()) {
                        for (int j = 0; j < photoList.size(); j++) {
                            list.add(WenConstans.BaseUrl + photoList.get(j).photoId);
                        }
                    }
                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                    intent.putStringArrayListExtra("imageList", list);
                    intent.putExtra(P.START_ITEM_POSITION, i);
                    intent.putExtra(P.START_IAMGE_POSITION, i);
                    intent.putExtra("isdel2", false);
                    context.startActivity(intent);
                });
            }
            itemView.setOnClickListener(v -> {
                if (question.type != 1) {
                    Intent intent = new Intent(context, WenwenDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("wenwen", question);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            llSave.setOnClickListener(v -> {
                if (question.type != 1 && mDoSaveListener != null) {
                    mDoSaveListener.doSave(position);
                }
            });

        }
    }

    private DoSaveListener mDoSaveListener;

    public interface DoSaveListener {
        void doSave(int position);
    }

    public void setListener(DoSaveListener mDoSaveListener) {
        this.mDoSaveListener = mDoSaveListener;
    }
}

