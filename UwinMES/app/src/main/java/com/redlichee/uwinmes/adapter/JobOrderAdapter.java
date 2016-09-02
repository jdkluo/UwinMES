package com.redlichee.uwinmes.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.application.Config;
import com.redlichee.uwinmes.model.Solution;
import com.redlichee.uwinmes.model.TestModel;
import com.redlichee.uwinmes.utils.JsonUtils;
import com.redlichee.uwinmes.utils.SharedPreUtil;
import com.redlichee.uwinmes.utils.ToastUtils;
import com.redlichee.uwinmes.utils.net.WebService;
import com.redlichee.uwinmes.utils.view.ShowAlertView;
import com.redlichee.uwinmes.view.activity.MainDetailActivity;
import com.redlichee.uwinmes.widget.ProgressBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务列表适配器
 */
public class JobOrderAdapter extends BaseExpandableListAdapter{

	private LayoutInflater mLayoutInflater;
	protected Activity mActivity;
	private List<TestModel> groupArray;
	private List<List<TestModel.QcVoucher>> childArray;
	private String UserId ;

	public JobOrderAdapter(Activity activity, List<TestModel> groupArray, List<List<TestModel.QcVoucher>> childArray){
		this.mActivity = activity;
		this.groupArray = groupArray;
		this.childArray = childArray;
		mLayoutInflater = LayoutInflater.from(activity);
		UserId = new SharedPreUtil(activity).get(Config.PERSON_ID);
	}

	public JobOrderAdapter(Activity activity){
		this.mActivity = activity;
		mLayoutInflater = LayoutInflater.from(activity);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childArray.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
//
//	@Override
//	public int getChildType(int groupPosition, int childPosition) {
//		if (childPosition == (-1 + getChildrenCount(groupPosition))) {
//			return 1;
//		}else {
//			return 0;
//		}
//	}
//
//	@Override
//	public int getChildTypeCount() {
//		return 2;
//	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childArray.get(groupPosition).size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder childViewHolder;
		if (convertView == null) {
			childViewHolder = new ChildViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.his_group_item_detail, null);
			childViewHolder.tv_child_table = (TextView)convertView.findViewById(R.id.tv_child_table);
			childViewHolder.tv_child_value = (TextView)convertView.findViewById(R.id.tv_child_value);
			childViewHolder.tv_child_qsname = (TextView)convertView.findViewById(R.id.tv_child_qsname);
			childViewHolder.tv_child_name = (TextView)convertView.findViewById(R.id.tv_child_name);
			childViewHolder.tv_child_delete = (TextView)convertView.findViewById(R.id.tv_child_delete);
			childViewHolder.tv_child_edit = (TextView)convertView.findViewById(R.id.tv_child_edit);
			childViewHolder.tv_detail_halving = (TextView)convertView.findViewById(R.id.tv_detail_halving);
			childViewHolder.tv_detail_halving_bottom = (TextView)convertView.findViewById(R.id.tv_detail_halving_bottom);
			childViewHolder.tv_detail_halving_top = (TextView)convertView.findViewById(R.id.tv_detail_halving_top);
			convertView.setTag(childViewHolder);
		}else{
			childViewHolder = (ChildViewHolder)convertView.getTag();
		}

		childViewHolder.tv_child_table.setText(childArray.get(groupPosition).get(childPosition).cqctypeTXT);
		childViewHolder.tv_child_value.setText(childArray.get(groupPosition).get(childPosition).ddate);
		childViewHolder.tv_child_qsname.setText(childArray.get(groupPosition).get(childPosition).cQsName);
		childViewHolder.tv_child_name.setText(childArray.get(groupPosition).get(childPosition).cmakerName);

		setDeleteOnClickListener(childViewHolder.tv_child_delete, childArray.get(groupPosition).get(childPosition).id, groupPosition, childPosition);//删除监听
		setEditOnClickListener(childViewHolder.tv_child_edit, childArray.get(groupPosition).get(childPosition).id, childArray.get(groupPosition).get(childPosition).cQsName);//编辑监听

		//判断该用户是否是创建人
		if(childArray.get(groupPosition).get(childPosition).cmaker.equals(UserId)){
			childViewHolder.tv_child_delete.setTextColor(mActivity.getResources().getColor(R.color.color_red));
			childViewHolder.tv_child_delete.setClickable(true);
		} else {
			childViewHolder.tv_child_delete.setTextColor(mActivity.getResources().getColor(R.color.text_hint));
			childViewHolder.tv_child_delete.setClickable(false);
		}

		if(childArray.get(groupPosition).size() > 0 && 0 == childPosition){//判断是否是最后一条
			childViewHolder.tv_detail_halving_top.setVisibility(View.VISIBLE);
		} else {
			childViewHolder.tv_detail_halving_top.setVisibility(View.GONE);
		}
		if(childArray.get(groupPosition).size() > 0 && childArray.get(groupPosition).size() - 1 == childPosition){//判断是否是最后一条
			childViewHolder.tv_detail_halving_bottom.setVisibility(View.VISIBLE);
			childViewHolder.tv_detail_halving.setVisibility(View.GONE);
		} else {
			childViewHolder.tv_detail_halving_bottom.setVisibility(View.GONE);
			childViewHolder.tv_detail_halving.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	/**
	 * 删除监听
	 * @param tv
     */
	private void setDeleteOnClickListener(TextView tv, final String id, final int groupPosition, final int childPosition){
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowAlertView.showOkAndNegative(mActivity, "是否确认删除此项？", new ShowAlertView.ClickCallback() {
					@Override
					public void clickOk() {
						loadDelete(id, groupPosition, childPosition);
					}
				});
			}
		});
	}

	/**
	 * 删除质检单
	 * @param id		质检单主表id
	 */
	private void loadDelete(String id, final int groupPosition, final int childPosition){

		SimpleArrayMap mapParams = new SimpleArrayMap();
		mapParams.put("id", id);//流程卡明细ID

		final ProgressBarView progressBar = new ProgressBarView(mActivity);//请求等待对话框
		progressBar.showProgressBar("加载中");
		WebService.call(mActivity, Config.URL_DELETE_VOCUCHER, mapParams, new WebService.Response() {
					@Override
					public void onSuccess(SoapObject result) {
						progressBar.dismissProgressBar();

						try {
							JSONObject json = new JSONObject(result.getProperty(0).toString());
							int code = JsonUtils.getJSONInt(json, "code");
							if (code == 1) {
								ToastUtils.showToast(mActivity, "删除成功");
								childArray.get(groupPosition).remove(childPosition);
								notifyDataSetChanged();
							} else {
								ToastUtils.showToast(mActivity, "删除失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
							ToastUtils.showToast(mActivity, "返回数据异常");
						}
					}

					@Override
					public void onError(Exception e) {
						e.printStackTrace();
						progressBar.dismissProgressBar();
						ToastUtils.showToast(mActivity, "返回数据异常");
					}
				});
	}

	/**
	 * 编辑监听
	 * @param tv
	 */
	private void setEditOnClickListener(TextView tv, final String id, final String cQsName){
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, MainDetailActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("qcType", 2);
				intent.putExtra("title", cQsName);
				mActivity.startActivityForResult(intent, 101);
			}
		});
	}
	class ChildViewHolder{
		TextView tv_child_table;
		TextView tv_child_value;
		TextView tv_child_qsname;//方案名称
		TextView tv_child_name;//姓名
		TextView tv_child_delete;//删除
		TextView tv_child_edit;//编辑
		TextView tv_detail_halving_top;//分割线上
		TextView tv_detail_halving_bottom;//分割线下
		TextView tv_detail_halving;//分割线
	}


	@Override
	public Object getGroup(int groupPosition) {
		return groupArray.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if (groupArray != null) {
			return groupArray.size();
		}else {
			return 0;
		}
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupViewHolder groupViewHolder;
		if (convertView == null) {
			groupViewHolder = new GroupViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.his_group_item, null);
			groupViewHolder.llayout_group_table = (LinearLayout)convertView.findViewById(R.id.llayout_group_table);
			groupViewHolder.tv_group_title = (TextView)convertView.findViewById(R.id.tv_group_title);
			groupViewHolder.iv_group_indicator = (ImageView)convertView.findViewById(R.id.iv_group_indicator);
			groupViewHolder.tv_group_table = (TextView)convertView.findViewById(R.id.tv_group_department);
			groupViewHolder.tv_group_production_line = (TextView)convertView.findViewById(R.id.tv_group_production_line);
			groupViewHolder.tv_group_value = (TextView)convertView.findViewById(R.id.tv_group_serial_number);
			groupViewHolder.tv_group_inventory_coding = (TextView)convertView.findViewById(R.id.tv_group_inventory_coding);
			groupViewHolder.tv_group_inventory_name = (TextView)convertView.findViewById(R.id.tv_group_inventory_name);
			groupViewHolder.tv_group_specifications = (TextView)convertView.findViewById(R.id.tv_group_specifications);
			groupViewHolder.tv_group_process = (TextView)convertView.findViewById(R.id.tv_group_process);
			groupViewHolder.tv_group_quantity = (TextView)convertView.findViewById(R.id.tv_group_quantity);
			groupViewHolder.tv_group_units = (TextView)convertView.findViewById(R.id.tv_group_units);
			groupViewHolder.tv_group_entering = (TextView)convertView.findViewById(R.id.tv_group_entering);
			convertView.setTag(groupViewHolder);
		}else{
			groupViewHolder = (GroupViewHolder)convertView.getTag();
		}

		if("type_title".equals(groupArray.get(groupPosition).Dept_Name)){//是否是标题
			groupViewHolder.llayout_group_table.setVisibility(View.GONE);//Table和合计
			groupViewHolder.tv_group_title.setVisibility(View.VISIBLE);//栏目标题

			groupViewHolder.tv_group_title.setText(groupArray.get(groupPosition).Dept_Name);
		} else {
			groupViewHolder.llayout_group_table.setVisibility(View.VISIBLE);//Table和合计
			groupViewHolder.tv_group_title.setVisibility(View.GONE);//栏目标题

			groupViewHolder.tv_group_table.setText(groupArray.get(groupPosition).Dept_Name);
			groupViewHolder.tv_group_production_line.setText(groupArray.get(groupPosition).PLName);
			groupViewHolder.tv_group_value.setText(groupArray.get(groupPosition).cCode);
			groupViewHolder.tv_group_inventory_coding.setText(groupArray.get(groupPosition).InvCode);
			groupViewHolder.tv_group_inventory_name.setText(groupArray.get(groupPosition).cInvName);
			groupViewHolder.tv_group_specifications.setText(groupArray.get(groupPosition).cInvStd);
			groupViewHolder.tv_group_process.setText(groupArray.get(groupPosition).Description);
			groupViewHolder.tv_group_quantity.setText(groupArray.get(groupPosition).CompleteQty);
			groupViewHolder.tv_group_units.setText(groupArray.get(groupPosition).cComUnitCode);
		}

		if (isExpanded) {//是否为展开
			groupViewHolder.iv_group_indicator.setImageResource(R.drawable.ic_me_arrow_bottom);
		}else {
			groupViewHolder.iv_group_indicator.setImageResource(R.drawable.ic_me_arrow);
		}

		setEnteringOnClickListener(groupViewHolder.tv_group_entering, groupArray.get(groupPosition).AutoId);//录入监听

		return convertView;
	}

	/**
	 * 录入监听
	 * @param tv
	 */
	private void setEnteringOnClickListener(TextView tv, final String FlowAutoId){
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getSolutionData(FlowAutoId);
			}
		});
	}

	/**
	 * 获取方案列表
	 * @param FlowAutoId
     */
	private void getSolutionData(String FlowAutoId){

		SimpleArrayMap mapParams = new SimpleArrayMap();
		mapParams.put("FlowAutoId", FlowAutoId);//流程卡明细ID
		mapParams.put("Customer", "");//客户


		final ProgressBarView progressBar = new ProgressBarView(mActivity);//请求等待对话框
		progressBar.showProgressBar("加载中");
		WebService.call(mActivity, Config.URL_SOLUTION_LIST, mapParams, new WebService.Response() {
					@Override
					public void onSuccess(SoapObject result) {
						progressBar.dismissProgressBar();

						try {
							JSONObject json = new JSONObject(result.getProperty(0).toString());
							int code = JsonUtils.getJSONInt(json, "code");
							if (code == 1) {
								ArrayList<Solution> mData = new ArrayList<Solution>();
								JSONArray arrayObj = JsonUtils.getSafeJsonArray(json, "reslut");
								Solution model ;
								for (int i = 0; i < arrayObj.length(); i++) {

									model = new Solution();
									JSONObject itemObj = arrayObj.getJSONObject(i);
									model.Id = JsonUtils.getJSONInt(itemObj, "Id");
									model.cQsCode = JsonUtils.getJSONString(itemObj, "cQsCode");
									model.cQsName = JsonUtils.getJSONString(itemObj, "cQsName");
									model.QsTypeTXT = JsonUtils.getJSONString(itemObj, "QsTypeTXT");
									model.SolutionId = JsonUtils.getJSONInt(itemObj, "SolutionId");
									model.cQsType = JsonUtils.getJSONInt(itemObj, "cQsType");
									model.FlowAutoId = JsonUtils.getJSONInt(itemObj, "FlowAutoId");

									mData.add(model);
								}

								if(mData.size() > 0){
									final ArrayList<Solution> data = mData;
									ShowAlertView.showRadiolListDialog(mActivity, "请选择检验方案", mData, new ShowAlertView.OnListClickCallback() {
										@Override
										public void clickList(int position) {
											Intent intent = new Intent(mActivity, MainDetailActivity.class);
											intent.putExtra("Id", data.get(position).Id);
											intent.putExtra("FlowAutoId", data.get(position).FlowAutoId);
											intent.putExtra("qcType", 1);
											intent.putExtra("title", data.get(position).cQsName);
											mActivity.startActivity(intent);
										}
									});
								} else {
									ToastUtils.showToast(mActivity, "暂无方案");
								}


							} else {
                                ToastUtils.showToast(mActivity, "获取方案失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
							ToastUtils.showToast(mActivity, "获取方案失败");
						}
					}

					@Override
					public void onError(Exception e) {
						e.printStackTrace();
						progressBar.dismissProgressBar();
						ToastUtils.showToast(mActivity, "获取方案失败");
					}
				});
	}

	class GroupViewHolder{
		LinearLayout llayout_group_table;//Table和合计View
		TextView tv_group_title;//Item项目标题
		ImageView iv_group_indicator;//Item项目标题
		TextView tv_group_table;//部门
		TextView tv_group_production_line;//生产线
		TextView tv_group_value;//流水号
		TextView tv_group_inventory_coding;//存货编码
		TextView tv_group_inventory_name;//存货名称
		TextView tv_group_specifications;//规格型号
		TextView tv_group_process;//工序
		TextView tv_group_quantity;//报检数量
		TextView tv_group_units;//单位
		TextView tv_group_entering;//录入
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}