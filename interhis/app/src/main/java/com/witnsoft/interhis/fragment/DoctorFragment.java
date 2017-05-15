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

    private String[] name=new String[]{"ceshi","ceshi2","0428"};
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        data=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CeShi ceShi=new CeShi(name[i],sex[i],content[i],age[i]);
            data.add(ceShi);
        }
        recyclerView= (RecyclerView) getActivity().findViewById(R.id.fragment_doctor_recycleView);
        doctorAdapter=new DoctorAdapter(Application.getInstance().getApplicationContext());
        doctorAdapter.setList(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(Application.getInstance().getApplicationContext()));
        recyclerView.setAdapter(doctorAdapter);
        doctorAdapter.setOnClick(this);

    }

    @Override
    public void onIteClick(int position) {
        doctorAdapter.setPos(position);


        //启动会话列表
        HelperFragment helperFragment = (HelperFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.helper);
        helperFragment.getContent(EaseConstant.EXTRA_USER_ID,
                "ceshi2",
                EaseConstant.EXTRA_CHAT_TYPE,
                EaseConstant.CHATTYPE_SINGLE);

    }
}
