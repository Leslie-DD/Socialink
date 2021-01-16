package com.example.heshequ.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.heshequ.R;


public class ToLoginDialog extends Dialog {
    private final TextView tvContent;
    private final TextView tvTitle;
    public ToLoginDialog(Context context, DoSomeThings things) {
        super(context, R.style.CustomProgressDialog);
        setContentView(R.layout.tologin);
        this.mDoSomeThings=things;
        Window window=getWindow();
        window.getAttributes().gravity= Gravity.CENTER;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        LinearLayout llCancle= (LinearLayout) this.findViewById(R.id.llCancle);
        TextView llOk= (TextView) this.findViewById(R.id.llOk);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        tvContent = (TextView) this.findViewById(R.id.tvContent);
        llCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToLoginDialog.this.dismiss();
            }
        });
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToLoginDialog.this.dismiss();
                if (mDoSomeThings!=null){
                    mDoSomeThings.dothing();
                }
            }
        });
    }
    public void setContent(String string){
        tvContent.setText(string);
    }
    public void setTitle(String title){
        tvContent.setVisibility(View.GONE);
        tvTitle.setText(title);
    }
    private DoSomeThings mDoSomeThings;
    public interface DoSomeThings{
        void dothing();
    }
}
