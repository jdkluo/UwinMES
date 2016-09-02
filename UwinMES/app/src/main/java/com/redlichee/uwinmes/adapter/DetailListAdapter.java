package com.redlichee.uwinmes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.model.DetailList;

import java.util.ArrayList;

/**
 * Created by LMW on 2016/5/7.
 */
public class DetailListAdapter extends BaseAdapter {

    private ArrayList<DetailList.Checkvouchers> data;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int selectItem = -1;

    public DetailListAdapter(Context context, ArrayList<DetailList.Checkvouchers> arr) {
        this.data = arr;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_detail_list, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tv_item_category);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_name);
            holder.tvUnits = (TextView) convertView.findViewById(R.id.tv_item_units);
            holder.ivDuigou = (ImageView) convertView.findViewById(R.id.iv_item_duigou);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        DetailList.Checkvouchers model = data.get(position);

        //分类 表头或表体
        if (position > 0) {
            if (model.category == data.get(position - 1).category) {
                holder.tvCategory.setVisibility(View.GONE);
            } else {
                holder.tvCategory.setVisibility(View.VISIBLE);
                holder.tvCategory.setText(model.category == 1 ? "Part.2整单统计" : "Part.1指标录入");
            }
        } else if (model.category == 1 || model.category == 2 ) {
            holder.tvCategory.setVisibility(View.VISIBLE);
            holder.tvCategory.setText(model.category == 1 ? "Part.2整单统计" : "Part.1指标录入");
        } else {
            holder.tvCategory.setVisibility(View.GONE);
        }

        if(model.category == 2){//表体才执行
            //标题
            if (position > 0) {
                if (model.cQiName.equals(data.get(position - 1).cQiName)) {
                    holder.tvTitle.setVisibility(View.GONE);
                } else {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvTitle.setText(model.cQiName);
                }
            } else if (!TextUtils.isEmpty(model.cQiName)) {
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.tvTitle.setText(model.cQiName);
            } else {
                holder.tvTitle.setVisibility(View.GONE);
            }
        }
        if(model.iguidetype == 1 || model.iguidetype == 2){
            //单位
            holder.tvUnits.setText(model.iguidetype == 1 ? "计数" : "计量");
        } else {
            holder.tvUnits.setText("");
        }


        //名称
        holder.tvName.setText(model.cQqName);

        //对勾
        if (model.duigou == 1) {
            holder.ivDuigou.setVisibility(View.VISIBLE);
        } else {
            holder.ivDuigou.setVisibility(View.GONE);
        }
        //设置选中颜色
        if (position == selectItem) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.text_00668C));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    class ViewHolder {
        TextView tvCategory;//分类 表头或表体
        TextView tvTitle;//分类标题
        TextView tvName;//名称
        TextView tvUnits;//单位
        ImageView ivDuigou;//对勾图标

    }
}
