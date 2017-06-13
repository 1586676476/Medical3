package com.witnsoft.interhis.setting;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.witnsoft.interhis.R;
import com.witnsoft.libinterhis.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by zhengchengpeng on 2017/6/13.
 */

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    // 我的信息
    @ViewInject(R.id.rl_my_info)
    private RelativeLayout rlMyInfo;
    // 我的问诊记录
    @ViewInject(R.id.rl_my_history)
    private RelativeLayout rlMyHistory;
    // 我的收入
    @ViewInject(R.id.rl_my_income)
    private RelativeLayout rlMyIncome;
    // 关于
    @ViewInject(R.id.rl_about)
    private RelativeLayout rlAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initCLick();
        init();
    }

    private void initCLick() {

    }

    private void init() {

    }
}
