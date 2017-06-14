package com.witnsoft.interhis.setting;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.witnsoft.interhis.R;
import com.witnsoft.libinterhis.base.BaseFragment;

/**
 * Created by zhengchengpeng on 2017/6/14.
 */

public class ChildBaseFragment extends BaseFragment {

    private FragmentManager mFragmentManager;

    @Override
    protected View initView(View view, LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
    }

//    //创建右侧fragment
//    public void pushChildFragment(ChildBaseFragment fragment, Bundle bundle, boolean isToBackStack) {
//        mFragmentManager = getChildFragmentManager();
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        if (bundle != null) {
//            fragment.setArguments(bundle);
//        }
//        transaction.replace(R.id.fl_content, fragment, ChildBaseFragment.class.getSimpleName());
//        if (isToBackStack) {
//            transaction.addToBackStack(null);
//        }
//        transaction.commit();
//    }

    //右侧子fragment进栈
    public void pushFragment(ChildBaseFragment fragment, Bundle bundle, boolean isToBackStack) {
        mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        transaction.add(R.id.fl_content, fragment, ChildBaseFragment.class.getSimpleName());
        if (isToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void finishFragment() {
        mFragmentManager = getFragmentManager();
        mFragmentManager.popBackStack();
    }
}
