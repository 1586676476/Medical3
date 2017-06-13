package com.witnsoft.interhis.db.model;


import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Created by zhengchengpeng on 2017/6/5.
 */

// 中药处方详细
@Table(name = "ASK_CHINESE_MX")
public class ChineseDetailModel {

    // primary key
    @Column(name = "ACCID",
            isId = true,
            autoGen = false)
    private String accid;

    // 中药处方ID
    @Column(name = "ACID")
    private String sfxmbm;

    // 中药代码
    @Column(name = "CDM")
    private String cdm;

    // 中药名称
    @Column(name = "CMC")
    private String cmc;

    // 中药规格代码
    @Column(name = "CGGDM")
    private String cggDm;

    // 中药规格名称
    @Column(name = "CGGMC")
    private String cggMc;

    // 中药数量
    @Column(name = "SL")
    private int sl;

    // 总金额
    @Column(name = "je")
    private String je;

    // 单价
    @Column(name = "dj")
    private int bzjg;

    private String XMMC;//药名
    private String XMRJ;//药品拼音
    private int SFDLBM;//药品类别

    public String getXMMC() {
        return XMMC;
    }

    public void setXMMC(String XMMC) {
        this.XMMC = XMMC;
    }

    public String getXMRJ() {
        return XMRJ;
    }

    public void setXMRJ(String XMRJ) {
        this.XMRJ = XMRJ;
    }

    public int getSFDLBM() {
        return SFDLBM;
    }

    public void setSFDLBM(int SFDLBM) {
        this.SFDLBM = SFDLBM;
    }

    public ChineseModel getChinese(DbManager db) throws DbException {
        return db.findById(ChineseModel.class, accid);
    }

    public void setAccid(String acmId){
        this.accid = acmId;
    }

    public String getAccid() {
        return accid;
    }

    public void setAcId(String acId) {
        this.sfxmbm = acId;
    }

    public String getAcId() {
        return sfxmbm;
    }

    public void setCdm(String cdm) {
        this.cdm = cdm;
    }

    public String getCdm() {
        return cdm;
    }

    public void setCmc(String cmc) {
        this.cmc = cmc;
    }

    public String getCmc() {
        return cmc;
    }

    public void setCggDm(String cggDm) {
        this.cggDm = cggDm;
    }

    public String getCggDm() {
        return cggDm;
    }

    public void setCggMc(String cggMc) {
        this.cggMc = cggMc;
    }

    public String getCggMc() {
        return cggMc;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public int getSl() {
        return sl;
    }

    public void setJe(String je) {
        this.je = je;
    }

    public String getJe() {
        return je;
    }

    public void setDj(int dj) {
        this.bzjg = dj;
    }

    public int getDj() {
        return bzjg;
    }

}