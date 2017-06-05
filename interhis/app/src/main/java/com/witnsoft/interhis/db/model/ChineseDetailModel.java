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
    @Column(
            name = "ACMID",
            isId = true,
            autoGen = false)
    private String acmId;

    // 中药处方ID
    @Column(name = "ACID")
    private String acId;

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
    private String sl;

    // 总金额
    @Column(name = "je")
    private String je;

    // 单价
    @Column(name = "dj")
    private String dj;

    public ChineseModel getChinese(DbManager db) throws DbException {
        return db.findById(ChineseModel.class, acmId);
    }

    public void setAcmId(String acmId){
        this.acmId = acmId;
    }

    public String getAcmId() {
        return acmId;
    }

    public void setAcId(String acId) {
        this.acId = acId;
    }

    public String getAcId() {
        return acId;
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

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getSl() {
        return sl;
    }

    public void setJe(String je) {
        this.je = je;
    }

    public String getJe() {
        return je;
    }

    public void setDj(String dj) {
        this.dj = dj;
    }

    public String getDj() {
        return dj;
    }

}