package com.witnsoft.interhis.tool;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.witnsoft.interhis.db.HisDbManager;
import com.witnsoft.interhis.fragment.DoctorFragment;

import org.xutils.x;

import java.util.List;
import java.util.Map;

/**
 * Created by zhengchengpeng on 2017/5/12.
 */


public class Application extends MultiDexApplication {

    private static Application app = null;
    private EMMessageListener mMessageListener;
    private static final String BROADCAST_REFRESH_LIST = "broadcastRefreshList";
    private static final String MESSAGE_USER_NAME = "messageUserName";


    public static synchronized Application getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        HisDbManager.attachTo(this);
        app = this;
        init();
        registerMessageListener();

    }

    private void init() {
        //环信聊天初始化
        EaseUI.getInstance().init(this, null);
        EMClient.getInstance().setDebugMode(true);

    }


    public void registerMessageListener() {


        mMessageListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> list) {
                Log.e("MainActivity", "!!!!!!!!!!!!########");
                // 收到新消息，发通知给doctorFragment
                for (EMMessage message : list) {
                    if (!EaseUI.getInstance().hasForegroundActivies()) {
                        EaseUI.getInstance().getNotifier().onNewMsg(message);

                        // 接收到新消息，将username发送广播通知DoctorFragment刷新列表
                        Intent intent = new Intent();
                        intent.setAction(BROADCAST_REFRESH_LIST);
                        intent.putExtra(MESSAGE_USER_NAME, message.getUserName());
                        sendBroadcast(intent);
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