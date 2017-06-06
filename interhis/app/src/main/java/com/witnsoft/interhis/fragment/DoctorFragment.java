package com.witnsoft.interhis.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.witnsoft.interhis.adapter.PatAdapter;
import com.witnsoft.interhis.bean.CeShi;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.mainpage.LoginActivity;
import com.witnsoft.libinterhis.utils.LogUtils;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by ${liyan} on 2017/5/8.
 */

@ContentView(R.layout.fragment_doctor)
public class DoctorFragment extends Fragment {
    private static final String TAG = "DoctorFragment";
    private static LogUtils logUtils = LogUtils.getLog();
    private Gson gson;

    private static final String TN_DOC_INFO = "F27.APP.01.01";
    private static final String TN_COUNT = "F27.APP.01.05";
    private static final String DOC_ID = "docid";
    private static final String DATA = "DATA";

    private final class ErrCode {
        private static final String ErrCode_200 = "200";
        private static final String ErrCode_504 = "504";
    }

    private final class DocInfoResponseKey {
        private static final String DOC_NAME = "docname";
        private static final String ZYDJ = "zydj";
        private static final String PJFS = "pjfs";
        private static final String JZL = "jzl";
        private static final String SSYYMC = "ssyymc";
        private static final String SSKB1MC = "sskb1mc";
    }

    private final class CountResponseKey {
        private static final String DD = "dd";
        private static final String JZL = "jzl";
        private static final String BRSR = "brsr";
        private static final String LJSR = "ljsr";
    }

    private PatAdapter patAdapter;

//    private List<CeShi> data;

    private String docId = "";

    // 下拉刷新
    @ViewInject(R.id.sl_refresh)
    private SwipeRefreshLayout slRefresh;
    // 患者列表
    @ViewInject(R.id.rv_pat)
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
    //医生信息
    @ViewInject(R.id.doctor_message)
    private LinearLayout doctor_message;
    //医生接诊数量
    @ViewInject(R.id.doctor_count)
    private LinearLayout doctor_number;
    //出诊按钮
    @ViewInject(R.id.doctor_visit)
    private Button visit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initViews();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private void initViews() {
        gson = new Gson();
        slRefresh.setEnabled(false);
        // 接收新消息通知广播
        receiver = new RefreshFriendListBroadcastReceiver();
        getActivity().registerReceiver(receiver, new IntentFilter(BROADCAST_REFRESH_LIST));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        docId = ThriftPreUtils.getDocId(getActivity());
        callDocInfoApi();
        callCountApi();


        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctor_message.setVisibility(View.VISIBLE);
                doctor_number.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.VISIBLE);
                if (!isVisit) {
                    isVisit = true;
                    slRefresh.setEnabled(true);
                    getChatList();
//                    initPatList();
                }
            }
        });
    }

    // F27.APP.01.01 查询医生详细信息
    private void callDocInfoApi() {
        OTRequest otRequest = new OTRequest(getActivity());
        // DATA
        DataModel data = new DataModel();
        data.setParam(DOC_ID, docId);
        otRequest.setDATA(data);
        // TN 接口辨别
        otRequest.setTN(TN_DOC_INFO);

        NetTool.getInstance().startRequest(false, getActivity(), null, otRequest, new CallBack<Map, String>() {
            @Override
            public void onSuccess(Map response, String resultCode) {
                if (ErrCode.ErrCode_200.equals(resultCode)) {
                    if (null != response) {
                        if (null != response.get(DATA)) {
                            Map<String, String> data = (Map<String, String>) response.get(DATA);
                            // 医生姓名
                            if (!TextUtils.isEmpty(data.get(DocInfoResponseKey.DOC_NAME))) {
                                tvDocName.setText(data.get(DocInfoResponseKey.DOC_NAME));
                            }
                            // 医生职称
                            if (!TextUtils.isEmpty(data.get(DocInfoResponseKey.ZYDJ))) {
                                tvDocDuties.setText(data.get(DocInfoResponseKey.ZYDJ));
                            }
                            // 医生评分
                            if (!TextUtils.isEmpty(data.get(DocInfoResponseKey.PJFS))) {
                                tvDocGrade.setText(data.get(DocInfoResponseKey.PJFS));
                            }
                            // 接诊量
                            if (!TextUtils.isEmpty(data.get(DocInfoResponseKey.JZL))) {
                                tvDocNum.setText(data.get(DocInfoResponseKey.JZL));
                            }
                            // 医生所在医院，科室
                            String hosp = "";
                            if (!TextUtils.isEmpty(data.get(DocInfoResponseKey.SSYYMC))) {
                                hosp = data.get(DocInfoResponseKey.SSYYMC);
                            }
                            if (!TextUtils.isEmpty(data.get(DocInfoResponseKey.SSKB1MC))) {
                                hosp = hosp + " " + data.get(DocInfoResponseKey.SSKB1MC);
                            }
                            if (!TextUtils.isEmpty(hosp)) {
                                tvHosp.setText(hosp);
                            }
                        }
                    }
//                    callPatApi();
                } else if (ErrCode.ErrCode_504.equals(resultCode)) {
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
        data.setParam(DOC_ID, docId);
        otRequest.setDATA(data);
        // TN 接口辨别
        otRequest.setTN(TN_COUNT);

        NetTool.getInstance().startRequest(false, getActivity(), null, otRequest, new CallBack<Map, String>() {
            @Override
            public void onSuccess(Map response, String resultCode) {
                if (ErrCode.ErrCode_200.equals(resultCode)) {
                    if (null != response) {
                        Map<String, String> data = (Map<String, String>) response.get(DATA);
                        // 等待人数
                        if (!TextUtils.isEmpty(data.get(CountResponseKey.DD))) {
                            tvPatWaiting.setText(data.get(CountResponseKey.DD));
                        }
                        // 累计人数
                        if (!TextUtils.isEmpty(data.get(CountResponseKey.JZL))) {
                            tvPatAll.setText(data.get(CountResponseKey.JZL));
                        }
                        // 本日收入
                        if (!TextUtils.isEmpty(data.get(CountResponseKey.BRSR))) {
                            tvDailyIncome.setText(data.get(CountResponseKey.BRSR));
                        }
                        // 累计收入
                        if (!TextUtils.isEmpty(data.get(CountResponseKey.LJSR))) {
                            tvAllIncome.setText(data.get(CountResponseKey.LJSR));
                        }
                    }
                } else if (ErrCode.ErrCode_504.equals(resultCode)) {
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

    // TODO: 2017/6/2 测试数据 
    private String[] name = new String[]{"ceshi2", "test001", "patid001"};
    private String[] sex = new String[]{"男", "女", "男"};
    private int[] age = new int[]{22, 40, 55};
    private String[] content = new String[]{"头痛", "嗓子痛", "感冒"};

    private RefreshFriendListBroadcastReceiver receiver;
    private static final String BROADCAST_REFRESH_LIST = "broadcastRefreshList";
    private static final String MESSAGE_USER_NAME = "messageUserName";

    // 接收application发来的广播，更新好友列表
    private class RefreshFriendListBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "!!!!!!!!!!!!doctorFragment has received message");
            // 获取到新消息的用户名
            String messageUserName = intent.getStringExtra(MESSAGE_USER_NAME);
            if (isVisit) {
                // 出诊
                if (null != data && 0 < data.size()) {
                    // 如果本地列表没有当前用户会话，重新获取环信会话列表，刷新界面
                    boolean isRefresh = true;
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getName().equals(messageUserName)) {
                            isRefresh = false;
                        }
                    }
                    if (isRefresh) {
//                        getFriendListAndRefreshData();
                        getChatList();

                        Log.e(TAG, "getFriendsList");
                    }
                } else {
//                    getFriendListAndRefreshData();
                    getChatList();
                }
            }
        }
    }

    private void getChatList() {
        Log.e(TAG, "!!!!!chatList begin");
        // 获取环信会话列表
        List<String> nameList = new ArrayList<String>();
        EMClient.getInstance().chatManager().loadAllConversations();
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        Iterator<String> iter = conversations.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            nameList.add(key);
        }
        if (null != nameList && 0 < nameList.size()) {
            data.clear();
            for (int i = 0; i < nameList.size(); i++) {
                CeShi ceshi = ceshi(nameList.get(i));
                data.add(ceshi);
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                freshUi();
            }
        });
        Log.e(TAG, "!!!!!chatList done");
    }

    private CeShi ceshi(String userName) {
        // TODO: 2017/6/6 明天验证这种方法是否合适（通过第一条消息获取患者信息）
        EMConversation conversation;
        conversation = EMClient.getInstance().chatManager().getConversation(userName, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
        EMMessage message = null;
        java.util.List<EMMessage> var = conversation.getAllMessages();
        message = var.get(0);
        Map<String, Object> extMap = message.ext();

        Map<String, Object> objectMap = new HashMap<String, Object>();
        String content = (String) extMap.get("content");

        Map<String, Object> contentMap = gson.fromJson(content, objectMap.getClass());
        Map<String, Object> patinfoMap = (Map<String, Object>) contentMap.get("patinfo");
        // 姓名
        String patname = (String) patinfoMap.get("patname");
        // 性别
        String patsexname = (String) patinfoMap.get("patsexname");
        // 年龄
        String patnlmc = (String) patinfoMap.get("patnlmc");
        // 症状
        String patContent = (String) contentMap.get("jbmc");
        return new CeShi(userName, patname, patsexname, patContent, patnlmc);
    }

    private List<CeShi> data = new ArrayList();
    private boolean isVisit = false;

    // 初始化出诊患者列表
    private void freshUi() {
        patAdapter = new PatAdapter(getContext(), data);
        patAdapter.setOnRecyclerViewItemClickListener(new PatAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClicked(PatAdapter adapter, int position) {
                patAdapter.setPos(position);
                //启动会话列表
                HelperFragment helperFragment = (HelperFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.helper);
                try {
                    Log.e(TAG, "!!!!arryay position = " + position + "  and data = " + data.get(position).getName());
                    helperFragment.getContent(EaseConstant.EXTRA_USER_ID,
                            data.get(position).getName(),
                            EaseConstant.EXTRA_CHAT_TYPE,
                            EaseConstant.CHATTYPE_SINGLE);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e(TAG, "!!!!!!!!!!!!!ArrayIndexOutOfBoundsException in freshUi()");
                }
                patAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(patAdapter);
        slRefresh.setRefreshing(false);
        slRefresh.setEnabled(true);
        refreshRecyclerView();
    }

    private void refreshRecyclerView() {
        slRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        slRefresh.setEnabled(false);
                        slRefresh.setRefreshing(true);
                        getChatList();
                    }
                }, 600);
            }
        });
    }
}
