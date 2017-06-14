package com.witnsoft.interhis.setting.myhistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.witnsoft.interhis.R;
import com.witnsoft.interhis.setting.ChildBaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by zhengchengpeng on 2017/6/13.
 */

@ContentView(R.layout.fragment_my_history)
public class MyHistoryFragment extends ChildBaseFragment {
    View rootView;

    // 总计接诊量
    @ViewInject(R.id.tv_visit_count_all)
    private TextView tvVisitCountAll;
    // 月接诊量
    @ViewInject(R.id.tv_visit_count_month)
    private TextView tvVisitCountMonth;
    // 日接诊量
    @ViewInject(R.id.tv_visit_count_daily)
    private TextView tvVisitCountDaily;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = x.view().inject(this, inflater, container);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        tvVisitCountAll.setText("33");
        tvVisitCountMonth.setText("22");
        tvVisitCountDaily.setText("11");
    }
}
