package com.witnsoft.interhis.db;

import android.content.Context;

import com.witnsoft.libinterhis.db.model.ChineseDetailModel;
import com.witnsoft.libinterhis.db.model.ChineseModel;
import com.witnsoft.libinterhis.utils.FileUtils;
import com.witnsoft.libinterhis.utils.LogUtils;

import org.xutils.DbManager;
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
//    private synchronized void upgradeDatabase(DbManager db, int oldVersion, int newVersion)
//            throws IOException, ParserConfigurationException, SAXException {
//        InputStream is = mContext.getAssets().open("db_upgrade.xml");
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document document = builder.parse(is);
//        Element root = document.getDocumentElement();
//        NodeList versions = root.getElementsByTagName("version");
//
//        for (int m = oldVersion; m < newVersion; ++m) {
//            Node version = versions.item(m);
//            if (null != version) {
//                NodeList modifications = version.getChildNodes();
//                int length = modifications.getLength();
//
//                for (int n = 0; n < length; ++n) {
//                    Node modification = modifications.item(n);
//                    if (null != modification && "modification".equals(modification.getNodeName())) {
//                        db.execNonQuery(modification.getTextContent());
//                    }
//                }
//            }
//        }
//
//    }

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

    public void deleteAskChinese(ChineseModel model) throws DbException {
        this.manager.delete(model);
    }

    public List<ChineseModel> findChineseByPrimId(String acId) throws DbException {
        Object messages = this.manager.selector(ChineseModel.class).
                where("ACID", "=", acId).findAll();
        if (null == messages) {
            messages = new ArrayList();
        }

        return (List) messages;
    }

    public void saveAskChinese(ChineseDetailModel model) throws DbException {
        this.manager.saveOrUpdate(model);
    }
    public void deleteAskChinese(ChineseDetailModel model) throws DbException {
        this.manager.delete(model);
    }
}
