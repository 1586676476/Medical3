package com.witnsoft.interhis.db.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by zhengchengpeng on 2017/6/5.
 */

// 西药处方明细
@Table(name = "ASK_WESTERN_MX")
public class WesternDetailModel {
    // primary key
    @Column(name = "AWMID",
            isId = true,
            autoGen = false)
    private String awmId;

    // 西药代码
    @Column(name = "AWDM")
    private String awDm;

    // 西药名称
    @Column(name = "AWMC")
    private String awMc;

    // 西药规格代码
    @Column(name = "AWGGDM")
    private String awGgDm;

    // 西药规格名称
    @Column(name = "AWGGMC")
    private String awGgMc;

    // 西药数量
    @Column(name = "AWSL")
    private String awSl;

    // 西药用法说明
    @Column(name = "AWSM")
    private String awSm;

    // 总金额
    @Column(name = "JE")
    private String je;

    // 单价
    @Column(name = "DJ")
    private String dj;

    public void setAwmId(String awmId) {
        this.awmId = awmId;
    }

    public String getAwmId() {
        return awmId;
    }

    public void setAwDm(String awDm) {
        this.awDm = awDm;
    }

    public String getAwDm() {
        return awDm;
    }

    public void setAwMc(String awMc) {
        this.awMc = awMc;
    }

    public String getAwMc() {
        return awMc;
    }

    public void setAwGgDm(String awGgDm) {
        this.awGgDm = awGgDm;
    }

    public String getAwGgDm() {
        return awGgDm;
    }

    public void setAwGgMc(String awGgMc) {
        this.awGgMc = awGgMc;
    }

    public String getAwGgMc() {
        return awGgMc;
    }

    public void setAwSl(String awSl) {
        this.awSl = awSl;
    }

    private String getAwSl() {
        return awSl;
    }

    public void setAwSm(String awSm) {
        this.awSm = awSm;
    }

    public String getAwSm() {
        return awSm;
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

