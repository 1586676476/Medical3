package com.witnsoft.interhis.tool;


import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import org.xutils.x;

import java.util.List;

/**
 * Created by zhengchengpeng on 2017/5/12.
 */


public class Application extends MultiDexApplication {

    private static Application app = null;
    private EMMessageListener mMessageListener;




    public static synchronized Application getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        app = this;
        init();
        registerMessageListener();



    }

    private void init() {
        //环信聊天初始化
        EaseUI.getInstance().init(this, null);
        EMClient.getInstance().setDebugMode(true);

    }

    public void registerMessageListener(){


        mMessageListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> list) {
                Log.e("MainActivity", "!!!!!!!!!!!!########");
                // sendBroadcast(new Intent("refresh"));
                for (EMMessage message : list) {
                    if (!EaseUI.getInstance().hasForegroundActivies()){
                        EaseUI.getInstance().getNotifier().onNewMsg(message);
                    }
                }

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }
}