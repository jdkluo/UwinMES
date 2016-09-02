package com.redlichee.uwinmes.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.model.DetailList;
import com.redlichee.uwinmes.model.Solution;

import java.util.ArrayList;

/**
 * Created by LMW on 2016/5/7.
 */
public class SolutionListAdapter extends BaseAdapter {

    private ArrayList<Solution> data;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public SolutionListAdapter(Context context, ArrayList<Solution> arr) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_solution_list, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_title);


            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        Solution model = data.get(position);

        //标题
        holder.tvTitle.setText(model.cQsName);


        return convertView;
    }


    class ViewHolder {
        TextView tvTitle;//标题


    }
}
