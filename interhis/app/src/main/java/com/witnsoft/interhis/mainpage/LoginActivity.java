package com.witnsoft.interhis.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.witnsoft.interhis.R;
import com.witnsoft.libinterhis.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by liyan on 2017/5/15.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login(){
        EMClient.getInstance().login("ceshi", "111111", new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e( "onSuccess: ","success!" );
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }

            @Override
            public void onError(int i, String s) {
                Log.e( "onError: ",i + " " + s + "登录失败");

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }
}
