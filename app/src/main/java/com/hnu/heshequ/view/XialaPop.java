package com.hnu.heshequ.view;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hnu.heshequ.R;
import com.hnu.heshequ.base.NetWorkActivity;
import com.hnu.heshequ.bean.ConsTants;
import com.hnu.heshequ.utils.Utils;


public class XialaPop {

    private static WindowManager.LayoutParams layoutParams;
    private static PopupWindow peoType;
    private static View v;

    public static void showSelectPop(final NetWorkActivity context,
                                     final TextListener mTextListener) {
        layoutParams = context.getWindow().getAttributes();
        v = LayoutInflater.from(context).inflate(R.layout.tippopitem, null);
        peoType = new PopupWindow(v, ConsTants.screenW * 10 / 30, ViewGroup.LayoutParams.WRAP_CONTENT);
        peoType.setBackgroundDrawable(new BitmapDrawable());
//        peoType.setAnimationStyle(R.style.MyPopwindow_anim_style);
        peoType.update();
        peoType.setOutsideTouchable(true);
        peoType.setFocusable(true);
        peoType.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                context.getWindow().setAttributes(layoutParams);
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        v.findViewById(R.id.llShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextListener != null) {
                    mTextListener.selectPosition(0);
                }
                peoType.dismiss();
            }
        });
        v.findViewById(R.id.llJuBao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextListener != null) {
                    mTextListener.selectPosition(1);
                }
                peoType.dismiss();
            }
        });

        layoutParams.alpha = 0.7f;
        context.getWindow().setAttributes(layoutParams);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        peoType.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, Utils.dip2px(context, 10), Utils.dip2px(context, 70));
    }

    public static void showSelectPop(final NetWorkActivity context, String text, boolean isDel, boolean isSharp,
                                     final TextListener mTextListener) {
        layoutParams = context.getWindow().getAttributes();
        v = LayoutInflater.from(context).inflate(R.layout.tippopitem, null);
        TextView tvStatu = (TextView) v.findViewById(R.id.tvStatu);
        tvStatu.setText(text);
        LinearLayout llDel = v.findViewById(R.id.llDel);
        LinearLayout llJuBao = v.findViewById(R.id.llJuBao);
        LinearLayout llSharp = v.findViewById(R.id.llSharp);
        if (isDel) {
            llDel.setVisibility(View.VISIBLE);
            llJuBao.setVisibility(View.GONE);
        }
        if (isSharp) {
            llSharp.setVisibility(View.VISIBLE);
        }
        peoType = new PopupWindow(v, ConsTants.screenW * 10 / 30, ViewGroup.LayoutParams.WRAP_CONTENT);
        peoType.setBackgroundDrawable(new BitmapDrawable());
//        peoType.setAnimationStyle(R.style.MyPopwindow_anim_style);
        peoType.update();
        peoType.setOutsideTouchable(true);
        peoType.setFocusable(true);
        peoType.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1f;
                context.getWindow().setAttributes(layoutParams);
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        });
        v.findViewById(R.id.llShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextListener != null) {
                    mTextListener.selectPosition(0);
                }
                peoType.dismiss();
            }
        });
        v.findViewById(R.id.llJuBao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextListener != null) {
                    mTextListener.selectPosition(1);
                }
                peoType.dismiss();
            }
        });
        llDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextListener != null) {
                    mTextListener.selectPosition(2);
                }
                peoType.dismiss();
            }
        });
        v.findViewById(R.id.llSharp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextListener != null) {
                    mTextListener.selectPosition(3);
                }
                peoType.dismiss();
            }
        });
        layoutParams.alpha = 0.7f;
        context.getWindow().setAttributes(layoutParams);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        peoType.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, Utils.dip2px(context, 10), Utils.dip2px(context, 70));
    }

    public interface TextListener {
        void selectPosition(int num);
    }

}
