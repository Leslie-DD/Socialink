package com.leslie.socialink.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.leslie.socialink.R;
import com.leslie.socialink.activity.team.TeamDetailActivity;
import com.leslie.socialink.utils.Utils;


/**
 * Description:
 * Data：7/10/2018-10:18 AM
 *
 * @author yanzhiwen
 */

public class PayPasswordView extends LinearLayout implements View.OnClickListener, PasswordEditText.PasswordFullListener {
    private LinearLayout mKeyBoardView;
    private PasswordEditText mPasswordEditText;
    public String pwd = "";
    private TeamDetailActivity context;

    public void setActivity(TeamDetailActivity teamDetailActivity2) {
        this.context = teamDetailActivity2;
    }

    public PayPasswordView(Context context) {
        this(context, null);
    }

    public PayPasswordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayPasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.pay_password_layout, this);
        mKeyBoardView = findViewById(R.id.keyboard);
        mPasswordEditText = findViewById(R.id.passwordEdt);
        mPasswordEditText.setPasswordFullListener(this);
        setItemClickListener(mKeyBoardView);
    }

    /**
     * 给每一个自定义数字键盘上的View 设置点击事件
     *
     * @param view
     */
    private void setItemClickListener(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                //不断的给里面所有的View设置setOnClickListener
                View childView = ((ViewGroup) view).getChildAt(i);
                setItemClickListener(childView);
            }
        } else {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String number = ((TextView) v).getText().toString().trim();
            mPasswordEditText.addPassword(number);
        }
        if (v instanceof ImageView) {
            mPasswordEditText.deletePassword();
        }
    }

    @Override
    public void passwordFull(String password) {

        if (context.IDENTIFY == 3) {
            context.setNewSecret(password);
            context.closePasswordDialog();
            context.joinWithSecret();
        } else {
            context.setNewSecret(password);
            context.closePasswordDialog();
            context.changeSecret();
        }

        Utils.toastShort(context, context.getNewSecret());
        this.context = null;//解除掉与TeamDetailActivity2之间的链接，让PayPasswordView可以被垃圾回收
    }

    public String getPwd() {
        return pwd;
    }


}
