package com.redlichee.uwinmes.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.model.CRUDQc;

import java.util.ArrayList;
import java.util.List;

public class DetectValueAdapter extends BaseAdapter {
    private List<CRUDQc.CheckvouchersBean.DetectValueBean> mData ;
    private Context mContext;

    public DetectValueAdapter(Context mContext, List<CRUDQc.CheckvouchersBean.DetectValueBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public int getCount() {
        return this.mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View view, ViewGroup arg2) {

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_detect_value, null);
            viewHolder.tvSampleNum = (TextView) view.findViewById(R.id.tv_position);
            viewHolder.edtSampleVal = (EditText) view.findViewById(R.id.edt_value);
            viewHolder.tv_delete = (TextView) view.findViewById(R.id.tv_delete);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        CRUDQc.CheckvouchersBean.DetectValueBean mode = mData.get(position);
        //监听检测值变化
//        setmOnTextChanged(viewHolder.edtSampleVal, viewHolder.tv_delete);
        //设置删除监听
        setOnClickListener(viewHolder.tv_delete, position);

        viewHolder.tvSampleNum.setText(mode.getSampleNum() + "");
        viewHolder.edtSampleVal.setText(mode.getSampleVal());

        return view;

    }

    /**
     * 删除监听
     * @param tv_delete
     * @param position
     */
    private void setOnClickListener(TextView tv_delete, final int position){
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 监听
     * @param edt 监听的控件
     * @param tvdelete
     */
    private void setmOnTextChanged(EditText edt,final TextView tvdelete){
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())){
                    tvdelete.setVisibility(View.INVISIBLE);
                } else {
                    tvdelete.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    class ViewHolder {
        TextView tvSampleNum;//样本号
        EditText edtSampleVal;//检验值
        TextView tv_delete;//删行
    }

}