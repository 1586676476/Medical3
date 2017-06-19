package com.witnsoft.interhis.mainpage;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.witnsoft.interhis.R;
import com.witnsoft.interhis.db.DataHelper;
import com.witnsoft.interhis.tool.Application;
import com.witnsoft.interhis.utils.AppUtils;
import com.witnsoft.interhis.utils.ConnectionUtils;
import com.witnsoft.interhis.utils.PermissionUtil;
import com.witnsoft.libinterhis.base.BaseActivity;
import com.witnsoft.libnet.model.DataModel;
import com.witnsoft.libnet.model.OTRequest;
import com.witnsoft.libnet.net.CallBack;
import com.witnsoft.libnet.net.NetTool;
import com.witnsoft.libnet.net.OKTool;
import com.witnsoft.libnet.token.TokenSharepref;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zhengchengpeng on 2017/5/12.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    private final class ErrCode {
        private static final String ErrCode_200 = "200";
        private static final String ErrCode_504 = "504";
    }

    private static final String TN_UPDATE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mProgressDialog = getProgressDialog();
        callUpdate();

    }

    private void callUpdate() {
        Log.d(TAG, "update running");
        final String url = "https://zy.renyibao.com/ver.json";
        okHttpClient = (new OkHttpClient.Builder()).retryOnConnectionFailure(true).connectTimeout(5L, TimeUnit.SECONDS)
                .cache(new Cache(Environment.getExternalStorageDirectory(), 10485760L)).build();
        Request MEDIA_TYPE_MARKDOWN = (new okhttp3.Request.Builder()).url(url).build();
        okHttpClient.newCall(MEDIA_TYPE_MARKDOWN).enqueue(new Callback() {
            public void onFailure(Call call, final IOException e) {
                MainActivity.this.handler.post(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d(TAG, "update connect failed");
                            }
                        });
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final String resp = response.body().string();
                        if (!TextUtils.isEmpty(resp)) {
                            MainActivity.this.handler.post(new Runnable() {
                                public void run() {
                                    HashMap mapObj = new HashMap();
                                    Gson gson = new Gson();
                                    Map<String, String> map = (Map<String, String>) gson.fromJson(resp, mapObj.getClass());
                                    if (null != map) {
                                        Log.d(TAG, "update resp returned");
//                                        Double ver = 0.0;
//                                        if (null != map.get("VERSION")) {
//                                            try {
//                                                ver = Double.parseDouble(map.get("VERSION"));
//                                            } catch (NumberFormatException e) {
//                                                ver = 0.0;
//                                            } catch (Exception e) {
//                                                ver = 0.0;
//                                            }
//                                        }
                                        // 服务器请求版本号
                                        final String newVersionCode = map.get("VERSION");
                                        // 本地versionCode
                                        final long pkgVerCode = AppUtils.getAppVersion(MainActivity.this);
                                        // 本地versionName
                                        final String pkgVersionName = AppUtils.getAppVersionName(MainActivity.this);
                                        Log.d(TAG, "newVersionCode = " + String.valueOf(newVersionCode)
                                                + " and pkgVersionCode = " + String.valueOf(pkgVerCode)
                                                + "and pkgVersionName = " + pkgVersionName);
                                        // 更新地址
                                        String urlAdr = "";
                                        if (!TextUtils.isEmpty(map.get("APPURL"))) {
                                            urlAdr = map.get("APPURL");
                                        }
                                        final String downloadUrl = urlAdr;
                                        // TODO: 2017/6/15 测试下载地址
//                                        final String downloadUrl = "http://business.cdn.qianqian.com/cms/BaiduMusic-pcwebapphomedown1.apk";
                                        String apkName = "";
                                        if (!TextUtils.isEmpty(map.get("APKNAME"))) {
                                            apkName = map.get("APKNAME");
                                        }
                                        final String newApkName = apkName;
//                                        if (newVersionCode - pkgVerCode > 0) {
                                        if (!newVersionCode.equals(pkgVersionName)) {
                                            // 版本更新提示
                                            AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                                            builder.setTitle(getString(R.string.update_title));
                                            builder.setCancelable(false);
                                            builder.setMessage(getString(R.string.update_txt));
                                            builder.setPositiveButton(getString(R.string.update_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
//                                                    String fileName = String.format(getString(R.string.apk_name), newVersionName);
                                                    String fileName = newApkName;
                                                    String filePath = getApplicationContext().getFilesDir().getAbsolutePath() + "/" + fileName;
                                                    String fileAbsolutePath = Environment.getExternalStorageDirectory() + filePath;
                                                    File file = new File(fileAbsolutePath);
                                                    if (file.exists()) {
                                                        file.delete();
                                                    }

                                                    if (ConnectionUtils.isWifi(MainActivity.this)) {
                                                        downloadNewApp(downloadUrl, fileName, false);
                                                    } else {
                                                        // 如果当前是3G或4G等移动网络提示用户
                                                        showMobileNetworkAlert(downloadUrl, fileName);
                                                    }
                                                }
                                            });
                                            builder.setNegativeButton(getString(R.string.update_cancel),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            builder.create().show();
                                            Log.d(TAG, "update alertDialog show");
                                        }
                                    }

                                }
                            });
                        }
                    } catch (IOException var4) {
                        ;
                    }
                }

            }
        });
    }

    void showMobileNetworkAlert(final String url, final String fileName) {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.alert_dialog_title));
        builder.setMessage(getString(R.string.network_is_mobile));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.later_wifi_download), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downloadNewApp(url, fileName, false);
            }
        });
        builder.setNegativeButton(getString(R.string.network_continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downloadNewApp(url, fileName, true);
            }
        });
        builder.create().show();
    }

    private long mDownloadId;
    private boolean isCompleted;
    private DownloadManager manager;

    /**
     * 下载apk
     */
    public void downloadNewApp(String url, String fileName, boolean canMobileNetworkDownload) {
        try {
            String filePath = getApplicationContext().getFilesDir().getAbsolutePath();

            manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
            down.setTitle(fileName);
            if (canMobileNetworkDownload) {
                down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            } else {
                down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            }
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            down.setVisibleInDownloadsUi(true);

            //6.0系统强开权限
            String[] PERMISSIONS_STORAGE = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            PermissionUtil.requestPermission(MainActivity.this, PERMISSIONS_STORAGE, 1);

            createFolder(filePath);
            if (hasSDCard()) {
                down.setDestinationInExternalPublicDir(filePath, fileName);
            }
//            manager.enqueue(down);
            mDownloadId = manager.enqueue(down);
            //使用RxJava对下载信息进行轮询，500毫秒一次
            Observable.interval(300, 500, TimeUnit.MILLISECONDS)
                    .takeUntil(new Func1<Long, Boolean>() {
                        @Override
                        public Boolean call(Long aLong) {
                            return isCompleted;
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            checkDownloadStatus();
                        }
                    });
        } catch (Exception e) {
            showDownloadFailedDialog();
        }
    }

    private ProgressDialog mProgressDialog;

    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mDownloadId);//筛选下载任务，传入任务ID，可变参数
        Cursor cursor = manager.query(query);
        if (cursor.moveToFirst()) {
            long downloadedBytes = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            long totalBytes = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            mProgressDialog.setMax(((int) (totalBytes / 1024)));
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_RUNNING:
                    mProgressDialog.setProgress(((int) (downloadedBytes / 1024)));
                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    isCompleted = true;
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
            }
        }
        cursor.close();
    }

    private void createFolder(String url) {
        File folder = new File(url);
        if (!(folder.exists() && folder.isDirectory())) {
            //noinspection ResultOfMethodCallIgnored
            folder.mkdirs();
        }
    }

    public boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private void showDownloadFailedDialog() {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.download_fail_title));
        builder.setMessage(getString(R.string.download_fail_message));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                toWebsite();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void toWebsite() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(getString(R.string.share_title_url));
        intent.setData(content_url);
        startActivity(intent);
    }

    private ProgressDialog getProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressNumberFormat("%1d KB/%2d KB");
        progressDialog.setMessage(getString(R.string.updating_progress_tittle));
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(getApplicationContext(), getString(R.string.quit), Toast.LENGTH_SHORT).show();

            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}
