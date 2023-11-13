package com.example.heshequ.activity.newsencond;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heshequ.MeetApplication;
import com.example.heshequ.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private static final String TAG = SearchViewHolder.class.getSimpleName();

    private Context mContext;

    private List<String> mList = new ArrayList<>();

    public SearchAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.slide_search_include_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        holder.tv_slide_searchgood.setText(mList.get(position));
        holder.iv_addgood.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.kj2));
        holder.tv_slide_searchgood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_firstSearch");
                Intent intent = new Intent(mContext, SearchGoodActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",0);
//                bundle.putInt("category2_id", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                MobclickAgent.onEvent(mContext, "event_teamSearch");
//                Toast.makeText(mContext, "你点击了Search" , Toast.LENGTH_SHORT).show();
            }
        });
        holder.iv_addgood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MeetApplication.getInstance(), "event_goodnumadd");
                startActivity(new Intent(mContext, SecondhandPostActivity.class));
//                Toast.makeText(mContext, "你点击了AddGood" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_slide_search;
        TextView tv_slide_searchgood;
        ImageView iv_addgood;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ll_slide_search = itemView.findViewById(R.id.ll_slide_search);
            tv_slide_searchgood = itemView.findViewById(R.id.tv_slide_searchgood);
            iv_addgood = itemView.findViewById(R.id.iv_slide_addgood);

        }
    }

}