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
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.witnsoft.interhis.adapter.PatAdapter;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.db.HisDbManager;
import com.witnsoft.interhis.db.model.ChineseDetailModel;
import com.witnsoft.interhis.mainpage.LoginActivity;
import com.witnsoft.interhis.tool.Application;
import com.witnsoft.interhis.utils.ComRecyclerAdapter;
import com.witnsoft.libinterhis.utils.LogUtils;
import com.witnsoft.libinterhis.utils.ThriftPreUtils;
import com.witnsoft.libnet.model.DataModel;
import com.witnsoft.libnet.model.LoginRequest;
import com.witnsoft.libnet.model.OTRequest;
import com.witnsoft.libnet.net.CallBack;
import com.witnsoft.libnet.net.NetTool;

import org.xutils.ex.DbException;
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
public class DoctorFragment extends Fragment {
    private static final String TAG = "DoctorFragment";
    private static LogUtils logUtils = LogUtils.getLog();

    private static final String TN_DOC_INFO = "F27.APP.01.01";
    private static final String TN_COUNT = "F27.APP.01.05";
    private static final String TN_PAT_LIST = "F27.APP.01.02";
    private static final String TN_VISIT = "F27.APP.01.03";
    private static final String DOC_ID = "docid";
    private static final String DATA = "DATA";
    private static final String ROWSPERPAGE = "rowsperpage";
    private static final String PAGE_NO = "pageno";
    private static final String ORDER_COLUMN = "ordercolumn";
    private static final String ORDER_TYPE = "ordertype";
    private static final String WORK_FLAG = "workflag";
    private static final String LOGOUT = "logout";
    private static final String LOGIN_NAME = "LOGINNAME";
    private static final String ERRO_MSG = "errmsg";
    private static final int PAGE_COUNT = 10;

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

    private PatAdapter patAdapter = null;
    private Gson gson;
    private String docId = "";
    private List<Map<String, String>> dataChatList = new ArrayList();

    // 下拉刷新
    @ViewInject(R.id.sl_refresh)
    private SwipeRefreshLayout slRefresh;
    // 联系人优化页
    @ViewInject(R.id.tv_no_contact)
    private TextView tvNoContact;
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
    private Button btnVisit;
    // 退出登录／收工
    @ViewInject(R.id.btn_take_rest)
    private Button btnTakeRest;

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
//        isVisiting = ThriftPreUtils.getIsVisiting(getActivity());
        if (!ThriftPreUtils.getIsVisiting(getActivity())) {
            // 不在出诊状态
            setBtnRest();
        } else {
            // 出诊中
            setBtnVisiting();
        }
        docId = ThriftPreUtils.getDocId(getActivity());
        callDocInfoApi();
        callCountApi(true);
        btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctor_message.setVisibility(View.VISIBLE);
                // 出诊
                callVisitApi();
//                    getChatList();
//                callPatListApi();
            }
        });
        btnTakeRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ThriftPreUtils.getIsVisiting(getActivity())) {
                    // 退出登录
                    callLogoutApi();
                } else {
                    // 收工
                    callVisitApi();
                }
            }
        });
    }

    /**
     * F27.APP.01.01 查询医生详细信息
     */
    private void callDocInfoApi() {
        OTRequest otRequest = new OTRequest(getActivity());
        // DATA
        DataModel data = new DataModel();
        data.setParam(DOC_ID, docId);
        otRequest.setDATA(data);
        // TN 接口辨别
        otRequest.setTN(TN_DOC_INFO);

        NetTool.getInstance().startRequest(false, true, getActivity(), null, otRequest, new CallBack<Map, String>() {
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

    /**
     * F27.APP.01.05 获得统计值
     */
    private void callCountApi(boolean isProgress) {
        OTRequest otRequest = new OTRequest(getActivity());
        // DATA
        DataModel data = new DataModel();
        data.setParam(DOC_ID, docId);
        otRequest.setDATA(data);
        // TN 接口辨别
        otRequest.setTN(TN_COUNT);

        NetTool.getInstance().startRequest(false, isProgress, getActivity(), null, otRequest, new CallBack<Map, String>() {
            @Override
            public void onSuccess(Map response, String resultCode) {
                if (ErrCode.ErrCode_200.equals(resultCode)) {
                    if (null != response) {
                        doctor_number.setVisibility(View.VISIBLE);
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

    private int pageNo = 1;
    private List<Map<String, String>> respList = new ArrayList<Map<String, String>>();
    // 记录点击位置
    private int checkedPosition = -1;

    /**
     * F27.APP.01.02 查询问诊人员列表
     */
    private void callPatListApi(boolean isProgress) {
        OTRequest otRequest = new OTRequest(getActivity());
        // DATA
        final DataModel data = new DataModel();
        data.setParam(DOC_ID, docId);
        data.setParam(ORDER_COLUMN, "paytime");
        data.setParam(ORDER_TYPE, "asc");
        data.setParam(ROWSPERPAGE, String.valueOf(PAGE_COUNT));
        //分页
        data.setParam(PAGE_NO, String.valueOf(pageNo));
        otRequest.setDATA(data);
        // TN 接口辨别
        otRequest.setTN(TN_PAT_LIST);

        NetTool.getInstance().startRequest(false, isProgress, getActivity(), null, otRequest, new CallBack<Map, String>() {
            @Override
            public void onSuccess(final Map response, String resultCode) {
                if (ErrCode.ErrCode_200.equals(resultCode)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnVisit.setEnabled(false);
                            if (null != response) {
                                respList.clear();
                                respList = (List<Map<String, String>>) response.get(DATA);
                                if (null != respList && 0 < respList.size()) {
                                    if (1 == pageNo) {
                                        // 如果是第一页，表示重新加载数据
                                        dataChatList.clear();
                                        dataChatList = respList;
                                    } else {
                                        // 不是第一页，表示分页加载
                                        for (int i = 0; i < respList.size(); i++) {
                                            dataChatList.add(respList.get(i));
                                        }
                                    }
                                    // 记录点击位置
                                    if (-1 < checkedPosition) {
                                        try {
                                            dataChatList.get(checkedPosition).put("color", "changed");
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            e.printStackTrace();
                                            Log.e(TAG, "!!!!ArrayIndexOutOfBoundsException in checkedPosition");
                                        }
                                    }
                                    // 获取未读消息
                                    if (null != dataChatList && 0 < dataChatList.size()) {
                                        for (int i = 0; i < dataChatList.size(); i++) {
                                            int unReadNumber = 0;
                                            try {
                                                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(dataChatList.get(i).get("ACCID"));
                                                unReadNumber = conversation.getUnreadMsgCount();
                                                Log.e(TAG, "unReadNumber = " + String.valueOf(unReadNumber));
                                            } catch (Exception e) {
                                                unReadNumber = 0;
                                            }
                                            dataChatList.get(i).put("readNo", String.valueOf(unReadNumber));
                                        }
                                    }
                                }

                                if (null != dataChatList && 0 < dataChatList.size()) {
                                    tvNoContact.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else if (1 == pageNo) {
                                    // 保证是第一次加载时的判断，分页加载不显示优化页
                                    tvNoContact.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                if (null == patAdapter) {
                                    patAdapter = new PatAdapter(getContext(), dataChatList, R.layout.fragment_doctor_recycleview_item);
                                    patAdapter.setFootViewId(R.layout.activity_load_footer);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setAdapter(patAdapter);
                                } else {
                                    patAdapter.notifyDataSetChanged();
                                }
                                patAdapter.setCanNotReadBottom(false);
                                patAdapter.setOnRecyclerViewBottomListener(new ComRecyclerAdapter.OnRecyclerViewBottomListener() {
                                    @Override
                                    public void OnBottom() {
                                        Log.d(TAG, "OnBottom: out");
                                        if (respList != null && respList.size() == PAGE_COUNT) {
                                            slRefresh.setEnabled(false);
                                            slRefresh.setRefreshing(true);
                                            patAdapter.setCanNotReadBottom(true);
                                            Log.d(TAG, "OnBottom: in");
                                            pageNo++;
                                            callPatListApi(true);
                                        }
                                    }
                                });
                                patAdapter.setOnItemClickListener(new ComRecyclerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        checkedPosition = position;
                                        for (int i = 0; i < dataChatList.size(); i++) {
                                            // 设置点击变色
                                            dataChatList.get(i).put("color", "unchanged");
                                        }
                                        // 设置已读
                                        dataChatList.get(position).put("readNo", "0");
                                        if (!TextUtils.isEmpty(dataChatList.get(position).get("ACCID"))) {
                                            try {
                                                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(dataChatList.get(position).get("ACCID"));
                                                //指定会话消息未读数清零
                                                conversation.markAllMessagesAsRead();
                                            } catch (Exception e) {

                                            }
                                        }
                                        dataChatList.get(position).put("color", "changed");
                                        patAdapter.notifyDataSetChanged();
                                        Intent intent = new Intent("SHUAXIN");
                                        getActivity().sendBroadcast(intent);
                                        //启动会话列表
                                        HelperFragment helperFragment = (HelperFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.helper);
                                        try {
                                            Log.e(TAG, "!!!!arryay position = " + position + "  and data = " + dataChatList.get(position).get("ACCID"));
                                            helperFragment.getContent(EaseConstant.EXTRA_USER_ID,
                                                    dataChatList.get(position).get("ACCID"),
                                                    EaseConstant.EXTRA_CHAT_TYPE,
                                                    EaseConstant.CHATTYPE_SINGLE);

                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            Log.e(TAG, "!!!!!!!!!!!!!ArrayIndexOutOfBoundsException in freshUi()");

                                        }
                                        //将UserName作为主键 存入数据库
                                        ChineseDetailModel chineseDetailModel = new ChineseDetailModel();
                                        chineseDetailModel.setAcmId(dataChatList.get(position).get(LOGIN_NAME));
                                        try {
                                            HisDbManager.getManager().saveAskChinese(chineseDetailModel);
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                slRefresh.setRefreshing(false);
                                slRefresh.setEnabled(true);
                                refreshRecyclerView();
                                // 会话列表变化时调用统计接口刷新统计数值
                                callCountApi(false);
                            }
                            Log.e(TAG, "!!!!!chatList done");
                        }
                    });
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

    // 是否出诊状态
//    private boolean isVisiting = false;

    /**
     * F27.APP.01.03 出诊／收工／离开
     */
    private void callVisitApi() {
        OTRequest otRequest = new OTRequest(getActivity());
        // DATA
        DataModel data = new DataModel();
        if (!ThriftPreUtils.getIsVisiting(getActivity())) {
            // 出诊
            data.setParam(WORK_FLAG, "online");
        } else {
            // 收工
            data.setParam(WORK_FLAG, "offline");
        }
        data.setParam(DOC_ID, docId);
        otRequest.setDATA(data);
        // TN 接口辨别
        otRequest.setTN(TN_VISIT);

        NetTool.getInstance().startRequest(false, true, getActivity(), null, otRequest, new CallBack<Map, String>() {
            @Override
            public void onSuccess(Map response, String resultCode) {
                if (ErrCode.ErrCode_200.equals(resultCode)) {
                    if (null != response) {
                        if (!ThriftPreUtils.getIsVisiting(getActivity())) {
                            // 出诊
//                            isVisiting = true;
                            ThriftPreUtils.putIsVisiting(getActivity(), true);
                            setBtnVisiting();
                            chatLogin();
                        } else {
                            // 收工
                            // 退出环信聊天
                            chatLogout();
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

    private RefreshFriendListBroadcastReceiver receiver;
    private static final String BROADCAST_REFRESH_LIST = "broadcastRefreshList";
    private static final String MESSAGE_USER_NAME = "messageUserName";

    /**
     * 接收application发来的广播，更新好友列表
     */
    private class RefreshFriendListBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "!!!!!!!!!!!!doctorFragment has received message");
            // 获取到新消息的用户名
//            String messageUserName = intent.getStringExtra(MESSAGE_USER_NAME);
//            // 出诊
//            if (null != dataChatList && 0 < dataChatList.size()) {
//                // 如果本地列表没有当前用户会话，重新获取环信会话列表，刷新界面
//                boolean isRefresh = true;
//                for (int i = 0; i < dataChatList.size(); i++) {
//                    if (dataChatList.get(i).get(LOGIN_NAME).equals(messageUserName)) {
//                        isRefresh = false;
//                    }
//                }
//                if (isRefresh) {
////                        getChatList();
//                    callPatListApi(true);
//
//                    Log.e(TAG, "getFriendsList");
//                }
//            } else {
////                    getChatList();
//                callPatListApi(true);
//            }
            if (Application.BROADCAST_REFRESH_LIST.equals(intent.getAction())) {
                pageNo = 1;
                dataChatList.clear();
                patAdapter = null;
                callPatListApi(true);
            }
        }
    }

    /**
     * 医生登出
     */
    private void callLogoutApi() {
        LoginRequest request = new LoginRequest();
        request.setReqType(LOGOUT);
        NetTool.getInstance().startRequest(true, true, getActivity(), request, null, new CallBack<Map, String>() {
            @Override
            public void onSuccess(Map response, String resultCode) {
                if ("200".equals(resultCode)) {
                    // 登出成功
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    if (null != response.get(ERRO_MSG)) {
                        try {
                            Toast.makeText(getActivity(),
                                    String.valueOf(response.get(ERRO_MSG)), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getActivity(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void refreshRecyclerView() {
        slRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        slRefresh.setEnabled(false);
                        slRefresh.setRefreshing(true);
//                        getChatList();
                        pageNo = 1;
                        dataChatList.clear();
                        patAdapter = null;
                        callPatListApi(false);
                    }
                }, 600);
            }
        });
    }

    /**
     * 环信登录
     */
    private void chatLogin() {
        EMClient.getInstance().login(docId, ThriftPreUtils.getLoginPassword(getActivity()), new EMCallBack() {
            //        EMClient.getInstance().login("ceshi", "111111", new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("onSuccess: ", "登录成功");
                // 获取患者列表
                callPatListApi(true);
            }

            @Override
            public void onError(int i, String s) {
                Log.e("onError: ", i + " " + s + "登录失败");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        isVisiting = false;
                        ThriftPreUtils.putIsVisiting(getActivity(), false);
                        setBtnRest();
                        Toast.makeText(getActivity(), getResources().getString(R.string.chat_failed), Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 环信登出
     */
    private void chatLogout() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ThriftPreUtils.putIsVisiting(getActivity(), false);
//                        isVisiting = false;
                        setBtnRest();
                        dataChatList.clear();
                        pageNo = 1;
                        if (null != patAdapter) {
                            patAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getResources().getString(R.string.chat_logout_failed), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    /**
     * 出诊状态底部按钮处理
     */
    private void setBtnVisiting() {
        btnVisit.setText(getResources().getString(R.string.visiting));
        btnVisit.setTextColor(getResources().getColor(R.color.visit_blue));
        btnVisit.setEnabled(false);
        btnTakeRest.setText(getResources().getString(R.string.take_rest));
    }

    /**
     * 休息状态底部按钮处理
     */
    private void setBtnRest() {
        btnVisit.setText(getResources().getString(R.string.visit));
        btnVisit.setTextColor(getResources().getColor(R.color.colorWhite));
        btnVisit.setEnabled(true);
        btnTakeRest.setText(getResources().getString(R.string.logout));
    }
}
