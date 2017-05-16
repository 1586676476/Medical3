package com.witnsoft.interhis.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.hyphenate.easeui.EaseConstant;

import com.witnsoft.interhis.bean.CeShi;

import com.witnsoft.interhis.tool.Application;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.adapter.DoctorAdapter;
import com.witnsoft.interhis.bean.HelperBean;
import com.witnsoft.interhis.inter.OnClick;
import com.witnsoft.interhis.tool.CallBack;
import com.witnsoft.interhis.tool.NetPostTool;

import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Created by ${liyan} on 2017/5/8.
 */

public class DoctorFragment extends Fragment implements OnClick {
    private static final String TAG = "DoctorFragment";

    private String[] name=new String[]{"ceshi2","test001","patid001"};
    private String[] sex=new String[]{"男","女","男"};
    private int[] age=new int[]{22,40,55};
    private String[] content=new String[]{"头痛","嗓子痛","感冒"};
    private RecyclerView recyclerView;
    private DoctorAdapter doctorAdapter;
    private List<CeShi> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_doctor,container,false);
        recyclerView= (RecyclerView) view.findViewById(R.id.fragment_doctor_recycleView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doctorAdapter=new DoctorAdapter(getContext());
        data=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CeShi ceShi=new CeShi(name[i],sex[i],content[i],age[i]);
            data.add(ceShi);
        }
        doctorAdapter.setList(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(doctorAdapter);
        doctorAdapter.setOnClick(this);

    }

    @Override
    public void onIteClick(int position) {
        doctorAdapter.setPos(position);

        //启动会话列表
        HelperFragment helperFragment = (HelperFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.helper);
        helperFragment.getContent(EaseConstant.EXTRA_USER_ID,
                name[position],
                EaseConstant.EXTRA_CHAT_TYPE,
                EaseConstant.CHATTYPE_SINGLE);

    }
}
