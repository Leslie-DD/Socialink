package com.hnu.heshequ.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.hnu.heshequ.R;
import com.hnu.heshequ.adapter.ShareAdapter;
import com.hnu.heshequ.bean.ShareBean;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

/**
 * Created by dev06 on 2018/6/13.
 */
public class BottomShareFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private View view;
    private TextView tvCancel;
    private GridView gv;
    private ArrayList<ShareBean> data;
    private ShareAdapter adapter;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    private static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        view = View.inflate(getContext(), R.layout.share_pop, null);
        view.setBackgroundResource(R.drawable.comment_bg);
        dialog.setContentView(view);
//        ButterKnife.bind(this,view);
        init();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            mBottomSheetBehavior = (BottomSheetBehavior) behavior;
            mBottomSheetBehavior.setPeekHeight(300);
            mBottomSheetBehavior.setHideable(false);
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        //设置BottomSheetBehavior状态为STATE_COLLAPSED
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });

            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int height = view.getMeasuredHeight();
                    mBottomSheetBehavior.setPeekHeight(height);
                }
            });
        }
        event();
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private void event() {
        tvCancel.setOnClickListener(this);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tip = data.get(i).getName();

                if (mDoClickListener != null) {
                    mDoClickListener.clickPosition(i);
                }
                dismiss();
            }
        });
    }

    private void init() {
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        gv = (GridView) view.findViewById(R.id.gv);
        if (data == null || data.size() == 0) {
            getData();
        }
        adapter = new ShareAdapter(getActivity(), data);
        gv.setAdapter(adapter);
    }

    public ArrayList<ShareBean> getGvData() {
        return data;
    }

    private void getData() {
        data = new ArrayList<>();
        ShareBean bean = new ShareBean();
        bean.setName("微信好友");
        bean.setResid(R.mipmap.travels7);
        data.add(bean);


        bean = new ShareBean();
        bean.setName("朋友圈");
        bean.setResid(R.mipmap.travels6);
        data.add(bean);

        bean = new ShareBean();
        bean.setName("微博");
        bean.setResid(R.mipmap.travels17);
        data.add(bean);


        bean = new ShareBean();
        bean.setName("QQ");
        bean.setResid(R.mipmap.travels4);
        data.add(bean);

        /*bean = new ShareBean();
        bean.setName("QQ空间");
        bean.setResid(R.mipmap.travels5);
        data.add(bean);*/
    }

    public void setData(ArrayList<ShareBean> list) {
        data = list;
        if (adapter != null) {
            adapter.setData(data);
        }
    }

    public void setData() {
        data = new ArrayList<>();
        ShareBean bean = new ShareBean();
        bean.setName("微信好友");
        bean.setResid(R.mipmap.travels7);
        data.add(bean);


        bean = new ShareBean();
        bean.setName("朋友圈");
        bean.setResid(R.mipmap.travels6);
        data.add(bean);

        bean = new ShareBean();
        bean.setName("微博");
        bean.setResid(R.mipmap.travels17);
        data.add(bean);


        bean = new ShareBean();
        bean.setName("QQ");
        bean.setResid(R.mipmap.travels4);
        data.add(bean);

        /*bean = new ShareBean();
        bean.setName("QQ空间");
        bean.setResid(R.mipmap.travels5);
        data.add(bean);*/

        if (adapter != null) {
            adapter.setData(data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
        }
    }

    public interface DoClickListener {
        void clickPosition(int position);
    }

    private DoClickListener mDoClickListener;

    public void setDoClickListener(DoClickListener mDoClickListener) {
        this.mDoClickListener = mDoClickListener;
    }
}
