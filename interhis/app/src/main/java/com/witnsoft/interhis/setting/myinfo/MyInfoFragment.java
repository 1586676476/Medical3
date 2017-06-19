package com.witnsoft.interhis.setting.myinfo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.PathUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.mainpage.LoginActivity;
import com.witnsoft.interhis.setting.ChildBaseFragment;
import com.witnsoft.interhis.setting.SettingActivity;
import com.witnsoft.interhis.utils.ui.ItemSettingRight;
import com.witnsoft.libinterhis.utils.ThriftPreUtils;
import com.witnsoft.libnet.model.LoginRequest;
import com.witnsoft.libnet.net.CallBack;
import com.witnsoft.libnet.net.NetTool;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private static final String PKG = "com.witnsoft.interhis";

    public final class ActivityRequestCode {
        //启动相机
        public static final int REQUEST_CODE_CAMERA = 1;
        //启动相册
        public static final int START_ALBUM_REQUESTCODE = 2;
    }

    public final class PermissionRequestCode {
        // 相机权限返回
        public static final int REQUEST_CAMERA_PERMISSION = 100;
        // 相册权限返回
        public static final int REQUEST_ALBUM_PERMISSION = 200;
    }

    View rootView;
    private String strImgPath;

    @ViewInject(R.id.tv_name)
    private TextView tvName;
    @ViewInject(R.id.tv_level)
    private TextView tvLevel;
    @ViewInject(R.id.tv_hosp)
    private TextView tvHosp;
    @ViewInject(R.id.iv_head)
    private CircleImageView ivHead;
    @ViewInject(R.id.tv_dept)
    private TextView tvDept;
    // 个人简介
    @ViewInject(R.id.view_introduction)
    private ItemSettingRight viewIntroduction;
    // 我的擅长
    @ViewInject(R.id.view_my_expert)
    private ItemSettingRight viewMyExpert;
    @ViewInject(R.id.view_evaluate)
    // 患者评价
    private ItemSettingRight viewEvaluate;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ActivityRequestCode.REQUEST_CODE_CAMERA) {
                // 相机返回
                String path = Environment.getExternalStorageDirectory().toString()
                        + TMP_PATH + pathFileName;
                Log.e(TAG, "path = " + path);
                load(path, this.ivHead, R.drawable.touxiang);
            } else if (requestCode == ActivityRequestCode.START_ALBUM_REQUESTCODE) {
                // 相册返回
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        load(getFilePath(selectedImage), this.ivHead, R.drawable.touxiang);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionRequestCode.REQUEST_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                startCamera();
            } catch (Exception e) {
                Toast.makeText(getActivity(), getResources().getString(R.string.none_permission), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PermissionRequestCode.REQUEST_ALBUM_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startAlbum();
        }
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
        // 我的擅长
        RxView.clicks(viewMyExpert)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toMyExpert();
                    }
                });
        // 患者评价
        RxView.clicks(viewEvaluate)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toEvaluate();
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
        viewIntroduction.setTvTitle(getResources().getString(R.string.personal_introduction), false);
        viewMyExpert.setTvTitle(getResources().getString(R.string.personal_my_expert));
        viewEvaluate.setTvTitle(getResources().getString(R.string.evaluate));
        Bundle bundle = getArguments();
        if (null != bundle) {
            if (!TextUtils.isEmpty(bundle.getString(SettingActivity.DOC_NAME))) {
                tvName.setText(bundle.getString(SettingActivity.DOC_NAME));
            }
            if (!TextUtils.isEmpty(bundle.getString(SettingActivity.DOC_LEVEL))) {
                tvLevel.setText(bundle.getString(SettingActivity.DOC_LEVEL));
            }
            if (!TextUtils.isEmpty(bundle.getString(SettingActivity.DOC_HOSP))) {
                tvHosp.setText(bundle.getString(SettingActivity.DOC_HOSP));
            }
            if (!TextUtils.isEmpty(bundle.getString(SettingActivity.DOC_DEPT))) {
                tvDept.setText(bundle.getString(SettingActivity.DOC_DEPT));
            }
        }
    }

    private void toIntroduction() {
        IntroductionFragment introductionFragment = new IntroductionFragment();
        pushFragment(introductionFragment, null, true);
    }

    private void toMyExpert() {
        MyExpertFragment myExpertFragment = new MyExpertFragment();
        pushFragment(myExpertFragment, null, true);
    }

    private void toEvaluate() {
        EvaluateFragment evaluateFragment = new EvaluateFragment();
        pushFragment(evaluateFragment, null, true);
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
    private PackageManager pm;

    private void showHeadDialog() {
        pm = getActivity().getPackageManager();
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
                // 获取相机读写权限
                // 如果没有权限，强开
                boolean cameraPermission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission(Manifest.permission.CAMERA, PKG));
                boolean readPermission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PKG));
                boolean writePermission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PKG));
                if (Build.VERSION.SDK_INT >= 23) {
                    if (cameraPermission && readPermission && writePermission) {
                        startCamera();
                    } else {
                        String[] PERMISSIONS_CAMERA = {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        };
                        requestPermissions(PERMISSIONS_CAMERA, PermissionRequestCode.REQUEST_CAMERA_PERMISSION);
                    }
                } else {
                    startCamera();
                }
                dialog.dismiss();
            }
        });
        // 从相册选择
        tvFromPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取读权限
                // 如果没有权限，强开
                boolean readPermission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PKG));
                if (Build.VERSION.SDK_INT >= 23) {
                    if (readPermission) {
                        startAlbum();
                    } else {
                        String[] PERMISSIONS_STORAGE = {
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        };
                        requestPermissions(PERMISSIONS_STORAGE, PermissionRequestCode.REQUEST_ALBUM_PERMISSION);
                    }
                } else {
                    startAlbum();
                }
                dialog.dismiss();
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
     * 相机拍照
     */
    public static final String TMP_PATH = "/DCIM/Camera/";
    protected static String pathFileName = "";
    private static Uri uri;

    private void startCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(), com.hyphenate.easeui.R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        // 照片保存路径
        strImgPath = Environment.getExternalStorageDirectory().toString() + TMP_PATH;
        // 照片以格式化日期方式命名
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + ".png";
        pathFileName = fileName;
        File outPutImage = new File(strImgPath);
        if (!outPutImage.exists()) {
            outPutImage.mkdirs();
        }
        outPutImage = new File(strImgPath, fileName);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                uri = FileProvider.getUriForFile(getActivity(), "com.witnsoft.interhis.fileprovider", outPutImage);
            } catch (Exception e) {
            }

        } else {
            uri = Uri.fromFile(outPutImage);
        }
        if (uri == null) {
            Toast.makeText(getActivity(), R.string.card_fali_msg, Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, ActivityRequestCode.REQUEST_CODE_CAMERA);
        }
    }

    /**
     * 从相册选择
     */
    private void startAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, ActivityRequestCode.START_ALBUM_REQUESTCODE);
    }

    public String getFilePath(Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return mUri.getPath();
            } else {
                return getFilePathByUri(mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    // 获取文件路径通过url
    private String getFilePathByUri(Uri mUri) throws FileNotFoundException {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        String url = "";
        try {
            cursor = getActivity().getContentResolver()
                    .query(mUri, proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            // 最后根据索引值获取图片路径
            url = cursor.getString(column_index);
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return url;
    }
}
