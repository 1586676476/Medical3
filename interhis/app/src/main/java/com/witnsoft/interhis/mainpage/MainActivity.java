package com.witnsoft.interhis.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.witnsoft.interhis.R;
import com.witnsoft.libinterhis.base.BaseActivity;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.List;

/**
 * Created by zhengchengpeng on 2017/5/12.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
<<<<<<< HEAD

=======
>>>>>>> b6a0c70f0c200fc8bfcc33431d35eec18ec6136b
}
