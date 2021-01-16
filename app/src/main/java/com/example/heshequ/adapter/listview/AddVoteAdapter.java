package com.example.heshequ.adapter.listview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.heshequ.R;
import com.example.heshequ.entity.Item;
import com.example.heshequ.utils.Utils;

import java.util.ArrayList;


/**
 * Hulk_Zhang on 2017/6/30 11:26
 * Copyright 2016, 长沙豆子信息技术有限公司, All rights reserved.
 */
public class AddVoteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Item> data=new ArrayList<>();
    public AddVoteAdapter(Context context,ArrayList<Item> data) {
        this.context=context;
        this.data=data;
    }
    public void setData(ArrayList<Item> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final Item item=data.get(position);
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.voew_additem,null);
            viewHolder = new ViewHolder();
            viewHolder.etName= (EditText) view.findViewById(R.id.etName);
            viewHolder.ivIcom= (ImageView) view.findViewById(R.id.ivIcon);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.etName.setTag(item);
        viewHolder.etName.clearFocus();
        viewHolder.ivIcom.setImageResource(position==data.size()-1?R.mipmap.increasdtp:R.mipmap.remove);
        viewHolder.etName.setText(item.getName());
        viewHolder.etName.setHint(position==data.size()-1?"添加选项":"选项"+(position+1));
        viewHolder.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Item item1= (Item) viewHolder.etName.getTag();
                item1.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        viewHolder.etName.setEnabled(position==data.size()-1?false:true);
        viewHolder.ivIcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==data.size()-1)
                {
                    if (data.size() > 15){
                        Utils.toastShort(context,"最多只能添加15个选项");
                        return;
                    }
                    data.add(data.size()-1,new Item());
                }else {
                    data.remove(position);
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }
    public class ViewHolder{
            EditText etName;
           ImageView ivIcom;
    }
}
