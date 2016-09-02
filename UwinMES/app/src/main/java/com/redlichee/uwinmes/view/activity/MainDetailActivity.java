package com.redlichee.uwinmes.view.activity;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.base.BaseActivity;
import com.redlichee.uwinmes.view.fragment.DetailListFragment;

/**
 * Created by LMW on 2016/7/28.
 */
public class MainDetailActivity extends BaseActivity{

    private LinearLayout llayoutBack;//返回按钮
    private TextView tvTitle;

    private String mTitle;

    public int qcType; //类型 1：创建质检单 2：编辑质检单
    public int FlowAutoId; //流程卡明细ID
    public int Id; //存货对照ID
    public String id = ""; //质检单主键ID
    public static MainDetailActivity mMainDetailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_detail);

        mMainDetailActivity = this;

        qcType = getIntent().getExtras().getInt("qcType", 1);
        if(qcType == 1){
            FlowAutoId = getIntent().getExtras().getInt("FlowAutoId");
            Id = getIntent().getExtras().getInt("Id");
            mTitle = getIntent().getExtras().getString("title", "") + "质检信息填写";

        } else {
            id = getIntent().getExtras().getString("id", "");
            mTitle = getIntent().getExtras().getString("title", "") + "质检信息编辑";
        }

        initViews();

        initDatas();

        setListeners();

        showLog("onCreate:" + id);
    }

    private void initViews() {
        llayoutBack = (LinearLayout) findViewById(R.id.llayout_title_back);
        tvTitle = (TextView) findViewById(R.id.tv_edtail_title);

        tvTitle.setText(mTitle);
    }
    private void initDatas() {
        Fragment fragment = new DetailListFragment().newInstance(qcType, FlowAutoId, Id, id);
        getFragmentManager().beginTransaction().replace(R.id.left_fragment, fragment).commit();

    }
    private void setListeners() {
        llayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();;
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        showLog("onConfigurationChanged:" + id);
    }
}
