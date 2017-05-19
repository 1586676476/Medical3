package com.witnsoft.interhis.bean;

/**
 * Created by ${liyan} on 2017/5/15.
 */

public class Bean {


    /**
     * OTRequest : {"VERSION":"1.0","TN":"F27.APP.01.02","CLIENTID":"YLAPP","RYBID":"DOCTOR","DATA":{"docid":"test001","rowsperpage":"10","pageno":"1","ordercolumn":"paytime","ordertype":"asc"}}
     */

    private OTRequestBean OTRequest;

    public OTRequestBean getOTRequest() {
        return OTRequest;
    }

    public void setOTRequest(OTRequestBean OTRequest) {
        this.OTRequest = OTRequest;
    }

    public static class OTRequestBean {
        /**
         * VERSION : 1.0
         * TN : F27.APP.01.02
         * CLIENTID : YLAPP
         * RYBID : DOCTOR
         * DATA : {"docid":"test001","rowsperpage":"10","pageno":"1","ordercolumn":"paytime","ordertype":"asc"}
         */

        private String VERSION;
        private String TN;
        private String CLIENTID;
        private String RYBID;
        private DATABean DATA;

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

        public String getCLIENTID() {
            return CLIENTID;
        }

        public void setCLIENTID(String CLIENTID) {
            this.CLIENTID = CLIENTID;
        }

        public String getRYBID() {
            return RYBID;
        }

        public void setRYBID(String RYBID) {
            this.RYBID = RYBID;
        }

        public DATABean getDATA() {
            return DATA;
        }

        public void setDATA(DATABean DATA) {
            this.DATA = DATA;
        }

        public static class DATABean {
            /**
             * docid : test001
             * rowsperpage : 10
             * pageno : 1
             * ordercolumn : paytime
             * ordertype : asc
             */

            private String docid;
            private String rowsperpage;
            private String pageno;
            private String ordercolumn;
            private String ordertype;

            public String getDocid() {
                return docid;
            }

            public void setDocid(String docid) {
                this.docid = docid;
            }

            public String getRowsperpage() {
                return rowsperpage;
            }

            public void setRowsperpage(String rowsperpage) {
                this.rowsperpage = rowsperpage;
            }

            public String getPageno() {
                return pageno;
            }

            public void setPageno(String pageno) {
                this.pageno = pageno;
            }

            public String getOrdercolumn() {
                return ordercolumn;
            }

            public void setOrdercolumn(String ordercolumn) {
                this.ordercolumn = ordercolumn;
            }

            public String getOrdertype() {
                return ordertype;
            }

            public void setOrdertype(String ordertype) {
                this.ordertype = ordertype;
            }
        }
    }
}
