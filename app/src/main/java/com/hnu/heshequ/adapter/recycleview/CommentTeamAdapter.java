package com.hnu.heshequ.adapter.recycleview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnu.heshequ.R;
import com.hnu.heshequ.activity.WebActivity;
import com.hnu.heshequ.activity.team.ImagePreviewActivity;
import com.hnu.heshequ.adapter.Adapter_GridView;
import com.hnu.heshequ.adapter.BannerAdapter;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.constans.P;
import com.hnu.heshequ.network.Constants;
import com.hnu.heshequ.network.entity.HomeBanner;
import com.hnu.heshequ.network.entity.TeamBean;
import com.hnu.heshequ.utils.Utils;
import com.hnu.heshequ.view.CircleView;
import com.hnu.heshequ.view.MyGv;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class CommentTeamAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<TeamBean> data = new ArrayList<>();
    private View views;
    private int num;
    private OnItemClickListener listener;
    private TeamBean bean;
    private ArrayList<String> imgs;

    private Gson gson = new Gson();
    private BannerAdapter bannerAdapter;
    private List<HomeBanner> imgsData;
    private int bannerFlag = 0;


    public CommentTeamAdapter(Context context, List<TeamBean> data) {
        super();
        this.context = context;
        this.data = data;
    }

    public List<TeamBean> getData() {
        return data;
    }

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<TeamBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getItemType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                views = LayoutInflater.from(context).inflate(R.layout.comment_team, parent, false);
                break;
            case 2:
                views = LayoutInflater.from(context).inflate(R.layout.myteam_speak, parent, false);
                break;
            case 3:
                views = LayoutInflater.from(context).inflate(R.layout.myteam_activity, parent, false);
                break;
            case 4:
                views = LayoutInflater.from(context).inflate(R.layout.myteam_team_banner, parent, false);
                break;
        }
        return new ViewHolder(views, viewType);
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

    public void setData(int num) {
        this.num = num;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private GridView gw;
        private CircleView ivHead;
        // type1 layout-view
        private TextView tvName, tvDesc, tvTz, tvNum, tvCollege;

        private RollPagerView rollPagerView;
        private LinearLayout llTeam;
        // type2 layout-view
        private TextView tvSpaek;
        // type3 layout-view
        private TextView tvActivity, tvInitiator, tvDate;

        private LinearLayout ll;

        public ViewHolder(View view, int type) {
            super(view);
            switch (type) {
                case 1:
                    tvCollege = (TextView) view.findViewById(R.id.tvCollege);
                    ivHead = (CircleView) view.findViewById(R.id.ivHead);
                    tvName = (TextView) view.findViewById(R.id.tvName);
                    tvDesc = (TextView) view.findViewById(R.id.tvDesc);
                    tvTz = (TextView) view.findViewById(R.id.tvTz);
                    tvNum = (TextView) view.findViewById(R.id.tvNum);
                    gw = (GridView) view.findViewById(R.id.gw);
                    llTeam = view.findViewById(R.id.llTeam);
                    break;
                case 2:
                    //test
                    tvCollege = (TextView) view.findViewById(R.id.tvCollege);
                    ivHead = (CircleView) view.findViewById(R.id.ivHead);
                    tvName = (TextView) view.findViewById(R.id.tvName);
                    tvTz = (TextView) view.findViewById(R.id.tvTz);
                    tvNum = (TextView) view.findViewById(R.id.tvNum);
                    tvSpaek = (TextView) view.findViewById(R.id.tvSpeak);
                    gw = (GridView) view.findViewById(R.id.gw);
                    break;
                case 3:
                    //test
                    tvCollege = (TextView) view.findViewById(R.id.tvCollege);
                    ivHead = (CircleView) view.findViewById(R.id.ivHead);
                    tvName = (TextView) view.findViewById(R.id.tvName);
                    tvTz = (TextView) view.findViewById(R.id.tvTz);
                    tvNum = (TextView) view.findViewById(R.id.tvNum);
                    tvActivity = (TextView) view.findViewById(R.id.tvActivity);
                    gw = (MyGv) view.findViewById(R.id.gw);
                    tvInitiator = (TextView) view.findViewById(R.id.tvInitiator);
                    tvDate = (TextView) view.findViewById(R.id.tvDate);
                    break;
                case 4:
                    ll = view.findViewById(R.id.ll);
                    rollPagerView = (RollPagerView) view.findViewById(R.id.rp);
                    ViewGroup.LayoutParams params = rollPagerView.getLayoutParams();
                    params.height = ConsTants.screenW * 10 / 22;
                    bannerAdapter = new BannerAdapter(rollPagerView, context);
                    imgs = new ArrayList<>();
                    rollPagerView.setAdapter(bannerAdapter);
                    // 设置播放时间间隔
                    rollPagerView.setPlayDelay(3000);
                    // 设置透明度
                    rollPagerView.setAnimationDurtion(500);
                    // 设置指示器（顺序依次）
                    rollPagerView.setHintView(new ColorPointHintView(context, context.getResources().getColor(R.color.colorPrimary, null), Color.WHITE));
                    break;
            }
        }

        @SuppressLint("SetTextI18n")
        public void setData(final int position) {
            bean = data.get(position);
            switch (bean.getItemType()) {
                case 1:
                    /*llTeam.setVisibility(View.VISIBLE);
                    rollPagerView.setVisibility(View.GONE);*/
                    if (TextUtils.isEmpty(bean.getLogoImage())) {
                        ivHead.setImageResource(R.mipmap.head3);
                    } else {
                        Glide.with(context).load(Constants.base_url + bean.getLogoImage()).asBitmap().into(ivHead);
                    }
                    tvCollege.setText(bean.getCollege());
                    tvName.setText(bean.getName());
                    tvDesc.setText(bean.getIntroduction());
                    tvNum.setText(bean.getMemberNumber() + "人");
                    tvTz.setText(bean.getCreatorName());
                    break;
                case 2:
                    if (bean.getLogoImage().isEmpty()) {
                        ivHead.setImageResource(R.mipmap.head3);
                    } else {
                        Glide.with(context).load(Constants.base_url + bean.getLogoImage()).asBitmap().into(ivHead);
                    }
                    tvCollege.setText(bean.getCollege());
                    tvName.setText(bean.getName());
                    tvNum.setText(bean.getMemberNumber() + "人");
                    tvTz.setText(bean.getCreatorName());
                    if (!bean.getSpeak().getContent().isEmpty())
                        tvSpaek.setText(bean.getSpeak().getContent());
                    if (bean.getSpeak().getPhotos() != null && bean.getSpeak().getPhotos().size() > 0) {
                        gw.setVisibility(View.VISIBLE);
                        imgs = new ArrayList<>();
                        for (int i = 0; i < data.get(position).getSpeak().getPhotos().size(); i++) {
                            imgs.add(Constants.base_url + data.get(position).getSpeak().getPhotos().get(i).getPhotoId());
                        }
                        switch (imgs.size()) {
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
                        gw.setAdapter(new Adapter_GridView(context, imgs));

                        gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                imgs = new ArrayList<>();
                                if (data.get(position).getSpeak() != null && data.get(position).getSpeak().getPhotos() != null) {
                                    for (int j = 0; j < data.get(position).getSpeak().getPhotos().size(); j++) {
                                        imgs.add(Constants.base_url + data.get(position).getSpeak().getPhotos().get(j).getPhotoId());
                                    }
                                }
                                Intent intent = new Intent(context, ImagePreviewActivity.class);
                                intent.putStringArrayListExtra("imageList", imgs);
                                intent.putExtra(P.START_ITEM_POSITION, i);
                                intent.putExtra(P.START_IAMGE_POSITION, i);
                                intent.putExtra("isdel2", false);
                                context.startActivity(intent);
                            }
                        });
                    } else {
                        gw.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    //test
                    if (bean.getLogoImage().isEmpty()) {
                        ivHead.setImageResource(R.mipmap.head2);
                    } else {
                        if (ivHead != null) {
                            Glide.with(context).load(Constants.base_url + bean.getLogoImage()).asBitmap().into(ivHead);
                        }
                    }
                    if (tvName != null) {
                        tvName.setText(bean.getName());
                    }
                    if (tvCollege != null) {
                        tvCollege.setText(bean.getCollege());
                    }
                    if (tvNum != null) {
                        tvNum.setText(bean.getMemberNumber() + "人");
                    }
                    if (tvTz != null) {
                        tvTz.setText(bean.getCreatorName());
                    }
                    if (!bean.getActivity().getApplyDeadline().isEmpty()) {
                        if (tvDate != null) {
                            tvDate.setText("报名截止时间：" + bean.getActivity().getApplyDeadline());
                        }
                    }
                    if (!bean.getActivity().getTitle().isEmpty())
                        if (tvActivity != null) {
                            tvActivity.setText(bean.getActivity().getTitle());
                        }
                    if (!bean.getActivity().getPresentorName().isEmpty())
                        if (tvInitiator != null) {
                            tvInitiator.setText(bean.getActivity().getPresentorName());
                        }
                    /*if (!bean.getActivity().getGmtCreate().isEmpty())
                        if (tvDate!=null) {
                            tvDate.setText("报名截止时间："+bean.getActivity().getGmtCreate());
                        }*/
                    if (bean.getActivity().getPhotos() != null && bean.getActivity().getPhotos().size() > 0) {
                        gw.setVisibility(View.VISIBLE);
                        if (gw != null) {
                            imgs = new ArrayList<>();
                            for (TeamBean.ActivityBean.PhotosBeanX photosBeanX : data.get(position).getActivity().getPhotos()) {
                                imgs.add(Constants.base_url + photosBeanX.getPhotoId());
                            }
                            switch (imgs.size()) {
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
                            gw.setAdapter(new Adapter_GridView(context, imgs));

                            gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    imgs = new ArrayList<>();
                                    for (int j = 0; j < data.get(position).getActivity().getPhotos().size(); j++) {
                                        imgs.add(Constants.base_url + data.get(position).getActivity().getPhotos().get(j).getPhotoId());
                                    }
                                    Intent intent = new Intent(context, ImagePreviewActivity.class);
                                    intent.putStringArrayListExtra("imageList", imgs);
                                    intent.putExtra(P.START_ITEM_POSITION, i);
                                    intent.putExtra(P.START_IAMGE_POSITION, i);
                                    intent.putExtra("isdel2", false);
                                    context.startActivity(intent);
                                }
                            });
                        }
                    } else {
                        gw.setVisibility(View.GONE);
                    }
                    break;
                case 4:
                    if (bean.getId() == -1) {
                        if (bannerFlag == 0) {
                            bannerFlag++;
                            getImgs(position);
                            bannerAdapter.setonBannerItemClickListener(new BannerAdapter.onBanneritemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    context.startActivity(new Intent(context, WebActivity.class)
                                            .putExtra("url", imgsData.get(position).getLinkUrl()));
                                }
                            });
                        }
                    }
                    break;
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (CommentTeamAdapter.this.getItemViewType(position) != 4) {
                            listener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }

    //获取首页轮播图
    private void getImgs(final int position) {
        /*setBodyParams(new String[]{"category"},new String[]{""+1});
        sendPost(Constants.base_url+"/api/pub/category/advertisement.do",getimgsCode,Constants.token);*/
        OkHttpUtils.post(Constants.base_url + "/api/pub/category/advertisement.do")
                .tag(context)
                .headers(Constants.Token_Header, Constants.token)
                .params("category", "2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject result = new JSONObject(s);
                            if (result.optInt("code") == 0) {
                                if (result.has("data") && !result.optString("data").isEmpty()) {
                                    imgsData = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<HomeBanner>>() {
                                    }.getType());
                                    if (imgsData != null && imgsData.size() > 0) {
                                        for (HomeBanner bannerImgsBean : imgsData) {
                                            imgs.add(Constants.base_url + bannerImgsBean.getCoverImage());
                                        }

                                    }
                                    if (imgs.size() > 0) {
                                        bannerAdapter.setData(imgs);
                                    } else {
                                        CommentTeamAdapter.this.data.remove(position);
                                        CommentTeamAdapter.this.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                Utils.toastShort(context, result.optString("msg"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
