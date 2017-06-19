package com.witnsoft.interhis.Chufang;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.witnsoft.interhis.db.model.ChineseDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by ${liyan} on 2017/6/14.
 */

public class ChuFangChinese extends ChuFangBase {

    private static final String TAG = "ChuFangChinese";
    private String acmxs,acsm,zdsm,aiid;
    JSONArray jsonArray;

    public void setList(List<ChineseDetailModel> list) {
        jsonArray=new JSONArray();
        for (int i = 0;i<list.size();i++){
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("cmc",list.get(i).getCmc())
                          .put("sl", list.get(i).getSl())
                          .put("cdm",list.get(i).getCdm())
                          .put("dj",list.get(i).getDj());
            }catch (JSONException e){
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
    }

    @Override
    public void setAcmxs(String acmxs) {
        this.acmxs = acmxs;
    }

    @Override
    public void setAcsm(String acsm) {
        this.acsm = acsm;
    }

    @Override
    public void setZdsm(String zdsm) {
        this.zdsm = zdsm;
    }

    @Override
    public void setAiid(String aiid) {
        this.aiid = aiid;
    }

    public JSONObject fromJSON(List<ChineseDetailModel> list,String acmxs,String acsm,String zdsm,String aiid) {
        setList(list);
        setAcmxs(acmxs);
        setAcsm(acsm);
        setZdsm(zdsm);
        setAiid(aiid);
        // DATA
        JSONObject dataJo = new JSONObject();
        try {
            dataJo.put("yftype", "chinese")
                    .put("aiid",aiid)
                    .put("zdsm",zdsm)
                    .put("acmxs",acmxs)
                    .put("acsm",acsm)
                    .put("je", "1293")
                    .put("chufangmx",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "initData: "+dataJo.toString() );
            return dataJo;
    }
    //上传图片的方法
    public String upLoadFile(File file, String RequestURL){

        int res=0;
        String result = null;
        String laboratory;
        String BOUNDARY= UUID.randomUUID().toString();//边界标识 随机生成
        String PREFIX="_ _",LINE_END="\r\n";
        String CONTENT_TYPE="multipart/form-data";//内容类型
        String CHARSET = "utf-8"; // 设置编码
        try {
            URL url=new URL("http://zy.renyibao.com/FileUploadServlet");
            try {
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10*1000);
                connection.setConnectTimeout(10*1000);
                connection.setDoInput(true);//允许输入流
                connection.setDoOutput(true);//允许输出流
                connection.setUseCaches(false);//不允许使用缓存
                connection.setRequestMethod("POST");//请求方式
                connection.setRequestProperty("connection",CHARSET);
                connection.setRequestProperty("connection", "keep-alive");
                connection.setRequestProperty("Content_Type",CONTENT_TYPE+";boundary="+BOUNDARY);

                if (file!=null){
                    /***
                     * 当文件不存在则上传
                     */
                    DataOutputStream dos=new DataOutputStream(connection.getOutputStream());
                    StringBuffer sb=new StringBuffer();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINE_END);

                    sb.append("Content_Dispositin:form-data;name=\"file\";filename=\""+file.getName()+"\""+LINE_END);
                    sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                    sb.append(LINE_END);
                    dos.write(sb.toString().getBytes());
                    InputStream is=new FileInputStream(file);
                    byte[] bytes=new byte[1024];
                    int len=0;
                    while ((len=is.read(bytes))!=-1){
                        dos.write(bytes,0,len);
                    }
                    is.close();
                    dos.write(LINE_END.getBytes());
                    byte[] end_data=(PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                    dos.write(end_data);
                    dos.flush();
                    /**
                     * 获取响应码 200=成功 当响应成功，获取响应的流
                     */
                    res=connection.getResponseCode();
                    if (res==200){
                        InputStream input=connection.getInputStream();
                        StringBuffer sb1=new StringBuffer();
                        int ss;
                        while ((ss=input.read())!=-1){
                            sb1.append((char)ss);
                        }
                        result=sb1.toString();
//                        JSONObject jo;
//                        try {
//                            jo=new JSONObject(result);
//                            laboratory=jo.getString("result");
////                            Log.e(TAG, "upLoadFile: "+laboratory );
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        Log.e(TAG, "upLoadFile: "+result );
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
            return result;
    }


}
