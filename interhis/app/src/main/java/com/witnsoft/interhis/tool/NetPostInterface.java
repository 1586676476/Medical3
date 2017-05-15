package com.witnsoft.interhis.tool;

import okhttp3.FormBody;

/**
 * Created by yuyongjie on 17/3/22.
 */


public interface NetPostInterface {
    <T> void startPostRequest(String url, FormBody formBody, Class<T> tClass, CallBack<T> tCallBack);
}
