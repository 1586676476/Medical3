package com.witnsoft.interhis.db;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.List;

/**
 * Created by dllo on 17/3/24.
 */

public class LiteOrmTool {
    private static LiteOrm liteOrm;

    public LiteOrm getLiteOrm() {
        return liteOrm;
    }

    public void setLiteOrm(LiteOrm liteOrm) {
        this.liteOrm = liteOrm;
    }
    //添加一条数据
    public static <T> void insert(final T t){
        MyThreadPool.getThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                liteOrm.insert(t);
            }
        });
    }

    //添加一个集合
    public static <T> void insertAll(final List<T> list){
        MyThreadPool.getThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                liteOrm.insert(list);
            }
        });
    }
    //删除某一条数据 content为输入某个字段 value为数据库中要查找的数据
    public static <T> void delete(Class<T> tClass,String content,String value){
        liteOrm.delete(tClass, WhereBuilder.create(tClass).where(content+"=?",value));
    }
    //删除所有的数据
    public static <T> void deleteAll(Class<T> tClass){
        liteOrm.delete(tClass);
    }
    //更新所有的数据
    public static <T> void updataList(List<T> list){
        liteOrm.update(list);
    }
    //更新某一条数据
    public static <T> void updata(T t){
        liteOrm.update(t);
    }
    //查询所有
    public static <T> List<T> getQueryAll(Class<T> tClass){
        return liteOrm.query(tClass);
    }
    //查询某一条数据
    public static <T> List<T> getQueryByWhere(Class<T> tClass,String content,String value){
        return liteOrm.<T>query(new QueryBuilder<T>(tClass).where(content+"=?",value));
    }
}
