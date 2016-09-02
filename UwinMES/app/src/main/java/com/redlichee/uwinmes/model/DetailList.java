package com.redlichee.uwinmes.model;

import java.io.Serializable;
import java.util.List;

/**
 * 详情列表
 * Created by LMW on 2016/07/28.
 */
public class DetailList implements Serializable {

    /**
     * 表体
     */
    public class Checkvouchers {

        public int duigou;// 对勾图标 填写完成1 未填写0
        public int category;//类型  1表头  2表体
        public String cQqName;//检验指标名称
        public int autoid;//质检明细主键
        public int id;//质检单主键
        public int irowno;//行号
        public String cqicode;//检验项目编码
        public String cQiName;//检验项目名称
        public String cqqcode;//检验指标编码
        public String cqcstyle;//检验方式 0：抽检；1：全检；2：免检
        public String cchkmachine;//检验设备仪器，即检验工具
        public String cchkmachineTXT;//检验设备仪器
        public int iguidetype;//标准值类型 1：计数；2计量
        public String cstandard;//标准值
        public String cupperlimit;//上限值
        public String clowerlimit;//下限值
        public String crequire;//检验要求
        public String cmemo;//备注

        public String iquantity;//检验数量
        public String ihgqty;//合格数量
        public String ingqty;//不良数量
        public String detectionValueTXT;//检测值字符串
        public String cngreason;//不良原因
        public String cngmemo;//不良描述
        public String iretqty;//返工数量
        public String itcqty;//特采数量
        public String iscrapqty;//报废数量
        private List<CRUDQc.CheckvouchersBean.DetectValueBean> detectValue;

        public List<CRUDQc.CheckvouchersBean.DetectValueBean> getDetectValue() {
            return detectValue;
        }

        public void setDetectValue(List<CRUDQc.CheckvouchersBean.DetectValueBean> detectValue) {
            this.detectValue = detectValue;
        }
    }

    /**
     * 表头
     */
    public class Checkvoucher {

        public int id;//质检主键id
        public String ccode;//质检单号
        public String ddate;//质检日期
        public String dtime;//质检时间
        public int flowid;//流程卡id
        public String flowCode;//流程卡号
        public String cInvCode;//物料编码
        public String cInvName;//物料名称
        public String cInvStd;//物料规格
        public String opCode;//工序编码
        public String Description;//工序名称
        public int flowautoid;//流程卡明细id
        public String flowsCode;//流程卡工序流程号
        public int CompleteQty;//数量
        public String cqcpersoncode;//检验员编码
        public String cqcpersonName;//检验员
        public String cqctype;//检验类型
        public String cqctypeTXT;//检验类型
        public String iquantity;//          检验数量
        public String ihgqty;//             合格数量
        public String iretqty;//            返工数量
        public String itcqty;//             特采数量
        public String iscrapqty;//          报废数量
        public String ingqty;//             不良数量
        public int solutionid;//方案id
        public String solutionCode;//方案编码
        public String solutionName;//方案名称
        public String cmaker;//创建人id
        public String dmaketime;//创建时间
        public String cmodifier;//修改人id
        public String dmodifytime;//修改时间
        public int cStatus;//状态
        public String RelsUser;//审核人id
        public String RelsTime;//审核时间
    }

}