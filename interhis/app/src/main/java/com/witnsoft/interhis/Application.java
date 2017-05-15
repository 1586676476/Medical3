package com.witnsoft.interhis;


import android.support.multidex.MultiDexApplication;

/**
 * Created by zhengchengpeng on 2017/5/12.
 */


public class Application extends MultiDexApplication {

    private static Application app = null;
    public static synchronized Application getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        init();
    }

    private void init() {

    }
}