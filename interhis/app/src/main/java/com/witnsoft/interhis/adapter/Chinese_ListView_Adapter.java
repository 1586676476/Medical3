package com.witnsoft.interhis.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.witnsoft.interhis.R;
import com.witnsoft.interhis.fragment.HelperFragment;
import com.witnsoft.interhis.inter.FilterListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${liyan} on 2017/5/17.
 */

public class Chinese_ListView_Adapter extends BaseAdapter implements Filterable{

    private List<String> list=new ArrayList<String>();
    private Context context;
    private MyFilter filter=null;//创建MyFilter对象
    private FilterListener listener=null;//接口对象

    public Chinese_ListView_Adapter(List<String> list, Context context, FilterListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.fragment_helper_chinese_listview_item,null);
            holder=new ViewHolder();
            holder.tv_ss= (TextView) convertView.findViewById(R.id.item_text);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        holder.tv_ss.setText(list.get(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        //如果MyFilter对象为空，那么重写创建一个
        if (filter==null){
            filter=new MyFilter(list);
        }
        return filter;
    }

    class MyFilter extends Filter{
        //创建集合保存原始数据
        private List<String> original=new ArrayList<String>();

        public MyFilter(List<String> original) {
            this.original = original;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //创建对象
            FilterResults results=new FilterResults();

            /**
             * 没有搜索内容的话就还是给results赋值原始数据的值和大小
             * 执行了搜索的话，根据搜索的规则过滤即可，最后把过滤后的数据的值和大小赋值个给results
             */
            if (TextUtils.isEmpty(constraint)){
                results.values=original;
                results.count=original.size();
            }else {
                //创建一个结合保存过滤后的数据
                List<String> mList=new ArrayList<String>();
                //遍历原始数据集合，根据搜索的规则过滤数据
                for (String s:original){
                    //这里就是过滤规则的具体实现
                    if (s.trim().toLowerCase().contains(constraint.toString().trim().toLowerCase())){
                        //规则匹配的话就往集合中添加该数据
                        mList.add(s);
                    }
                }
                results.values=mList;
                results.count=mList.size();
            }
            return results;
        }

        /**
         * 该方法用来刷新用户界面，根据过滤后的数据重新展示列表
         * @param constraint
         * @param results
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //获取过滤后的数据
            list= (List<String>) results.values;
            //如果接口对象不为空，那么调用接口中的方法获取过滤后的数据，具体的实现在new这个接口的时候
            //重写的方法里面执行
            if (listener!=null){
               listener.getFilterData(list);
            }
            notifyDataSetChanged();
        }
    }

    class ViewHolder{
        TextView tv_ss;
    }
}
