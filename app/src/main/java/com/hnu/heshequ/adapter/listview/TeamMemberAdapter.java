package com.hnu.heshequ.adapter.listview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.team.PersonalInformationActivity;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.constans.Constants;
import com.hnu.heshequ.entity.TeamMemberBean;
import com.hnu.heshequ.view.CircleView;

import java.util.ArrayList;



public class TeamMemberAdapter extends BaseAdapter {
    private Context context;
    private NetWorkActivity activity;
    private ArrayList<TeamMemberBean> data = new ArrayList<>();
    private OnItemEditorNameListener onItemEditorNameListener;
    private int teamid;
    // edit pop


    public TeamMemberAdapter(Context context, ArrayList<TeamMemberBean> data) {
        this.context = context;
        activity = (NetWorkActivity) context;
        this.data = data;
    }


    public void setData(ArrayList<TeamMemberBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final TeamMemberBean bean = data.get(position);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.teammemberitem, null);
            viewHolder = new ViewHolder();
            viewHolder.ivHead = (CircleView) view.findViewById(R.id.ivHead);
            viewHolder.llHead = (LinearLayout) view.findViewById(R.id.llHead);
            viewHolder.llBg = (LinearLayout) view.findViewById(R.id.llBg);
            viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
            viewHolder.tvLevel = (TextView) view.findViewById(R.id.tvLevel);
            viewHolder.tvType = (TextView) view.findViewById(R.id.tvType);
            viewHolder.tvTop = (TextView) view.findViewById(R.id.tvTop);
            viewHolder.tvEditor = (TextView) view.findViewById(R.id.tvEditor);
            viewHolder.btsave = (Button) view.findViewById(R.id.btSave);
            viewHolder.etMark = (EditText) view.findViewById(R.id.etMark);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvName.setText(bean.getNickname());
        if (position == 0 || !bean.getInitialLetter().equals(data.get(position - 1).getInitialLetter())) {
            viewHolder.llHead.setVisibility(View.VISIBLE);
            viewHolder.tvTop.setText(bean.getInitialLetter() + "(" + bean.getCount() + "人)");
        } else {
            viewHolder.llHead.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(bean.getHeader())) {
            Glide.with(context).load(Constants.base_url + bean.getHeader()).asBitmap().into(viewHolder.ivHead);
        } else {
            viewHolder.ivHead.setImageResource(R.mipmap.head3);
        }

        viewHolder.tvLevel.setText("LV" + data.get(position).getGrade());

        if (bean.getRole() == 1 || bean.getRole() == 2) {
            viewHolder.tvType.setVisibility(View.VISIBLE);
            viewHolder.tvType.setText(bean.getRole() == 1 ? "团长" : "副团长");
            viewHolder.tvType.setBackgroundResource(bean.getRole() == 1 ? R.drawable.fe8888_10 :
                    R.drawable.bg_7bd8fa_10);
        } else {
            viewHolder.tvType.setVisibility(View.GONE);
        }

        if (bean.getStatus() == 0) {
            viewHolder.etMark.setVisibility(View.INVISIBLE);
            viewHolder.btsave.setVisibility(View.GONE);
            viewHolder.llBg.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            viewHolder.etMark.setVisibility(View.VISIBLE);
            viewHolder.etMark.clearComposingText();
            viewHolder.btsave.setVisibility(View.VISIBLE);
            viewHolder.llBg.setBackgroundColor(Color.parseColor("#E1F3FA"));
        }

        viewHolder.btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(
                        new Intent(context, PersonalInformationActivity.class)
                                .putExtra("uid", data.get(position).getMemberId())
                                .putExtra("role", data.get(position).getRole())
                                .putExtra("id", data.get(position).getId())
                                .putExtra("teamid", teamid)
                );

            }
        });
        return view;
    }

    public void getTeamId(int teamId) {
        this.teamid = teamId;
    }

    public class ViewHolder {
        private CircleView ivHead;
        private LinearLayout llHead, llBg;
        private TextView tvName, tvLevel, tvType, tvEditor, tvTop;
        private Button btsave;
        private EditText etMark;
    }

    public void setOnItemEditorNameListener(OnItemEditorNameListener itemEditorNameListener) {
        this.onItemEditorNameListener = itemEditorNameListener;
    }

    public interface OnItemEditorNameListener {
        void ItemEditor(int position, String mark);
    }
}
