package com.witnsoft.interhis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.witnsoft.interhis.R;
import com.witnsoft.interhis.bean.Prescription;
import com.witnsoft.interhis.tool.BaseViewHolder;

import java.util.List;

/**
 * Created by ${liyan} on 2017/5/15.
 */

public class Chinese_RecycleView_Adapter extends RecyclerView.Adapter<BaseViewHolder>{

    private Context context;
    private List<Prescription> list;

    public void setList(List<Prescription> list) {
        this.list = list;
    }

    public Chinese_RecycleView_Adapter(Context context, List<Prescription> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.onCreatMyViewHolder(context,parent, R.layout.fragment_helper_chinese_recycleview_item);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
    holder.setText(R.id.fragment_helper_chinese_linearLayout_recycleView_edittext,list.get(position).getName());
        holder.setText(R.id.fragment_helper_chinese_linearLayout_recycleView_edittext_number,list.get(position).getNumber()+"");
    }

    @Override
    public int getItemCount() {
        return 20;
    }
}
