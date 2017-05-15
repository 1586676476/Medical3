package com.witnsoft.interhis.tool;


import android.support.multidex.MultiDexApplication;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;

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
        //环信聊天初始化
        EaseUI.getInstance().init(this, null);
        EMClient.getInstance().setDebugMode(true);

    }
}