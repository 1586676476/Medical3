package com.witnsoft.interhis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.witnsoft.interhis.R;
import com.witnsoft.interhis.bean.PatChatInfo;
import com.witnsoft.interhis.utils.ComRecyclerAdapter;
import com.witnsoft.interhis.utils.ComRecyclerViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by zhengchengpeng on 2017/6/2.
 */

public class PatAdapter extends ComRecyclerAdapter<Map<String, String>> {

    private Context context;
    private int pos = -1;
    private int unread;

    public PatAdapter(Context context, List<Map<String, String>> list, int layoutId) {
        super(context, list, layoutId);
        this.context = context;
        this.list = list;
    }

    @Override
    public void convert(ComRecyclerViewHolder comRecyclerViewHolder, Map<String, String> item) {
        if (!TextUtils.isEmpty(item.get("PATNAME"))) {
            comRecyclerViewHolder.setText(R.id.fragment_doctor_recycleView_item_name, item.get("PATNAME"));
        } else {
            comRecyclerViewHolder.setText(R.id.fragment_doctor_recycleView_item_name, "");
        }
        if (!TextUtils.isEmpty(item.get("PATNL"))) {
            comRecyclerViewHolder.setText(R.id.fragment_doctor_recycleView_item_age, item.get("PATNL"));
        } else {
            comRecyclerViewHolder.setText(R.id.fragment_doctor_recycleView_item_age, "");
        }
        if (!TextUtils.isEmpty(item.get("PATSEXNAME"))) {
            comRecyclerViewHolder.setText(R.id.fragment_doctor_recycleView_item_sex, item.get("PATSEXNAME"));
        } else {
            comRecyclerViewHolder.setText(R.id.fragment_doctor_recycleView_item_sex, "");
        }
        if (!TextUtils.isEmpty(item.get("JBMC"))) {
            comRecyclerViewHolder.setText(R.id.fragment_doctor_recycleView_item_content, item.get("JBMC"));
        } else {
            comRecyclerViewHolder.setText(R.id.fragment_doctor_recycleView_item_content, "");
        }
    }

    @Override
    public void convertHeader(ComRecyclerViewHolder comRecyclerViewHolder) {

    }

    @Override
    public void convertFooter(ComRecyclerViewHolder comRecyclerViewHolder) {
        if (!canNotReadBottom) {
            comRecyclerViewHolder.setText(R.id.load_more, context.getString(R.string.load_done));
        } else {
            comRecyclerViewHolder.setText(R.id.load_more, context.getString(R.string.load_more));
        }
    }
}
