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
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by liyan on 2017/5/9.
 */

public class MyEaseChatRowProject extends EaseChatRow {


//    private LinearLayout yaofangLl;
    private TextView sickNameTv,sickConditionTv;
//    private ImageView iv_mychatlist_xiangmuice;
//    private TextView tv_mychatlist_xiangmucontent;
    private LinearLayout ll_mychatlist_xiangmu;
    private TextView tv_mychatlist_xiangmuname;
    private ImageView iv_mychatlist_xiangmuice;
    private TextView tv_mychatlist_xiangmucontent;
    private String yaofangName;
    private String yaofangContent;

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
          sickNameTv = (TextView) findViewById(R.id.tv_sick_name);
        sickConditionTv = (TextView) findViewById(R.id.tv_sick_condition);
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

        if (message.getBooleanAttribute("yaofang",true)) {
            String yaofangNum = message.getStringAttribute("yaofangNum", null);
            String yaofangPrice = message.getStringAttribute("yaofangPrice", null);
//            sickNameTv.setText(yaofangNum);
//            sickConditionTv.setText(yaofangPrice);
           // handleTextMessage();
        }

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
        } else {
            if (!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
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
