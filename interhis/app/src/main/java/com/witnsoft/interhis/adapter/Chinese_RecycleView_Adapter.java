package com.witnsoft.interhis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.witnsoft.interhis.R;
import com.witnsoft.interhis.fragment.HelperFragment;
import com.witnsoft.interhis.inter.OnClick;
import com.witnsoft.interhis.tool.BaseViewHolder;

import java.util.List;

/**
 * Created by ${liyan} on 2017/5/15.
 */

public class Chinese_RecycleView_Adapter extends RecyclerView.Adapter<BaseViewHolder>{
    private static final String TAG = "Chinese_RecycleView_Ada";

    private Context context;
    private List<String> list;
    private OnClick onClick;
    private int count;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Chinese_RecycleView_Adapter(Context context){
        this.context = context;
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    //定义一个添加text的方法
    public void addTextView(String str){
        list.add(str);
    }

    public void onCountChanged(int count){
        this.count = count;
        notifyDataSetChanged();
    }

    public void deleteTextView(int position){
        list.remove(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.onCreatMyViewHolder(context,parent, R.layout.fragment_helper_chinese_recycleview_item);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClick.onIteClick(position);
//            }
//        });

        holder.setText(R.id.fragment_helper_chinese_recycleview_item_text,list.get(position));
        holder.setText(R.id.fragment_helper_chinese_recycleview_item_number,count+""+"g");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onIteClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
