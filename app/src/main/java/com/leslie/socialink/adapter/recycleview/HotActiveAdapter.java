package com.leslie.socialink.adapter.recycleview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.ActivityDateilActivity;
import com.leslie.socialink.activity.team.ImagePreviewActivity;
import com.leslie.socialink.adapter.Adapter_GridView;
import com.leslie.socialink.constans.P;

import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.HotActivities;
import com.leslie.socialink.view.CircleView;

import java.util.ArrayList;
import java.util.List;

public class HotActiveAdapter extends RecyclerView.Adapter {

    private final Context context;
    private List<HotActivities.HotBean> data = new ArrayList<>();

    public HotActiveAdapter(Context context) {
        super();
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
    }

    public void setData(List<HotActivities.HotBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void setData2(List<HotActivities.HotBean> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View views = LayoutInflater.from(context).inflate(R.layout.item_hot_active, parent, false);
        return new ViewHolder(views);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImg;

        private CircleView ivHead;
        private TextView tvName, tvTitle, tvTz, tvNum, tvFqr, tvTime, tvCollege;
        private GridView gw;

        public ViewHolder(View view) {
            super(view);
            ivHead = (CircleView) view.findViewById(R.id.ivHead);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTz = (TextView) view.findViewById(R.id.tvTz);
            tvNum = (TextView) view.findViewById(R.id.tvNum);
            tvFqr = (TextView) view.findViewById(R.id.tvFqr);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvCollege = (TextView) view.findViewById(R.id.tvCollege);
            gw = (GridView) view.findViewById(R.id.gw);
        }

        @SuppressLint("SetTextI18n")
        public void setData(final int position) {
            final HotActivities.HotBean hotBean = data.get(position);

            if (!TextUtils.isEmpty(hotBean.getClubInfo().getLogoImage())) {
                Glide.with(context).load(Constants.BASE_URL + hotBean.getClubInfo().getLogoImage()).asBitmap().into(ivHead);
            } else {
                Glide.with(context).load(R.mipmap.head3).asBitmap().into(ivHead);
            }
            tvName.setText("" + hotBean.getClubInfo().getName());
            tvCollege.setText("" + hotBean.getClubInfo().getCollege());
            tvTitle.setText(hotBean.getTitle());
            tvFqr.setText(hotBean.getPresentorName());
            if (hotBean.getPhotos() != null && hotBean.getPhotos().size() > 0) {
                gw.setVisibility(View.VISIBLE);
                switch (hotBean.getPhotos().size()) {
                    case 1:
                        gw.setNumColumns(1);
                        break;
                    case 2:
                        gw.setNumColumns(2);
                        break;
                    case 4:
                        gw.setNumColumns(2);
                        break;
                    default:
                        gw.setNumColumns(3);
                        break;
                }
                ArrayList<String> strings = new ArrayList<>();
                for (int i = 0; i < hotBean.getPhotos().size(); i++) {
                    strings.add(Constants.BASE_URL + hotBean.getPhotos().get(i).getPhotoId());
                }
                gw.setAdapter(new Adapter_GridView(context, strings));
                gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        List<HotActivities.HotBean.PhotosBean> photoList = data.get(position).getPhotos();
                        ArrayList<String> list = new ArrayList<String>();
                        if (photoList != null && photoList.size() > 0) {
                            for (int j = 0; j < photoList.size(); j++) {
                                list.add(Constants.BASE_URL + photoList.get(j).getPhotoId());
                            }
                        }
                        Intent intent = new Intent(context, ImagePreviewActivity.class);
                        intent.putStringArrayListExtra("imageList", list);
                        intent.putExtra(P.START_ITEM_POSITION, i);
                        intent.putExtra(P.START_IAMGE_POSITION, i);
                        intent.putExtra("isdel2", false);
                        context.startActivity(intent);
                    }
                });
            } else {
                gw.setVisibility(View.GONE);
            }
            tvTime.setText("报名截止时间:" + hotBean.getApplyDeadline());
            if (hotBean.getClubInfo() != null) {
                if (hotBean.getClubInfo().getCreatorName() != null) {
                    tvTz.setText(hotBean.getClubInfo().getCreatorName() + "");
                }

                tvNum.setText(hotBean.getClubInfo().getMemberNumber() + "");

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(
                            new Intent(context, ActivityDateilActivity.class)
                                    .putExtra("id", hotBean.getId())
                    );
                }
            });
        }
    }
}
