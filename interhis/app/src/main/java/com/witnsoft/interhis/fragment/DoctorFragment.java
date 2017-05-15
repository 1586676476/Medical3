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
import com.witnsoft.interhis.tool.Application;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.adapter.DoctorAdapter;
import com.witnsoft.interhis.bean.HelperBean;
import com.witnsoft.interhis.inter.OnClick;
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

    private String url = "https://zy.renyibao.com/ylapp/debug/";

    private String key1="docid";
    private String value1="test001";
//    private String key1 = "COUNTNUM";
//    private String value1 = "1";
//    private String key2 = "AIID";
//    private String value2 = "aiid002";
//    private String key3 = "DOCID";
//    private String value3 = "test001";
//    private String key4= "DOCNAME";
//    private String value4 = "张强强";
//    private String key5 = "ACCID";
//    private String value5 = "accid001";
//    private String key6 = "PATID";
//    private String value6 = "patid002";
//    private String key7 = "PATNAME";
//    private String value7 = "张三";
//    private String key8 = "JCJKXX";
//    private String value8 = "家族病史、过敏史等";
//    private String key9= "JBMC";
//    private String value9 = "咳嗽多日，无痰，晚上睡眠不好";
//    private String key10 = "WZMD";
//    private String value10 = "first";


//    private String[] name=new String[]{"张三","李四","王五"};
//    private String[] sex=new String[]{"男","女","男"};
//    private int[] age=new int[]{22,40,55};
//    private String[] content=new String[]{"头痛","嗓子痛","感冒"};

    private RecyclerView recyclerView;
    private DoctorAdapter doctorAdapter;
    private List<HelperBean> data;



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
//        for (int i = 0; i < 3; i++) {
//            HelperBean helperBean=new HelperBean(name[i],sex[i],content[i],age[i]);
//            data.add(helperBean);
//        }
        recyclerView= (RecyclerView) getActivity().findViewById(R.id.fragment_doctor_recycleView);
        doctorAdapter=new DoctorAdapter(Application.getInstance().getApplicationContext());
        doctorAdapter.setList(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(Application.getInstance().getApplicationContext()));
        recyclerView.setAdapter(doctorAdapter);
        doctorAdapter.setOnClick(this);
       // eventBus=EventBus.getDefault();

        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody formBody=new FormBody.Builder().add(key1,value1).build();

        final Request request=new Request.Builder().url(url).post(formBody).build();
        Log.e(TAG, "onActivityCreated: "+ url);
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: "+"请求失败");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("TAG", response.body().string());
//            }
//        });
//        NetPostTool.getInstance().startPostRequest(url, formBody, HelperBean.class,
//                new CallBack<HelperBean>() {
//                    @Override
//                    public void onSuccess(HelperBean response) {
//                        data= (List<HelperBean>) response;
//                        Log.e("onSuccess: ", String.valueOf(data.size()));
//                        doctorAdapter.setList(data);
//                }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        Log.e(TAG, "onError: "+"请求失败" );
//                    }
//                }
//        );

    }

    @Override
    public void onIteClick(int position) {
//        HelperBean helperBean=new HelperBean();
//        helperBean.setName(name[position]);
//        helperBean.setSex(sex[position]);
//        helperBean.setAge(age[position]);
//        helperBean.setContent(content[position]);
//        eventBus.post(helperBean);
//        Log.e(TAG, "onIteClick: "+helperBean );
//        doctorAdapter.setPos(position);


        //启动会话列表
        HelperFragment helperFragment = (HelperFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.helper);
        helperFragment.getContent(EaseConstant.EXTRA_USER_ID,
                "ceshi2",
                EaseConstant.EXTRA_CHAT_TYPE,
                EaseConstant.CHATTYPE_SINGLE);

    }
}
