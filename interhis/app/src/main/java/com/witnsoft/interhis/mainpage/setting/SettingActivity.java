package com.witnsoft.interhis.mainpage.setting;

import android.os.Bundle;

import com.witnsoft.interhis.R;
import com.witnsoft.libinterhis.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by zhengchengpeng on 2017/6/13.
 */

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
