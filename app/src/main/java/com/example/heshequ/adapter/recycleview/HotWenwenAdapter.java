/**
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */

package com.example.heshequ.adapter.recycleview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heshequ.R;
import com.example.heshequ.activity.WebActivity;
import com.example.heshequ.activity.team.ImagePreviewActivity;
import com.example.heshequ.activity.wenwen.WenwenDetailActivity;
import com.example.heshequ.adapter.Adapter_GridView;
import com.example.heshequ.adapter.MyBannerAdapter;
import com.example.heshequ.bean.ConsTants;
import com.example.heshequ.bean.HomeBannerImgsBean;
import com.example.heshequ.bean.WenwenBean;
import com.example.heshequ.bean.WwPhotoBean;
import com.example.heshequ.constans.Constants;
import com.example.heshequ.constans.P;
import com.example.heshequ.constans.WenConstans;
import com.example.heshequ.utils.Utils;
import com.example.heshequ.view.CircleView;
import com.example.heshequ.view.MyGv;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author dev06
 * 2016年7月4日
 */

public class HotWenwenAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<WenwenBean> data = new ArrayList<>();
    private View views;

    private ArrayList<String> imgs;
    private Gson gson = new Gson();
    private MyBannerAdapter bannerAdapter;
    private ArrayList<HomeBannerImgsBean> imgsData;
    private int bannerFlag = 0;

    public HotWenwenAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setData(List<WenwenBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            views = LayoutInflater.from(context).inflate(R.layout.item_hot_ww, parent, false);
        } else if (viewType == 1) {
            views = LayoutInflater.from(context).inflate(R.layout.myteam_team_banner, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImg;
        private MyGv gv;
        private CircleView ivHead;
        private TextView tvName, tvTitle, tvBelong, tvNum, tvTime;
        private TextView tvLoves;
        private LinearLayout llSave;
        private RollPagerView rollPagerView;
        private LinearLayout ll;

        public ViewHolder(View view, int type) {
            super(view);
            if (type == 0) {
                ivImg = (ImageView) view.findViewById(R.id.ivImg);
                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                tvBelong = (TextView) view.findViewById(R.id.tvBelong);
                tvNum = (TextView) view.findViewById(R.id.tvNum);
                tvTime = (TextView) view.findViewById(R.id.tvTime);
                tvLoves = (TextView) view.findViewById(R.id.tvLoves);
                gv = (MyGv) view.findViewById(R.id.gv);
                llSave = (LinearLayout) view.findViewById(R.id.llSave);
            } else if (type == 1) {
                ll = view.findViewById(R.id.ll);
                rollPagerView = (RollPagerView) view.findViewById(R.id.rp);
                ViewGroup.LayoutParams params = rollPagerView.getLayoutParams();
                params.height = ConsTants.screenW * 10 / 22;
                bannerAdapter = new MyBannerAdapter(rollPagerView, context);
                imgs = new ArrayList<>();
                rollPagerView.setAdapter(bannerAdapter);
                // 设置播放时间间隔
                rollPagerView.setPlayDelay(3000);
                // 设置透明度
                rollPagerView.setAnimationDurtion(500);
                // 设置指示器（顺序依次）
                rollPagerView.setHintView(new ColorPointHintView(context, Color.parseColor("#00bbff"), Color.WHITE));
            }
        }

        public void setData(final int position) {
            if (data.get(position).type == 0) {
                tvTime.setText(data.get(position).time == null ? "" : data.get(position).time);
                tvTitle.setText(data.get(position).title == null ? "" : data.get(position).title);
                tvNum.setText(data.get(position).commentAmount == null ? "" : data.get(position).commentAmount);
                tvLoves.setText(data.get(position).likeAmount + "");
                if (data.get(position).anonymity == 0) {
                    tvName.setText(data.get(position).nn == null ? "" : data.get(position).nn);
                    if (TextUtils.isEmpty(data.get(position).header)) {
                        ivHead.setImageResource(R.mipmap.head3);
                    } else {
                        Glide.with(context).load(Constants.base_url + data.get(position).header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
                    }
                } else {
                    tvName.setText("匿名用户");
                    ivHead.setImageResource(R.mipmap.head3);
                }

                if (TextUtils.isEmpty(data.get(position).userLike)) {
                    ivImg.setImageResource(R.mipmap.sc);
                    tvLoves.setTextColor(Color.parseColor("#ababb3"));
                } else {
                    ivImg.setImageResource(R.mipmap.saved);
                    tvLoves.setTextColor(Color.parseColor("#00bbff"));
                }
                if (data.get(position).labels != null && data.get(position).labels.size() > 0) {
                    String str = "";
                    for (int i = 0; i < data.get(position).labels.size(); i++) {
                        if (i == 0) {
                            str = "#" + data.get(position).labels.get(i).name + "#";
                        } else {
                            str = str + "   #" + data.get(position).labels.get(i).name + "#";
                        }
                    }
                    tvBelong.setText(str);
                } else {
                    tvBelong.setText("");
                }
                if (data.get(position).photos == null || data.get(position).photos.size() == 0) {
                    gv.setVisibility(View.GONE);
                } else {
                    gv.setVisibility(View.VISIBLE);
                    switch (data.get(position).photos.size()) {
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
                    for (int i = 0; i < data.get(position).photos.size(); i++) {
                        strings.add(WenConstans.BaseUrl + data.get(position).photos.get(i).photoId);
                    }
                    gv.setAdapter(new Adapter_GridView(context, strings));
                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            List<WwPhotoBean> photoList = data.get(position).photos;
                            ArrayList<String> list = new ArrayList<String>();
                            if (photoList != null && photoList.size() > 0) {
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
                        }
                    });
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.get(position).type != 1) {
                            if (Objects.equals(data.get(position).uid, Constants.uid + "")) {
                            }

                            Intent intent = new Intent(context, WenwenDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("wenwen", data.get(position));
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                });
                llSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    if (!TextUtils.isEmpty(data.get(position).userLike)){
//                        Utils.toastShort(context,"你已经收藏过了");
//                        return;
//                    }
                        if (data.get(position).type != 1) {
                            if (mDoSaveListener != null) {
                                mDoSaveListener.doSave(position);
                            }
                        }
                    }
                });
            } else if (data.get(position).type == 1) {
                //LogUtils.e("DDQ --> "+ bannerFlag);
                if (bannerFlag == 0) {
                    bannerFlag++;
                    getImgs(position);
                    bannerAdapter.setonBanneritemClickListener(new MyBannerAdapter.onBanneritemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            context.startActivity(new Intent(context, WebActivity.class)
                                    .putExtra("url", imgsData.get(position).getLinkUrl()));
                        }
                    });
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
                            if (result.optInt("code") == 0) {
                                if (result.has("data") && !result.optString("data").isEmpty()) {
                                    imgsData = gson.fromJson(result.optString("data"), new TypeToken<ArrayList<HomeBannerImgsBean>>() {
                                    }.getType());
                                    if (imgsData != null && imgsData.size() > 0) {
                                        for (HomeBannerImgsBean bannerImgsBean : imgsData) {
                                            imgs.add(Constants.base_url + bannerImgsBean.getCoverImage());
                                        }

                                    }
                                    if (imgs.size() > 0) {
                                        bannerAdapter.setData(imgs);
                                    } else {
                                        HotWenwenAdapter.this.data.remove(position);
                                        HotWenwenAdapter.this.notifyDataSetChanged();
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

    private DoSaveListener mDoSaveListener;

    public interface DoSaveListener {
        void doSave(int position);
    }

    public void DoSaveListener(DoSaveListener mDoSaveListener) {
        this.mDoSaveListener = mDoSaveListener;
    }
}
