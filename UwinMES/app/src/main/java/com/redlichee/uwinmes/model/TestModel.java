package com.redlichee.uwinmes.model;

import java.util.ArrayList;

/**
 *
 * Created by LMW on 2016/8/18.
 */
public class TestModel {

    public String Id;              //流程卡主表ID
    public String AutoId;          //流程卡明细ID
    public String cCode;           //流程卡号
    public String PLName;           //生产线
    public String InvCode;         //存货编号
    public String cInvName;        //存货名称
    public String cInvStd;         //存货规则
    public String opCode;          //工序编码
    public String Description;     //工序名称
    public String CompleteQty;     //报工数量
    public String cComUnitCode;    //单位
    public String MDeptCode;       //部门号
    public String Dept_Name;       //部门名称
    public ArrayList<QcVoucher>  QcVouchers;       //流程明细记录

    public class QcVoucher {
       public String id;                  //质检单主键ID
       public String ccode;               //质检单号
       public String ddate;               //质检日期
       public String dtime;               //质检时间
       public String flowid;              //流程卡ID
       public String flowautoid;         //流程卡明细ID
       public String cqcpersoncode;     //质检人员编码
       public String cqctype;             //检验类型号
       public String cqctypeTXT;             //检验类型名
       public String iquantity;           //检验数量
       public String ihgqty;           //合格数量
       public String ingqty;           //不良数量
       public String iretqty;           //返工数量
       public String itcqty;           //特采数量
       public String iscrapqty;           //报废数量
       public String cQsName;           //方案名称
       public String solutionid;           //方案ID
       public String cmaker;           //创建人ID
       public String cmakerName;           //创建人
       public String dmaketime;           //创建时间
       public String cmodifier;           //修改人
       public String dmodifytime;           //修改时间
       public String cStatus;           //状态
    }

}
