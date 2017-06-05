package com.witnsoft.interhis.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.witnsoft.interhis.R;
import com.witnsoft.interhis.bean.NumberBean;
import com.witnsoft.interhis.db.HisDbManager;
import com.witnsoft.libinterhis.base.BaseActivity;
import com.witnsoft.libinterhis.db.model.ChineseDetailModel;

import org.greenrobot.eventbus.EventBus;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by ${liyan} on 2017/5/26.
 */
@ContentView(R.layout.activity_number_second_dialog)
public class SecondDialogActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "DialogActivity";
    private LinearLayout ll_root,ll_dialog;
    private Button cancel,delete;
    private TextView five,ten,fifteen,twenty,number,add,less,name,show;
    private LinearLayout linearLayout;
    private String medical;
    private int position;
    private int num=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        initView();
    }

    private void initView() {
        ll_root = (LinearLayout) findViewById(R.id.second_ll_root);
        ll_dialog = (LinearLayout) findViewById(R.id.second_ll_dialog);
        five = (TextView) findViewById(R.id.second_dialog_five);
        ten = (TextView) findViewById(R.id.second_dialog_ten);
        fifteen = (TextView) findViewById(R.id.second_dialog_fifteen);
        twenty = (TextView) findViewById(R.id.second_dialog_twenty);
        number = (TextView) findViewById(R.id.second_dialog_number);
        add = (TextView) findViewById(R.id.second_dialog_add);
        less = (TextView) findViewById(R.id.second_dialog_less);
        cancel = (Button) findViewById(R.id.second_bt_cancel);
        delete = (Button) findViewById(R.id.second_bt_delete);
        name = (TextView) findViewById(R.id.second_type);
        linearLayout = (LinearLayout) findViewById(R.id.dialog_linearLayout);


        ll_root.setOnClickListener(this);
        ll_dialog.setOnClickListener(this);
        five.setOnClickListener(this);
        ten.setOnClickListener(this);
        fifteen.setOnClickListener(this);
        twenty.setOnClickListener(this);
        number.setOnClickListener(this);
        add.setOnClickListener(this);
        less.setOnClickListener(this);
        cancel.setOnClickListener(this);
        delete.setOnClickListener(this);
        less.setTag("+");
        add.setTag("-");
        //设置输入类型为数字
        number.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        number.setText(String.valueOf(num));
        SetViewListener();

        //接受传递过来的药名
        medical = getIntent().getStringExtra("medical_name");
        name.setText(medical);


    }

    private void SetViewListener() {
        add.setOnClickListener(new OnButtonClickListener());
        less.setOnClickListener(new OnButtonClickListener());
        number.addTextChangedListener(new OnTextChangeListener());
    }

    @Override
    public void onClick(View v) {
        NumberBean numberBean=new NumberBean();
        position=getIntent().getIntExtra("position",0);
        switch (v.getId()){
            case R.id.second_ll_root:
                finish();
                break;
            case R.id.second_dialog_five:
                numberBean.setCount(5);
                EventBus.getDefault().post(numberBean);
                finish();
                break;
            case R.id.second_dialog_ten:
                numberBean.setCount(10);
                EventBus.getDefault().post(numberBean);
                finish();
                break;
            case R.id.second_dialog_fifteen:
                numberBean.setCount(15);
                EventBus.getDefault().post(numberBean);
                finish();
                break;
            case R.id.second_dialog_twenty:
                numberBean.setCount(20);
                EventBus.getDefault().post(numberBean);
                finish();
                break;
            case R.id.second_dialog_number:
                numberBean.setCount(num);
                EventBus.getDefault().post(numberBean);
                finish();
                break;
            case R.id.second_bt_cancel:
                finish();
                break;
            case R.id.second_bt_delete:
                Intent intent=new Intent("shanchu");
                intent.putExtra("pos",position);
                sendBroadcast(intent);
                finish();
                break;

        }

    }


    /**
     * 加减按钮事件监听器
     *
     *
     */
    class OnButtonClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            String numString = number.getText().toString();
            if (numString == null || numString.equals(""))
            {
                num = 0;
                number.setText("0");
            } else
            {
                if (v.getTag().equals("-"))
                {
                    if (++num < 0)  //先加，再判断
                    {
                        num--;
                        Toast.makeText(SecondDialogActivity.this, "请输入一个大于0的数字",
                                Toast.LENGTH_SHORT).show();
                    } else
                    {
                        number.setText(String.valueOf(num));
//                        show.setText(number.getText()+"g");
                    }
                } else if (v.getTag().equals("+"))
                {
                    if (--num < 0)  //先减，再判断
                    {
                        num++;
                        Toast.makeText(SecondDialogActivity.this, "请输入一个大于0的数字",
                                Toast.LENGTH_SHORT).show();
                    } else
                    {
                        number.setText(String.valueOf(num));
//                        show.setText(number.getText()+"g");
                    }
                }
            }
        }
    }

    /**
     * EditText输入变化事件监听器
     */
    class OnTextChangeListener implements TextWatcher
    {

        @Override
        public void afterTextChanged(Editable s)
        {
            String numString = s.toString();
            if(numString == null || numString.equals(""))
            {
                num = 0;
                number.setText(0);
//                show.setText(number.getText());
            }
            else {
                int numInt = Integer.parseInt(numString);
                if (numInt < 0)
                {
                    Toast.makeText(SecondDialogActivity.this, "请输入一个大于0的数字",
                            Toast.LENGTH_SHORT).show();
                }else {
//                    show.setText(number.getText()+"g");
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count)
        {

        }

    }
}
