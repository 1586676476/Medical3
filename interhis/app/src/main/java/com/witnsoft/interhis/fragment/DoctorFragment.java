package com.witnsoft.interhis.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.hyphenate.easeui.EaseConstant;
import com.witnsoft.interhis.bean.CeShi;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.adapter.DoctorAdapter;
import com.witnsoft.interhis.inter.OnClick;
import com.witnsoft.interhis.mainpage.LoginActivity;
import com.witnsoft.libinterhis.utils.ThriftPreUtils;
import com.witnsoft.libnet.model.DataModel;
import com.witnsoft.libnet.model.OTRequest;
import com.witnsoft.libnet.net.CallBack;
import com.witnsoft.libnet.net.NetTool;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ${liyan} on 2017/5/8.
 */

@ContentView(R.layout.fragment_doctor)
public class DoctorFragment extends Fragment implements OnClick {
    private static final String TAG = "DoctorFragment";

    private String[] name = new String[]{"ceshi2", "test001", "patid001"};
    private String[] sex = new String[]{"男", "女", "男"};
    private int[] age = new int[]{22, 40, 55};
    private String[] content = new String[]{"头痛", "嗓子痛", "感冒"};
    private DoctorAdapter doctorAdapter;
    private List<CeShi> data;

    private String docId = "";

    @ViewInject(R.id.fragment_doctor_recycleView)
    private RecyclerView recyclerView;
    // 医生姓名
    @ViewInject(R.id.fragment_doctor_name)
    private TextView tvDocName;
    // 医生职称
    @ViewInject(R.id.fragment_doctor_duties)
    private TextView tvDocDuties;
    // 医生评分
    @ViewInject(R.id.fragment_doctor_grade)
    private TextView tvDocGrade;
    // 接诊量
    @ViewInject(R.id.fragment_doctor_number)
    private TextView tvDocNum;
    // 医生所在医院，科室
    @ViewInject(R.id.tv_hosp)
    private TextView tvHosp;
    // 等待人数
    @ViewInject(R.id.tv_pat_waiting)
    private TextView tvPatWaiting;
    // 累计人人数
    @ViewInject(R.id.tv_pat_all)
    private TextView tvPatAll;
    // 本日收入
    @ViewInject(R.id.tv_daily_income)
    private TextView tvDailyIncome;
    // 累计收入
    @ViewInject(R.id.tv_all_income)
    private TextView tvAllIncome;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initViews();
        return view;
    }


    private void initViews() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        docId = ThriftPreUtils.getDocId(getActivity());
        callDocInfoApi();
        callCountApi();
        doctorAdapter = new DoctorAdapter(getContext());
        data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CeShi ceShi = new CeShi(name[i], sex[i], content[i], age[i]);
            data.add(ceShi);
        }

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.fragment_doctor_recycleView);
        doctorAdapter = new DoctorAdapter(getContext());

        doctorAdapter.setList(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(doctorAdapter);
        doctorAdapter.setOnClick(this);

    }

    // F27.APP.01.01 查询医生详细信息
    private void callDocInfoApi() {
        OTRequest otRequest = new OTRequest(getActivity());
        // DATA
        DataModel data = new DataModel();
        data.setParam("docid", docId);
        otRequest.setDATA(data);
        // TN 接口辨别
        otRequest.setTN("F27.APP.01.01");

        NetTool.getInstance().startRequest(false, getActivity(), null, otRequest, new CallBack<Map, String>() {
            @Override
            public void onSuccess(Map response, String resultCode) {
                if ("200".equals(resultCode)) {
                    if (null != response) {
                        if (null != response.get("DATA")) {
                            Map<String, String> data = (Map<String, String>) response.get("DATA");
                            // 医生姓名
                            if (!TextUtils.isEmpty(data.get("docname"))) {
                                tvDocName.setText(data.get("docname"));
                            }
                            // 医生职称
                            if (!TextUtils.isEmpty(data.get("zydj"))) {
                                tvDocDuties.setText(data.get("zydj"));
                            }
                            // 医生评分
                            if (!TextUtils.isEmpty(data.get("pjfs"))) {
                                tvDocGrade.setText(data.get("pjfs"));
                            }
                            // 接诊量
                            if (!TextUtils.isEmpty(data.get("jzl"))) {
                                tvDocNum.setText(data.get("jzl"));
                            }
                            // 医生所在医院，科室
                            String hosp = "";
                            if (!TextUtils.isEmpty(data.get("ssyymc"))) {
                                hosp = data.get("ssyymc");
                            }
                            if (!TextUtils.isEmpty(data.get("sskb1mc"))) {
                                hosp = hosp + " " + data.get("sskb1mc");
                            }
                            if (!TextUtils.isEmpty(hosp)) {
                                tvHosp.setText(hosp);
                            }
                        }
                    }
//                    callPatApi();
                } else if ("504".equals(resultCode)) {
                    // token失效
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    // TODO: 2017/5/26 当获取环信新消息，得知患者列表改变动作，调用统计接口，主动请求刷新视图
    // F27.APP.01.05 获得统计值
    private void callCountApi() {
        OTRequest otRequest = new OTRequest(getActivity());
        // DATA
        DataModel data = new DataModel();
        data.setParam("docid", docId);
        otRequest.setDATA(data);
        // TN 接口辨别
        otRequest.setTN("F27.APP.01.05");

        NetTool.getInstance().startRequest(false, getActivity(), null, otRequest, new CallBack<Map, String>() {
            @Override
            public void onSuccess(Map response, String resultCode) {
                if ("200".equals(resultCode)) {
                    if (null != response) {
                        Map<String, String> data = (Map<String, String>) response.get("DATA");
                        // 等待人数
                        if (!TextUtils.isEmpty(data.get("dd"))) {
                            tvPatWaiting.setText(data.get("dd"));
                        }
                        // 累计人数
                        if (!TextUtils.isEmpty(data.get("jzl"))) {
                            tvPatAll.setText(data.get("jzl"));
                        }
                        // 本日收入
                        if (!TextUtils.isEmpty(data.get("brsr"))) {
                            tvDailyIncome.setText(data.get("brsr"));
                        }
                        // 累计收入
                        if (!TextUtils.isEmpty(data.get("ljsr"))) {
                            tvAllIncome.setText(data.get("ljsr"));
                        }
                    }
                } else if ("504".equals(resultCode)) {
                    // token失效
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

//    // TODO: 2017/5/26 上拉加载更多，下拉刷新，docid存本地
//    // F27.APP.01.02 查询问诊人员列表
//    private void callPatApi() {
//        OTRequest otRequest = new OTRequest(getActivity());
//        // DATA
//        DataModel data = new DataModel();
//        data.setParam("docid", docId);
//        data.setParam("rowsperpage", "10");
//        data.setParam("pageno", "1");
//        data.setParam("ordercolumn", "paytime");
//        data.setParam("ordertype", "asc");
//        otRequest.setDATA(data);
//        // TN 接口辨别
//        otRequest.setTN("F27.APP.01.02");
//
//        NetTool.getInstance().startRequest(false, getActivity(), null, otRequest, new CallBack<Map, String>() {
//            @Override
//            public void onSuccess(Map response, String resultCode) {
//                //返回respnse即为DATAARRAY的json字符串，进一步根据需求自行解析
//                if ("200".equals(resultCode)) {
//
//                } else if ("504".equals(resultCode)) {
//                    // token失效
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                    getActivity().finish();
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//            }
//        });
//    }

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
