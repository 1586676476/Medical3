package com.witnsoft.interhis.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jakewharton.rxbinding.view.RxView;
import com.witnsoft.interhis.R;
import com.witnsoft.libinterhis.base.BaseActivity;
import com.witnsoft.libinterhis.utils.ClearEditText;
import com.witnsoft.libinterhis.utils.ThriftPreUtils;
import com.witnsoft.libnet.model.LoginRequest;
import com.witnsoft.libnet.net.CallBack;
import com.witnsoft.libnet.net.NetTool;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by liyan on 2017/5/15.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private static final long THROTTLE_TIME = 500;
    private static final String LOGIN = "login";
    private static final String ERR_MSG = "errmsg";

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
        init();
        // 登录
        initClick(this.btnLogin, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                login();
            }
        });
    }

    private void init() {
        // 显示缓存用户名
        if (!TextUtils.isEmpty(ThriftPreUtils.getLoginName(LoginActivity.this))) {
            etUserName.setText(ThriftPreUtils.getLoginName(LoginActivity.this));
        }
    }

    private void login() {
        if (TextUtils.isEmpty(etUserName.getText().toString())) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_user_name_tip),
                    Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(etUserPassword.getText().toString())) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.no_user_password_tip),
                    Toast.LENGTH_LONG).show();
        } else {
            callLoginApi(etUserName.getText().toString(), etUserPassword.getText().toString());
        }
    }

    // 医生登录
    private void callLoginApi(final String name, String password) {
        LoginRequest request = new LoginRequest();
        request.setUsername(name);
        request.setPassword(password);
        request.setReqType(LOGIN);
        NetTool.getInstance().startRequest(true, LoginActivity.this, request, null, new CallBack<Map, String>() {
            @Override
            public void onSuccess(Map response, String resultCode) {
                // 登录成功将用户名存本地
                ThriftPreUtils.putLoginName(LoginActivity.this, name);
                if ("200".equals(resultCode)) {
                    chatLogin();
                } else {
                    if (null != response.get(ERR_MSG)) {
                        try {
                            Toast.makeText(LoginActivity.this,
                                    String.valueOf(response.get(ERR_MSG)), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                }
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
                // 获取所有会话列表
                EMClient.getInstance().chatManager().loadAllConversations();
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

    // 医生登出
//    private void callLogoutApi() {
//        LoginRequest request = new LoginRequest();
//        request.setReqType("logout");
//        NetTool.getInstance().startRequest(true, LoginActivity.this, request, null, new CallBack<String>() {
//            @Override
//            public void onSuccess(String response, String resultCode) {
//                Gson gson = new Gson();
//                Map<String, Map<String, Object>> mapObj = new HashMap<String, Map<String, Object>>();
//                final Map<String, Map<String, Object>> map = gson.fromJson(response, mapObj.getClass());
//                if ("200".equals(resultCode)) {
//                    // 登出成功
//                    Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    if (null != map.get("errmsg")) {
//                        try {
//                            Toast.makeText(LoginActivity.this,
//                                    String.valueOf(map.get("errmsg")), Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//
//                        }
//                    }
//                }
//            }
//
//
//
//
//            @Override
//            public void onError(Throwable throwable) {
//                Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}
