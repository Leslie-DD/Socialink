package com.leslie.socialink.activity.oldsecond;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leslie.socialink.R;
import com.leslie.socialink.classification.ClassifySecondaryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: ClassifyAdapter2
 * Author: Ding Yifan
 * Data: 2020/9/9
 * Time: 16:00
 * Description:
 */
public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ViewHolder> {

    private LocalBroadcastManager localBroadcastManager;

    private Context context;
    private OnItemClickListener mOnItemClickListener = null;
    private LayoutInflater mInflater;
    private List<ClassifySecondaryBean> mLabelList;

    private ClassifySecondaryBean addDevice;
    private List<Boolean> isClicks; // 控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    public ClassifyAdapter(List<ClassifySecondaryBean> labelList, Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mLabelList = labelList;
        isClicks = new ArrayList<>();
        for (int i = 0; i < mLabelList.size(); i++) {
            isClicks.add(false);
        }
    }

    public void setDatas(List<ClassifySecondaryBean> datas) {
        mLabelList = datas;
//        L.e("AddDeviceAdapter's mDatas===" + new Gson().toJson(mLabelList));
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mLabelList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.label_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        ClassifySecondaryBean goodLabel = mLabelList.get(i);
        Log.e("ClassifyAdapter2", goodLabel.getCategory2Name());
        viewHolder.labelName.setText(goodLabel.getCategory2Name());

        addDevice = mLabelList.get(i);
        // 将数据保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(viewHolder.labelName);
        if (isClicks.get(i)) {
            viewHolder.labelName.setTextColor(Color.parseColor("#00a0e9"));
        } else {
            viewHolder.labelName.setTextColor(Color.parseColor("#601A1A"));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < isClicks.size(); i++) {
                    isClicks.set(i, false);
                }
                isClicks.set(i, true);
                notifyDataSetChanged();

                localBroadcastManager = LocalBroadcastManager.getInstance(context);
                final Intent intent = new Intent(SecondFragment.LOCAL_BROADCAST);
                intent.putExtra("position", i);   //通知fragment，让它去调用setTvBg()方法
                localBroadcastManager.sendBroadcast(intent);   //发送本地广播，通知fragment该刷新了
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View labelView;
        TextView labelName;

        public ViewHolder(View view) {
            super(view);
            labelView = view;
            labelName = (TextView) view.findViewById(R.id.labelName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, TextView textView, int position);
    }
}