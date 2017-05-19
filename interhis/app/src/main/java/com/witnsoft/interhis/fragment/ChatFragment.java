package com.witnsoft.interhis.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.witnsoft.interhis.R;

/**
 * Created by liyan on 2017/5/9.
 */

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {
    /**
     * 药方
     */

    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;
    private static final int ITEM_PROJECT=20;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    private static final int REQUEST_CODE_SELECT=20;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

    /**
     * if it is chatBot
     */
    private boolean isRobot;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: " );
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //隐藏titlebar
       // hideTitleBar();
    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        super.setUpView();
    }
    //获取拓展消息返回值
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                            //sendFileMessage("");
                        }
                    }
                    break;
            }
        }
    }
    //注册拓展消息  设置图标 名称等
    @Override
    protected void registerExtendMenuItem() {
        inputMenu.registerExtendMenuItem("文件", R.drawable.ic_launcher, ITEM_FILE, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem("项目", R.drawable.ic_launcher, ITEM_PROJECT, extendMenuItemClickListener);
        Log.e(TAG, "registerExtendMenuItem: " );
        inputMenu.init();
    }
    //设置消息属性
    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }
    // 进入聊天细节
    @Override
    public void onEnterToChatDetails() {

    }
    //点击头像
    @Override
    public void onAvatarClick(String username) {

    }
    //长按头像
    @Override
    public void onAvatarLongClick(String username) {

    }
    //点击气泡
    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }
    //长按气泡
    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }
    //扩展菜单项点击 如果想覆盖 返回true
    //设置拓展消息点击事件
    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        Log.e(TAG, "onExtendMenuItemClick: " );
        switch (itemId) {
            case ITEM_FILE:
                selectFileFromLocal();
                break;
            case ITEM_PROJECT:
                sendProject();
                break;

            default:
                break;
        }
        return false;
    }

    private void sendProject() {
        // TODO Auto-generated method stub
        //TODO 这个地方就是发送消息了 试试可不可以在别的地方写
        EMMessage message = EMMessage.createTxtSendMessage("project", toChatUsername);

        // 增加自己特定的属性
        message.setAttribute("type", "project");
        sendMessage(message);
    }


    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }
    //设置自定义的聊天行提供者
    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
