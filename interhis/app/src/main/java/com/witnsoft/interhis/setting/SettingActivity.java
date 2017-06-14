package com.witnsoft.interhis.setting;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.setting.about.AboutFragment;
import com.witnsoft.interhis.setting.myhistory.MyHistoryFragment;
import com.witnsoft.interhis.setting.myincome.MyIncomeFragment;
import com.witnsoft.interhis.setting.myinfo.MyInfoFragment;
import com.witnsoft.libinterhis.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhengchengpeng on 2017/6/13.
 */

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.ll_back)
    private LinearLayout llBack;
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

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initCLick();
        init();
    }

    private void initCLick() {
        // 返回
        RxView.clicks(llBack)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
        // 我的信息
        RxView.clicks(rlMyInfo)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MyInfoFragment myInfoFragment = new MyInfoFragment();
                        setChecked(myInfoFragment, rlMyInfo, rlAbout, rlMyHistory, rlMyIncome);
                    }
                });
        // 我的问诊记录
        RxView.clicks(rlMyHistory)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MyHistoryFragment myHistoryFragment = new MyHistoryFragment();
                        setChecked(myHistoryFragment, rlMyHistory, rlAbout, rlMyInfo, rlMyIncome);
                    }
                });
        // 我的收入
        RxView.clicks(rlMyIncome)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MyIncomeFragment myIncomeFragment = new MyIncomeFragment();
                        setChecked(myIncomeFragment, rlMyIncome, rlAbout, rlMyInfo, rlMyHistory);
                    }
                });
        // 关于
        RxView.clicks(rlAbout)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        AboutFragment aboutFragment = new AboutFragment();
                        setChecked(aboutFragment, rlAbout, rlMyIncome, rlMyInfo, rlMyHistory);
                    }
                });
    }

    private void init() {
        fragmentManager = getFragmentManager();
        MyInfoFragment myInfoFragment = new MyInfoFragment();
        setChecked(myInfoFragment, rlMyInfo, rlAbout, rlMyHistory, rlMyIncome);
    }

    private void setChecked(Fragment fragment, RelativeLayout llChecked, RelativeLayout ll1, RelativeLayout ll2, RelativeLayout ll3) {
        llChecked.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        ll1.setBackgroundColor(getResources().getColor(R.color.background_divider_color));
        ll2.setBackgroundColor(getResources().getColor(R.color.background_divider_color));
        ll3.setBackgroundColor(getResources().getColor(R.color.background_divider_color));
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
}
