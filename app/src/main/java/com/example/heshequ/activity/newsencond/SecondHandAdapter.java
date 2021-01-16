package com.example.heshequ.activity.newsencond;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.heshequ.R;
import com.example.heshequ.bean.HomeBannerImgsBean;
import com.example.heshequ.bean.SecondhandgoodBean;
import com.example.heshequ.classification.ClassifationActivity;
import com.example.heshequ.classification.ClassifySecondaryBean;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class SecondHandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final String TAG = SecondHandAdapter.class.getSimpleName();

    private static final int TYPE_SEARCH = 3;
    private static final int TYPE_PIC = 2;
    private static final int TYPE_CLASSIFY = 0;
    private static final int TYPE_GOOD = 1;

    private Context mContext;
    private List<Integer> mTypeList = new ArrayList<>();

    private List<String> mBannerList = new ArrayList<>();
    private List<HomeBannerImgsBean> mBannerList2 = new ArrayList<>();
    private List<String> mSearchList = new ArrayList<>();
    private List<String> mPicList = new ArrayList<>();
    private List<ClassifySecondaryBean> mClassifyList = new ArrayList<>();
    private List<SecondhandgoodBean> mGoodList = new ArrayList<>();

    private SearchAdapter searchAdapter;
    private PicAdapter picAdapter;
    private ClassifyAdapter classifyAdapter;
    private GoodAdapter goodAdapter;

    private int posi = 0;

    public SecondHandAdapter(Context context, List<Integer> typeList) {
        mContext = context;
        mTypeList = typeList;
    }

    public void setBannerDataList(List<String> bannerDataList, List<HomeBannerImgsBean> bannerDataList2) {
        mBannerList = bannerDataList;
        mBannerList2 = bannerDataList2;
        notifyDataSetChanged();
    }

    public void setSearchDataList(List<String> searchDataList) {
        mSearchList = searchDataList;
        notifyDataSetChanged();
    }

    public void setPicDataList(List<String> picDataList) {
        mPicList = picDataList;
        notifyDataSetChanged();
    }

    public void setClassifyDataList(List<ClassifySecondaryBean> classifyDataList) {
        mClassifyList.clear();
        mClassifyList.addAll(classifyDataList);
        Log.e("分类个数", mClassifyList.size() + "");
        notifyDataSetChanged();
    }

    public void setGoodDataList(List<SecondhandgoodBean> goodDataList) {
        mGoodList.clear();
        mGoodList.addAll(goodDataList);
        goodAdapter.notifyDataSetChanged();
    }

    public void addGoodDataList(List<SecondhandgoodBean> goodDataList) {
        int start = mGoodList.size();
        int itemsCount = goodDataList.size();
        mGoodList.addAll(goodDataList);
        goodAdapter.notifyItemRangeInserted(start, itemsCount);
    }

    public void setPosi(int posi) {
        this.posi = posi;
        classifyAdapter.setPosi(this.posi);
    }

    @Override
    public int getItemViewType(int position) {
        if (mTypeList.get(position) == 3) {         // Search
            return TYPE_SEARCH;
        } else if (mTypeList.get(position) == 2) {  // Banner
            return TYPE_PIC;
        } else if (mTypeList.get(position) == 0) {  // 横向
            return TYPE_CLASSIFY;
        } else if (mTypeList.get(position) == 1) {  // 纵向
            return TYPE_GOOD;
        } else {
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEARCH) {
            View viewSearch = LayoutInflater.from(mContext).inflate(R.layout.slide_search_include, parent, false);
            return new SearchViewHolder(viewSearch);
        } else if (viewType == TYPE_PIC) {
            View viewBanner = LayoutInflater.from(mContext).inflate(R.layout.slide_banner_include, parent, false);
            return new PicViewHolder(viewBanner);
        } else if (viewType == TYPE_CLASSIFY) {
            View viewClassify = LayoutInflater.from(mContext).inflate(R.layout.slide_horizontal_include, parent, false);
            return new ClassifylViewHolder(viewClassify);
        } else if (viewType == TYPE_GOOD) {
            View viewGood = LayoutInflater.from(mContext).inflate(R.layout.slide_vertical_include, parent, false);
            return new GoodViewHolder(viewGood);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchViewHolder) {
            if (mSearchList != null) {
                searchAdapter = new SearchAdapter(mContext, mSearchList);
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                ((SearchViewHolder) holder).rcvSearch.setLayoutManager(manager);
                ((SearchViewHolder) holder).rcvSearch.setHasFixedSize(true);
//                ((SearchViewHolder) holder).rcvSearch.addItemDecoration(new XRecyclerView.DividerItemDecoration(mContext, XRecyclerView.DividerItemDecoration.HORIZONTAL));
                ((SearchViewHolder) holder).rcvSearch.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();

            }
        } else if (holder instanceof ClassifylViewHolder) {
            if (mClassifyList != null) {
                classifyAdapter = new ClassifyAdapter(mContext, mClassifyList);
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                ((ClassifylViewHolder) holder).rcvHorizontal.setLayoutManager(manager);
                ((ClassifylViewHolder) holder).rcvHorizontal.setHasFixedSize(true);
//                ((ClassifylViewHolder) holder).rcvHorizontal.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
                ((ClassifylViewHolder) holder).rcvHorizontal.setAdapter(classifyAdapter);
                classifyAdapter.notifyDataSetChanged();
                ((ClassifylViewHolder) holder).ibSlideSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ClassifationActivity.class);
                        startActivity(intent);
                    }
                });

            }
        } else if (holder instanceof GoodViewHolder) {
            if (mGoodList != null) {
                goodAdapter = new GoodAdapter(mContext, mGoodList);
                ((GoodViewHolder) holder).rcvVertical.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                ((GoodViewHolder) holder).rcvVertical.setHasFixedSize(true);
//                ((GoodViewHolder) holder).rcvVertical.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                ((GoodViewHolder) holder).rcvVertical.setAdapter(goodAdapter);

                goodAdapter.notifyDataSetChanged();
            }
        } else if (holder instanceof PicViewHolder) {
            if (mPicList != null) {
                picAdapter = new PicAdapter(mContext, mPicList, mBannerList, mBannerList2);
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                ((PicViewHolder) holder).rcvBanner.setLayoutManager(manager);
                ((PicViewHolder) holder).rcvBanner.setHasFixedSize(true);
//                ((PicViewHolder) holder).rcvBanner.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                ((PicViewHolder) holder).rcvBanner.setAdapter(picAdapter);

                picAdapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    public int getItemCount() {
        return mTypeList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rcvSearch;

        public SearchViewHolder(View itemView) {
            super(itemView);
            rcvSearch = itemView.findViewById(R.id.rcv_slide_search);
        }

    }

    public class PicViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rcvBanner;

        public PicViewHolder(View itemView) {
            super(itemView);
            rcvBanner = itemView.findViewById(R.id.rcv_slide_banner);
        }

    }

    public class ClassifylViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rcvHorizontal;
        ImageButton ibSlideSearch;

        public ClassifylViewHolder(View itemView) {
            super(itemView);
            rcvHorizontal = itemView.findViewById(R.id.rcv_slide_horizontal);
            ibSlideSearch = itemView.findViewById(R.id.ib_silde_search);
        }
    }

    public class GoodViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rcvVertical;

        public GoodViewHolder(View itemView) {
            super(itemView);
            rcvVertical = itemView.findViewById(R.id.rcv_slide_vertical);
        }
    }


}