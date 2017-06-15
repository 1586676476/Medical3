package com.witnsoft.interhis.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.util.Log;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.witnsoft.interhis.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.witnsoft.interhis.adapter.Chinese_Fixed_Adapter;
import com.witnsoft.interhis.adapter.Chinese_ListView_Adapter;
import com.witnsoft.interhis.adapter.Chinese_RecycleView_Adapter;
import com.witnsoft.interhis.adapter.Western_ListView_Adapter;
import com.witnsoft.interhis.adapter.Western_RecycleView_Adapter;
import com.witnsoft.interhis.db.DataHelper;
import com.witnsoft.interhis.db.HisDbManager;
import com.witnsoft.interhis.db.model.ChineseDetailModel;
import com.witnsoft.interhis.db.model.WesternDetailModel;
import com.witnsoft.interhis.inter.DialogListener;

import com.witnsoft.interhis.inter.FilterListener;
import com.witnsoft.interhis.inter.OnClick;
import com.witnsoft.interhis.inter.OnFixClick;
import com.witnsoft.interhis.inter.WritePadDialog;
import com.witnsoft.interhis.mainpage.DialogActivity;
import com.witnsoft.interhis.mainpage.SecondDialogActivity;
import com.witnsoft.interhis.tool.KeyboardUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.ex.DbException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${liyan} on 2017/5/8.
 */

public class HelperFragment extends Fragment implements View.OnClickListener, OnClick, OnFixClick {
    private static final String TAG = "HelperFragment";
    private LinearLayout llContent;
    private TextView tvNoData;
    //手写签名
    private Bitmap mSignBitmap;
    private String signPath;
    //RadioButton
    private RadioButton ask, chat, chinese, western;
    private FrameLayout ask_linearLayout;
    //所对应布局
    private LinearLayout chinese_linearLayout, western_linearLayout, chat_linearLayout;
    private LinearLayout chinese_linearLayout_linearLayout, western_linearLayout_linearLayout;
    private ImageView chinese_img, western_img, chahao, western_chahao;
    //中西药显示部分
    private RecyclerView chinese_recyclerView, western_recycleView;
    private Chinese_RecycleView_Adapter chinese_adapter;
    private Western_RecycleView_Adapter western_adapter;
    private List<ChineseDetailModel> data;
    private List<WesternDetailModel> western_data;

    //中西药按钮
    private Button chinese_button, western_button;

    //中西药搜索框
    private EditText chinese_edittext, western_edittext;
    private Chinese_ListView_Adapter adapter = null;
    private Western_ListView_Adapter western_listView_adapter = null;
    private ListView chinese_listView, western_listView;
    private List<ChineseDetailModel> list = new ArrayList<>();
    private List<WesternDetailModel> western_list = new ArrayList<>();
    //固定药方显示部分
    private RecyclerView chinese_fixed, western_fixed;
    private Chinese_Fixed_Adapter fixed_adapter;
    private List<ChineseDetailModel> fix_data;

    private String userName;
    private String type1;
    private int single1;
    private String id;

    private EaseChatFragment chatFragment;
    private Bundle bundle;

    private Context ctx;
    private Activity act;
    //动态广播
    private Receiver receiver;
    private Refresh refresh;
    private KeyboarrReceiver keyboarrReceiver;

    private String pinyin, accid;

    private Object object;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_helper, container, false);
        /**
         * xiugai de neirong
         * 444444444444444444
         */
        llContent = (LinearLayout) view.findViewById(R.id.ll_content);
        tvNoData = (TextView) view.findViewById(R.id.tv_no_data);

        ask = (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_ask);
        chat = (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_chat);
        chinese = (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_chinese);
        western = (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_western);

        western_linearLayout = (LinearLayout) view.findViewById(R.id.fragment_helper_western_medical_linearLayout);
        chinese_linearLayout = (LinearLayout) view.findViewById(R.id.fragment_helper_chinese_linearLayout);
        chat_linearLayout = (LinearLayout) view.findViewById(R.id.fragment_helper_chat_linearLayout);
        ask_linearLayout = (FrameLayout) view.findViewById(R.id.fragment_helper_ask_linearLayout);
        chinese_fixed = (RecyclerView) view.findViewById(R.id.fragment_helper_chinese_fixed_recycleview);
        western_fixed = (RecyclerView) view.findViewById(R.id.fragment_helper_western_fixed_recycleview);
        chinese_img = (ImageView) view.findViewById(R.id.fragment_helper_chinese_linearLayout_linearLayout_image);
        western_img = (ImageView) view.findViewById(R.id.fragment_helper_western_linearLayout_linearLayout_image);
        chahao = (ImageView) view.findViewById(R.id.fragment_helper_chinese_chahao);
        western_chahao = (ImageView) view.findViewById(R.id.fragment_helper_western_chahao);

        chinese_linearLayout_linearLayout = (LinearLayout) view.findViewById(R.id.fragment_helper_chinese_linearLayout_linearLayout);
        western_linearLayout_linearLayout = (LinearLayout) view.findViewById(R.id.fragment_helper_western_linearLayout_linearLayout);
        chinese_recyclerView = (RecyclerView) view.findViewById(R.id.fragment_helper_chinese_linearLayout_recycleView);
        western_recycleView = (RecyclerView) view.findViewById(R.id.fragment_helper_western_linearLayout_recycleView);
        chinese_button = (Button) view.findViewById(R.id.fragment_helper_chinese_button);
        western_button = (Button) view.findViewById(R.id.fragment_helper_western_button);
        chinese_edittext = (EditText) view.findViewById(R.id.fragment_helper_chinese_edittext);
        western_edittext = (EditText) view.findViewById(R.id.fragment_helper_chinese_edittext);


        ask = (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_ask);
        chat = (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_chat);
        chinese = (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_chinese);
        western = (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_western);

        western_linearLayout = (LinearLayout) view.findViewById(R.id.fragment_helper_western_medical_linearLayout);
        chinese_linearLayout = (LinearLayout) view.findViewById(R.id.fragment_helper_chinese_linearLayout);
        chat_linearLayout = (LinearLayout) view.findViewById(R.id.fragment_helper_chat_linearLayout);
        ask_linearLayout = (FrameLayout) view.findViewById(R.id.fragment_helper_ask_linearLayout);

        //中西药签名点击事件
//        western_linearLayout_linearLayout.setOnClickListener(signListenerWestern);
//        chinese_linearLayout_linearLayout.setOnClickListener(signListener);

        //搜索列表
        chinese_listView = (ListView) view.findViewById(R.id.fragment_helper_chinese_listview);
        western_listView = (ListView) view.findViewById(R.id.fragment_helper_western_listview);

        ctx = getContext();
        act = getActivity();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ask.setOnClickListener(this);
        chat.setOnClickListener(this);
        chinese.setOnClickListener(this);
        western.setOnClickListener(this);
        chinese_button.setOnClickListener(this);
        chahao.setOnClickListener(this);

        //显示药方的地方
        chinese_adapter = new Chinese_RecycleView_Adapter(getContext());
        data = new ArrayList<>();
        chinese_adapter.setList(data);
        chinese_adapter.setOnClick(this);
        chinese_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        chinese_recyclerView.setAdapter(chinese_adapter);

        western_adapter = new Western_RecycleView_Adapter(getContext());
        western_data = new ArrayList<>();
        western_adapter.setList(western_data);
        western_adapter.setOnClick(this);
        western_recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        western_recycleView.setAdapter(western_adapter);


        //固定药方
        fix_data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            initData();
        }

        fixed_adapter = new Chinese_Fixed_Adapter(getContext());
        chinese_fixed.setLayoutManager(new GridLayoutManager(getContext(), 3));
        fixed_adapter.setList(fix_data);
        fixed_adapter.setOnFixClick(this);
        chinese_fixed.setAdapter(fixed_adapter);

        //点击edittext实现自定义软键盘
        chinese_edittext.setInputType(InputType.TYPE_NULL);
        new KeyboardUtil(act, ctx, chinese_edittext).showKeyboard();


        //实现搜索功能
        setData();//给listview设置adapter
        setListener();//给listview设置监听

        EventBus.getDefault().register(this);
        //动态广播
        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter("shanchu");
        getActivity().registerReceiver(receiver, intentFilter);

        refresh = new Refresh();
        IntentFilter intentRefresh = new IntentFilter("SHUAXIN");
        getActivity().registerReceiver(refresh, intentRefresh);

        keyboarrReceiver = new KeyboarrReceiver();
        IntentFilter intentKey = new IntentFilter("RUANJIANPAN");
        getActivity().registerReceiver(keyboarrReceiver, intentKey);


    }

    private void setListener() {
        //没有进行搜索的时候，也要添加对listview的item单击监听
        setItemClick(list);

    }

    private void setData() {
        initData();//初始化搜索数据

        //这里面创建adapter的时候，构造方法参数传了一个接口对象，回调借口中的方法来实现对过滤的数据的获取
        adapter = new Chinese_ListView_Adapter(list, getContext(), new FilterListener() {
            @Override
            public void getFilterData(List<String> datas) {
                setItemClick(list);
            }
        });
        chinese_listView.setAdapter(adapter);
    }

    private void setItemClick(final List<ChineseDetailModel> datas) {
        chinese_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, filter_lists.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DialogActivity.class);
                intent.putExtra("medical_name", list.get(position).getCmc());
                startActivity(intent);

            }
        });
    }

    private void initData() {

        ChineseDetailModel a = new ChineseDetailModel();
        ChineseDetailModel b = new ChineseDetailModel();
        ChineseDetailModel c = new ChineseDetailModel();
        ChineseDetailModel d = new ChineseDetailModel();
        ChineseDetailModel e = new ChineseDetailModel();
        ChineseDetailModel f = new ChineseDetailModel();
        ChineseDetailModel g = new ChineseDetailModel();
        ChineseDetailModel h = new ChineseDetailModel();
        ChineseDetailModel i = new ChineseDetailModel();
        a.setCmc("车前子");
        b.setCmc("人参");
        c.setCmc("卜芥");
        d.setCmc("儿茶");
        e.setCmc("八角");
        f.setCmc("丁香");
        g.setCmc("刀豆");
        h.setCmc("三七");
        i.setCmc("三棱");

        fix_data.add(a);
        fix_data.add(b);
        fix_data.add(c);
        fix_data.add(d);
        fix_data.add(e);
        fix_data.add(f);
        fix_data.add(g);
        fix_data.add(h);
        fix_data.add(i);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_helper_radioButton_ask:
                playAskVeiw();
                chatFragment = new EaseChatFragment();
                bundle = new Bundle();
                bundle.putString("userName", userName);
                bundle.putString("userId", id);
                bundle.putString("type", type1);
                bundle.putInt("single", single1);
                chatFragment.setArguments(bundle);
                getChildFragmentManager().beginTransaction().add(R.id.fragment_helper_ask_linearLayout, chatFragment).commit();
                break;
            case R.id.fragment_helper_radioButton_chat:
                playChatView();
                break;
            case R.id.fragment_helper_radioButton_chinese:
                playChineseView();
                //查询本地数据库
                try {
                    data = HisDbManager.getManager().findChineseDeatilModel(id);

                } catch (DbException e) {
                    e.printStackTrace();
                }
                chinese_adapter.setList(data);
                chinese_adapter.notifyDataSetChanged();

                break;
            case R.id.fragment_helper_radioButton_western:
                playWesternView();
                break;
            case R.id.fragment_helper_chinese_button:
//               createYaoFang(id, "中药","1029405","7","1000");
                chinese_button.setOnClickListener(signListener);
                break;

            case R.id.fragment_helper_chinese_chahao:
                chinese_edittext.setText(null);
                chinese_listView.setVisibility(View.GONE);
                chinese_fixed.setVisibility(View.VISIBLE);


        }
    }


    private View.OnClickListener signListenerWestern = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WritePadDialog writeTabletDialog = new WritePadDialog(getActivity(),
                    getContext(), R.style.SignBoardDialog, new DialogListener() {
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = createFile();

                    //对图片进行压缩
                            /*BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 15;
							options.inTempStorage = new byte[5 * 1024];
							Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);
*/
                    Bitmap zoombm = getCompressBitmap(signPath);
                    western_img.setImageBitmap(zoombm);


                }
            });
            writeTabletDialog.show();
        }
    };

    private View.OnClickListener signListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WritePadDialog writeTabletDialog = new WritePadDialog(
                    getActivity(),getContext(), R.style.SignBoardDialog, new DialogListener() {
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = createFile();

                    //对图片进行压缩
                            /*BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 15;
							options.inTempStorage = new byte[5 * 1024];
							Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);
*/
                    Bitmap zoombm = getCompressBitmap(signPath);
                    chinese_img.setImageBitmap(zoombm);

                }
            });
            writeTabletDialog.show();
        }

    };

    /**
     * 创建手写签名文件
     *
     * @return
     */

    private String createFile() {
        ByteArrayOutputStream baos = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory()
                    + File.separator;
            _path = sign_dir + System.currentTimeMillis() + ".png";
            baos = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _path;
    }

    /**
     * 根据图片路径获取图片的压缩图
     *
     * @param filePath
     * @return
     */
    public Bitmap getCompressBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options); //此时返回bm为空
        if (bitmap == null) {
        }
        //计算缩放比
        int simpleSize = (int) (options.outHeight / (float) 200);
        if (simpleSize <= 0)
            simpleSize = 1;
        options.inSampleSize = simpleSize;
        options.inJustDecodeBounds = false;
        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap = BitmapFactory.decodeFile(filePath, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + "   " + h);
        return bitmap;
    }

    public void playAskVeiw() {
        ask_linearLayout.setVisibility(View.VISIBLE);
        chat_linearLayout.setVisibility(View.GONE);
        chinese_linearLayout.setVisibility(View.GONE);
        western_linearLayout.setVisibility(View.GONE);
    }

    public void playChatView() {
        chat_linearLayout.setVisibility(View.VISIBLE);
        ask_linearLayout.setVisibility(View.GONE);
        chinese_linearLayout.setVisibility(View.GONE);
        western_linearLayout.setVisibility(View.GONE);
    }

    public void playChineseView() {
        chinese_linearLayout.setVisibility(View.VISIBLE);
        ask_linearLayout.setVisibility(View.GONE);
        chat_linearLayout.setVisibility(View.GONE);
        western_linearLayout.setVisibility(View.GONE);
    }

    public void playWesternView() {
        western_linearLayout.setVisibility(View.VISIBLE);
        ask_linearLayout.setVisibility(View.GONE);
        chat_linearLayout.setVisibility(View.GONE);
        chinese_linearLayout.setVisibility(View.GONE);
    }


    public void getContent(String userId) {
            id=userId;
    }


    public void setRest() {
        llContent.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);
    }

    public void setContent(String userName, String userId, String type, int single) {
        llContent.setVisibility(View.VISIBLE);

        tvNoData.setVisibility(View.GONE);
        id = userId;
        this.userName = userName;
        type1 = type;
        single1 = single;

        chatFragment = new EaseChatFragment();
        bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putString("userId", userId);
        bundle.putString("type", type);
        bundle.putInt("single", single);
        chatFragment.setArguments(bundle);


        getChildFragmentManager().beginTransaction().add(R.id.fragment_helper_ask_linearLayout, chatFragment).commit();

    }


    public void createYaoFang(String userName, String yaofangType, String yaofangNum, String yaoNum, String yaofangPrice) {
        EMMessage message = EMMessage.createTxtSendMessage("yaofang", id);

        message.setAttribute("type", "yaofang");
        message.setAttribute("userName", userName);
        message.setAttribute("yaofangType", yaofangType);
        message.setAttribute("yaofangNum", yaofangNum);
        message.setAttribute("yaoNum", yaoNum);
        message.setAttribute("yaofangPrice", yaofangPrice);

        EMClient.getInstance().chatManager().sendMessage(message);

    }

    @Override
    public void onIteClick(int position) {
        Intent intent = new Intent(getActivity(), SecondDialogActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("chinese_name", data.get(position).getCmc());
        startActivity(intent);
        chinese_adapter.deleteTextView(position);
//        western_adapter.deleteTextView(position);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getData(ChineseDetailModel numberBean) {
        String chinese_name = numberBean.getCmc();
        int count = numberBean.getSl();
        Log.e(TAG, "getData: " + count);
        ChineseDetailModel a = new ChineseDetailModel();

        a.setSl(count);
        a.setCmc(chinese_name);
        chinese_adapter.addTextView(a);
        chinese_adapter.notifyDataSetChanged();

        chinese_listView.setVisibility(View.GONE);
        chinese_fixed.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        getActivity().unregisterReceiver(receiver);
        getActivity().unregisterReceiver(refresh);
    }

    @Override
    public void OnFixItemClick(int position) {
        Intent intent = new Intent(getActivity(), DialogActivity.class);
        intent.putExtra("medical_name", fix_data.get(position).getCmc());
        startActivity(intent);
    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("pos", 0);
            String name = intent.getStringExtra("name");
            chinese_adapter.notifyDataSetChanged();
            try {
                HisDbManager.getManager().deleteAskChinese(name);
//                HisDbManager.getManager().deleteAskNumbwe(0);
            } catch (DbException e) {
                e.printStackTrace();
            }

        }
    }

    class Refresh extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            playAskVeiw();
            ask.setChecked(true);
        }
    }

    class KeyboarrReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //根据拼音查询数据
            pinyin = chinese_edittext.getText().toString();
            String xmmc = null;
            List<String> asd = new ArrayList<>();
            Cursor cursor = DataHelper.getInstance(getContext()).getXMRJ(pinyin);
            if (pinyin.length() >= 2) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        xmmc = cursor.getString(cursor.getColumnIndex("xmmc"));
                        asd.add(xmmc);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                //显示搜索列表药名
                for (String s : asd) {
                    ChineseDetailModel chinese = new ChineseDetailModel();
                    chinese.setCmc(s);
                    list.add(chinese);
                }
                adapter.notifyDataSetChanged();
                chinese_listView.setVisibility(View.VISIBLE);
                chinese_fixed.setVisibility(View.GONE);
            }
        }
    }


}
