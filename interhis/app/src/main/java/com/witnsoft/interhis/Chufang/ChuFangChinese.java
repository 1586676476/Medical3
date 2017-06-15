package com.witnsoft.interhis.Chufang;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ${liyan} on 2017/6/14.
 */

public class ChuFangChinese extends ChuFangBase {

    private static final String TAG = "ChuFangChinese";

    public JSONObject fromJSON() {
        // 最里层数组
        JSONArray ja = new JSONArray();
        // 子类
        JSONObject jo00 = new JSONObject();
        try {
            jo00.put("cmd", "C00688")
                    .put("cmc", "胡梅")
                    .put("cggdm", "")
                    .put("cggmc", "")
                    .put("sf", "10")
                    .put("je", "100")
                    .put("dj", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ja.put(jo00);
        JSONObject jo01 = new JSONObject();
        try {
            jo01.put("cmd", "C00671")
                    .put("cmc", "车前子")
                    .put("cggdm", "")
                    .put("cggmc", "")
                    .put("sf", "10")
                    .put("je", "150")
                    .put("dj", "15");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ja.put(jo01);

        // DATA
        JSONObject dataJo = new JSONObject();
        try {
            dataJo.put("yftype", "chinese")
                    .put("aiid", "aiid001")
                    .put("ydid", "")
                    .put("zdsm", "肝热气滞症、脾胃不和症")
                    .put("acmxs", "2")
                    .put("acsm", "acsm")
                    .put("docqm", "")
                    .put("je", "1293")
                    .put("chufangmx", ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "initData: "+dataJo.toString() );
            return dataJo;
    }
}
