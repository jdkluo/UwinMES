package com.redlichee.uwinmes.view.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.karics.library.zxing.android.CaptureActivity;
import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.adapter.JobOrderAdapter;
import com.redlichee.uwinmes.application.Config;
import com.redlichee.uwinmes.application.MyApplication;
import com.redlichee.uwinmes.base.BaseActivity;
import com.redlichee.uwinmes.model.BaseData;
import com.redlichee.uwinmes.model.TestModel;
import com.redlichee.uwinmes.utils.DateUtils;
import com.redlichee.uwinmes.utils.JsonUtils;
import com.redlichee.uwinmes.utils.SharedPreUtil;
import com.redlichee.uwinmes.utils.ToastUtils;
import com.redlichee.uwinmes.utils.net.WebService;
import com.redlichee.uwinmes.utils.view.ShowAlertView;
import com.redlichee.uwinmes.widget.DatePickSeleteDialog;
import com.redlichee.uwinmes.widget.ProgressBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private PullToRefreshExpandableListView mListView;
    private ExpandableListView  mExpandableListView;
    private JobOrderAdapter mExpandAdpter;
    private List<TestModel> groupArray = new ArrayList<TestModel>();
    private List<List<TestModel.QcVoucher>> childArray = new ArrayList<List<TestModel.QcVoucher>>();

    private TextView tvLogout;
    private TextView tvUserInfo;
    private TextView tvStartDate;//开始时间
    private TextView tvEndDate;//结束时间
    private LinearLayout llayoutBackTop;//返回顶部
    private RelativeLayout rlayoutZxing;//二维码扫码

    private Spinner spDepartment;//输入部门下拉列表
    private Spinner spProductionLine;//生产线下拉列表
    private Spinner spProcess;//工序下拉列表

    private AutoCompleteTextView tvProcessCode;//输入流程卡号
    private LinearLayout llayout_search;//搜索

    private String strStartDate;//筛选开始日期格式:2016-05-23
    private String strEndDate;//筛选结束日期格式:2016-05-23

    private SharedPreUtil sharedPre;

    private ArrayAdapter<String> mDeptAdapter;//部门适配器
    private ArrayAdapter<String> mPLineAdapter;//生产线适配器
    private ArrayAdapter<String> mProcessAdapter;//工序适配器

    private List<BaseData.DeptBean> mDataDept = new ArrayList<BaseData.DeptBean>();//部门数据
    private List<BaseData.ProdLineBean> mDataPLine = new ArrayList<BaseData.ProdLineBean>();//生产线数据
    private List<BaseData.ProcessBean> mDataProcess = new ArrayList<BaseData.ProcessBean>();//工序数据

    public static final int REQUEST_CODE_SCAN = 0x0000;//二维码扫码回调码

    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        initViews();//初始化组件控件

        initDatas();//初始化数据

        setListeners();//监听

        loadData("", "", "");//获取质检信息列表

        loadBaseData();//获取部门，生产线，标准工序
    }

    private void initViews() {
        tvLogout = (TextView) findViewById(R.id.tv_main_logout) ;
        tvUserInfo = (TextView) findViewById(R.id.tv_user_info) ;
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);// 开始时间
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);// 结束时间
        llayoutBackTop = (LinearLayout) findViewById(R.id.llaout_back_top);// 结束时间
        rlayoutZxing = (RelativeLayout) findViewById(R.id.rlayout_zxing);// 二维码

        spDepartment = (Spinner) findViewById(R.id.sp_department);
        spProductionLine = (Spinner) findViewById(R.id.sp_production_line);
        spProcess = (Spinner) findViewById(R.id.sp_process);

        tvProcessCode = (AutoCompleteTextView) findViewById(R.id.tv_process_code);

        llayout_search = (LinearLayout) findViewById(R.id.llayout_search);

        mListView = (PullToRefreshExpandableListView) findViewById(R.id.expandableListView);
        mExpandAdpter = new JobOrderAdapter(this, groupArray, childArray);
        mExpandableListView = mListView.getRefreshableView();
        mExpandableListView.setAdapter(mExpandAdpter);

        setGroupExpandListener(mExpandableListView);//设置监听,打开一个组的时候，要关闭其他组

    }

    private void setSpinnerAdapter(){

        //适配器
        mDeptAdapter= new ArrayAdapter<String>(this, R.layout.dropdown_spinner_item, getListDept(mDataDept));
        mPLineAdapter= new ArrayAdapter<String>(this, R.layout.dropdown_spinner_item, getListPLine(mDataPLine));
        mProcessAdapter= new ArrayAdapter<String>(this, R.layout.dropdown_spinner_item, getListProcess(mDataProcess));
        //设置样式
        mDeptAdapter.setDropDownViewResource(R.layout.dropdown_style);
        mPLineAdapter.setDropDownViewResource(R.layout.dropdown_style);
        mProcessAdapter.setDropDownViewResource(R.layout.dropdown_style);
        //加载适配器
        spDepartment.setAdapter(mDeptAdapter);
        spProductionLine.setAdapter(mPLineAdapter);
        spProcess.setAdapter(mProcessAdapter);
    }


    /**
     * 打开一个组的时候，要关闭其他组
     * @param mView
     */
    private void setGroupExpandListener(final ExpandableListView  mView){

        mView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = mView.getExpandableListAdapter().getGroupCount();
                for(int j = 0; j < count; j++){
                    if(j != groupPosition){
                        mView.collapseGroup(j);
                    }
                }
            }
        });
    }

    private void setListeners() {
        //注销登录
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPopupWindows mPopupWindows = new UserPopupWindows(MainActivity.this);
//                mPopupWindows.showLocation(R.id.tv_title_iconfont1);// 设置弹出菜单弹出的位置
            }
        });

        //下拉刷新
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {

                String Dep = spDepartment.getSelectedItem().toString();//输入部门下拉列表
                String PLine = spProductionLine.getSelectedItem().toString();//生产线下拉列表
                String Process = spProcess.getSelectedItem().toString();//工序下拉列表

                loadData(Dep, PLine, Process);
            }
        });

        //搜索
        llayout_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Dep = spDepartment.getSelectedItem().toString();//输入部门下拉列表
                String PLine = spProductionLine.getSelectedItem().toString();//生产线下拉列表
                String Process = spProcess.getSelectedItem().toString();//工序下拉列表

                loadData(Dep, PLine, Process);
            }
        });
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        //返回顶部
        llayoutBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandableListView.smoothScrollToPosition(0);
            }
        });
        //二维码
        rlayoutZxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
    }

    private void initDatas() {
        sharedPre =  new SharedPreUtil(mContext);
        tvUserInfo.setText(sharedPre.get(Config.USER_NAME) + " - " + sharedPre.get(Config.DEPARTMENT));

        strStartDate = DateUtils.getCurDate("yyyy.MM.dd");
        strEndDate = DateUtils.getCurDate("yyyy.MM.dd");

        tvStartDate.setText(strStartDate);//开始时间
        tvEndDate.setText(strEndDate);//结束时间
    }
    /**
     * 获取部门，生产线，标准工序
     */
    private void loadBaseData(){

        SimpleArrayMap mapParams = new SimpleArrayMap();

        WebService.call(MainActivity.this, Config.URL_GET_BASEDATA, mapParams,  new WebService.Response() {
                    @Override
                    public void onSuccess(SoapObject result) {

                        if( result!=null || result.getPropertyCount() > 0){
                            try {
                                JSONObject json = new JSONObject(result.getProperty(0).toString());
                                int code = JsonUtils.getJSONInt(json, "code");
                                if (code == 1) {

                                    JSONObject resut = json.optJSONObject("reslut");
                                    //部门
                                    JSONArray arrayObj = JsonUtils.getSafeJsonArray(resut, "Dept");
                                    BaseData.DeptBean model ;
                                    for (int i = 0; i < arrayObj.length(); i++) {
                                        model = new BaseData.DeptBean();
                                        JSONObject itemObj = arrayObj.getJSONObject(i);
                                        model.setDept_Id(JsonUtils.getJSONInt(itemObj, "Dept_Id"));
                                        model.setDept_Number(JsonUtils.getJSONString(itemObj, "Dept_Number"));
                                        model.setDept_Name(JsonUtils.getJSONString(itemObj, "Dept_Name"));

                                        mDataDept.add(model);
                                    }

                                    //生产线
                                    JSONArray qlObj = JsonUtils.getSafeJsonArray(resut, "ProdLine");
                                    BaseData.ProdLineBean qlModel ;
                                    for (int j = 0; j < qlObj.length(); j++) {
                                        qlModel = new BaseData.ProdLineBean();
                                        JSONObject qlItemObj = qlObj.getJSONObject(j);
                                        qlModel.setPlId(JsonUtils.getJSONInt(qlItemObj, "PlId"));
                                        qlModel.setPlCode(JsonUtils.getJSONString(qlItemObj, "PlCode"));
                                        qlModel.setDescription(JsonUtils.getJSONString(qlItemObj, "Description"));
                                        qlModel.setDept_Number(JsonUtils.getJSONString(qlItemObj, "Dept_Number"));
                                        qlModel.setDept_Name(JsonUtils.getJSONString(qlItemObj, "Dept_Name"));
                                        qlModel.setWcCode(JsonUtils.getJSONString(qlItemObj, "WcCode"));
                                        qlModel.setWcName(JsonUtils.getJSONString(qlItemObj, "WcName"));

                                        mDataPLine.add(qlModel);
                                    }

                                    //工序
                                    JSONArray pObj = JsonUtils.getSafeJsonArray(resut, "Process");
                                    BaseData.ProcessBean pModel ;
                                    for (int z = 0; z < pObj.length(); z++) {
                                        pModel = new BaseData.ProcessBean();
                                        JSONObject pItemObj = pObj.getJSONObject(z);
                                        pModel.setOperationId(JsonUtils.getJSONInt(pItemObj, "PlId"));
                                        pModel.setOpCode(JsonUtils.getJSONString(pItemObj, "OpCode"));
                                        pModel.setDescription(JsonUtils.getJSONString(pItemObj, "Description"));
                                        pModel.setWcId(JsonUtils.getJSONInt(pItemObj, "WcId"));

                                        mDataProcess.add(pModel);
                                    }

                                } else {
                                    showToast(JsonUtils.getJSONString(json, "msg"));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setSpinnerAdapter();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        setSpinnerAdapter();
                        showToast(e.toString());
                    }
                });
    }

    /**
     * 获取待检验质检单
     */
    private void loadData(String Dep, String PLine, String Process){

        SimpleArrayMap mapParams = new SimpleArrayMap();

        mapParams.put("StDay", DateUtils.StrFormat(strStartDate,"yyyy-MM-dd", "yyyy.MM.dd"));//开始日期
        mapParams.put("EdDay", DateUtils.StrFormat(strEndDate,"yyyy-MM-dd", "yyyy.MM.dd"));//结束日期
        mapParams.put("DeptCode", "部门".equals(Dep) || Dep == null ? "" : Dep);//部门编号
        mapParams.put("ProductLineCode", "生产线".equals(PLine) || Dep == null  ? "" : PLine);//生产线编码
        mapParams.put("OpCode", "工序".equals(Process) || Dep == null  ? "" : Process);//工序代码
        mapParams.put("FlowCode", tvProcessCode.getText().toString());//流程卡号

        final ProgressBarView progressBar = new ProgressBarView(MainActivity.this);
        progressBar.showProgressBar("加载中");
        WebService.call(MainActivity.this, Config.URL_PROCESS_FLOW, mapParams,  new WebService.Response() {
                    @Override
                    public void onSuccess(SoapObject result) {
                        progressBar.dismissProgressBar();

                        childArray.clear();
                        groupArray.clear();

                        if( result!=null || result.getPropertyCount() > 0){
                            try {
                                JSONObject json = new JSONObject(result.getProperty(0).toString());
                                int code = JsonUtils.getJSONInt(json, "code");
                                if (code == 1) {

                                    JSONObject resut = json.optJSONObject("reslut");
                                    JSONArray arrayObj = JsonUtils.getSafeJsonArray(resut, "ProcessFlow");
                                    TestModel model ;
                                    for (int i = 0; i < arrayObj.length(); i++) {
                                        model = new TestModel();
                                        JSONObject itemObj = arrayObj.getJSONObject(i);
                                        model.Id = JsonUtils.getJSONString(itemObj, "Id");
                                        model.AutoId = JsonUtils.getJSONString(itemObj, "AutoId");
                                        model.cCode = JsonUtils.getJSONString(itemObj, "cCode");
                                        model.InvCode = JsonUtils.getJSONString(itemObj, "InvCode");
                                        model.cInvName = JsonUtils.getJSONString(itemObj, "cInvName");
                                        model.cInvStd = JsonUtils.getJSONString(itemObj, "cInvStd");
                                        model.opCode = JsonUtils.getJSONString(itemObj, "opCode");
                                        model.Description = JsonUtils.getJSONString(itemObj, "Description");
                                        model.CompleteQty = JsonUtils.getJSONString(itemObj, "CompleteQty");
                                        model.cComUnitCode = JsonUtils.getJSONString(itemObj, "cComUnitCode");
                                        model.MDeptCode = JsonUtils.getJSONString(itemObj, "MDeptCode");
                                        model.Dept_Name = JsonUtils.getJSONString(itemObj, "Dept_Name");
                                        model.PLName = JsonUtils.getJSONString(itemObj, "PLName");

                                        JSONArray qcObj = JsonUtils.getSafeJsonArray(itemObj, "QcVoucher");
                                        List<TestModel.QcVoucher> array = new ArrayList<TestModel.QcVoucher>();
                                        TestModel.QcVoucher qcModel ;
                                        for (int j = 0; j < qcObj.length(); j++) {
                                            qcModel = new TestModel() .new QcVoucher();
                                            JSONObject qcItemObj = qcObj.getJSONObject(j);
                                            qcModel.id = JsonUtils.getJSONString(qcItemObj, "id");
                                            qcModel.ccode = JsonUtils.getJSONString(qcItemObj, "ccode");
                                            qcModel.ddate = JsonUtils.getJSONString(qcItemObj, "ddate");
                                            qcModel.dtime = JsonUtils.getJSONString(qcItemObj, "dtime");
                                            qcModel.flowid = JsonUtils.getJSONString(qcItemObj, "flowid");
                                            qcModel.flowautoid = JsonUtils.getJSONString(qcItemObj, "flowautoid");
                                            qcModel.cqcpersoncode = JsonUtils.getJSONString(qcItemObj, "cqcpersoncode");
                                            qcModel.cqctype = JsonUtils.getJSONString(qcItemObj, "cqctype");
                                            qcModel.cqctypeTXT = JsonUtils.getJSONString(qcItemObj, "cqctypeTXT");
                                            qcModel.iquantity = JsonUtils.getJSONString(qcItemObj, "iquantity");
                                            qcModel.ihgqty = JsonUtils.getJSONString(qcItemObj, "ihgqty");
                                            qcModel.ingqty = JsonUtils.getJSONString(qcItemObj, "ingqty");
                                            qcModel.iretqty = JsonUtils.getJSONString(qcItemObj, "iretqty");
                                            qcModel.itcqty = JsonUtils.getJSONString(qcItemObj, "itcqty");
                                            qcModel.iscrapqty = JsonUtils.getJSONString(qcItemObj, "iscrapqty");
                                            qcModel.cQsName = JsonUtils.getJSONString(qcItemObj, "cQsName");
                                            qcModel.solutionid = JsonUtils.getJSONString(qcItemObj, "solutionid");
                                            qcModel.cmaker = JsonUtils.getJSONString(qcItemObj, "cmaker");
                                            qcModel.cmakerName = JsonUtils.getJSONString(qcItemObj, "cmakerName");
                                            qcModel.dmaketime = JsonUtils.getJSONString(qcItemObj, "dmaketime");
                                            qcModel.cmodifier = JsonUtils.getJSONString(qcItemObj, "cmodifier");
                                            qcModel.dmodifytime = JsonUtils.getJSONString(qcItemObj, "dmodifytime");
                                            qcModel.cStatus = JsonUtils.getJSONString(qcItemObj, "cStatus");
                                            array.add(qcModel);
                                        }
                                        childArray.add(array);
                                        groupArray.add(model);

                                    }

                                } else {
                                    showToast(JsonUtils.getJSONString(json, "msg"));

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
                        showToast(e.toString());
                    }
                });
    }


    private void onListNull() {

        if(groupArray.size() == 0){
//            imageView_null.setVisibility(View.VISIBLE);
        }else{
//            imageView_null.setVisibility(View.GONE);
        }
    }

    private void onLoad() {

        mExpandAdpter.notifyDataSetChanged();
        mListView.onRefreshComplete();
    }


    /**
     * 选择开始时间
     */
    public void showDialogStartDate() {
        DatePickSeleteDialog startdatePickDialogUtil = new DatePickSeleteDialog(MainActivity.this,
                DateUtils.StrFormat(strStartDate,"yyyy年MM月dd日","yyyy.MM.dd"), new DatePickSeleteDialog.DatepickClickListener() {

            @Override
            public void yesClick(String comStr) {
                strStartDate = comStr;
            }

            @Override
            public void noClick() {
                // 错误了
                ShowAlertView.showDialog(MainActivity.this, "时间格式有误！");
            }
        });
        startdatePickDialogUtil.dateTimePicKTextViewDialog(tvStartDate, "yyyy.MM.dd");
    }

    /**
     * 选择结束时间
     */
    public void showDialogEndDate() {
        DatePickSeleteDialog startdatePickDialogUtil = new DatePickSeleteDialog(MainActivity.this,
                DateUtils.StrFormat(strEndDate,"yyyy年MM月dd日","yyyy.MM.dd"), new DatePickSeleteDialog.DatepickClickListener() {

            @Override
            public void yesClick(String comStr) {
                strEndDate = comStr;
            }

            @Override
            public void noClick() {
                // 错误了
                ShowAlertView.showDialog(MainActivity.this, "时间格式有误！");
            }
        });
        startdatePickDialogUtil.dateTimePicKTextViewDialog(tvEndDate, "yyyy.MM.dd");
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), getString(R.string.text_again_press_exit), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                MyApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start_date:
                //开始时间
                showDialogStartDate();
                break;
            case R.id.tv_end_date:
                //结束时间
                showDialogEndDate();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 101:
                if(resultCode == RESULT_OK){
                    if(spDepartment.getSelectedItem() != null && spProductionLine.getSelectedItem() != null && spProcess.getSelectedItem() != null){
                        String Dep = spDepartment.getSelectedItem().toString();//输入部门下拉列表
                        String PLine = spProductionLine.getSelectedItem().toString();//生产线下拉列表
                        String Process = spProcess.getSelectedItem().toString();//工序下拉列表

                        loadData(Dep, PLine, Process);
                    }
                }
                break;
            case REQUEST_CODE_SCAN: // 扫描二维码/条码回传
                if(resultCode == RESULT_OK){
                    if (data != null) {

                        String content = data.getStringExtra(DECODED_CONTENT_KEY);
                        Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                        tvProcessCode.setText(content != null ? content : "");
//                        qrCodeImage.setImageBitmap(bitmap);
                    }
                }
                break;
        }
    }

    //获取部门名称列表
    private List<String> getListDept(List<BaseData.DeptBean> mDataDept){
        List<String> data = new ArrayList<String>();
        data.add("部门");
        for (int i = 0 ; i < mDataDept.size(); i++) {
            data.add(mDataDept.get(i).getDept_Name());
        }
        return data;
    }

    //获取生产线名称列表
    private List<String> getListPLine(List<BaseData.ProdLineBean> mDataPLine){
        List<String> data = new ArrayList<String>();
        data.add("生产线");
        for (int i = 0 ; i < mDataPLine.size(); i++) {
            data.add(mDataPLine.get(i).getDescription());
        }
        return data;
    }

    //获取工序名称列表
    private List<String> getListProcess(List<BaseData.ProcessBean> mDataProcess){
        List<String> data = new ArrayList<String>();
        data.add("工序");
        for (int i = 0 ; i < mDataProcess.size(); i++) {
            data.add(mDataProcess.get(i).getDescription());
        }
        return data;
    }

    /**
     * 弹出PopupView用户信息
     * @author
     *
     */
    public class UserPopupWindows extends PopupWindow {

        public UserPopupWindows(final Context mContext) {

            View view = View.inflate(mContext, R.layout.popup_menu,null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));

//            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
//            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));

            this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
            this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            setBackgroundDrawable(new BitmapDrawable());
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            this.setFocusable(true);// 获取焦点
            this.setTouchable(true); // 设置PopupWindow可触摸
            this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
//            ColorDrawable dw = new ColorDrawable(0x00000000);
//            this.setBackgroundDrawable(dw);
            setContentView(view);
            update();

            final TextView tv_name = (TextView) view.findViewById(R.id.tv_name);// 请现在体验身份
            final TextView tv_dept = (TextView) view.findViewById(R.id.tv_dept);// 请现在体验身份
            final TextView tv_user_code = (TextView) view.findViewById(R.id.tv_user_code);// 请现在体验身份
            final TextView tv_logout = (TextView) view.findViewById(R.id.tv_logout);// 请现在体验身份

            tv_name.setText(sharedPre.get(Config.USER_NAME));
            tv_dept.setText(sharedPre.get(Config.DEPARTMENT));
            tv_user_code.setText(sharedPre.get(Config.EMPLOYEE_NUM));

            tv_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 注销登录
                    ShowAlertView.showOkAndNegative(MainActivity.this, "确定注销登录？", new ShowAlertView.ClickCallback() {
                        @Override
                        public void clickOk() {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
                    dismiss();

                }
            });

        }
    }
}
