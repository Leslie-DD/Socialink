package com.leslie.socialink.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leslie.socialink.R;
import com.leslie.socialink.entity.Item;
import com.leslie.socialink.entity.VoteBean;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.utils.Utils;

import java.util.ArrayList;


public class VoteAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<VoteBean.VoteItem> data;
    private LayoutInflater inflater;
    private int type;//当前的状态 0 添加投票信息 1开始投票 2 展示投票效果

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public VoteAdapter(Context mContext, ArrayList<VoteBean.VoteItem> data) {
        this.data = data;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    public ArrayList<VoteBean.VoteItem> getData() {
        return data;
    }

    public void setData(ArrayList<VoteBean.VoteItem> data) {
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return data.get(i).getData().size();
    }

    @Override
    public Object getGroup(int i) {
        return data.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return data.get(i).getData().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.vote_item, null);
            holder = new ViewHolder();
            holder.tvTheme = (TextView) view.findViewById(R.id.tvTheme);
            holder.tvType = (TextView) view.findViewById(R.id.tvType);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        VoteBean.VoteItem item = data.get(i);
        holder.tvTheme.setText(i + 1 + "、" + item.getTheme());
        holder.tvType.setText(item.getType() == 0 ? "[单选]" : "[多选]");
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewCHolder holder = null;
        final Item item = data.get(i).getData().get(i1);
        final VoteBean.VoteItem voteItem = data.get(i);
        if (view == null) {
            view = inflater.inflate(R.layout.vote_citem, null);
            holder = new ViewCHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvCount = (TextView) view.findViewById(R.id.tvCount);
            holder.ivBg = view.findViewById(R.id.ivBg);
            view.setTag(holder);
        } else {
            holder = (ViewCHolder) view.getTag();
        }
        holder.tvName.setText(item.getName());
        if (type == 1) {
            //正在投票状态
            holder.ivBg.setVisibility(View.GONE);
            view.setOnClickListener(view1 -> {
                if (voteItem.getType() == 0) {
                    for (int j = 0; j < voteItem.getData().size(); j++) {
                        Item im = voteItem.getData().get(j);
                        if (j == i1) {
                            im.setStatus(Math.abs(item.getStatus() - 1));
                        } else {
                            im.setStatus(0);
                        }
                    }
                } else {
                    item.setStatus(Math.abs(item.getStatus() - 1));

                }
                notifyDataSetChanged();
            });
            view.setBackgroundColor(Color.parseColor(item.getStatus() == 0 ? "#E6F3FA" : "#C8E9FA"));
        } else if (type == 2) {
            //查看投票
            holder.ivBg.setVisibility(View.VISIBLE);
            view.setBackgroundColor(Color.parseColor("#E6F3FA"));
            holder.tvCount.setText(item.getCount() + "票");
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.ivBg.getLayoutParams();

            lp.width = (int) (Utils.getScreenWidth((Activity) mContext) * (item.getCount() * 1f / item.getTotal()));
            holder.ivBg.setLayoutParams(lp);
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private class ViewHolder {
        private TextView tvTheme, tvType;

        public TextView getTvTheme() {
            return tvTheme;
        }

        public void setTvTheme(TextView tvTheme) {
            this.tvTheme = tvTheme;
        }

        public TextView getTvType() {
            return tvType;
        }

        public void setTvType(TextView tvType) {
            this.tvType = tvType;
        }
    }

    private class ViewCHolder {
        private TextView tvName, tvCount;
        private View ivBg;
    }

    //单选接口
    public interface SingleSelectionCheckListener {
        void check(int parentP, int childP);
    }

    //多选接口
    public interface MultipleSelectionCheckListener {
        void check(int parentP, int childP);
    }
}
