package com.witnsoft.interhis.tool;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yuyongjie on 17/3/22.
 */


public class OkPostTool implements NetPostInterface{
    private OkHttpClient mOkHttpClient;
    private Handler mHandler=new Handler(Looper.getMainLooper());
    private Gson mGson;

    public OkPostTool() {
        mGson=new Gson();
        mOkHttpClient=new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).retryOnConnectionFailure(true).cache(new Cache(Environment.getExternalStorageDirectory(),10*1024*1024)).build();

    }
    @Override
    public <T> void startPostRequest(String url, FormBody formBody, final Class<T> tClass, final CallBack<T> tCallBack) {
        Request request=new Request.Builder().url(url).post(formBody).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                tCallBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str =response.body().string();
                final T result=mGson.fromJson(str,tClass);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tCallBack.onSuccess(result);
                    }
                });
            }
        });
    }
}
