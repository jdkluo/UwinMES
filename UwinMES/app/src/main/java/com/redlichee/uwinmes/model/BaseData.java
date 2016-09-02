package com.redlichee.uwinmes.model;

import java.util.List;

/**
 * 部门，生产线，标准工序
 * Created by Administrator on 2016/8/25.
 */
public class BaseData {

    /**
     * Dept_Id : 15
     * Dept_Number : 06
     * Dept_Name : 生产部
     */

    private List<DeptBean> Dept;
    /**
     * PlId : 3
     * PlCode : 100001
     * Description : 芯线1
     * Dept_Number : 0610
     * Dept_Name : 低温护套车间
     * WcCode : 0101
     * WcName : 电缆芯线
     */

    private List<ProdLineBean> ProdLine;
    /**
     * OperationId : 1000000001
     * OpCode : 11
     * Description : 电缆芯线
     * WcId : 1000000001
     */

    private List<ProcessBean> Process;

    public List<DeptBean> getDept() {
        return Dept;
    }

    public void setDept(List<DeptBean> Dept) {
        this.Dept = Dept;
    }

    public List<ProdLineBean> getProdLine() {
        return ProdLine;
    }

    public void setProdLine(List<ProdLineBean> ProdLine) {
        this.ProdLine = ProdLine;
    }

    public List<ProcessBean> getProcess() {
        return Process;
    }

    public void setProcess(List<ProcessBean> Process) {
        this.Process = Process;
    }

    public static class DeptBean {
        private int Dept_Id;
        private String Dept_Number;
        private String Dept_Name;

        public int getDept_Id() {
            return Dept_Id;
        }

        public void setDept_Id(int Dept_Id) {
            this.Dept_Id = Dept_Id;
        }

        public String getDept_Number() {
            return Dept_Number;
        }

        public void setDept_Number(String Dept_Number) {
            this.Dept_Number = Dept_Number;
        }

        public String getDept_Name() {
            return Dept_Name;
        }

        public void setDept_Name(String Dept_Name) {
            this.Dept_Name = Dept_Name;
        }
    }

    public static class ProdLineBean {
        private int PlId;
        private String PlCode;
        private String Description;
        private String Dept_Number;
        private String Dept_Name;
        private String WcCode;
        private String WcName;

        public int getPlId() {
            return PlId;
        }

        public void setPlId(int PlId) {
            this.PlId = PlId;
        }

        public String getPlCode() {
            return PlCode;
        }

        public void setPlCode(String PlCode) {
            this.PlCode = PlCode;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getDept_Number() {
            return Dept_Number;
        }

        public void setDept_Number(String Dept_Number) {
            this.Dept_Number = Dept_Number;
        }

        public String getDept_Name() {
            return Dept_Name;
        }

        public void setDept_Name(String Dept_Name) {
            this.Dept_Name = Dept_Name;
        }

        public String getWcCode() {
            return WcCode;
        }

        public void setWcCode(String WcCode) {
            this.WcCode = WcCode;
        }

        public String getWcName() {
            return WcName;
        }

        public void setWcName(String WcName) {
            this.WcName = WcName;
        }
    }

    public static class ProcessBean {
        private int OperationId;
        private String OpCode;
        private String Description;
        private int WcId;

        public int getOperationId() {
            return OperationId;
        }

        public void setOperationId(int OperationId) {
            this.OperationId = OperationId;
        }

        public String getOpCode() {
            return OpCode;
        }

        public void setOpCode(String OpCode) {
            this.OpCode = OpCode;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public int getWcId() {
            return WcId;
        }

        public void setWcId(int WcId) {
            this.WcId = WcId;
        }
    }
}
