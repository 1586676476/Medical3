package com.witnsoft.interhis.setting.myinfo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.jakewharton.rxbinding.view.RxView;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.db.HisDbManager;
import com.witnsoft.interhis.db.model.ChineseDetailModel;
import com.witnsoft.interhis.fragment.HelperFragment;
import com.witnsoft.interhis.mainpage.LoginActivity;
import com.witnsoft.interhis.setting.ChildBaseFragment;
import com.witnsoft.interhis.utils.PermissionUtil;
import com.witnsoft.interhis.utils.ui.ItemSettingRight;
import com.witnsoft.libnet.model.LoginRequest;
import com.witnsoft.libnet.net.CallBack;
import com.witnsoft.libnet.net.NetTool;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by zhengchengpeng on 2017/6/13.
 */

@ContentView(R.layout.fragment_my_info)
public class MyInfoFragment extends ChildBaseFragment {
    private static final String TAG = "MyInfoFragment";

    private static final String LOGOUT = "logout";
    private static final String ERRO_MSG = "errmsg";

    private final int REQUEST_CODE_CAMERA = 1;
    View rootView;

    @ViewInject(R.id.tv_name)
    private TextView tvName;
    @ViewInject(R.id.tv_level)
    private TextView tvLevel;
    @ViewInject(R.id.tv_hosp)
    private TextView tvHosp;
    @ViewInject(R.id.iv_head)
    private CircleImageView ivHead;
    // 个人简介
    @ViewInject(R.id.view_introduction)
    private ItemSettingRight viewIntroduction;
    // 注销登录
    @ViewInject(R.id.rl_logout)
    private RelativeLayout rlLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = x.view().inject(this, inflater, container);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initClick();
        init();
    }

    private void initClick() {
        // 个人简介
        RxView.clicks(viewIntroduction)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toIntroduction();
                    }
                });
        // 退出登录
        RxView.clicks(rlLogout)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        chatLogout();
                    }
                });
        // 修改头像
        RxView.clicks(ivHead)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showHeadDialog();
                    }
                });
    }

    private void init() {
        viewIntroduction.setTvTitle(getResources().getString(R.string.personal_introduction));
        tvName.setText("医生名字");
        tvLevel.setText("主任医师");
        tvHosp.setText("天津市第一人民医院");
    }

    private void toIntroduction() {
        IntroductionFragment introductionFragment = new IntroductionFragment();
        pushFragment(introductionFragment, null, true);
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
                Toast.makeText(getActivity(), getResources().getString(R.string.logout_failed), Toast.LENGTH_LONG).show();
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
                        callLogoutApi();
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
     * 修改头像
     */
    private AlertDialog dialog;
    private TextView tvTakePhoto;
    private TextView tvFromPhoto;
    private TextView tvCancel;

    private void showHeadDialog() {
        final LayoutInflater linearLayout = getActivity().getLayoutInflater();
        View view = linearLayout.inflate(R.layout.dialog_select_head, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.create();
        dialog = builder.show();
        android.view.Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.round);
        dialog.setCanceledOnTouchOutside(false);
        tvTakePhoto = (TextView) view.findViewById(R.id.tv_take_photo);
        tvFromPhoto = (TextView) view.findViewById(R.id.tv_from_photo);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        // 拍照
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                dialog.dismiss();
            }
        });
        // 从相册选择
        tvFromPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fromPhoto();
            }
        });
        // 取消
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS_CAMERA = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };
            PermissionUtil.requestPermission(getActivity(), PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA);
        } else {
            startCapture();
        }
    }

    /**
     * 从相册选择
     */
    private void fromPhoto() {

    }

    private void startCapture() {

    }
}
