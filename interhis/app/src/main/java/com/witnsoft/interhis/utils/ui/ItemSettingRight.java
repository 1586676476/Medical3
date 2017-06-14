package com.witnsoft.interhis.utils.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.witnsoft.interhis.R;

/**
 * Created by zhengchengpeng on 2017/6/14.
 */

public class ItemSettingRight extends RelativeLayout {

    private TextView tvTitle;

    public ItemSettingRight(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_setting_right, this, true);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    public void setTvTitle(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvTitle.setText(str);
        } else {
            tvTitle.setText(str);
        }
    }
}
