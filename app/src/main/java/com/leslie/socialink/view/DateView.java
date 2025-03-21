package com.leslie.socialink.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.leslie.socialink.R;
import com.leslie.socialink.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateView {

    private static DatePicker dp;
    private static AlertDialog dialog;

    public static void showDialog(final Context context, final DoThings doThings) {
        AlertDialog.Builder bui = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.datepk, null);
        dp = (DatePicker) v.findViewById(R.id.dp);
        dp.getYear();
        bui.setView(v);
        bui.setNegativeButton("取消", null);
        bui.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                int date = dp.getDayOfMonth();
                int month = dp.getMonth() + 1;
                int year = dp.getYear();
                String str = year + "-" + (month < 10 ? "0" + month : month)
                        + "-" + (date < 10 ? "0" + date : date);
                String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
                if (Integer.parseInt(str.replace("-", "")) > Integer.parseInt(today)) {
                    Utils.toastShort(context, "所选日期不能超过今天");
                    return;
                }
                doThings.data(str);
            }
        });
        dialog = bui.create();
        dialog.show();
    }

    public interface DoThings {
        void data(String date);
    }
}
