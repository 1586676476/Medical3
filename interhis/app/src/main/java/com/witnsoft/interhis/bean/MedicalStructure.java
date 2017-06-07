package com.witnsoft.interhis.bean;

/**
 * Created by ${liyan} on 2017/6/7.
 */

public class MedicalStructure {
    private int SFXMBM;//药品的id号
    private String XMMC;//药名
    private String XMRJ;//药品拼音
    private int SFDLBM;//药品类别
    private int BZJG;//价格

    public int getSFXMBM() {
        return SFXMBM;
    }

    public void setSFXMBM(int SFXMBM) {
        this.SFXMBM = SFXMBM;
    }

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

    public int getBZJG() {
        return BZJG;
    }

    public void setBZJG(int BZJG) {
        this.BZJG = BZJG;
    }
}
