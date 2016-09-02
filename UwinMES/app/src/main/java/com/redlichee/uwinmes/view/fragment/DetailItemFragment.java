package com.redlichee.uwinmes.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.adapter.DetectValueAdapter;
import com.redlichee.uwinmes.application.Config;
import com.redlichee.uwinmes.base.BaseFragment;
import com.redlichee.uwinmes.model.CRUDQc;
import com.redlichee.uwinmes.model.DetailList;
import com.redlichee.uwinmes.utils.DateUtils;
import com.redlichee.uwinmes.utils.SharedPreUtil;
import com.redlichee.uwinmes.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 详情
 * Created by LMW on 2016/7/28.
 */
public class DetailItemFragment extends BaseFragment {
    private View mView;
    private Activity mActivity;

    private RelativeLayout rlayoutNorm;//标准要求
    private RelativeLayout rlayoutReality;//实际校验
    private RelativeLayout rlayoutUnhealthy;//不良处理
    private RelativeLayout rlayoutInUnhealthy;//整单不良处理

    private Button btnPositive;//保存此数据

    private TextView tvDestMethod;//检验方法
    private TextView tvNorm;//标准值
    private TextView tvCeiling;//上限
    private TextView tvFloor;//下限
    private TextView tvRequirement;//文字要求

    private EditText edtIquantity;//测量数量
    private EditText edtIhgqty;//合格数量
    private EditText edtIngqty;//不良数量
    private EditText edtUnit;//单位
    private EditText edtDetectionValue;//测量值
    private TextView tvDetectionValue;//测量值
    private EditText edtPrimaryAnalysis;//不良原因
    private EditText edtDescription;//不良描述

    private EditText edtIretqty;//返工
    private EditText edtItcqty;//特采
    private EditText edtIscrapqty;//报废

    private EditText edtWholeIquantity;//检验数量
    private EditText edtWholeIhgqty;//合格数量
    private EditText edtWholeIngqty;//不良数量
    private EditText edtWholeIretqty;//返工
    private EditText edtWholeItcqty;//特采
    private EditText edtWholeIscrapqty;//报废

    private Spinner spTesult;//判断结果下拉列表
    private List<String> mTesultData;//判断结果数据
    private ArrayAdapter<String> mTesultAdapter;//判断结果适配器

    private DetailList.Checkvouchers mData;

    private DetectValueAdapter mAdapter;
    private List<CRUDQc.CheckvouchersBean.DetectValueBean> mValueData = new ArrayList<CRUDQc.CheckvouchersBean.DetectValueBean>();

    private OnSaveData mOnSaveData;//保存数据接口
    private int mPosition;//在编写第mPosition项数据

    public static DetailItemFragment newInstance(DetailList.Checkvouchers data, int position, OnSaveData mOnSaveData){
        DetailItemFragment newFragment = new DetailItemFragment();
        newFragment.mData = data;
        newFragment.mOnSaveData = mOnSaveData;
        newFragment.mPosition = position;
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_detail_item, container, false);

        mActivity = getActivity();

        initViews();

        setSpinnerAdapter();

        setListeners();

        initDatas();

        return mView;
    }


    /**
     * 初始化组件
     */
    private void initViews() {

        rlayoutNorm = (RelativeLayout) mView.findViewById(R.id.rlayout_norm);
        rlayoutReality = (RelativeLayout) mView.findViewById(R.id.rlayout_reality);
        rlayoutUnhealthy = (RelativeLayout) mView.findViewById(R.id.rlayout_unhealthy);
        rlayoutInUnhealthy = (RelativeLayout) mView.findViewById(R.id.rlayout_integer_unhealthy);

        btnPositive = (Button) mView.findViewById(R.id.positive_button);

        tvDestMethod = (TextView) mView.findViewById(R.id.tv_test_method);
        tvNorm = (TextView) mView.findViewById(R.id.tv_norm);
        tvCeiling = (TextView) mView.findViewById(R.id.tv_ceiling);
        tvFloor = (TextView) mView.findViewById(R.id.tv_floor);
        tvRequirement = (TextView) mView.findViewById(R.id.tv_requirement);

        edtIquantity = (EditText) mView.findViewById(R.id.edt_iquantity);
        edtIhgqty = (EditText) mView.findViewById(R.id.edt_ihgqty);
        edtIngqty = (EditText) mView.findViewById(R.id.edt_ingqty);
        edtUnit = (EditText) mView.findViewById(R.id.edt_unit);
        edtDetectionValue = (EditText) mView.findViewById(R.id.edt_detection_value);
        tvDetectionValue = (TextView) mView.findViewById(R.id.tv_detection_value);
        edtPrimaryAnalysis = (EditText) mView.findViewById(R.id.edt_primary_analysis);
        edtDescription = (EditText) mView.findViewById(R.id.edt_description);

        spTesult = (Spinner)  mView.findViewById(R.id.sp_result);

        edtIretqty = (EditText) mView.findViewById(R.id.edt_iretqty);
        edtItcqty = (EditText) mView.findViewById(R.id.edt_itcqty);
        edtIscrapqty = (EditText) mView.findViewById(R.id.edt_iscrapqty);

        edtWholeIquantity = (EditText) mView.findViewById(R.id.edt_whole_iquantity);
        edtWholeIhgqty = (EditText) mView.findViewById(R.id.edt_whole_ihgqty);
        edtWholeIngqty = (EditText) mView.findViewById(R.id.edt_whole_ingqty);
        edtWholeIretqty = (EditText) mView.findViewById(R.id.edt_whole_iretqty);
        edtWholeItcqty = (EditText) mView.findViewById(R.id.edt_whole_itcqty);
        edtWholeIscrapqty = (EditText) mView.findViewById(R.id.edt_whole_iscrapqty);

        mTesultData = new ArrayList<String>();

    }

    private void initDatas() {
        if(mData != null){
            if (mData.category == 1) {
                //表头
                //控制View的显示
                rlayoutNorm.setVisibility(View.GONE);//标准要求
                rlayoutReality.setVisibility(View.GONE);//实际校验
                rlayoutUnhealthy.setVisibility(View.GONE);//不良处理
                rlayoutInUnhealthy.setVisibility(View.VISIBLE);//整单不良处理

                //设置从服务器获取的数据值
                edtWholeIquantity.setText(mData.iquantity);//检验数量
                edtWholeIhgqty.setText(mData.ihgqty);//合格数量
                edtWholeIngqty.setText(mData.ingqty);//不良数量
                edtWholeIretqty.setText(mData.iretqty);//返工
                edtWholeItcqty.setText(mData.itcqty);//特采
                edtWholeIscrapqty.setText(mData.iscrapqty);//报废
            } else {
                //表体
                //控制View的显示
                rlayoutNorm.setVisibility(View.VISIBLE);//标准要求
                rlayoutReality.setVisibility(View.VISIBLE);//实际校验
                rlayoutUnhealthy.setVisibility(View.VISIBLE);//不良处理
                rlayoutInUnhealthy.setVisibility(View.GONE);//整单不良处理

                //设置从服务器获取的数据值
                tvDestMethod.setText("检验方法：" + mData.cchkmachineTXT);//检验方法
                tvNorm.setText("标准值：" + mData.cstandard);//标准值
                tvCeiling.setText("上限：" + mData.cupperlimit);//上限
                tvFloor.setText("下限：" + mData.clowerlimit);//下限
                tvRequirement.setText("文字要求：" + mData.crequire);//文字要求

                edtIquantity.setText(mData.iquantity);//测量数量
                edtIhgqty.setText(mData.ihgqty );//合格数量
                edtIngqty.setText(mData.ingqty);//不良数量
//                edtUnit.setText("");//单位
                if(mData.getDetectValue() != null && mData.getDetectValue().size() > 0){
                    mValueData = mData.getDetectValue();
                }
                edtDetectionValue.setText(mData.detectionValueTXT);//测量值
                edtPrimaryAnalysis.setText(mData.cngreason);//不良原因
                edtDescription.setText(mData.cngmemo);//不良描述

                edtIretqty.setText(mData.iretqty );//返工
                edtItcqty.setText(mData.itcqty );//特采
                edtIscrapqty.setText(mData.iscrapqty);//报废
            }

        }
    }

    private void setSpinnerAdapter(){
        mTesultData.clear();

        mTesultData.add("无");
        mTesultData.add("合格");
        mTesultData.add("不合格");
        mTesultData.add("特采");

        //适配器
        mTesultAdapter= new ArrayAdapter<String>(mActivity, R.layout.dropdown_spinner_item, mTesultData);
        //设置样式
        mTesultAdapter.setDropDownViewResource(R.layout.dropdown_style);
        //加载适配器
        spTesult.setAdapter(mTesultAdapter);
    }

    /**
     * 设置测量值
     * @return
     */
    private String setDetectionValue(){
        if(mValueData == null){
            mValueData = new ArrayList<CRUDQc.CheckvouchersBean.DetectValueBean>();
        }
        List<CRUDQc.CheckvouchersBean.DetectValueBean> valueData = getValueData(mValueData);
        if(valueData.size() > 1){
            return valueData.get(0).getSampleVal() + "-" + valueData.get(valueData.size() - 1).getSampleVal();
        } else if(valueData.size() > 0){
            return valueData.get(0).getSampleVal();
        } else {
            return "";
        }
    }

    /**
     * 组件设置监听
     */
    private void setListeners() {
        //检测值
        tvDetectionValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mValueData.size() == 0){
                    mValueData.clear();
                    for (int i = 0; i < 5; i++) {
                        mValueData.add(new CRUDQc().new CheckvouchersBean().new DetectValueBean(mData.autoid, i+1, ""));
                    }
                }

                showListAddDialog(mActivity, "检测值", mValueData);

            }
        });
        //保存此数据
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mData != null){
                    if (mData.category == 1) {
                        //表头
                        String strWholeIquantity = edtWholeIquantity.getText().toString();//检验数量
                        String strWholeIhgqty = edtWholeIhgqty.getText().toString();//合格数量
                        String strWholeIngqty = edtWholeIngqty.getText().toString();//不良数量
                        String strWholeIretqty = edtWholeIretqty.getText().toString();//返工
                        String strWholeItcqty = edtWholeItcqty.getText().toString();//特采
                        String strWholeIscrapqty = edtWholeIscrapqty.getText().toString();//报废

                        //保存上传
                        CRUDQc.CheckvoucherBean cheModel = new CRUDQc().new CheckvoucherBean();
                        cheModel.setId(mData.id);
                        cheModel.setIquantity(Integer.valueOf(TextUtils.isEmpty(strWholeIquantity) ? "0" : strWholeIquantity));//检验数量
                        cheModel.setIhgqty(Integer.valueOf(TextUtils.isEmpty(strWholeIhgqty) ? "0" : strWholeIhgqty));//合格数量
                        cheModel.setIngqty(Integer.valueOf(TextUtils.isEmpty(strWholeIngqty) ? "0" : strWholeIngqty));//不良数量
                        cheModel.setIretqty(Integer.valueOf(TextUtils.isEmpty(strWholeIretqty) ? "0" : strWholeIretqty));//返工
                        cheModel.setItcqty(Integer.valueOf(TextUtils.isEmpty(strWholeItcqty) ? "0" : strWholeItcqty));//特采
                        cheModel.setIscrapqty(Integer.valueOf(TextUtils.isEmpty(strWholeIscrapqty) ? "0" : strWholeIscrapqty));//报废
                        cheModel.setCmodifier(Integer.valueOf(new SharedPreUtil(mActivity).get(Config.PERSON_ID)));//修改人
                        cheModel.setDmodifytime(DateUtils.getCurDate("yyyy-MM-dd HH:mm:ss"));//修改时间

                        //列表显示更新
                        mData.iquantity = TextUtils.isEmpty(strWholeIquantity) ? "0" : strWholeIquantity;
                        mData.ihgqty = TextUtils.isEmpty(strWholeIhgqty) ? "0" : strWholeIhgqty;
                        mData.ingqty = TextUtils.isEmpty(strWholeIngqty) ? "0" : strWholeIngqty;
                        mData.iretqty = TextUtils.isEmpty(strWholeIretqty) ? "0" : strWholeIretqty;
                        mData.itcqty = TextUtils.isEmpty(strWholeItcqty) ? "0" : strWholeItcqty;
                        mData.iscrapqty = TextUtils.isEmpty(strWholeIscrapqty) ? "0" : strWholeIscrapqty;
                        mData.duigou = 1;

                        //接口回调
                        mOnSaveData.putCheData(cheModel, mData, mPosition);
                    } else {
                        //表体
                        String strIquantity = edtIquantity.getText().toString();//测量数量
                        String strIhgqty = edtIhgqty.getText().toString();//合格数量
                        String strIngqty = edtIngqty.getText().toString();//不良数量
                        String strUnit = edtUnit.getText().toString();//单位
                        String strDetectionValue = edtDetectionValue.getText().toString();//测量值
                        String strPrimaryAnalysis = edtPrimaryAnalysis.getText().toString();//不良原因
                        String strDescription = edtDescription.getText().toString();//不良描述

                        String strIretqty = edtIretqty.getText().toString();//返工
                        String strItcqty = edtItcqty.getText().toString();//特采
                        String strIscrapqty = edtIscrapqty.getText().toString();//报废

                        //保存上传
                        CRUDQc.CheckvouchersBean chesModel = new CRUDQc().new CheckvouchersBean();
                        chesModel.setAutoid(mData.autoid);
                        chesModel.setIquantity(Integer.valueOf(TextUtils.isEmpty(strIquantity) ? "0" : strIquantity));
                        chesModel.setIhgqty(Integer.valueOf(TextUtils.isEmpty(strIhgqty) ? "0" : strIhgqty));
                        chesModel.setIngqty(Integer.valueOf(TextUtils.isEmpty(strIngqty) ? "0" : strIngqty));
                        chesModel.setUnit(strUnit);
                        chesModel.setDetectionValueTXT(strDetectionValue);
                        chesModel.setCngreason(strPrimaryAnalysis);
                        chesModel.setCngmemo(strDescription);
                        chesModel.setIretqty(Integer.valueOf(TextUtils.isEmpty(strIretqty) ? "0" : strIretqty));
                        chesModel.setItcqty(Integer.valueOf(TextUtils.isEmpty(strItcqty) ? "0" : strItcqty));
                        chesModel.setIscrapqty(Integer.valueOf(TextUtils.isEmpty(strIscrapqty) ? "0" : strIscrapqty));
                        chesModel.setDetectValue(getValueData(mValueData));

                        //列表显示更新
                        mData.iquantity = TextUtils.isEmpty(strIquantity) ? "0" : strIquantity;
                        mData.ihgqty = TextUtils.isEmpty(strIhgqty) ? "0" : strIhgqty;
                        mData.ingqty = TextUtils.isEmpty(strIngqty) ? "0" : strIngqty;
//                        mData.unit = strUnit;
                        mData.detectionValueTXT = strDetectionValue;
                        mData.cngreason = strPrimaryAnalysis;
                        mData.cngmemo = strDescription;
                        mData.iretqty = TextUtils.isEmpty(strIretqty) ? "0" : strIretqty;
                        mData.itcqty = TextUtils.isEmpty(strItcqty) ? "0" : strItcqty;
                        mData.iscrapqty = TextUtils.isEmpty(strIscrapqty) ? "0" : strIscrapqty;
                        mData.setDetectValue(getValueData(mValueData));
                        mData.duigou = 1;

                        //接口回调
                        mOnSaveData.putChesData(chesModel, mData,  mPosition);
                    }
                } else {
                    ToastUtils.showToast(mActivity, "此指标数据异常，请刷新数据！");
                }


//                mData.duigou = 1;//对勾

                //检查信息是否填写完整
//                if(TextUtils.isEmpty(strIquantity) || TextUtils.isEmpty(strIhgqty) || TextUtils.isEmpty(strIngqty)
//                        || TextUtils.isEmpty(strUnit) || TextUtils.isEmpty(strDetectionValue) || TextUtils.isEmpty(strPrimaryAnalysis)
//                        || TextUtils.isEmpty(strDescription) || TextUtils.isEmpty(strIretqty) || TextUtils.isEmpty(strItcqty)
//                        || TextUtils.isEmpty(strIscrapqty) || TextUtils.isEmpty(strWholeIretqty) || TextUtils.isEmpty(strWholeItcqty)
//                        || TextUtils.isEmpty(strWholeIscrapqty)){
//
//                    ToastUtils.showToast(getActivity(), "请将信息填写完整！");
//
//                } else {
//
//                }
            }
        });
    }

    /**
     * 检测值列表对话框
     * @param mContext
     * @param title
     */
    public void showListAddDialog(Context mContext, String title, final List<CRUDQc.CheckvouchersBean.DetectValueBean> valueData) {
        if (mContext == null){ return; }

        final Dialog d = new Dialog(mContext, R.style.DialogStyle);
        d.setContentView(R.layout.dialog_radio_list_add);
        d.setCanceledOnTouchOutside(false);
        d.setCancelable(true);
        TextView tv_title = (TextView) d.findViewById(R.id.tv_dialog_title);
        final ListView mListView = (ListView) d.findViewById(R.id.lv_dialog_radio);
        Button btn_add = (Button) d.findViewById(R.id.btn_add);
        Button btn_save = (Button) d.findViewById(R.id.btn_save);

        tv_title.setText(title);
        mAdapter = new DetectValueAdapter(mActivity, valueData);
        mListView.setAdapter(mAdapter);

        d.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                edtDetectionValue.setText(setDetectionValue());//设置检测值
            }
        });
        //增行
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCustomer(mListView);
                valueData.add(new CRUDQc().new CheckvouchersBean().new DetectValueBean(mData.autoid, mValueData.size()+1, ""));
                mAdapter.notifyDataSetChanged();
            }
        });
        //保存
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCustomer(mListView);
                edtDetectionValue.setText(setDetectionValue());//设置检测值
                d.dismiss();
            }
        });


        d.show();

    }

    /**
     * 处理检测值数据
     */
    public void setCustomer(ListView mListView){
        mValueData.clear();

        for (int i = 0; i < mListView.getChildCount(); i++) {
            TextView tv_position = (TextView) mListView.getChildAt(i).findViewById(R.id.tv_position);// 从layout中获得控件,根据其id
            EditText edt_value = (EditText) mListView.getChildAt(i).findViewById(R.id.edt_value);// 从layout中获得控件,根据其id
            String visitorPosition = tv_position.getText().toString();
            String visitorValue = edt_value.getText().toString();
            mValueData.add(new CRUDQc().new CheckvouchersBean().new DetectValueBean(
                    mData.autoid, Integer.valueOf(TextUtils.isEmpty(visitorPosition) ? "0" : visitorPosition), visitorValue));
        }
    }

    private List<CRUDQc.CheckvouchersBean.DetectValueBean> getValueData(List<CRUDQc.CheckvouchersBean.DetectValueBean> mValueData) {
        List<CRUDQc.CheckvouchersBean.DetectValueBean> valueData = new ArrayList<CRUDQc.CheckvouchersBean.DetectValueBean>();
        for (int i = 0; i < mValueData.size(); i++) {
            if (!TextUtils.isEmpty(mValueData.get(i).getSampleVal())) {
                valueData.add(mValueData.get(i));
            }
        }
        return valueData;
    }

    public interface OnSaveData {

        /**
         * 表头
         * @param cheModel
         */
        public void putCheData(CRUDQc.CheckvoucherBean cheModel,DetailList.Checkvouchers chesData, int position);

        /**
         *表体
         * @param chesModel
         */
        public void putChesData(CRUDQc.CheckvouchersBean chesModel,DetailList.Checkvouchers chesData, int position);
    }
}
