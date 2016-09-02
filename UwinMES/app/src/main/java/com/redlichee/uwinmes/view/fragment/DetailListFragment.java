package com.redlichee.uwinmes.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.adapter.DetailListAdapter;
import com.redlichee.uwinmes.application.Config;
import com.redlichee.uwinmes.base.BaseFragment;
import com.redlichee.uwinmes.model.BaseData;
import com.redlichee.uwinmes.model.CRUDQc;
import com.redlichee.uwinmes.model.DetailList;
import com.redlichee.uwinmes.utils.DateUtils;
import com.redlichee.uwinmes.utils.JsonUtils;
import com.redlichee.uwinmes.utils.LogUtils;
import com.redlichee.uwinmes.utils.SharedPreUtil;
import com.redlichee.uwinmes.utils.ToastUtils;
import com.redlichee.uwinmes.utils.net.WebService;
import com.redlichee.uwinmes.view.activity.DetailItemActivity;
import com.redlichee.uwinmes.view.activity.MainDetailActivity;
import com.redlichee.uwinmes.widget.ProgressBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 质检信息列表
 * Created by LMW on 2016/7/28.
 */
public class DetailListFragment extends BaseFragment implements DetailItemFragment.OnSaveData {

    private View mView;
    private Activity mActivity;
    private ListView mListView;
    private Button btnSave;//保存按钮
    private DetailListAdapter mAdapter;
    private ArrayList<DetailList.Checkvouchers> mData = new ArrayList<DetailList.Checkvouchers>();

    /**
     * 是否是双页模式。如果一个Activity中包含了两个Fragment，就是双页模式。
     */
    private boolean isTwoPane;

    private CRUDQc qcModel;

    public int qcType; //类型 1：创建质检单 2：编辑质检单
    public int FlowAutoId; //流程卡明细ID
    public int Id; //存货对照ID
    public String id = ""; //质检单主键ID

    public static DetailListFragment newInstance(int qcType, int FlowAutoId, int Id, String id){
        DetailListFragment newFragment = new DetailListFragment();
        newFragment.qcType = qcType;
        newFragment.FlowAutoId = FlowAutoId;
        newFragment.Id = Id;
        newFragment.id = id;
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_detail_list, container, false);

        mActivity = getActivity();

        initViews();

        setListeners();

        loadData();

        return mView;
    }

    private void initViews() {
        btnSave = (Button) mView.findViewById(R.id.btn_detail_list_save);
        mListView = (ListView) mView.findViewById(R.id.list_detail_list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选模式

        mAdapter = new DetailListAdapter(mActivity, mData);
        mListView.setAdapter(mAdapter);
    }

    private void initDatas() {

        if(mData.size() > 0){
            //默认选中第一个
            toDetailItem(0);
        }

    }

    /**
     * 当Activity创建完毕后，尝试获取一下布局文件中是否有details_layout这个元素，如果有说明当前
     * 是双页模式，如果没有说明当前是单页模式。
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.details_layout) != null) {
            isTwoPane = true;
        } else {
            isTwoPane = false;
        }
    }

    private void setListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                toDetailItem(position);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSaveData(qcModel == null ? "" : new Gson().toJson(qcModel) + "");
            }
        });
    }

    private void toDetailItem(int position){
        if (isTwoPane) {
            //设置当前点击的Item的背景颜色
            mAdapter.setSelectItem(position);
            mAdapter.notifyDataSetInvalidated();

            Fragment fragment = new DetailItemFragment().newInstance(mData.get(position), position, DetailListFragment.this);
            getFragmentManager().beginTransaction().replace(R.id.details_layout, fragment).commit();
        } else {
            Intent intent = new Intent(getActivity(), DetailItemActivity.class);
            startActivity(intent);
        }

    }

    /**
     * 获取待检验质检单
     */
    private void loadData(){

        String URL;
        SimpleArrayMap mapParams = new SimpleArrayMap();

        if( qcType == 1){
            mapParams.put("Id", String.valueOf(Id));//存货对照ID
            mapParams.put("flowautoid", String.valueOf(FlowAutoId));//流程卡明细ID
            mapParams.put("User_Number", new SharedPreUtil(mActivity).get(Config.EMPLOYEE_NUM));//操作人员编号
            mapParams.put("User_Id", new SharedPreUtil(mActivity).get(Config.PERSON_ID));//用户ID
            mapParams.put("Ddate", DateUtils.getCurDate("yyyy-MM-dd HH:mm:ss"));//日期
            URL = Config.URL_QC_LIST;
        } else {
            if(TextUtils.isEmpty(id)){
                return;
            }
            mapParams.put("id", id);//质检单主键ID
            URL = Config.URL_GET_QC_LIST;
        }

        final ProgressBarView progressBar = new ProgressBarView(mActivity);//请求等待对话框
        progressBar.showProgressBar("加载中");
        WebService.call(mActivity, URL, mapParams,  new WebService.Response() {
                    @Override
                    public void onSuccess(SoapObject result) {
                        progressBar.dismissProgressBar();
                        mData.clear();

                        if( result!=null || result.getPropertyCount() > 0){
                            try {
                                JSONObject json = new JSONObject(result.getProperty(0).toString());
                                int code = JsonUtils.getJSONInt(json, "code");
                                if (code == 1) {

                                    JSONObject resut = json.optJSONObject("reslut");

                                    //初始化编辑或保存数据
                                    qcModel = new CRUDQc();
                                    List<CRUDQc.CheckvoucherBean> checkvoucher = new ArrayList<CRUDQc.CheckvoucherBean>();
                                    List<CRUDQc.CheckvouchersBean> checkvouchers = new ArrayList<CRUDQc.CheckvouchersBean>();

                                    //初始化编辑或保存数据
                                    JSONArray cObj = JsonUtils.getSafeJsonArray(resut, "checkvouchers");
                                    DetailList.Checkvouchers cModel ;
                                    CRUDQc.CheckvouchersBean chesModel;
                                    for (int j = 0; j < cObj.length(); j++) {
                                        JSONObject cItemObj = cObj.getJSONObject(j);
                                        chesModel = new CRUDQc().new CheckvouchersBean();
                                        chesModel.setAutoid(JsonUtils.getJSONInt(cItemObj, "autoid"));
                                        chesModel.setIquantity(JsonUtils.getJSONInt(cItemObj, "iquantity"));
                                        chesModel.setIhgqty(JsonUtils.getJSONInt(cItemObj, "ihgqty"));
                                        chesModel.setIngqty(JsonUtils.getJSONInt(cItemObj, "ingqty"));
                                        chesModel.setUnit(JsonUtils.getJSONString(cItemObj, "unit"));
                                        chesModel.setDetectionValueTXT(JsonUtils.getJSONString(cItemObj, "detectionValueTXT"));
                                        chesModel.setCngreason(JsonUtils.getJSONString(cItemObj, "cngreason"));
                                        chesModel.setCngmemo(JsonUtils.getJSONString(cItemObj, "cngmemo"));
                                        chesModel.setIretqty(JsonUtils.getJSONInt(cItemObj, "iretqty"));
                                        chesModel.setItcqty(JsonUtils.getJSONInt(cItemObj, "itcqty"));
                                        chesModel.setIscrapqty(JsonUtils.getJSONInt(cItemObj, "iscrapqty"));

                                        JSONArray valueObj = JsonUtils.getSafeJsonArray(cItemObj, "detectValue");
                                        List<CRUDQc.CheckvouchersBean.DetectValueBean> valueArray = new ArrayList<CRUDQc.CheckvouchersBean.DetectValueBean>();
                                        CRUDQc.CheckvouchersBean.DetectValueBean vModel;
                                        for (int z = 0; z < valueObj.length(); z++) {
                                            JSONObject vItemObj = valueObj.getJSONObject(z);
                                            vModel = new CRUDQc().new CheckvouchersBean().new DetectValueBean();
                                            vModel.setAutoid(JsonUtils.getJSONInt(vItemObj, "autoid"));
                                            vModel.setSampleNum(JsonUtils.getJSONInt(vItemObj, "samplenum"));//样本号
                                            vModel.setSampleVal(JsonUtils.getJSONString(vItemObj, "sampleval"));//样本值
                                            valueArray.add(vModel);
                                        }
                                        chesModel.setDetectValue(valueArray);
                                        checkvouchers.add(chesModel);

                                        //获取列表数据
                                        cModel = new DetailList() .new Checkvouchers();
                                        cModel.duigou = JsonUtils.getJSONInt(cItemObj, "duigou");//数据是否填写
                                        cModel.category = 2;//类型  1表头  2表体

                                        cModel.cQqName = JsonUtils.getJSONString(cItemObj, "cQqName");
                                        cModel.autoid = JsonUtils.getJSONInt(cItemObj, "autoid");
                                        cModel.id = JsonUtils.getJSONInt(cItemObj, "id");
                                        cModel.irowno = JsonUtils.getJSONInt(cItemObj, "irowno");
                                        cModel.cqicode = JsonUtils.getJSONString(cItemObj, "cqicode");
                                        cModel.cQiName = JsonUtils.getJSONString(cItemObj, "cQiName");
                                        cModel.cqqcode = JsonUtils.getJSONString(cItemObj, "cqqcode");
                                        cModel.cqcstyle = JsonUtils.getJSONString(cItemObj, "cqcstyle");
                                        cModel.cchkmachine = JsonUtils.getJSONString(cItemObj, "cchkmachine");
                                        cModel.cchkmachineTXT = JsonUtils.getJSONString(cItemObj, "cchkmachineTXT");
                                        cModel.iguidetype = JsonUtils.getJSONInt(cItemObj, "iguidetype");
                                        cModel.cstandard = JsonUtils.getJSONString(cItemObj, "cstandard");
                                        cModel.cupperlimit = JsonUtils.getJSONString(cItemObj, "cupperlimit");
                                        cModel.clowerlimit = JsonUtils.getJSONString(cItemObj, "clowerlimit");
                                        cModel.crequire = JsonUtils.getJSONString(cItemObj, "crequire");
                                        cModel.cmemo = JsonUtils.getJSONString(cItemObj, "cmemo");

                                        cModel.iquantity = JsonUtils.getJSONInt(cItemObj, "iquantity") + "";
                                        cModel.ihgqty = JsonUtils.getJSONInt(cItemObj, "ihgqty") + "";
                                        cModel.ingqty = JsonUtils.getJSONInt(cItemObj, "ingqty") + "";
                                        cModel.detectionValueTXT = JsonUtils.getJSONString(cItemObj, "detectionValueTXT");
                                        cModel.cngreason = JsonUtils.getJSONString(cItemObj, "cngreason");
                                        cModel.cngmemo = JsonUtils.getJSONString(cItemObj, "cngmemo");
                                        cModel.iretqty = JsonUtils.getJSONInt(cItemObj, "iretqty") + "";
                                        cModel.itcqty = JsonUtils.getJSONInt(cItemObj, "itcqty") + "";
                                        cModel.iscrapqty = JsonUtils.getJSONInt(cItemObj, "iscrapqty") + "";
                                        List<CRUDQc.CheckvouchersBean.DetectValueBean> cValueArray = new ArrayList<CRUDQc.CheckvouchersBean.DetectValueBean>();
                                        for (int z = 0; z < valueObj.length(); z++) {
                                            JSONObject vItemObj = valueObj.getJSONObject(z);
                                            vModel = new CRUDQc().new CheckvouchersBean().new DetectValueBean();
                                            vModel.setAutoid(JsonUtils.getJSONInt(vItemObj, "autoid"));
                                            vModel.setSampleNum(JsonUtils.getJSONInt(vItemObj, "samplenum"));//样本号
                                            vModel.setSampleVal(JsonUtils.getJSONString(vItemObj, "sampleval"));//样本值
                                            cValueArray.add(vModel);
                                        }
                                        cModel.setDetectValue(cValueArray);

                                        mData.add(cModel);
                                    }

                                    JSONArray arrayObj = JsonUtils.getSafeJsonArray(resut, "checkvoucher");
                                    CRUDQc.CheckvoucherBean cheModel;
                                    for (int i = 0; i < arrayObj.length(); i++) {
                                        JSONObject itemObj = arrayObj.getJSONObject(i);

                                        //初始化编辑或保存数据
                                        cheModel = new CRUDQc().new CheckvoucherBean();
                                        cheModel.setId(JsonUtils.getJSONInt(itemObj, "id"));
                                        cheModel.setIquantity(JsonUtils.getJSONInt(itemObj, "iquantity"));
                                        cheModel.setIhgqty(JsonUtils.getJSONInt(itemObj, "ihgqty"));
                                        cheModel.setIretqty(JsonUtils.getJSONInt(itemObj, "iretqty"));
                                        cheModel.setItcqty(JsonUtils.getJSONInt(itemObj, "itcqty"));
                                        cheModel.setIscrapqty(JsonUtils.getJSONInt(itemObj, "iscrapqty"));
                                        cheModel.setIngqty(JsonUtils.getJSONInt(itemObj, "ingqty"));
                                        cheModel.setCmodifier(JsonUtils.getJSONInt(itemObj, "cmodifier"));
                                        cheModel.setDmodifytime(JsonUtils.getJSONString(itemObj, "dmodifytime"));
                                        checkvoucher.add(cheModel);

                                        //获取列表数据
                                        cModel = new DetailList() .new Checkvouchers();
                                        cModel.duigou = JsonUtils.getJSONInt(itemObj, "duigou");//数据是否填写
                                        cModel.category = 1;//类型  1表头  2表体

                                        cModel.cQqName = "整单不良数据";

                                        cModel.id = JsonUtils.getJSONInt(itemObj, "id");
                                        cModel.iquantity = JsonUtils.getJSONInt(itemObj, "iquantity") + "";
                                        cModel.ihgqty = JsonUtils.getJSONInt(itemObj, "ihgqty") + "";
                                        cModel.ingqty = JsonUtils.getJSONInt(itemObj, "ingqty") + "";
                                        cModel.iretqty = JsonUtils.getJSONInt(itemObj, "iretqty") + "";
                                        cModel.itcqty = JsonUtils.getJSONInt(itemObj, "itcqty") + "";
                                        cModel.iscrapqty = JsonUtils.getJSONInt(itemObj, "iscrapqty") + "";
                                        mData.add(cModel);
                                    }

                                    //初始化编辑或保存数据
                                    qcModel.setCheckvoucher(checkvoucher);
                                    qcModel.setCheckvouchers(checkvouchers);

                                    LogUtils.d("WebService", new Gson().toJson(qcModel) + "");
                                } else {
                                    ToastUtils.showToast(mActivity, JsonUtils.getJSONString(json, "msg"));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        onLoad();
                        onListNull();
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        progressBar.dismissProgressBar();
                        onLoad();
                        onListNull();
                        ToastUtils.showToast(mActivity, e.toString());
                    }
                });
    }


    private void onListNull() {

        if(mData.size() == 0){
//            imageView_null.setVisibility(View.VISIBLE);
        }else{
//            imageView_null.setVisibility(View.GONE);
        }
    }

    private void onLoad() {

        mAdapter.notifyDataSetChanged();
//        mListView.onRefreshComplete();
        initDatas();
    }

    /**
     * 保存数据
     */
    private void loadSaveData(String json){

        SimpleArrayMap mapParams = new SimpleArrayMap();
        mapParams.put("json", json);//json数据


        final ProgressBarView progressBar = new ProgressBarView(mActivity);//请求等待对话框
        progressBar.showProgressBar("加载中");
        WebService.call(mActivity, Config.URL_CRUDQC, mapParams,  new WebService.Response() {
                    @Override
                    public void onSuccess(SoapObject result) {
                        progressBar.dismissProgressBar();

                        if( result!=null && result.getPropertyCount() > 0){
                            try {
                                JSONObject json = new JSONObject(result.getProperty(0).toString());
                                int code = JsonUtils.getJSONInt(json, "code");
                                if (code == 1) {
                                    JSONObject resut = json.optJSONObject("reslut");
                                    ToastUtils.showToast(getActivity(), "保存成功！");

                                    MainDetailActivity.mMainDetailActivity.setResult(mActivity.RESULT_OK);
                                    MainDetailActivity.mMainDetailActivity.finish();
                                } else {
                                    ToastUtils.showToast(getActivity(), JsonUtils.getJSONString(json, "msg"));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                ToastUtils.showToast(getActivity(), "数据异常，请稍后再试！");
                            }
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        progressBar.dismissProgressBar();
                        ToastUtils.showToast(getActivity(),e.toString());
                    }
                });
    }

    @Override
    public void putCheData(CRUDQc.CheckvoucherBean cheModel, DetailList.Checkvouchers chesData, int position) {
        qcModel.getCheckvoucher().set(0, cheModel);
        mData.set(position, chesData);
        ToastUtils.showToast(mActivity, "此指标数据已保存！");
    }

    @Override
    public void putChesData(CRUDQc.CheckvouchersBean chesModel, DetailList.Checkvouchers chesData, int position) {
        qcModel.getCheckvouchers().set(position, chesModel);
        mData.set(position, chesData);
        ToastUtils.showToast(mActivity, "此指标数据已保存！");

    }
}
