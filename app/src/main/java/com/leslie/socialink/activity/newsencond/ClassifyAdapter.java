package com.leslie.socialink.activity.newsencond;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leslie.socialink.R;
import com.leslie.socialink.classification.ClassifySecondaryBean;

import java.util.ArrayList;
import java.util.List;

public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ClassifyViewHolder> {

    // 广播发送者
    private LocalBroadcastManager localBroadcastManager;

    private static final String TAG = ClassifyViewHolder.class.getSimpleName();

    private Context mContext;

    private List<ClassifySecondaryBean> mList = new ArrayList<>();

    private int posi = 0;

    public ClassifyAdapter(Context context, List<ClassifySecondaryBean> list) {
        mContext = context;
        mList = list;
    }

    public void setPosi(int posi) {
        this.posi = posi;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClassifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 这里分类item先用slide的
        View view = LayoutInflater.from(mContext).inflate(R.layout.slide_horizontal_include_item, parent, false);
        return new ClassifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassifyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tvText.setText(mList.get(position).getCategory2Name());
        for (int i = 0; i < mList.size(); i++) {
            if (position == this.posi) {
                holder.tvText.setTextColor(Color.parseColor("#000000"));
                holder.tvText.setTextSize(16);
            } else {
                holder.tvText.setTextColor(Color.parseColor("#6e6e73"));

                holder.tvText.setTextSize(14);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 发送广播
                 */
                localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
                final Intent intent = new Intent(SecondHandActivity.LOCAL_BROADCAST);
                intent.putExtra("category2Id", mList.get(position).getCategory2Id());
                intent.putExtra("position", position);
                localBroadcastManager.sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ClassifyViewHolder extends RecyclerView.ViewHolder {

        TextView tvText;

        public ClassifyViewHolder(View itemView) {
            super(itemView);
            // 这里分类item先用slide的
            tvText = itemView.findViewById(R.id.tv_slide_horizontal_text);
        }
    }


}