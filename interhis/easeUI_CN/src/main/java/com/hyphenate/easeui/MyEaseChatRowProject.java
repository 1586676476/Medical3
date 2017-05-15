package com.hyphenate.easeui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by liyan on 2017/5/9.
 */

public class MyEaseChatRowProject extends EaseChatRow {

    private LinearLayout ll_mychatlist_xiangmu;
    private TextView tv_mychatlist_xiangmuname;
    private ImageView iv_mychatlist_xiangmuice;
    private TextView tv_mychatlist_xiangmucontent;

    public MyEaseChatRowProject(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        // TODO Auto-generated constructor stub
    }

    /**
     * 注入布局
     */
    @Override
    protected void onInflateView() {
        // TODO Auto-generated method stub
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_project
                : R.layout.ease_row_sent_project, this);
    }

    /**
     * 寻找id
     */
    @Override
    protected void onFindViewById() {
        // TODO Auto-generated method stub

        ll_mychatlist_xiangmu = (LinearLayout) findViewById(R.id.ll_mychatlist_xiangmu);
        tv_mychatlist_xiangmuname = (TextView) findViewById(R.id.tv_mychatlist_xiangmuname);
        iv_mychatlist_xiangmuice = (ImageView) findViewById(R.id.iv_mychatlist_xiangmuice);
        tv_mychatlist_xiangmucontent = (TextView) findViewById(R.id.tv_mychatlist_xiangmucontent);
    }

    /**
     * 刷新列表视图状态更改时消息
     */
    @Override
    protected void onUpdateView() {
        // TODO Auto-generated method stub
        adapter.notifyDataSetChanged();
        Log.e("onUpdateView", "刷新了");
    }

    /**
     * 显示消息和位置等属性
     */
    @Override
    protected void onSetUpView() {
        // TODO Auto-generated method stub

        // 设置内容,通过扩展自文本获取消息内容，填充到相应的位置

        tv_mychatlist_xiangmuname.setText("测试的项目");
        tv_mychatlist_xiangmucontent.setText("测试项目的内容");
        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat){
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 点击气泡
     */
    @Override
    protected void onBubbleClick() {
        // TODO 自定义消息的点击事件
        Toast.makeText(activity, "点击了项目", Toast.LENGTH_SHORT).show();
    }

}
