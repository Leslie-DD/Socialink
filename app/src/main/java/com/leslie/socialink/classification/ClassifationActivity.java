package com.leslie.socialink.classification;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.leslie.socialink.R;
import com.leslie.socialink.base.NetWorkActivity;
import com.leslie.socialink.network.Constants;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClassifationActivity extends NetWorkActivity {

    private List<String> menuList = new ArrayList<>();
    private List<CategoryBean.DataBean> homeList = new ArrayList<>();
    private List<ClassificationBean.DataBean> homeList2 = new ArrayList<>();
    private List<Integer> showTitle;

    private ListView lv_menu;
    private ListView lv_home;

    private ClearEditText et_search;

    private MenuAdapter menuAdapter;
    private HomeAdapter homeAdapter;
    private int currentItem;

    private TextView tv_title;
    private ImageButton ib_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classifation_activity);
//        ImmersionBar.with(this).init();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
//                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        et_search = findViewById(R.id.et_classi_search);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_search);
        drawable.setBounds(0, 0, 80, 80);  // 第一0是距左边距离，第二0是距上边距离，30、35分别是长宽
        et_search.setCompoundDrawables(drawable, null, null, null);

        ib_back = findViewById(R.id.classification_back);
        ib_back.setOnClickListener(v -> finish());
        Fresco.initialize(this);
        initView();
        getData();
//        loadData();
    }

    private void getData() {
        sendPost(Constants.SECOND_GOOD_CLASSIFICATIONS, 102, Constants.token);
    }

    @Override
    protected void onSuccess(org.json.JSONObject result, int where, boolean fromCache) throws JSONException {
        String json2 = result.toString();
        ClassificationBean classificationBean = JSONObject.parseObject(json2, ClassificationBean.class);
        if (classificationBean == null) {
            return;
        }
        showTitle = new ArrayList<>();

        if (classificationBean.getData() != null && classificationBean.getData().size() != 0) {
            for (int i = 0; i < classificationBean.getData().size(); i++) {
                ClassificationBean.DataBean dataBean2 = classificationBean.getData().get(i);
                menuList.add(dataBean2.getCategory1Name());    // 男装
                showTitle.add(i);
                homeList2.add(dataBean2);
            }
            tv_title.setText(classificationBean.getData().get(0).getCategory1Name());
        }
        menuAdapter.notifyDataSetChanged();
        homeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onFailure(String result, int where) {

    }


    private void loadData() {
//
//        String json = getJson(this, "category.json");
//        CategoryBean categoryBean = JSONObject.parseObject(json, CategoryBean.class);

        String json2 = getJson(this, "ourCategory.json");
        ClassificationBean classificationBean = JSONObject.parseObject(json2, ClassificationBean.class);
        Log.e("json2", json2);
        Log.e("json2.size", classificationBean.getData().size() + "");

        showTitle = new ArrayList<>();
//        for (int i = 0; i < categoryBean.getData().size(); i++) {
//            CategoryBean.DataBean dataBean = categoryBean.getData().get(i);
//            menuList.add(dataBean.getModuleTitle());    // 男装
//            showTitle.add(i);
//            homeList.add(dataBean);
//    }
        for (int i = 0; i < classificationBean.getData().size(); i++) {
            ClassificationBean.DataBean dataBean2 = classificationBean.getData().get(i);
            menuList.add(dataBean2.getCategory1Name());    // 男装
            showTitle.add(i);
            homeList2.add(dataBean2);
        }
//        tv_title.setText(categoryBean.getData().get(0).getModuleTitle());
        tv_title.setText(classificationBean.getData().get(0).getCategory1Name());

        menuAdapter.notifyDataSetChanged();
        homeAdapter.notifyDataSetChanged();
    }

    private void initView() {
        lv_menu = (ListView) findViewById(R.id.lv_menu);
        tv_title = (TextView) findViewById(R.id.tv_titile);
        lv_home = (ListView) findViewById(R.id.lv_home);
        menuAdapter = new MenuAdapter(this, menuList);
        lv_menu.setAdapter(menuAdapter);

        homeAdapter = new HomeAdapter(this, homeList2);
        lv_home.setAdapter(homeAdapter);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuAdapter.setSelectItem(position);
                menuAdapter.notifyDataSetInvalidated();
                tv_title.setText(menuList.get(position));
                lv_home.setSelection(showTitle.get(position));
            }
        });


        lv_home.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    return;
                }
                int current = showTitle.indexOf(firstVisibleItem);
//				lv_home.setSelection(current);
                if (currentItem != current && current >= 0) {
                    currentItem = current;
                    tv_title.setText(menuList.get(currentItem));
                    menuAdapter.setSelectItem(currentItem);
                    menuAdapter.notifyDataSetInvalidated();
                }
            }
        });
    }

    /**
     * 读取json文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(ClassifationActivity context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
