package com.example.heshequ.activity.team;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SharedElementCallback;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.heshequ.R;
import com.example.heshequ.adapter.ImagePreviewAdapter;
import com.example.heshequ.base.BaseActivity;
import com.example.heshequ.constans.P;
import com.example.heshequ.entity.DelEvent;
import com.example.heshequ.view.CustomViewPager2;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ImagePreviewActivity extends BaseActivity implements View.OnClickListener {
    private int itemPosition;
    private List<String> imageList;
    private CustomViewPager2 viewPager;
    private LinearLayout main_linear;
    private boolean mIsReturning;
    private int mStartPosition;
    private int mCurrentPosition;
    private ImagePreviewAdapter adapter;
    public RelativeLayout rlDel;
    private ImageView ivDel, ivBack;
    private TextView tvTip;
    private boolean isDel;
    private AlertDialog dialog;
    private boolean isdel2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        initShareElement();
        getIntentData();
        initView();
        renderView();
        getData();
        setListener();
        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("要删除这张图片吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除
                DelEvent event = new DelEvent();
                event.setCp(mCurrentPosition);
                EventBus.getDefault().post(event);
                if (imageList.size() == 1) {
                    imageList.remove(mCurrentPosition);
                    ImagePreviewActivity.this.finish();
                } else {
                    imageList.remove(mCurrentPosition);
                    reSetUi();
                }

            }


        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.setCancelable(false);
    }

    private void reSetUi() {
        if (mCurrentPosition != 0) {
            mCurrentPosition = mCurrentPosition - 1;

        }
        tvTip.setText((mCurrentPosition + 1) + "/" + imageList.size());
        adapter = new ImagePreviewAdapter(this, imageList, itemPosition);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mCurrentPosition);

    }

    private void initShareElement() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterSharedElementCallback(mCallback);
        }
    }

    private void setListener() {
        main_linear.getChildAt(mCurrentPosition).setEnabled(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideAllIndicator(position);
                main_linear.getChildAt(position).setEnabled(true);
                mCurrentPosition = position;
                tvTip.setText((mCurrentPosition + 1) + "/" + imageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition / 2 + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);
            }
        });
    }

    private void hideAllIndicator(int position) {
        for (int i = 0; i < imageList.size(); i++) {
            if (i != position) {
                main_linear.getChildAt(i).setEnabled(false);
            }
        }
    }

    private void initView() {
        viewPager = (CustomViewPager2) findViewById(R.id.imageBrowseViewPager);
        main_linear = (LinearLayout) findViewById(R.id.main_linear);
        rlDel = (RelativeLayout) findViewById(R.id.rlDel);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivDel = (ImageView) findViewById(R.id.ivDel);
        tvTip = (TextView) findViewById(R.id.tvTip);

        ivBack.setOnClickListener(this);
        ivDel.setOnClickListener(this);
        if (isDel) {
            rlDel.setVisibility(View.VISIBLE);
        } else {
            rlDel.setVisibility(View.GONE);
        }

        if (isdel2) {
            ivDel.setVisibility(View.VISIBLE);
        } else {
            ivDel.setVisibility(View.GONE);
        }


    }

    private void renderView() {
        if (imageList == null) return;
        if (imageList.size() == 1) {
            main_linear.setVisibility(View.GONE);
        } else {
            main_linear.setVisibility(isDel ? View.GONE : View.VISIBLE);
        }
        adapter = new ImagePreviewAdapter(this, imageList, itemPosition);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mCurrentPosition);
    }

    private void getIntentData() {
        if (getIntent() != null) {
            mStartPosition = getIntent().getIntExtra(P.START_IAMGE_POSITION, 0);
            mCurrentPosition = mStartPosition;
            itemPosition = getIntent().getIntExtra(P.START_ITEM_POSITION, 0);
            imageList = getIntent().getStringArrayListExtra("imageList");
            Log.e("YSF", imageList + "&&" + mCurrentPosition + "&&" + itemPosition);
            isDel = getIntent().getBooleanExtra("del", false);
            isdel2 = getIntent().getBooleanExtra("isdel2", true);
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        View view;
        for (String pic : imageList) {
            //创建底部指示器(小圆点)
            view = new View(ImagePreviewActivity.this);
            view.setBackgroundResource(R.drawable.indicator);
            view.setEnabled(false);
            //设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            //设置间隔
            if (!pic.equals(imageList.get(0))) {
                layoutParams.leftMargin = 20;
            }
            //添加到LinearLayout
            main_linear.addView(view, layoutParams);
        }
        tvTip.setText((mCurrentPosition + 1) + "/" + imageList.size());
    }


    @Override
    public void finishAfterTransition() {
        mIsReturning = true;
        Intent data = new Intent();
        data.putExtra(P.START_IAMGE_POSITION, mStartPosition);
        data.putExtra(P.CURRENT_IAMGE_POSITION, mCurrentPosition);
        data.putExtra(P.CURRENT_ITEM_POSITION, itemPosition);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }


    private final SharedElementCallback mCallback = new SharedElementCallback() {

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView sharedElement = adapter.getPhotoView();
                if (sharedElement == null) {
                    names.clear();
                    sharedElements.clear();
                } else if (mStartPosition != mCurrentPosition) {
                    names.clear();
                    names.add(sharedElement.getTransitionName());
                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.ivDel:
                dialog.show();
                break;
        }
    }


}
