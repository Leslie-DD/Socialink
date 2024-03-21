

package com.hnu.heshequ.adapter.recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.CommentBean;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CircleView;

import java.util.ArrayList;

/**
 * @author dev06
 * 2016年7月4日
 */

public class CommentAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CommentBean> data = new ArrayList<>();
    private DelListener delListener = null;

    public CommentAdapter(Context context, ArrayList<CommentBean> data) {
        super();
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<CommentBean> data) {
        this.data.clear();
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setData2(ArrayList<CommentBean> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvTime, tvComment;
        private CircleView ivHead;
        private TextView tvDel;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvComment = (TextView) view.findViewById(R.id.tvComment);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            tvDel = (TextView) view.findViewById(R.id.tvDel);
        }

        public void setData(final int position) {
            CommentBean bean = data.get(position);
            tvName.setText(bean.getPresentorName());
            tvTime.setText(bean.getTime());
            tvComment.setText(Utils.getEmoji(context, bean.getContent()));
            if (bean.getHeader() != null && !bean.getHeader().isEmpty()) {
                Glide.with(context).load(Constants.base_url + bean.getHeader()).asBitmap().into(ivHead);
            } else {
                Glide.with(context).load(R.mipmap.head3).asBitmap().into(ivHead);
            }
            if (Constants.isAdmin || bean.getPresentor() == Constants.uid) {
                tvDel.setVisibility(View.VISIBLE);
            } else {
                tvDel.setVisibility(View.GONE);
            }

            tvDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delListener.onDelListener(data.get(position).getId());
                }
            });

            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delListener.onHeadClick(data.get(position).getPresentor());
                }
            });

        }
    }

    public interface DelListener {
        void onDelListener(int id);

        void onHeadClick(int uid);
    }

    public void setDelListener(DelListener delListener) {
        this.delListener = delListener;
    }
}
