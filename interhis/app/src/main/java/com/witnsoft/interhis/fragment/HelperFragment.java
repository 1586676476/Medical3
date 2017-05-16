package com.witnsoft.interhis.fragment;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import com.witnsoft.interhis.R;
import com.witnsoft.interhis.adapter.Chinese_RecycleView_Adapter;
import com.witnsoft.interhis.bean.Prescription;

import com.witnsoft.interhis.inter.DetailsInter;
import com.witnsoft.interhis.inter.DialogListener;

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
    private String signPath,prescription;
    private RadioButton ask,chat,chinese,western;



    private FrameLayout ask_linearLayout;
    private LinearLayout chinese_linearLayout,western_linearLayout,chat_linearLayout;
    private LinearLayout chinese_linearLayout_linearLayout,western_linearLayout_linearLayout;
    private ImageView chinese_img,western_img;

    private RecyclerView chinese_recyclerView;
    private Chinese_RecycleView_Adapter chinese_adapter;
    private List<Prescription> data;

    private Button chinese_button;
    private TextView chinese_recycleview_text;
    private EditText chinese_edittext;
    private DetailsInter detailsInter;
    private int id;

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
        chinese_recycleview_text= (TextView) view.findViewById(R.id.fragment_helper_chinese_linearLayout_text);
        chinese_edittext= (EditText) view.findViewById(R.id.fragment_helper_chinese_edittext);

        chinese_linearLayout_linearLayout.setOnClickListener(signListener);
        chinese_img.setOnClickListener(signListener);
        western_linearLayout_linearLayout.setOnClickListener(signListenerWestern);
        western_img.setOnClickListener(signListenerWestern);

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

        chinese_adapter=new Chinese_RecycleView_Adapter(getContext());
        data=new ArrayList<>();
        chinese_recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        chinese_adapter.setList(data);

        chinese_adapter.setContext(getContext());
        chinese_recyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));

        chinese_recyclerView.setAdapter(chinese_adapter);

        detailsInter=getActivity().getIntent().getParcelableExtra("details");
        prescription=detailsInter.getDetails();
        id=detailsInter.getId();
        chinese_edittext.setHint(prescription);


    }

    @Override
    public void onClick(View v) {
        String content=chinese_edittext.getText().toString();
        switch (v.getId()){
            case R.id.fragment_helper_radioButton_ask:
               playAskVeiw();
                break;
            case R.id.fragment_helper_radioButton_chat:
                playChatView();
                break;
            case R.id.fragment_helper_radioButton_chinese:
                playChineseView();
                break;
            case R.id.fragment_helper_radioButton_western:
                playWesternView();
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
