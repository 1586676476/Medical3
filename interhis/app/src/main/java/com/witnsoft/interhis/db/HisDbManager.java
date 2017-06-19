package com.witnsoft.interhis.db;

import android.content.Context;
import android.view.View;


import com.witnsoft.interhis.Chufang.ChuFangChinese;
import com.witnsoft.interhis.db.model.ChineseDetailModel;
import com.witnsoft.interhis.db.model.ChineseModel;
import com.witnsoft.libinterhis.utils.FileUtils;
import com.witnsoft.libinterhis.utils.LogUtils;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengchengpeng on 2017/6/2.
 */

public class HisDbManager {
    public static final String TAG = "DatabaseUtil";

    private static LogUtils logUtils = LogUtils.getLog();

    private static final int VERSION = 1;

    private static HisDbManager nManager = null;

    private DbManager manager = null;

    private static Context mContext;

    private HisDbManager() {
        DbManager.DaoConfig daoConfig = (new DbManager.DaoConfig())
                .setDbName("ASK_CHINESE.db") // db名
                .setDbVersion(VERSION) // db版本
                .setAllowTransaction(true) // 开启事务操作
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        logUtils.d(TAG, "in DatabaseUtils(), oldVersion=" + oldVersion
                                + ", newVersion=" + newVersion);
                        if (newVersion > oldVersion) {
                            // 版本更新时，在此执行表格表格等操作
                            upgradeDb(db, newVersion, oldVersion);
                        }
//                        try {
//                            HisDbManager.this.upgradeDatabase(db, oldVersion, newVersion);
//                        } catch (SAXException | IOException | ParserConfigurationException var5) {
//                            System.out.println();
//                        }

                    }
                });
        this.manager = x.getDb(daoConfig);
    }

    public static HisDbManager getManager() {
        if (null == nManager) {
            nManager = new HisDbManager();
        }
        return nManager;
    }

    public static synchronized void attachTo(Context context) {
        mContext = context.getApplicationContext();
    }


    // 版本更新内容
    private void upgradeDb(DbManager db, int newVersion, int oldVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            List<String> sols;
            String file = "db/" + String.valueOf(i + 1) + "/sql";
            sols = FileUtils.getFromAssets(mContext, file);
            if (sols != null && sols.size() > 0) {
                for (String sql : sols) {
                    try {
                        db.execNonQuery(sql);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void saveAskChinese(ChineseModel model) throws DbException {
        this.manager.saveOrUpdate(model);
    }

    public void saveAskChinese(ChineseDetailModel model) throws DbException {
        this.manager.saveOrUpdate(model);
    }

    public void deleteAskChinese(ChineseModel model) throws DbException {
        this.manager.delete(model);
    }

    public void deleteAskChinese(String str) throws DbException {
        this.manager.delete(ChineseDetailModel.class, WhereBuilder.b("CMC","=",str));
    }

    public void deleteAskNumbwe(int count) throws DbException{
        this.manager.delete(ChineseDetailModel.class,WhereBuilder.b("sl","=",count));
    }

    public List<ChineseDetailModel> upDate(String sl) throws DbException{
        Object message=this.manager.update(ChineseDetailModel.class,WhereBuilder.b("sl","=",sl));
        if (message!=null){
            message=new ArrayList<>();
        }
        return (List<ChineseDetailModel>) message;
    }

    public List<ChineseDetailModel> findChineseDeatilModel(String accid) throws DbException{
        Object message=this.manager.selector(ChineseDetailModel.class).where("ACCID","=",accid).findAll();
        if(null==message){
            message=new ArrayList();
        }
        return (List<ChineseDetailModel>) message;
    }

    public List<ChineseModel> findChineseModel(String acid) throws DbException{
        Object message=this.manager.selector(ChineseModel.class).where("ACID","=",acid).findAll();
        if (message!=null){
            message=new ArrayList<>();
        }
        return (List<ChineseModel>) message;
    }
}
