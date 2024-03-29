package com.hnu.heshequ.message.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hnu.heshequ.R;
import com.hnu.heshequ.message.adapter.MessageFragmentViewPagerAdapter;
import com.hnu.heshequ.network.Constants;
import com.hnu.heshequ.network.HttpRequestUtil;
import com.hnu.heshequ.utils.StatusBarUtil;
import com.hnu.heshequ.utils.Utils;

import org.json.JSONObject;

public class MessageFragment extends Fragment {
    private static final String TAG = "[MessageFragment]";
    private final int DELETE_ALL_MESSAGES = 1000;

    private View view;
    private MessageFragmentViewPagerAdapter adapter;

    private int currentPosition = -1;
    private AlertDialog deleteDialog;

    private final String[] tabTitleList = {"团队", "团言", "问题", "消息", "好友申请"};

    private final HttpRequestUtil.RequestCallBack callBack = new HttpRequestUtil.RequestCallBack() {
        @Override
        public void onSuccess(JSONObject result, int where, boolean fromCache) {
            if (where == DELETE_ALL_MESSAGES) {
                if (result.optInt("code") != 0) {
                    Utils.toastShort(getContext(), result.optString("msg"));
                } else if (adapter != null) {
                    adapter.refreshFragmentData(currentPosition);
                }
            }
        }

        @Override
        public void onFailure(String result, int where) {
            Utils.toastShort(getContext(), "操作失败");
        }
    };

    private final HttpRequestUtil httpRequest = new HttpRequestUtil(callBack, TAG);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_msg, container, false);
        RelativeLayout titleLayout = view.findViewById(R.id.title_layout);
        StatusBarUtil.setMarginStatusBar(titleLayout);
        init();
        return view;
    }

    private void init() {
        TextView tvClear = view.findViewById(R.id.tvClear);
        tvClear.setOnClickListener(v -> deleteDialog.show());
        initDialog();

        adapter = new MessageFragmentViewPagerAdapter(requireActivity().getSupportFragmentManager(), getLifecycle());
        TabLayout tabs = view.findViewById(R.id.tabs);
        ViewPager2 viewPager = view.findViewById(R.id.vp);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                tvClear.setVisibility(position <= 2 ? View.VISIBLE : View.GONE);
            }
        });

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(tabTitleList[position]);
        }).attach();

    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("确定要清空消息吗？");
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            //删除
            if (currentPosition == 0) {
                httpRequest.setBodyParams(new String[]{"type"}, new String[]{"" + 2});
                httpRequest.sendPostConnection(Constants.base_url + "/api/user/news/clearBatchNews.do", DELETE_ALL_MESSAGES, Constants.token);
            } else if (currentPosition == 1) {
                httpRequest.setBodyParams(new String[]{"type"}, new String[]{"" + 1});
                httpRequest.sendPostConnection(Constants.base_url + "/api/user/news/clearBatchNews.do", DELETE_ALL_MESSAGES, Constants.token);
            } else if (currentPosition == 2) {
                httpRequest.setBodyParams(new String[]{"type"}, new String[]{"" + 3});
                httpRequest.sendPostConnection(Constants.base_url + "/api/user/news/clearBatchNews.do", DELETE_ALL_MESSAGES, Constants.token);
            }
            deleteDialog.dismiss();
        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> deleteDialog.dismiss());
        deleteDialog = builder.create();
        deleteDialog.setCancelable(false);
    }
}
