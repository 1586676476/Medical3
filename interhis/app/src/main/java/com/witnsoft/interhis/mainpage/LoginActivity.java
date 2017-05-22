package com.witnsoft.interhis.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jakewharton.rxbinding.view.RxView;
import com.witnsoft.interhis.R;
import com.witnsoft.libinterhis.base.BaseActivity;
import com.witnsoft.libinterhis.utils.ClearEditText;
import com.witnsoft.libinterhis.utils.ThriftPreUtils;
import com.witnsoft.libnet.model.DataModel;
import com.witnsoft.libnet.model.OTRequest;
import com.witnsoft.libnet.net.CallBack;
import com.witnsoft.libnet.net.NetTool;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by liyan on 2017/5/15.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private static final long THROTTLE_TIME = 500;

    @ViewInject(R.id.btn_login)
    private Button btnLogin;
    @ViewInject(R.id.et_userName)
    private ClearEditText etUserName;
    @ViewInject(R.id.et_user_password)
    private ClearEditText etUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        // 登录
        initClick(this.btnLogin, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                login();
            }
        });
    }

    private void login() {
        if (TextUtils.isEmpty(etUserName.getText().toString())) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_user_name_tip),
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(etUserPassword.getText().toString())) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_user_password_tip),
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            callLoginApi(etUserName.getText().toString(), etUserPassword.getText().toString());
        }
    }

    // F27.APP.01.00 医生登录
    private void callLoginApi(String name, String password) {
        OTRequest otRequest = new OTRequest();
        DataModel data = new DataModel();
        data.setParam("name", name);
        data.setParam("password", password);
        otRequest.setDATA(data);
        otRequest.setTN("F27.APP.01.01");

        NetTool.getInstance().startRequest(LoginActivity.this, otRequest, new CallBack<String>() {
            @Override
            public void onSuccess(String response) {
                //返回response即为DATAARRAY的json字符串，进一步根据需求自行解析
                String token = "";
                ThriftPreUtils.putToken(LoginActivity.this, token);
                // app登录返回成功后调用环信登录
                chatLogin();
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void chatLogin() {
        EMClient.getInstance().login("ceshi", "111111", new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("onSuccess: ", "登录成功");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(int i, String s) {
                Log.e("onError: ", i + " " + s + "登录失败");

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    private void initClick(View view, Action1<Void> action1) {
        RxView.clicks(view)
                .throttleFirst(THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(action1);
    }
}
