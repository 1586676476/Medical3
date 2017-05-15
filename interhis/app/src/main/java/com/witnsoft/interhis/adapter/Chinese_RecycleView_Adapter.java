package com.witnsoft.interhis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.witnsoft.interhis.R;
import com.witnsoft.interhis.bean.Prescription;
import com.witnsoft.interhis.inter.OnClick;
import com.witnsoft.interhis.tool.BaseViewHolder;

import java.util.List;

/**
 * Created by ${liyan} on 2017/5/15.
 */

public class Chinese_RecycleView_Adapter extends RecyclerView.Adapter<BaseViewHolder>{
    private static final String TAG = "Chinese_RecycleView_Ada";

    private Context context;
    private List<Prescription> list;
//    private OnClick onClick;
//
//    public void setOnClick(OnClick onClick) {
//        this.onClick = onClick;
//    }


    public void setList(List<Prescription> list) {
        this.list = list;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.onCreatMyViewHolder(context,parent, R.layout.fragment_helper_chinese_recycleview_item);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
