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

import java.util.List;

/**
 * Created by zhengchengpeng on 2017/6/2.
 */

public class PatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PatChatInfo> list;
    private int pos = -1;
    OnRecyclerViewItemClickListener clickListener;
    private int unread;

    public PatAdapter(Context context, List<PatChatInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setUnread(int unread) {
        this.unread = unread;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_doctor_recycleview_item, parent, false);
        return new PatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PatChatInfo item = list.get(position);
        if (position == pos) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }
        setText(((PatViewHolder) holder).tvName, item.getName());
        setText(((PatViewHolder) holder).tvAge, item.getAge());
        setText(((PatViewHolder) holder).tvSex, item.getSex());
        setText(((PatViewHolder) holder).tvContent, item.getContent());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setText(TextView tv, String str) {
        if (!TextUtils.isEmpty(str)) {
            tv.setText(str);
        } else {
            tv.setText("");
        }
    }

    private void setText(TextView tv, int i) {
        String str = String.valueOf(i);
        tv.setText(str);
    }

    class PatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // 年龄
        TextView tvAge;
        // 姓名
        TextView tvName;
        // 性别
        TextView tvSex;
        // 内容
        TextView tvContent;


        public PatViewHolder(View itemView) {
            super(itemView);
            tvAge = (TextView) itemView.findViewById(R.id.fragment_doctor_recycleView_item_age);
            tvName = (TextView) itemView.findViewById(R.id.fragment_doctor_recycleView_item_name);
            tvSex = (TextView) itemView.findViewById(R.id.fragment_doctor_recycleView_item_sex);
            tvContent = (TextView) itemView.findViewById(R.id.fragment_doctor_recycleView_item_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClicked(PatAdapter.this, getAdapterPosition());
            }
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClicked(PatAdapter adapter, int position);
    }
}
