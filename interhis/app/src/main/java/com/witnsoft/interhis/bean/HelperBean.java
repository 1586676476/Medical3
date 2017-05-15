package com.witnsoft.interhis.bean;

import java.util.List;

/**
 * Created by ${liyan} on 2017/5/9.
 */

public class HelperBean {
    /**
     * VERSION : 1.0
     * TN : F27.APP.01.02
     * RESULT : 200
     * RESULTTEXT : 处理成功
     * REPARAM :
     * DATAARRAY : [{"DTYPE":"F27.APP.01.02.A","DNO":"0","TOTAL":"2","ROWSPERPAGE":"10","PAGENO":"1","DATA":[{"COUNTNUM":"1","AIID":"aiid002","DOCID":"test001","DOCNAME":"张强强","ACCID":"accid001","PATID":"patid002","PATNAME":"张三","JCJKXX":"家族病史、过敏史等","JBMC":"咳嗽多日，无痰，晚上睡眠不好","WZMD":"first","CREATETIME":"2017-05-04","ASKFEE":"10","PAYFLAG":"y","PAYTIME":"2017-05-04","PAYTYPE":"weixin","BEGINFLAG":"n","BEGINTIME":"2017-05-04","ENDTIME":"","ENDTYPE":""},{"COUNTNUM":"2","AIID":"aiid003","DOCID":"test001","DOCNAME":"张强强","ACCID":"accid001","PATID":"patid003","PATNAME":"王五","JCJKXX":"家族病史、过敏史等","JBMC":"咳嗽多日，无痰，晚上睡眠不好","WZMD":"first","CREATETIME":"2017-05-04","ASKFEE":"10","PAYFLAG":"y","PAYTIME":"2017-05-04","PAYTYPE":"weixin","BEGINFLAG":"n","BEGINTIME":"2017-05-04","ENDTIME":"","ENDTYPE":""}]}]
     */

    private String VERSION;
    private String TN;
    private String RESULT;
    private String RESULTTEXT;
    private String REPARAM;
    private List<DATAARRAYBean> DATAARRAY;

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getTN() {
        return TN;
    }

    public void setTN(String TN) {
        this.TN = TN;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public String getRESULTTEXT() {
        return RESULTTEXT;
    }

    public void setRESULTTEXT(String RESULTTEXT) {
        this.RESULTTEXT = RESULTTEXT;
    }

    public String getREPARAM() {
        return REPARAM;
    }

    public void setREPARAM(String REPARAM) {
        this.REPARAM = REPARAM;
    }

    public List<DATAARRAYBean> getDATAARRAY() {
        return DATAARRAY;
    }

    public void setDATAARRAY(List<DATAARRAYBean> DATAARRAY) {
        this.DATAARRAY = DATAARRAY;
    }

    public static class DATAARRAYBean {
        /**
         * DTYPE : F27.APP.01.02.A
         * DNO : 0
         * TOTAL : 2
         * ROWSPERPAGE : 10
         * PAGENO : 1
         * DATA : [{"COUNTNUM":"1","AIID":"aiid002","DOCID":"test001","DOCNAME":"张强强","ACCID":"accid001","PATID":"patid002","PATNAME":"张三","JCJKXX":"家族病史、过敏史等","JBMC":"咳嗽多日，无痰，晚上睡眠不好","WZMD":"first","CREATETIME":"2017-05-04","ASKFEE":"10","PAYFLAG":"y","PAYTIME":"2017-05-04","PAYTYPE":"weixin","BEGINFLAG":"n","BEGINTIME":"2017-05-04","ENDTIME":"","ENDTYPE":""},{"COUNTNUM":"2","AIID":"aiid003","DOCID":"test001","DOCNAME":"张强强","ACCID":"accid001","PATID":"patid003","PATNAME":"王五","JCJKXX":"家族病史、过敏史等","JBMC":"咳嗽多日，无痰，晚上睡眠不好","WZMD":"first","CREATETIME":"2017-05-04","ASKFEE":"10","PAYFLAG":"y","PAYTIME":"2017-05-04","PAYTYPE":"weixin","BEGINFLAG":"n","BEGINTIME":"2017-05-04","ENDTIME":"","ENDTYPE":""}]
         */

        private String DTYPE;
        private String DNO;
        private String TOTAL;
        private String ROWSPERPAGE;
        private String PAGENO;
        private List<DATABean> DATA;

        public String getDTYPE() {
            return DTYPE;
        }

        public void setDTYPE(String DTYPE) {
            this.DTYPE = DTYPE;
        }

        public String getDNO() {
            return DNO;
        }

        public void setDNO(String DNO) {
            this.DNO = DNO;
        }

        public String getTOTAL() {
            return TOTAL;
        }

        public void setTOTAL(String TOTAL) {
            this.TOTAL = TOTAL;
        }

        public String getROWSPERPAGE() {
            return ROWSPERPAGE;
        }

        public void setROWSPERPAGE(String ROWSPERPAGE) {
            this.ROWSPERPAGE = ROWSPERPAGE;
        }

        public String getPAGENO() {
            return PAGENO;
        }

        public void setPAGENO(String PAGENO) {
            this.PAGENO = PAGENO;
        }

        public List<DATABean> getDATA() {
            return DATA;
        }

        public void setDATA(List<DATABean> DATA) {
            this.DATA = DATA;
        }

        public static class DATABean {
            /**
             * COUNTNUM : 1
             * AIID : aiid002
             * DOCID : test001
             * DOCNAME : 张强强
             * ACCID : accid001
             * PATID : patid002
             * PATNAME : 张三
             * JCJKXX : 家族病史、过敏史等
             * JBMC : 咳嗽多日，无痰，晚上睡眠不好
             * WZMD : first
             * CREATETIME : 2017-05-04
             * ASKFEE : 10
             * PAYFLAG : y
             * PAYTIME : 2017-05-04
             * PAYTYPE : weixin
             * BEGINFLAG : n
             * BEGINTIME : 2017-05-04
             * ENDTIME :
             * ENDTYPE :
             */

            private String COUNTNUM;
            private String AIID;
            private String DOCID;
            private String DOCNAME;
            private String ACCID;
            private String PATID;
            private String PATNAME;
            private String JCJKXX;
            private String JBMC;
            private String WZMD;
            private String CREATETIME;
            private String ASKFEE;
            private String PAYFLAG;
            private String PAYTIME;
            private String PAYTYPE;
            private String BEGINFLAG;
            private String BEGINTIME;
            private String ENDTIME;
            private String ENDTYPE;

            public String getCOUNTNUM() {
                return COUNTNUM;
            }

            public void setCOUNTNUM(String COUNTNUM) {
                this.COUNTNUM = COUNTNUM;
            }

            public String getAIID() {
                return AIID;
            }

            public void setAIID(String AIID) {
                this.AIID = AIID;
            }

            public String getDOCID() {
                return DOCID;
            }

            public void setDOCID(String DOCID) {
                this.DOCID = DOCID;
            }

            public String getDOCNAME() {
                return DOCNAME;
            }

            public void setDOCNAME(String DOCNAME) {
                this.DOCNAME = DOCNAME;
            }

            public String getACCID() {
                return ACCID;
            }

            public void setACCID(String ACCID) {
                this.ACCID = ACCID;
            }

            public String getPATID() {
                return PATID;
            }

            public void setPATID(String PATID) {
                this.PATID = PATID;
            }

            public String getPATNAME() {
                return PATNAME;
            }

            public void setPATNAME(String PATNAME) {
                this.PATNAME = PATNAME;
            }

            public String getJCJKXX() {
                return JCJKXX;
            }

            public void setJCJKXX(String JCJKXX) {
                this.JCJKXX = JCJKXX;
            }

            public String getJBMC() {
                return JBMC;
            }

            public void setJBMC(String JBMC) {
                this.JBMC = JBMC;
            }

            public String getWZMD() {
                return WZMD;
            }

            public void setWZMD(String WZMD) {
                this.WZMD = WZMD;
            }

            public String getCREATETIME() {
                return CREATETIME;
            }

            public void setCREATETIME(String CREATETIME) {
                this.CREATETIME = CREATETIME;
            }

            public String getASKFEE() {
                return ASKFEE;
            }

            public void setASKFEE(String ASKFEE) {
                this.ASKFEE = ASKFEE;
            }

            public String getPAYFLAG() {
                return PAYFLAG;
            }

            public void setPAYFLAG(String PAYFLAG) {
                this.PAYFLAG = PAYFLAG;
            }

            public String getPAYTIME() {
                return PAYTIME;
            }

            public void setPAYTIME(String PAYTIME) {
                this.PAYTIME = PAYTIME;
            }

            public String getPAYTYPE() {
                return PAYTYPE;
            }

            public void setPAYTYPE(String PAYTYPE) {
                this.PAYTYPE = PAYTYPE;
            }

            public String getBEGINFLAG() {
                return BEGINFLAG;
            }

            public void setBEGINFLAG(String BEGINFLAG) {
                this.BEGINFLAG = BEGINFLAG;
            }

            public String getBEGINTIME() {
                return BEGINTIME;
            }

            public void setBEGINTIME(String BEGINTIME) {
                this.BEGINTIME = BEGINTIME;
            }

            public String getENDTIME() {
                return ENDTIME;
            }

            public void setENDTIME(String ENDTIME) {
                this.ENDTIME = ENDTIME;
            }

            public String getENDTYPE() {
                return ENDTYPE;
            }

            public void setENDTYPE(String ENDTYPE) {
                this.ENDTYPE = ENDTYPE;
            }
        }
    }
}
