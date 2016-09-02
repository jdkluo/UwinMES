package com.redlichee.uwinmes.model;

import java.util.List;

/**
 * 编辑保存质检单
 * Created by LMW on 2016/8/24.
 */
public class CRUDQc {

    /**
     * id : 1
     * iquantity : 200
     * ihgqty : 192
     * iretqty : 5
     * itcqty : 5
     * iscrapqty : 4
     * ingqty : 8
     * cmodifier : 11
     * dmodifytime : 2016-08-22 16:48:00
     */

    private List<CheckvoucherBean> checkvoucher;
    /**
     * autoid : 306
     * iquantity : 210
     * ihgqty : 50
     * ingqty : 8
     * unit : KG
     * cngreason : 不良原因11
     * cngmemo : 不良描述11
     * iretqty : 6
     * itcqty : 5
     * iscrapqty : 4
     * detectValue : [{"autoid":306,"sampleNum":1,"sampleVal":33},{"autoid":306,"sampleNum":2,"sampleVal":34},{"autoid":306,"sampleNum":3,"sampleVal":35}]
     */

    private List<CheckvouchersBean> checkvouchers;

    public List<CheckvoucherBean> getCheckvoucher() {
        return checkvoucher;
    }

    public void setCheckvoucher(List<CheckvoucherBean> checkvoucher) {
        this.checkvoucher = checkvoucher;
    }

    public List<CheckvouchersBean> getCheckvouchers() {
        return checkvouchers;
    }

    public void setCheckvouchers(List<CheckvouchersBean> checkvouchers) {
        this.checkvouchers = checkvouchers;
    }

    public class CheckvoucherBean {
        private int id;
        private int iquantity;
        private int ihgqty;
        private int iretqty;
        private int itcqty;
        private int iscrapqty;
        private int ingqty;
        private int cmodifier;
        private String dmodifytime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIquantity() {
            return iquantity;
        }

        public void setIquantity(int iquantity) {
            this.iquantity = iquantity;
        }

        public int getIhgqty() {
            return ihgqty;
        }

        public void setIhgqty(int ihgqty) {
            this.ihgqty = ihgqty;
        }

        public int getIretqty() {
            return iretqty;
        }

        public void setIretqty(int iretqty) {
            this.iretqty = iretqty;
        }

        public int getItcqty() {
            return itcqty;
        }

        public void setItcqty(int itcqty) {
            this.itcqty = itcqty;
        }

        public int getIscrapqty() {
            return iscrapqty;
        }

        public void setIscrapqty(int iscrapqty) {
            this.iscrapqty = iscrapqty;
        }

        public int getIngqty() {
            return ingqty;
        }

        public void setIngqty(int ingqty) {
            this.ingqty = ingqty;
        }

        public int getCmodifier() {
            return cmodifier;
        }

        public void setCmodifier(int cmodifier) {
            this.cmodifier = cmodifier;
        }

        public String getDmodifytime() {
            return dmodifytime;
        }

        public void setDmodifytime(String dmodifytime) {
            this.dmodifytime = dmodifytime;
        }
    }

    public class CheckvouchersBean {
        private int autoid;
        private int iquantity;
        private int ihgqty;
        private int ingqty;
        private String unit;
        private String detectionValueTXT;
        private String cngreason;
        private String cngmemo;
        private int iretqty;
        private int itcqty;
        private int iscrapqty;
        /**
         * autoid : 306
         * sampleNum : 1
         * sampleVal : 33
         */

        private List<DetectValueBean> detectValue;

        public int getAutoid() {
            return autoid;
        }

        public void setAutoid(int autoid) {
            this.autoid = autoid;
        }

        public int getIquantity() {
            return iquantity;
        }

        public void setIquantity(int iquantity) {
            this.iquantity = iquantity;
        }

        public int getIhgqty() {
            return ihgqty;
        }

        public void setIhgqty(int ihgqty) {
            this.ihgqty = ihgqty;
        }

        public int getIngqty() {
            return ingqty;
        }

        public void setIngqty(int ingqty) {
            this.ingqty = ingqty;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDetectionValueTXT() {
            return detectionValueTXT;
        }

        public void setDetectionValueTXT(String detectionValueTXT) {
            this.detectionValueTXT = detectionValueTXT;
        }

        public String getCngreason() {
            return cngreason;
        }

        public void setCngreason(String cngreason) {
            this.cngreason = cngreason;
        }

        public String getCngmemo() {
            return cngmemo;
        }

        public void setCngmemo(String cngmemo) {
            this.cngmemo = cngmemo;
        }

        public int getIretqty() {
            return iretqty;
        }

        public void setIretqty(int iretqty) {
            this.iretqty = iretqty;
        }

        public int getItcqty() {
            return itcqty;
        }

        public void setItcqty(int itcqty) {
            this.itcqty = itcqty;
        }

        public int getIscrapqty() {
            return iscrapqty;
        }

        public void setIscrapqty(int iscrapqty) {
            this.iscrapqty = iscrapqty;
        }

        public List<DetectValueBean> getDetectValue() {
            return detectValue;
        }

        public void setDetectValue(List<DetectValueBean> detectValue) {
            this.detectValue = detectValue;
        }

        /**
         * 样本值实体
         */
        public class DetectValueBean {
            private int autoid;//质检明细主键
            private int sampleNum;//样本号
            private String sampleVal;//样本值

            public DetectValueBean() {
            }

            public DetectValueBean(int autoid, int sampleNum, String sampleVal) {
                this.autoid = autoid;
                this.sampleNum = sampleNum;
                this.sampleVal = sampleVal;
            }
            public int getAutoid() {
                return autoid;
            }

            public void setAutoid(int autoid) {
                this.autoid = autoid;
            }

            public int getSampleNum() {
                return sampleNum;
            }

            public void setSampleNum(int sampleNum) {
                this.sampleNum = sampleNum;
            }

            public String getSampleVal() {
                return sampleVal;
            }

            public void setSampleVal(String sampleVal) {
                this.sampleVal = sampleVal;
            }
        }
    }
}
