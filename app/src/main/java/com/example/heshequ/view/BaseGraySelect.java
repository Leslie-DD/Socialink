package com.example.heshequ.view;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;

import java.util.List;


public class BaseGraySelect {

    private static WindowManager.LayoutParams layoutParams;
    private static PopupWindow peoType;
    private static View v;
    private static PickerView pickerView;

    public static void showSelectPop(final NetWorkActivity context, List<String> arr,
                                     final TextListener mTextListener) {
        layoutParams = context.getWindow().getAttributes();
        v = LayoutInflater.from(context).inflate(R.layout.under_gray_selete_pop, null);
        peoType = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        peoType.setBackgroundDrawable(new BitmapDrawable());
        peoType.setAnimationStyle(R.style.MyPopwindow_anim_style);
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
        pickerView = (PickerView) v.findViewById(R.id.pickview);
        pickerView.setData(arr);
        v.findViewById(R.id.tv01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peoType.dismiss();
            }
        });
        v.findViewById(R.id.tv02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextListener != null) {
                    String text = pickerView.getText();
                    mTextListener.getShowText(text);
                }
                peoType.dismiss();
            }
        });
        layoutParams.alpha = 0.5f;
        context.getWindow().setAttributes(layoutParams);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        peoType.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public interface TextListener {
        void getShowText(String text);
    }
}
