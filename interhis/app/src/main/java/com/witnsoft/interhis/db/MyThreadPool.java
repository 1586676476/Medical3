package com.witnsoft.interhis.db;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dllo on 17/3/24.
 */

public class MyThreadPool {
    private static  MyThreadPool ourInstance ;
    private static ThreadPoolExecutor threadPoolExecutor;

    public static MyThreadPool getInstance() {
        return ourInstance;
    }

    private MyThreadPool() {
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(){
        if (ourInstance==null){
            synchronized (MyThreadPool.class){
                if (ourInstance==null){
                    threadPoolExecutor=new ThreadPoolExecutor(2,4,2000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(3));
                }
            }
        }
        return threadPoolExecutor;
    }
}
