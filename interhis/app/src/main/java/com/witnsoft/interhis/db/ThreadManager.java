package com.witnsoft.interhis.db;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuyongjie on 17/3/22.
 */


public class ThreadManager {
    private static ThreadPool mThreadPool;
    public static synchronized ThreadPool getmThreadPool(){
        if (mThreadPool==null){
            int cpuNum=Runtime.getRuntime().availableProcessors();//获取设备的cpu核心数
            int count=cpuNum*2+1;
            mThreadPool=new ThreadPool(count,count,0L);
        }
        return mThreadPool;
    }

    public  static class ThreadPool{
        private int corePoolSize; //核心线程数
        private int maxCorePoolSize;//最大线程数
        private long keepAliveTime;  //保持活跃时间(休息时间)
        private ThreadPoolExecutor mExecutor;

        public ThreadPool(int corePoolSize, int maxCorePoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maxCorePoolSize = maxCorePoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable runnable){
            if (mExecutor==null){
                mExecutor=new ThreadPoolExecutor(corePoolSize,maxCorePoolSize,keepAliveTime, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
            }
            mExecutor.execute(runnable);
        }


        public void cancel(Runnable runnable){
            if (mExecutor!=null){
                mExecutor.getQueue().remove(runnable);
            }
        }
    }

    
}
