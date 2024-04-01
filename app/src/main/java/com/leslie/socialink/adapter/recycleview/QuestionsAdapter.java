package com.leslie.socialink.adapter.recycleview;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.WebActivity;
import com.leslie.socialink.activity.team.ImagePreviewActivity;
import com.leslie.socialink.activity.wenwen.WenwenDetailActivity;
import com.leslie.socialink.adapter.Adapter_GridView;
import com.leslie.socialink.adapter.BannerAdapter;
import com.leslie.socialink.bean.ConsTants;
import com.leslie.socialink.constans.P;
import com.leslie.socialink.constans.WenConstans;
import com.leslie.socialink.network.Constants;
import com.leslie.socialink.network.entity.HomeBanner;
import com.leslie.socialink.network.entity.QuestionBean;
import com.leslie.socialink.network.entity.QuestionPhotoBean;
import com.leslie.socialink.utils.Utils;
import com.leslie.socialink.view.CircleView;
import com.leslie.socialink.view.MyGv;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private final Context context;
    private List<QuestionBean> data = new ArrayList<>();
    private View views;

    private ArrayList<String> images;
    private final Gson gson = new Gson();
    private BannerAdapter bannerAdapter;
    private ArrayList<HomeBanner> imgsData;
    private int bannerFlag = 0;

    public QuestionsAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setData(List<QuestionBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @NonNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            views = LayoutInflater.from(context).inflate(R.layout.item_hot_ww, parent, false);
        } else if (viewType == 1) {
            views = LayoutInflater.from(context).inflate(R.layout.myteam_team_banner, parent, false);
        }
        return new ViewHolder(views, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImg;
        private MyGv gv;
        private CircleView ivHead;
        private TextView tvName, tvCollege, tvTitle, tvBelong, tvNum, tvTime;
        private TextView tvLoves;
        private LinearLayout llSave;
        private RollPagerView rollPagerView;

        public ViewHolder(View view, int type) {
            super(view);
            if (type == 0) {
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
            } else if (type == 1) {
                rollPagerView = view.findViewById(R.id.rp);
                ViewGroup.LayoutParams params = rollPagerView.getLayoutParams();
                params.height = ConsTants.screenW * 10 / 22;
                bannerAdapter = new BannerAdapter(rollPagerView, context);
                images = new ArrayList<>();
                rollPagerView.setAdapter(bannerAdapter);
                // 设置播放时间间隔
                rollPagerView.setPlayDelay(3000);
                // 设置透明度
                rollPagerView.setAnimationDurtion(500);
                // 设置指示器（顺序依次）
                rollPagerView.setHintView(new ColorPointHintView(context, context.getResources().getColor(R.color.colorPrimary, null), Color.WHITE));
            }
        }

        public void setData(final int position) {
            QuestionBean wenwenBean = data.get(position);
            if (wenwenBean.type == 0) {
                tvTime.setText(wenwenBean.time == null ? "" : wenwenBean.time);
                if (TextUtils.isEmpty(wenwenBean.college)) {
                    tvCollege.setVisibility(View.GONE);
                } else {
                    tvCollege.setVisibility(View.VISIBLE);
                    tvCollege.setText(wenwenBean.college);
                }
                tvTitle.setText(wenwenBean.title == null ? "" : wenwenBean.title);
                tvNum.setText(wenwenBean.commentAmount == null ? "" : wenwenBean.commentAmount);
                tvLoves.setText(wenwenBean.likeAmount + "");
                if (wenwenBean.anonymity == 0) {
                    tvName.setText(wenwenBean.nn == null ? "" : wenwenBean.nn);
                    if (TextUtils.isEmpty(wenwenBean.header)) {
                        ivHead.setImageResource(R.mipmap.head3);
                    } else {
                        Glide.with(context).load(Constants.base_url + wenwenBean.header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                    }
                } else {
                    tvName.setText("匿名用户");
                    ivHead.setImageResource(R.mipmap.head3);
                }

                if (TextUtils.isEmpty(wenwenBean.userLike)) {
                    ivImg.setImageResource(R.mipmap.sc);
                    tvLoves.setTextColor(Color.parseColor("#ababb3"));
                } else {
                    ivImg.setImageResource(R.mipmap.saved);
                    tvLoves.setTextColor(context.getResources().getColor(R.color.colorPrimary, null));
                }
                if (wenwenBean.labels != null && !wenwenBean.labels.isEmpty()) {
                    String str = "";
                    for (int i = 0; i < wenwenBean.labels.size(); i++) {
                        if (i == 0) {
                            str = "#" + wenwenBean.labels.get(i).name + "#";
                        } else {
                            str = str + "   #" + wenwenBean.labels.get(i).name + "#";
                        }
                    }
                    tvBelong.setText(str);
                } else {
                    tvBelong.setText("");
                }
                if (wenwenBean.photos == null || wenwenBean.photos.isEmpty()) {
                    gv.setVisibility(View.GONE);
                } else {
                    gv.setVisibility(View.VISIBLE);
                    switch (wenwenBean.photos.size()) {
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
                    for (int i = 0; i < wenwenBean.photos.size(); i++) {
                        strings.add(WenConstans.BaseUrl + wenwenBean.photos.get(i).photoId);
                    }
                    gv.setAdapter(new Adapter_GridView(context, strings));
                    gv.setOnItemClickListener((adapterView, view, i, l) -> {
                        List<QuestionPhotoBean> photoList = wenwenBean.photos;
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
                    if (wenwenBean.type != 1) {
                        Intent intent = new Intent(context, WenwenDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("wenwen", wenwenBean);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                llSave.setOnClickListener(v -> {
                    if (wenwenBean.type != 1 && mDoSaveListener != null) {
                        mDoSaveListener.doSave(position);
                    }
                });
            } else if (wenwenBean.type == 1) {
                if (bannerFlag == 0) {
                    bannerFlag++;
                    getImgs(position);
                    bannerAdapter.setonBannerItemClickListener(position1 -> context.startActivity(new Intent(context, WebActivity.class)
                            .putExtra("url", imgsData.get(position1).getLinkUrl())));
                }
            }
        }
    }

    //获取首页轮播图
    private void getImgs(final int position) {
        OkHttpUtils.post(Constants.base_url + "/api/pub/category/advertisement.do")
                .tag(context)
                .headers(Constants.Token_Header, Constants.token)
                .params("category", "4")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject result = new JSONObject(s);
                            if (result.optInt("code") != 0) {
                                Utils.toastShort(context, result.optString("msg"));
                                return;
                            }
                            if (!result.has("data") || result.optString("data").isEmpty()) {
                                return;
                            }
                            imgsData = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<HomeBanner>>() {
                            }.getType());
                            if (imgsData != null && !imgsData.isEmpty()) {
                                for (HomeBanner bannerImgsBean : imgsData) {
                                    images.add(Constants.base_url + bannerImgsBean.getCoverImage());
                                }
                            }
                            if (!images.isEmpty()) {
                                bannerAdapter.setData(images);
                            } else {
                                QuestionsAdapter.this.data.remove(position);
                                QuestionsAdapter.this.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private DoSaveListener mDoSaveListener;

    public interface DoSaveListener {
        void doSave(int position);
    }

    public void setSaveListener(DoSaveListener mDoSaveListener) {
        this.mDoSaveListener = mDoSaveListener;
    }
}
