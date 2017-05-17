package com.witnsoft.interhis.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.adapter.Chinese_ListView_Adapter;
import com.witnsoft.interhis.adapter.Chinese_RecycleView_Adapter;
import com.witnsoft.interhis.inter.DialogListener;

import com.witnsoft.interhis.inter.FilterListener;
import com.witnsoft.interhis.inter.WritePadDialog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${liyan} on 2017/5/8.
 */

public class HelperFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "HelperFragment";

    private Bitmap mSignBitmap;
    private String signPath;
    private RadioButton ask,chat,chinese,western;
    private FrameLayout ask_linearLayout;
    private LinearLayout chinese_linearLayout,western_linearLayout,chat_linearLayout;
    private LinearLayout chinese_linearLayout_linearLayout,western_linearLayout_linearLayout;
    private ImageView chinese_img,western_img;

    private RecyclerView chinese_recyclerView;
    private Chinese_RecycleView_Adapter chinese_adapter;
    private List<String> data;

    private Button chinese_button,chinese_confirm;
    private TextView chinese_recycleview_text;
    private EditText chinese_edittext;

    private ListView chinese_listView;
    private List<String> list=new ArrayList<String>();
    boolean isFilter;
    private Chinese_ListView_Adapter adapter=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_helper,container,false);

        ask= (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_ask);
        chat= (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_chat);
        chinese= (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_chinese);
        western= (RadioButton) view.findViewById(R.id.fragment_helper_radioButton_western);

        western_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_western_medical_linearLayout);
        chinese_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_chinese_linearLayout);
        chat_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_chat_linearLayout);
        ask_linearLayout= (FrameLayout) view.findViewById(R.id.fragment_helper_ask_linearLayout);
        chinese_img= (ImageView) view.findViewById(R.id.fragment_helper_chinese_linearLayout_linearLayout_image);
        western_img= (ImageView) view.findViewById(R.id.fragment_helper_western_medical_linearLayout_linearLayout_image);

        chinese_linearLayout_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_chinese_linearLayout_linearLayout);
        western_linearLayout_linearLayout= (LinearLayout) view.findViewById(R.id.fragment_helper_western_medical_linearLayout_linearLayout);
        chinese_recyclerView= (RecyclerView) view.findViewById(R.id.fragment_helper_chinese_linearLayout_recycleView);
        chinese_button= (Button) view.findViewById(R.id.fragment_helper_chinese_button);
        chinese_confirm= (Button) view.findViewById(R.id.fragment_helper_chinese_confirm);
        chinese_recycleview_text= (TextView) view.findViewById(R.id.fragment_helper_chinese_linearLayout_text);
        chinese_edittext= (EditText) view.findViewById(R.id.fragment_helper_chinese_edittext);

        chinese_linearLayout_linearLayout.setOnClickListener(signListener);
        chinese_img.setOnClickListener(signListener);
        western_linearLayout_linearLayout.setOnClickListener(signListenerWestern);
        western_img.setOnClickListener(signListenerWestern);

        chinese_listView= (ListView) view.findViewById(R.id.fragment_helper_chinese_listview);

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
        chinese_confirm.setOnClickListener(this);

        chinese_adapter=new Chinese_RecycleView_Adapter(getContext());
        data=new ArrayList<>();
        chinese_recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        chinese_adapter.setList(data);

        chinese_adapter.setContext(getContext());
        chinese_recyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));
        chinese_recyclerView.setAdapter(chinese_adapter);

        //实现搜索功能
        setData();//给listview设置adapter
        setListener();//给listview设置监听

    }

    private void setListener() {
        //没有进行搜索的时候，也要添加对listview的item单击监听
        setItemClick(list);

        /**
         * 对编辑框添加文本改变监听，搜索的具体功能在这里实现
         * 文本改变的时候进行搜索，重写onTextChanges()方法
         */
        chinese_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //编辑框内容改变的时候会执行该方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果adapter不为空的话就根据编辑框中的内容来过滤数据
                if (adapter!=null){
                    adapter.getFilter().filter(s);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void setData() {
        initData();//初始化搜索数据

        //这里面创建adapter的时候，构造方法参数传了一个接口对象，回调借口中的方法来实现对过滤的数据的获取
        adapter=new Chinese_ListView_Adapter(list, getContext(), new FilterListener() {
            @Override
            public void getFilterData(List<String> datas) {
                setItemClick(list);
            }
        });
        chinese_listView.setAdapter(adapter);

    }

    private void setItemClick(final List<String> datas) {
        chinese_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, filter_lists.get(position), Toast.LENGTH_SHORT).show();
                chinese_edittext.setText(datas.get(position));
            }
        });
    }

    private void initData() {
        list.add("看着飞舞的尘埃   掉下来");
        list.add("没人发现它存在");
        list.add("多自由自在");
        list.add("可世界都爱热热闹闹");
        list.add("容不下   我百无聊赖");
        list.add("不应该   一个人 发呆");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fragment_helper_radioButton_ask:
               playAskVeiw();
                break;
            case R.id.fragment_helper_radioButton_chat:
                playChatView();
                break;
            case R.id.fragment_helper_radioButton_chinese:
                playChineseView();
                chinese_edittext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chinese_listView.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R.id.fragment_helper_radioButton_western:
                playWesternView();
                break;
            case R.id.fragment_helper_chinese_confirm:
                String content=chinese_edittext.getText().toString();
                if (!TextUtils.isEmpty(content)){
                    chinese_adapter.addTextView(content);
                    chinese_adapter.notifyDataSetChanged();
                    chinese_edittext.setText("");
                }
                break;
            case R.id.fragment_helper_chinese_button:

            break;
        }
    }

    private View.OnClickListener signListenerWestern = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WritePadDialog writeTabletDialog = new WritePadDialog(
                    getContext(),R.style.SignBoardDialog, new DialogListener() {
                public void refreshActivity(Object object) {
                    mSignBitmap = (Bitmap) object;
                    signPath = createFile();
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
                    getContext(),R.style.SignBoardDialog, new DialogListener() {
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
     * @param filePath
     * @return
     */
    public Bitmap getCompressBitmap(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options); //此时返回bm为空
        if(bitmap == null){
        }
        //计算缩放比
        int simpleSize = (int)(options.outHeight / (float)200);
        if (simpleSize <= 0)
            simpleSize = 1;
        options.inSampleSize = simpleSize;
        options.inJustDecodeBounds = false;
        //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
        bitmap=BitmapFactory.decodeFile(filePath,options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w+"   "+h);
        return bitmap;
    }

    public void playAskVeiw(){
        ask_linearLayout.setVisibility(View.VISIBLE);
        chat_linearLayout.setVisibility(View.GONE);
        chinese_linearLayout .setVisibility(View.GONE);
        western_linearLayout.setVisibility(View.GONE);
    }

    public void playChatView(){
        chat_linearLayout.setVisibility(View.VISIBLE);
        ask_linearLayout.setVisibility(View.GONE);
        chinese_linearLayout.setVisibility(View.GONE);
        western_linearLayout.setVisibility(View.GONE);
    }

    public void playChineseView(){
        chinese_linearLayout.setVisibility(View.VISIBLE);
        ask_linearLayout.setVisibility(View.GONE);
        chat_linearLayout.setVisibility(View.GONE);
        western_linearLayout.setVisibility(View.GONE);
    }

    public void playWesternView(){
        western_linearLayout.setVisibility(View.VISIBLE);
        ask_linearLayout.setVisibility(View.GONE);
        chat_linearLayout.setVisibility(View.GONE);
        chinese_linearLayout.setVisibility(View.GONE);
    }

    public void getContent(String userName, String userId, String type, int single){
        Log.e(TAG, "getContent: "+userName+userId+type+single );
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userName",userName);
        bundle.putString("userId",userId);
        bundle.putString("type",type);
        bundle.putInt("single",single);
        chatFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().add(R.id.fragment_helper_ask_linearLayout, chatFragment).commit();

    }




}
