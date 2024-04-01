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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;
import com.leslie.socialink.R;
import com.leslie.socialink.activity.oldsecond.GoodDetailActivity;
import com.leslie.socialink.bean.SecondhandgoodBean;

import com.leslie.socialink.network.Constants;
import com.leslie.socialink.view.CircleView;
import com.leslie.socialink.view.MyGv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SecondhandgoodAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<SecondhandgoodBean> data = new ArrayList<>();
    private HashMap<String, List<SecondhandgoodBean>> map = new HashMap<>();
    private LayoutInflater inflater;
    private Gson gson = new Gson();
    private ArrayList<String> imgs;

    private View views;

    //对标签进行重选
    public void setData(String labelName, List<SecondhandgoodBean> data) {
        this.data = data;
        map.put(labelName, data);
        this.notifyDataSetChanged();
    }

    public void setData(List<SecondhandgoodBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public SecondhandgoodAdapter(Context context) {
        super();
        this.context = context;

    }


    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
//            views = LayoutInflater.from(context).inflate(R.layout.item_secondhandgood, parent, false);
            views = LayoutInflater.from(context).inflate(R.layout.item_second_good, parent, false);
        } else {

        }
        return new ViewHolder(views, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivImg;
        private MyGv gv;
        private CircleView ivHead;
        private TextView tvName, tvTitle, tvBelong, tvNum, tvPrice;
        private TextView tvLoves;
        private LinearLayout llSave;
        private RollPagerView rollPagerView;
        private LinearLayout ll;

        private ImageView ivImage; // 商品宣传图

        public ViewHolder(View view, int type) {
            super(view);
            if (type == 0) {
                ivImg = (ImageView) view.findViewById(R.id.ivImg);
                ivHead = (CircleView) view.findViewById(R.id.ivHead);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
                tvBelong = (TextView) view.findViewById(R.id.tvBelong);
                tvNum = (TextView) view.findViewById(R.id.tvNum);
                tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                tvLoves = (TextView) view.findViewById(R.id.tvLoves);
                gv = (MyGv) view.findViewById(R.id.gv);
                llSave = (LinearLayout) view.findViewById(R.id.llSave);
                ivImage = (ImageView) view.findViewById(R.id.ivImage);
            } else {

            }
        }

        public void setData(final int position) {
            if (data.get(position).type == 0) {
                tvPrice.setText(data.get(position).time == null ? "" : data.get(position).price);
                tvTitle.setText(data.get(position).content == null ? "" : data.get(position).content);
                tvNum.setText(data.get(position).commentAmount == null ? "" : data.get(position).commentAmount);
                tvLoves.setText(data.get(position).likeAmount + "");
                if (data.get(position).anonymity == 0) {
                    tvName.setText(data.get(position).nn == null ? "" : data.get(position).nn);
                    if (TextUtils.isEmpty(data.get(position).header)) {
                        ivHead.setImageResource(R.mipmap.head3);
                    } else {
                        Glide.with(context).load(Constants.BASE_URL + data.get(position).header).asBitmap().fitCenter().placeholder(R.mipmap.head3).into(ivHead);
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
                    tvLoves.setTextColor(context.getResources().getColor(R.color.colorPrimary, null));
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
                /**
                 * 弃用MyGirdView
                 * 改为只展示一张图片作为商品展示图
                 */
//                if (data.get(position).photos == null || data.get(position).photos.size() == 0) {
//                    gv.setVisibility(View.GONE);
//                } else {
//                    gv.setVisibility(View.VISIBLE);
//                    switch (data.get(position).photos.size()) {
//                        case 1:
//                            gv.setNumColumns(1);
//                            break;
//                        case 2:
//                            gv.setNumColumns(2);
//                            break;
//                        case 4:
//                            gv.setNumColumns(2);
//                            break;
//                        default:
//                            gv.setNumColumns(3);
//                            break;
//                    }
//                    ArrayList<String> strings = new ArrayList<>();
//                    for (int i = 0; i < data.get(position).photos.size(); i++) {
//                        strings.add(Constants.BASE_URL + data.get(position).photos.get(i).photoId);
//                    }
//                    gv.setAdapter(new Adapter_GridView(context, strings));
//                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            List<SecondhandphotoBean> photoList = data.get(position).photos;
//                            ArrayList<String> list = new ArrayList<String>();
//                            if (photoList != null && photoList.size() > 0) {
//                                for (int j = 0; j < photoList.size(); j++) {
//                                    list.add(Constants.BASE_URL + photoList.get(j).photoId);
//                                }
//                            }
//                            Intent intent = new Intent(context, ImagePreviewActivity.class);
//                            intent.putStringArrayListExtra("imageList", list);
//                            intent.putExtra(P.START_ITEM_POSITION, i);
//                            intent.putExtra(P.START_IAMGE_POSITION, i);
//                            intent.putExtra("isdel2", false);
//                            context.startActivity(intent);
//                        }
//                    });
//                }
                /**
                 * 给商品展示图赋值
                 * 选取商品图片的第一张
                 */
                if (data.get(position).photos == null || data.get(position).photos.size() == 0) {
                    ivImage.setImageResource(R.drawable.noimg);
                } else {
                    Glide
                            .with(context)
                            .load(Constants.BASE_URL + data.get(position).photos.get(0).photoId)
                            .placeholder(R.drawable.noimg)//图片加载中显示
                            .into(ivImage);
                }


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.get(position).type != 1) {
                            if (Objects.equals(data.get(position).uid, Constants.uid + "")) {
                            }

                            Intent intent = new Intent(context, GoodDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Secondhandgood", data.get(position));
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

                    }
                });
            } else if (data.get(position).type == 1) {

            }
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    private DoSaveListener mDoSaveListener;

    public interface DoSaveListener {
        void doSave(int position);
    }

    public void DoSaveListener(DoSaveListener mDoSaveListener) {
        this.mDoSaveListener = mDoSaveListener;
    }


}
